package com.softarum.svsa.controller;

import com.softarum.svsa.dao.ProfileDAO;
import com.softarum.svsa.modelo.Profile;
import com.softarum.svsa.modelo.UsuarioTemp;
import com.softarum.svsa.util.GenerateValidation;
import com.softarum.svsa.util.security.GenerateMD5;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;

@Getter
@Setter
public class UsuarioBean {
    private UsuarioTemp usuarioTemp = null;
    private Profile profile = null;
    private ProfileDAO profileDAO = null;

    @PostConstruct
    public void init() {
        usuarioTemp = new UsuarioTemp();
        profile = new Profile();
        profileDAO = new ProfileDAO();
    }

    public String save() {

        usuarioTemp.setSenha(GenerateMD5.generate(usuarioTemp.getSenha()));
        usuarioTemp.setValidacao(GenerateValidation.keyValidation());
        profile.setUsuarioTemp(usuarioTemp);
        profileDAO.save(profile);

        return "/public/feedback_login";
    }
}
