package org.egov.wm.service;

import java.util.List;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wm.model.AuditInfo;
import org.egov.wm.model.DumpingGround;
import org.egov.wm.contract.DumpingGroundRequest;
import org.egov.wm.contract.DumpingGroundResponse;
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
public class DumpingGroundService {
	
	@Value("${kafka.topics.save.dumpingGround}")
	private String saveTopic ;
	
	@Autowired
	private WMUtils wMUtils;
	
	@Autowired
	private ResponseInfoFactory factory;
	
	@Autowired 
	private WMProducer producer;
	
	@Autowired
	private CassandraRepository cassandraRepository; 
	
	public static final Logger logger = LoggerFactory.getLogger(DumpingGroundService.class);
	
	public DumpingGroundResponse create(DumpingGroundRequest dumpingGroundRequest) {
		enrichDumpingGroundRequestForCreate(dumpingGroundRequest);
		producer.push(saveTopic, dumpingGroundRequest);
		return getDumpingGroundResponse(dumpingGroundRequest);
	}
	
	public DumpingGroundResponse update(DumpingGroundRequest dumpingGroundRequest) {
		enrichDumpingGroundRequestForUpdate(dumpingGroundRequest);
		producer.push(saveTopic, dumpingGroundRequest);
		return getDumpingGroundResponse(dumpingGroundRequest);
	}
	
	public void enrichDumpingGroundRequestForCreate(DumpingGroundRequest dumpingGroundRequest) {
		RequestInfo requestInfo=dumpingGroundRequest.getRequestInfo();
		List<DumpingGround> grounds=dumpingGroundRequest.getGrounds();
		AuditInfo auditInfo = wMUtils.getAuditInfo(String.valueOf(requestInfo.getUserInfo().getId()), true);
		Integer groundsCount=grounds.size();
		for(int index=0; index<groundsCount; index++) {
			DumpingGround ground=grounds.get(index);
			ground.setAuditInfo(auditInfo);
			UUID id=UUIDs.timeBased();
			ground.setId(id);
		}
		
	}
	
	public void enrichDumpingGroundRequestForUpdate(DumpingGroundRequest dumpingGroundRequest) {
		RequestInfo requestInfo=dumpingGroundRequest.getRequestInfo();
		List<DumpingGround> grounds=dumpingGroundRequest.getGrounds();
		AuditInfo auditInfo = wMUtils.getAuditInfo(String.valueOf(requestInfo.getUserInfo().getId()), false);
		Integer groundsCount=grounds.size();
		for(int index=0; index<groundsCount; index++) {
			DumpingGround ground=grounds.get(index);
			ground.setAuditInfo(auditInfo);
		}	
	}

	public DumpingGroundResponse getDumpingGroundResponse(DumpingGroundRequest dumpingGroundRequest) {
		return DumpingGroundResponse.builder()
				.responseInfo(factory.createResponseInfoFromRequestInfo(dumpingGroundRequest.getRequestInfo(), true))
				.grounds(dumpingGroundRequest.getGrounds()).build();
	}
	
	public void insertDumpingGroundIntoDb(DumpingGroundRequest dumpingGroundRequest) {
		List<DumpingGround> grounds= dumpingGroundRequest.getGrounds();
		for(DumpingGround ground:grounds) {
			cassandraRepository.insertDumpingGround(ground);
		}
	}
}












