package com.personiv.portal.mapextractor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.personiv.portal.model.User;

public class UsersMapExtractor implements ResultSetExtractor<List<User>>{

	@Override
	public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<Long,User> userMap = new HashMap<>();
		
		while(rs.next()) {
			Long id = rs.getLong("id");
			
			User user = userMap.get(id);

			Boolean enabled = rs.getBoolean("enabled");
			String authority = rs.getString("authority");
			String username = rs.getString("username");
			String fullname = rs.getString("fullname");
			Date createdAt = rs.getTimestamp("createdAt");
			
			if(user == null) {
				user = new User();
				user.setId(id);
				user.setUsername(username);
				user.setEnabled(enabled);
				user.setFullname(fullname);
				user.setCreatedAt(createdAt);
				List<String> authorities = new ArrayList<>();
				user.setAuthorities(authorities);
				userMap.put(id,user);
			}
			
			user.getAuthorities().add(authority);
			
			
		}
		return new ArrayList<User>(userMap.values());
	}

}
