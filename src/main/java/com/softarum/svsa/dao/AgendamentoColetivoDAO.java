package com.softarum.svsa.dao;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

import com.softarum.svsa.modelo.Acao;
import com.softarum.svsa.modelo.AgendamentoColetivo;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.StatusAtendimento;
import com.softarum.svsa.modelo.to.AtendimentoDTO;
import com.softarum.svsa.service.AgendamentoColetivoService;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author murakamiadmin
 *
 */
public class AgendamentoColetivoDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(AgendamentoColetivoService.class);
	
	@Inject
	private EntityManager manager;	
	
	@Transactional
	public void salvarAgendamento(AgendamentoColetivo col) throws NegocioException {			
		
		// todo agendamento gera uma ação. Criar ação na criação e no reagendamento.
		if(col.getCodigo() == null) {
			gerarAcaoAgendamento(col, false, null);
		}
		else {
			log.info("Reagendamento coletivo");
			// verifica se é reagendamento
			AgendamentoColetivo atend = buscarPeloCodigo(col.getCodigo());
			
			// gera uma ação de reagendamento
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        String dataAntiga = dateFormat.format(atend.getDataAgendamento());
	        String dataNova = dateFormat.format(col.getDataAgendamento());
	        
	        if(!dataNova.equals(dataAntiga)) {
				log.info("data alterou...de " +  dataAntiga + " para " + dataNova);
				gerarAcaoAgendamento(col, true, atend.getDataAgendamento());
			}			
		}
		try {
			manager.merge(col);
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
	/* cria uma ação para cada pessoa do atendimento coletivo agendado. */
	private void gerarAcaoAgendamento(AgendamentoColetivo coletivo, boolean reagendamento, Date novaData) throws NegocioException {
		
		SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		Acao a = new Acao();
		a.setData(new Date());
		if(coletivo.getTecnico() != null)
			a.setTecnico(coletivo.getTecnico());
		a.setAgendador(coletivo.getAgendador());
		a.setUnidade(coletivo.getUnidade());
		a.setTenant_id(coletivo.getTenant_id());
		a.setStatusAtendimento(StatusAtendimento.ATENDIDO);
		
		if(reagendamento) {
			log.info("Reagendado atendimento coletivo de: " + out.format(novaData) + " para: "+ out.format(coletivo.getDataAgendamento()) );
			a.setDescricao("Reagendado atendimento coletivo de: " + out.format(novaData) + " para: "+ out.format(coletivo.getDataAgendamento()) );
		}
		else {
			log.info("Agendado atendimento coletivo para "+ out.format(coletivo.getDataAgendamento()) );
			a.setDescricao("Agendado atendimento coletivo para "+ out.format(coletivo.getDataAgendamento()) );
		}
		
		a.setPessoas(new ArrayList<Pessoa>());
		
		for(Pessoa p : coletivo.getPessoas()) {
			a.getPessoas().add(p);			
		}
		// salva ação com todas as pessoas agregadas.
		log.info("salvando ação agendamento coletivo "+ out.format(coletivo.getDataAgendamento()) );
		
		try {
			manager.merge(a);
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
	
	
	@Transactional
	public void salvarAtendColetivo(AgendamentoColetivo a) throws NegocioException {	
		try {
			manager.merge(a);
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
	
		
	@Transactional
	public void excluir(AgendamentoColetivo a) throws NegocioException {
		a = buscarPeloCodigo(a.getCodigo());
		try {
			manager.remove(a);
			manager.flush();
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
	
	public AgendamentoColetivo buscarPeloCodigo(Long codigo) {
		return manager.find(AgendamentoColetivo.class, codigo);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<AgendamentoColetivo> buscarTodos(Long tenantId) {
		return manager.createNamedQuery("AgendamentoColetivo.buscarTodos")
				.setParameter("tenantId", tenantId)
				.getResultList();
	}
	
	/* ########################################
	 * Consulta atendimentos coletivos da pessoa usando DTO Projection  JPQL
	 * #########################################
	 */
	public List<AtendimentoDTO> buscarResumoAtendimentosTO(Pessoa pessoa, Long tenantId) {		
		/*
		 SELECT a.dataAtendimento AS data, 
				a.resumoAtendimento AS resumoAtendimento, 
				c.nome AS nomeTecnico, 
				d.nome AS nomeUnidade,
				b.nome AS nomePessoa
			FROM svsa_salto.AgendamentoColetivo a
				INNER JOIN svsa_salto.pessoaatendida r ON (a.codigo = r.codigo_agendamento)
				INNER JOIN svsa_salto.pessoa b ON (b.codigo = r.codigo_pessoa)
				INNER JOIN svsa_salto.usuario c ON (c.codigo = a.codigo_tecnico)
				INNER JOIN svsa_salto.unidade d ON (d.codigo = a.codigo_unidade)
			WHERE r.codigo_pessoa = 23018 and a.statusAtendimento = 'ATENDIDO';
		 */	
		List<AtendimentoDTO> lista = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.AtendimentoDTO( "
				+ "a.dataAtendimento, "
				+ "a.resumoAtendimento, "
				+ "c.nome, "
				+ "d.nome,"
				+ "b.nome, "
				+ "a.codigoAuxiliar) "
			+ "FROM AgendamentoColetivo a "
				+ "INNER JOIN a.pessoas r "
				+ "INNER JOIN Pessoa b ON b.codigo = r.codigo "
				+ "INNER JOIN Usuario c ON c.codigo = a.tecnico.codigo "
				+ "INNER JOIN Unidade d ON d.codigo = a.unidade.codigo "
			+ "WHERE r.codigo = :codigo_pessoa "
				+ "and a.tenant_id = :tenantId "
				+ "and a.statusAtendimento = :status ", AtendimentoDTO.class)
		.setParameter("codigo_pessoa", pessoa.getCodigo())
		.setParameter("tenantId", tenantId)
		.setParameter("status", StatusAtendimento.ATENDIDO)
		.getResultList();
		return lista;

	}
	
	public List<AgendamentoColetivo> buscarAtendimentosAgendados(Unidade unidade, Long tenantId) {			
		return manager.createNamedQuery("AgendamentoColetivo.buscarAtendimentosAgendados", AgendamentoColetivo.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("status", StatusAtendimento.AGENDADO)
				.getResultList();	
	}
	
	
	/* DashBoard */
	
	public Long buscarAtendimentosAtendidos(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("select count(a) from AgendamentoColetivo a "
				+ "where a.statusAtendimento = :status "
				+ "and a.unidade = :unidade "
				+ "and a.tenant_id = :tenantId "
				+ "and a.dataAtendimento between :ini and :fim ", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getSingleResult();
	}
	
	/* grafico atendimentos coletivos no relatorio de atendimentos */
	public List<AgendamentoColetivo> buscarAtendimentosCodAuxPeriodo(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		return manager.createNamedQuery("AgendamentoColetivo.buscarAtendimentosCodAuxPeriodo", AgendamentoColetivo.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getResultList();
	}	
	
	public List<AgendamentoColetivo> buscarAtendimentosCodAux(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("AgendamentoColetivo.buscarAtendimentosCodAux", AgendamentoColetivo.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getResultList();	
	}
	
			
	// criado para realização de testes unitários com JIntegrity
	public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}
	
	
	
}