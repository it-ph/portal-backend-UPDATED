package com.personiv.portal.model;

import java.util.Date;

import lombok.Data;

@Data
public class Link {
	private Long id;
	private String app;
	private String location;
	private Date createdAt;
	private Date updatedAt;
}
