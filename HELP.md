# Getting Started
### Overview
Reads the cities connections data from csv file and exposes a rest endpoint
 which provides the information if two given cities are connected either
  directly or indirectly.

### Dependencies
    1. Java 1.8
    2. Spring boot 2.3.2
    3. OpenCsv 3.8
    4. Swagger 2.3.2
    5. Lombok
    6. Junit 5
    7. Logback
### Project Structure
    1. Controller
    2. Service
    3. Exception 
    4. Model
    5. Config
    6. logging - logback
    
### Design
    On start up Cities Link data is Loaded and Parsed as part of
     CityLinkService class initlization
        1. Chose open csv library to read csv file from the source and parse and
            Bind to Java bean
        2. Data stored in a HashMap with every city in the file as key and
            all its connections as values in as Set.  
          for eg: If the data in file is like  
             A,B  
             A,C  
             Data in HashMap is like  
             A-> B,C  
             B-> A  
             C-> A  
    User Passes Origin city and destination city as part of the request to
     look up the connection   
     Algorithm for searching connection in cities data  
        1. Searching is a simple BFS algorithm. BFS search performed starting
           from the source city until a destination found or till reaching the
           end of the chain with no match. 
    Once the search is done, "yes"/"no" is passed back to client depending on
     weather link found or not  
    Exceptions:  
      If user passes a city in the URL which is not in the data file
      , invalidCity Exception is thrown, this could also be handled
       differently with out throwing exception by just returning "no".  
    Swagger:
       Swagger is implemented  for API documentation
       http://localhost:8080/swagger-url.html
    API:
        http://localhost:8080/connection?origin=Boston&destination=Philadelphia
    

Enhancements:
    1. GraphDB can be used for storing the data  
    2. More endpoints can be exposed to print the full path instead of yes/no  
     if the connection exists  
    3. Proper Junit test coverage has been done but full Integration testing
     can be added  
    3. Given the distance between all the nodes, shortest distance between the
     nodes can be implemented.
    
 
### 
   1. User passes Origin and Destination cities as part of request params

### Reference Documentation

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
