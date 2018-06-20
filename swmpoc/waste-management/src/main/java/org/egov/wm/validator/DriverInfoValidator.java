package org.egov.wm.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tracer.model.CustomException;
import org.egov.wm.model.DriverInfo;

@org.springframework.stereotype.Service
public class DriverInfoValidator {
	
	
	public void validateCreate(List<DriverInfo> drivers)  {
		Map<String, String> errorMap = new HashMap<>();
		for(DriverInfo driverInfo: drivers) {
			
			if (String.valueOf(driverInfo.getPhoneNo()).length()!=10) {
				errorMap.put("Invalid length of field", "The length of PhoneNo should be 10");
			}
			if (String.valueOf(driverInfo.getAadhaarNo()).length()!=12) {
				errorMap.put("Invalid length of field", "The length of AadharNo should be 12");
			}
			if (String.valueOf(driverInfo.getLicenseNo()).length()!=13) {
				errorMap.put("Invalid length of field", "The length of AadharNo should be 13");
			}
			if (!errorMap.isEmpty())
				throw new CustomException(errorMap);
		}
	}
}
