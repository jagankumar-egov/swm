package org.egov;


import java.net.URISyntaxException;

import javax.annotation.PostConstruct;

import org.egov.wm.service.SocketIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


@SpringBootApplication
@Configuration
@PropertySource("classpath:application.properties")
@EnableAutoConfiguration(exclude={CassandraDataAutoConfiguration.class})
public class WasteManagementApp
{	
    @Autowired
    private static Environment env;
    
	public static final Logger logger = LoggerFactory.getLogger(WasteManagementApp.class);
    
	@Value("${socket.io.host}")
	private String socketHost;
	
	@Value("${socket.io.namespace}")
	private String socketNamespace;
	
	public static Socket socket = null;
        
    public void setEnvironment(final Environment env) {
    	WasteManagementApp.env = env;
    }
	
	public static void main(String[] args) {
		SpringApplication.run(WasteManagementApp.class, args);
		
	}    

	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
	private static ObjectMapper getMapperConfig() {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper;
	}
	
	@PostConstruct
	public void connectSocket(){
		IO.Options opts = new IO.Options();
		opts.forceNew = true;
		opts.reconnection = true;
		opts.timeout = 100 * 1000;
		opts.reconnectionAttempts = 1000;
		opts.reconnectionDelay = 0;
		try {
			logger.info("Connecting to the socket at.......: "+socketHost+socketNamespace);
			final Socket socket = IO.socket(socketHost+socketNamespace, opts);
			this.socket = socket;
		}catch(Exception e) {
			logger.error("socket connection failed!: ",e);
		}
		
	}
	
	public Socket getSocket() {
		socket.on("message.event", new Emitter.Listener() {
	        @Override
	        public void call(Object... args) {
	            logger.info("addedmessage.event");
	            socket.emit("message.event", args);
	        }
	    });
		return this.socket;
	}
}
