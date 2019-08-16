package com.personiv.portal.model;

import java.util.Date;

import lombok.Data;

@Data
public class Complaint {
	private Long id;
	private String sender;
	private String message;
	private Date createdAt;
}
