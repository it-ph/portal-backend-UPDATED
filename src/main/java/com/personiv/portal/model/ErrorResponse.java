package com.personiv.portal.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
	private Date timestamp;
	private String error;
	private Integer status;
	private String message;
	
}
