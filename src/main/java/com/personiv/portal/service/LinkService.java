package com.personiv.portal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personiv.portal.dao.LinkDao;
import com.personiv.portal.model.Link;

@Service
public class LinkService {

	@Autowired
	private LinkDao linkDao;
	
	public List<Link> getLinks(){
		return linkDao.getLinks();
	}
	
	public void addLink(Link link) {
		linkDao.addLink(link);
	}
	
	public void deleteLink(Link link) {
		linkDao.deleteLink(link);
	}
	
	public void updateLink(Link link) {
		linkDao.updateLink(link);
	}
}
