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
