package org.egov;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;

public class SimpleLambdaHandler implements RequestHandler<KinesisEvent, Boolean> {

	private static KinesisEventHandler _eventHandler = new VehicleKinesisEventHandler();

	@Override
	public Boolean handleRequest(KinesisEvent event, Context context) {
		return _eventHandler.handleEvent(event);
	}

}
