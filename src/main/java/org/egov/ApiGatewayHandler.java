package org.egov;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.StringEscapeUtils;

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
            //context.getLogger().log(OBJECT_MAPPER.writeValueAsString(jsonNode));
            JsonNode body = jsonNode.path("body");
            // TODO: this body element has escaped quotes
            //       like this: "{\"records\":[{\"value\":{\"vehicleNo\":\"KA1201\",
            String stringBody = OBJECT_MAPPER.writeValueAsString(body);
            stringBody = stringBody.substring(1, stringBody.length() - 1); // kill leading and trailing "
            String unescaped = StringEscapeUtils.unescapeJava(stringBody); // unescape "

            context.getLogger().log(unescaped);
            JsonNode jsonBody = OBJECT_MAPPER.readValue(unescaped, JsonNode.class);
            processMessageBody(jsonBody);

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
