package hr.fer.opp.project.services.impl;

import hr.fer.opp.project.entities.User;
import hr.fer.opp.project.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MailServiceJpa implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment env;

    public void sendNotification(User user) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(user.getEmail());
        mail.setFrom(env.getProperty("spring.mail.username"));
        mail.setSubject("Verifikacija e-mail adrese");
        mail.setText("Molimo potvrdite svoju e-mail adresu unosom verifikacijskog koda u za to predviđen tekstualni okvir: "
                + System.lineSeparator() + System.lineSeparator() + user.getVerificationCode());

        javaMailSender.send(mail);
    }
}
