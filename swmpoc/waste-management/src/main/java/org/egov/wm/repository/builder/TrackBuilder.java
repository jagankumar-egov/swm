package org.egov.wm.repository.builder;

import org.egov.wm.model.Track;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Service
public class TrackBuilder {
	
	@Value("${cassandra.keyspace}")
    private String cassandraKeySpace;

	public static final Logger logger = LoggerFactory.getLogger(TrackBuilder.class);
	
	public String createQuery(Track track) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String query = new String();
		try {
			JSONObject trackJson= new JSONObject(ow.writeValueAsString(track));
			JSONObject coordsJson= new JSONObject(ow.writeValueAsString(track.getCoords()));
			trackJson.remove("coords");
			StringBuilder trackSb=new StringBuilder(trackJson.toString());
			StringBuilder coordsSb=new StringBuilder(coordsJson.toString());
			coordsSb.deleteCharAt(0);
			trackSb.deleteCharAt(trackSb.length() - 1);
			trackSb.append(",").append(coordsSb);
			
			StringBuilder querySb = new StringBuilder("INSERT INTO ")
			          .append(cassandraKeySpace).append(".track_info").append(" JSON ")
			          .append("'")
			          .append(trackSb.toString())
			          .append("';");
			query = querySb.toString();
			logger.info("\n\nquery: "+ query);
		}catch(Exception e) {
			logger.info("processing failed");
		}
		
		return query;
	}	
}