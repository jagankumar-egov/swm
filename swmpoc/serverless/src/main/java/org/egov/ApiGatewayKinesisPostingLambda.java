package org.egov;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClient;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.amazonaws.services.kinesis.model.PutRecordResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

// This lambda receives a message from API Gateway and passes it on to Kinesis
// It may be possible to configure API Gateway to directly pass data to Kinesis
// setting that up on the UI and exporting the definition via swagger may be the best bet

public class ApiGatewayKinesisPostingLambda implements ApiGatewayHandler {

	private static final Logger LOG = Logger.getLogger(String.valueOf(ApiGatewayKinesisPostingLambda.class));

	private AmazonKinesis _kinesisClient = AmazonKinesisClient
			.builder()
			.withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
			.build();


    final static String OUTPUT_STREAM = System.getenv("KINESIS_STREAM");

    @Override
    public void processMessageBody(JsonNode body) {
        ByteBuffer data;
        try {
            data = ByteBuffer.wrap(OBJECT_MAPPER.writeValueAsBytes(body));
        } catch (JsonProcessingException e) {
            LOG.log(Level.SEVERE, "Unable to convert JSON to ByteBuffer. ", e);
            return; // throw e;
        }
        // post data to kinesis
        PutRecordRequest putRecordRequest = new PutRecordRequest();
        putRecordRequest.setStreamName(OUTPUT_STREAM);
        putRecordRequest.setData(data);
        putRecordRequest.setPartitionKey("session");
        PutRecordResult putRecordResult = _kinesisClient.putRecord(putRecordRequest);
        LOG.info("Successful PutRecordRequest: partition key : " + putRecordRequest.getPartitionKey()
                + ", ShardID : " + putRecordResult.getShardId() + ", Sequence Number: "
                + putRecordResult.getSequenceNumber());
    }
}
