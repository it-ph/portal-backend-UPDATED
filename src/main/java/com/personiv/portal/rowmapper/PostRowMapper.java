package com.personiv.portal.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.personiv.portal.model.Employee;
import com.personiv.portal.model.Post;

public class PostRowMapper implements RowMapper<Post>{

	@Override
	public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Long id = rs.getLong("id");
		String employeeName = rs.getString("employeeName");
		Long employeeId = rs.getLong("employeeId");
		String avatar = rs.getString("avatar");
		
		String comment = rs.getString("comment");
		String attachment = rs.getString("attachment");
		String fileType = rs.getString("fileType");
		
		Date createdAt = rs.getTimestamp("createdAt");
		Date updatedAt = rs.getTimestamp("updatedAt");
		
		Employee emp = new Employee();
		emp.setId(employeeId);
		emp.setFullname(employeeName);
		emp.setAvatar(avatar);
		Post post = new Post();
		post.setId(id);
		post.setComment(comment);
		post.setAttachment(attachment);
		post.setFileType(fileType);
		post.setCreatedAt(createdAt);
		post.setUpdatedAt(updatedAt);
		
		post.setPostedBy(emp);
		
		return post;
	}

}
