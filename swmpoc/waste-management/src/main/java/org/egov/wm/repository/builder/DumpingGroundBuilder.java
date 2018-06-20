package org.egov.wm.repository.builder;

import org.egov.wm.model.DumpingGround;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Service
public class DumpingGroundBuilder {
	
	@Value("${cassandra.keyspace}")
    private String cassandraKeySpace;
	
	public static final Logger logger = LoggerFactory.getLogger(DumpingGroundBuilder.class);
	
	public String createQuery(DumpingGround dumpingGround) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String query=new String();
		try {
			JSONObject dumpingGroundJson= new JSONObject(ow.writeValueAsString(dumpingGround));
			JSONObject auditInfoJson= new JSONObject(ow.writeValueAsString(dumpingGround.getAuditInfo()));
			dumpingGroundJson.remove("auditInfo");
			StringBuilder auditInfoSb=new StringBuilder(auditInfoJson.toString());
			StringBuilder dumpingGroundSb=new StringBuilder(dumpingGroundJson.toString());
			auditInfoSb.deleteCharAt(0);
			dumpingGroundSb.deleteCharAt(dumpingGroundSb.length() - 1);
			dumpingGroundSb.append(",").append(auditInfoSb);
			
			StringBuilder querySb = new StringBuilder("INSERT INTO ")
			          .append(cassandraKeySpace).append(".dumpingGround_info").append(" JSON ")
			          .append("'")
			          .append(dumpingGroundSb.toString())
			          .append("';");
			query = querySb.toString();
		}catch(Exception e) {
			logger.info("processing failed");
		}
		
		return query;
	}
}
