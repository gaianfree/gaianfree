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
import com.softarum.svsa.modelo.AgendamentoFamiliar;
import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.StatusAtendimento;
import com.softarum.svsa.modelo.to.AtendimentoDTO;
import com.softarum.svsa.service.AgendamentoFamiliarService;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author Talita
 *
 */
public class AgendamentoFamiliarDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(AgendamentoFamiliarService.class);
	
	@Inject
	private EntityManager manager;	
	
	@Transactional
	public void salvarAgendamento(AgendamentoFamiliar af) throws NegocioException {
		
		
		// todo agendamento é uma ação.
		if(af.getCodigo() == null) {
			log.info("gravando agendamento familiar");
			gerarAcaoAgendamento(af, false, null);
		}
		else {
			log.info("Reagendamento familiar");
			// verifica se é reagendamento
			AgendamentoFamiliar atend = buscarPeloCodigo(af.getCodigo());
			
			// gera uma ação de reagendamento
			
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        String dataAntiga = dateFormat.format(atend.getDataAgendamento());
	        String dataNova = dateFormat.format(af.getDataAgendamento());
	        
			if(!dataNova.equals(dataAntiga)) {
				log.info("data alterou...de " +  dataAntiga + " para " + dataNova);
				gerarAcaoAgendamento(af, true, atend.getDataAgendamento());
			}			
		}
		try {
			manager.merge(af);
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
	/* cria uma ação para cada pessoa do atendimento familiar agendado. */	
	private void gerarAcaoAgendamento(AgendamentoFamiliar familiar, boolean reagendamento, Date novaData) throws NegocioException {	
		
		SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		Acao a = new Acao();
		a.setData(new Date());
		if(familiar.getTecnico() != null)
			a.setTecnico(familiar.getTecnico());
		a.setAgendador(familiar.getAgendador());
		a.setUnidade(familiar.getUnidade());
		a.setTenant_id(familiar.getTenant_id());
		a.setStatusAtendimento(StatusAtendimento.ATENDIDO);
		
		if(reagendamento) {
			a.setDescricao("Reagendado atendimento familiar de: " + out.format(novaData) + " para: "+ out.format(familiar.getDataAgendamento()) );
		}
		else {
			a.setDescricao("Agendado atendimento familiar para "+ out.format(familiar.getDataAgendamento()) );
		}
		
		a.setPessoas(new ArrayList<Pessoa>());
		
		for(Pessoa p : familiar.getPessoas()) {
			a.getPessoas().add(p);			
		}
		// salva ação com todas as pessoas agregadas.
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
	public void salvarAlterar(AgendamentoFamiliar familiar) throws NegocioException {
		try {
			manager.merge(familiar);
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
	public void salvarAtendFamiliar(AgendamentoFamiliar a, ListaAtendimento atendimento) throws NegocioException {	
		try {
			manager.merge(a);		
			manager.merge(atendimento);
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
	public void excluir(AgendamentoFamiliar a) throws NegocioException {
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
	@Transactional
	public void salvar(AgendamentoFamiliar lista) {				
		
		// todo agendamento é uma ação.
		if(lista.getCodigo() == null) {
			gerarAcaoAgendamento(lista, false, null);
		}
		else {
			// verifica se é reagendamento
			AgendamentoFamiliar atend = buscarPeloCodigo(lista.getCodigo());
			
			// gera uma ação de reagendamento
			if(!atend.getDataAgendamento().equals(lista.getDataAgendamento())) {
				gerarAcaoAgendamento(lista, true, atend.getDataAgendamento());
			}			
		}
		
		manager.merge(lista);
	}
	*/
	
	/*
	 * Buscas
	 */
	
	public AgendamentoFamiliar buscarPeloCodigo(Long codigo) {
		return manager.find(AgendamentoFamiliar.class, codigo);
	}	
	
	
	@SuppressWarnings("unchecked")
	public List<AgendamentoFamiliar> buscarTodos(Long tenantId) {
		return manager.createNamedQuery("AgendamentoFamiliar.buscarTodos")
				.setParameter("tenantId", tenantId)
				.getResultList();
	}
	
	/* ########################################
	 * Consulta usando DTO Projection  JPQL
	 * #########################################
	 */
	public List<AtendimentoDTO> buscarResumoAtendimentosTO(Pessoa pessoa, Long tenantId) {		
		
		List<AtendimentoDTO> lista = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.AtendimentoDTO( "
				+ "a.dataAtendimento, "
				+ "a.resumoAtendimento, "
				+ "c.nome, "
				+ "d.nome,"
				+ "b.nome, "
				+ "a.codigoAuxiliar) "
			+ "FROM AgendamentoFamiliar a "
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
	
	
	public List<AgendamentoFamiliar> buscarAtendimentosAgendados(Unidade unidade, Long tenantId) {			
		return manager.createNamedQuery("AgendamentoFamiliar.buscarAtendimentosAgendados", AgendamentoFamiliar.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("status", StatusAtendimento.AGENDADO)
				.getResultList();	
	}
	
	
	/* DashBoard */
	
	public Long buscarAtendimentosAtendidos(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("select count(a) from AgendamentoFamiliar a "
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
	
	/* grafico atendimentos familiares no relatorio de atendimentos */
	public List<AgendamentoFamiliar> buscarAtendimentosCodAuxPeriodo(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		return manager.createNamedQuery("AgendamentoFamiliar.buscarAtendimentosCodAuxPeriodo", AgendamentoFamiliar.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getResultList();
	}	
	
	public List<AgendamentoFamiliar> buscarAtendimentosCodAuxPeriodo(Unidade unidade, Long tenantId) {
		
		return manager.createNamedQuery("AgendamentoFamiliar.buscarAtendimentosCodAux", AgendamentoFamiliar.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getResultList();
	}	
	
	public List<PessoaReferencia> todasPessoasReferencia(Long tenantId) {		
        return manager.createQuery("from PessoaReferencia where p.excluida = :exc and p.tenant_id = :tenantId ", PessoaReferencia.class)
        		.setParameter("exc", false)
        		.setParameter("tenantId", tenantId)
        		.getResultList();
    }
		
	// criado para realização de testes unitários com JIntegrity
	public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}
	
	
	
}