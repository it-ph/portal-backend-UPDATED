package com.personiv.portal.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.personiv.portal.mapextractor.UsersMapExtractor;
import com.personiv.portal.model.User;
import com.personiv.portal.rowmapper.UserRowMapper;


@Repository
@Transactional(readOnly = false)
public class UserDao extends JdbcDaoSupport{
	
	private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;
    
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }
    


	public User findUserById(Long id) {
		
		String sql = "SELECT u.id userId,u.username,u.enabled userEnabled, a.authority userAuth, u.fullname,u.email " + 
					 "  FROM users u " + 
					 "  JOIN authorities a ON u.username = a.username "+
					 " WHERE u.id = ?";
		User user = jdbcTemplate.queryForObject(sql,new Object[] {id}, new BeanPropertyRowMapper<User>(User.class));
	
		return user;
	}
	
	public List<User>getUsers(){
		
		String sql = "SELECT u.id, " + 
				"       u.username, " + 
				"		 u.fullName, " + 
				"		 u.password, " + 
				"		 u.accountNonLocked, " + 
				"		 u.accountNonExpired, " + 
				"		 u.credentialsNonExpired, " + 
				"        u.createdAt, "+
				"        u.updatedAt, "+
				"		 u.enabled, " + 
				"		 u.avatar, " + 
				"		 a.authority " + 
				"  FROM users u " + 
				"  JOIN authorities a ON a.username = u.username ";
		
		List<User> users =jdbcTemplate.query(sql,new UsersMapExtractor());
	
		return users;
	}


	public User getUserByUsername(String username) {
		
		String sql = "SELECT u.id, " + 
				"       u.username, " + 
				"		 u.fullName, " + 
				"		 u.password, " + 
				"		 u.accountNonLocked, " + 
				"		 u.accountNonExpired, " + 
				"		 u.credentialsNonExpired, " + 
				"        u.createdAt, "+
				"        u.updatedAt, "+
				"		 u.enabled, " + 
				"		 u.avatar, " + 
				"		 a.authority " + 
				"  FROM users u " + 
				"  JOIN authorities a ON a.username = u.username "+
				" WHERE u.username = ? "+
				" ORDER BY authority ASC";
		
		List<User> users = jdbcTemplate.query(sql,new Object[] {username}, new UsersMapExtractor());
		
		return users.isEmpty() ? null : users.get(0);
	}
	

	
	public void updatePassword(String username, String password) {
		
		
		String sql ="UPDATE users SET password =? WHERE username = ?";
		jdbcTemplate.update(sql, new Object[] {
				passwordEncoder().encode(password),
				username
		});
		
	}
	

	public List<User> getUserByRole(String role) {
		String sql = "SELECT u.id, u.employeeId,u.username,u.enabled, u.fullname, a.authority  role" + 
				 "  FROM users u " + 
			     "  JOIN authorities a ON a.username = u.username "+
				 " WHERE a.authority =?";
	
		return jdbcTemplate.query(sql,new Object[] {role}, new BeanPropertyRowMapper<User>(User.class));
	
	}
	
	public void addUser(User user) {
		String userSql ="INSERT INTO users(username,fullname,password) VALUES(?,?,?)";
		String roleSql ="INSERT INTO authorities(username,authority) VALUES(?,?)";
		
		jdbcTemplate.update(userSql, new Object[] {
				user.getUsername(),
				user.getFullname(),
				passwordEncoder().encode("!Welcome19")
		});
		
		jdbcTemplate.batchUpdate( roleSql, new BatchPreparedStatementSetter() {

			@Override
			public int getBatchSize() {
				return user.getAuthorities().size();
			}

			@Override
			public void setValues(PreparedStatement ps, int row) throws SQLException {
				String role = user.getAuthorities().get(row);
				ps.setString(1, user.getUsername());
				ps.setString(2, role);
			}
			
			
		});
		
	}
	/* Deletes current user role then insert the array of user roles*/
	public void updateUser(User user) {
		
		String roleSql ="INSERT INTO authorities(username,authority) VALUES(?,?)";
		String resetRole ="DELETE FROM authorities WHERE username =?";
		jdbcTemplate.update(resetRole, new Object[] {user.getUsername()});
		
		
		
		jdbcTemplate.batchUpdate( roleSql, new BatchPreparedStatementSetter() {

			@Override
			public int getBatchSize() {
				return user.getAuthorities().size();
			}

			@Override
			public void setValues(PreparedStatement ps, int row) throws SQLException {
				String role = user.getAuthorities().get(row);
				ps.setString(1, user.getUsername());
				ps.setString(2, role);
			}
			
			
		});
	}
	
	public void disableUser(User user) {
		String sql ="UPDATE users SET enabled = 0 WHERE id =?";
		jdbcTemplate.update(sql, new Object[] {user.getId()});
	}
	
	public void enableUser(User user) {
		String sql ="UPDATE users SET enabled = 1 WHERE id =?";
		jdbcTemplate.update(sql, new Object[] {user.getId()});
	}
	
	public void resetPassword(User user) {
		
		String sql ="UPDATE users SET password =? WHERE id =?";
		jdbcTemplate.update(sql, new Object[] {
				passwordEncoder().encode("!Welcome19"),
				user.getId()
		});
	}

	


	
}
