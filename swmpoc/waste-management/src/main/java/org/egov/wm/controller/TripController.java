package org.egov.wm.controller;

import javax.validation.Valid;

import org.egov.wm.contract.TripRequest;
import org.egov.wm.contract.TripResponse;
import org.egov.wm.service.TripService;
import org.egov.wm.validator.TripValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/v1/trip/")
public class TripController {
	@Autowired
	private TripService tripService;
	
	@Autowired
	private TripValidator tripValidator;

	
	
	@PostMapping("_create")
	@ResponseBody
	private ResponseEntity<?> create(@RequestBody @Valid TripRequest tripRequest) {	
		//tripValidator.validateCreate(tripRequest.gettrips());
		TripResponse response=tripService.create(tripRequest);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	
	@PostMapping("_update")
	@ResponseBody
	private ResponseEntity<?> update(@RequestBody @Valid TripRequest tripRequest) {
		//tripValidator.validateCreate(tripRequest.gettrips());
		TripResponse response=tripService.update(tripRequest);	
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	

}
