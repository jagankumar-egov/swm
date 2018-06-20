package org.egov.wm.repository.builder;

import org.egov.wm.model.CollectionPoint;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Service
public class CollectionPointBuilder {
	
	@Value("${cassandra.keyspace}")
    private String cassandraKeySpace;
	
	public static final Logger logger = LoggerFactory.getLogger(CollectionPointBuilder.class);
	
	public String createQuery(CollectionPoint collectionPoint) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String query=new String();
		try {
			JSONObject collectionPointJson= new JSONObject(ow.writeValueAsString(collectionPoint));
			JSONObject auditInfoJson= new JSONObject(ow.writeValueAsString(collectionPoint.getAuditInfo()));
			collectionPointJson.remove("auditInfo");
			StringBuilder auditInfoSb=new StringBuilder(auditInfoJson.toString());
			StringBuilder collectionPointSb=new StringBuilder(collectionPointJson.toString());
			auditInfoSb.deleteCharAt(0);
			collectionPointSb.deleteCharAt(collectionPointSb.length() - 1);
			collectionPointSb.append(",").append(auditInfoSb);
			
			StringBuilder querySb = new StringBuilder("INSERT INTO ")
			          .append(cassandraKeySpace).append(".collectionPoint_info").append(" JSON ")
			          .append("'")
			          .append(collectionPointSb.toString())
			          .append("';");
			query = querySb.toString();
		}catch(Exception e) {
			logger.info("processing failed");
		}
		
		return query;
	}
}
