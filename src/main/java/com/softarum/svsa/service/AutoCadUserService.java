package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.mail.EmailException;

import com.softarum.svsa.dao.AutoCadUserDAO;
import com.softarum.svsa.modelo.UsuarioTemp;
import com.softarum.svsa.util.AutoCadHtmlUtil;
import com.softarum.svsa.util.EmailUtil;
import com.softarum.svsa.util.NegocioException;

import lombok.extern.log4j.Log4j;

@Log4j
public class AutoCadUserService implements Serializable {


	private static final long serialVersionUID = 1L;
	
	@Inject
	private AutoCadUserDAO autoCadUserDAO;

    public Boolean buscaEmail(UsuarioTemp usuarioTemp) {
        return autoCadUserDAO.buscaEmail(usuarioTemp);
    }

    public void envia(UsuarioTemp usuarioTemp) throws EmailException {
        String msgCorpo = AutoCadHtmlUtil.sendHtml(usuarioTemp.getValidacao());
        String email = usuarioTemp.getEmail();
        List<String> destinatario=new ArrayList<>();
        destinatario.add(email);
        EmailUtil.sendHtmlEmail("SSL",destinatario,"Confirmação de Cadastro",msgCorpo);
    }
    public void salvar(UsuarioTemp usuarioTemp) throws NegocioException {
        autoCadUserDAO.salvar(usuarioTemp);
    }
    public Boolean verifyToken(UsuarioTemp usuarioTemp){
        if(usuarioTemp.getValidacao().equals(usuarioTemp.getToken())) {
            return true;
        }
        else{
            return false;
        }
    }
    public Long updateId(UsuarioTemp usuarioTemp) {
        return autoCadUserDAO.updateId(usuarioTemp);
    }
}
