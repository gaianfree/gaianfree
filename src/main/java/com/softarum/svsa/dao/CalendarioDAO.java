package com.softarum.svsa.dao;

import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

import com.softarum.svsa.modelo.Calendario;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author murakamiadmin
 *
 */
public class CalendarioDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(CalendarioDAO.class);
	
	@Inject
	private EntityManager manager;
	

	@Transactional
	public Calendario merge(Calendario calendario) throws NegocioException {
		try {
			return manager.merge(calendario);
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
	public void excluir(Calendario calendario) throws NegocioException {
		calendario = buscarPeloCodigo(calendario.getCodigo());
		try {
			manager.remove(calendario);
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
	
	
	public Calendario buscarPeloCodigo(Long codigo) {
		return manager.find(Calendario.class, codigo);
	}
	
	@SuppressWarnings("unchecked")
	public List<Calendario> buscarTodos(Unidade unidade, Long tenantId) throws ParseException {
		
		LocalDateTime data = LocalDateTime.now();
		data = data.minusYears(1);
		log.info("Data inicio da busca: " + data);
				
		return manager.createNamedQuery("Calendario.buscarTodos")
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("data", data)
				.getResultList();
	}
	
	/*
	 * Verificar se data está ocupada
	 */
	public void verificaDataFeriados(Date data, Unidade unidade, Long tenantId) throws NegocioException {
		/*
		 * SELECT *
				FROM svsa_salto.calendario
				where '2020-11-18 07:00:00' >= startDate 
					AND '2020-11-18 07:00:00' <= endDate;
		 */
		
		//log.info("Data conferir : " + data);
		LocalDateTime dat = DateUtils.asLocalDateTime(data);		
		//log.info("Data conferir : " + dat);
		
		Long qde = manager.createQuery("SELECT count(c) from Calendario c "
				+ "where :data >= c.startDate and :data <= c.endDate "
				+ "and c.unidade = :unidade "
				+ "and c.tenant_id = :tenantId "
				+ "and c.tecnico is null", Long.class)
		.setParameter("data", dat)
		.setParameter("unidade", unidade)
		.setParameter("tenantId", tenantId)
		.getSingleResult();
		
	
		if(qde > 0) {
			throw new NegocioException("Verifique o calendário de feriados e datas comemorativas, existe restrição para esta data.");
		}		
	}
	/*
	 * Verificar se tecnico está ocupado (folga ou férias)
	 */
	public void verificaDataTecnico(Date data, Usuario tecnico, Long tenantId) throws NegocioException {
		/*
		 * SELECT *
				FROM svsa_salto.calendario
				where '2020-11-18 07:00:00' >= startDate 
					AND '2020-11-18 07:00:00' <= endDate
						AND codigo_tecnico = 74;
		 */
		
		//log.info("Data conferir : " + data);
		LocalDateTime dat = DateUtils.asLocalDateTime(data);
		//log.info("Data conferir : " + dat);
		
		Long qde = manager.createQuery("SELECT count(c) from Calendario c "
				+ "where :data >= c.startDate and :data <= c.endDate "
				+ "and c.tecnico = :tecnico "
				+ "and c.tenant_id = :tenantId ", Long.class)
		.setParameter("data", dat)
		.setParameter("tecnico", tecnico)
		.setParameter("tenantId", tenantId)
		.getSingleResult();
		
	
		if(qde > 0) {
			throw new NegocioException("O(a)" + tecnico.getNome() + " está de folga ou em férias. Verifique no calendário de feriados.");
		}		
	}
	
	/*
	 * Verificar se tecnico está agendado no mesmo dia e horário
	 */
	public void verificaAgendaTecnico(Date data, Usuario tecnico, Long tenantId) throws NegocioException {	
		
		Long qde = manager.createQuery("SELECT count(l) FROM ListaAtendimento l "
					+ "INNER JOIN AgendamentoColetivo c ON l.tecnico = c.tecnico "
					+ "INNER JOIN AgendamentoFamiliar f ON l.tecnico = f.tecnico "
				+ "WHERE (l.dataAgendamento = :data or c.dataAgendamento = :data or f.dataAgendamento = :data) "
					+ "and (l.tecnico = :tecnico or c.tecnico = :tecnico or f.tecnico = :tecnico) "
					+ "and l.tenant_id = :tenantId "
					+ "and (l.statusAtendimento = 'AGENDADO' or c.statusAtendimento = 'AGENDADO' or f.statusAtendimento = 'AGENDADO') ", Long.class)
		.setParameter("data", data, TemporalType.TIMESTAMP)
		.setParameter("tecnico", tecnico)
		.setParameter("tenantId", tenantId)
		.getSingleResult();
		
		log.info("Quantidade: "+qde);
		if(qde > 0) {
			throw new NegocioException("Já existe um agendamento para " + tecnico.getNome() + " neste horário. Porém, isso não impede de salvar.");

		}		
	}
	
	// criado para realização de testes unitários com JIntegrity
	public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}
		
}