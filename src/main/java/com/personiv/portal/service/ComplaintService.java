package com.personiv.portal.service;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personiv.portal.dao.ComplaintDao;
import com.personiv.portal.model.Complaint;

@Service
public class ComplaintService {
	
	@Autowired
	private ComplaintDao compDao;
	
	public List<Complaint> getComplaints(){
		return compDao.getComplaints();
	}
	
	public void addComplaint(Complaint comp) throws MessagingException {
		compDao.addComplaint(comp);
	}
}
