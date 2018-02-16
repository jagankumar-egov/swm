package org.egov;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

//@RunWith(PowerMockRunner.class)
//@PowerMockIgnore("javax.net.ssl.*")
public class ApiGatewayKinesisPostingLambdaTest {

    private static final Logger LOG = Logger.getLogger(String.valueOf(ApiGatewayKinesisPostingLambdaTest.class));

    String HTTP_POST = "{\n" +
            "    \"resource\": \"/kinesistest\",\n" +
            "    \"path\": \"/kinesistest\",\n" +
            "    \"httpMethod\": \"POST\",\n" +
            "    \"headers\": null,\n" +
            "    \"queryStringParameters\": null,\n" +
            "    \"pathParameters\": null,\n" +
            "    \"stageVariables\": null,\n" +
            "    \"requestContext\": {\n" +
            "        \"path\": \"/kinesistest\",\n" +
            "        \"accountId\": \"1234\",\n" +
            "        \"resourceId\": \"u9imhz\",\n" +
            "        \"stage\": \"test-invoke-stage\",\n" +
            "        \"requestId\": \"test-invoke-request\",\n" +
            "        \"identity\": {\n" +
            "            \"cognitoIdentityPoolId\": null,\n" +
            "            \"cognitoIdentityId\": null,\n" +
            "            \"apiKey\": \"test-invoke-api-key\",\n" +
            "            \"cognitoAuthenticationType\": null,\n" +
            "            \"userArn\": \"arn:aws:iam::1234:user/seth-egov\",\n" +
            "            \"apiKeyId\": \"test-invoke-api-key-id\",\n" +
            "            \"userAgent\": \"Apache-HttpClient/4.5.x (Java/1.8.0_144)\",\n" +
            "            \"accountId\": \"1234\",\n" +
            "            \"caller\": \"asdfasdf\",\n" +
            "            \"sourceIp\": \"test-invoke-source-ip\",\n" +
            "            \"accessKey\": \"asdfasdf\",\n" +
            "            \"cognitoAuthenticationProvider\": null,\n" +
            "            \"user\": \"asdfasdf\"\n" +
            "        },\n" +
            "        \"resourcePath\": \"/kinesistest\",\n" +
            "        \"httpMethod\": \"POST\",\n" +
            "        \"apiId\": \"7v1vh08e4j\"\n" +
            "    },\n" +
            "    \"body\": \"{\\r\\n  \\\"vehicleNo\\\":\\\"123\\\",\\r\\n  \\\"routeCode\\\":\\\"abc\\\",\\r\\n  \\\"BatteryInfo\\\":{},\\r\\n  \\\"coords\\\": {\\r\\n    \\\"accuracy\\\": 9.64799976348877,\\r\\n    \\\"altitude\\\": 815.634765625,\\r\\n    \\\"heading\\\": 0,\\r\\n    \\\"latitude\\\": 12.9187821,\\r\\n    \\\"longitude\\\": 77.6702731,\\r\\n    \\\"speed\\\": 0\\r\\n  },\\r\\n  \\\"mocked\\\": false,\\r\\n  \\\"timestamp\\\": 1518584082935\\r\\n}\",\n" +
            "    \"isBase64Encoded\": false\n" +
            "}";

    //@Test
    public void testSendMessage() throws Exception {
        ApiGatewayKinesisPostingLambda kinesisPostingLambda = new ApiGatewayKinesisPostingLambda();
        InputStream inputStream = new ByteArrayInputStream(HTTP_POST.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();
        kinesisPostingLambda.handleRequest(inputStream, outputStream, null);
    }
}
