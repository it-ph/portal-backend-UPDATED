package com.personiv.portal.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.personiv.portal.dao.NewsletterDao;
import com.personiv.portal.model.Attachment;
import com.personiv.portal.model.Newsletter;

@Service
public class NewsletterService {
	
	@Autowired
	private NewsletterDao newsDao;
	
	@Autowired
	private FileService fileService;
	
	public List<Newsletter> getNewsletters(){
		return newsDao.getNewsletters();
	}
	
	public void addNewsletter(String caption, MultipartFile file) throws IOException {
		newsDao.addNewsletter(caption, file);
	}
	
	public void updateNewsletter(Newsletter news, MultipartFile file) throws IOException {
		newsDao.updateNewsletter(news, file);
	}
	
    public Attachment getAttachment(Long id) {
			
		Newsletter newsletter = newsDao.getNewsletter(id);
	
		if(newsletter != null && newsletter.getAttachment() != null) {
			
			Attachment attachment = new Attachment();
			Resource resource = fileService.loadFile("/opt/portal/newsletters/"+ newsletter.getFilename());
			attachment.setFile(resource);
			attachment.setFileType(newsletter.getFileType());
			
			return attachment;
		}
		
		return null;
	}

	public void deleteNewsletter(Newsletter newsletter) {
		newsDao.deleteNewsletter(newsletter);
		
	}

	
}
