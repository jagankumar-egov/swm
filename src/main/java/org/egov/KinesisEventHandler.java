package org.egov;

import com.amazonaws.services.kinesis.clientlibrary.types.ProcessRecordsInput;
import com.amazonaws.services.kinesis.model.Record;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public interface KinesisEventHandler {

    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    default boolean handleEvent(KinesisEvent event) {
        event.getRecords().forEach(this::handleKinesisEvent);
        return true; // TODO: consider what to do with runtime exceptions
    }

    default boolean handleEvent(ProcessRecordsInput processRecordsInput) {
        processRecordsInput.getRecords().forEach(this::handleKinesisEvent);
        return true;
    }

    // Lambdas provide com.amazonaws.services.lambda.runtime.events.KinesisEvent.KinesisEventRecord
    default void handleKinesisEvent(KinesisEvent.KinesisEventRecord kinesisEventRecord) {
        processData(kinesisEventRecord.getKinesis().getData());
    }

    // KCL provides com.amazonaws.services.kinesis.model.Record
    default void handleKinesisEvent(Record kinesisModelRecord) {
        processData(kinesisModelRecord.getData());
    }

    default String byteBufferToString(ByteBuffer byteBuffer) {
        byte[] byteArray = byteBuffer.array();
        return new String(byteArray, Charset.forName("UTF-8"));
    }

    void processData(ByteBuffer data);
}
