package org.egov.wm.consumer;

import java.util.HashMap;

import org.egov.wm.contract.VehicleRequest;
import org.egov.wm.service.VehicleService;
import org.egov.wm.contract.CollectionPointRequest;
import org.egov.wm.contract.DriverRequest;
import org.egov.wm.contract.DumpingGroundRequest;
import org.egov.wm.contract.RouteRequest;
import org.egov.wm.contract.TripRequest;
import org.egov.wm.service.CollectionPointService;
import org.egov.wm.service.DriverService;
import org.egov.wm.service.DumpingGroundService;
import org.egov.wm.service.RouteService;
import org.egov.wm.service.TripService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

import com.fasterxml.jackson.databind.ObjectMapper;

@org.springframework.stereotype.Service
public class WMConsumer {
	
	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private RouteService routeService;
	
	@Autowired
	private TripService tripService;
	
	@Autowired
	private DriverService driverService;

	@Autowired
	private CollectionPointService collectionPointService;  
	
	@Autowired
	private DumpingGroundService dumpingGroundService;  
	
	public static final Logger logger = LoggerFactory.getLogger(WMConsumer.class);

	
	@KafkaListener(topics = {"${kafka.topics.save.driverInfo}", "${kafka.topics.save.collectionPoint}",
			"${kafka.topics.save.dumpingGround}","${kafka.topics.save.vehicleInfo}",
			"${kafka.topics.save.route}","${kafka.topics.save.trip}"})
	public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		ObjectMapper mapper = new ObjectMapper();
		if(topic.equals("vehicleInfo-create")) {
			VehicleRequest vehicleRequest = new VehicleRequest();
			try {
				logger.info("Consuming record: " + record);
				vehicleRequest = mapper.convertValue(record, VehicleRequest.class);
			} catch (final Exception e) {
				logger.error("Error while listening to value: " + record + " on topic: " + topic + ": " + e);
			}
			logger.info("Vehicle Consumption successful!");
			vehicleService.insertVehicleInfoIntoDb(vehicleRequest);
		}
		else if(topic.equals("route-create")) {
			RouteRequest routeRequest = new RouteRequest();
			try {
				logger.info("Consuming record: " + record);
				routeRequest = mapper.convertValue(record, RouteRequest.class);
			} catch (final Exception e) {
				logger.error("Error while listening to value: " + record + " on topic: " + topic + ": " + e);
			}
			logger.info("Route Consumption successful!");
			routeService.insertRouteIntoDb(routeRequest);
		}
		else if(topic.equals("trip-create")) {
			TripRequest tripRequest = new TripRequest();
			try {
				logger.info("Consuming record: " + record);
				tripRequest = mapper.convertValue(record, TripRequest.class);
			} catch (final Exception e) {
				logger.error("Error while listening to value: " + record + " on topic: " + topic + ": " + e);
			}
			logger.info("Trip Consumption successful!");
			tripService.insertTripIntoDb(tripRequest);
		}
		else if (topic.equals("driverInfo-create")) {
			DriverRequest driverRequest = new DriverRequest();
			try {
				logger.info("Consuming record: " + record);
				driverRequest = mapper.convertValue(record, DriverRequest.class);
			} catch (final Exception e) {
				logger.error("Error while listening to value: " + record + " on topic: " + topic + ": " + e);
			}
			
			logger.info("Consumption successful!");
			driverService.insertDriverInfoIntoDb(driverRequest);
	}
	else if (topic.equals("collectionPoint-create")) {
		CollectionPointRequest collectionPointRequest= new CollectionPointRequest();
		try {
			logger.info("Consuming record: " + record);
			collectionPointRequest=mapper.convertValue(record, CollectionPointRequest.class);
		}catch (final Exception e) {
			logger.error("Error while listening to value: " + record + " on topic: " + topic + ": " + e);
		}
		
		logger.info("Consumption successful!");
		 collectionPointService.insertCollectionPointIntoDb(collectionPointRequest);
	}
	else if (topic.equals("dumpingGround-create")) {
		DumpingGroundRequest dumpingGroundRequest= new DumpingGroundRequest();
		try {
			logger.info("Consuming record: " + record);
			dumpingGroundRequest=mapper.convertValue(record, DumpingGroundRequest.class);
		}catch (final Exception e) {
			logger.error("Error while listening to value: " + record + " on topic: " + topic + ": " + e);
		}
		
		logger.info("Consumption successful!");
		 dumpingGroundService.insertDumpingGroundIntoDb(dumpingGroundRequest);
	}
	}
}
