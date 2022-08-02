package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.softarum.svsa.controller.pront.PesquisaCapaProntuarioBean;
import com.softarum.svsa.dao.AgendamentoIndividualDAO;
import com.softarum.svsa.dao.CapaProntuarioDAO;
import com.softarum.svsa.dao.MPComposicaoDAO;
import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.to.ProntuarioUnidadeTO;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;


/**
 * @author murakamiadmin
 *
 */
public class CapaProntuarioService implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(PesquisaCapaProntuarioBean.class);
	
	@Inject
	private CapaProntuarioDAO prontuarioDAO;
	@Inject
	private AgendamentoIndividualDAO atendDAO;
	@Inject
	private MPComposicaoDAO composicaoDAO;
	
	
	public Prontuario salvar(Prontuario prontuario) throws NegocioException {
		
		try {
			if (prontuario.getFamilia() == null) {
				throw new NegocioException("A Família é obrigatória");
			}
			if (prontuario.getFamilia().getEndereco() == null) {
				throw new NegocioException("O Endereço é obrigatório");
			}
			if (prontuario.getFamilia().getPessoaReferencia() == null) {
				throw new NegocioException("A pessoa de referência é obrigatória");
			}
			
			/* grava municipio em maiusculo*/
			prontuario.getFamilia().getEndereco().setMunicipio(prontuario.getFamilia().getEndereco().getMunicipio());
			
			/* atribui o numero do prontuario fisico com o mesmo valor do código gerado incrementalmente */
			if(prontuario.getProntuario() == null || prontuario.getProntuario().equals("")) {
				Prontuario p = this.prontuarioDAO.salvar(prontuario);
				p.setProntuario(p.getCodigo().toString());
				log.info("codigo prontuario = " + p.getCodigo() + " fisico = " + p.getProntuario() );
				prontuario = this.prontuarioDAO.salvar(p);
			}
			else {
				prontuario = this.prontuarioDAO.salvar(prontuario);
			}
			
			return prontuario;
		}catch(Exception e) {
			e.printStackTrace();
			throw new NegocioException("Problema na gravação do prontuário.");
		}
	}
	
	public Prontuario salvarComPdf(Prontuario prontuario) throws NegocioException {
		try {
			log.info("Salvando pdfCadUnico no prontuario ... ");
			
			return prontuarioDAO.salvar(prontuario);
			
		} catch (Exception e) {
			throw new NegocioException("Não foi possível salvar prontuario com o PdfCadUnico.");
		}
	}

	

	/* Exclui o prontuario e todos os seus membros, exceto se houver atendimentos registrados */
	public void excluir(Prontuario prontuario) throws NegocioException {
		
		try {
			
			log.info("Excluindo prontuario e membros ... ");
			
			/*
			Verificar se existe prontuarioVinculado, se sim limpar o vinculo do vinculado.		
			*/
			limparVinculo(prontuario);
			
			prontuarioDAO.excluirProntuario(prontuario);
			
		} catch (Exception e) {
			//throw new NegocioException("Não foi possível excluir o prontuário. Todos os AGENDAMENTOS, ATENDIMENTOS e AÇÕES de todos os membros da família devem ser excluídos ou migrados antes.");
			throw new NegocioException("Não foi possível excluir o prontuário.");
		}		
	}	
	private void limparVinculo(Prontuario prontuario) {		
		try {
						
			if( prontuario.getProntuarioVinculado() != null ) {
				log.info("Limpando vinculo = " + prontuario.getProntuarioVinculado().getCodigo());
				prontuario.getProntuarioVinculado().setProntuarioVinculado(null);
				this.prontuarioDAO.salvar(prontuario);
			}
		}catch(Exception e) {
			MessageUtil.erro("Erro ao desvincular prontuário excluído!");
		}		
	}
	
	/* Ativa o prontuario e todos os seus membros, exceto se houver atendimentos registrados */
	public void ativar(Prontuario prontuario) throws NegocioException {
		
		try {
			log.info("Ativando prontuario e membros ... ");
			
			prontuarioDAO.ativarProntuario(prontuario);
			
		} catch (Exception e) {
			throw new NegocioException("Não foi possível ativar o prontuário.");
		}		
	}
	public void inativar(Prontuario prontuario) throws NegocioException {
		
		try {
			log.info("Inativando prontuario e membros ... ");
			
			prontuarioDAO.inativarProntuario(prontuario);
			
		} catch (Exception e) {
			throw new NegocioException("Não foi possível inativar o prontuário.");
		}
		
	}
	
	public Prontuario buscarPeloCodigo(Long codigo) {
		return prontuarioDAO.buscarPeloCodigo(codigo);
	}
	public Prontuario buscarProntuarioCRAS(Long codigo, Long tenantId) {
		return prontuarioDAO.buscarProntuarioCRAS(codigo, tenantId);
	}
	
	public List<Prontuario> buscarTodos(Unidade unidade, Long tenantId) {
		return prontuarioDAO.buscarTodos(unidade, tenantId);
	}
	
	
	
	/* Rel Prontuarios G */
	
	
	public List<ProntuarioUnidadeTO> consultarProntuariosRelTO(Unidade unidade, List<Unidade> unidades, Long tenantId) {	
		
		List<ProntuarioUnidadeTO> listaProntuarios = new ArrayList<>();	
		ProntuarioUnidadeTO to;
		
		if(unidade != null) {
			to = new ProntuarioUnidadeTO();
			to.setQdeProntuarios(prontuarioDAO.encontrarQuantidadeDeProntuarios(unidade, tenantId));
			to.setQdeProntuariosInativos(prontuarioDAO.encontrarQuantidadeInativos(unidade, tenantId));
			to.setQdeProntuariosPAIF(prontuarioDAO.encontrarQuantidadePAIF(unidade, tenantId));
			to.setQdeProntuariosExcluidos(prontuarioDAO.encontrarQuantidadeExcluidos(unidade, tenantId));
			to.setNomeUnidade(unidade.getNome());
			listaProntuarios.add(to);
		}
		else {
			for(Unidade u: unidades) {				
				to = new ProntuarioUnidadeTO();
				to.setQdeProntuarios(prontuarioDAO.encontrarQuantidadeDeProntuarios(u, tenantId));
				to.setQdeProntuariosInativos(prontuarioDAO.encontrarQuantidadeInativos(u, tenantId));
				to.setQdeProntuariosPAIF(prontuarioDAO.encontrarQuantidadePAIF(u, tenantId));
				to.setQdeProntuariosExcluidos(prontuarioDAO.encontrarQuantidadeExcluidos(u, tenantId));
				to.setNomeUnidade(u.getNome());
				listaProntuarios.add(to);
			}
		}
		
		return listaProntuarios;
	}
	public Long encontrarTotalDeProntuarios(Long tenantId) {
		return prontuarioDAO.encontrarTotalDeProntuarios(tenantId);
	}
	public Long encontrarProntuariosAtivos(Long tenantId) {
		return prontuarioDAO.encontrarProntuariosAtivos(tenantId);
	}

	
	
	/*
	 * RelatorioProntuariosNovos
	 */
	
	public List<Prontuario> buscarProntuariosPorData(Unidade unidade, Date ini, Date fim, Long tenantId) {
		if(ini != null)
			if(fim != null)
				return prontuarioDAO.buscarTodosPorData(unidade, ini, fim, tenantId);
			else
				return prontuarioDAO.buscarTodosPorData(unidade, ini, new Date(), tenantId);
		return prontuarioDAO.buscarTodos(unidade, tenantId);
	}
	
	/*
	 * RelatorioProntuariosAtualizados
	 */
	
	public List<Prontuario> buscarProntuariosPorDataModificacao(Unidade unidade, Date ini, Date fim, Long tenantId) {
		if(ini != null)
			if(fim != null)
				return prontuarioDAO.buscarTodosPorDataModificacao(unidade, ini, fim, tenantId);
			else
				return prontuarioDAO.buscarTodosPorDataModificacao(unidade, ini, new Date(), tenantId);
		return prontuarioDAO.buscarTodos(unidade, tenantId);
	}
	
	/* Grafico Prontuarios por Trimestre */

	public List<ProntuarioUnidadeTO> buscarProntuariosGrafico(Unidade unidade, Integer ano, Long tenantId) throws NegocioException {
		
		List<ProntuarioUnidadeTO> tos = new ArrayList<>();
		
		/* A partir do ano busca um TO para trimestre */
		// 1 trimestre
		ProntuarioUnidadeTO to = prontuarioDAO.buscarProntuariosTrimestre(unidade, ano, 1, tenantId);
		tos.add(to);
		// 2 trimestre
		to = prontuarioDAO.buscarProntuariosTrimestre(unidade, ano, 2, tenantId);
		tos.add(to);
		// 3 trimestre
		to = prontuarioDAO.buscarProntuariosTrimestre(unidade, ano, 3, tenantId);
		tos.add(to);
		// 4 trimestre
		to = prontuarioDAO.buscarProntuariosTrimestre(unidade, ano, 4, tenantId);
		tos.add(to);
		
		return tos;
	}
	
	

	/*
	 * Obter ultimo atendimento na familia p/ mostrar na capa
	 */
	public ListaAtendimento ultimoAtendimento(Prontuario prontuarioSelecionado, Long tenantId) {
		
		ArrayList<Pessoa> pessoas = (ArrayList<Pessoa>) composicaoDAO.buscarTodosMembros(prontuarioSelecionado, tenantId);
		ListaAtendimento ultimo = null;
		Date dtUltimo = null;
		
		if(pessoas.size() > 0 && pessoas.size() < 2) {
			ultimo = atendDAO.ultimoAtendimento(pessoas.get(0), tenantId);
			if(ultimo != null && ultimo.getDataAtendimento() != null) {
				dtUltimo = ultimo.getDataAtendimento();
				log.info("ultimo atendimento 1 = " + dtUltimo);
			}
		}
		else {
			if(pessoas.size() > 1) {
				ultimo = atendDAO.ultimoAtendimento(pessoas.get(0), tenantId);
				if(ultimo != null && ultimo.getDataAtendimento() != null) {
					dtUltimo = ultimo.getDataAtendimento();
					log.info("ultimo atendimento 1 = " + dtUltimo);
				
					for(Pessoa p : pessoas) {
						ListaAtendimento a = atendDAO.ultimoAtendimento(p, tenantId);
						if(a != null && a.getDataAtendimento() != null) {
							Date dt = a.getDataAtendimento();					
							if(dt.after(dtUltimo)) {
								ultimo = a;
								dtUltimo = dt;
							}
						}
					}
				}
				log.info("ultimo atendimento fim = " + dtUltimo);
			}
		}
		return ultimo;
	}
	public ListaAtendimento ultimoAtendimento(Long codigoPessoa, Long tenantId) {
		log.info("codigo pessoa = " + codigoPessoa);
		
		ListaAtendimento ultimo = atendDAO.ultimoAtendimento(codigoPessoa, tenantId);
		if(ultimo != null && ultimo.getDataAtendimento() != null) {
			Date dtUltimo = ultimo.getDataAtendimento();
			log.info("ultimo atendimento 1 = " + dtUltimo);
		}
				
		return ultimo;
	}
	
	
	public CapaProntuarioDAO getProntuarioDAO() {
		return prontuarioDAO;
	}

	
}