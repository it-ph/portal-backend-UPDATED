package com.personiv.portal.controller;

import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.personiv.portal.model.ErrorResponse;
import com.personiv.portal.model.JwtAuthenticationResponse;
import com.personiv.portal.model.PlainUser;
import com.personiv.portal.model.User;
import com.personiv.portal.service.UserService;
import com.personiv.portal.util.JwtTokenUtil;

@RestController
@CrossOrigin(origins = "*")
public class AccessController {
	
	@Autowired
   	private AuthenticationManager authenticationManager;

    @Autowired
	private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private UserService userService;
    
    
//    @RequestMapping(path ="/", method =RequestMethod.GET)
//    public String welcomeMessage(){
//    	return "Dexification Productivity Tool";
//    }
    
	@RequestMapping(path = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody PlainUser user) throws AuthenticationException {
	
	   User authUser = null;

	  try {
		  //check if this user has inventory scope
		  authUser = userService.getUserByUsername(user.getUsername());  
       
	  }catch(EmptyResultDataAccessException e) {
		  e.printStackTrace();
		  //if no matching found from users with username
		  return ResponseEntity.status(401).body(new ErrorResponse(new Date(),"Unauthorized",401,"Access denied"));
	  }
	  
	  //hard to compare Bcrypt password in the database
	  //fire the default authentication to compare username and password
	  try {
		  final Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        user.getUsername(),
	                        user.getPassword()
	                )
	                
	        );
		
		   //did not use single sign on because of problem defining authorities and scope for single signed on users
	       SecurityContextHolder.getContext().setAuthentication(authentication);
	     
	        String token = jwtTokenUtil.generateToken(authUser);
	        
	        Date expiration = jwtTokenUtil.getExpirationDateFromToken(token);
		    
		    return ResponseEntity.ok(new JwtAuthenticationResponse(token,  "bearer",expiration));
	  }catch(DisabledException ex) {
		  return ResponseEntity.status(401).body(new ErrorResponse(new Date(),"Unauthorized",401,"Account disabled"));
			 
	  }
	  catch(Exception e) {
		  e.printStackTrace();
		  return ResponseEntity.status(401).body(new ErrorResponse(new Date(),"Unauthorized",401,"Access denied"));
			 
	  }
       
	  
    }
	 
	@RequestMapping(path ="/claims" ,  method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Principal claims(Principal principal) {
		return principal;
	}
	
	

	@RequestMapping(path ="/change_password" ,  method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> changePassword(Principal principal, @RequestBody String newPassword) {
		
		PlainUser user = new PlainUser();
		user.setPassword(newPassword);
		user.setUsername(principal.getName());
		
		userService.updatePassword(user);
		
		return ResponseEntity.ok(user);
	}


	
}
