package com.softarum.svsa.controller.autocad;

import com.softarum.svsa.modelo.UsuarioTemp;
import com.softarum.svsa.service.AutoCadUserService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Map;

@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class AutoCadUserParamsBean implements Serializable {

    private long id = 0L;
    private UsuarioTemp usuarioTemp;
    @Inject
    private AutoCadUserService autoCadUserService;

    public void validarUsuario(){
        Map<String, String> parametro = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        setId(Long.parseLong(parametro.get("userID")));
        log.info("Id do usuário: " + id);

        usuarioTemp = autoCadUserService.buscaUsuarioTemp(usuarioTemp);
        log.info("Nome do usuário registrado: " + usuarioTemp.getNome());
        log.info("E-mail do usuário registrado: " + usuarioTemp.getEmail());
    }
}
