package org.egov.wm.controller;

import javax.validation.Valid;

import org.egov.wm.contract.VehicleResponse;
import org.egov.wm.contract.DriverInfoSearchCriteria;
import org.egov.wm.contract.RequestInfoWrapper;
import org.egov.wm.contract.VehicleInfoSearchCriteria;
import org.egov.wm.contract.VehicleRequest;
import org.egov.wm.service.VehicleService;
import org.egov.wm.validator.VehicleValidator;
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
@RequestMapping(value = "/v1/vehicle/")
public class VehicleController {

	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private VehicleValidator vehicleValidator;

	
	
	@PostMapping("_create")
	@ResponseBody
	private ResponseEntity<?> create(@RequestBody @Valid VehicleRequest vehicleRequest) {	
		//vehicleValidator.validateCreate(vehicleRequest.getVehicles());
		VehicleResponse response=vehicleService.create(vehicleRequest);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	
	@PostMapping("_update")
	@ResponseBody
	private ResponseEntity<?> update(@RequestBody @Valid VehicleRequest vehicleRequest) {
		//vehicleValidator.validateCreate(vehicleRequest.getVehicles());
		VehicleResponse response=vehicleService.update(vehicleRequest);	
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("_search")
	@ResponseBody
	private void search(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid VehicleInfoSearchCriteria vehicleInfoSearchCriteria){
		vehicleValidator.validateSearch(vehicleInfoSearchCriteria, requestInfoWrapper.getRequestInfo());
		vehicleService.searchVehicleInfo(vehicleInfoSearchCriteria);
	}


}
