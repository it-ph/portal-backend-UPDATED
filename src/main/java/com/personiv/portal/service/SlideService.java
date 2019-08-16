package com.personiv.portal.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.personiv.portal.dao.SlideDao;
import com.personiv.portal.model.Attachment;
import com.personiv.portal.model.Slide;

@Service
public class SlideService {
	
	@Autowired
	private SlideDao slideDao;
	
	@Autowired
	private FileService fileService;
	
	
	public List<Slide> getSlides(){
		return slideDao.getSlides();
	}
	
	public List<Slide> getSlidesBySequence() {
		return slideDao.getSlidesBySequence();
	}
	
	public void addSlide(String caption, MultipartFile file) throws IOException {
		slideDao.addSlide(caption, file);
	}
	
	public void updateSlide(Slide slide, MultipartFile file) throws IOException {
		slideDao.updateSlide(slide, file);
	}
	
    public Attachment getAttachment(Long id) {
			
		Slide slideletter = slideDao.getSlide(id);
	
		if(slideletter != null && slideletter.getAttachment() != null) {
			
			Attachment attachment = new Attachment();
			Resource resource = fileService.loadFile("/opt/portal/slides/"+ slideletter.getFilename());
			attachment.setFile(resource);
			attachment.setFileType(slideletter.getFileType());
			
			return attachment;
		}
		
		return null;
	}

	public void deleteSlide(Slide slide) {
		slideDao.deleteSlide(slide);
		
	}

	
}
