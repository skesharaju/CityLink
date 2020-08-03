package com.codechallenge.citylink.service;

import com.codechallenge.citylink.exception.InvalidCityException;
import com.codechallenge.citylink.model.CityLink;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class CityLinkService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Getter
  private final Map<String, Set<String>> cityLinksData = new HashMap<>();

  @Value("${cityLink.load.file}")
  private String loadFileName;

  /**
   * 1. Read the data file (csv) at the initialization of the controller and
   * load to a list of CityConnection objects
   * 2. For every row in the file,
   * create two entries in the CitiesConnectionsData map one for
   * origin->destination and another for destination->origin
   */
  @PostConstruct
  public void init() {
    List<CityLink> connections = loadCityLinkData();
    logger.info("Total no. of Cities " + connections.size());
    for (CityLink connection : connections) {
      createConnections(connection.getOrigin(), connection.getDestination());
      createConnections(connection.getDestination(), connection.getOrigin());
    }
  }

  private void createConnections(String origin, String destination) {
    origin = origin.trim().toLowerCase();
    destination = destination.trim().toLowerCase();
    Set<String> sourceConnections = cityLinksData.getOrDefault(origin,
            new HashSet<>());
    sourceConnections.add(destination);
    cityLinksData.put(origin, sourceConnections);
  }

  /**
   * On start up read the data file from disk, parse , convert to a domain
   * object and return list of domain objects Used OpenCSV library to convert
   * csv file to domain object.
   *
   * @return List<CityConnection> - list of connections
   */
  private List<CityLink> loadCityLinkData() {
    List<CityLink> cityConnections = new ArrayList<>();
    logger.info(String.format("Loading data file from '%s'", loadFileName));
    try (CSVReader reader = new CSVReader(new FileReader(loadFileName), ',')) {
      ColumnPositionMappingStrategy<CityLink> beanStrategy =
              new ColumnPositionMappingStrategy<>();
      beanStrategy.setType(CityLink.class);
      beanStrategy.setColumnMapping("origin", "destination");
      CsvToBean<CityLink> csvToBean = new CsvToBean<>();
      cityConnections = csvToBean.parse(beanStrategy, reader);
    } catch (IOException io) {
      logger.error("Error loading file " + loadFileName, io);
    }
    return cityConnections;
  }

  /**
   * @param origin      - Origin City
   * @param destination - Destination City
   * @return true/false based on if the connection exists or not Performing
   * simple BFS algorithm to find if a connection exists in the data or not.
   * Algorithm:
   * 1. Start search with Origin city
   * 2. Add Origin city to a queue
   * 3. Get all its child nodes(connections)
   * 4. Check if destination exists in child nodes
   * 5. Add the current node to the visitedCities Set, so that we
   * don't process this node again.
   * 6. If Yes, return true, there is connection, else, go to step 3
   * 7. Loop until the queue is empty
   * 8. return false, if no match found by the end of loop
   */
  public boolean checkIfConnectionExists(String origin, String destination) {
    Set<String> visitedCities = new HashSet<>();
    LinkedList<String> queue = new LinkedList<>();

    if (origin == null || destination == null) {
      logger.error("Origin or Destination value is null");
      throw new InvalidCityException("One of the city names passed has null " +
              "value");
    }

    queue.add(origin.trim().toLowerCase());
    while (!queue.isEmpty()) {
      String city = queue.poll();
      if (!visitedCities.contains(city)) {
        visitedCities.add(city);
        Set<String> connectedCities = cityLinksData.get(city);
        if (connectedCities != null) {
          if (connectedCities.contains(destination.trim().toLowerCase()))
            return true;
          queue.addAll(connectedCities);
        }
      }
    }
    return false;
  }
}
