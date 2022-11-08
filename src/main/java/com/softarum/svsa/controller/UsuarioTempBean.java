package com.softarum.svsa.controller;

import com.softarum.svsa.controller.autocad.AutoCadSecBean;
import com.softarum.svsa.modelo.UsuarioTemp;
import com.softarum.svsa.service.UsuarioTempService;
import com.softarum.svsa.util.GenerateValidation;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.commons.mail.EmailException;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Log4j
@Getter
@Setter
@Named
@SessionScoped
public class UsuarioTempBean implements Serializable {
    // FacesContext
    private UsuarioTemp usuarioTemp;
    private UsuarioTempService usuarioTempService;
    private AutoCadSecBean autoCadSecBean;

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
            passaParametros();
            return "confirmado.xhtml?faces-redirect=true";
        }
        else{
            return "naoconfirmado.xhtml?faces-redirect=true";
        }
    }

    public void passaParametros() {

        autoCadSecBean = new AutoCadSecBean();
        autoCadSecBean.setUsuarioTemp(usuarioTemp);
        log.info("Nome UsuarioTemp: " + usuarioTemp.getNome());
        log.info("Nome AutoCadBean: " + autoCadSecBean.getUsuarioTemp().getNome());
        log.info("E-mail UsuarioTemp: " + usuarioTemp.getEmail());
        log.info("E-mail AutoCadBean: " + autoCadSecBean.getUsuarioTemp().getEmail());
        autoCadSecBean.inicializar();
    }
}
