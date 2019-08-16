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

import com.personiv.portal.model.Aim;
import com.personiv.portal.model.ErrorResponse;
import com.personiv.portal.service.AimService;

@RestController
@CrossOrigin(origins = "*")
public class AimController {
	
	@Autowired
	private AimService aimService;
	
	@RequestMapping(path ="/aims", method = RequestMethod.GET)
    public ResponseEntity<?> getAims(){	
		try {
			return ResponseEntity.ok(aimService.getAims());
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server error",500,"Failed executing request"));
		}
	}
	
	@RequestMapping(path ="/aims/add", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addAim(@RequestBody Aim aim){	
		try {
			aimService.addAim(aim);
			return ResponseEntity.ok(aim);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server error",500,"Failed executing request"));
		}
	}
}
