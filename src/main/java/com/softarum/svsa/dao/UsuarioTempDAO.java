package com.softarum.svsa.dao;

import com.softarum.svsa.modelo.UsuarioTemp;
import org.apache.commons.mail.EmailException;
import org.hibernate.Session;
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
    }
}
