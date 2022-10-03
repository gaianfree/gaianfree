package com.softarum.svsa.dao;

import com.softarum.svsa.modelo.UsuarioTemp;
import org.apache.commons.mail.EmailException;
import org.hibernate.Session;
<<<<<<< HEAD
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
=======
import com.softarum.svsa.util.email.*;

import lombok.Setter;

@Setter
public class UsuarioTempDAO {
    private Session session;

    public void save(UsuarioTemp usuarioTemp) throws EmailException {
        this.session.saveOrUpdate(usuarioTemp);//salva o objeto no banco de dados caso não possua um valor no id
        Mensagem mensagem = new Mensagem();
        mensagem.setDestino("eduarda.costa6016@gmail.com");
        mensagem.setTitulo("Teste de autenticação - título");
        mensagem.setMensagem("Teste de autenticação - corpo\nValidação: " + usuarioTemp.getValidacao());
        EMailUtil.enviaEmail(mensagem);
>>>>>>> b0267cc28b78e8d049ddf3430d547d8c62320560
    }
}
