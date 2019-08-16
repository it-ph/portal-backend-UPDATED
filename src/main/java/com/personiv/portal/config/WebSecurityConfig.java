package com.personiv.portal.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.personiv.portal.config.filter.JwtTokenAuthenticationFilter;


@Configurable
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;
	
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
  
    @Bean
    public JwtTokenAuthenticationFilter authenticationFilterBean() throws Exception {
        return new JwtTokenAuthenticationFilter();
    }
    
    //need to add to allow autowiring authentication manager
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {		
		auth
			.jdbcAuthentication()
			.dataSource(dataSource)
			.passwordEncoder(passwordEncoder());	    
		   
	}
	


	// This method is for overriding some configuration of the WebSecurity
	// If you want to ignore some request or request patterns then you can
	// specify that inside this method
	@Override
	public void configure(WebSecurity web) throws Exception {
		super.configure(web);
	}

	// This method is used for override HttpSecurity of the web Application.
	// We can specify our authorization criteria inside this method.
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
			// starts authorizing configurations
		http.authorizeRequests()
			// ignore the "/" and "/index.html"
			.antMatchers("/",
					     "/ng/**",
					     "/users/**",
					     "/aims/**",
					     "/posts/page/**",
					     "/complaints/add",
					     "/newsletters",
					     "/newsletters/attachment/**",
					     "/posts/attachment/**",
					     "/forms/attachment/**",
					     "/slides/attachment/**",
					     "/slides/by_sequence",
					     "/links",
					     "/forms",
					     "/favicon.ico",
						"/login").permitAll()
			// authenticate all remaining URLS
			.anyRequest().authenticated().and()
			// disabling the basic authentication
			.formLogin().disable()
			.httpBasic().disable()
			// configuring the session as state less. Which means there is
			// no session in the server
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.addFilterBefore(authenticationFilterBean(), UsernamePasswordAuthenticationFilter.class)
			.cors().and()
			// disabling the CSRF - Cross Site Request Forgery, Our JWT is unbreakable
			.csrf().disable();
		
		// disable page caching
		http.headers().cacheControl();
		
   
		
	}
}
