package org.egov.wm.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tracer.model.CustomException;
import org.egov.wm.model.Route;

@org.springframework.stereotype.Service
public class RouteValidator {
	public void validateCreate(List<Route> routes){
		Map<String, String> errorMap = new HashMap<>();
		for(Route route: routes) {
			if (!errorMap.isEmpty())
				throw new CustomException(errorMap);
		}
	}
}
