package com.personiv.portal.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.personiv.portal.model.User;


public class UserRowMapper implements RowMapper<User>{

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		
		
		Long id = rs.getLong("id");
		Boolean enabled = rs.getBoolean("enabled");
		String username = rs.getString("username");
		String fullname = rs.getString("fullname");
		Date createdAt = rs.getTimestamp("createdAt");
		Date updatedAt = rs.getTimestamp("updatedAt");
		
		
		user.setId(id);
		user.setEnabled(enabled);
		user.setUsername(username);
		user.setFullname(fullname);
		user.setCreatedAt(createdAt);
		user.setUpdatedAt(updatedAt);
		return user;
	}

}
