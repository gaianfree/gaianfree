package com.softarum.svsa.dao;

import com.mysql.cj.xdevapi.SessionImpl;
import com.softarum.svsa.modelo.UsuarioTemp;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

import javax.inject.Inject;
import javax.persistence.*;
import java.io.Serializable;

public class UsuarioTempDAO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private EntityManager manager;

    @Transactional
    public void salvar(UsuarioTemp usuarioTemp) throws PersistenceException, NegocioException {
        try {
            manager.merge(usuarioTemp);
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
