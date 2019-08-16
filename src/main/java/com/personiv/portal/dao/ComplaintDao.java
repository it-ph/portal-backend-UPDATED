package com.personiv.portal.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.personiv.portal.model.Complaint;
import com.personiv.portal.service.MailService;

@Repository
@Transactional(readOnly = false)
public class ComplaintDao extends JdbcDaoSupport{
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MailService mailService;
	
	@Value("${complaint-receiver}")
	private String complaintReceiver;
	
    @Autowired
    private DataSource dataSource;
    
    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }
    
    public List<Complaint> getComplaints(){
    	String sql ="SELECT id, sender, message, createdAt FROM complaints ORDER BY id DESC";
    	return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Complaint>(Complaint.class));
    }
    
    public void addComplaint(Complaint comp) throws MessagingException {
    	String sql ="INSERT INTO complaints(sender,message) VALUES(?,?)";
    	jdbcTemplate.update(sql, new Object[] {
    			comp.getSender(),
    			comp.getMessage()
    	});
    	
    	String message ="\n\nSender: "+comp.getSender()+"\n\nMessage: "+comp.getMessage();
    	String complaintReceiverCC = "sherryl.sanchez@personiv.com";
    	
    	mailService.sendMail(complaintReceiver, "We Hear You", message, "no-reply@personiv.com");
    	mailService.sendMail(complaintReceiverCC, "We Hear You", message, "no-reply@personiv.com");
    }
}
