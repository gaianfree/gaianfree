package com.softarum.svsa.controller.autocad;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.PersistenceException;

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
        autoCadUserService.envia(usuarioTemp);

        return "feedback.xhtml?faces-redirect=true";
    }

    public String verificaToken() throws NegocioException {
        if (autoCadUserService.verifyToken(usuarioTemp)) {
        	
        	usuarioTemp.setSenha(BCrypt.hashpw(password, BCrypt.gensalt()));
        	
        	log.info("bean " + usuarioTemp.getEmail());
        	log.info("bean -> service " + autoCadUserService);

            verificarEmail();
            return "confirmado.xhtml?faces-redirect=true";
        } else {
            return "naoconfirmado.xhtml?faces-redirect=true";
        }
    }

    public void verificarEmail() {
        try {

            log.info("Usuario a ser armazenado:\nEmail: " + usuarioTemp.getEmail());
            log.info("Nome: " + usuarioTemp.getNome());
            autoCadUserService.salvar(usuarioTemp);
            MessageUtil.sucesso("Usuario salvo com sucesso!");

        } catch (PersistenceException e) {
            MessageUtil.erro("E-mail já cadastrado! Tente outro. O sistema não permite e-mails repetidos.");
            e.printStackTrace();
        } catch (NegocioException e) {
            e.printStackTrace();
            MessageUtil.erro(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.erro("Erro desconhecido. Contatar o administrador");
        }
    }
}