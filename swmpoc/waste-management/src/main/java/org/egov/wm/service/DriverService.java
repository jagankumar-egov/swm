package org.egov.wm.service;

import java.util.List;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wm.contract.DriverInfoSearchCriteria;
import org.egov.wm.model.AuditInfo;
import org.egov.wm.model.DriverInfo;
import org.egov.wm.contract.DriverRequest;
import org.egov.wm.contract.DriverResponse;
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
public class DriverService {
	@Value("${kafka.topics.save.driverInfo}")
	private String saveTopic;

	@Autowired
	private WMUtils wMUtils;
	
	@Autowired
	private ResponseInfoFactory factory;
	
	@Autowired 
	private WMProducer producer;
	
	@Autowired
	private CassandraRepository cassandraRepository; 
	
	public static final Logger logger = LoggerFactory.getLogger(DriverService.class);
	
	public DriverResponse create(DriverRequest driverRequest) {
		enrichDriverRequestForCreate(driverRequest);
		producer.push(saveTopic, driverRequest);
		return getDriverResponse(driverRequest);
	}
	public DriverResponse update(DriverRequest driverRequest) {
		enrichDriverRequestForUpdate(driverRequest);
		producer.push(saveTopic, driverRequest);
		return getDriverResponse(driverRequest);
	}
	
	public void enrichDriverRequestForCreate(DriverRequest driverRequest) {
		RequestInfo requestInfo = driverRequest.getRequestInfo();
		List<DriverInfo> drivers=driverRequest.getDrivers();
		AuditInfo auditInfo = wMUtils.getAuditInfo(String.valueOf(requestInfo.getUserInfo().getId()), true);
		Integer driverInfoCount=drivers.size();
		for(int index=0; index<driverInfoCount; index++) {
			DriverInfo driverInfo=drivers.get(index);
			driverInfo.setAuditInfo(auditInfo);
			UUID id=UUIDs.timeBased();
			driverInfo.setId(id);
		}
		//return getDriverResponse(driverRequest);
	}
	
	public void enrichDriverRequestForUpdate(DriverRequest driverRequest) {
		RequestInfo requestInfo = driverRequest.getRequestInfo();
		List<DriverInfo> drivers=driverRequest.getDrivers();
		AuditInfo auditInfo = wMUtils.getAuditInfo(String.valueOf(requestInfo.getUserInfo().getId()), false);
		Integer driverInfoCount=drivers.size();
		for(int index=0; index<driverInfoCount; index++) {
			DriverInfo driverInfo=drivers.get(index);
			driverInfo.setAuditInfo(auditInfo);
		}
	}
	
	public DriverResponse getDriverResponse(DriverRequest driverRequest) {
		return DriverResponse.builder()
				.responseInfo(factory.createResponseInfoFromRequestInfo(driverRequest.getRequestInfo(), true))
				.drivers(driverRequest.getDrivers()).build();	
	}
	
	public void insertDriverInfoIntoDb(DriverRequest driverRequest) {
		List<DriverInfo> drivers=driverRequest.getDrivers();
		for(DriverInfo driverInfo:drivers) {
			cassandraRepository.insertDriverInfo(driverInfo);
		}
	}
	
	public void searchDriverInfo(DriverInfoSearchCriteria driverInfoSearchCriteria) {
		cassandraRepository.getDriverInfo(driverInfoSearchCriteria);
	}
	
}
