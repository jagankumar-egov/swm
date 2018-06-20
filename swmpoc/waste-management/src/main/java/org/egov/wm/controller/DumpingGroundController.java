package org.egov.wm.controller;

import org.egov.wm.contract.DumpingGroundRequest;
import org.egov.wm.contract.DumpingGroundResponse;
import org.egov.wm.service.DumpingGroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/v1/dumpingGround/")
public class DumpingGroundController {
	@Autowired
	private DumpingGroundService service;
	
	@PostMapping("_create")
	@ResponseBody
	private ResponseEntity<?> create(@RequestBody DumpingGroundRequest dumpingGroundRequest){
		//validate
		DumpingGroundResponse response=service.create(dumpingGroundRequest);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PostMapping("_update")
	@ResponseBody
	private ResponseEntity<?> update(@RequestBody DumpingGroundRequest dumpingGroundRequest){
		//validate
		DumpingGroundResponse response=service.update(dumpingGroundRequest);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
}
