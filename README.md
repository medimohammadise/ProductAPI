# Product API

This application developed as a hand-on app for exposing product api using Kotlin and Spring Boot

## Development utilities/libs/components

The following components have been used:
Flyway for DB migration
ActiveMQ for messaging (PubSub)
Junit5 for testing
Coroutine for asynchronous REST API calls
Mockito
Java Faker 
Open API documentation using swagger 
Gradle as a build tool

## How to run the application
  1- (For the first time only) In the project dir got to src/main/docker and run init-all.sh (This will create required docker containers: ActiveMQ
 and Postgress)
 
  2- go tto the project folder and run ./gradlew (in Mac)
The API will be accessible in the http://localhost:8080/

## Open API / Swagger for checking the API documentation 
http://localhost:8080/v3/api-docs/
http://localhost:8080/webjars/swagger-ui