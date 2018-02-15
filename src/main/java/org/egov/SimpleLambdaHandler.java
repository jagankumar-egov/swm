package org.egov;

import java.util.logging.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;

public class SimpleLambdaHandler implements RequestHandler<KinesisEvent, Boolean> {

	private static final Logger LOG = Logger.getLogger(String.valueOf(SimpleLambdaHandler.class));

	private static KinesisEventHandler _eventHandler = new VehicleKinesisEventHandler();

	@Override
	public Boolean handleRequest(KinesisEvent event, Context context) {
		LOG.info("received: " + event);
		return _eventHandler.handleEvent(event);
	}

}
