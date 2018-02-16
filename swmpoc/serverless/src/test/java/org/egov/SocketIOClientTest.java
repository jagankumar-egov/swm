package org.egov;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.logging.Logger;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SocketIOClient.class)
// The PowerMockIgnore is needed because otherwise PowerMockito screws up an
// internal HTTP (okhttp3) library used by SocketIO
// See https://stackoverflow.com/a/23937192
@PowerMockIgnore("javax.net.ssl.*")
public class SocketIOClientTest {
    /* NB: This client exists solely for debugging purposes (e.g., via IntelliJ) rather than as a
     * test to run as part of the build process. To use, you'll need to update the SOCKETIO_SERVER
     * below to point to your test SocketIO endpoint.
     * */

    private static final Logger LOG = Logger.getLogger(String.valueOf(SocketIOClientTest.class));

    String SOCKETIO_SERVER = "http://serverless-java-devjoel-Pub-LB-1976546250.ap-southeast-1.elb.amazonaws.com/location";

    String JSON_MESSAGE = "{\n" +
            "  \"vehicleNo\":\"123\",\n" +
            "  \"routeCode\":\"abc\",\n" +
            "  \"BatteryInfo\":{},\n" +
            "  \"coords\": {\n" +
            "    \"accuracy\": 9.64799976348877,\n" +
            "    \"altitude\": 815.634765625,\n" +
            "    \"heading\": 0,\n" +
            "    \"latitude\": 12.9187821,\n" +
            "    \"longitude\": 77.6702731,\n" +
            "    \"speed\": 0\n" +
            "  },\n" +
            "  \"mocked\": false,\n" +
            "  \"timestamp\": 1518584082935\n" +
            "}";

    //@Test
    public void testSendMessage() throws Exception {
        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getenv(SocketIOClient.SOCKETIO_SERVER)).thenReturn(SOCKETIO_SERVER);
        SocketIOClient.SendMessage(JSON_MESSAGE); // TODO: stop testing external dependencies in unit test
    }
}
