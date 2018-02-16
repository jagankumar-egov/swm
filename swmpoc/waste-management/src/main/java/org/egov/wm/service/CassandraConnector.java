package org.egov.wm.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.wm.model.Test;
import org.egov.wm.model.VehicleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

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
        StringBuilder sb = new StringBuilder("INSERT INTO ")
          .append(cassandraKeySpace).append(".tour_info").append("(id, accuracy, altitude, heading, latitude, "
          		+ "longitude, mocked, routecode, speed, timestamp, vehicleno) ")
          .append("VALUES (").append(vehicleInfo.getId())
          .append(", ").append(vehicleInfo.getCoords().getAccuracy())
          .append(", ").append(vehicleInfo.getCoords().getAltitude())
          .append(", ").append(vehicleInfo.getCoords().getHeading())
          .append(", ").append(vehicleInfo.getCoords().getLatitude())
          .append(", ").append(vehicleInfo.getCoords().getLongitude())
          .append(", ").append(vehicleInfo.getMocked())
          .append(", '").append(vehicleInfo.getRouteCode())
          .append("', ").append(vehicleInfo.getCoords().getSpeed())
          .append(", ").append(vehicleInfo.getTimestamp())
          .append(", '").append(vehicleInfo.getVehicleNo())
          .append("');");
     
        String query = sb.toString();
        logger.info("Cassandra query: "+query);
        session.execute(query);
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
	     
	     StringBuilder query = new StringBuilder("CREATE TABLE tour_info "
	     		+ "(id uuid PRIMARY KEY, vehicleNo text, routeCode text, accuracy double, "
	     		+ "altitude double, heading double, latitude double, longitude double, speed double, mocked boolean, timestamp double)");
	        
	     session.execute(sb.toString());
	     logger.info("Successfully created key space");
	     
	     session.execute(use);
	     logger.info("Using keyspace WMKeyspace");

	     session.execute(query.toString());
	     logger.info("Successfully created table tour_info");

    }
}
