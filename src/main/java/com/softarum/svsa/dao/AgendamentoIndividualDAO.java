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
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import com.softarum.svsa.modelo.Acao;
import com.softarum.svsa.modelo.AgendamentoColetivo;
import com.softarum.svsa.modelo.AgendamentoFamiliar;
import com.softarum.svsa.modelo.BeneficioEventual;
import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.CodigoAuxiliarAtendimento;
import com.softarum.svsa.modelo.enums.StatusAtendimento;
import com.softarum.svsa.modelo.to.AtendimentoDTO;
import com.softarum.svsa.modelo.to.DatasIniFimTO;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author murakamiadmin
 *
 */
public class AgendamentoIndividualDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Logger log = Logger.getLogger(AgendamentoIndividualDAO.class);	
	
	
	@Inject
	private EntityManager manager;	
	
	@Transactional
	public void salvar(ListaAtendimento lista) throws NegocioException {				
		
		try {
			// todo agendamento é uma ação.
			if(lista.getCodigo() == null) {
				manager.merge(gerarAcaoAgendamento(lista, false, null));
			}
			else {
				// verifica se é reagendamento
				ListaAtendimento atend = buscarPeloCodigo(lista.getCodigo());
				
				// gera uma ação de reagendamento
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		        String dataAntiga = dateFormat.format(atend.getDataAgendamento());
		        String dataNova = dateFormat.format(lista.getDataAgendamento());
		        
		        if(!dataNova.equals(dataAntiga)) {				
					manager.merge(gerarAcaoAgendamento(lista, true, atend.getDataAgendamento()));
				}			
			}
			manager.merge(lista);
			
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
	public void salvarRecepcao(ListaAtendimento lista) throws NegocioException {
		try {
			manager.merge(lista);
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
	public void salvarAlterar(ListaAtendimento lista) throws NegocioException {	
		try {
			manager.merge(lista);
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
	public void salvarEncerramento(ListaAtendimento lista, BeneficioEventual beneficio) throws NegocioException {
		try {
			manager.merge(beneficio);
			manager.merge(lista);
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
	public void salvarEncerramento(ListaAtendimento lista) throws NegocioException {
		try {
			manager.merge(lista);		
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
	public ListaAtendimento autoSaveVisita(ListaAtendimento lista) throws NegocioException {
		try {
			return manager.merge(lista);
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
	
	private Acao gerarAcaoAgendamento(ListaAtendimento lista, boolean reagendamento, Date novaData) {		
		
		SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				
		Acao a = new Acao();
		a.setData(new Date());
		if(reagendamento) {
			a.setDescricao("Reagendado atendimento individualizado de: " + out.format(novaData) + " para: "+ out.format(lista.getDataAgendamento()) );
		}
		else {
			a.setDescricao("Agendado atendimento individualizado para "+ out.format(lista.getDataAgendamento()) );
		}		
		a.setPessoa(lista.getPessoa());
		a.setAgendador(lista.getAgendador());
		if(lista.getTecnico() != null)
			a.setTecnico(lista.getTecnico());
		a.setUnidade(lista.getUnidade());
		a.setTenant_id(lista.getTenant_id());
		a.setStatusAtendimento(StatusAtendimento.ATENDIDO);
		
		return a;		
	}	
		
	@Transactional
	public void excluir(ListaAtendimento lista) throws NegocioException {
		lista = buscarPeloCodigo(lista.getCodigo());
		try {
			manager.remove(lista);
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
	
	public ListaAtendimento buscarPeloCodigo(Long codigo) {
		return manager.find(ListaAtendimento.class, codigo);
	}
	
	@SuppressWarnings("unchecked")
	public List<ListaAtendimento> buscarTodos(Long tenantId) {
		return manager.createNamedQuery("ListaAtendimento.buscarTodos")
				.setParameter("tenantId", tenantId)
				.getResultList();
	}
	
	public ListaAtendimento ultimoAtendimento(Pessoa pessoa, Long tenantId) {
		
		String jpql = "SELECT la FROM ListaAtendimento la where la.statusAtendimento = :status "
				+ "and la.pessoa.codigo = :pessoa "
				+ "and la.tenant_id = :tenantId "
				+ "ORDER BY la.dataAtendimento DESC ";
		
		TypedQuery<ListaAtendimento> query = manager.createQuery(jpql, ListaAtendimento.class);		
		query.setParameter("pessoa", pessoa.getCodigo());
		query.setParameter("tenantId", tenantId);
		query.setParameter("status", StatusAtendimento.ATENDIDO);
		query.setMaxResults(1);
		
		try {			
			return query.getSingleResult();
		}catch(Exception e) {
			return null;
		}		
	}
	
	// para relatorioAcompPAIF
	public ListaAtendimento ultimoAtendimento(Long pessoa, Long tenantId) {
		
		String jpql = "SELECT la FROM ListaAtendimento la where la.statusAtendimento = :status "
				+ "and la.pessoa.codigo = :pessoa "
				+ "and la.tenant_id = :tenantId "
				+ "ORDER BY la.dataAtendimento DESC ";
		
		TypedQuery<ListaAtendimento> query = manager.createQuery(jpql, ListaAtendimento.class);		
		query.setParameter("pessoa", pessoa);
		query.setParameter("tenantId", tenantId);
		query.setParameter("status", StatusAtendimento.ATENDIDO);
		query.setMaxResults(1);
		
		try {			
			return query.getSingleResult();
		}catch(Exception e) {
			return null;
		}		
	}

	
	/*
	 * ATENDIMENTOS AGENDADOS
	 */
		
	public List<ListaAtendimento> buscarAtendimentosRole(Usuario usuarioLogado, Long tenantId) {		
		return manager.createNamedQuery("ListaAtendimento.buscarAtendimentosRole", ListaAtendimento.class)
							.setParameter("unidade", usuarioLogado.getUnidade())
							.setParameter("tenantId", tenantId)
							.setParameter("role", usuarioLogado.getRole())
							.setParameter("status", StatusAtendimento.AGENDADO)
							.getResultList();	
	}
	
	public List<ListaAtendimento> buscarAtendimentosTecnicos(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("ListaAtendimento.buscarAtendimentosTecnicos", ListaAtendimento.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("status", StatusAtendimento.AGENDADO)
				.getResultList();	
	}
	
	public List<ListaAtendimento> buscarAtendimentosAgendados(Unidade unidade, Long tenantId) {			
		return manager.createNamedQuery("ListaAtendimento.buscarAtendimentosAgendados", ListaAtendimento.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("status", StatusAtendimento.AGENDADO)
				.getResultList();	
	}
	public List<ListaAtendimento> buscarAtendimentosAgendados(Unidade unidade, Date ini, Long tenantId) {			
		return manager.createNamedQuery("ListaAtendimento.buscarAtendAgendados", ListaAtendimento.class)
				.setParameter("unidade", unidade)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDays(ini, 31), TemporalType.TIMESTAMP)
				.setParameter("tenantId", tenantId)
				.setParameter("status", StatusAtendimento.AGENDADO)
				.getResultList();	
	}
	
	public List<ListaAtendimento> buscarAgendaUsuario(Usuario usuario, Long tenantId) {			
		return manager.createNamedQuery("ListaAtendimento.buscarAgendaUsuario", ListaAtendimento.class)
				.setParameter("tecnico", usuario)
				.setParameter("tenantId", tenantId)
				.setParameter("status", StatusAtendimento.AGENDADO)
				.getResultList();	
	}
	
	public Long encontrarQuantidadeAgendados(Unidade unidade, Long tenantId) {
		return manager.createQuery("select count(la) from ListaAtendimento la "
				+ "where la.statusAtendimento = :status "
				+ "and la.tenant_id = :tenantId "
				+ "and la.unidade = :unidade", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("status", StatusAtendimento.AGENDADO)
				.getSingleResult();
	}

	public Long buscarPorPessoa(PessoaReferencia pessoaReferencia, Long tenantId) {
		return manager.createQuery("select count(la) from ListaAtendimento la "
				+ "where la.statusAtendimento = :status "
				+ "and la.tenant_id = :tenantId "
				+ "and la.pessoa.codigo = :codigo", Long.class)
				.setParameter("codigo", pessoaReferencia.getCodigo())
				.setParameter("tenantId", tenantId)
				.setParameter("status", StatusAtendimento.AGENDADO)
				.getSingleResult();
		
	}
	
	public List<ListaAtendimento> buscarAtendimentosPendentes(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("ListaAtendimento.buscarAtendimentosPendentes", ListaAtendimento.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("status", StatusAtendimento.EM_ATENDIMENTO)
				.getResultList();
	}
	
	
	
	
	/*
	 * ATENDIMENTOS ATENDIDOS
	 */
	
	
	public List<ListaAtendimento> buscarAtendimentosRecepcao(Usuario usuario, Date ini, Date fim, Long tenantId) {		
		return manager.createNamedQuery("ListaAtendimento.buscarAtendimentosRecepcao", ListaAtendimento.class)
				.setParameter("unidade", usuario.getUnidade())
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("codigoAux", CodigoAuxiliarAtendimento.ATENDIMENTO_RECEPCAO)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getResultList();	
	}
	
	
	/* ########################################
	 * Consulta atendimentos individualizados usando DTO Projection JPQL (Helper)
	 * #########################################
	 */
	public List<AtendimentoDTO> buscarResumoAtendimentosDTO(Pessoa pessoa, Long tenantId) {		
		/*
		 SELECT a.dataAtendimento, 
			a.resumoAtendimento, 
			c.nome AS nomeTecnico, 
			d.nome AS nomeUnidade,
			b.nome AS nomePessoa,
			a.codigoAuxiliar
		FROM svsa.listaatendimento a
			INNER JOIN svsa.pessoa b ON b.codigo = a.codigo_pessoa
			INNER JOIN svsa.usuario c ON c.codigo = a.codigo_tecnico
			INNER JOIN svsa.unidade d ON d.codigo = a.codigo_unidade
		WHERE a.codigo_pessoa = 31722 
			and a.tenant_id = 1
            and (a.codigoAuxiliar not in ('ATENDIMENTO_RECEPCAO') or a.codigoAuxiliar is null)
			and (a.statusAtendimento = "ATENDIDO" or a.statusAtendimento = "FALTOU")
		 */
		List<AtendimentoDTO> lista = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.AtendimentoDTO( "
				+ "a.dataAtendimento, "
				+ "a.resumoAtendimento, "
				+ "c.nome, "
				+ "d.nome, "
				+ "b.nome, "
				+ "a.codigoAuxiliar) "
			+ "FROM ListaAtendimento a "
				+ "INNER JOIN Pessoa b ON b.codigo = a.pessoa.codigo "
				+ "INNER JOIN Usuario c ON c.codigo = a.tecnico.codigo "
				+ "INNER JOIN Unidade d ON d.codigo = a.unidade.codigo "
			+ "WHERE a.pessoa.codigo = :codigo_pessoa "
			 	+ "and a.tenant_id = :tenantId "
				+ "and (a.codigoAuxiliar not in ('ATENDIMENTO_RECEPCAO') or a.codigoAuxiliar is null)"
				+ "and (a.statusAtendimento = :status or a.statusAtendimento = :falta)", AtendimentoDTO.class)
		.setParameter("codigo_pessoa", pessoa.getCodigo())
		.setParameter("tenantId", tenantId)
		.setParameter("status", StatusAtendimento.ATENDIDO)
		.setParameter("falta", StatusAtendimento.FALTOU)
		.getResultList();

		return lista;
	}
	
	
	
	
	/*
	 * RelatorioAtendimentoFamilia
	 */
	
	public List<ListaAtendimento> buscarAtendimentoFamilia(Unidade unidade, Prontuario prontuario, Long tenantId) {
		return manager.createNamedQuery("ListaAtendimento.buscarAtendimentoFamilia", ListaAtendimento.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("prontuario", prontuario)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getResultList();	
	}

	
	
	
	
	
	
	
	
	/*
	 * RelatorioAtendimentos
	 */
	
	
	
	/*
	 * Filtros para atender relatorio de atendimentos lazy
	 * 
	 * 
	 */	
	// filtros
	public List<ListaAtendimento> buscarComPaginacao(int first, int pageSize, Unidade unidade, DatasIniFimTO datasTO, String filtro, int opcao, Long tenantId) {
		
		List<ListaAtendimento> lista = new ArrayList<ListaAtendimento>();
		
		if(opcao == 1) {  // codigo pessoa
			lista = manager.createQuery("select la from ListaAtendimento la "
					+ " INNER JOIN Pessoa pes ON la.pessoa = pes "
					+ " INNER JOIN Familia fam ON pes.familia = fam "
					+ " INNER JOIN Prontuario pro ON fam.prontuario = pro "
					+ " INNER JOIN Unidade uni ON pro.unidade = uni "
					+ "where la.statusAtendimento = :status "
					+ " and uni = :unidade "
					+ " and la.tenant_id = :tenantId "
					+ " and pes.codigo = :filtro "
					+ " and la.codigoAuxiliar not in ('ATENDIMENTO_RECEPCAO')"
					+ " and la.dataAtendimento between :ini and :fim "
					+ " order by la.dataAtendimento", ListaAtendimento.class)
				.setParameter("ini", datasTO.getIni(), TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(datasTO.getFim()), TemporalType.TIMESTAMP)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("filtro", Long.valueOf(filtro))
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.setFirstResult(first)
				.setMaxResults(pageSize)
				.getResultList();				
		} else if(opcao == 2) {  // nome pessoa
			lista = manager.createQuery("select la from ListaAtendimento la "
					+ " INNER JOIN Pessoa pes ON la.pessoa = pes "
					+ " INNER JOIN Familia fam ON pes.familia = fam "
					+ " INNER JOIN Prontuario pro ON fam.prontuario = pro "
					+ " INNER JOIN Unidade uni ON pro.unidade = uni "
					+ "where la.statusAtendimento = :status "
					+ " and uni = :unidade "
					+ " and la.tenant_id = :tenantId "
					+ " and pes.nome LIKE :filtro "
					+ " and la.codigoAuxiliar not in ('ATENDIMENTO_RECEPCAO')"
					+ " and la.dataAtendimento between :ini and :fim "
					+ " order by la.dataAtendimento", ListaAtendimento.class)
				.setParameter("ini", datasTO.getIni(), TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(datasTO.getFim()), TemporalType.TIMESTAMP)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("filtro", filtro.toUpperCase() + "%")
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.setFirstResult(first)
				.setMaxResults(pageSize)
				.getResultList();				
		} else if(opcao == 3) {  // nome tecnico
			lista = manager.createQuery("select la from ListaAtendimento la "
					+ " INNER JOIN Pessoa pes ON la.pessoa = pes "
					+ " INNER JOIN Familia fam ON pes.familia = fam "
					+ " INNER JOIN Prontuario pro ON fam.prontuario = pro "
					+ " INNER JOIN Unidade uni ON pro.unidade = uni "
					+ "where la.statusAtendimento = :status "
					+ " and uni = :unidade "
					+ " and la.tenant_id = :tenantId "
					+ " and la.tecnico.nome LIKE :filtro "
					+ " and la.codigoAuxiliar not in ('ATENDIMENTO_RECEPCAO')"
					+ " and la.dataAtendimento between :ini and :fim "
					+ " order by la.dataAtendimento", ListaAtendimento.class)
				.setParameter("ini", datasTO.getIni(), TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(datasTO.getFim()), TemporalType.TIMESTAMP)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("filtro", filtro.toUpperCase() + "%")
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.setFirstResult(first)
				.setMaxResults(pageSize)
				.getResultList();				
		}		
		return lista;		
	}
	// quantidade total
	public Long encontrarQde(Unidade unidade, DatasIniFimTO datasTO, String filtro, int opcao, Long tenantId) {	
		
		Long qde = 0L;
		
		if(opcao == 1) {  // codigo pessoa			
			qde = manager.createQuery("select count(la) from ListaAtendimento la "
				+ " INNER JOIN Pessoa pes ON la.pessoa = pes "
				+ " INNER JOIN Familia fam ON pes.familia = fam "
				+ " INNER JOIN Prontuario pro ON fam.prontuario = pro "
				+ " INNER JOIN Unidade uni ON pro.unidade = uni "
				+ "where la.statusAtendimento = :status "
				+ " and uni = :unidade "
				+ " and la.tenant_id = :tenantId "
				+ " and pes.codigo = :filtro "
				+ " and la.codigoAuxiliar not in ('ATENDIMENTO_RECEPCAO')"
				+ " and la.dataAtendimento between :ini and :fim "
				+ " order by la.dataAtendimento", Long.class)
			.setParameter("ini", datasTO.getIni(), TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(datasTO.getFim()), TemporalType.TIMESTAMP)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("filtro", Long.valueOf(filtro))
			.setParameter("status", StatusAtendimento.ATENDIDO)
			.getSingleResult();
		} else if(opcao == 2) {  // nome pessoa			
			qde = manager.createQuery("select count(la) from ListaAtendimento la "
					+ " INNER JOIN Pessoa pes ON la.pessoa = pes "
					+ " INNER JOIN Familia fam ON pes.familia = fam "
					+ " INNER JOIN Prontuario pro ON fam.prontuario = pro "
					+ " INNER JOIN Unidade uni ON pro.unidade = uni "
					+ "where la.statusAtendimento = :status "
					+ " and uni = :unidade "
					+ " and la.tenant_id = :tenantId "
					+ " and pes.nome LIKE :filtro "
					+ " and la.codigoAuxiliar not in ('ATENDIMENTO_RECEPCAO')"
					+ " and la.dataAtendimento between :ini and :fim "
					+ " order by la.dataAtendimento", Long.class)
				.setParameter("ini", datasTO.getIni(), TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(datasTO.getFim()), TemporalType.TIMESTAMP)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("filtro", filtro.toUpperCase() + "%")
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getSingleResult();
		} else if(opcao == 3) {  // nome tecnico			
			qde = manager.createQuery("select count(la) from ListaAtendimento la "
					+ " INNER JOIN Pessoa pes ON la.pessoa = pes "
					+ " INNER JOIN Familia fam ON pes.familia = fam "
					+ " INNER JOIN Prontuario pro ON fam.prontuario = pro "
					+ " INNER JOIN Unidade uni ON pro.unidade = uni "
					+ "where la.statusAtendimento = :status "
					+ " and uni = :unidade "
					+ " and la.tenant_id = :tenantId "
					+ " and la.tecnico.nome LIKE :filtro "
					+ " and la.codigoAuxiliar not in ('ATENDIMENTO_RECEPCAO')"
					+ " and la.dataAtendimento between :ini and :fim "
					+ " order by la.dataAtendimento", Long.class)
				.setParameter("ini", datasTO.getIni(), TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(datasTO.getFim()), TemporalType.TIMESTAMP)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("filtro", filtro.toUpperCase() + "%")
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getSingleResult();
		}
		
		return qde;	
	}	
	
	// sem filtro 
	//SELECT * FROM svsa_salto.ListaAtendimento where codigo_unidade=1 and statusAtendimento = 'ATENDIDO' order by dataAtendimento;	
	public List<ListaAtendimento> buscarComPaginacao(int first, int pageSize, Unidade unidade, DatasIniFimTO datasTO, Long tenantId) {
		List<ListaAtendimento> lista = manager.createQuery("select la from ListaAtendimento la "				
				+ "where la.statusAtendimento = :status "
				+ " and la.unidade = :unidade "
				+ " and la.tenant_id = :tenantId "
				+ " and la.codigoAuxiliar not in ('ATENDIMENTO_RECEPCAO')"
				+ " and la.dataAtendimento between :ini and :fim "
				+ " order by la.dataAtendimento", ListaAtendimento.class)
			.setParameter("ini", datasTO.getIni(), TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(datasTO.getFim()), TemporalType.TIMESTAMP)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("status", StatusAtendimento.ATENDIDO)
			.setFirstResult(first)
			.setMaxResults(pageSize)
			.getResultList();			
		return lista;
	}
	public Long encontrarQde(Unidade unidade, DatasIniFimTO datasTO, Long tenantId) {		
		Long qde = manager.createQuery("select COUNT(la) from ListaAtendimento la "				
				+ "where la.statusAtendimento = :status "
				+ " and la.unidade = :unidade "
				+ " and la.tenant_id = :tenantId "
				+ " and la.codigoAuxiliar not in ('ATENDIMENTO_RECEPCAO')"
				+ " and la.dataAtendimento between :ini and :fim "
				+ " order by la.dataAtendimento", Long.class)
			.setParameter("ini", datasTO.getIni(), TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(datasTO.getFim()), TemporalType.TIMESTAMP)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("status", StatusAtendimento.ATENDIDO)
			.getSingleResult();			
		return qde;
	}
	
	// grafico do relatorio atendimentos
	public List<ListaAtendimento> buscarAtendimentosCodAuxGrafico(Unidade unidade, Long tenantId) {			
		return manager.createNamedQuery("ListaAtendimento.buscarAtendimentosCodAux", ListaAtendimento.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getResultList();	
	}	
	public List<ListaAtendimento> buscarAtendimentosCodAuxGrafico(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createNamedQuery("ListaAtendimento.buscarAtendimentosCodAuxGrafico", ListaAtendimento.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getResultList();
	}
	/*
	 * 
	 * 
	 * 
	 * Fim filtros para atender relatorio de atendimentos lazy
	 */
	
	
	
	
	
	
	// Helper
	public Long buscarQdeAtendimentoCodAux(CodigoAuxiliarAtendimento c, Unidade unidade, Date ini, Date fim, Long tenantId){
				
		log.debug("antes: " + fim + "...depois: " + DateUtils.plusDay(fim));
		
		Query q = manager.createQuery("Select count(la.codigo) from ListaAtendimento la  "
				+ "where la.statusAtendimento = :status "
				+ "and la.unidade = :unidade "
				+ "and la.tenant_id = :tenantId "
				+ "and la.dataAtendimento between :ini and :fim "
				+ "and la.codigoAuxiliar = :codAux");
		q.setParameter("unidade", unidade);
		q.setParameter("tenantId", tenantId);
		q.setParameter("ini", ini, TemporalType.TIMESTAMP);
		q.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP);
		q.setParameter("codAux", c);
		q.setParameter("status", StatusAtendimento.ATENDIDO);
		Long qde = (Long) q.getSingleResult();
		
		return qde;
	}
	
	public Long buscarQdeAtendimentoCodAux(CodigoAuxiliarAtendimento c, Unidade unidade, Long tenantId) {
		Query q = manager.createQuery("Select count(la.codigo) from ListaAtendimento la  "
				+ "where la.statusAtendimento = :status "
				+ "and la.unidade = :unidade "
				+ "and la.tenant_id = :tenantId "
				+ "and la.codigoAuxiliar = :codAux");
		q.setParameter("unidade", unidade);
		q.setParameter("tenantId", tenantId);
		q.setParameter("codAux", c);
		q.setParameter("status", StatusAtendimento.ATENDIDO);
		Long qde = (Long) q.getSingleResult();
		
		return qde;
	}
	public Long buscarQdeAtendimentoCodAux(CodigoAuxiliarAtendimento c, Long tenantId) {
		Query q = manager.createQuery("Select count(la.codigo) from ListaAtendimento la  "
				+ "where la.statusAtendimento = :status "
				+ "and la.tenant_id = :tenantId "
				+ "and la.codigoAuxiliar = :codAux");
		q.setParameter("codAux", c);
		q.setParameter("tenantId", tenantId);
		q.setParameter("status", StatusAtendimento.ATENDIDO);
		Long qde = (Long) q.getSingleResult();
		
		return qde;
	}
	
	public Long buscarQdeAtendimentoUnidade(Unidade unidade, Long tenantId) {
		
		Query q = manager.createQuery("Select count(la.codigo) from ListaAtendimento la  "
				+ "where la.statusAtendimento = :status "
				+ "and la.tenant_id = :tenantId "
				+ "and unidade = :unidade");		
		q.setParameter("unidade", unidade);
		q.setParameter("tenantId", tenantId);
		q.setParameter("status", StatusAtendimento.ATENDIDO);
		Long qde = (Long) q.getSingleResult();
		
		return qde;
	}	
	
	
	
	/*
	 * Atendimentos CadUnico
	 */
	
	public List<ListaAtendimento> buscarAtendCadUnicoDataPeriodo(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		return manager.createNamedQuery("ListaAtendimento.buscarAtendCadUnicoDataPeriodo", ListaAtendimento.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getResultList();
	}
	public List<ListaAtendimento> buscarAtendCadUnicoDataPeriodo2(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		return manager.createNamedQuery("ListaAtendimento.buscarAtendCadUnicoDataPeriodo2", ListaAtendimento.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getResultList();
	}
	public List<ListaAtendimento> buscarAtendidosCadUnico(Unidade unidade, Long tenantId) {			
		return manager.createNamedQuery("ListaAtendimento.buscarAtendidosCadUnico", ListaAtendimento.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("status", StatusAtendimento.ATENDIDO)				
				.getResultList();	
	}
	public List<ListaAtendimento> buscarAtendidosCadUnico2(Unidade unidade, Long tenantId) {			
		return manager.createNamedQuery("ListaAtendimento.buscarAtendidosCadUnico2", ListaAtendimento.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("status", StatusAtendimento.ATENDIDO)				
				.getResultList();	
	}
	
	/*
	 * Faltas individualizadas
	 */
	public List<ListaAtendimento> consultaFaltas(Unidade unidade, Pessoa pessoa, Long tenantId) {
		return manager.createNamedQuery("ListaAtendimento.consultaFaltas", ListaAtendimento.class)
			.setParameter("unidade", unidade)
			.setParameter("pessoa", pessoa)
			.setParameter("tenantId", tenantId)
			.setParameter("status", StatusAtendimento.FALTOU)
			.getResultList();	
	}
	/*
	 * Faltas coletivas
	 */
	public List<AgendamentoColetivo> consultaFaltasColetivas(Pessoa pessoa, Long tenantId) {		
		
		/*
		 SELECT * FROM svsa.AgendamentoColetivo a
			INNER JOIN svsa.pessoaagendadafaltosa pa ON (a.codigo = pa.codigo_agendamento)
			INNER JOIN svsa.pessoa p ON (p.codigo = pa.codigo_pessoa)				
			INNER JOIN svsa.unidade u ON (u.codigo = a.codigo_unidade)
		WHERE pa.codigo_pessoa = 23021 and a.statusAtendimento = 'ATENDIDO';
		 */	
		List<AgendamentoColetivo> lista = manager.createQuery("select a from AgendamentoColetivo a "
						+ "INNER JOIN a.pessoasFaltosas pa "
						+ "INNER JOIN Pessoa p ON (p.codigo = pa.codigo) "
						+ "INNER JOIN Unidade u ON (u.codigo = a.unidade) "				
						+ "where pa.codigo = :codigo_pessoa "
						+ "and a.statusAtendimento = :status "
						+ "and a.tenant_id = :tenantId", AgendamentoColetivo.class)
			.setParameter("codigo_pessoa", pessoa.getCodigo())
			.setParameter("tenantId", tenantId)
			.setParameter("status", StatusAtendimento.ATENDIDO)
			.getResultList();
		
		return lista;
	}
	
	/*
	 * Faltas familiares
	 */
	public List<AgendamentoFamiliar> consultaFaltasFamiliares(Pessoa pessoa, Long tenantId) {		
		
		/*
		 SELECT * FROM svsa.AgendamentoFamiliar a
			INNER JOIN svsa.pessoaagendadafamiliarfaltosa pa ON (a.codigo = pa.codigo_agendamento)
			INNER JOIN svsa.pessoa p ON (p.codigo = pa.codigo_pessoa)				
			INNER JOIN svsa.unidade u ON (u.codigo = a.codigo_unidade)
		WHERE a.statusAtendimento = 'ATENDIDO';
		 */	
		List<AgendamentoFamiliar> lista = manager.createQuery("select a from AgendamentoFamiliar a "
						+ "INNER JOIN a.pessoasFaltosas pa "
						+ "INNER JOIN Pessoa p ON (p.codigo = pa.codigo) "
						+ "INNER JOIN Unidade u ON (u.codigo = a.unidade) "				
						+ "where pa.codigo = :codigo_pessoa "
						+ "and a.statusAtendimento = :status "
						+ "and a.tenant_id = :tenantId", AgendamentoFamiliar.class)
			.setParameter("codigo_pessoa", pessoa.getCodigo())
			.setParameter("tenantId", tenantId)
			.setParameter("status", StatusAtendimento.ATENDIDO)
			.getResultList();
		
		return lista;
	}
	
	
	// criado para realização de testes unitários com JIntegrity
	public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}
	
	public void setManager(EntityManager manager2) {
		this.manager = manager2;
		
	}	
}