## What is this

This project uses Serverless to create a Cloudformation stack containing a Kinesis stream and a Lambda
that is triggered by events on that stream. Resource names are namespaced to the project and deployment stage.
Standard lambda logging goes to the CloudWatch log group that is automatically created for the Lambda.

The Lambda function in this project is written in Java.

## Setup

Requires Java8, Maven, Serverless

[Serverless docs](https://serverless.com/framework/docs/providers/aws/guide/intro/)

You can set the RDS username and password from the aws command line or via the [AWS Console](https://ap-southeast-1.console.aws.amazon.com/systems-manager/parameters?region=ap-southeast-1)

    aws ssm put-parameter --type String --name /serverless-java/dev/rds_username --value rdsusername
    aws ssm put-parameter --type SecureString --name /serverless-java/dev/rds_password --value rdspassword

If you don't create these parameters you will see a warning like this when deploying your stack:

    Serverless Warning --------------------------------------
    
         A valid SSM parameter to satisfy the declaration 'ssm:/serverless-java/prod/rds_username' could not be found.
  
## Build

### Lambda function

```bash
$ mvn clean package
```

### SocketIO Docker Image

This comes from the [swm](https://github.com/egovernments/swm) repository in the `socketio-server`
folder. The docker image is stored in Joel's private ECR registry.  From the socketio-server
directory of the swm repository, execute these commands (updating the image repository with your ECR
repository, replacing `854766835649.dkr.ecr.ap-southeast-1.amazonaws.com/egov-dev/socketio` with
your ECR repository as appropriate):

1. `$ aws ecr get-login --no-include-email --region ap-southeast-1`
1. Run the docker login command that was returned in the previous step.
1. `$ docker build -t egov-dev/socketio .`
1. After the build completes, tag your image so you can push the image to this repository:
   `$ docker tag egov-dev/socketio:latest 854766835649.dkr.ecr.ap-southeast-1.amazonaws.com/egov-dev/socketio:latest`
1. Run the following command to push this image to your ECR repository:
   `$ docker push 854766835649.dkr.ecr.ap-southeast-1.amazonaws.com/egov-dev/socketio:latest`

### AdminUI Docker Image

This comes from the [swm](https://github.com/egovernments/swm) repository, in the `swm-admin`
folder.  Follow the same steps as above, but replace `socketio` with `swm-admin`.

## Deploy

The default deployment stage is dev.

    serverless deploy -v

You will probably want to create your own stack under your name.
Note that due to API Gateway naming conventions, the stage name must be alphanumeric or underscore only. (Creation will fail with error "Stage name only allows a-zA-Z0-9_")

    serverless deploy -v -s devseth

## Undeploy (remove)

    serverless remove -s devseth

## Test

These should be picked up by a lambda function and logged to
[CloudWatch](https://ap-southeast-1.console.aws.amazon.com/cloudwatch/home?region=ap-southeast-1#logs:)
and then make their way to your admin UI which you can see via Chrome developer tools



### Directly to Kinesis

This will send a message to the kinesis stream from the command line:

```bash
$ aws kinesis put-record --stream-name <stream_name> --partition-key 1 --data '{
  "vehicleNo":"123",
  "routeCode":"abc",
  "BatteryInfo":{},
  "coords":  {
    "accuracy": 9.64799976348877,
    "altitude": 815.634765625,
    "heading": 0,
    "latitude": 12.9187821,
    "longitude": 77.6702731,
    "speed": 0
  },
  "mocked": false,
  "timestamp": 1518584082935
}'
```

### Via API Gateway

_Note that only the first method works properly (methods 2 and 3 appear to have a bug in the JSON
encoding causing the admin UI to be unable to deserialize it)._


There are currently three API gateway ingest points set up:

1. API Gateway to Kinesis. To test this, run a command like:
```bash
$ curl -X POST https://<gateway_host>.execute-api.ap-southeast-1.amazonaws.com/<stage_name>/swm-ingest -H "Content-Type: application/json" -d '{
  "vehicleNo":"213",
  "routeCode":"abc",
  "BatteryInfo":{},
  "coords":  {
    "accuracy": 9.64799976348877,
    "altitude": 815.634765625,
    "heading": 0,
    "latitude": 12.9137821,
    "longitude": 77.6402731,
    "speed": 0
  },
  "mocked": false,
  "timestamp": 1518584085000
}'
```
1. API Gateway to lamba to Kinesis. To test this, run a command like:
```bash
$ curl -X POST https://lq5hyy4kua.execute-api.ap-southeast-1.amazonaws.com/devjoel/apikinesis -H "Content-Type: application/json" -d '{
  "vehicleNo":"213",
  "routeCode":"abc",
  "BatteryInfo":{},
  "coords":  {
    "accuracy": 9.64799976348877,
    "altitude": 815.634765625,
    "heading": 0,
    "latitude": 12.9137821,
    "longitude": 77.6402731,
    "speed": 0
  },
  "mocked": false,
  "timestamp": 1518584085000
}'
```
1. API Gateway to lambda directly. To test this, run a command like:
```bash
$ curl -X POST https://lq5hyy4kua.execute-api.ap-southeast-1.amazonaws.com/devjoel/direct -H "Content-Type: application/json" -d '{
  "vehicleNo":"213",
  "routeCode":"abc",
  "BatteryInfo":{},
  "coords":  {
    "accuracy": 9.64799976348877,
    "altitude": 815.634765625,
    "heading": 0,
    "latitude": 12.9137821,
    "longitude": 77.6402731,
    "speed": 0
  },
  "mocked": false,
  "timestamp": 1518584085000
}'
```

## Errata

If you try to remove your stack, it may not delete the serverless deployment bucket cleanly. You may have to delete that manually.
