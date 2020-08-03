package com.codechallenge.citylink.controller;

import com.codechallenge.citylink.exception.InvalidCityException;
import com.codechallenge.citylink.service.CityLinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;

@Api
@RestController
public class CityLinkController {
  private final CityLinkService cityLinkService;
  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  public CityLinkController(CityLinkService cityLinkService) {
    this.cityLinkService = cityLinkService;
  }

  /**
   * @param origin      - Origin city
   * @param destination - Destination city
   * @return "yes" if a connection is found , else "no" is returned Exception:
   * If Origin and Destination cities passed are invalid, validation will happen
   * and Exception is returned to client.
   */

  @GetMapping(value = "/connection")
  @ApiOperation(value = "Return if there a connections exists or not, if a " +
          "connection exist, return 'yes' else 'no'.", notes = "This is a" +
          "publicAPI", response = String.class)
  @ApiResponse(code = HttpServletResponse.SC_OK, message = "Success")
  @ResponseBody
  public String findConnection(String origin, String destination) {
    logger.info(String.format("Checking for connection between Origin city " +
            "'%s' and Destination city '%s' ", origin, destination));
    validateCities(origin, destination);
    boolean ifConnectionExists =
            cityLinkService.checkIfConnectionExists(origin, destination);
    return ifConnectionExists ? "yes" : "no";
  }

  /**
   * Validate origin and destination cities
   * @param origin  - Otigin city
   * @param destination - Destination city
   */
  public void validateCities(String origin, String destination) {
    if (isInvalidCity(origin)) {
      String errorMsg = String.format("Invalid origin City '%s' is passed as " +
              "an argument", origin);
      logger.error(errorMsg);
      throw new InvalidCityException(String.format(errorMsg, origin));
    }
    if (isInvalidCity(destination)) {
      String errorMsg = String.format("Invalid destination City '%s' is " +
              "passed as an argument", destination);
      logger.error(errorMsg);
      throw new InvalidCityException(String.format(errorMsg, destination));
    }
  }

  private boolean isInvalidCity(String city) {
    return !cityLinkService.getCityLinksData().containsKey(city.trim().toLowerCase());
  }
}
