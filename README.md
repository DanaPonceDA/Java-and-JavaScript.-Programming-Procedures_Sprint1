#  Java Spark CRUD Project — Test & Coverage Report

##  Overview
This project is a **Java CRUD web application** built with the **Spark framework**, using **DAO pattern**, **H2 database**, and **RESTful controllers**.  
It includes complete **unit and integration testing** with **JUnit 5**, **Mockito**, and **JaCoCo** for code coverage.

--

## System Objectives

Enable item management (CRUD).

Enable bidding on available items.

Display real-time price updates using WebSockets.

Include unit and integration tests to ensure code quality.

---

## Main Modules
Module                  |       Description
ItemController          |       Manages the creation and listing of items.
OfferController         |       Allows users to place and register bids.
PriceWebSocket          |       Sends real-time price updates.
ItemDAO, OfferDAO       |        Access to the database for the corresponding objects.
Frontend                |        User interface for interacting with the system.





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


    <dependencies>
        <!-- Spark Java -->
        <dependency>
            <groupId>com.sparkjava</groupId>
            <artifactId>spark-core</artifactId>
            <version>2.9.4</version>
        </dependency>
        
        <dependency>
            <groupId>com.sparkjava</groupId>
            <artifactId>spark-template-mustache</artifactId>
            <version>2.7.1</version>
        </dependency>

        <!-- Gson for JSON serialization -->
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.10.1</version>
        </dependency>

        <dependency>
            <groupId>org.eclipse.jetty.websocket</groupId>
            <artifactId>websocket-server</artifactId>
            <version>9.4.50.v20221201</version>
        </dependency>
        <dependency>
            <groupId>org.eclipse.jetty.websocket</groupId>
            <artifactId>websocket-servlet</artifactId>
            <version>9.4.50.v20221201</version>
        </dependency>

        <!-- Logback for logging -->
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.4.11</version>
        </dependency>

        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <version>5.17.0</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.7</version>
        </dependency>

        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-inline</artifactId>
            <version>5.2.0</version>
            <scope>test</scope>
        </dependency>

        <!-- MySQL Connector -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>9.4.0</version>
        </dependency>

        <dependency>
            <groupId>org.apache.velocity</groupId>
            <artifactId>velocity-engine-core</artifactId>
            <version>2.3</version>
        </dependency>
        <dependency>
            <groupId>com.sparkjava</groupId>
            <artifactId>spark-template-velocity</artifactId>
            <version>2.7.1</version>
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
    </dependencies>

    <build>
        <plugins>
            <!-- Maven compiler plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>0.8.11</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>prepare-agent</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>report</id>
                        <phase>test</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.1.2</version>
                <configuration>
                    <forkCount>1</forkCount>
                    <reuseForks>true</reuseForks>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.3</version>
            </plugin>
        </plugins>
    </build>


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
