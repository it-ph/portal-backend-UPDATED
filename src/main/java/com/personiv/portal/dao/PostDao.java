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

import com.personiv.portal.model.Post;
import com.personiv.portal.rowmapper.PostRowMapper;
import com.personiv.portal.service.FileService;

@Repository
@Transactional(readOnly = false)
public class PostDao extends JdbcDaoSupport{
	
	private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;
    
    @Autowired
	private FileService fileService;
    
    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }

    public List<Post> getPosts(){
    	String sql =" SELECT p.id,  " + 
				    "        u.fullName employeeName,  " +
				    "        u.id employeeId,  " + 
				    "		 p.post_comment comment,  " + 
				    "		 p.file_name attachment, " + 
				    "		 p.file_type fileType, " + 
				    "        u.avatar,"+
				    "		 p.createdAt,  " + 
				    "		 p.updatedAt " + 
				    "   FROM posts p " + 
				    "   JOIN users u ON p.user_id = u.id ";
    	return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Post>(Post.class));
    }
    public Post getPost(Long id){
    	String sql =" SELECT p.id,  " + 
				    "        u.fullName employeeName,  " +
				    "        u.id employeeId,  " + 
				    "		 p.post_comment comment,  " + 
				    "		 p.file_name attachment, " + 
				    "		 p.file_type fileType, " + 
				    "        u.avatar,"+
				    "		 p.createdAt,  " + 
				    "		 p.updatedAt " + 
				    "   FROM posts p " + 
				    "   JOIN users u ON p.user_id = u.id "+
    			    "  WHERE p.id =?";
    	return jdbcTemplate.queryForObject(sql, new Object[] {id}, new BeanPropertyRowMapper<Post>(Post.class));
    }
    
    
    public List<Post> getPostByPage(int page){
    	
    	int offset = (page - 1) * 5;
    	
    	String sql =" SELECT p.id,  " + 
    			    "        u.fullName employeeName,  " +
    			    "        u.id employeeId,  " + 
    			    "		 p.post_comment comment,  " + 
    			    "		 p.file_name attachment, " + 
    			    "		 p.file_type fileType, " + 
				    "        u.avatar,"+
    			    "		 p.createdAt,  " + 
    			    "		 p.updatedAt " + 
    			    "   FROM posts p " + 
    			    "   JOIN users u ON p.user_id = u.id "+
    			    "  ORDER BY p.id DESC LIMIT 5 OFFSET ?";
    	return jdbcTemplate.query(sql,new Object[] {offset}, new PostRowMapper());
    }

    public void addPost(Post post,Long id) {
    	
    	String sql ="INSERT INTO posts(user_id, post_comment) VALUES(?,?)";
    	
    	jdbcTemplate.update(sql, new Object[] {
        		id,
        		post.getComment()
        	});
    }
	
    public void addPostWithAttachment(Post post, Long id,MultipartFile file) throws IOException {
		
		UUID uuid = UUID.randomUUID();
    	String filename = uuid.toString() +"."+ FilenameUtils.getExtension(file.getOriginalFilename());
    	
    	String path ="/opt/portal/posts/";		
    	
    	String sql ="INSERT INTO posts(user_id, post_comment,file_name, file_type) VALUES(?,?,?,?)";
    	
    	jdbcTemplate.update(sql, new Object[] {
    		id,
    		post.getComment(),
    		filename,
    		file.getContentType()
    	});
    	
    	this.fileService.uploadFile(file, path, filename);
		
	}

	public void updatePost(Post post, MultipartFile file) throws IOException {
    	
    	if(file != null) {
    		
    		UUID uuid = UUID.randomUUID();
        	String filename = uuid.toString() +"."+ FilenameUtils.getExtension(file.getOriginalFilename());
        	
        	String path ="/opt/portal/posts/";		
        	
        	String updateWithFile ="UPDATE posts SET post_comment =?,file_name =?, file_type =? WHERE id = ?";
        	
    		jdbcTemplate.update(updateWithFile, new Object[] {
	    		post.getComment(),
	    		filename,
	    		file.getContentType(),
	    		post.getId()
	    	});
    	    	
	    	this.fileService.uploadFile(file, path, filename);
    	}else {
    		
    		String sql ="UPDATE posts SET post_comment =?,file_name =?, file_type =? WHERE id = ?";
        	
    		jdbcTemplate.update(sql, new Object[] {
	    		post.getComment(),
	    		null,
	    		null,
	    		post.getId()
	    	});
    	}
    
		
	}

	public void deletePost(Post post) {
		String sql= "DELETE FROM posts WHERE id =?";
		jdbcTemplate.update(sql, new Object[] {post.getId()});
		
	}
    
}
