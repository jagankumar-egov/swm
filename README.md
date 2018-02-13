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
    aws ssm put-parameter --type String --name /serverless-java/dev/rds_password --value rdspassword

If you want to use SecureString you'll need to update the serverless.yml file to tell it to decrypt the value with the ~true flag:

    ${ssm:/path/to/secureparam~true}

## Build

    mvn clean package

## Deploy

The default deployment stage is dev.

    serverless deploy -v

You will probably want to create your own stack under your name.

    serverless deploy -v -s dev-seth

## Undeploy (remove)

    serverless remove -s dev-seth

## Test

send a message to a kinesis stream from the command line

     aws kinesis put-record --stream-name serverless-java-dev-seth-TestKinesisStream --data "hello seth" --partition-key 1

This should be picked up by the lambda function and logged to [CloudWatch](https://ap-southeast-1.console.aws.amazon.com/cloudwatch/home?region=ap-southeast-1#logs:)


## Errata

If you try to remove your stack, it may not delete the serverless deployment bucket cleanly. You may have to delete that manually.