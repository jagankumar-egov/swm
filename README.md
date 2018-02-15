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
* Setup Confluent kafka (https://docs.confluent.io/current/quickstart.html)
  - Start zookeeper
  - Start kafka server
  - Start kafka rest proxy
* Run the springboot application
* Run socketio server
* Run admin dashboard server
