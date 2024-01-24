package com.zero.weightTracker.service;

import org.hibernate.validator.cfg.defs.UUIDDef;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.UUID;

@Service
public class MailService {

    @Value("${emailSender.user}")
    private String user;

    @Value("${emailSender.password}")
    private String password;

    public void sendVerificationEmail(String remitent, String UUIDToken, String username) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Crear sesion de correo
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {
            // Crear un objeto MimeMessage
            Message message = new MimeMessage(session);

            // Configurar la direccion del remitente
            message.setFrom(new InternetAddress(user));

            // Configurar asunto y contenido del mensaje
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(remitent));

            // Configurar el asunto y contenido del mensaje
            message.setSubject("Verifica tu email - WeightTracker | zerodev");


            String url = "http://localhost:8080/auth/verification?token=" + UUIDToken + "&username=" + username;
            message.setText("Usa este url para verificar tu email: " + url);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

}
