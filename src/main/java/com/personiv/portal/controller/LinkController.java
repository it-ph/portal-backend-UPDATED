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

import com.personiv.portal.model.ErrorResponse;
import com.personiv.portal.model.Link;
import com.personiv.portal.service.LinkService;

@RestController
@CrossOrigin(origins = "*")
public class LinkController {
	
	@Autowired
	private LinkService linkService;
	
	@RequestMapping(path ="/links", method = RequestMethod.GET)
    public ResponseEntity<?> getLinks(){	
		try {
			return ResponseEntity.ok(linkService.getLinks());
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server error",500,"Failed executing request"));
		}
	}
	
	@RequestMapping(path ="/links/add", method = RequestMethod.POST, consumes =MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addLink(@RequestBody Link link){	
		try {
			linkService.addLink(link);
			return ResponseEntity.ok(link);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server error",500,"Failed executing request"));
		}
	}
	
	@RequestMapping(path ="/links/update", method = RequestMethod.POST, consumes =MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateLink(@RequestBody Link link){	
		try {
			linkService.updateLink(link);
			return ResponseEntity.ok(link);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server error",500,"Failed executing request"));
		}
	}
	
	@RequestMapping(path ="/links/delete", method = RequestMethod.POST, consumes =MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteLink(@RequestBody Link link){	
		try {
			linkService.deleteLink(link);
			return ResponseEntity.ok(link);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server error",500,"Failed executing request"));
		}
	}

}
