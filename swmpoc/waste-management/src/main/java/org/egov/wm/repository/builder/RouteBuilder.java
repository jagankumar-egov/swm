package org.egov.wm.repository.builder;

import org.egov.wm.model.Route;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Service
public class RouteBuilder {

	@Value("${cassandra.keyspace}")
    private String cassandraKeySpace;

	public static final Logger logger = LoggerFactory.getLogger(RouteBuilder.class);

	public String createRouteQuery(Route route) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String query = new String();
		try {
			JSONObject driverInfoJson= new JSONObject(ow.writeValueAsString(route));
			JSONObject auditInfoJson= new JSONObject(ow.writeValueAsString(route.getAuditInfo()));
			driverInfoJson.remove("auditInfo");
			StringBuilder routeSb=new StringBuilder(driverInfoJson.toString());
			StringBuilder auditInfoSb=new StringBuilder(auditInfoJson.toString());
			auditInfoSb.deleteCharAt(0);
			routeSb.deleteCharAt(routeSb.length() - 1);
			routeSb.append(",").append(auditInfoSb);
			
			StringBuilder querySb = new StringBuilder("INSERT INTO ")
			          .append(cassandraKeySpace).append(".route_info").append(" JSON ")
			          .append("'")
			          .append(routeSb.toString())
			          .append("';");
			query = querySb.toString();
		}catch(Exception e) {
			logger.info("processing failed");
		}
		
		return query;
	}

}
