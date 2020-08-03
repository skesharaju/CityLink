package com.codechallenge.citylink.controller.impl;

import com.codechallenge.citylink.controller.CityLinkController;
import com.codechallenge.citylink.exception.InvalidCityException;
import com.codechallenge.citylink.service.CityLinkService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CityLinkControllerTest {
  private final Function<String[], Set<String>> citiesSet;
  CityLinkService linkService;
  CityLinkController cityLinkController;
  Map<String, Set<String>> cityLinkConnectionMap;

  {
    citiesSet = (cities) -> {
      Set<String> citiesList = new HashSet<>();
      Collections.addAll(citiesList, cities);
      return citiesList;
    };
  }

  public CityLinkControllerTest() {
    this.linkService = new CityLinkService();
    this.cityLinkConnectionMap = this.linkService.getCityLinksData();
    this.cityLinkConnectionMap.put("a", citiesSet.apply(new String[]{"b","c"}));
    this.cityLinkConnectionMap.put("b", citiesSet.apply(new String[]{"a"}));
    this.cityLinkConnectionMap.put("c", citiesSet.apply(new String[]{"a","d"}));
    this.cityLinkConnectionMap.put("d", citiesSet.apply(new String[]{"c"}));
    this.cityLinkConnectionMap.put("k", citiesSet.apply(new String[]{"e", "f"}));
    this.cityLinkConnectionMap.put("e", citiesSet.apply(new String[]{"k"}));
    this.cityLinkConnectionMap.put("f", citiesSet.apply(new String[]{"k"}));
    this.cityLinkController = new CityLinkController(this.linkService);
  }

  /*
      Simple test to check if a test is performed when no data exists in the
      load
   */
  @Test
  void findConnectionWithInvalidParams_ShouldThrowException() {
    Exception exception = assertThrows(InvalidCityException.class,
            () -> cityLinkController.findConnection("J", "a"));
    assertEquals("Invalid origin City 'J' is passed as an argument",
            exception.getMessage());
  }

  @Test
  void findConnectionPositiveCase() {
    String result = this.cityLinkController.findConnection("a", "d");
    assertEquals("yes", result);
  }

  @Test
  void findConnectionInReverseOrder() {
    String result = this.cityLinkController.findConnection("d", "a");
    assertEquals("yes", result);
  }


  @Test
  void findConnectionNegativeCase() {
    String result = this.cityLinkController.findConnection("a", "k");
    assertEquals("no", result);

  }

  @Test
  void findConnectionForCircularConnection() {

    //Mock data setup
    CityLinkService linkService = new CityLinkService();
    Map<String, Set<String>> cityLinkConnectionMap =
            linkService.getCityLinksData();
    cityLinkConnectionMap.put("a", citiesSet.apply(new String[]{"b"}));
    cityLinkConnectionMap.put("b", citiesSet.apply(new String[]{"a"}));

    CityLinkController cityLinkController =
            new CityLinkController(linkService);
    String result = cityLinkController.findConnection("a", "a");
    assertEquals("yes", result);
  }
}