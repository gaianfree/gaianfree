package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.dao.AgendamentoColetivoDAO;
import com.softarum.svsa.dao.AgendamentoFamiliarDAO;
import com.softarum.svsa.dao.AgendamentoIndividualDAO;
import com.softarum.svsa.dao.EncaminhamentoDAO;
import com.softarum.svsa.dao.HistoricoTransferenciaDAO;
import com.softarum.svsa.dao.UnidadeDAO;
import com.softarum.svsa.modelo.AgendamentoColetivo;
import com.softarum.svsa.modelo.AgendamentoFamiliar;
import com.softarum.svsa.modelo.Encaminhamento;
import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.modelo.Oficio;
import com.softarum.svsa.modelo.OficioEmitido;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.CodigoAuxiliarAtendimento;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.modelo.to.AtendimentoDTO;
import com.softarum.svsa.modelo.to.AtendimentoTO;

/**
 * Classe criada para desonerar a classe ListaAtendimentoService Especificamente
 * para relatórios. Essa classe só pode ser utilizada pelo
 * ListaAtendimentoService
 * 
 * @author murakamiadmin
 *
 */
class AgendamentoIndividualHelper implements Serializable {

	private static final long serialVersionUID = 1L;

	private Logger log = Logger.getLogger(AgendamentoIndividualHelper.class);

	@Inject
	private AgendamentoIndividualDAO listaDAO;
	@Inject
	private AgendamentoColetivoDAO coletivoDAO;
	@Inject
	private AgendamentoFamiliarDAO familiarDAO;
	@Inject
	private UnidadeDAO unidadeDAO;
	@Inject
	private EncaminhamentoDAO encDAO;
	@Inject
	AcaoService acaoService;
	@Inject
	OficioService oficioService;
	@Inject
	OficioEmitidoService oficioEmitidoService;
	@Inject
	private HistoricoTransferenciaDAO historicoDAO;
	@Inject
	private LoginBean loginBean;

	/*
	 * RelatorioAtendimentos (cadUnico)
	 */
	public List<ListaAtendimento> buscarAtendCadUnicoPeriodo(Unidade unidade, Date ini, Date fim, Long tenantId) {
		if (ini != null)
			if (fim != null)
				return listaDAO.buscarAtendCadUnicoDataPeriodo(unidade, ini, fim, tenantId);
			else
				return listaDAO.buscarAtendCadUnicoDataPeriodo(unidade, ini, new Date(), tenantId);
		return listaDAO.buscarAtendidosCadUnico(unidade, tenantId);
	}

	public List<ListaAtendimento> buscarAtendCadUnicoPeriodo2(Unidade unidade, Date ini, Date fim, Long tenantId) {
		if (ini != null)
			if (fim != null)
				return listaDAO.buscarAtendCadUnicoDataPeriodo2(unidade, ini, fim, tenantId);
			else
				return listaDAO.buscarAtendCadUnicoDataPeriodo2(unidade, ini, new Date(), tenantId);
		return listaDAO.buscarAtendidosCadUnico2(unidade, tenantId);
	}

	/*
	 * RelatorioAtendimentos (gestão)
	 */

	private Long buscar(CodigoAuxiliarAtendimento c, Unidade unidade, Date ini, Date fim, Long tenantId) {
		if (ini != null)
			if (fim != null)
				return listaDAO.buscarQdeAtendimentoCodAux(c, unidade, ini, fim, tenantId);
			else
				return listaDAO.buscarQdeAtendimentoCodAux(c, unidade, ini, new Date(), tenantId);
		return listaDAO.buscarQdeAtendimentoCodAux(c, unidade, tenantId);
	}
	private Long buscar(CodigoAuxiliarAtendimento c, Long tenantId) {			
			
		return listaDAO.buscarQdeAtendimentoCodAux(c, tenantId);
	}

	public List<AtendimentoTO> relatorioAtendimentosTOCodAux(Unidade unidade, Date ini, Date fim, Long tenantId) {

		/*
		 * Recuperar codigos auxiliares e para cada codigo buscar a quantidade de
		 * atendimentos da unidade
		 * 
		 */
		List<CodigoAuxiliarAtendimento> codigos = Arrays.asList(CodigoAuxiliarAtendimento.values());

		List<AtendimentoTO> listaTO = new ArrayList<>();

		for (CodigoAuxiliarAtendimento c : codigos) {
			AtendimentoTO to = new AtendimentoTO();
			to = new AtendimentoTO();
			to.setNome(c.name());
			to.setQdeAtendimentos(buscar(c, unidade, ini, fim, tenantId));
			if(to.getQdeAtendimentos() > 0) {
				listaTO.add(to);
			}			
		}

		return listaTO;
	}
	public List<AtendimentoTO> relatorioAtendimentosTOCodAux(Long tenantId) {

		/*
		 * Recuperar codigos auxiliares e para cada codigo buscar a quantidade de
		 * atendimentos da unidade
		 * 
		 */
		List<CodigoAuxiliarAtendimento> codigos = Arrays.asList(CodigoAuxiliarAtendimento.values());

		List<AtendimentoTO> listaTO = new ArrayList<>();

		for (CodigoAuxiliarAtendimento c : codigos) {
			AtendimentoTO to = new AtendimentoTO();
			to = new AtendimentoTO();
			to.setNome(c.name());
			to.setQdeAtendimentos(buscar(c, tenantId));
			if(to.getQdeAtendimentos() > 0) {
				listaTO.add(to);
			}
		}

		return listaTO;
	}

	public List<AtendimentoTO> relatorioAtendimentosTO(Long tenantId) {

		/*
		 * Recuperar unidades e para cada unidade buscar a quantidade de atendimentos
		 * 
		 */
		List<Unidade> unidades = unidadeDAO.buscarTodos(loginBean.getTenantId());

		List<AtendimentoTO> listaTO = new ArrayList<>();

		for (Unidade u : unidades) {

			if (u.getTipo() != TipoUnidade.SASC) {
				AtendimentoTO to = new AtendimentoTO();
				to = new AtendimentoTO();
				to.setNome(u.getNome());
				to.setQdeAtendimentos(listaDAO.buscarQdeAtendimentoUnidade(u, tenantId));
				listaTO.add(to);
			}
		}

		return listaTO;
	}
	
	
	

	/*
	 * ------- HISTÓRICO PESSOA ---------------------------
	 * Recuperação de toda evolução da Pessoa
	 * ---------------------------------------------------
	 */	
	
	
	
	public List<AtendimentoDTO> buscarResumoAtendimentosDTO(Pessoa pessoa, Long tenantId) {

		/*
		 * ================================================= 
		 * Buscar Atendimentos individualizados e Faltas
		 * =================================================
		 */
		List<AtendimentoDTO> atendimentos = listaDAO.buscarResumoAtendimentosDTO(pessoa, tenantId);
		log.debug("Qde atendimentos individualizados : (" + pessoa.getCodigo() + "-" + pessoa.getNome() + ") = "
				+ atendimentos.size());

		/*
		 * =================================== 
		 * Buscar Ações
		 * ===================================
		 */
		List<AtendimentoDTO> atendimentosAcoes = acaoService.buscarAcoes(pessoa, tenantId);
		log.debug("Qde ações : (" + pessoa.getCodigo() + "-" + pessoa.getNome() + ") = "
				+ atendimentosAcoes.size());
		atendimentos.addAll(atendimentosAcoes);

		/*
		 * ================================================= 
		 * Buscar Atendimentos Coletivos 
		 * =================================================
		 */
		List<AtendimentoDTO> atendimentosCol = coletivoDAO.buscarResumoAtendimentosTO(pessoa, tenantId);
		log.debug("Qde atendimentos coletivos (" + pessoa.getCodigo() + "-" + pessoa.getNome() + ") = "
				+ atendimentosCol.size());
		atendimentos.addAll(atendimentosCol);
		
		/*
		 * ================================================= 
		 * Buscar Atendimentos Familiares 
		 * =================================================
		 */
		// Excluido devido trazer no atendimento individualizado
		
		List<AtendimentoDTO> atendimentosFam = familiarDAO.buscarResumoAtendimentosTO(pessoa, tenantId);
		log.debug("Qde atendimentos familiares (" + pessoa.getCodigo() + "-" + pessoa.getNome() + ") = "
				+ atendimentosFam.size());
		atendimentos.addAll(atendimentosFam);
		
		/*
		 * ========================================== 
		 * Buscar Encaminhamentos Outros (externos)
		 * ==========================================
		 */
		List<Encaminhamento> encaminhamentos = encDAO.buscarEncaminhamentos(pessoa, tenantId);
		log.debug("Qde encaminhamentos da pessoa : (" + pessoa.getCodigo() + "-" + pessoa.getNome() + ") = "
				+ encaminhamentos.size());
		if (!encaminhamentos.isEmpty()) {
			for (Encaminhamento e : encaminhamentos) {
				AtendimentoDTO dto = new AtendimentoDTO();

				dto.setData(e.getData());

				dto.setResumoAtendimento("[Enc.Externo] PARA: " + e.getOrgaoUnidadeDestino()  + " - MOTIVO: " + e.getMotivo());
				if (e.getTecnico() != null)
					dto.setNomeTecnico(e.getTecnico().getNome());
				dto.setNomeUnidade(e.getTecnico().getUnidade().getNome());
				dto.setNomePessoa(e.getPessoa().getNome());
				atendimentos.add(dto);
			}
		}

		/*
		 * ========================================== 
		 * Buscar ofícios Recebidos/respondidos 
		 * ==========================================
		 */
		List<Oficio> oficios = oficioService.buscarOficiosHist(pessoa, tenantId);
		log.debug("Qde oficios recebidos/emitidos da pessoa : (" + pessoa.getCodigo() + "-" + pessoa.getNome() + ") = "
				+ oficios.size());

		if (!oficios.isEmpty()) {

			for (Oficio o : oficios) {

				AtendimentoDTO dto, dto2 = null;

				if (o.getDataResposta() != null) {

					// oficio recebido
					dto = new AtendimentoDTO();
					dto.setData(o.getDataRecebimento());
					if (o.getCoordenador() != null) {
						dto.setNomeTecnico(o.getCoordenador().getNome()); // coordenador que recebeu
						dto.setNomeUnidade(o.getCoordenador().getUnidade().getNome()); // unidade do coordenador
					}
					dto.setNomePessoa(o.getPessoa().getNome());
					dto.setResumoAtendimento("[Ofício Recebido] " + o.getNrOficio() + " ( " + o.getAssunto() + " ) ");
					atendimentos.add(dto);

					// oficio respondido
					dto2 = new AtendimentoDTO();
					dto2.setData(o.getDataResposta());
					dto2.setNomeTecnico(o.getTecnico().getNome());
					dto2.setNomeUnidade(o.getTecnico().getUnidade().getNome());
					dto2.setNomePessoa(o.getPessoa().getNome());
					dto2.setResumoAtendimento(
							"[Ofício Resposta] " + o.getNrOficioResp() + " ( " + o.getAssunto() + " ) ");
					atendimentos.add(dto2);
				} else {

					// oficio recebido
					dto = new AtendimentoDTO();
					dto.setData(o.getDataRecebimento());
					if (o.getCoordenador() != null) {
						dto.setNomeTecnico(o.getCoordenador().getNome()); // coordenador que recebeu
						dto.setNomeUnidade(o.getCoordenador().getUnidade().getNome()); // unidade do coordenador
					}
					dto.setNomePessoa(o.getPessoa().getNome());
					dto.setResumoAtendimento("[Ofício Recebido] " + o.getNrOficio() + " ( " + o.getAssunto() + " ) ");
					atendimentos.add(dto);
				}
			}
		}

		/*
		 * ========================================== 
		 * Buscar Faltas Atendimento Coletivo
		 * ==========================================
		 */
		
		// faltas coletivo
		List<AgendamentoColetivo> faltasCol = listaDAO.consultaFaltasColetivas(pessoa, tenantId);
		if (!faltasCol.isEmpty()) {
			for (AgendamentoColetivo a : faltasCol) {
				for(Pessoa p : a.getPessoasFaltosas()) {
					if (p.getCodigo().longValue() == pessoa.getCodigo().longValue()) {
						AtendimentoDTO dto = new AtendimentoDTO();
						dto.setData(a.getDataAgendamento());
						dto.setResumoAtendimento("[Falta Coletivo] " + a.getResumoAtendimento());
						if (a.getTecnico() != null)
							dto.setNomeTecnico(a.getTecnico().getNome());
						dto.setNomeUnidade(a.getUnidade().getNome());
						dto.setNomePessoa(p.getNome());
						if (a.getAgendador() != null)
							dto.setNomeAgendador(a.getAgendador().getNome());
						atendimentos.add(dto);
						break;
					}
				}
			}		
		}
		
		/*
		 * ========================================== 
		 * Buscar Faltas Atendimento Familiar
		 * ==========================================
		 */
		
		List<AgendamentoFamiliar> faltasFam = listaDAO.consultaFaltasFamiliares(pessoa, tenantId);
		log.debug("faltas familiares : " + faltasFam.size());
							
		if (!faltasFam.isEmpty()) {
			for (AgendamentoFamiliar a : faltasFam) {	
				for(Pessoa p : a.getPessoasFaltosas()) {
					if (p.getCodigo().longValue() == pessoa.getCodigo().longValue()) {
						AtendimentoDTO dto = new AtendimentoDTO();
						dto.setData(a.getDataAgendamento());
						dto.setResumoAtendimento("[Falta Familiar] " + a.getResumoAtendimento());
						if (a.getTecnico() != null)
							dto.setNomeTecnico(a.getTecnico().getNome());
						dto.setNomeUnidade(a.getUnidade().getNome());
						dto.setNomePessoa(p.getNome());
						if (a.getAgendador() != null)
							dto.setNomeAgendador(a.getAgendador().getNome());
						atendimentos.add(dto);
						break;
					}
				}
			}
		}
		
		
		/*
		 * ========================================== 
		 * Buscar ofícios Emitidos
		 * ==========================================
		 */
		List<OficioEmitido> oficiosEmitidos = oficioEmitidoService.buscarOficiosEmitidosHist(pessoa, tenantId);

		log.debug("Qde oficios emitidos da pessoa : (" + pessoa.getCodigo() + "-" + pessoa.getNome() + ") = "
				+ oficiosEmitidos.size());

		if (!oficiosEmitidos.isEmpty()) {

			for (OficioEmitido o : oficiosEmitidos) {

				AtendimentoDTO dto = new AtendimentoDTO();
				dto.setData(o.getDataEmissao());
				if (o.getTecnico() != null)
					dto.setNomeTecnico(o.getTecnico().getNome());
				// emitidos
				dto.setResumoAtendimento("[Ofício Emitido] " + o.getNrOficioEmitido() + " ( " + o.getAssunto() + " ) ");
				dto.setNomeUnidade(o.getTecnico().getUnidade().getNome());
				dto.setNomePessoa(o.getPessoa().getNome());
				atendimentos.add(dto);
			}
		}
		
		
		/*
		 * ================================================= 
		 * Buscar Transferencias de Prontuários
		 * =================================================
		 */
		List<AtendimentoDTO> transferencias = historicoDAO.buscarTransferenciasDTO(pessoa, tenantId);
		log.debug("Qde transferencias : (" + pessoa.getCodigo() + "-" + pessoa.getNome() + ") = "
				+ transferencias.size());
		atendimentos.addAll(transferencias);
		
		
		
		
		/* ordem descendente por data */
		
		Collections.sort(atendimentos, new Comparator<AtendimentoDTO>() {
			  public int compare(AtendimentoDTO o1, AtendimentoDTO o2) {
			      if (o1.getData() == null || o2.getData() == null)
			        return 0;
			      return o2.getData().compareTo(o1.getData());
			  }
			});
		
		//retorna evolução ordenada
		return atendimentos;
	}

	
	
	/*
	 * ------- FIM HISTÓRICO PESSOA ---------------------------	
	 */	
	
	

	public List<ListaAtendimento> consultaFaltas(Pessoa pessoa, Long tenantId) {
		
		// faltas individualizada
		List<ListaAtendimento> faltas = listaDAO.consultaFaltas(pessoa.getFamilia().getProntuario().getUnidade(), pessoa, tenantId);
		List<ListaAtendimento> faltas2 = new ArrayList<ListaAtendimento>();
		List<ListaAtendimento> faltas3 = new ArrayList<ListaAtendimento>();
		
		// faltas coletivo
		List<AgendamentoColetivo> faltasCol = listaDAO.consultaFaltasColetivas(pessoa, tenantId);
		log.debug("faltas coletivas : " + faltasCol.size());
		log.info("codigo pessoa: " + pessoa.getCodigo());
							
		if (!faltasCol.isEmpty()) {
			for (AgendamentoColetivo a : faltasCol) {
				for(Pessoa p : a.getPessoasFaltosas()) {
					log.info(p.getCodigo().longValue() == pessoa.getCodigo().longValue());
					if (p.getCodigo().longValue() == pessoa.getCodigo().longValue()) {
						ListaAtendimento dto = new ListaAtendimento();
						dto.setDataAgendamento(a.getDataAgendamento());
						dto.setResumoAtendimento("[Coletivo] " + a.getResumoAtendimento());
						if (a.getTecnico() != null)
							dto.setTecnico(a.getTecnico());
						dto.setUnidade(a.getUnidade());
						dto.setPessoa(p);
						if (a.getAgendador() != null)
							dto.setAgendador(a.getAgendador());
						faltas2.add(dto);
						log.info("codigo pessoa faltosa adicionada: " + p.getCodigo());
						break;
					}
					log.info("codigo pessoa faltosa: " + p.getCodigo());
				}
			}
			faltas.addAll(faltas2);
		}	
		
		// faltas familiar
		List<AgendamentoFamiliar> faltasFam = listaDAO.consultaFaltasFamiliares(pessoa, tenantId);
		log.debug("faltas familiares : " + faltasFam.size());
							
		if (!faltasFam.isEmpty()) {
			for (AgendamentoFamiliar a : faltasFam) {	
				for(Pessoa p : a.getPessoasFaltosas()) {
					if (p.getCodigo().longValue() == pessoa.getCodigo().longValue()) {
						ListaAtendimento dto = new ListaAtendimento();
						dto.setDataAgendamento(a.getDataAgendamento());
						dto.setResumoAtendimento("[Familiar] " + a.getResumoAtendimento());
						if (a.getTecnico() != null)
							dto.setTecnico(a.getTecnico());
						dto.setUnidade(a.getUnidade());
						dto.setPessoa(p);
						if (a.getAgendador() != null)
							dto.setAgendador(a.getAgendador());
						faltas3.add(dto);
						break;
					}
				}
			}
			faltas.addAll(faltas3);
		}		
		
		return  faltas;
	}

}
