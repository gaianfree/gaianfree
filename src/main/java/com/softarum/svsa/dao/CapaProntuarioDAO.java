package com.softarum.svsa.dao;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

import com.softarum.svsa.controller.pront.PesquisaCapaProntuarioBean;
import com.softarum.svsa.modelo.Familia;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Status;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.modelo.to.ProntuarioUnidadeTO;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;


/**
 * @author murakamiadmin
 *
 */
public class CapaProntuarioDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(PesquisaCapaProntuarioBean.class);
	
	@Inject
	private EntityManager manager;
	
	@Transactional
	public Prontuario salvar(Prontuario prontuario) throws NegocioException {
		try {
			
			return manager.merge(prontuario);
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
	
	// trocaPessoa de Referencia
	@Transactional
	public void trocarPR(Familia familia, PessoaReferencia prAntiga, Pessoa prNova) throws NegocioException {
		try {
			
			log.info("trocando pessoa referencia...3 para " + prNova.getCodigo());
			
			// altera para PessoaReferencia
			final Query query = manager.createNativeQuery( "UPDATE Pessoa SET TIPO_PESSOA = 'PESSOA_REFERENCIA', "
					+ "parentescoPessoaReferencia = 'RESPONSAVEL_FAMILIAR' WHERE codigo = :id" );
			query.setParameter( "id", prNova.getCodigo() );	 
            query.executeUpdate();
            
            // altera para Pessoa
            final Query query2 = manager.createNativeQuery( "UPDATE Pessoa SET TIPO_PESSOA = 'Pessoa', "
            		+ "parentescoPessoaReferencia = 'NAO_PARENTE' WHERE codigo = :id" );
            query2.setParameter( "id", prAntiga.getCodigo() );
            query2.executeUpdate();
            
            //Altera PessoaReferencia da Familia
            final Query query3 = manager.createNativeQuery( "UPDATE Familia SET codigo_pessoa_referencia = :pr WHERE codigo = :id" );
            query3.setParameter( "pr", prNova.getCodigo() );	 
            query3.setParameter( "id", familia.getCodigo() );
            query3.executeUpdate();
			
            log.info("trocado.");
		
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
	
	
	/* Exclui completamente o prontuario e seus relacionamentos */
	@Transactional
	public void excluirProntuario(Prontuario prontuario) throws NegocioException {

		/* transferir essa regra de negocio para o Service */
		try {
			Prontuario p = buscarPeloCodigo(prontuario.getCodigo());
			Familia f = p.getFamilia();
			List<Pessoa> membros = f.getMembros();		
			PessoaReferencia pf = f.getPessoaReferencia();
			
			/*  membros*/			
			for(Pessoa pe : membros) {
				if(pe instanceof Pessoa) {
					pe.setExcluida(true);
					pe.setStatus(Status.INATIVO);					
					log.info("Membro excluido: " + pe.getCodigo());
				}
			}
			
			pf.setExcluida(true);
			pf.setStatus(Status.INATIVO);		
		
			p.setExcluido(true);
			p.setStatus(Status.INATIVO);
			manager.merge(p);			
			
			log.info("Prontuario excluido: " + p.getCodigo());
			
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
	public void ativarProntuario(Prontuario prontuario) throws NegocioException {
		
		try {
			Prontuario p = buscarPeloCodigo(prontuario.getCodigo());
			Familia f = p.getFamilia();
			List<Pessoa> membros = f.getMembros();		
			PessoaReferencia pf = f.getPessoaReferencia();
			
			/*  membros*/
			for(Pessoa pe : membros) {
				if(pe instanceof Pessoa) {
					pe.setStatus(Status.ATIVO);					
					log.info("Membro ativado: " + pe.getCodigo());
				}
			}		
		
			pf.setStatus(Status.ATIVO);
			
			p.setStatus(Status.ATIVO);
			manager.merge(p);			
			log.info("Prontuario ativado: " + p.getCodigo());
			
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
	public void inativarProntuario(Prontuario prontuario) throws NegocioException {
		try {
			Prontuario p = buscarPeloCodigo(prontuario.getCodigo());
			Familia f = p.getFamilia();
			List<Pessoa> membros = f.getMembros();		
			PessoaReferencia pf = f.getPessoaReferencia();
			
			/*  membros*/
			log.info("tamanho membros: " + membros.size());
			
			for(Pessoa pe : membros) {
				if(pe instanceof Pessoa) {					
					pe.setStatus(Status.INATIVO);
					log.info("Membro inativado: " + pe.getCodigo());
				}
			}	
						
			pf.setStatus(Status.INATIVO);						
			
			p.setStatus(Status.INATIVO);
			manager.merge(p);
			log.info("Prontuario inativado: " + p.getCodigo());
			
			
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
	
	public void setManager(EntityManager manager2) {
		this.manager = manager2;
		
	}
	
	
	
	/*
	 * Buscas
	 */
	
	

	public Prontuario buscarPeloCodigo(Long codigo) {
		return manager.find(Prontuario.class, codigo);
	}
	public Prontuario buscarProntuarioCRAS(Long codigo, Long tenantId) {
		return manager.createQuery("select p from Prontuario p where p.codigo = :codigo "
				+ "and p.tenant_id = :tenantId "
				+ "and p.excluido = :exc "
				+ "and p.unidade.tipo = :cras", Prontuario.class)
				.setParameter("codigo", codigo)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.setParameter("cras", TipoUnidade.CRAS)
				.getSingleResult();
	}
		
	
	public Familia buscarFamilia(Long codigo) {
		return manager.find(Familia.class, codigo);
	}
	
	@SuppressWarnings("unchecked")
	public List<Prontuario> buscarTodos(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("Prontuario.buscarTodosUnidade")
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.getResultList();
	}
	
	
	
	/*
	 * Filtros para atender a pesquisa de CapaProntuario
	 */
	
	@SuppressWarnings("unchecked")
	public List<Prontuario> buscarComPaginacao(int first, int pageSize, Unidade unidade, String nome, Long tenantId) {
		return manager.createNamedQuery("Prontuario.buscarTodosPR")
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("nome", nome.toUpperCase() + "%")
			.setParameter("exc", false)
			.setFirstResult(first)
			.setMaxResults(pageSize)
			.getResultList();
	}	
	public Long encontrarQdePR(Unidade unidade, String nome, Long tenantId) {		
		return (Long) manager.createNamedQuery("Prontuario.qdeTodosPR")
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("nome", nome.toUpperCase() + "%")
				.setParameter("exc", false)
			.getSingleResult();
	}	
	
	@SuppressWarnings("unchecked")
	public List<Prontuario> buscarComPaginacao(int first, int pageSize, Unidade unidade, Long prontuario, Long tenantId) {		
		return manager.createNamedQuery("Prontuario.buscarTodosProntuario")
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("codigo", prontuario)
			.setParameter("exc", false)
			.setFirstResult(first)
			.setMaxResults(pageSize)
			.getResultList();
	}
	public Long encontrarQdeProntuario(Unidade unidade, Long prontuario, Long tenantId) {		
		return (Long) manager.createNamedQuery("Prontuario.qdeTodosProntuario")
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("codigo", prontuario)
				.setParameter("exc", false)
			.getSingleResult();
	}	
	
	@SuppressWarnings("unchecked")
	public List<Prontuario> buscarComPaginacao(int first, int pageSize, Unidade unidade, Long tenantId) {	
		return manager.createNamedQuery("Prontuario.buscarTodosUnid")
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.setFirstResult(first)
				.setMaxResults(pageSize)
				.getResultList();
	}
	public Long encontrarQdeUnid(Unidade unidade, Long tenantId) {		
		return (Long) manager.createNamedQuery("Prontuario.qdeTodosUnid")
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
			.getSingleResult();
	}	
	
	/* fim pesqusas CapaProntuario */
	
	
	
	
	/* buscas de qdes para preencher TOs do relatorio Geral de Prontuarios*/
	
	public Long encontrarQuantidadeDeProntuarios(Unidade unidade, Long tenantId) {
		return manager.createQuery("select count(p) from Prontuario p where p.unidade = :unidade "
				+ "and p.tenant_id = :tenantId "
				+ "and p.excluido = :exc", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.getSingleResult();
	}
	public Long encontrarQuantidadeInativos(Unidade unidade, Long tenantId) {
		return manager.createQuery("select count(p) from Prontuario p where p.unidade = :unidade "
				+ "and p.tenant_id = :tenantId "
				+ "and p.status = 'INATIVO' and p.excluido = :exc", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.getSingleResult();
	}
	public Long encontrarQuantidadeExcluidos(Unidade unidade, Long tenantId) {
		return manager.createQuery("select count(p) from Prontuario p where p.unidade = :unidade "
				+ "and p.tenant_id = :tenantId "
				+ "and p.excluido = :exc ", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", true)
				.getSingleResult();
	}
	public Long encontrarTotalDeProntuarios(Long tenantId) {
		return manager.createQuery("select count(p) from Prontuario p where p.excluido = :exc "
				+ "and p.tenant_id = :tenantId ", Long.class)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.getSingleResult();
	}
	public Long encontrarProntuariosAtivos(Long tenantId) {
		return manager.createQuery("select count(p) from Prontuario p where p.status = 'ATIVO' "
				+ "and p.tenant_id = :tenantId "
				+ "and p.excluido = :exc", Long.class)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.getSingleResult();
	}
	
	public Long encontrarQuantidadePAIF(Unidade unidade, Long tenantId) {
		
		/*
		 SELECT count(prontuario)
			FROM svsa_salto.PlanoAcompanhamento plano
				INNER JOIN svsa_salto.Prontuario prontuario ON plano.codigo_prontuario = prontuario.codigo				
			WHERE prontuario.codigo_unidade = 2 
				and plano.dataDesligamento is null 
                and prontuario.excluido = 0b0;
		 */	
		Long qde = manager.createQuery("SELECT count(prontuario)"
			+ "FROM PlanoAcompanhamento plano "
				+ "INNER JOIN Prontuario prontuario ON plano.prontuario.codigo = prontuario.codigo "				
				+ "WHERE prontuario.unidade.codigo = :codigo_unidade "
				+ "and plano.tenant_id = :tenantId "
				+ "and plano.dataDesligamento is null "
				+ "and prontuario.excluido = :exc", Long.class)
			.setParameter("codigo_unidade", unidade.getCodigo())
			.setParameter("tenantId", tenantId)
			.setParameter("exc", false)
			.getSingleResult();
		return qde;
	}
	
	/*
	 * RelatorioProntuariosNovos
	 */
	
	public List<Prontuario> buscarTodosPorData(Unidade unidade, Date ini, Date fim, Long tenantId) {	
		return  manager.createNamedQuery("Prontuario.buscarTodosPorData", Prontuario.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("exc", false)
				.getResultList();	
	}
	
	/*
	 * RelatorioProntuariosAtualizados
	 */
	
	public List<Prontuario> buscarTodosPorDataModificacao(Unidade unidade, Date ini, Date fim, Long tenantId) {	
		return  manager.createNamedQuery("Prontuario.buscarTodosPorDataModificacao", Prontuario.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("exc", false)
				.getResultList();	
	}
	
	
	/*
	 * RelatorioProntuariosTrim - grafico de prontuarios por trimestre
	 * Consulta todos de uma vez e carrega TO para cada trimestre
	 * 
	 */
	
	public ProntuarioUnidadeTO buscarProntuariosTrimestre(Unidade unidade, Integer ano, int tri, Long tenantId) throws NegocioException {
		
			ProntuarioUnidadeTO to = new ProntuarioUnidadeTO();
			
			// ano inteiro
			String iniTri = "-01-01";
			String fimTri = "-12-31";
			
			if(tri == 1) {
				iniTri = ano.toString().concat("-01-01");
				fimTri = ano.toString().concat("-03-31");
			} else if(tri == 2) {
				iniTri = ano.toString().concat("-04-01");
				fimTri = ano.toString().concat("-06-30");
			} else if(tri == 3) {
				iniTri = ano.toString().concat("-07-01");
				fimTri = ano.toString().concat("-09-30");
			} else if(tri == 4) {
				iniTri = ano.toString().concat("-10-01");
				fimTri = ano.toString().concat("-12-31");
			}
			
			log.debug("Data ini: " + iniTri + " / Data fim: " + fimTri);
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date ini;
			Date fim;
			try {
				ini = format.parse(iniTri);
				fim = format.parse(fimTri);
				log.debug("Data ini: " + ini + " / Data fim: " + fim);
			} catch (ParseException e) {				
				throw new NegocioException("Problema com conversão de datas.");
			}
			
			
			
			// Novos
			Long qde = manager.createQuery("SELECT count(*) FROM Prontuario p"
					+ " WHERE p.dataEntrada >= :ini and p.dataEntrada <= :fim "
					+ " and p.tenant_id = :tenantId "
					+ " and p.unidade = :unidade", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.getSingleResult();
			to.setQdeProntuarios(qde);
			
			// Inativos
			qde = manager.createQuery("SELECT count(*) FROM Prontuario p"
					+ " WHERE p.dataEntrada >= :ini and p.dataEntrada <= :fim "
					+ " and p.unidade = :unidade "
					+ " and p.tenant_id = :tenantId "
					+ " and p.status = 'INATIVO'", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.getSingleResult();
			to.setQdeProntuariosInativos(qde);
			
			// Excluidos
			qde = manager.createQuery("SELECT count(*) FROM Prontuario p"
					+ " WHERE p.dataEntrada >= :ini and p.dataEntrada <= :fim "
					+ " and p.unidade = :unidade "
					+ " and p.tenant_id = :tenantId "
					+ " and p.excluido = true", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.getSingleResult();
			to.setQdeProntuariosExcluidos(qde);
			
			if(unidade.getTipo().equals(TipoUnidade.CRAS)) {
				// PAIF
				qde = manager.createQuery("SELECT count(*) FROM Prontuario p"
						+ " WHERE p.dataEntrada >= :ini and p.dataEntrada <= :fim "
						+ " and p.unidade = :unidade "
						+ " and p.tenant_id = :tenantId ", Long.class)
					.setParameter("unidade", unidade)
					.setParameter("tenantId", tenantId)
					.setParameter("ini", ini, TemporalType.TIMESTAMP)
					.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
					.getSingleResult();
				to.setQdeProntuariosPAIF(qde);
			}
			else {
				if(unidade.getTipo().equals(TipoUnidade.CREAS)) {
					// PAEFI						
					qde = manager.createQuery("SELECT count(*) FROM Prontuario p"
							+ " WHERE p.dataEntrada >= :ini and p.dataEntrada <= :fim "
							+ " and p.unidade = :unidade "
							+ " and p.tenant_id = :tenantId ", Long.class)
						.setParameter("unidade", unidade)
						.setParameter("tenantId", tenantId)
						.setParameter("ini", ini, TemporalType.TIMESTAMP)
						.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
						.getSingleResult();
					to.setQdeProntuariosPAIF(qde);
				}
				else {
					to.setQdeProntuariosPAIF(0l);
				}
			}			
			
			return to;
	}	
	
	public Long obterNrProntuario() {
		return (Long) manager.createNamedQuery("Prontuario.obterNrProntuario").getSingleResult();
	}
	
}
