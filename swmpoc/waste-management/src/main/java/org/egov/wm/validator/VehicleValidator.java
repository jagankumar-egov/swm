package org.egov.wm.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.egov.wm.contract.VehicleInfoSearchCriteria;
import org.egov.wm.model.VehicleInfo;

@org.springframework.stereotype.Service
public class VehicleValidator {
	
	public void validateCreate(List<VehicleInfo> vehicles){
		Map<String, String> errorMap = new HashMap<>();
		for(VehicleInfo vehicleInfo: vehicles) {
			if (vehicleInfo.getCapacity()<0) {
				errorMap.put("Invalid value of field", "The capacity can'y be negative");
			}
			if (!errorMap.isEmpty())
				throw new CustomException(errorMap);
		}
	}

	public void validateSearch(VehicleInfoSearchCriteria criteria, RequestInfo requestInfo) {
		Map<String, String> errorMap = new HashMap<>();

		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}

}
