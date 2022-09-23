package com.softarum.svsa.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import lombok.extern.log4j.Log4j;

@Log4j
public class EmailUtil {	

	/**
	 * Utility method to send simple HTML email
	 * 
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
	public static void sendEmail(String authenticator, String toEmail, String subject, String body) {				
		
		try {
			MimeMessage msg = new MimeMessage(getSession(authenticator));
			// set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");
			msg.setFrom(new InternetAddress("no_reply@gaian.com.br", "NoReply-JD"));
			msg.setReplyTo(InternetAddress.parse("no_reply@gaian.com.br", false));
			msg.setSubject(subject, "UTF-8");
			msg.setText(body, "UTF-8");
			msg.setSentDate(new Date());
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

			log.info("Message is ready");
			Transport.send(msg);

			log.info("EMail Sent Successfully!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendEmail(String authenticator, List<String> destinatarios, String subject, String body) {				
		
		try {
			MimeMessage msg = new MimeMessage(getSession(authenticator));
			// set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");
			msg.setFrom(new InternetAddress("no_reply@gaian.com.br", "NoReply-JD"));
			msg.setReplyTo(InternetAddress.parse("no_reply@gaian.com.br", false));
			msg.setSubject(subject, "UTF-8");
			msg.setText(body, "UTF-8");
			msg.setSentDate(new Date());

            for (String destinatario: destinatarios) {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            }

			//msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
			log.info("Message is ready");
			Transport.send(msg);

			log.info("EMails Sent Successfully!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	
	
	/**
	 * Utility method to send email with attachment
	 * 
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
	public static void sendAttachmentEmail(String authenticator, String toEmail, String subject, String body, String filename) {
	
		try {
			MimeMessage msg = new MimeMessage(getSession(authenticator));
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");
			msg.setFrom(new InternetAddress("no_reply@gaian.com.br", "NoReply-JD"));
			msg.setReplyTo(InternetAddress.parse("no_reply@gaian.com.br", false));
			msg.setSubject(subject, "UTF-8");
			msg.setSentDate(new Date());
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

			// Create the message body part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setText(body);

			// Create a multipart message for attachment
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Second part is attachment
			messageBodyPart = new MimeBodyPart();
			//String filename = "abc.txt";
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			msg.setContent(multipart);

			// Send message
			Transport.send(msg);
			System.out.println("EMail Sent Successfully with attachment!!");
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public static void sendAttachmentEmail(String authenticator, List<String> destinatarios, String subject, String body, String filename) {
		try {
			MimeMessage msg = new MimeMessage(getSession(authenticator));
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");
			msg.setFrom(new InternetAddress("no_reply@gaian.com.br", "NoReply-JD"));
			msg.setReplyTo(InternetAddress.parse("no_reply@gaian.com.br", false));
			msg.setSubject(subject, "UTF-8");
			msg.setSentDate(new Date());

            for (String destinatario: destinatarios) {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            }

			// Create the message body part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setText(body);

			// Create a multipart message for attachment
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Second part is attachment
			messageBodyPart = new MimeBodyPart();
			//String filename = "abc.txt";
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			msg.setContent(multipart);

			// Send message
			Transport.send(msg);
			System.out.println("EMails Sent Successfully with attachment!!");
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	
	
	
	/**
	 * Utility method to send image in email body
	 * 
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
	public static void sendImageEmail(String authenticator, String toEmail, String subject, String body, String filename) {
		
		try {
			MimeMessage msg = new MimeMessage(getSession(authenticator));
			
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress("no_reply@gaian.com.br", "NoReply-JD"));

			msg.setReplyTo(InternetAddress.parse("no_reply@gaian.com.br", false));

			msg.setSubject(subject, "UTF-8");

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

			// Create the message body part
			BodyPart messageBodyPart = new MimeBodyPart();

			messageBodyPart.setText(body);

			// Create a multipart message for attachment
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Second part is image attachment
			messageBodyPart = new MimeBodyPart();
			//String filename = "image.png";
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			// Trick is to add the content-id header here
			messageBodyPart.setHeader("Content-ID", "image_id");
			multipart.addBodyPart(messageBodyPart);

			// third part for displaying image in the email body
			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent("<h1>Attached Image</h1>" + "<img src='cid:image_id'>", "text/html");
			multipart.addBodyPart(messageBodyPart);

			// Set the multipart message to the email message
			msg.setContent(multipart);

			// Send message
			Transport.send(msg);
			System.out.println("EMail Sent Successfully with image!!");
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public static void sendImageEmail(String authenticator, List<String> destinatarios, String subject, String body, String filename) {
		
		try {
			MimeMessage msg = new MimeMessage(getSession(authenticator));
			
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress("no_reply@gaian.com.br", "NoReply-JD"));

			msg.setReplyTo(InternetAddress.parse("no_reply@gaian.com.br", false));

			msg.setSubject(subject, "UTF-8");

			msg.setSentDate(new Date());

            for (String destinatario: destinatarios) {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            }

			// Create the message body part
			BodyPart messageBodyPart = new MimeBodyPart();

			messageBodyPart.setText(body);

			// Create a multipart message for attachment
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Second part is image attachment
			messageBodyPart = new MimeBodyPart();
			//String filename = "image.png";
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			// Trick is to add the content-id header here
			messageBodyPart.setHeader("Content-ID", "image_id");
			multipart.addBodyPart(messageBodyPart);

			// third part for displaying image in the email body
			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent("<h1>Attached Image</h1>" + "<img src='cid:image_id'>", "text/html");
			multipart.addBodyPart(messageBodyPart);

			// Set the multipart message to the email message
			msg.setContent(multipart);

			// Send message
			Transport.send(msg);
			System.out.println("EMails Sent Successfully with image!!");
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	
	
	/*
	 * Configurations
	 */
	
	private static final String fromEmail = "no_reply@gaian.com.br"; // requires valid gmail id
	private static final String password = "S6P54jsWLq"; // correct password for gmail id	
	private static final String smtpHostServer = "smtp.titan.email"; // smtp host server	
	
	
	private static Session getSession(String authenticator) {
			
		Session session;
		
		if(authenticator.equals("TLS"))
			session = getSessionTLS();
		else 
			if(authenticator.equals("SSL"))
				session = getSessionSSL();
			else {
				/* sem autenticação */
				Properties props = System.getProperties();
				props.put("mail.smtp.host", smtpHostServer);
				session = Session.getInstance(props, null);
			}
		
		return session;
	}
	
	/**
	 * Outgoing Mail (SMTP) Server requires TLS or SSL: smtp.gmail.com (use
	 * authentication) Use Authentication: Yes Port for TLS/STARTTLS: 587
	 */
	private static Session getSessionTLS() {
		
		log.info("TLSEmail Start");
		Properties props = new Properties();
		props.put("mail.smtp.host", smtpHostServer); // SMTP Host
		props.put("mail.smtp.port", "587"); // TLS Port
		props.put("mail.smtp.auth", "true"); // enable authentication
		props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS
		
		// create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			// override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		
		return Session.getInstance(props, auth);
	}

	/**
	 * Outgoing Mail (SMTP) Server requires TLS or SSL: smtp.gmail.com (use
	 * authentication) Use Authentication: Yes Port for SSL: 465
	 */
	private static Session getSessionSSL() {
		
		log.info("SSLEmail Start");
		Properties props = new Properties();
		props.put("mail.smtp.host", smtpHostServer); // SMTP Host
		props.put("mail.smtp.socketFactory.port", "465"); // SSL Port
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // SSL Factory Class
		props.put("mail.smtp.auth", "true"); // Enabling SMTP Authentication
		props.put("mail.smtp.port", "465"); // SMTP Port
		
		Authenticator auth = new Authenticator() {
			// override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};

		return Session.getDefaultInstance(props, auth);
	}

}