package com.codechallenge.citylink.service;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;;
import static org.junit.jupiter.api.Assertions.*;

class CityLinkServiceTest {

  @Test
  void testCreateConnectionsData() throws Exception {
    CityLinkService  service = new CityLinkService();
    Method privateMethod = CityLinkService.class.getDeclaredMethod(
            "createConnections",String.class,String.class);
    privateMethod.setAccessible(true);
    privateMethod.invoke(service, "Dallas","Austin");
    privateMethod.invoke(service, "Austin","Dallas");
    assertTrue(service.getCityLinksData().containsKey("dallas"));
    assertTrue(service.getCityLinksData().containsKey("austin"));
  }

  /*
  This method is tested as part of the controller test for several paths
  */
  @Disabled
  @Test
  void testCheckIfConnectionExists() {
  }
}