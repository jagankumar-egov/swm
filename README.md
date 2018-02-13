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