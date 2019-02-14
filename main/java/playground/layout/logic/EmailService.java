package playground.layout.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import playground.aop.MyLogger;
import playground.aop.MyPerformanceCheck;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

	@MyLogger
	@MyPerformanceCheck
    public void sendSimpleMessage(Mail mail){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(mail.getSubject());
        message.setText(mail.getContent());
        message.setTo(mail.getTo());
        message.setFrom(mail.getFrom());

        emailSender.send(message);
    }

}
