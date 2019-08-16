package com.personiv.portal.model;

import java.util.List;

public class MyPrincipal {
	

	private Long id;
	private String username;
	private String fullname;
	private List<String> authorities;
	
	public MyPrincipal(Long id,String username, String fullname, List<String> authorities) {
		this.id = id;
		this.username = username;
		this.fullname = fullname;
		this.authorities = authorities;
	}
	

	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getFullname() {
		return fullname;
	}


	public void setFullname(String fullname) {
		this.fullname = fullname;
	}


	public String toString() {
		return username;
	}

	public List<String> getAuthorities(){
		return authorities;
	}
	
	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

}
