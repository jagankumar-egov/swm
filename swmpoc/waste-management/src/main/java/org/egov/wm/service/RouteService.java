package org.egov.wm.service;

import java.util.List;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wm.contract.VehicleInfoSearchCriteria;
import org.egov.wm.contract.RouteRequest;
import org.egov.wm.contract.RouteResponse;
import org.egov.wm.model.AuditInfo;
import org.egov.wm.model.Route;
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
public class RouteService {
	
	@Value("${kafka.topics.save.route}")
	private String saveTopic;
	
	@Autowired
	private WMProducer wMProducer;
	
	@Autowired
	private CassandraRepository cassandraRepository;
	
	@Autowired
	private ResponseInfoFactory factory;
	
	@Autowired
	private WMUtils utils;

	public static final Logger logger = LoggerFactory.getLogger(RouteService.class);
	
	public RouteResponse create(RouteRequest routeRequest) {
		enrichserviceRequestForcreate(routeRequest);
		wMProducer.push(saveTopic, routeRequest);

		return getServiceResponse(routeRequest);
	}
	
	private void enrichserviceRequestForcreate(RouteRequest routeRequest) {

		RequestInfo requestInfo = routeRequest.getRequestInfo();
		List<Route> routes = routeRequest.getRoutes();
		AuditInfo auditInfo = utils.getAuditInfo(String.valueOf(requestInfo.getUserInfo().getId()), true);

		for(Route route:routes) {
			route.setAuditInfo(auditInfo);
			UUID id=UUIDs.timeBased();
			route.setId(id);
		}

	}
	
	private void enrichserviceRequestForupdate(RouteRequest routeRequest) {

		RequestInfo requestInfo = routeRequest.getRequestInfo();
		List<Route> routes = routeRequest.getRoutes();
		AuditInfo auditInfo = utils.getAuditInfo(String.valueOf(requestInfo.getUserInfo().getId()), true);

		for(Route route:routes) {
			route.setAuditInfo(auditInfo);
		}

	}
	
	public RouteResponse update(RouteRequest routeRequest) {
		enrichserviceRequestForupdate(routeRequest);
		wMProducer.push(saveTopic, routeRequest);

		return getServiceResponse(routeRequest);
	}
	
	public RouteResponse getServiceResponse(RouteRequest routeRequest) {

		return RouteResponse.builder()
				.responseInfo(factory.createResponseInfoFromRequestInfo(routeRequest.getRequestInfo(), true))
				.routes(routeRequest.getRoutes()).build();
	}

	public void insertRouteIntoDb(RouteRequest routeRequest) {
		List<Route> routes = routeRequest.getRoutes();
		for(Route route:routes) {
			cassandraRepository.insertRoute(route);
		}
	}

	
	public Object getServiceRequestDetails(RequestInfo requestInfo, VehicleInfoSearchCriteria vehicleInfoSearchCriteria) {
		return requestInfo;
	}
	

}
