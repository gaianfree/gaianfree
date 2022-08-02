package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.softarum.svsa.dao.MPComposicaoDAO;
import com.softarum.svsa.dao.ProntuarioDAO;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.Status;
import com.softarum.svsa.util.NegocioException;

/**
 * Classe criada para desonerar a classe CapaProntuarioService, MPComposicaoFamiliaService
 * 
 * @author murakamiadmin
 *
 */
public class ProntuarioService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Logger log = Logger.getLogger(ProntuarioService.class);
	
	@Inject
	private ProntuarioDAO prontuarioDAO;
	@Inject
	private MPComposicaoDAO composicaoDAO;
	
		
	/*
	 *  cria um prontuario novo por exclusão de pessoa da composiçãoFamiliar 	
	 */
	public void criarProntuario(Pessoa pessoa, Usuario tecnico, Long tenantId) throws NegocioException {
		
		try {
			
			// Recuperar prontuario da pessoa
			Prontuario prontuario = prontuarioDAO.buscarPeloCodigo(pessoa.getFamilia().getProntuario().getCodigo());
			
			log.info("prontuario : " + prontuario);
			log.info("prontuario : " + prontuario.getCodigo());
			log.info("familia:   : " + prontuario.getFamilia() + " : " + prontuario.getFamilia().getCodigo());
			log.info("endereço:  : " + prontuario.getFamilia().getEndereco() + " : " + prontuario.getFamilia().getEndereco().getCodigo());
			log.info("pessoa ref : " + prontuario.getFamilia().getPessoaReferencia() + " : " + prontuario.getFamilia().getPessoaReferencia().getCodigo());
			
			// prontuário			
			Prontuario pront = prontuario.clone();		// clonagem	
			pront.setCodigo(null);
			pront.setDataEntrada(new Date());
			pront.setDataCriacao(pront.getDataCriacao());
			pront.setStatus(Status.ATIVO);
			pront.setObsComposicaoFamiliar(null);
			pront.setProntuario(null);			
			pront.setProntuarioVinculado(null);
			
			// familia
			pront.getFamilia().setCodigo(null);
			pront.getFamilia().setDataCriacao(pront.getDataCriacao());
			pront.getFamilia().setProntuario(pront);
			pront.getFamilia().setMembros(new ArrayList<>());
			
			// endereco
			pront.getFamilia().getEndereco().setCodigo(null);
			pront.getFamilia().getEndereco().setDataCriacao(pront.getDataCriacao());
							
			
			log.info("prontuario clone: " + pront);
			log.info("prontuario clone: " + pront.getCodigo());
			log.info("familia:   clone: " + pront.getFamilia() + " : " + pront.getFamilia().getCodigo());
			log.info("endereço:  clone: " + pront.getFamilia().getEndereco() + " : " + pront.getFamilia().getEndereco().getCodigo());
			log.info("pessoa ref clone: " + pront.getFamilia().getPessoaReferencia() + " : " + pront.getFamilia().getPessoaReferencia().getCodigo());
			
			
			// Gravar prontuário novo
			prontuario = prontuarioDAO.salvarDesmembramento(pront, pessoa, tecnico, tenantId);
	
			log.info("prontuario  novo: " + prontuario);		
			log.info("prontuario  novo: " + prontuario.getCodigo());
			log.info("familia:    novo: " + prontuario.getFamilia().getCodigo());
			log.info("endereço:   novo: " + prontuario.getFamilia().getEndereco().getCodigo());
			log.info("pessoa ref: novo: " + prontuario.getFamilia().getPessoaReferencia());
			log.info("qde membros novo: " + prontuario.getFamilia().getMembros().size());
			
		}
		catch(NegocioException ne) {
			ne.printStackTrace();
			throw ne;
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new NegocioException("Problema para salvar o prontuário.");
		}
	}
	
	public void transferirMembro(Pessoa pessoa, Long codigoProntDestino) throws NegocioException {
		
		// Recuperar prontuario destino	
		Prontuario prontuarioDestino = prontuarioDAO.buscarPeloCodigo(codigoProntDestino);
		
		pessoa.setFamilia(prontuarioDestino.getFamilia());
		
		composicaoDAO.salvar(pessoa);
		
	}


	public Prontuario buscarProntuario(Long codigo, Unidade unidade, Long tenantId) {
		
		return prontuarioDAO.buscarProntuario(codigo, unidade, tenantId);
	}
}
