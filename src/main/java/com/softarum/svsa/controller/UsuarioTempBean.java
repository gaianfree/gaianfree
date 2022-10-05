package com.softarum.svsa.controller;

import com.softarum.svsa.dao.UsuarioTempDAO;
import com.softarum.svsa.modelo.UsuarioTemp;
import com.softarum.svsa.util.GenerateValidation;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.mail.EmailException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Getter
@Setter
@Named
@SessionScoped
//@ViewScoped
public class UsuarioTempBean implements Serializable {
    // FacesContext
    private UsuarioTemp usuarioTemp;
    private UsuarioTempDAO usuarioTempDAO;

    @PostConstruct
    public void init() {
        usuarioTemp = new UsuarioTemp();
        usuarioTempDAO = new UsuarioTempDAO();
        usuarioTemp.setValidacao(GenerateValidation.keyValidation());
    }
    public String enviaEmail() throws EmailException {
        usuarioTempDAO.envia(usuarioTemp);

        return "feedback.xhtml?faces-redirect=true";
    }
    public String verificaToken(){
        if(usuarioTempDAO.verifyToken(usuarioTemp)){
            return "confirmado.xhtml?faces-redirect=true";
        }
        else{
            return "naoconfirmado.xhtml?faces-redirect=true";
        }
    }
}
