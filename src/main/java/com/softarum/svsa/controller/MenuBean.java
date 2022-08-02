package com.softarum.svsa.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.menu.MenuModel;

/**
 * @author murakamiadmin
 *
 */
@Named
@ViewScoped
public class MenuBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private LoginBean loginBean;	
		
	public MenuModel getMenu() {
		return loginBean.getMenu();
	}

	public long getTempo() {
		return loginBean.getTempoRestante();
	}
}
