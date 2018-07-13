## SWM Truck tracking System

Prerequisite:
* Spring boot
* Confluent Kafka
* cassandra
* NPM socket io, socket server

### Folders
    * waste-management/ : Core spring-boot application
    * socketio-server/ : websocket server
    * swm-admin/ : Admin Dashboard (React App)
    * swm-driver/: Driver App (React Native)
    * serverless/: POC for the data flow through AWS-native services

Local setup:

* Setup Confluent kafka (https://docs.confluent.io/current/quickstart.html)
  - Start zookeeper
  - Start kafka server
  - Start kafka rest proxy
* Run the springboot application
* Run socketio server
* Run admin dashboard server

Serverless Setup: Follow the instructions in the [serverless README](./swmpoc/serverless/README.md).

#Interns - Changes made:
Backend - in WasteManagement repo
* Added new classes with required attributes
* Added apis for create, update, search for all classes
Data flow:
* Data is recieved from api end points by controller, then a call is made to service where it is pushed to kafka-topics. 
It is picked up by consumer, and then to service. Here a call is made to repository and the response is sent back to           controller. Controller sends the response to front end.
* The tracking data recieved from kafka rest proxy is streamed to socket and repository.

Frontend - in swm-admin repo:
* We modified the home page. We added links in side menu for adding new instances of the classes.
* We made slight changes in dashboard display - passed some props (source, dest, enrouteOrders) to Tracker.




