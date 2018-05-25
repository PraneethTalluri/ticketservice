# Ticket Service

**Introduction**

This repository hosts the ticket service application

**Details**
- Build tool: Gradle
- Server language: Java 8
- Major frameworks: Spring 5, Hibernate
- Spring Boot Application with embedded Tomcat
- Uses in memory HSQLDB as database
- Swagger documentation: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

**Build**

 Use the gradle wrapper to build the application from the root directory. Run the following command:
```bash
gradlew.[bat|sh] clean build
```

**Run**
```bash
java -jar build\libs\ticket-service-0.0.1.jar
```

**Application Notes**

 - Venue controller handles new venue creation and provides info of the available venues.
 - Ticket controller handles new seat holds/reservations and also provides info about existing seat holds/reservations.
 - Current Seat Hold Timeout is set for 30 sec. If needed to change timeout set 'TIMEOUTINSECONDS' in TicketServiceImpl.java and TicketControllerTest.java.
  
 **Venue Controller**
 
 - To create new venue: POST: "http://localhost:8080/venue/{number of rows}/{seats per Row}"
    ```bash
    curl -X POST "http://localhost:8080/venue/2/3" -H  "accept: */*"
    ```
    Note the *venue number* received in the response body
    
 - To get information about a venue:  GET "http://localhost:8080/venue/{venueNumber}"
      ```bash
      curl -X GET "http://localhost:8080/venue/Venue1" -H  "accept: */*"
      ```
    Response body will have details about venue and its seating info
    
 - To get information all the venues:  GET "http://localhost:8080/venue"
 
    ```bash
    curl -X GET "http://localhost:8080/venue" -H  "accept: */*"
    ```
    Response body will contain all the venues and their seating info
    
 **Ticket Controller**
 
 - To get information about number of available seats at a venue: GET "http://localhost:8080/tickets/{venue number}"
 
    ```bash
    curl -X GET "http://localhost:8080/tickets/Venue1" -H  "accept: */*"
    ```
     Returns number of available seats at a specified venue
     
 - To hold seats at a specific venue: POST "http://localhost:8080/tickets/seatHold/{venue number}/{number of seats}/{customer Email}"
 
     ```bash
     curl -X POST "http://localhost:8080/tickets/seatHold/Venue1/3/abc%40test.com" -H  "accept: */*"
     ```
     Response body will have *seat hold Id* and details of seats held
     
 - To get info about specific seat hold: GET "http://localhost:8080/tickets/seatHold/{seatHoldId}"
    
    ```bash
    curl -X GET "http://localhost:8080/tickets/seatHold/10" -H  "accept: */*"
    ```
    Returns specific seat hold information(if the seat hold expires, then it would return bad request with seat hold info not found)
    
 - To get all available seatHolds: GET "http://localhost:8080/tickets/seatHold/{seatHoldId}"
 
    ```bash
    curl -X GET "http://localhost:8080/tickets/seatHold" -H  "accept: */*"
    ```
    Returns all available seat hold information with seats held
    
 -  To reserve a specific seat hold: POST "http://localhost:8080/tickets/reserve/{seatHoldId}"
    
    ```bash
    curl -X POST "http://localhost:8080/tickets/reserve/15" -H  "accept: */*"
    ```
    Returns reservation confirmation number(if the seat hold expires, then it would return bad request with seat hold info not found)
 
 - To get info about specific reservation details: GET "http://localhost:8080/tickets/reserve/{confirmation number}"
     
   ```bash
     curl -X GET "http://localhost:8080/tickets/reserve/16" -H  "accept: */*"
   ```
   Returns specific reservation information with seats info
     
 - To get all available reservations: GET "http://localhost:8080/tickets/reserve}"
  
   ```bash
     curl -X GET "http://localhost:8080/tickets/reserve" -H  "accept: */*"
   ```
   Returns all available reservations with seats info
 
 
 
 
