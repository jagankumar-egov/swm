package org.egov.wm.controller;

import javax.validation.Valid;

import org.egov.wm.contract.DriverInfoSearchCriteria;
import org.egov.wm.contract.RequestInfoWrapper;
import org.egov.wm.contract.DriverRequest;
import org.egov.wm.contract.DriverResponse;
import org.egov.wm.service.DriverService;
import org.egov.wm.validator.DriverInfoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/v1/driver/")
public class DriverInfoController {
	@Autowired
	private DriverInfoValidator driverInfoValidator;
	@Autowired
	private DriverService driverService;
	
	@PostMapping("_create")
	@ResponseBody
	private ResponseEntity<?> create(@RequestBody @Valid DriverRequest driverRequest){//ResponseEntity<?>
		//driverInfoValidator.validateCreate(driverRequest.getDrivers());
		DriverResponse response=driverService.create(driverRequest);
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
		
	}
	@PostMapping("_update")
	@ResponseBody
	private ResponseEntity<?> update(@RequestBody DriverRequest driverRequest) {
		//validate update
		DriverResponse response=driverService.update(driverRequest);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("_search")
	@ResponseBody
	private void search(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid DriverInfoSearchCriteria driverInfoSearchCriteria){
		//validations
		driverService.searchDriverInfo(driverInfoSearchCriteria);
		
	}
	
}


















