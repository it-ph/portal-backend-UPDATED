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

import com.personiv.portal.model.Form;
import com.personiv.portal.model.Newsletter;
import com.personiv.portal.service.FileService;

@Repository
@Transactional(readOnly = false)
public class FormDao extends JdbcDaoSupport{
	
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

    public List<Form> getForms(){
    	String sql="   SELECT id, "
	    		 + "          attachment, "
	    		 + "          file_name filename, "
	    		 + "          file_type fileType,"
	    		 + "          createdAt,"
	    		 + "          updatedAt"
	    		 + "     FROM forms "
	    		 + " ORDER BY id DESC";
    	return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Form>(Form.class));
    }
    
    public void addForm(MultipartFile file) throws IOException {
    	
    	UUID uuid = UUID.randomUUID();
    	String filename = uuid.toString() +"."+ FilenameUtils.getExtension(file.getOriginalFilename());
    	
    	String sql="INSERT INTO forms(attachment,file_name,file_type) VALUES(?,?,?)";
    	jdbcTemplate.update(sql, new Object[] {
    			file.getOriginalFilename(),
    			filename,
    			file.getContentType()
    	});
    	
    	String path ="/opt/portal/forms/";
    	this.fileService.uploadFile(file, path, filename);
    }
    
    public void updateForm(Form form, MultipartFile file) throws IOException {
    	
    	UUID uuid = UUID.randomUUID();
    	String filename = uuid.toString() +"."+ FilenameUtils.getExtension(file.getOriginalFilename());
    
    	
    	
    	String sql="UPDATE forms SET attachment= ?, file_name =?, file_type = ?, updatedAt = CURRENT_TIMESTAMP WHERE id = ?";
    	jdbcTemplate.update(sql, new Object[] {
    			file.getOriginalFilename(),
    			filename,
    			file.getContentType(),
    			form.getId()
    	});
    	
    	String path ="/opt/portal/forms/";
    	this.fileService.uploadFile(file, path, filename);
    }

	public void deleteForm(Form form) {
		String sql ="DELETE FROM forms WHERE id = ?";
		jdbcTemplate.update(sql, new Object[] {form.getId()});
	}

	public Form getForm(String filename) {
    	String sql="   SELECT id, "
	    		 + "          attachment, "
	    		 + "          file_name filename, "
	    		 + "          file_type fileType, "
	    		 + "          createdAt,"
	    		 + "          updatedAt "
	    		 + "     FROM forms "
	    		 + "    WHERE attachment = ?";
    	return jdbcTemplate.queryForObject(sql,new Object[] {filename}, new BeanPropertyRowMapper<Form>(Form.class));
 
	}
}
