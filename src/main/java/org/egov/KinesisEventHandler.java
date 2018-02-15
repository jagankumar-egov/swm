package org.egov;

import com.amazonaws.services.lambda.runtime.events.KinesisEvent;

public interface KinesisEventHandler {
    default boolean handleEvent(KinesisEvent event) {
        event.getRecords().forEach(this::handleKinesisEvent);
        return true; // TODO: consider what to do with runtime exceptions
    }

    void handleKinesisEvent(KinesisEvent.KinesisEventRecord kinesisEventRecord);
}
