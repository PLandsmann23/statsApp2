package cz.landspa.statsapp2.service.impl;

import cz.landspa.statsapp2.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String replyTo, String to, String subject, String message) {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("noreply@patriklandsmann.cz");
            mailMessage.setReplyTo(replyTo);
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            mailSender.send(mailMessage);

    }

    @Override
    public void sendVerificationEmail(String to, String token) {

    }
}
