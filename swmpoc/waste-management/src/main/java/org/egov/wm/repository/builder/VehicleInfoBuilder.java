package org.egov.wm.repository.builder;

import org.egov.wm.contract.VehicleInfoSearchCriteria;
import org.egov.wm.model.VehicleInfo;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Service
public class VehicleInfoBuilder {
	
	@Value("${cassandra.keyspace}")
    private String cassandraKeySpace;

	public static final Logger logger = LoggerFactory.getLogger(VehicleInfoBuilder.class);

	public String createVehicleInfoQuery(VehicleInfo vehicleInfo) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String query = new String();
		try {
			JSONObject driverInfoJson= new JSONObject(ow.writeValueAsString(vehicleInfo));
			JSONObject auditInfoJson= new JSONObject(ow.writeValueAsString(vehicleInfo.getAuditInfo()));
			driverInfoJson.remove("auditInfo");
			StringBuilder vehicleInfoSb=new StringBuilder(driverInfoJson.toString());
			StringBuilder auditInfoSb=new StringBuilder(auditInfoJson.toString());
			auditInfoSb.deleteCharAt(0);
			vehicleInfoSb.deleteCharAt(vehicleInfoSb.length() - 1);
			vehicleInfoSb.append(",").append(auditInfoSb);
			
			StringBuilder querySb = new StringBuilder("INSERT INTO ")
			          .append(cassandraKeySpace).append(".vehicle_info").append(" JSON ")
			          .append("'")
			          .append(vehicleInfoSb.toString())
			          .append("';");
			query = querySb.toString();
		}catch(Exception e) {
			logger.info("processing failed");
		}
		
		return query;
	}

	public String createVehicleInfoSearchQuery(VehicleInfoSearchCriteria criteria) {
		String query = new String();
		try {	
			StringBuilder querySb=new StringBuilder("SELECT JSON * FROM ")
					 .append(cassandraKeySpace).append(".driver_info WHERE ");
			if (criteria.getId()!=null)
				querySb.append("name='").append(criteria.getId()).append("' AND ");
			if(criteria.getTenantId()!=null)
				querySb.append("phoneNo='").append(criteria.getTenantId()).append("' AND ");
			querySb.append("tenantId='").append(criteria.getTenantId()).append("'")
					 .append(" ALLOW FILTERING;");
			
			query = querySb.toString();
			logger.info("\n\nquery:\n"+query+"\n\n");
		}catch(Exception e) {
			logger.info("processing failed");
		}	
		return query;
	}

}
