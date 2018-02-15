package org.egov;

import com.fasterxml.jackson.databind.JsonNode;
import java.nio.ByteBuffer;

// This lambda uses the VehicleKinesisEventHandler to write the message to RDS and send to SocketIO server

public class ApiGatewayProcessingLambda implements ApiGatewayHandler{

	private static KinesisEventHandler _eventHandler = new VehicleKinesisEventHandler();

	@Override
	public void processMessageBody(JsonNode body) {
		_eventHandler.processData(ByteBuffer.wrap(body.toString().getBytes()));
	}
}
