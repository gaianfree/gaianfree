package com.softarum.svsa.dao;

import com.softarum.svsa.modelo.UsuarioTemp;
import org.apache.commons.mail.EmailException;
import org.hibernate.Session;
import com.softarum.svsa.util.EmailUtil;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
public class UsuarioTempDAO {
    //private Session session;

    public void envia(UsuarioTemp usuarioTemp) throws EmailException {
        //this.session.saveOrUpdate(usuarioTemp);//salva o objeto no banco de dados caso não possua um valor no id
        String msgCorpo = "Teste de autenticação - corpo\nValidação: " + usuarioTemp.getValidacao();
        String email = usuarioTemp.getEmail();
        List<String> destinatario=new ArrayList<>();
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
