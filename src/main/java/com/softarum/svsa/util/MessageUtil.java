package com.softarum.svsa.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * @author murakamiadmin
 *
 */
public class MessageUtil {

	public static void sucesso(String message) {
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						message, message)); 
	}
	
	public static void erro(String message) {
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						message, message)); 
	}
	
	public static void alerta(String message) {
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_WARN,
						message, message)); 
	}
	
	public static void info(String message) {
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.FACES_MESSAGES,
						message)); 
	}
	
	
}