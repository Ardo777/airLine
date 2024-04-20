package com.example.airlineproject.service.impl;

import com.example.airlineproject.entity.User;
import com.example.airlineproject.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    @Qualifier("emailTemplateEngine")
    private final TemplateEngine templateEngine;



    @Async
    public void sendMail(User user) {

        final Context ctx = new Context();
        ctx.setVariable("user", user);

        final String htmlContent = templateEngine.process("mail/welcome.html", ctx);

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setSubject("Welcome Fly Now  Social Network");
            message.setTo(user.getEmail());
            message.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void sendMail(String email, String messageContent) {
        try {
            final SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("Welcome Fly Now Social Network");
            mailMessage.setTo(email);
            mailMessage.setText(messageContent);
            javaMailSender.send(mailMessage);
            log.info("Mail sent successfully to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send mail to {}: {}", email, e.getMessage());
        }
    }


    @Async
    public void sendRecoveryMail(User user) {

        final Context ctx = new Context();
        ctx.setVariable("user", user);

        final String htmlContent = templateEngine.process("mail/recoveryPassword.html", ctx);

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setSubject("Recovery Password");
            message.setTo(user.getEmail());
            message.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void sendBirthdayMail(User user) {


        final Context ctx = new Context();
        ctx.setVariable("user", user);


        final String htmlContent = templateEngine.process("mail/recoveryPassword.html", ctx);


        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setSubject("Recovery Password");

            message.setTo(user.getEmail());
            message.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
