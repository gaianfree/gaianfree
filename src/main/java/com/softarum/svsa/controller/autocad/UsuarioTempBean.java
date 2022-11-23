package com.softarum.svsa.controller.autocad;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.util.NegocioException;
import org.apache.commons.mail.EmailException;

import com.softarum.svsa.modelo.UsuarioTemp;
import com.softarum.svsa.service.UsuarioTempService;
import com.softarum.svsa.util.GenerateValidation;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.mindrot.jbcrypt.BCrypt;

@Log4j
@Getter
@Setter
@Named
@SessionScoped
public class UsuarioTempBean implements Serializable {

	private static final long serialVersionUID = 1L;
    private UsuarioTemp usuarioTemp;
    @Inject
    private UsuarioTempService usuarioTempService;
    private String password;

    @PostConstruct
    public void init() {
        usuarioTemp = new UsuarioTemp();
        usuarioTempService = new UsuarioTempService();
        usuarioTemp.setValidacao(GenerateValidation.keyValidation());
    }
    public String enviaEmail() throws EmailException {
        usuarioTempService.envia(usuarioTemp);

        return "feedback.xhtml?faces-redirect=true";
    }

    public void setSenha() {

        usuarioTemp.setSenha(BCrypt.hashpw(password, BCrypt.gensalt()));
    }
    public String verificaToken() throws NegocioException {
        if (usuarioTempService.verifyToken(usuarioTemp)) {
            usuarioTempService.salvar(usuarioTemp);
            return "confirmado.xhtml?faces-redirect=true";
        } else {
            return "naoconfirmado.xhtml?faces-redirect=true";
        }
    }
}