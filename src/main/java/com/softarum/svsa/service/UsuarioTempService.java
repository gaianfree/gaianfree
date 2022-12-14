package com.softarum.svsa.service;

import com.softarum.svsa.modelo.UsuarioTemp;
import org.apache.commons.mail.EmailException;
import com.softarum.svsa.util.EmailUtil;
import com.softarum.svsa.util.AutoCadHtmlUtil;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
public class UsuarioTempService {

    public void envia(UsuarioTemp usuarioTemp) throws EmailException {
        String msgCorpo = AutoCadHtmlUtil.sendHtml(usuarioTemp.getValidacao());
        String email = usuarioTemp.getEmail();
        List<String> destinatario=new ArrayList<>();
        destinatario.add(email);
        EmailUtil.sendHtmlEmail("SSL",destinatario,"Confirmação de Cadastro",msgCorpo);
    }

    public void envia(String nome, List<String> email, String token, String assunto, String corpo) {
        EmailUtil.sendHtmlEmail("SSL", email, assunto, corpo);
    }
    public Boolean verifyToken(UsuarioTemp usuarioTemp){
        if(usuarioTemp.getValidacao().equals(usuarioTemp.getToken())) {
            return true;
        }
        else{
            return false;
        }
    }
}
