package org.egov.wm.service;

import java.util.List;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wm.model.AuditInfo;
import org.egov.wm.model.CollectionPoint;
import org.egov.wm.contract.CollectionPointRequest;
import org.egov.wm.contract.CollectionPointResponse;
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
public class CollectionPointService {
	
	@Value("${kafka.topics.save.collectionPoint}")
	private String saveTopic ;
	
	@Autowired
	private WMUtils wMUtils;
	
	@Autowired
	private ResponseInfoFactory factory;
	
	@Autowired 
	private WMProducer producer;
	
	@Autowired
	private CassandraRepository cassandraRepository; 
	
	public static final Logger logger = LoggerFactory.getLogger(CollectionPointService.class);
	
	public CollectionPointResponse create(CollectionPointRequest collectionPointRequest) {
		enrichCollectionPointRequestForCreate(collectionPointRequest);
		producer.push(saveTopic, collectionPointRequest);
		return getCollectionPointResponse(collectionPointRequest);
	}
	
	public CollectionPointResponse update(CollectionPointRequest collectionPointRequest) {
		enrichCollectionPointRequestForUpdate(collectionPointRequest);
		producer.push(saveTopic, collectionPointRequest);
		return getCollectionPointResponse(collectionPointRequest);
	}
	
	public void enrichCollectionPointRequestForCreate(CollectionPointRequest collectionPointRequest) {
		RequestInfo requestInfo=collectionPointRequest.getRequestInfo();
		List<CollectionPoint> points=collectionPointRequest.getPoints();
		AuditInfo auditInfo = wMUtils.getAuditInfo(String.valueOf(requestInfo.getUserInfo().getId()), true);
		Integer pointsCount=points.size();
		for(int index=0; index<pointsCount; index++) {
			CollectionPoint point=points.get(index);
			point.setAuditInfo(auditInfo);
			UUID id=UUIDs.timeBased();
			point.setId(id);
		}
		
	}
	
	public void enrichCollectionPointRequestForUpdate(CollectionPointRequest collectionPointRequest) {
		RequestInfo requestInfo=collectionPointRequest.getRequestInfo();
		List<CollectionPoint> points=collectionPointRequest.getPoints();
		AuditInfo auditInfo = wMUtils.getAuditInfo(String.valueOf(requestInfo.getUserInfo().getId()), false);
		Integer pointsCount=points.size();
		for(int index=0; index<pointsCount; index++) {
			CollectionPoint point=points.get(index);
			point.setAuditInfo(auditInfo);
		}	
	}

	public CollectionPointResponse getCollectionPointResponse(CollectionPointRequest collectionPointRequest) {
		return CollectionPointResponse.builder()
				.responseInfo(factory.createResponseInfoFromRequestInfo(collectionPointRequest.getRequestInfo(), true))
				.points(collectionPointRequest.getPoints()).build();
	}
	
	public void insertCollectionPointIntoDb(CollectionPointRequest collectionPointRequest) {
		List<CollectionPoint> points= collectionPointRequest.getPoints();
		for(CollectionPoint point:points) {
			cassandraRepository.insertCollectionPoint(point);
		}
	}
}












