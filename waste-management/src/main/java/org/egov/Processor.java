package org.egov;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import javax.annotation.PostConstruct;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;


@Component
@Order(1)
public class Processor{

	@Autowired
	public static ResourceLoader resourceLoader;
	        
    @Autowired
    private static Environment env;
      
	
	public static final Logger logger = LoggerFactory.getLogger(Processor.class);
	
    @PostConstruct      
    public void kafkaStreamProcessor(){
        Properties props = new Properties();
    	CassandraConnector cassandraConnector = new CassandraConnector();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        
        logger.info("Into the method");
        final StreamsBuilder builder = new StreamsBuilder();
 
        KStream<String, String> source = builder.stream("jsontest");
        source.to("vishal");
        source.foreach(new ForeachAction<String, String>() {
            public void apply(String key, String value) {
                logger.info(key + ": " + value);
                transformDataAndPersist(cassandraConnector, value);
            }
         });        
        
        final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, props);
        try {
            streams.start();
        } catch (Throwable e) {
        	logger.error("Exception while starting stream: ",e);
        }	
    }
    
    private void transformDataAndPersist(CassandraConnector cassandraConnector, String value) {
    	logger.info("Transforming Data...");
    	ObjectMapper mapper = new ObjectMapper();
    	Map<String, String> map = new HashMap<>();
    	try {
    		map = mapper.readValue(value, Map.class);
    	}catch(Exception e) {
    		logger.error("Couldn't convert to map");
            cassandraConnector.connect();
            cassandraConnector.insertTest(new Test(value, value));
    	}
        cassandraConnector.connect();
        for(String key: map.keySet()) {
            cassandraConnector.insertTest(new Test(key, map.get(key)));
        }
    	
    }
   
}
