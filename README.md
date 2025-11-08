#  Java Spark CRUD Project — Test & Coverage Report

##  Overview
This project is a **Java CRUD web application** built with the **Spark framework**, using **DAO pattern**, **H2 database**, and **RESTful controllers**.  
It includes complete **unit and integration testing** with **JUnit 5**, **Mockito**, and **JaCoCo** for code coverage.

The goal was to ensure functional reliability and achieve **over 90% code coverage** across all modules.

---

##  Tech Stack

| Component | Description |
|------------|--------------|
| **Language** | Java 17 |
| **Framework** | Spark Java |
| **Database** | H2 (in-memory for testing) |
| **Testing** | JUnit 5, Mockito |
| **Code Coverage** | JaCoCo |
| **Build Tool** | Maven |

---

##  Project Structure

<img width="296" height="475" alt="image" src="https://github.com/user-attachments/assets/971e80c5-95bd-472c-a79d-4d6ea4123035" />
##  Testing Environment

###  Dependencies

Add the following to your `pom.xml`:
```xml
<dependencies>
  <!-- JUnit 5 -->
  <dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.9.3</version>
    <scope>test</scope>
  </dependency>

  <!-- Mockito -->
  <dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.10.0</version>
    <scope>test</scope>
  </dependency>

  <!-- H2 Database -->
  <dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.2.224</version>
    <scope>test</scope>
  </dependency>

  <!-- JaCoCo Plugin -->
  <dependency>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.12</version>
  </dependency>
</dependencies>

Test summary:
<img width="904" height="258" alt="image" src="https://github.com/user-attachments/assets/08ac54de-1e3f-49f6-9cdc-84825373639f" />

Results Overview:
<img width="946" height="237" alt="image" src="https://github.com/user-attachments/assets/81aed30f-896c-4588-ac6e-a3e5de07eb32" />

Code Coverage by Package
Package	                  |Coverage    |	Status
org.example.model	        |100%        |	✅
org.example.dao	          |94%	       |✅
org.example.controller	  | 91%	        |✅
Total Average	            |92.4%	      |✅


 Issues Detected & Fixes Applied
Issue	Description	Fix
Route mismatch in UserController.addUser()	Required /users/:id instead of POST /users	Adjusted to accept POST /users with ID from body
NullPointerException in PriceWebSocket	DAO not initialized before broadcast	Initialized DAO in Main.java before WebSocket start
DAO test pollution	Tables retained previous data	Added @BeforeEach cleanup in DAO tests


Final Evaluation

<img width="923" height="244" alt="image" src="https://github.com/user-attachments/assets/3c486c96-ba9d-4e8c-bac9-c8c316a4d79c" />
