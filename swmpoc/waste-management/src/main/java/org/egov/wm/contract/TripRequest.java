package org.egov.wm.contract;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wm.model.Trip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TripRequest {
	
	public RequestInfo requestInfo;
	
	public List<Trip> trips;
	
}