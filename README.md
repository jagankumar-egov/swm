## What is this

This project uses Serverless to create a Cloudformation stack containing a Kinesis stream and a Lambda 
that is triggered by events on that stream. Resource names are namespaced to the project and deployment stage.
Standard lambda logging goes to the CloudWatch log group that is automatically created for the Lambda.

The Lambda function in this project is written in Java.

## Setup

Requires Java8, Maven, Serverless

[Serverless docs](https://serverless.com/framework/docs/providers/aws/guide/intro/)

## Build

    mvn clean package

## Deploy

The default deployment stage is dev.

    serverless deploy -v

You may want to stand up your own stack under your name.

    serverless deploy -v -s dev-seth

## Undeploy (remove)

    serverless remove

    serverless remove -s dev-seth
