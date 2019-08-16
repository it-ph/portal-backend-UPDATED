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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personiv.portal.model.Attachment;
import com.personiv.portal.model.ErrorResponse;
import com.personiv.portal.model.Newsletter;
import com.personiv.portal.model.Post;
import com.personiv.portal.model.User;
import com.personiv.portal.service.FileService;
import com.personiv.portal.service.NewsletterService;

@RestController
@CrossOrigin(origins = "*")
public class NewsletterController {
	
	@Autowired
	private NewsletterService newsService;
	
	@Autowired
	private FileService fileService;
	
	@RequestMapping(path ="/newsletters", method = RequestMethod.GET)
    public ResponseEntity<?> getNewsletters(){	
		try {
			return ResponseEntity.ok(newsService.getNewsletters());
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server error",500,"Failed executing request"));
		}
	}

	@RequestMapping(value = "/newsletters/add", method = RequestMethod.POST)
    public ResponseEntity<?>addNewsletter(@RequestParam(value="file", required = false) MultipartFile file,@RequestParam("caption")String caption ,Principal principal) {

    	
    	try {
    		newsService.addNewsletter(caption, file);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
    	return ResponseEntity.ok(null);
	}
	
	@RequestMapping(value = "/newsletters/delete", method = RequestMethod.POST)
    public ResponseEntity<?>deleteNewsletter(@RequestBody Newsletter newsletter) {

    	
    	try {
    		newsService.deleteNewsletter(newsletter);

        	return ResponseEntity.ok(newsletter);
		} catch (Exception e) {
			
			e.printStackTrace();
			return ResponseEntity.status(500).body(new ErrorResponse(new Date(),"Internal Server error",500,"Failed executing request"));
		}
	}
	
	@RequestMapping(value = "/newsletters/update", method = RequestMethod.POST)
    public ResponseEntity<?>updateNewsletter(@RequestParam(value="file", required = false) MultipartFile file,@RequestParam("data")String data ,Principal principal) {

		
    	Newsletter news = null;
    	try {
    		ObjectMapper mapper = new ObjectMapper();
    		
    		news = mapper.readValue(data, Newsletter.class);
    		newsService.updateNewsletter(news, file);
        	return ResponseEntity.ok(news);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			return ResponseEntity.status(500).body(new ErrorResponse(new Date(), "Failed executing request", 500, data));
		}
	}
	

	@RequestMapping(path = "/newsletters/attachment/{filename:.+}", method = RequestMethod.GET)
	public void getPhoto(@PathVariable("filename")String filename, HttpServletResponse response) throws IOException{
		
		
		Resource resource = fileService.loadFile("/opt/portal/newsletters/"+ filename);
		
		byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
		
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
	    response.getOutputStream().write(bytes);
	    response.getOutputStream().close();
		
	}
}
