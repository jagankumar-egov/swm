package org.egov.wm.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tracer.model.CustomException;
import org.egov.wm.model.Trip;

@org.springframework.stereotype.Service
public class TripValidator {
	public void validateCreate(List<Trip> trips){
		Map<String, String> errorMap = new HashMap<>();
		for(Trip trip: trips) {
			if (!errorMap.isEmpty())
				throw new CustomException(errorMap);
		}
	}
}
