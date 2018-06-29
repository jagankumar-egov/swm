package org.egov.wm.service;

import java.util.UUID;

import org.egov.wm.model.Track;
import org.egov.wm.repository.CassandraRepository;
import org.springframework.beans.factory.annotation.Autowired;


@org.springframework.stereotype.Service
public class TrackService {
	
	@Autowired
	private CassandraRepository cassandraRepository; 
	
	public void insertTrackInfoIntoDb(Track track) {
		track.setId(UUID.randomUUID());	
		cassandraRepository.insertTrackInfo(track);
	}
}
