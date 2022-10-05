package com.softarum.svsa.dao;

import com.softarum.svsa.modelo.UsuarioTemp;
import org.apache.commons.mail.EmailException;
import com.softarum.svsa.util.EmailUtil;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
public class UsuarioTempDAO {

    List<String> destinatario = new ArrayList<>();
    String email = new String();
    String msgCorpo = new String();
    public void envia(UsuarioTemp usuarioTemp) throws EmailException {

        msgCorpo = "Teste de autenticação - corpo\nValidação: " + usuarioTemp.getValidacao();
        email = usuarioTemp.getEmail();
        destinatario.add(email);
        EmailUtil.sendEmail("SSL",destinatario,"Token validação",msgCorpo);
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
