package com.personiv.portal.model;

import java.util.Date;

import lombok.Data;

@Data
public class Aim {
	private Long id;
	private String fullname;
	private String email;
	private String scenario;
	private String aim;
	private String department;
	private Date createdAt;
}
