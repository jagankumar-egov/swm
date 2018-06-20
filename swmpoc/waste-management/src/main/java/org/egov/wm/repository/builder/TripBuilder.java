package org.egov.wm.repository.builder;

import org.egov.wm.model.Trip;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Service
public class TripBuilder {

	@Value("${cassandra.keyspace}")
    private String cassandraKeySpace;

	public static final Logger logger = LoggerFactory.getLogger(TripBuilder.class);

	public String createTripQuery(Trip trip) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String query = new String();
		try {
			JSONObject driverInfoJson= new JSONObject(ow.writeValueAsString(trip));
			JSONObject auditInfoJson= new JSONObject(ow.writeValueAsString(trip.getAuditInfo()));
			driverInfoJson.remove("auditInfo");
			StringBuilder tripSb=new StringBuilder(driverInfoJson.toString());
			StringBuilder auditInfoSb=new StringBuilder(auditInfoJson.toString());
			auditInfoSb.deleteCharAt(0);
			tripSb.deleteCharAt(tripSb.length() - 1);
			tripSb.append(",").append(auditInfoSb);
			
			StringBuilder querySb = new StringBuilder("INSERT INTO ")
			          .append(cassandraKeySpace).append(".trip_info").append(" JSON ")
			          .append("'")
			          .append(tripSb.toString())
			          .append("';");
			query = querySb.toString();
		}catch(Exception e) {
			logger.info("processing failed");
		}
		
		return query;
	}

}
