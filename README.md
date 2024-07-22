<h1 align="center"> Market Management System </h1> <br>

<p align="center">
  Market Management System is a Java-based application designed to efficiently manage a market website, including user roles, permissions, product management, and order processing.
  This project leverages the power of the Spring Framework and Spring Data JPA for a robust data access layer and Hibernate for ORM. 
</p>


## Table of Contents

- [Dependencies](#dependencies)
- [Requirements](#requirements)
- [Installation and Running the System](#installation-and-running-the-system)
- [Testing](#testing)
- [API](#api)
- [init file](#init-file)



## Dependencies
* Spring Boot Starter Data JPA: For data access and ORM
* Spring Boot Starter Web: For building web applications
* Hibernate: For ORM
* Vaadin: Web application framework
* com.microsoft.sqlserver: for database connectivity
  

## Requirements
- Java 8 or higher
- Maven
- Having an account on azure and connecting to the database


## Installation and Running the System

### Run Local
```bash
$ mvn spring-boot:run
```
Application will run by default on port `8080`
Configure the port by changing `server.port` in [application.yml](market14B/src/main/resources/application.yml)

## Configuration Files
the configurations for the web are in file [application.properties](market14B/src/main/resources/application.properties)

For database including the URL,username and password are in [application.yml](market14B/src/main/resources/application.properties)
note : the project has a README.md file in the bundles directory that helps configure vaadin automatically 

## Testing
Tests for the system are in [src/test](market14B/src/test).
tests use a different database, configuration details are in [application-test.yml](market14B/src/main/resources/application-test.yml)

## API
payment service url="https://damp-lynna-wsep-1984852e.koyeb.app/"

## init file
This file describes the initial state of the system as well as a use case for using the system [initFile2.txt](market14B/src/main/resources/initFile2.txt)
After initializing with this file there will be 6 users in the system ui accordingly. 

u1 is the admin , u2 is the first owner of store "s1".
u2 sets u3 to be the manager of his store, and u4 u5 to be the co-owners.
