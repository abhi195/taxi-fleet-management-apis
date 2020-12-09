# Taxi Fleet Management

A small web service for taxi fleet management that I wrote in an effort to learn Spring-Boot, JPA and Hibernate.

It mainly has two resources drivers and taxis. 
Service exposes various HTTP REST APIs to manage these drivers & cars.

There are APIs for creating/deleting drivers and cars, assigning cars to online drivers, searching & sorting drivers based on various filters, etc.

For detailed APIs documentation, please refer to SwaggerUI(http://localhost:8080/swagger-ui.html) after starting the server.
It serves both as API document as well as interactive playground for trying out APIs.

## Starting the server

- You should be able to start the server by executing com.triptaxi.TaxiFleetManagementApplication, 
  which starts a webserver on port 8080 (http://localhost:8080).


- Alternatively use following command line to start the server
```bash
./mvnw spring-boot:run
```

## Technologies

- Java 1.8
- Spring Boot
- Database H2 (In-Memory)
- Maven