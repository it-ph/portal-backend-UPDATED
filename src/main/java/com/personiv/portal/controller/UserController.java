package com.personiv.portal.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.personiv.portal.model.ErrorResponse;
import com.personiv.portal.model.User;
import com.personiv.portal.service.FileService;
import com.personiv.portal.service.UserService;

@RestController
@CrossOrigin(origins = "*")
public class UserController {
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FileService fileService;

	@RequestMapping(path ="/users", method = RequestMethod.GET)
    public ResponseEntity<?> getusers(){
		
		try {
			return ResponseEntity.ok(userService.getUsers());
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server error",500,"Failed executing request"));
		}
	}
	
	@RequestMapping(path ="/users/add", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addUser(@RequestBody User user){
		
		try {
			userService.addUser(user);
			return ResponseEntity.ok(user);
		}catch(DuplicateKeyException ex) {
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server ",500,"User account already exist"));
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server error",500,"Failed executing request"));
		}
	}
	
	@RequestMapping(path ="/users/update", method = RequestMethod.POST)
    public ResponseEntity<?> updateUser(@RequestBody User user){
		
		try {
			userService.updateUser(user);
			return ResponseEntity.ok(user);
		}catch(DuplicateKeyException ex) {
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server ",500,"User account already exist"));
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server error",500,"Failed executing request"));
		}
	}
	
	@RequestMapping(path ="/users/enable", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> enableUser(@RequestBody User user){
		
		try {
			userService.enableUser(user);
			return ResponseEntity.ok(user);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server error",500,"Failed executing request"));
		}
	}
	
	@RequestMapping(path ="/users/disable", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> disableUser(@RequestBody User user){
		
		try {
			userService.disableUser(user);
			return ResponseEntity.ok(user);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server error",500,"Failed executing request"));
		}
	}
	
	
	@RequestMapping(path = "/users/photo/{filename:.+}", method = RequestMethod.GET)
	public void getPhoto(@PathVariable("filename")String filename, HttpServletResponse response) throws IOException{
		
		
		Resource resource = fileService.loadFile("/opt/portal/avatar/"+ filename);
		
		byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
		
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
	    response.getOutputStream().write(bytes);
	    response.getOutputStream().close();
		
	}
}
