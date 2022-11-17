# spring-boot

##Application:

## Technical:
1. Framework: Spring Boot v2.0.4
2. Java 8

## Install:
You will find below a step by step explanation that tell you how to get a development environment running :

1.Install Java: https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html

2.Install Gradle: https://gradle.org/install/

# Gradle :
## Test :

In a terminal, you go to the source folder of the application and can run the `mvn test` command to perform the tests
## Deploy :

In a terminal, you go to the source folder of the application and can run the `mvn package` command to generate the application jar file

After generate jar file, run the `mvn install` command for push jar file in the local repository

And last run the `mvn deploy` command for deploy application in your server

But you can only use `mvn deploy` command, this command execute previous commands

## Perform Test
highVolumeTrackLocation 100 000 user : 2 min 16 sec
highVolumeGetRewards 10 000 user : 26 sec 

## Endpoints :

Endpoints are available for managing financial entities with CRUD methods. Endpoints are available for each entity :
1. GetIndex : GET http://localhost:8080/
2. GetLocation user : GET http://localhost:8080/getLocation/userName
3. GetClosesAttrations user : GET http://localhost:8080/getClosesAttrations/userName
4. GetAllCurrentLocations : GET http://localhost:8080/getAllCurrentLocations
5. GetTripDeals user : GET http://localhost:8080/getTripDeals/userName