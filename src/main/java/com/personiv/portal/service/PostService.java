package com.personiv.portal.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.personiv.portal.dao.PostDao;
import com.personiv.portal.model.Attachment;
import com.personiv.portal.model.Post;

@Service
public class PostService {
	
	@Autowired
	private PostDao postDao;
	
	@Autowired
	private FileService fileService;
	
	
	public List<Post> getPosts(){
		return postDao.getPosts();
	}

	public void addPost(Post post, Long id) {
		postDao.addPost(post,id);
	}
	
	public void addPostWithAttachment(Post post,Long id, MultipartFile file) throws IOException {
		postDao.addPostWithAttachment(post, id, file);
	}
	
	public List<Post> getPostByPage(int page){
		return postDao.getPostByPage(page);
	}

	public Attachment getAttachment(Long id) {
		
		Post post = postDao.getPost(id);
	
		if(post != null && post.getAttachment() != null) {
			
			Attachment attachment = new Attachment();
			Resource resource = fileService.loadFile("/opt/portal/posts/"+ post.getAttachment());
			attachment.setFile(resource);
			attachment.setFileType(post.getFileType());
			
			return attachment;
		}
		
		return null;
	}

	public void updatePost(Post post, MultipartFile file) throws IOException {
		postDao.updatePost(post,file);
		
	}

	public void deletePost(Post post) {
		postDao.deletePost(post);
		
	}
	
	
}
