package org.egov.wm.contract;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wm.model.Route;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteRequest {
	
	public RequestInfo requestInfo;
	
	public List<Route> routes;
	
}