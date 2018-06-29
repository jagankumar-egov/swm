package org.egov.wm.repository;

import org.egov.wm.cassandra.CassandraConnect;
import org.egov.wm.contract.DriverInfoSearchCriteria;
import org.egov.wm.contract.DriverResponse;
import org.egov.wm.contract.VehicleInfoSearchCriteria;
import org.egov.wm.model.CollectionPoint;
import org.egov.wm.model.DriverInfo;
import org.egov.wm.model.DumpingGround;
import org.egov.wm.model.Route;
import org.egov.wm.model.Track;
import org.egov.wm.model.Trip;
import org.egov.wm.model.VehicleInfo;
import org.egov.wm.repository.builder.CollectionPointBuilder;
import org.egov.wm.repository.builder.DriverInfoBuilder;
import org.egov.wm.repository.builder.DumpingGroundBuilder;
import org.egov.wm.repository.builder.RouteBuilder;
import org.egov.wm.repository.builder.TrackBuilder;
import org.egov.wm.repository.builder.TripBuilder;
import org.egov.wm.repository.builder.VehicleInfoBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class CassandraRepository {
	
	@Autowired
	public DriverInfoBuilder driverInfoBuilder;
	
	@Autowired
	public CollectionPointBuilder collectionPointBuilder;
	
	@Autowired
	public DumpingGroundBuilder dumpingGroundBuilder;
	
	@Autowired
	public VehicleInfoBuilder vehicleInfoBuilder;
	
	@Autowired
	public RouteBuilder routeBuilder;
	
	@Autowired
	public TripBuilder tripBuilder;
	
	@Autowired
	public TrackBuilder trackBuilder;
	
	@Autowired
	public CassandraConnect cassandraConnect;
	
	public static final Logger logger = LoggerFactory.getLogger(CassandraRepository.class);
	
	public void insertVehicleInfo(VehicleInfo vehicleInfo) {
		String query=vehicleInfoBuilder.createVehicleInfoQuery(vehicleInfo);
		Session session=cassandraConnect.getSession();
		session.execute(query);
		logger.info("executed query: "+query);
	}

	public void insertRoute(Route route) {
		String query=routeBuilder.createRouteQuery(route);
		Session session=cassandraConnect.getSession();
		session.execute(query);
		logger.info("executed query: "+query);
	}
	
	public void insertTrip(Trip trip) {
		String query=tripBuilder.createTripQuery(trip);
		Session session=cassandraConnect.getSession();
		session.execute(query);
		logger.info("executed query: "+query);
	}
	

	public void insertDriverInfo(DriverInfo driverInfo) {
		String query=driverInfoBuilder.createDriverInfoQuery(driverInfo);
		Session session=cassandraConnect.getSession();
		session.execute(query);
		logger.info("executed query: "+query);
	}
	
	public void getDriverInfo(DriverInfoSearchCriteria driverInfoSearchCriteria) {
		ObjectMapper mapper = new ObjectMapper();
		String query=driverInfoBuilder.createDriverInfoSearchQuery(driverInfoSearchCriteria);
		Session session=cassandraConnect.getSession();
		ResultSet rs=session.execute(query);
		logger.info("Resultset: "+mapper.convertValue(rs, DriverResponse.class));
	}
	
	public void getVehicleInfo(VehicleInfoSearchCriteria vehicleInfoSearchCriteria) {
		ObjectMapper mapper = new ObjectMapper();
		String query=vehicleInfoBuilder.createVehicleInfoSearchQuery(vehicleInfoSearchCriteria);
		Session session=cassandraConnect.getSession();
		ResultSet rs=session.execute(query);
		logger.info("Resultset: "+mapper.convertValue(rs, DriverResponse.class));
	}
	
	public void insertCollectionPoint(CollectionPoint collectionPoint) {
		String query=collectionPointBuilder.createQuery(collectionPoint);
		Session session=cassandraConnect.getSession();
		session.execute(query);
		logger.info("executed query: "+query);
	}

	public void insertDumpingGround(DumpingGround dumpingGround) {
		String query=dumpingGroundBuilder.createQuery(dumpingGround);
		Session session=cassandraConnect.getSession();
		session.execute(query);
		logger.info("executed query: "+query);
	}

	public void insertTrackInfo(Track track) {
		String query=trackBuilder.createQuery(track);
		Session session=cassandraConnect.getSession();
		session.execute(query);
		logger.info("executed query: "+query);
	}
}
