package com.personiv.portal.config.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.personiv.portal.model.MyPrincipal;
import com.personiv.portal.model.User;
import com.personiv.portal.service.UserService;
import com.personiv.portal.util.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;

public class JwtTokenAuthenticationFilter extends  OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(this.getClass());
   
    
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		
		String username = null;
		User user = null;
		
		// 1. get the authentication header. Tokens are supposed to be passed in the authentication header
		String header = request.getHeader("Authorization");
				
		// 2. validate the header and check the prefix
		if(header == null || !header.startsWith("Bearer ")) {
			chain.doFilter(request, response);  		// If not valid, go to the next filter.
			return;
		}
				
		logger.info("Header: "+header);
		// If there is no token provided and hence the user won't be authenticated. 
		// It's Ok. Maybe the user accessing a public path or asking for a token.
		
		// All secured paths that needs a token are already defined and secured in config class.
		// And If user tried to access without access token, then he won't be authenticated and an exception will be thrown.
		
		// 3. Get the token
		String token = header.replace("Bearer ", "");
		logger.info("TOKEN: "+token);
		
		try {
             username = jwtTokenUtil.getUsernameFromToken(token);
         } catch (IllegalArgumentException e) {
             logger.error("an error occured during getting username from token", e);
         } catch (ExpiredJwtException e) {
             logger.warn("the token is expired and not valid anymore", e);
         }
		
		
		
		if(username != null  && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			user = userService.getUserByUsername(username);
			
			MyPrincipal principal = new MyPrincipal(user.getId(),user.getUsername(), user.getFullname(),user.getAuthorities());
			
			// 4. Validate the token
		   if (jwtTokenUtil.validateToken(token, user)) {
		        
			   
               UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, getGrantedAuthorities(user));
                
              
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 5. Set the authenticated user
                logger.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
		   }
		   
			
		}
	
		
		// go to the next filter in the filter chain
		chain.doFilter(request, response);
	}
	
    public List<GrantedAuthority> getGrantedAuthorities(User user){

    	List<GrantedAuthority> authorities =  new ArrayList<>();
    	
    	for(String auth: user.getAuthorities()) {
    		authorities.add( new SimpleGrantedAuthority(auth));
    	}
    	
    	return authorities;
    }
}
