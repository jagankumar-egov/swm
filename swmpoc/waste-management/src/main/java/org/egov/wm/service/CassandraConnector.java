package org.egov.wm.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.wm.model.Test;
import org.egov.wm.model.VehicleInfo;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Service
public class CassandraConnector {
	 
	public static final Logger logger = LoggerFactory.getLogger(CassandraConnector.class);

    private Cluster cluster;
    
    private Session session;
    
    @Value("${cassandra.contactpoints}")
    private String cassandraContactPoint;
    
    @Value("${cassandra.port}")
    private Integer cassandraPort;
    
    @Value("${cassandra.keyspace}")
    private String cassandraKeySpace;
 
    public void connect() {
    	logger.info("Connecting to Cassandra..");
        Builder b = Cluster.builder().addContactPoint(cassandraContactPoint);
        b.withPort(cassandraPort);
        cluster = b.build();
 
        session = cluster.connect();
    	logger.info("Connection to Cassandra successfull");
    }
 
    public Session getSession() {
        return this.session;
    }
 
    public void close() {
        session.close();
        cluster.close();
    }
    
    public void insertTest(Test test) {
        StringBuilder sb = new StringBuilder("INSERT INTO ")
          .append(cassandraKeySpace).append(".test").append("(name, code) ")
          .append("VALUES ('").append(test.getName())
          .append("', '").append(test.getCode()).append("');");
     
        String query = sb.toString();
        logger.info("Cassandra query: "+query);
        session.execute(query);
    }
    
    public void insertVehicleInfo(VehicleInfo vehicleInfo) {
    	try {
    		createKeySpace();
    	}catch(Exception e) {
    		logger.error("DB set up already exists");
    	}
    	ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    	try {
    		logger.info("\n\n\n In cassandra connecter\n\n\n");
    		JSONObject json1 = new JSONObject(ow.writeValueAsString(vehicleInfo));
    		String str1 = json1.toString();
    		StringBuilder sb = new StringBuilder("INSERT INTO ")
                    .append(cassandraKeySpace).append(".vehicle_info").append(" JSON ")
                    .append("'")
                    .append(str1)
                    .append("';");
    		String query = sb.toString();
    		logger.info("\n\n\n###Cassandra insertVehicleInfo query: "+query+"\n\n");
    	    session.execute(query);
		} catch (Exception e) {
			logger.error("Invalid VehicleInfo");
		}  
    }

    
    public List<Test> selectAll() {
        StringBuilder sb = 
          new StringBuilder("SELECT * FROM ").append("test");
     
        String query = sb.toString();
        ResultSet rs = session.execute(query);
     
        List<Test> tests = new ArrayList<Test>();
     
        rs.forEach(r -> {
            tests.add(new Test(
              r.getString("name"),  
              r.getString("code")));
        });
        return tests;
    }
    
    public void createKeySpace() {
	     StringBuilder sb = new StringBuilder("CREATE KEYSPACE WMKeyspace ")
	        		.append("WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 3}; ");
	     
	     String use = "use WMKeyspace";
	     
	     StringBuilder queryVehicleInfo = new StringBuilder("CREATE TABLE vehicle_info "
		     		+ "(id uuid PRIMARY KEY, driverId uuid, vehicleNo text, type text, capacity int, "
		     		+ "color text, owner text, purchaseDate text, company text, model text, "
		     		+"code text, tenantId text, createdBy text, createdTime double, lastModifiedBy text, lastModifiedTime double)");
	     
	     StringBuilder queryDriverInfo = new StringBuilder("CREATE TABLE driver_info "
		     		+ "(id uuid PRIMARY KEY, name text, code text, tenantId text, dateOfBirth double, "
		     		+"phoneNo double, aadhaarNo double, licenseNo double, bloodGroup text, emailId text, "
		     		+"createdBy text, createdTime double, lastModifiedBy text, lastModifiedTime double)");
	     
	     StringBuilder queryCollectonPointInfo = new StringBuilder("CREATE TABLE collectionPoint_info "
		     		+ "(id uuid PRIMARY KEY, binId uuid, latitude double, longitude double, "
		     		+"name text, ward text, street text, colony text, "
		     		+"code text, tenantId text, createdBy text, createdTime double, lastModifiedBy text, lastModifiedTime double)");
	     
	     StringBuilder queryDumpingGroundInfo = new StringBuilder("CREATE TABLE dumpingGround_info "
		     		+ "(id uuid PRIMARY KEY, binId uuid, latitude double, longitude double, "
		     		+"name text, ward text, street text, colony text, "
		     		+"code text, tenantId text, createdBy text, createdTime double, lastModifiedBy text, lastModifiedTime double)");
	     

	     StringBuilder queryRouteInfo = new StringBuilder("CREATE TABLE route_info "
		     		+ "(id uuid PRIMARY KEY, collectionPointIds list<uuid>, dumpingGroundIds list<uuid>, name text, "
		     		+"code text, tenantId text, createdBy text, createdTime double, lastModifiedBy text, lastModifiedTime double)");
	     
	     StringBuilder queryTripInfo = new StringBuilder("CREATE TABLE trip_info "
		     		+ "(id uuid PRIMARY KEY, routeId uuid, vehicleId uuid, status text, startTime double, "
		     		+"endTime double, createdBy text, createdTime double, lastModifiedBy text, lastModifiedTime double)");
	     
	     StringBuilder queryTrackInfo = new StringBuilder("CREATE TABLE track_info "
		     		+ "(id uuid PRIMARY KEY, tripId uuid, timeStamp long, "
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
}

