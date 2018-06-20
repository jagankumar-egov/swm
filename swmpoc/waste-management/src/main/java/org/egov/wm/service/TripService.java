package org.egov.wm.service;

import java.util.List;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wm.contract.VehicleInfoSearchCriteria;
import org.egov.wm.contract.TripRequest;
import org.egov.wm.contract.TripResponse;
import org.egov.wm.model.AuditInfo;
import org.egov.wm.model.Trip;
import org.egov.wm.producer.WMProducer;
import org.egov.wm.repository.CassandraRepository;
import org.egov.wm.utils.ResponseInfoFactory;
import org.egov.wm.utils.WMUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.datastax.driver.core.utils.UUIDs;

@org.springframework.stereotype.Service
public class TripService {
	
	@Value("${kafka.topics.save.trip}")
	private String saveTopic;
	
	@Autowired
	private WMProducer wMProducer;
	
	@Autowired
	private CassandraRepository cassandraRepository;
	
	@Autowired
	private ResponseInfoFactory factory;
	
	@Autowired
	private WMUtils utils;

	public static final Logger logger = LoggerFactory.getLogger(TripService.class);
	
	public TripResponse create(TripRequest tripRequest) {
		enrichserviceRequestForcreate(tripRequest);
		wMProducer.push(saveTopic, tripRequest);

		return getServiceResponse(tripRequest);
	}
	
	private void enrichserviceRequestForcreate(TripRequest tripRequest) {

		RequestInfo requestInfo = tripRequest.getRequestInfo();
		List<Trip> trips = tripRequest.getTrips();
		AuditInfo auditInfo = utils.getAuditInfo(String.valueOf(requestInfo.getUserInfo().getId()), true);

		for(Trip trip:trips) {
			trip.setAuditInfo(auditInfo);
			UUID id=UUIDs.timeBased();
			trip.setId(id);
		}

	}
	
	private void enrichserviceRequestForupdate(TripRequest tripRequest) {

		RequestInfo requestInfo = tripRequest.getRequestInfo();
		List<Trip> trips = tripRequest.getTrips();
		AuditInfo auditInfo = utils.getAuditInfo(String.valueOf(requestInfo.getUserInfo().getId()), true);

		for(Trip trip:trips) {
			trip.setAuditInfo(auditInfo);
		}

	}
	
	public TripResponse update(TripRequest tripRequest) {
		enrichserviceRequestForupdate(tripRequest);
		wMProducer.push(saveTopic, tripRequest);

		return getServiceResponse(tripRequest);
	}
	
	public TripResponse getServiceResponse(TripRequest tripRequest) {

		return TripResponse.builder()
				.responseInfo(factory.createResponseInfoFromRequestInfo(tripRequest.getRequestInfo(), true))
				.trips(tripRequest.getTrips()).build();
	}

	public void insertTripIntoDb(TripRequest tripRequest) {
		List<Trip> trips = tripRequest.getTrips();
		for(Trip trip:trips) {
			cassandraRepository.insertTrip(trip);
		}
	}

	
	public Object getServiceRequestDetails(RequestInfo requestInfo, VehicleInfoSearchCriteria vehicleInfoSearchCriteria) {
		return requestInfo;
	}
	

}
