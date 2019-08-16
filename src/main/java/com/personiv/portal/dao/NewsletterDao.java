package com.personiv.portal.dao;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.personiv.portal.model.Attachment;
import com.personiv.portal.model.Newsletter;
import com.personiv.portal.model.Post;
import com.personiv.portal.service.FileService;

@Repository
@Transactional(readOnly = false)
public class NewsletterDao  extends JdbcDaoSupport{
	
	@Autowired
	private FileService fileService;
	 
	private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;
    
    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }
    
    public List<Newsletter> getNewsletters(){
    	String sql="   SELECT id, "
	    		 + "          caption, "
	    		 + "          attachment, "
	    		 + "          file_name filename, "
	    		 + "          file_type fileType,"
	    		 + "          createdAt,"
	    		 + "          updatedAt"
	    		 + "     FROM newsletters "
	    		 + " ORDER BY id DESC";
    	return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Newsletter>(Newsletter.class));
    }
    
    public void addNewsletter(String caption, MultipartFile file) throws IOException {
    	
    	UUID uuid = UUID.randomUUID();
    	String filename = uuid.toString() +"."+ FilenameUtils.getExtension(file.getOriginalFilename());
    	
    	String sql="INSERT INTO newsletters(caption,attachment,file_name,file_type) VALUES(?,?,?,?)";
    	jdbcTemplate.update(sql, new Object[] {
    			caption,
    			file.getOriginalFilename(),
    			filename,
    			file.getContentType()
    	});
    	
    	String path ="/opt/portal/newsletters/";
    	this.fileService.uploadFile(file, path, filename);
    }
    
    public void updateNewsletter(Newsletter news, MultipartFile file) throws IOException {
    	
    	UUID uuid = UUID.randomUUID();
    	String filename = uuid.toString() +"."+ FilenameUtils.getExtension(file.getOriginalFilename());
    
    	
    	
    	String sql="UPDATE newsletters SET caption =?, attachment= ?, file_name =?, file_type = ? WHERE id = ?";
    	jdbcTemplate.update(sql, new Object[] {
    			news.getCaption(),
    			file.getOriginalFilename(),
    			filename,
    			file.getContentType(),
    			news.getId()
    	});
    	
    	String path ="/opt/portal/newsletters/";
    	this.fileService.uploadFile(file, path, filename);
    }

	public Newsletter getNewsletter(Long id) {
	   	String sql="   SELECT id, "
	    		 + "          caption, "
	    		 + "          attachment, "
	    		 + "          file_name filename, "
	    		 + "          file_type fileType,"
	    		 + "          createdAt,"
	    		 + "          updatedAt"
	    		 + "     FROM newsletters "
	    		 + "    WHERE id =?";
	   	return jdbcTemplate.queryForObject(sql,new Object[] {id}, new BeanPropertyRowMapper<Newsletter>(Newsletter.class));
   
	}

	public void deleteNewsletter(Newsletter newsletter) {
		String sql ="DELETE FROM newsletters WHERE id = ?";
		jdbcTemplate.update(sql, new Object[] {newsletter.getId()});
		
	}
    
  
	
}
