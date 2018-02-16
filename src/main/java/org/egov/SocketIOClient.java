package org.egov;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class SocketIOClient {

    private static final Logger LOG = Logger.getLogger(String.valueOf(SocketIOClient.class));

    public static final String SOCKETIO_SERVER = "SOCKETIO_SERVER";

    public static final String _serverURL = System.getenv(SOCKETIO_SERVER);
    private static final URI _socketURI = convertToUri(_serverURL);
    private static URI convertToUri(String url)
    {
            try {
                return new URI(url);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
    }

    public static void SendMessage(Object message) throws TimeoutException {
        LOG.info("SENDING MESSAGE to " + _serverURL);
        Socket socket;
        CookieManager cookieManager = new CookieManager();
        socket = IO.socket(_socketURI);
        // This is a bit of an abomination because the socketio maintainer refuses to include
        // proper cookie support, so we're left to build it in ourselves.
        // See https://github.com/socketio/socket.io-client-java/issues/32
        // Cookie support is needed if we're going to load balance between multiple SocketIO
        // servers and rely on cookies for session stickiness (which is what AWS ALBs do)
        // https://socket.io/docs/using-multiple-nodes/#sticky-load-balancing only suggests
        // creating stickiness based upon source IP
        socket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Transport transport = (Transport)args[0];
                transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        Map<String, List<String>> headers = (Map<String, List<String>>)args[0];
                        Map<String, List<String>> cookieHeaders;
                        try {
                            cookieHeaders = cookieManager.get(_socketURI, headers);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        for(Map.Entry<String, List<String>> entry : cookieHeaders.entrySet()) {
                            headers.put(entry.getKey(), entry.getValue());
                        }
                    }
                }).on(Transport.EVENT_RESPONSE_HEADERS, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        Map<String, List<String>> headers = (Map<String, List<String>>)args[0];
                        try {
                            cookieManager.put(_socketURI, headers);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
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
