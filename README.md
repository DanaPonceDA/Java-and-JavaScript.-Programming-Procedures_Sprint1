# Auction House Project - Sprint 2

## Project Description
This project involves developing a web-based auction house platform where users can browse available items and place bids. The system also includes a graph visualization module showing nearby auctions and distances between them. The goal of Sprint 2 is to implement **unit tests for the graph visualization module** in JavaScript, ensuring high reliability and performance of the front-end functionality.

**Auction House Website Access:**
- [View Items](http://localhost:4567/items)
- [Place Offer on Item](http://localhost:4567/offers/new?itemId=1)

---

## Project Scope
- Implement unit tests using **Jest** for the graph visualization module.
- Validate that the graph displays nearby auctions and distances correctly.
- Handle edge cases such as empty or inconsistent data.
- Achieve at least 90% test coverage for the graph module.
- Ensure seamless integration with the backend system developed in Java.

---

## How It Works
1. **Browsing Items:** Users can navigate to the items list and select any auction item they are interested in.
2. **Placing Offers:** Users can submit bids through the offer page.
3. **Graph Visualization:** The front-end graph shows nearby auctions and distances, updating dynamically.
4. **Testing:** Jest tests simulate different scenarios to validate graph functionality, error handling, and edge cases.

---

## Technology Stack & Dependencies
**Backend (Java):**
- Java 17
- [Spark Java](http://sparkjava.com/) for web framework
- Gson for JSON serialization
- Spark Template Engines (Mustache & Velocity)
- Jetty WebSocket Server & Servlet
- MySQL Connector
- Logback & SLF4J for logging

**Testing (Java):**
- JUnit 5
- Mockito for mocking
- H2 In-memory Database
- Hamcrest for assertions
- JaCoCo for code coverage

**Frontend (JavaScript):**
- Jest for unit testing

**Maven Plugins:**
- maven-compiler-plugin
- maven-surefire-plugin
- jacoco-maven-plugin

**Sample Maven Dependencies:**
```xml
<dependency>
    <groupId>com.sparkjava</groupId>
    <artifactId>spark-core</artifactId>
    <version>2.9.4</version>
</dependency>
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.10.2</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.3.232</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-inline</artifactId>
    <version>5.2.0</version>
    <scope>test</scope>
</dependency>


Project Structure:
src
 └── main
      ├── java
      │    └── org.example
      │         ├── controller
      │         │    ├── ItemController.java        
      │         │    ├── OfferController.java
      │         │    ├── UserController.java
      │         │    └── PriceWebSocket.java        
      │         │
      │         ├── dao
      │         │    ├── ItemDAO.java               
      │         │    ├── OfferDAO.java
      │         │    └── UserDAO.java
      │         │
      │         ├── Item.java
      │         ├── Offer.java
      │         ├── User.java
      │         ├── Database.java
      │         └── Main.java
      │
      └── resources
           ├── public
           │    ├── script.js                       
           │
           │
           └── templates
               ├── item_detail.mustache
               ├── item_list.mustache              
               ├── offer_form.mustache
               ├── offer_list.mustache
└── test 
     └── java
         └── org.example
             ├── controller
             │   ├── ItemControllerTest.java
             │   └── OfferControllerTest.java
             │
             └── dao
                 ├── ItemDAOTest.java
                 ├── OfferDAOTest.java
                 └── DatabaseTestHelper.java 

