package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.hibernate.mapping.Set;

import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.EsqueciSenha;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

public class EsqueciSenhaDAO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private EntityManager manager;
	
	@Transactional
	public void salvar(EsqueciSenha esqueciSenha) throws PersistenceException, NegocioException {
		try {
			manager.merge(esqueciSenha);
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
	public void excluir(Usuario usuario) throws NegocioException {
			
		try {
			manager.merge(usuario);
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
	
	/*
	 * Buscas
	 */
	public EsqueciSenha buscarEsqueciSenhaPeloToken(String token) throws NoResultException {
		 return manager.createQuery("select u from EsqueciSenha u where u.token = :token", EsqueciSenha.class)
					.setParameter("token", token)
					.getSingleResult();
	 }
	
	 public EsqueciSenha buscarEsqueciSenhaPeloEmail(String email) throws NoResultException {
		 return manager.createQuery("select u from EsqueciSenha u where u.email = :email", EsqueciSenha.class)
					.setParameter("email", email)
					.getSingleResult();
	 }
	 
	 public Boolean buscarIsExpired(String email) {
		 return manager.createQuery("select t.isExpired from EsqueciSenha t where t.email = :email", Boolean.class)
				 .setParameter("token", email)
				 .getSingleResult();	 
	 }
	 public Date buscarDataReferencia(String email) {
		 return manager.createQuery("select t.dataReferencia from EsqueciSenha t where t.email = :email", Date.class)
				 .setParameter("email", email)
				 .getSingleResult();
	 }
	 public String buscarEmailPeloToken(String token)throws NoResultException {
		 return manager.createQuery("select t.email from EsqueciSenha t where t.token = :token", String.class)
				 .setParameter("token", token)
				 .getSingleResult();
	 }
	
}
