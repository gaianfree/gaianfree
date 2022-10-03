package com.softarum.svsa.controller;

import com.softarum.svsa.dao.UsuarioTempDAO;
import com.softarum.svsa.modelo.UsuarioTemp;
import com.softarum.svsa.util.GenerateValidation;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.mail.EmailException;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Getter
@Setter
@Named
@ViewScoped
public class UsuarioBean implements Serializable {
    private UsuarioTemp usuarioTemp = null;
    private UsuarioTempDAO usuarioTempDAO = null;

    @PostConstruct
    public void init() {
        usuarioTemp = new UsuarioTemp();
        usuarioTempDAO = new UsuarioTempDAO();
    }

    public String save() throws EmailException {

        usuarioTemp.setValidacao(GenerateValidation.keyValidation());
        usuarioTempDAO.save(usuarioTemp);

        return "/public/feedback";
    }
}
