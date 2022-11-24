package com.softarum.svsa.controller.autocad;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.util.MessageUtil;
import org.apache.commons.mail.EmailException;
import org.mindrot.jbcrypt.BCrypt;

import com.softarum.svsa.modelo.UsuarioTemp;
import com.softarum.svsa.service.AutoCadUserService;
import com.softarum.svsa.util.GenerateValidation;
import com.softarum.svsa.util.NegocioException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Getter
@Setter
@Named
@SessionScoped
public class AutoCadUserBean implements Serializable {

	private static final long serialVersionUID = 1L;
    private UsuarioTemp usuarioTemp;
    @Inject
    private AutoCadUserService autoCadUserService;
    private String password;

    @PostConstruct
    public void init() {
        usuarioTemp = new UsuarioTemp();
        usuarioTemp.setValidacao(GenerateValidation.keyValidation());
    }
    public String enviaEmail() throws EmailException {

        if(autoCadUserService.buscaEmail(usuarioTemp)) {
            MessageUtil.alerta("E-mail já está cadastrado. Use um e-mail diferente para se cadastrar.");
            return "";
        } else {
            autoCadUserService.envia(usuarioTemp);
            return "feedback.xhtml?faces-redirect=true";
        }
    }

    public String verificaToken() throws NegocioException {
        if (autoCadUserService.verifyToken(usuarioTemp)) {
        	
        	usuarioTemp.setSenha(BCrypt.hashpw(password, BCrypt.gensalt()));
        	
        	log.info("bean " + usuarioTemp.getEmail());
        	log.info("bean -> service " + autoCadUserService);

            salvarEmail();
            return "confirmado.xhtml?faces-redirect=true";
        } else {
            MessageUtil.alerta("A chave de acesso inserida é inválida. Tente novamente.");
            return "";
        }
    }

    public void salvarEmail() throws NegocioException {

        log.info("Usuario a ser armazenado:\nEmail: " + usuarioTemp.getEmail());
        log.info("Nome: " + usuarioTemp.getNome());
        autoCadUserService.salvar(usuarioTemp);

        log.info("Before TempUser Id update: " + usuarioTemp.getId());
        log.info("Loading update...");
        usuarioTemp.setId(autoCadUserService.updateId(usuarioTemp));
        log.info("Update completed.");
        log.info("After TempUser Id update: " + usuarioTemp.getId());

        MessageUtil.sucesso("Usuario salvo com sucesso!");
    }
}