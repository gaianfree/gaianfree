package com.softarum.svsa.service;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import com.softarum.svsa.dao.AtividadeDAO;
import com.softarum.svsa.dao.FrequenciaDAO;
import com.softarum.svsa.dao.InscricaoDAO;
import com.softarum.svsa.dao.ServicoDAO;
import com.softarum.svsa.modelo.Atividade;
import com.softarum.svsa.modelo.Frequencia;
import com.softarum.svsa.modelo.Inscricao;
import com.softarum.svsa.modelo.Servico;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Status;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;

import lombok.extern.log4j.Log4j;

/**
 * @author murakamiadmin
 *
 */
@Log4j
public class ServicoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private ServicoDAO servicoDAO;
	@Inject
	private AtividadeDAO atividadeDAO;
	@Inject
	private InscricaoDAO inscricaoDAO;
	@Inject
	private FrequenciaDAO frequenciaDAO;
		
	/*
	 * Servico
	 */
	
	public Servico salvar(Servico servico) throws NegocioException {			
		
		if(servico.getStatus() == Status.INATIVO) {
			servico.setDataFim(new Date());
		}
		return this.servicoDAO.salvar(servico);
	}	
	
	public void excluir(Servico servico, Long tenantId) throws NegocioException {
		
		log.info("excluir servico... " + servico.getNome());		
		
		Set<Atividade> atividades = atividadeDAO.buscarAtividades(servico, tenantId);
		
		log.info("excluir servico.. atividades =  " + atividades.size());		
		
		if(atividades.size() > 0) {
			throw new NegocioException("Existem atividades planejadas. Antes exclua as atividades do plano.");
		}
		else {
			servicoDAO.excluir(servico);
		}
	}
	
	/* cria um servico para o ano corrente */
	public void copiar(Servico servico) throws NegocioException {
		
		log.info("iniciando copia de serviço..." + servico.getNome());
		
		Servico copiaServico = null;		
					
		try {
			
			// criação de clones
			copiaServico =servico.clone();
			copiaServico.setCodigo(null);
			LocalDate data = LocalDate.now();
			copiaServico.setAno(data.getYear());
			copiaServico.setDataIni(DateUtils.asDate(data));
			copiaServico.setDataCriacao(null);
			log.info("servico copiado..." + copiaServico.getNome());
						
			// inscricoes
			Set<Inscricao> inscricoes = servico.getInscricoes();			
			
			if(inscricoes != null && !inscricoes.isEmpty()) {
				log.debug("criando inscricoes..." + inscricoes.size());
				
				for(Inscricao insc : inscricoes) {					
					insc.setCodigo(null);
					insc.setServico(copiaServico);
					insc.setData(copiaServico.getDataIni());
					insc.setDataCriacao(null);					
					//log.info("inscricao adicionada...");
				}
				
				log.debug("criado novas inscricoes = " + copiaServico.getInscricoes().size());
			}
			
			// atividades
			Set<Atividade> atividades = servico.getAtividades();
						
			if(atividades != null && !atividades.isEmpty()) {
				log.debug("criando atividades..." + atividades.size());
				
				for(Atividade ativ : atividades) {

					ativ.setCodigo(null);
					ativ.setServico(copiaServico);
					ativ.setData(copiaServico.getDataIni());
					ativ.setDataCriacao(null);
					//log.info("atividade adicionada...");
				}
				
				log.debug("criado novas atividades = " + copiaServico.getAtividades().size());				
			}
			
			copiaServico = servicoDAO.salvarCopia(copiaServico);
			log.debug("copia de serviço..." + copiaServico.getCodigo() + "" + copiaServico.getNome() + " realizada. Salvando no banco...");
			log.debug("atividades..." + copiaServico.getAtividades().size());
			log.debug("inscricoes..." + copiaServico.getInscricoes().size());
			
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new NegocioException("Problema na cópia do serviço (clone)");
		}	
		
	}
	
	public Servico buscarPeloCodigo(long codigo) {
		return this.servicoDAO.buscarPeloCodigo(codigo);
	}
	
	public List<Servico> buscarTodos(Long tenantId) {
		return servicoDAO.buscarTodos(tenantId);
	}
	
	public List<Servico> buscarServicosAno(Integer ano, Unidade unidade, Long tenantId) {
		return servicoDAO.buscarServicosAno(ano, unidade, tenantId);
	}
	// busca serviços por unidade executora
	public List<Servico> buscarServicosAnoExecutora(Integer ano, Unidade unidade, Long tenantId) {
		return servicoDAO.buscarServicosAnoExecutora(ano, unidade, tenantId);
	}
	
	public List<Servico> buscarServicos(Unidade unidade, Long tenantId) {
		return servicoDAO.buscarServicos(unidade, tenantId);
	}
	
	public List<Servico> buscarServicos(Unidade unidade, Date ini, Date fim, Long tenantId) {
		if(ini != null)
			if(fim != null)
				return servicoDAO.buscarServicosPeriodo(unidade, ini, fim, tenantId);
			else
				return servicoDAO.buscarServicosPeriodo(unidade, ini, new Date(), tenantId);
		return servicoDAO.buscarServicosPeriodo(unidade, tenantId);
	}
	public List<Servico> buscarServicosExec(Unidade unidade, Date ini, Date fim, Long tenantId) {
		if(ini != null)
			if(fim != null)
				return servicoDAO.buscarServicosPeriodoExec(unidade, ini, fim, tenantId);
			else
				return servicoDAO.buscarServicosPeriodoExec(unidade, ini, new Date(), tenantId);
		return servicoDAO.buscarServicosPeriodoExec(unidade, tenantId);
	}
	
	
	public Long qdeServicosAtivos(Unidade unidade, Long tenantId) {
		return servicoDAO.qdeServicosAtivos(unidade, tenantId);
	}
	
	
	/*
	 * Atividade
	 */
	
	public void salvar(Atividade atividade) throws NegocioException {			
		
		this.atividadeDAO.salvar(atividade);
	}	
	
	public void excluir(Atividade atividade, Long tenantId) throws NegocioException {
		
		if(!frequenciaDAO.verificarFrequencia(atividade, tenantId)) {
			atividadeDAO.excluir(atividade);
		}else {
			throw new NegocioException("Essa atividade não pode ser excluída porque já existe frequência registrada.");
		}		
	}
	
	public Atividade buscarAtividadePeloCodigo(long codigo) {
		return this.atividadeDAO.buscarPeloCodigo(codigo);
	}
	
	public Set<Atividade> buscarTodosAtividade(Long tenantId) {
		return atividadeDAO.buscarTodos(tenantId);
	}
	
	public Set<Atividade> buscarAtividades(Servico servico, Long tenantId) {
		return atividadeDAO.buscarAtividades(servico, tenantId);
	}
		
	
	/*
	 * Inscricao
	 */

	public void adicionar(Inscricao inscricao) throws NegocioException {			
		
		this.inscricaoDAO.adicionar(inscricao);
	}
	public void cancelar(Inscricao inscricao) throws NegocioException {			
		
		this.inscricaoDAO.cancelar(inscricao);
	}
	public void alterar(Inscricao inscricao) throws NegocioException {			
		
		this.inscricaoDAO.alterar(inscricao);
	}	
	public void excluir(Inscricao inscricao, Long tenantId) throws NegocioException {
		
		log.debug("ServicoService excluir inscricao: " + inscricao.getCodigo());	
				
		Set<Atividade> atividades = atividadeDAO.buscarAtividades(inscricao.getServico(), tenantId);

		boolean temFrequencia = false;
		for(Atividade a: atividades) {
			
			if(frequenciaDAO.verificarFrequencia(a, inscricao.getPessoa(), tenantId)) {
				temFrequencia = true;
				break;
			}
		}
		
		if(temFrequencia) {
			throw new NegocioException("Essa pessoa não pode ser excluída porque já existe frequência registrada.");
		}
		inscricaoDAO.excluir(inscricao);
		log.info("inscricao excluida: " + inscricao.getCodigo());		
	}

	public Set<Inscricao> buscarInscricoes(Servico servico, Long tenantId) {
		return inscricaoDAO.buscarInscricoes(servico, tenantId);
	}
	
	public Set<Inscricao> buscarInscricoesAtivas(Servico servico, Long tenantId) {
		return inscricaoDAO.buscarInscricoesAtivas(servico, tenantId);
	}
	
	public Long qdeInscricoesAtivas(Servico servico, Long tenantId) {
		return inscricaoDAO.qdeInscricoesAtivas(servico, tenantId);
	}

	public long buscarQdePessoas(Unidade unidade, Date ini, Date fim, Long tenantId) {
		if(ini != null)
			if(fim != null)
				return inscricaoDAO.buscarQdePessoas(unidade, ini, fim, tenantId);
			else
				return inscricaoDAO.buscarQdePessoas(unidade, ini,  new Date(), tenantId);			
		return inscricaoDAO.buscarQdePessoasTotal(unidade, tenantId);
	}
	public long buscarQdePessoasExec(Unidade unidade, Date ini, Date fim, Long tenantId) {
		if(ini != null)
			if(fim != null)
				return inscricaoDAO.buscarQdePessoasExec(unidade, ini, fim, tenantId);
			else
				return inscricaoDAO.buscarQdePessoasExec(unidade, ini,  new Date(), tenantId);			
		return inscricaoDAO.buscarQdePessoasExecTotal(unidade, tenantId);
	}
	
	/*
	 * Frequencia
	 */
	public List<Frequencia> carregarFrequencias(List<Inscricao> inscricoes, Atividade atividade, Servico servico, Long tenantId) throws NegocioException {	
		
		log.info("##Carregar Frequencias inicio = " + LocalDateTime.now());
		List<Frequencia> frequencias = buscarFrequencias(atividade, servico, tenantId);	
		
		if(inscricoes.size() != frequencias.size()) {
		
			for(Inscricao i : inscricoes) {				
				
				Frequencia freq = null;
				try {
					 freq = frequenciaDAO.buscarFrequencia(atividade, i.getPessoa(), tenantId);
					 
				}catch(NoResultException e) {
					if(freq == null) {
		
						freq = new Frequencia();
						freq.setAtividade(atividade);
						freq.setPessoa(i.getPessoa());
						freq.setPresente(false);
						freq.setTenant_id(tenantId);
						log.debug("NOVA frequencia criada no buscarFrequencias()");
						frequenciaDAO.salvar(freq);
					}
				}
				
			} // for
			frequencias = buscarFrequencias(atividade, servico, tenantId);
		}
		log.info("##Carregar Frequencias fim = " + LocalDateTime.now());
		log.info("Frequencias = " + frequencias.size());
		return frequencias;
	}	
	
	public void salvar(Frequencia frequencia) throws NegocioException {		
		log.debug("Salvar Frequencia");
		frequenciaDAO.salvar(frequencia);
}
	
	public List<Frequencia> buscarFrequencias(Atividade atividade, Servico servico, Long tenantId) {
		return frequenciaDAO.buscarFrequencias(atividade, servico, tenantId);
	}
	
}