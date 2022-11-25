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
    String msgCorpo;
    String email;
    List<String> destinatario = new ArrayList<>();

    public Boolean buscaEmail(UsuarioTemp usuarioTemp) {
        return autoCadUserDAO.buscaEmail(usuarioTemp);
    }

    public void envia(UsuarioTemp usuarioTemp) throws EmailException {
        msgCorpo = AutoCadHtmlUtil.sendHtml(usuarioTemp.getValidacao());
        email = usuarioTemp.getEmail();
        destinatario.add(email);
        EmailUtil.sendHtmlEmail("SSL",destinatario,"Confirmação de Cadastro",msgCorpo);
    }
    public Boolean verificaToken(UsuarioTemp usuarioTemp){
        return usuarioTemp.getValidacao().equals(usuarioTemp.getToken());
    }
    public void salva(UsuarioTemp usuarioTemp) throws NegocioException {
        autoCadUserDAO.salvar(usuarioTemp);
    }
    public Long atualizaId(UsuarioTemp usuarioTemp) {
        return autoCadUserDAO.atualizarId(usuarioTemp);
    }

}
