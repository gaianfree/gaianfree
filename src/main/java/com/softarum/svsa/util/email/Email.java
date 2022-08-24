package com.softarum.svsa.util.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Properties;

public class Email {
    private final static String REMETENTE = "gaianfree.teste@gmail.com";

    private static Properties configurarPropriedades() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        return properties;
    }
    private static Session configurarSessao() {
        return Session.getInstance(configurarPropriedades(), new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        REMETENTE,
                        System.getenv("GAIAN_ACCOUNT_PASSWORD")
                );
            }
        });
    }

    public static boolean enviarEmail(ArrayList<String> destinatarios, String assunto, String conteudo) {
        Session session = configurarSessao();
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(REMETENTE));
            for (String destinatario: destinatarios) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            }
            message.setSubject(assunto);
            message.setText(conteudo);

            Transport.send(message);
            return true;
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return false;
        }
    }
}
