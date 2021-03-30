package sportal.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text) {

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("itpisibg@gmail.com");
        email.setTo(to);
        email.setSubject(subject);
        email.setText(text);
        emailSender.send(email);

    }
}
