package sportal.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import sportal.model.pojo.User;

@Component
public class EmailService{

    @Autowired
    private EmailCfg emailCfg;

    public void sendConfirmationEmail(User user){

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.emailCfg.getHost());
        mailSender.setPort(this.emailCfg.getPort());
        mailSender.setUsername(this.emailCfg.getUsername());
        mailSender.setPassword(this.emailCfg.getPassword());

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("sportalProject@gmail.com");
        mailMessage.setTo("sportalProject@gmail.com");
        mailMessage.setSubject("registration notice");
        mailMessage.setText("You have registered in our web service project");

        mailSender.send(mailMessage);

    }

//    @Autowired
//    private JavaMailSender emailSender;
//
//    public void sendSimpleMessage(String to, String subject, String text) {
//
//        SimpleMailMessage email = new SimpleMailMessage();
//        email.setFrom("itpisibg@gmail.com");
//        email.setTo(to);
//        email.setSubject(subject);
//        email.setText(text);
//        emailSender.send(email);
//
//    }

}
