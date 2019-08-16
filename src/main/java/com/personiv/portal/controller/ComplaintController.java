package com.personiv.portal.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.personiv.portal.model.Complaint;
import com.personiv.portal.model.ErrorResponse;
import com.personiv.portal.service.ComplaintService;

@RestController
@CrossOrigin(origins = "*")
public class ComplaintController {
	
	@Autowired
	private ComplaintService compService;
	
	@RequestMapping(path ="/complaints", method = RequestMethod.GET)
    public ResponseEntity<?> getComplaints(){	
		try {
			return ResponseEntity.ok(compService.getComplaints());
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server error",500,"Failed executing request"));
		}
	}
	
	@RequestMapping(path ="/complaints/add", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_UTF8_VALUE, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addComplaint(@RequestBody Complaint complaint){	
		try {
			compService.addComplaint(complaint);
			return ResponseEntity.ok(complaint);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server error",500,"Failed executing request"));
		}
	}
}
