package com.softarum.svsa.dao;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.UsuarioTemp;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Getter
@Setter
public class AutoCadUserDAO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Boolean achouEmail;
    @Inject
    private EntityManager manager;

    public Boolean buscaEmail (UsuarioTemp tempUser) {

        UsuarioTemp usuarioTemp;
        try {
            usuarioTemp = manager.createQuery("SELECT u"
                            + " from UsuarioTemp u"
                            + " WHERE u.email = :email", UsuarioTemp.class)
                    .setParameter("email", tempUser.getEmail())
                    .getSingleResult();
            if (usuarioTemp != null) {
                setAchouEmail(true);
            }
        } catch (PersistenceException e1) {

            Usuario usuario;
            try {
                usuario = manager.createNamedQuery("Usuario.buscarPorEmail", Usuario.class)
                        .setParameter("email", tempUser.getEmail())
                        .getSingleResult();
                if (usuario != null) {
                    setAchouEmail(true);
                }
            } catch (PersistenceException e2) {
                setAchouEmail(false);
            }
        }

        return achouEmail;
    }
    public Long updateId (UsuarioTemp usuarioTemp) {

        return manager.createQuery("SELECT u.id"
                            + " from UsuarioTemp u"
                            + " WHERE u.email = :email", Long.class)
                    .setParameter("email", usuarioTemp.getEmail())
                    .getSingleResult();
    }
    @Transactional
    public void salvar(UsuarioTemp usuarioTemp) throws PersistenceException, NegocioException {
        try {
            UsuarioTemp user = manager.merge(usuarioTemp);
            log.info("usuariotemp gravado = " + user.getEmail());
            
        } catch (PersistenceException e) {
            e.printStackTrace();
            throw e;
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new NegocioException("Não foi possível executar a operação.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new NegocioException("Não foi possível executar a operação.");
        } catch (Error e) {
            e.printStackTrace();
            throw new NegocioException("Não foi possível executar a operação.");
        }
    }
    @Transactional
    public void excluir(UsuarioTemp usuarioTemp) throws NegocioException {

        try {
            manager.merge(usuarioTemp);
        } catch (PersistenceException e) {
            e.printStackTrace();
            throw new NegocioException("Não foi possível executar a operação.");
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new NegocioException("Não foi possível executar a operação.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new NegocioException("Não foi possível executar a operação.");
        } catch (Error e) {
            e.printStackTrace();
            throw new NegocioException("Não foi possível executar a operação.");
        }
    }
}
