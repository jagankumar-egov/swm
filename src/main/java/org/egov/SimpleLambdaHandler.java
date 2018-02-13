package org.egov;

import java.nio.charset.Charset;
import java.util.logging.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;

public class SimpleLambdaHandler implements RequestHandler<KinesisEvent, Boolean> {

	private static final Logger LOG = Logger.getLogger(String.valueOf(SimpleLambdaHandler.class));

	@Override
	public Boolean handleRequest(KinesisEvent event, Context context) {
		LOG.info("received: " + event);
		event.getRecords().forEach(this::handleKinesisEvent);
		return true;
	}

	private void handleKinesisEvent(KinesisEvent.KinesisEventRecord kinesisEventRecord) {
        byte[] byteArray = kinesisEventRecord.getKinesis().getData().array();
        String message = new String(byteArray, Charset.forName("UTF-8"));
        LOG.info(message);
	}
}
