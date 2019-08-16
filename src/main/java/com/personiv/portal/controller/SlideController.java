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
import com.personiv.portal.model.ErrorResponse;
import com.personiv.portal.model.Slide;
import com.personiv.portal.service.FileService;
import com.personiv.portal.service.SlideService;

@RestController
@CrossOrigin(origins = "*")
public class SlideController {
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private SlideService slideService;
	
	@RequestMapping(path ="/slides", method = RequestMethod.GET)
    public ResponseEntity<?> getSlides(){	
		try {
			return ResponseEntity.ok(slideService.getSlides());
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server error",500,"Failed executing request"));
		}
	}
	
	@RequestMapping(path ="/slides/by_sequence", method = RequestMethod.GET)
    public ResponseEntity<?> getSlidesBySequence(){	
		try {
			return ResponseEntity.ok(slideService.getSlidesBySequence());
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server error",500,"Failed executing request"));
		}
	}
	@RequestMapping(value = "/slides/add", method = RequestMethod.POST)
    public ResponseEntity<?>addSlide(@RequestParam(value="file", required = false) MultipartFile file,@RequestParam("caption")String caption ,Principal principal) {

    	
    	try {
    		slideService.addSlide(caption, file);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
    	return ResponseEntity.ok(null);
	}
	
	@RequestMapping(value = "/slides/delete", method = RequestMethod.POST)
    public ResponseEntity<?>deleteSlide(@RequestBody Slide slide) {

    	
    	try {
    		slideService.deleteSlide(slide);

        	return ResponseEntity.ok(slide);
		} catch (Exception e) {
			
			e.printStackTrace();
			return ResponseEntity.status(500).body(new ErrorResponse(new Date(), "Internal Server Error",500,"Failed executing request"));
		}
	}
	
	@RequestMapping(value = "/slides/update", method = RequestMethod.POST)
    public ResponseEntity<?>updateSlide(@RequestParam(value="file", required = false) MultipartFile file,@RequestParam("data")String data ,Principal principal) {

		
    	Slide news = null;
    	try {
    		ObjectMapper mapper = new ObjectMapper();
    		
    		news = mapper.readValue(data, Slide.class);
    		slideService.updateSlide(news, file);
        	return ResponseEntity.ok(news);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			return ResponseEntity.status(500).body(new ErrorResponse(new Date(), "Failed executing request", 500, data));
		}
	}
	

	@RequestMapping(path = "/slides/attachment/{filename:.+}", method = RequestMethod.GET)
	public void getPhoto(@PathVariable("filename")String filename, HttpServletResponse response) throws IOException{
		
		
		Resource resource = fileService.loadFile("/opt/portal/slides/"+ filename);
		
		byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
		
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
	    response.getOutputStream().write(bytes);
	    response.getOutputStream().close();
		
	}
}
