package hr.fer.opp.project.services;

import hr.fer.opp.project.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import java.net.HttpURLConnection;
import java.net.URL;

public interface MailService {

	void sendNotification(User user);
}