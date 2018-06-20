package org.egov.wm.cassandra;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Cluster.Builder;

@Service
public class CassandraConnect {
	public static final Logger logger = LoggerFactory.getLogger(CassandraConnect.class);

    private Cluster cluster;
    
    private Session session;
    
    @Value("${cassandra.contactpoints}")
    private String cassandraContactPoint;
    
    @Value("${cassandra.port}")
    private Integer cassandraPort;
    
    @Value("${cassandra.keyspace}")
    private String cassandraKeySpace;
    
    @PostConstruct
    public Session connect() {
    	logger.info("Connecting to Cassandra..");
        Builder b = Cluster.builder().addContactPoint(cassandraContactPoint);
        b.withPort(cassandraPort);
        cluster = b.build();
 
        session = cluster.connect();
    	logger.info("Connection to Cassandra successfull");
    	try {
    		createKeySpace();
    	}catch(Exception e) {
    		logger.error("DB set up already exists");
    	}
    	return session;
    }
    
    public void close() {
        session.close();
        cluster.close();
    }
    
    public void createKeySpace() {
	     StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS WMKeyspace ")
	        		.append("WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 3}; ");
	     
	     String use = "use WMKeyspace";
	     
	     StringBuilder queryDriverInfo = new StringBuilder("CREATE TABLE IF NOT EXISTS driver_info "
		     		+ "(id uuid, name text, code text, tenantId text, dateOfBirth double, "
		     		+"phoneNo double, aadhaarNo double, licenseNo double, bloodGroup text, emailId text, "
		     		+"createdBy text, createdTime double, lastModifiedBy text, lastModifiedTime double, PRIMARY KEY (id, name, phoneNo, tenantId))");
	     
	     StringBuilder queryVehicleInfo = new StringBuilder("CREATE TABLE IF NOT EXISTS vehicle_info "
		     		+ "(id uuid PRIMARY KEY, driverId uuid, vehicleNo text, type text, capacity int, "
		     		+ "color text, owner text, purchaseDate text, company text, model text, "
		     		+"code text, tenantId text, createdBy text, createdTime double, lastModifiedBy text, lastModifiedTime double)");
	     
	     StringBuilder queryCollectonPointInfo = new StringBuilder("CREATE TABLE IF NOT EXISTS collectionPoint_info "
		     		+ "(id uuid PRIMARY KEY, binId text, latitude double, longitude double, "
		     		+"name text, ward text, street text, colony text, "
		     		+"code text, tenantId text, createdBy text, createdTime double, lastModifiedBy text, lastModifiedTime double)");
	     
	     StringBuilder queryDumpingGroundInfo = new StringBuilder("CREATE TABLE IF NOT EXISTS dumpingGround_info "
		     		+ "(id uuid PRIMARY KEY, binId text, latitude double, longitude double, "
		     		+"name text, ward text, street text, colony text, "
		     		+"code text, tenantId text, createdBy text, createdTime double, lastModifiedBy text, lastModifiedTime double)");
	     

	     StringBuilder queryRouteInfo = new StringBuilder("CREATE TABLE IF NOT EXISTS route_info "
		     		+ "(id uuid PRIMARY KEY, collectionPointIds list<uuid>, dumpingGroundIds list<uuid>, name text, "
		     		+"code text, tenantId text, createdBy text, createdTime double, lastModifiedBy text, lastModifiedTime double)");
	     
	     StringBuilder queryTripInfo = new StringBuilder("CREATE TABLE IF NOT EXISTS trip_info "
		     		+ "(id uuid PRIMARY KEY, routeId uuid, vehicleId uuid, status text, startTime double, "
		     		+"endTime double, createdBy text, createdTime double, lastModifiedBy text, lastModifiedTime double)");
	     
	     StringBuilder queryTrackInfo = new StringBuilder("CREATE TABLE IF NOT EXISTS track_info "
		     		+ "(id uuid PRIMARY KEY, tripId uuid, timeStamp double, "
		     		+" latitude double, longitude double,  altitude double, accuracy double, "
		     		+"altitudeAccuracy double, speed int)");
	     
	     
	        
	     session.execute(sb.toString());
	     logger.info("Successfully created key space");
	     
	     session.execute(use);
	     logger.info("Using keyspace WMKeyspace");

	     session.execute(queryVehicleInfo.toString());
	     logger.info("Successfully created table vehicle_info");

	     session.execute(queryDriverInfo.toString());
	     logger.info("Successfully created table driver_info");

	     session.execute(queryCollectonPointInfo.toString());
	     logger.info("Successfully created table collectionPoint_info");

	     session.execute(queryDumpingGroundInfo.toString());
	     logger.info("Successfully created table dumpingGround_info");

	     session.execute(queryRouteInfo.toString());
	     logger.info("Successfully created table route_info");

	     session.execute(queryTripInfo.toString());
	     logger.info("Successfully created table trip_info");

	     session.execute(queryTrackInfo.toString());
	     logger.info("Successfully created table track_info");
	     
   }

	public Session getSession() {
		return session;
	}
}
