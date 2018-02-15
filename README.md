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

If you want to use SecureString you'll need to update the serverless.yml file to tell it to decrypt the value with the ~true flag:

    ${ssm:/path/to/secureparam~true}

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

    serverless deploy -v -s dev-seth

## Undeploy (remove)

    serverless remove -s dev-seth

## Test

Send a message to a kinesis stream from the command line:

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

This should be picked up by the lambda function and logged to [CloudWatch](https://ap-southeast-1.console.aws.amazon.com/cloudwatch/home?region=ap-southeast-1#logs:)
and then make its way to your admin UI which you can see via Chrome developer tools


## Errata

If you try to remove your stack, it may not delete the serverless deployment bucket cleanly. You may have to delete that manually.
