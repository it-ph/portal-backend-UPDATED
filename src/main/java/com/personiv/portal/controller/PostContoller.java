package com.personiv.portal.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.personiv.portal.model.Post;
import com.personiv.portal.model.User;
import com.personiv.portal.service.PostService;
import com.personiv.portal.service.UserService;

@RestController
@CrossOrigin(origins = "*")
public class PostContoller {
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private UserService userService;
	
	
	@RequestMapping(path ="/posts", method = RequestMethod.GET)
    public ResponseEntity<?> getPosts(){	
		try {
			return ResponseEntity.ok(postService.getPosts());
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server error",500,"Failed executing request"));
		}
	}
	
	@RequestMapping(path ="/posts/page/{numPage}", method = RequestMethod.GET)
    public ResponseEntity<?> getPostByPage(@PathVariable("numPage") int id){	
		try {
			return ResponseEntity.ok(postService.getPostByPage(id));
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server error",500,"Failed executing request"));
		}
	}
	
    @RequestMapping(value = "/posts/add_with_attachment", method = RequestMethod.POST)
    public ResponseEntity<?>addPostWithAttachment(@RequestParam(value="file", required = true) MultipartFile file,@RequestParam("comment")String comment ,Principal principal) {

    	User user = userService.getUserByUsername(principal.getName());
    	Post post = null;
    	try {
		
			post = new Post();
			post.setComment(comment);
			
			postService.addPostWithAttachment(post, user.getId(), file);
		
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
    	return ResponseEntity.ok(post);
	}
	
    @RequestMapping(value = "/posts/add", method = RequestMethod.POST)
    public ResponseEntity<?>addPost(@RequestBody String comment ,Principal principal) {

    	User user = userService.getUserByUsername(principal.getName());
    	Post post = null;
    	try {
		
			post = new Post();
			post.setComment(comment);
			postService.addPost(post,user.getId());
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
    	return ResponseEntity.ok(post);
	}
    
    @RequestMapping(value = "/posts/update", method = RequestMethod.POST)
    public ResponseEntity<?>updatePost(@RequestParam(value="file", required = false) MultipartFile file,@RequestParam("data")String data) {

    	Post post = null;
    	try {
		
			ObjectMapper mapper = new ObjectMapper();
			post = mapper.readValue(data, Post.class);
			
			postService.updatePost(post, file);
		
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
    	return ResponseEntity.ok(post);
	}
	
	@RequestMapping(path ="/posts/delete", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePost(@RequestBody Post post){	
		try {
			postService.deletePost(post);
			return ResponseEntity.ok(post);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server error",500,"Failed executing request"));
		}
	}
	
	@RequestMapping(path ="/posts/reply", method = RequestMethod.POST)
    public ResponseEntity<?> replyPost(){	
		try {
			return ResponseEntity.ok(null);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(),"Internal Server error",500,"Failed executing request"));
		}
	}
	
	@RequestMapping(path ="/posts/attachment/{id}", method = RequestMethod.GET)
    public void getAttachment(@PathVariable("id")Long id, HttpServletResponse response) throws IOException{	
		
		Attachment attachment = postService.getAttachment(id);
		
		if(attachment != null) {
			
			Resource photo = attachment.getFile();
			
			byte[] bytes = StreamUtils.copyToByteArray(photo.getInputStream());
			
			response.setContentType(attachment.getFileType());
		    response.getOutputStream().write(bytes);
		    response.getOutputStream().close();
		}
		
	}
	

}
