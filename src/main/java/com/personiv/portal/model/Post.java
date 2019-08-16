package com.personiv.portal.model;

import java.util.Date;

import lombok.Data;

@Data
public class Post {
	
	private Long id;
	private Employee postedBy;
	private String comment;
	private String attachment;
	private String fileType;
	private Date createdAt;
	private Date updatedAt;
}
