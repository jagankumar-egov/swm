package org.egov.wm.service;

import java.util.List;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wm.model.AuditInfo;
import org.egov.wm.model.VehicleInfo;
import org.egov.wm.contract.VehicleInfoSearchCriteria;
import org.egov.wm.contract.VehicleRequest;
import org.egov.wm.contract.VehicleResponse;
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
public class VehicleService {

	@Value("${kafka.topics.save.vehicleInfo}")
	private String saveTopic;
	
	@Autowired
	private WMProducer wMProducer;
	
	@Autowired
	private CassandraRepository cassandraRepository;
	
	@Autowired
	private ResponseInfoFactory factory;
	
	@Autowired
	private WMUtils utils;

	public static final Logger logger = LoggerFactory.getLogger(VehicleService.class);
	
	public VehicleResponse create(VehicleRequest vehicleRequest) {
		enrichserviceRequestForcreate(vehicleRequest);
		wMProducer.push(saveTopic, vehicleRequest);

		return getServiceResponse(vehicleRequest);
	}
	
	private void enrichserviceRequestForcreate(VehicleRequest vehicleRequest) {

		RequestInfo requestInfo = vehicleRequest.getRequestInfo();
		List<VehicleInfo> vehicles = vehicleRequest.getVehicles();
		AuditInfo auditInfo = utils.getAuditInfo(String.valueOf(requestInfo.getUserInfo().getId()), true);

		for(VehicleInfo vehicleInfo:vehicles) {
			vehicleInfo.setAuditInfo(auditInfo);
			UUID id=UUIDs.timeBased();
			vehicleInfo.setId(id);
		}

	}
	
	private void enrichserviceRequestForupdate(VehicleRequest vehicleRequest) {

		RequestInfo requestInfo = vehicleRequest.getRequestInfo();
		List<VehicleInfo> vehicles = vehicleRequest.getVehicles();
		AuditInfo auditInfo = utils.getAuditInfo(String.valueOf(requestInfo.getUserInfo().getId()), true);

		for(VehicleInfo vehicleInfo:vehicles) {
			vehicleInfo.setAuditInfo(auditInfo);
		}

	}
	
	public VehicleResponse update(VehicleRequest vehicleRequest) {
		enrichserviceRequestForupdate(vehicleRequest);
		wMProducer.push(saveTopic, vehicleRequest);

		return getServiceResponse(vehicleRequest);
	}
	
	public VehicleResponse getServiceResponse(VehicleRequest vehicleRequest) {

		return VehicleResponse.builder()
				.responseInfo(factory.createResponseInfoFromRequestInfo(vehicleRequest.getRequestInfo(), true))
				.vehicles(vehicleRequest.getVehicles()).build();
	}

	public void insertVehicleInfoIntoDb(VehicleRequest vehicleRequest) {
		List<VehicleInfo> vehicles = vehicleRequest.getVehicles();
		for(VehicleInfo vehicleInfo:vehicles) {
			cassandraRepository.insertVehicleInfo(vehicleInfo);
		}
	}
	
	public void searchVehicleInfo(VehicleInfoSearchCriteria vehicleInfoSearchCriteria) {
		cassandraRepository.getVehicleInfo(vehicleInfoSearchCriteria);
	}
	
	
}
