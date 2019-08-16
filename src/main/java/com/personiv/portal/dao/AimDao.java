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

import com.personiv.portal.model.Aim;
import com.personiv.portal.service.MailService;

@Repository
@Transactional(readOnly = false)
public class AimDao extends JdbcDaoSupport{
	
	private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;
    
    @Value("${complaint-receiver}")
	private String complaintReceiver;
	
    @Autowired
    private MailService mailService;
    
    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }
    
    public List<Aim> getAims(){
    	String sql ="   SELECT id, "
    			  + "          fullname, "
    			  + "          email, "
    			  + "          scenario, "
    			  + "          aim, "
    			  + "          department, "
    			  + "          createdAt "
    			  + "     FROM aims "
    			  + " ORDER BY id DESC";
    	return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Aim>(Aim.class));
    }
    
    public void addAim(Aim aim) throws MessagingException {
    	String sql ="INSERT INTO aims(fullname,email,scenario,aim,department) VALUES(?,?,?,?,?)";
    	jdbcTemplate.update(sql, new Object[] {
    			aim.getFullname(),
    			aim.getEmail(),
    			aim.getScenario(),
    			aim.getAim(),
    			aim.getDepartment()
    	});
    	
    	String receiver =aim.getEmail();
		String message ="Current Scenario: \n"+aim.getScenario()+"\n\n"
						+"Aim/Idea: \n"+aim.getAim()+"\n";
		String sender ="portal@personive.com";
		String complaintReceiverCC = "sherryl.sanchez@personiv.com";
	
		mailService.sendMail(receiver, "Aim/Ideas Matters", message, sender);
		mailService.sendMail(complaintReceiver, "Aim/Ideas Matters", message, sender);
		mailService.sendMail(complaintReceiverCC, "Aim/Ideas Matters", message, sender);
		
    }

}
