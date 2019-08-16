package com.personiv.portal.model;

import java.util.Date;

import lombok.Data;

@Data
public class Newsletter {
	private Long id;
	private String caption;
	private String attachment;
	private String filename;
	private String fileType;
	private Date createdAt;
	private Date updatedAt;
}
