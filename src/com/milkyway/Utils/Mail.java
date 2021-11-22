/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.Utils;

import static com.milkyway.Model.addTable.addTable;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author hoang
 */
public class Mail {

    private final String emailSubject = "The new password send to from MilkyWay";

    public void sendEmail(String emailToAddress, String textMessage) {

        final String username = "hoangvan.flshin@gmail.com";//Enter your email
        final String password = "Vihoangvan257"; //Enter your password 

        Properties props = new Properties();

        props.put("mail.smtp.user", "username");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.EnableSSL.enable", "true");

        props.setProperty("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailToAddress));
            message.setSubject(emailSubject);
            message.setContent(addTable(textMessage), "text/html; charset=UTF-8");
            // send the email
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
