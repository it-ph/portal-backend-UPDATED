package com.personiv.portal.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.personiv.portal.dao.FormDao;
import com.personiv.portal.model.Attachment;
import com.personiv.portal.model.Form;
import com.personiv.portal.model.Post;

@Service
public class FormService {
	
	@Autowired
	private FormDao formDao;
	
	@Autowired
	private FileService fileService;
	
	public List<Form> getForms(){
		return formDao.getForms();
	}
	
	public void addForm(MultipartFile file) throws IOException {
		formDao.addForm(file);
	}
	
	public void deleteForm(Form form) {
		formDao.deleteForm(form);
	}

	public void updateForm(Form form, MultipartFile file) throws IOException {
		formDao.updateForm(form, file);
		
	}

	public Attachment getAttachment(String filename) {
		
		Form  form = formDao.getForm(filename);
	
		if(form != null) {
			
			Attachment attachment = new Attachment();
			Resource resource = fileService.loadFile("/opt/portal/forms/"+ form.getFilename());
			attachment.setFile(resource);
			attachment.setFileType(form.getFileType());
			
			return attachment;
		}
		
		return null;
	}
}
