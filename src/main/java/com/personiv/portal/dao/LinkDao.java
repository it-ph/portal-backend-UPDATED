package com.personiv.portal.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.personiv.portal.model.Link;

@Repository
@Transactional(readOnly = false)
public class LinkDao extends JdbcDaoSupport{
	
	
	@Autowired
	private DataSource dataSource;
	
	private JdbcTemplate jdbcTemplate;
	
    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }
    
    public List<Link> getLinks(){
    	String sql= "SELECT id, app,location,createdAt,updatedAt FROM links ORDER BY id DESC";
    	return jdbcTemplate.query(sql,  new BeanPropertyRowMapper<Link>(Link.class));
    }
    
    public void addLink(Link link) {
    	String sql ="INSERT INTO links(app,location) VALUES(?,?)";
    	jdbcTemplate.update(sql, new Object[] {
    			link.getApp(),
    			link.getLocation()
    	});
    }
    
    public void updateLink(Link link) {
    	String sql ="UPDATE links SET app =?, location = ?, updatedAt = CURRENT_TIMESTAMP WHERE id =? ";
    	jdbcTemplate.update(sql, new Object[] {
    			link.getApp(),
    			link.getLocation(),
    			link.getId()
    	});
    }
    public void deleteLink(Link link) {
    	String sql ="DELETE FROM links WHERE id =?";
    	jdbcTemplate.update(sql, new Object[] { link.getId() });
    }
}
