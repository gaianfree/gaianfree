package com.softarum.svsa.util.email;




import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import com.softarum.svsa.util.MessageUtil;

/**
 * @author murakamiadmin
 *
 */
public class EMailUtil {

	private static final String HOSTNAME = "smtp.gmail.com";
	private static final String USERNAME = "Svsa Desk";
	private static final String PASSWORD = "Svsa@*2021Desk";
	private static final String EMAILORIGEM = "svsadesk@gmail.com";

	public static Email conectaEmail() throws EmailException {
		Email email = new SimpleEmail();
		email.setHostName(HOSTNAME);
		email.setSmtpPort(587);
		email.setAuthenticator(new DefaultAuthenticator(USERNAME, PASSWORD));
		email.setStartTLSEnabled(true);
		email.setFrom(EMAILORIGEM);
		return email;
	}

	public static void enviaEmail(Mensagem mensagem) throws EmailException {
		Email email = new SimpleEmail();
		email = conectaEmail();
		email.setSubject(mensagem.getTitulo());
		email.setMsg(mensagem.getMensagem());
		email.addTo(mensagem.getDestino());
		String resposta = email.send();
		MessageUtil.sucesso("E-mail enviado com sucesso para: " + mensagem.getDestino() + " (" + resposta + ").");
		
	}
}