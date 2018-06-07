package org.egov.wm.service;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.apache.kafka.streams.kstream.KStream;
import org.egov.wm.model.VehicleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;


import com.google.gson.Gson;


@Component
@Order(1)
public class Processor{

	@Autowired
	public static ResourceLoader resourceLoader;
	        
    @Autowired
    private static Environment env;
      
	@Autowired
	private SocketIO socketIO;
	
	@Autowired
	private CassandraConnector cassandraConnector;
	
	@Value("${spring.kafka.bootstrap.servers}")
	private String bootStrapServer;
	
	@Value("${kafka.stream.in.topic}")
	private String kafkaInStreamTopic;
	
	@Value("${kafka.stream.cassandra.topic}")
	private String kafkaStreamCassandraTopic;
	
	public static final Logger logger = LoggerFactory.getLogger(Processor.class);
	
    @PostConstruct  
    public void kafkaStreamProcessor(){
    	
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "wm-kafka-streams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.BUFFERED_RECORDS_PER_PARTITION_CONFIG, 500);
        props.put(StreamsConfig.producerPrefix(ProducerConfig.MAX_BLOCK_MS_CONFIG), 1000);
        
        

        
        logger.info("Into the method");
        final StreamsBuilder builder = new StreamsBuilder();
 
        KStream<String, String> source = builder.stream(kafkaInStreamTopic);//Topic
        source.to(kafkaStreamCassandraTopic);//Topic
        
        source.foreach(new ForeachAction<String, String>() {
            public void apply(String key, String value) {
                logger.info("\n\n\n\n processor info\n kafkaStreamCassandraTopic : "+kafkaStreamCassandraTopic+"\n kafkaInStreamTopic: "+kafkaInStreamTopic+ key + ": " + value+"\n\n\n\n");
                try {
                	socketIO.pushToSocketIO(key, value);
                	logger.info("\n\n\n\n$$$ pushing to socket\n\n\n\n");
                }catch(Exception e) {
                	logger.error("Couldn't post to socketIO: ",e);
                    transformDataAndPersist(cassandraConnector, value, kafkaInStreamTopic);
                }
                transformDataAndPersist(cassandraConnector, value, kafkaInStreamTopic);
            }
         });        
        
        final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, props);
        try {
            streams.start();
        } catch (Throwable e) {
        	logger.error("Exception while starting stream: ",e);
        }
        //cassandraConnector.connect();
    }
    
    private void transformDataAndPersist(CassandraConnector cassandraConnector, String value, String topic) {
    	logger.info("Transforming Data...");
    	VehicleInfo vehicleInfo = new VehicleInfo();
    	try {
    		Gson gson = new Gson(); 
    		if (topic.equals("vehicle.info")) {
    			vehicleInfo = gson.fromJson(value, VehicleInfo.class);
    			cassandraConnector.connect();
    	        cassandraConnector.insertVehicleInfo(vehicleInfo);
    		}
    		//logger.info("\nProcessor :\n str_value: "+value+"\n"+"\n lat: "+ vehicleInfo.getCoords().getLatitude()+"\n\n");
    	}catch(Exception e) {
    		logger.error("Couldn't convert to VehicleInfo: ",e);
            cassandraConnector.connect();
            cassandraConnector.insertVehicleInfo(vehicleInfo);
    	}
        	
    }
}