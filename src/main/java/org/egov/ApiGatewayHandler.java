package org.egov;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public interface ApiGatewayHandler extends RequestStreamHandler {

    void processMessageBody(JsonNode body);

    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    default void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        ObjectNode response = OBJECT_MAPPER.createObjectNode();
        try {
            JsonNode jsonNode = OBJECT_MAPPER.readValue(inputStream, JsonNode.class);
            context.getLogger().log(jsonNode.toString());
            JsonNode body = jsonNode.path("body");

            processMessageBody(body);

            ObjectNode headerJson = OBJECT_MAPPER.createObjectNode();
            headerJson.put("x-custom-header", "bw");

            response.put("isBase64Encoded", false);
            response.put("statusCode", 200);

            response.set("headers", headerJson);
            response.put("body", "{}");

        } catch(Exception pex) {
            response.put("statusCode", 400);
            response.put("exception", pex.getMessage());
        }

        context.getLogger().log(response.toString());
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(response.toString());
        writer.close();
    }
}
