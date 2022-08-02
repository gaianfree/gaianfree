package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.softarum.svsa.dao.AgendamentoFamiliarDAO;
import com.softarum.svsa.modelo.AgendamentoFamiliar;
import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.StatusAtendimento;
import com.softarum.svsa.modelo.to.AtendimentoDTO;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;

/**
 * @author Talita
 *
 */
public class AgendamentoFamiliarService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Logger log = Logger.getLogger(AgendamentoFamiliarService.class);
	
	
	@Inject
	private AgendamentoFamiliarDAO agendamentoFamiliarDAO;
	@Inject 
	private CalendarioAgendaHelper calendarioHelper;
	
	
	public void salvarAgendamento(AgendamentoFamiliar atendimento, Long tenantId) throws NegocioException {
		
		if (atendimento.getUnidade() == null) 
			throw new NegocioException("A unidade é obrigatória.");
		
		if (atendimento.getDataAgendamento() == null) {
			log.info("Data agendamento = " + atendimento.getDataAtendimento());
			throw new NegocioException("Defina a data do agendamento!");
		}
		else {
			verificarDisponibilidade(atendimento.getUnidade(), atendimento, tenantId);			
		}		
		
		atendimento.setStatusAtendimento(StatusAtendimento.AGENDADO);
		
		this.agendamentoFamiliarDAO.salvarAgendamento(atendimento);
	}
	public void verificarDisponibilidade(Unidade unidade, AgendamentoFamiliar atendimento, Long tenantId) throws NegocioException {
	
		if(atendimento.getTecnico() == null) {
			calendarioHelper.verificarDisponibilidade(unidade, atendimento.getDataAgendamento(), tenantId);
		}
		else {
			calendarioHelper.verificarDisponibilidade(atendimento.getTecnico(), atendimento.getDataAgendamento(), tenantId);
		}
		
	}
	
	public void salvarAtendFamiliar(AgendamentoFamiliar agendamento) throws NegocioException {		
		
		if (agendamento.getResumoAtendimento() == null || agendamento.getResumoAtendimento().equals("")) {
			throw new NegocioException("O resumo do atendimento é obrigatório.");	
		}
		
		if (agendamento.getDataAtendimento() == null ) {
			agendamento.setDataAtendimento(new Date());	
			log.info("Data atendimento = " + agendamento.getDataAtendimento());
		}
		
		agendamento.setStatusAtendimento(StatusAtendimento.ATENDIDO);
		
		/* cria um atendimento individualizado para pessoa de referencia */
		ListaAtendimento lista = new ListaAtendimento();
		lista.setCodigoAuxiliar(agendamento.getCodigoAuxiliar());
		lista.setDataAgendamento(agendamento.getDataAgendamento());
		lista.setDataAtendimento(agendamento.getDataAtendimento());
		lista.setPessoa(agendamento.getPessoaReferencia());
		lista.setResumoAtendimento(agendamento.getResumoAtendimento());
		lista.setRole(agendamento.getRole());
		lista.setStatusAtendimento(StatusAtendimento.ATENDIDO);
		lista.setTecnico(agendamento.getTecnico());		
		lista.setTecnicos(new HashSet<Usuario>(agendamento.getTecnicos()));
		lista.setUnidade(agendamento.getUnidade());
		lista.setTenant_id(agendamento.getTenant_id());
		
		this.agendamentoFamiliarDAO.salvarAtendFamiliar(agendamento, lista);
	}
	
	public void autoSave(AgendamentoFamiliar agendamento) throws NegocioException {			
			
		if (agendamento.getDataAtendimento() == null ) {
			agendamento.setDataAtendimento(new Date());	
			log.info("Auto save = " + agendamento.getDataAtendimento() );
		}
		
		this.agendamentoFamiliarDAO.salvarAgendamento(agendamento);
	}
	
	public void salvarAlterar(AgendamentoFamiliar item, Usuario usuario) throws NegocioException {
		
		if(usuario.getCodigo().longValue() == item.getTecnico().getCodigo().longValue()) {
			if(new Date().after(DateUtils.plusDays(item.getDataAtendimento(), 7)) ){
				throw new NegocioException("Prazo para alteração (7 dias) foi ultrapassado!");
					
			}
			else {
				agendamentoFamiliarDAO.salvarAlterar(item);
			}
		}
		else {
			throw new NegocioException("Somente o técnico que atendeu pode alterar o atendimento!");
		}	
		
	}
	
	public void excluir(AgendamentoFamiliar item) throws NegocioException {
		agendamentoFamiliarDAO.excluir(item);
		
	}	
	
	// ----------------------------------------------------------------------------
	
	
	public List<AtendimentoDTO> buscarResumoAtendimentosTO(Pessoa pessoa, Long tenantId) {	
		
		return agendamentoFamiliarDAO.buscarResumoAtendimentosTO(pessoa, tenantId);
	}

	public List<AgendamentoFamiliar> buscarAtendimentosAgendados(Unidade unidade, Long tenantId) {
		
		return agendamentoFamiliarDAO.buscarAtendimentosAgendados(unidade, tenantId);
	}

	public List<AgendamentoFamiliar> buscarAtendimentosCodAux(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		if(ini != null)
			if(fim != null)
				return agendamentoFamiliarDAO.buscarAtendimentosCodAuxPeriodo(unidade, ini, fim, tenantId);
			else
				return agendamentoFamiliarDAO.buscarAtendimentosCodAuxPeriodo(unidade, ini, new Date(), tenantId);
		return agendamentoFamiliarDAO.buscarAtendimentosCodAuxPeriodo(unidade, tenantId);
	}
	
	public List<PessoaReferencia> todasPessoasReferencia(Long tenantId) {
		return agendamentoFamiliarDAO.todasPessoasReferencia(tenantId);
	}	
	
}
