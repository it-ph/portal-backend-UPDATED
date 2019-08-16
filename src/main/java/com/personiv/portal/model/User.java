package com.personiv.portal.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class User {
	private Long id;
	private String username;
	private String password;
	private String fullname;
	private boolean enabled;
	private String avatar;
	private Date createdAt;
	private Date updatedAt;
	private List<String> authorities;
}
