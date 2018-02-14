package org.egov;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class CassandraConnector {
	 
	public static final Logger logger = LoggerFactory.getLogger(CassandraConnector.class);

    private Cluster cluster;
    
    private Session session;
 
    public void connect() {
    	logger.info("Connecting to Cassandra..");
        Builder b = Cluster.builder().addContactPoint("127.0.0.1");
        b.withPort(9042);
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
          .append("testKeySpace.test").append("(name, code) ")
          .append("VALUES ('").append(test.getName())
          .append("', '").append(test.getCode()).append("');");
     
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
}
