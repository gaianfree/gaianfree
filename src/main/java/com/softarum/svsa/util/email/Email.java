package com.softarum.svsa.util.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Properties;

@AllArgsConstructor
@Getter
@Setter
public class Email {
    private final ArrayList<String> destinatarios;
    private final String assunto;
    private final String conteudo;

    final String REMETENTE = "gaianfree.teste@gmail.com";

    private Properties configurarPropriedades() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        return properties;
    }

    private Session configurarSessao() {
        return Session.getInstance(this.configurarPropriedades(), new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        REMETENTE,
                        "gaianfree.teste123"
                );
            }
        });
    }

    public boolean enviarEmail() {
        Session session = this.configurarSessao();
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(REMETENTE));
            for (String destinatario: this.destinatarios) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            }
            message.setSubject(this.assunto);
            message.setText(this.conteudo);

            Transport.send(message);
            return true;
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return false;
        }
    }
}
