package com.softarum.svsa.controller;

import com.softarum.svsa.modelo.UsuarioTemp;
import com.softarum.svsa.service.UsuarioTempService;
import com.softarum.svsa.util.GenerateValidation;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.mail.EmailException;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Getter
@Setter
@Named
@SessionScoped
public class UsuarioTempBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private UsuarioTemp usuarioTemp;
    private UsuarioTempService usuarioTempService;

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
    public String verificaToken(){
        if(usuarioTempService.verifyToken(usuarioTemp)){
            return "confirmado.xhtml?faces-redirect=true";
        }
        else{
            return "naoconfirmado.xhtml?faces-redirect=true";
        }
    }
}
