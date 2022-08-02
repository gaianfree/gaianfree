package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.softarum.svsa.dao.AcaoDAO;
import com.softarum.svsa.modelo.Acao;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.StatusAtendimento;
import com.softarum.svsa.modelo.to.AtendimentoDTO;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;

/**
 * @author murakamiadmin
 *
 */
public class AcaoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Logger log = Logger.getLogger(AcaoService.class);
	 
	
	@Inject
	private AcaoDAO acaoDAO;	

	public void salvar(Acao acao) throws NegocioException {
			
		if (acao.getUnidade() == null) 
			throw new NegocioException("A unidade é obrigatória.");
		
		if (acao.getData() == null) {
			log.info("Data agendamento = " + acao.getData());
			throw new NegocioException("Defina a data da ação");
		}	
		
		acao.setStatusAtendimento(StatusAtendimento.ATENDIDO);
		
		this.acaoDAO.salvar(acao);
	}
	public Acao autosave(Acao acao, Usuario usuario) throws NegocioException {			
		
		if (acao.getData() == null ) {	
			log.info("Auto save = " + acao.getData() + " pessoas: " + acao.getPessoas());
			throw new NegocioException("Defina a data da ação");
		}
		
		if(acao.getDescricao() == null || acao.getDescricao().isEmpty())			
			throw new NegocioException("A descrição não pode ser vazia!");
		else
			if(acao.getPessoas().isEmpty())
				throw new NegocioException("Deve ser adicionada pelo menos uma pessoa!");
		
		acao.setStatusAtendimento(StatusAtendimento.AGENDADO);
		return this.acaoDAO.salvar(acao);
	}
	
	public void salvarAlterar(Acao acao, Long codigoUsuarioLogado) throws NegocioException {
		
		log.info("para verificar o problema de alteração de ação...Ação criada por: " + acao.getTecnico().getCodigo() + " **** Tentativa de alteração por : " + codigoUsuarioLogado);
			
		if(codigoUsuarioLogado.longValue() == acao.getTecnico().getCodigo().longValue()) {
			if(new Date().after(DateUtils.plusDays(acao.getData(), 7)) ){
				throw new NegocioException("Prazo para alteração (7 dias) foi ultrapassado!");
					
			}
			else {
				acaoDAO.salvar(acao);
			}
		}
		else {
			throw new NegocioException("Somente o técnico que registrou pode alterar a ação! E isso só pode ser feito antes de 7 dias do registro.");
		}	
	}

	
	public List<AtendimentoDTO> buscarAcoes(Pessoa pessoa, Long tenantId) {
		
		List<AtendimentoDTO> dtos = new ArrayList<>();
		
		// ações antigas com pessoa unica
		List<Acao> acoes = acaoDAO.buscarAcoes(pessoa, tenantId);
		log.debug("Qde ações velhas da pessoa : (" + pessoa.getCodigo() + "-" + pessoa.getNome() + ") = " + acoes.size());		
		
		// ações novas com coleção de pessoas
		List<Acao> acoesNovas = acaoDAO.buscarPessoaAcao(pessoa, tenantId);
		log.debug("Qde ações novas da pessoa : (" + pessoa.getCodigo() + "-" + pessoa.getNome() + ") = " + acoesNovas.size());
				
		if (!acoes.isEmpty()) {
			
			if (!acoesNovas.isEmpty()) {
				acoes.addAll(acoesNovas);
			}
			
			for (Acao a : acoes) {
				
				log.debug(a.getCodigo() + "-" + a.getDescricao() + "-" + pessoa.getNome());
				
				AtendimentoDTO dto = new AtendimentoDTO();	
				dto.setData(a.getData());
				dto.setResumoAtendimento("[Ação] " + a.getDescricao());
				if (a.getTecnico() != null)
					dto.setNomeTecnico(a.getTecnico().getNome());
				dto.setNomeUnidade(a.getUnidade().getNome());
				dto.setNomePessoa(pessoa.getNome());
				if (a.getAgendador() != null)
					dto.setNomeAgendador(a.getAgendador().getNome());
				dtos.add(dto);
			}
		}
		
		log.debug("Qde ações : " + acoes.size());
		log.debug("Qde dtos ações : " + dtos.size());
		return dtos;
	}

	public List<Acao> buscarAcoesPendentes(Unidade unidade, Long tenantId) {
		return acaoDAO.buscarAcoesPendentes(unidade, tenantId);
	}
	
	public void excluir(Acao acao) throws NegocioException {
		this.acaoDAO.excluir(acao);
		
	}
	
	public List<Acao> buscarAcoesPeriodo(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		if(ini != null)
			if(fim != null) {
				log.info("ações periodo -- Ini: " + ini + " -- fim: " + fim);
				return acaoDAO.buscarAcoesPeriodo(unidade, ini, fim, tenantId);
			}
			else {
				log.info("ações periodo -- Ini: " + ini + " -- fim: " + fim);
				return acaoDAO.buscarAcoesPeriodo(unidade, ini, new Date(), tenantId);
			}
				
		return acaoDAO.buscarAcoesUnidade(unidade, tenantId);
	}
	
	public List<Acao> buscarAcao(Unidade unidade, Long tenantId) {
			
			return acaoDAO.buscarAcaoAndamento(unidade, tenantId);
		}


}
