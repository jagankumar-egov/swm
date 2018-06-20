package org.egov.wm.controller;

import javax.validation.Valid;

import org.egov.wm.contract.RouteRequest;
import org.egov.wm.contract.RouteResponse;
import org.egov.wm.service.RouteService;
import org.egov.wm.validator.RouteValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/v1/route/")
public class RouteController {
	@Autowired
	private RouteService routeService;
	
	@Autowired
	private RouteValidator routeValidator;

	
	
	@PostMapping("_create")
	@ResponseBody
	private ResponseEntity<?> create(@RequestBody @Valid RouteRequest routeRequest) {	
		//routeValidator.validateCreate(routeRequest.getroutes());
		RouteResponse response=routeService.create(routeRequest);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	
	@PostMapping("_update")
	@ResponseBody
	private ResponseEntity<?> update(@RequestBody @Valid RouteRequest routeRequest) {
		//routeValidator.validateCreate(routeRequest.getroutes());
		RouteResponse response=routeService.update(routeRequest);	
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	

}
