package org.egov.wm.service;

import java.io.*;
import java.net.*;

import javax.annotation.PostConstruct;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

@Service
public class SocketClient {
	@Value("${socket.io.host}")
	private String socketHost;
	
	@Value("${socket.io.namespace}")
	private String socketNamespace;

	@Value("${socket.default.room}")
	private String socketRoom;
	
    private Socket socket = null;
    private String address="127.0.0.1";
    private int port=3005;
    public static final Logger logger = LoggerFactory.getLogger(SocketClient.class);
    @PostConstruct
    public void connect() {
    	IO.Options opts = new IO.Options();
		opts.forceNew = true;
		opts.reconnection = true;
		opts.timeout = 100 * 1000;
		opts.reconnectionAttempts = 1000;
		opts.reconnectionDelay = 0;
		try {
			logger.info("Client Connecting to the socket at.......: "+socketHost+socketNamespace);
			socket= IO.socket(socketHost+socketNamespace, opts);
			//this.socket = socket;
		}catch(Exception e) {
			logger.error("client-socket connection failed!: ",e);
		}
		
    }
    public void listen() {
    	logger.info("step1");
		socket.on(io.socket.client.Socket.EVENT_CONNECT, new Emitter.Listener() {
	        @Override
	        public void call(Object... args) {
	    		logger.info("client-EVENT_CONNECT");
	            logger.info("client-Message Received: ");
	            for (int i = 0; i < args.length; i++) {
	            	logger.info(args[i].toString());
	            }
	            logger.info("\n#eom\n"); 
	        }

	    }).on(io.socket.client.Socket.EVENT_MESSAGE, new Emitter.Listener() {
	        @Override
	        public void call(Object... args) {
	    		logger.info("client-EVENT_MESSAGE");
	            logger.info("client-Message Received: ");
	            for (int i = 0; i < args.length; i++) {
	            	logger.info(args[i].toString());
	            }
	        }

	    });
	    socket.connect();
	    logger.info("step2");
    }
}
