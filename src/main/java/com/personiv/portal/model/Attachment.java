package com.personiv.portal.model;

import org.springframework.core.io.Resource;

import lombok.Data;

@Data
public class Attachment {
	private Resource file;
	private String fileType;
}
