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

    private static final Logger LOG = Logger.getLogger(String.valueOf(SocketIOClientTest.class));

    String SOCKETIO_SERVER = "http://serverless-java-dev-sl-Pub-LB-892070502.ap-southeast-1.elb.amazonaws.com:3005/location";

    @Test
    public void testSendMessage() throws Exception {
        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getenv(SocketIOClient.SOCKETIO_SERVER)).thenReturn(SOCKETIO_SERVER);
        SocketIOClient.SendMessage("{}"); // TODO: stop testing external dependencies in unit test
    }
}
