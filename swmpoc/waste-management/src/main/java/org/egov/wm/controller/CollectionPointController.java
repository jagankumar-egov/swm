package org.egov.wm.controller;

import org.egov.wm.contract.CollectionPointRequest;
import org.egov.wm.contract.CollectionPointResponse;
import org.egov.wm.service.CollectionPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/v1/collectionPoint/")
public class CollectionPointController {
	@Autowired
	private CollectionPointService service;
	
	@PostMapping("_create")
	@ResponseBody
	private ResponseEntity<?> create(@RequestBody CollectionPointRequest collectionPointRequest){
		//validate
		CollectionPointResponse response=service.create(collectionPointRequest);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PostMapping("_update")
	@ResponseBody
	private ResponseEntity<?> update(@RequestBody CollectionPointRequest collectionPointRequest){
		//validate
		CollectionPointResponse response=service.update(collectionPointRequest);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
}
