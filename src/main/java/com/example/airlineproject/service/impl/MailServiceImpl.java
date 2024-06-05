package com.example.airlineproject.service.impl;

import com.example.airlineproject.dto.UserRegisterDto;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final     @Qualifier("emailTemplateEngine")
    TemplateEngine templateEngine;


    @Async
    public void sendMail(UserRegisterDto userRegisterDto) {

        final Context ctx = new Context();
        ctx.setVariable("userRegisterDto", userRegisterDto);

        mailSending(ctx, userRegisterDto.getEmail());
    }

    @Override
    public void sendMail(User user) {

        final Context ctx = new Context();
        ctx.setVariable("user", user);

        mailSending(ctx, user.getEmail());
    }

    private void mailSending(Context ctx, String email) {
        final String htmlContent = templateEngine.process("mail/welcome.html", ctx);

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setSubject("Welcome Fly Now  Social Network");
            message.setTo(email);
            message.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    @Async
    public void sendRecoveryMail(String email) {
        final Context ctx = new Context();
        ctx.setVariable("email", email);
        final String htmlContent = templateEngine.process("mail/recoveryPassword.html", ctx);
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setSubject("Recovery Password");
            message.setTo(email);
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
            message.setSubject("Happy Birthday");
            message.setTo(user.getEmail());
            message.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
