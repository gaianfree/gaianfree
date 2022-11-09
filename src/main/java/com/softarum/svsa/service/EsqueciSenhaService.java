package com.softarum.svsa.service;

import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.mindrot.jbcrypt.BCrypt;

import com.softarum.svsa.dao.EsqueciSenhaDAO;
import com.softarum.svsa.dao.UsuarioDAO;
import com.softarum.svsa.modelo.EsqueciSenha;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.EmailUtil;
import com.softarum.svsa.util.NegocioException;

import lombok.extern.log4j.Log4j;

@Log4j
public class EsqueciSenhaService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private EsqueciSenhaDAO esqueciSenhaDAO;
	@Inject 
	private UsuarioDAO usuarioDAO;
	
	public void salvar(EsqueciSenha esqueciSenha) throws NegocioException, SQLIntegrityConstraintViolationException{
		this.esqueciSenhaDAO.salvar(esqueciSenha);
	}
	
	public void novoToken(String email) throws PersistenceException, NegocioException {
		try {this.usuarioDAO.buscarPeloEmail(email);}
		catch(NoResultException e) {
			throw new NegocioException("Email inválido.");
		}
		
		try {
			log.info("aqui");
			EsqueciSenha esqc = esqueciSenhaDAO.buscarEsqueciSenhaPeloEmail(email);
			if(this.verificaTokenValido(esqc)) {
				throw new NegocioException("Já existe um token válido para este email, verifique sua caixa de entrada.");
			}else {
				this.criaTokenMandaEmail(esqc);
			}
		} catch(NoResultException e) {
			EsqueciSenha esqc = new EsqueciSenha();
			esqc.setEmail(email);
			this.criaTokenMandaEmail(esqc);
			
		}
	
	}
	
	public void criaTokenMandaEmail(EsqueciSenha esqc) throws PersistenceException, NegocioException {
		log.info("MÉTODO DE ENVIAR EMAILS.");
		UUID token = UUID.randomUUID();
		esqc.setToken(token.toString());
		esqc.setIsExpired(false);
		this.esqueciSenhaDAO.salvar(esqc);
		EmailUtil.sendEmail("TLS", esqc.getEmail(), "Redefinir senha", "Olá, você solicitou a redefinição de senha \n"
				+ "Link para redefinição de senha: http://localhost:8080/svsafree/unrestricted/home/ResetSenha.xhtml?token="
				+ esqc.getToken() + "\n\nO link de acesso é válido por 15 minutos.");
		
	}
	
	public long calcularMinutos(LocalDateTime data) {
		long diff = ChronoUnit.MINUTES.between(data, LocalDateTime.now());
			return diff;
	}
	
	public Boolean verificaTokenValido(EsqueciSenha esqc) {
		log.info("ESTÁ EXPIRADO? " + esqc.getIsExpired() + "TEMPO VALIDO?: " + (this.calcularMinutos(DateUtils.asLocalDateTime(esqc.getDataReferencia())) < 15) );
		if ((esqc.getIsExpired() == false) && (this.calcularMinutos(DateUtils.asLocalDateTime(esqc.getDataReferencia())) < 15)) {
			return true;
		}
		else {
			return false; 
		}
	}
	
	public Boolean esqueciSenhaValidar(String token){
		try {
			EsqueciSenha esqc = this.esqueciSenhaDAO.buscarEsqueciSenhaPeloToken(token);
			
			if(this.verificaTokenValido(esqc)) {
				return true;
			}
			else {
				return false;
			}
		} catch(NoResultException e) {
			return false;
		}
	}
	
	public void alterarSenhaBanco(String token, String novaSenha) throws PersistenceException, NegocioException {
		Usuario userAlterado = this.usuarioDAO.buscarPeloEmail(this.esqueciSenhaDAO.buscarEsqueciSenhaPeloToken(token).getEmail());
		userAlterado.setSenha(BCrypt.hashpw(novaSenha, BCrypt.gensalt()));
		usuarioDAO.salvar(userAlterado);
		EsqueciSenha esqc = esqueciSenhaDAO.buscarEsqueciSenhaPeloEmail(esqueciSenhaDAO.buscarEmailPeloToken(token));
		esqc.setIsExpired(true);
		esqueciSenhaDAO.salvar(esqc);
	}
	

}
