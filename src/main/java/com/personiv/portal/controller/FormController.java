package com.personiv.portal.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personiv.portal.model.Attachment;
import com.personiv.portal.model.ErrorResponse;
import com.personiv.portal.model.Form;
import com.personiv.portal.model.Newsletter;
import com.personiv.portal.service.FileService;
import com.personiv.portal.service.FormService;

@RestController
@CrossOrigin(origins = "*")
public class FormController {
	
	@Autowired
	private FormService formService;
	@Autowired
	private FileService fileService;
	
	@RequestMapping(path ="/forms", method = RequestMethod.GET)
    public ResponseEntity<?> getForms(){	
		try {
			return ResponseEntity.ok(formService.getForms());
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server error",500,"Failed executing request"));
		}
	}
	
	@RequestMapping(value = "/forms/add", method = RequestMethod.POST)
    public ResponseEntity<?>addForm(@RequestParam(value="file", required = false) MultipartFile file) {

    	
    	try {
    		formService.addForm(file);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
    	return ResponseEntity.ok(null);
	}
	
	@RequestMapping(value = "/forms/delete", method = RequestMethod.POST)
    public ResponseEntity<?>deleteForm(@RequestBody Form form) {

    	
    	try {
    		formService.deleteForm(form);
	    	return ResponseEntity.ok(form);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return ResponseEntity.status(500).body(new ErrorResponse(new Date(), "Internal server Error", 500,"Failed executing request"));
		}
	}
	@RequestMapping(value = "/forms/update", method = RequestMethod.POST)
    public ResponseEntity<?>updateForm(@RequestParam(value="file", required = false) MultipartFile file,@RequestParam("data")String data ) {

		
    	Form form = null;
    	try {
    		ObjectMapper mapper = new ObjectMapper();
    		
    		form = mapper.readValue(data, Form.class);
    		formService.updateForm(form, file);
        	return ResponseEntity.ok(form);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			return ResponseEntity.status(500).body(new ErrorResponse(new Date(), "Failed executing request", 500, data));
		}
	}
	

	@RequestMapping(path ="/forms/attachment/{filename:.+}", method = RequestMethod.GET)
    public void getAttachment(@PathVariable("filename")String filename, HttpServletResponse response) throws IOException{	
		
		Attachment attachment = formService.getAttachment(filename);
		
		if(attachment != null) {
			
			Resource photo = attachment.getFile();
			
			byte[] bytes = StreamUtils.copyToByteArray(photo.getInputStream());
			
			response.setContentType(attachment.getFileType());
		    response.getOutputStream().write(bytes);
		    response.getOutputStream().close();
		}
		
	}
}
