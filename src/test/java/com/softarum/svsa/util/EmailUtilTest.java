package com.softarum.svsa.util;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import gaian.mail.EmailUtil;
import lombok.extern.log4j.Log4j;

@Log4j
class EmailUtilTest {
	
	//@Test
	void TLSEmailtest() {	

		EmailUtil.sendEmail("TLS", "murakami.edson@gmail.com", "TLSEmail Testing Subject", "TLSEmail Testing Body");
	}
	
	@Test
		void TLSEmailstest() {	
			ArrayList<String> destinatarios = new ArrayList<>();
			destinatarios.add("murakami.edson@gaian.com.br");
			destinatarios.add("murakami.edson@gmail.com");
			EmailUtil.sendEmail("TLS", destinatarios, "TLSEmails Testing Subject", "TLSEmails Testing Body");
		}

	//@Test
	void SSLEmailtest() {
		log.info("Session created");
		EmailUtil.sendEmail("SSL", "murakami.edson@gmail.com", "SSLEmail Testing Subject", "SSLEmail Testing Body");

	}
	
	//@Test
	void AttachmentEmailImagetest() {
		
		
		EmailUtil.sendAttachmentEmail("SSL", "murakami.edson@gmail.com", "SSLEmail Testing Subject with Attachment",
			"SSLEmail Testing Body with Attachment", 
			"C:\\Users\\murak\\Documents\\repos\\GitHub\\gaian\\gaianfree\\src\\main\\webapp\\resources\\css\\sistema.css");


		EmailUtil.sendImageEmail("SSL", "murakami.edson@gmail.com", "SSLEmail Testing Subject with Image",
				"SSLEmail Testing Body with Image", 
				"C:\\Users\\murak\\Documents\\repos\\GitHub\\gaian\\gaianfree\\src\\main\\webapp\\resources\\imagens\\gaian_t.png");

	}

}
