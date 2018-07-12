package org.egov.wm.repository.builder;

import org.egov.wm.contract.DriverInfoSearchCriteria;
import org.egov.wm.model.DriverInfo;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Service
public class DriverInfoBuilder {
	
	@Value("${cassandra.keyspace}")
    private String cassandraKeySpace;

	public static final Logger logger = LoggerFactory.getLogger(DriverInfoBuilder.class);
	
	public String createDriverInfoQuery(DriverInfo driverInfo) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String query = new String();
		try {
			JSONObject driverInfoJson= new JSONObject(ow.writeValueAsString(driverInfo));
			JSONObject auditInfoJson= new JSONObject(ow.writeValueAsString(driverInfo.getAuditInfo()));
			driverInfoJson.remove("auditInfo");
			StringBuilder driverInfoSb=new StringBuilder(driverInfoJson.toString());
			StringBuilder auditInfoSb=new StringBuilder(auditInfoJson.toString());
			auditInfoSb.deleteCharAt(0);
			driverInfoSb.deleteCharAt(driverInfoSb.length() - 1);
			driverInfoSb.append(",").append(auditInfoSb);
			
			StringBuilder querySb = new StringBuilder("INSERT INTO ")
			          .append(cassandraKeySpace).append(".driver_info").append(" JSON ")
			          .append("'")
			          .append(driverInfoSb.toString())
			          .append("';");
			query = querySb.toString();
		}catch(Exception e) {
			logger.info("processing failed");
		}
		
		return query;
	}
	
	public String createDriverInfoSearchQuery(DriverInfoSearchCriteria criteria) {
		String query = new String();
		try {	
			StringBuilder querySb=new StringBuilder("SELECT JSON * FROM ")
					 .append(cassandraKeySpace).append(".driver_info ");
			if (criteria.getId()!=null)
				querySb.append("WHERE id=").append(criteria.getId());
			//if(criteria.getPhoneNo()!=null)
				//querySb.append("AND phoneNo='").append(criteria.getPhoneNo()).append("' AND ");
			//if (criteria.getName()!=null)
				//querySb.append("name='").append(criteria.getName()).append("' AND ");
			//querySb.append("tenantId='").append(criteria.getTenantId()).append("'")
			//		 .append(" ALLOW FILTERING;");
			querySb.append(";");
			query = querySb.toString();
			logger.info("\n\nquery:\n"+query+"\n\n");
		}catch(Exception e) {
			logger.info("processing failed");
		}	
		return query;			
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}