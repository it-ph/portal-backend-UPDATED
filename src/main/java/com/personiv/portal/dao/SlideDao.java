package com.personiv.portal.dao;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.personiv.portal.model.Slide;
import com.personiv.portal.service.FileService;

@Repository
@Transactional(readOnly = false)
public class SlideDao extends JdbcDaoSupport{


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
    
    public List<Slide> getSlides(){
    	String sql="   SELECT id, "
	    		 + "          caption, "
	    		 + "          attachment, "
	    		 + "          file_name filename, "
	    		 + "          file_type fileType,"
	    		 + "          createdAt,"
	    		 + "          updatedAt"
	    		 + "     FROM slides "
	    		 + " ORDER BY id DESC";
    	return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Slide>(Slide.class));
    }

	public List<Slide> getSlidesBySequence() {
	 	String sql="   SELECT id, "
	    		 + "          caption, "
	    		 + "          attachment, "
	    		 + "          file_name filename, "
	    		 + "          file_type fileType,"
	    		 + "          createdAt,"
	    		 + "          updatedAt"
	    		 + "     FROM slides "
	    		 + " ORDER BY sequence ASC";
	 	return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Slide>(Slide.class));
  
	}
    
    public void addSlide(String caption, MultipartFile file) throws IOException {
    	
    	UUID uuid = UUID.randomUUID();
    	String filename = uuid.toString() +"."+ FilenameUtils.getExtension(file.getOriginalFilename());
    	
    	String sql="INSERT INTO slides(caption,attachment,file_name,file_type) VALUES(?,?,?,?)";
    	jdbcTemplate.update(sql, new Object[] {
    			caption,
    			file.getOriginalFilename(),
    			filename,
    			file.getContentType()
    	});
    	
    	String path ="/opt/portal/slides/";
    	this.fileService.uploadFile(file, path, filename);
    }
    
    public void updateSlide(Slide news, MultipartFile file) throws IOException {
    	
    	UUID uuid = UUID.randomUUID();
    	String filename = uuid.toString() +"."+ FilenameUtils.getExtension(file.getOriginalFilename());
    
    	
    	
    	String sql="UPDATE slides SET caption =?, attachment= ?, file_name =?, file_type = ? WHERE id = ?";
    	jdbcTemplate.update(sql, new Object[] {
    			news.getCaption(),
    			file.getOriginalFilename(),
    			filename,
    			file.getContentType(),
    			news.getId()
    	});
    	
    	String path ="/opt/portal/slides/";
    	this.fileService.uploadFile(file, path, filename);
    }

	public Slide getSlide(Long id) {
	   	String sql="   SELECT id, "
	    		 + "          caption, "
	    		 + "          attachment, "
	    		 + "          file_name filename, "
	    		 + "          file_type fileType,"
	    		 + "          createdAt,"
	    		 + "          updatedAt"
	    		 + "     FROM slides "
	    		 + "    WHERE id =?";
	   	return jdbcTemplate.queryForObject(sql,new Object[] {id}, new BeanPropertyRowMapper<Slide>(Slide.class));
   
	}

	public void deleteSlide(Slide slide) {
		String sql ="DELETE FROM slides WHERE id =?";
		jdbcTemplate.update(sql, new Object[] {slide.getId()});
		
	}

  
}
