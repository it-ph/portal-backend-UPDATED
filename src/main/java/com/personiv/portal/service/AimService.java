package com.personiv.portal.service;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personiv.portal.dao.AimDao;
import com.personiv.portal.model.Aim;

@Service
public class AimService {
	
	@Autowired
	private AimDao aimDao;
	
	public List<Aim> getAims(){
		return aimDao.getAims();
	}
	public void addAim(Aim aim) throws MessagingException {
		aimDao.addAim(aim);
	}
}
