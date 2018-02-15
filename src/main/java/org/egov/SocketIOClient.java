package org.egov;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class SocketIOClient {

    private static final Logger LOG = Logger.getLogger(String.valueOf(SocketIOClient.class));

    public static final String SOCKETIO_SERVER = "SOCKETIO_SERVER";

    public static final String _serverURL = System.getenv(SOCKETIO_SERVER);

    public static void SendMessage(Object message) throws TimeoutException {
        LOG.info("SENDING MESSAGE to " + _serverURL);
        Socket socket;
        try {
            socket = IO.socket(_serverURL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        socket.connect();
        socket.emit("data", message);
        // The socket connection happens asynchronously on a background thread,
        // and if the thread isn't connected, the message to send gets put into
        // a queue. Because it's a background thread, it won't keep the app
        // running until it completes. So, we need to wait for the socket to
        // connect, at which point we can reasonably assume our message has been
        // sent, and then we can disconnect
        int i = 0;
        while (!socket.connected())
        {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            i++;
            if (i > 200) {
                throw new TimeoutException("Timed out waiting for socketIO to connect");
            }
        }
        socket.disconnect();
        LOG.info("SENT MESSAGE");
    }
}
