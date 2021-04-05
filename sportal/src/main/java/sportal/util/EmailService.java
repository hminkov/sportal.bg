package sportal.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import sportal.model.pojo.User;

import java.util.Properties;

@Component
public class EmailService{

    @Autowired
    private EmailCfg emailCfg;

    public void sendConfirmationEmail(User user){
        JavaMailSenderImpl mailSender = getMailSender();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("sportalProject@gmail.com");
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("registration notice");
        mailMessage.setText("You have registered in our web service project with the username " + user.getUsername());

        mailSender.send(mailMessage);

    }

    public String sendForgotPasswordMail(String email) {
        JavaMailSenderImpl mailSender = getMailSender();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("sportalProject@gmail.com");
        mailMessage.setTo(email);
        mailMessage.setSubject("registration notice");
        mailMessage.setText("Life is hard without a password.");

        mailSender.send(mailMessage);
        return "Mail sent to " + email;
    }

    private JavaMailSenderImpl getMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.emailCfg.getHost());
        mailSender.setPort(this.emailCfg.getPort());
        mailSender.setUsername(this.emailCfg.getUsername());
        mailSender.setPassword(this.emailCfg.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return mailSender;
    }
}
