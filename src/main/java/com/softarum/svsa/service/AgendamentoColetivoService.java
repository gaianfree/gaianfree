package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.softarum.svsa.dao.AgendamentoColetivoDAO;
import com.softarum.svsa.modelo.AgendamentoColetivo;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.StatusAtendimento;
import com.softarum.svsa.modelo.to.AtendimentoDTO;
import com.softarum.svsa.util.NegocioException;

/**
 * @author murakamiadmin
 *
 */
public class AgendamentoColetivoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Logger log = Logger.getLogger(AgendamentoColetivoService.class);
	
	
	@Inject
	private AgendamentoColetivoDAO agendamentoColetivoDAO;
	@Inject 
	private CalendarioAgendaHelper calendarioHelper;
	
	
	public void salvarAgendamento(AgendamentoColetivo atendimento, Long tenantId) throws NegocioException {
		
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
		
		this.agendamentoColetivoDAO.salvarAgendamento(atendimento);
	}
	
	public void verificarDisponibilidade(Unidade unidade, AgendamentoColetivo atendimento, Long tenantId) throws NegocioException {
		if(atendimento.getTecnico() == null) {
			calendarioHelper.verificarDisponibilidade(unidade, atendimento.getDataAgendamento(), tenantId);
		}
		else {
			calendarioHelper.verificarDisponibilidade(atendimento.getTecnico(), atendimento.getDataAgendamento(), tenantId);
		}
		
	}
	
	public void salvarAtendColetivo(AgendamentoColetivo agendamento) throws NegocioException {		
		
		if (agendamento.getResumoAtendimento() == null || agendamento.getResumoAtendimento().equals("")) {
			throw new NegocioException("O resumo do atendimento é obrigatório.");	
		}
		
		if (agendamento.getDataAtendimento() == null ) {
			agendamento.setDataAtendimento(new Date());	
			log.info("Data atendimento = " + agendamento.getDataAtendimento());
		}
		
		agendamento.setStatusAtendimento(StatusAtendimento.ATENDIDO);
		
		/* cria um atendimento individualizado para cada pessoa 
		List<ListaAtendimento> listaColetivo = new ArrayList<>();
		for(Pessoa p : agendamento.getPessoas()) {
			
			ListaAtendimento lista = new ListaAtendimento();
			lista.setCodigoAuxiliar(agendamento.getCodigoAuxiliar());
			lista.setDataAgendamento(agendamento.getDataAgendamento());
			lista.setDataAtendimento(agendamento.getDataAtendimento());
			lista.setPessoa(p);
			lista.setResumoAtendimento(agendamento.getResumoAtendimento());
			lista.setRole(agendamento.getRole());
			lista.setStatusAtendimento(StatusAtendimento.ATENDIDO);
			lista.setTecnico(agendamento.getTecnico());
			//lista.setTecnicos(agendamento.getTecnicos());
			lista.setTecnicos(new HashSet<Usuario>(agendamento.getTecnicos()));
			lista.setUnidade(agendamento.getUnidade());			
			listaColetivo.add(lista);
		}
		*/
		this.agendamentoColetivoDAO.salvarAtendColetivo(agendamento);
	}
	
	public void autoSave(AgendamentoColetivo agendamento) throws NegocioException {			
			
		if (agendamento.getDataAtendimento() == null ) {
			agendamento.setDataAtendimento(new Date());	
			log.info("Auto save = " + agendamento.getDataAtendimento() );
		}
		
		this.agendamentoColetivoDAO.salvarAgendamento(agendamento);
	}
	
	public void excluir(AgendamentoColetivo item) throws NegocioException {
		agendamentoColetivoDAO.excluir(item);
		
	}	
	
	
	// ----------------------------------------------------------------------------
	
	
	public List<AtendimentoDTO> buscarResumoAtendimentosTO(Pessoa pessoa, Long tenantId) {	
		
		return agendamentoColetivoDAO.buscarResumoAtendimentosTO(pessoa, tenantId);
	}

	public List<AgendamentoColetivo> buscarAtendimentosAgendados(Unidade unidade, Long tenantId) {
		
		return agendamentoColetivoDAO.buscarAtendimentosAgendados(unidade, tenantId);
	}

	public List<AgendamentoColetivo> buscarAtendimentosCodAux(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		if(ini != null)
			if(fim != null)
				return agendamentoColetivoDAO.buscarAtendimentosCodAuxPeriodo(unidade, ini, fim, tenantId);
			else
				return agendamentoColetivoDAO.buscarAtendimentosCodAuxPeriodo(unidade, ini, new Date(), tenantId);
		return agendamentoColetivoDAO.buscarAtendimentosCodAux(unidade, tenantId);
	}	
	
	
}
