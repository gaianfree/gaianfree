package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.dao.AgendamentoIndividualDAO;
import com.softarum.svsa.modelo.BeneficioEventual;
import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.Beneficio;
import com.softarum.svsa.modelo.enums.CodigoAuxiliarAtendimento;
import com.softarum.svsa.modelo.enums.Role;
import com.softarum.svsa.modelo.enums.StatusAtendimento;
import com.softarum.svsa.modelo.to.AtendimentoDTO;
import com.softarum.svsa.modelo.to.AtendimentoTO;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;

/**
 * @author murakamiadmin
 *
 */
public class AgendamentoIndividualService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Logger log = Logger.getLogger(AgendamentoIndividualService.class);
	
	@Inject
	private AgendamentoIndividualHelper helper;	
	@Inject
	private AgendamentoIndividualDAO listaDAO;
	@Inject 
	private PessoaService pessoaService;
	@Inject 
	private CalendarioAgendaHelper calendarioHelper;
	@Inject 
	private LoginBean loginBean;
	

	public void salvar(ListaAtendimento lista, Long tenantId) throws NegocioException {
		
		log.info("Agendamento pessoa:  " + lista.getPessoa().getNome());
		
		verificarDisponibilidade(loginBean.getUsuario().getUnidade(), lista, tenantId);
		
		if(lista.getPessoa().getTelefone() == null 
				|| lista.getPessoa().getTelefone().isEmpty() 
				|| lista.getPessoa().getTelefone().contentEquals("")) {
			throw new NegocioException("Pessoa sem telefone de contato! Cadastre o telefone antes de agendar.");
		}
		
		lista.setStatusAtendimento(StatusAtendimento.AGENDADO);		
		
		this.listaDAO.salvar(lista);
	}

	public void verificarDisponibilidade(Unidade unidade, ListaAtendimento item, Long tenantId) throws NegocioException {		
				
		if(item.getTecnico() == null) {
			calendarioHelper.verificarDisponibilidade(unidade, item.getDataAgendamento(), tenantId);
		}
		else {
			calendarioHelper.verificarDisponibilidade(item.getTecnico(), item.getDataAgendamento(), tenantId);
		}
		
	}
		
	

	/* RelatorioAtendimento migração */
	public void salvarMigrar(ListaAtendimento lista) throws NegocioException {

		this.listaDAO.salvarAlterar(lista);
	}	
	
	public void atualizar(ListaAtendimento lista) throws NegocioException {
		
		lista.setDataAtendimento(new Date());
		lista.setStatusAtendimento(StatusAtendimento.FALTOU);
		
		this.listaDAO.salvar(lista);
	}
	
	public void encerrarAtendimento(ListaAtendimento lista) throws NegocioException {		
			
		try {
			lista.setStatusAtendimento(StatusAtendimento.ATENDIDO);		
			
			this.listaDAO.salvarEncerramento(lista);
			
				
			}catch(Exception ex) {
				ex.printStackTrace();
				throw new NegocioException("Problema na gravação do atendimento!");
			}
	}
	
	public void encerrarAtendimento(ListaAtendimento lista, BeneficioEventual beneficio) throws NegocioException {		
		
		if (lista.getCodigoAuxiliar() == null) {
			throw new NegocioException("O código Auxiliar é obrigatório.");
		}
		if (lista.getResumoAtendimento() == null || lista.getResumoAtendimento().equals("")) {
			throw new NegocioException("O resumo do atendimento é obrigatório.");	
		}
		if (lista.getDataAtendimento() == null ) {
			lista.setDataAtendimento(new Date());	
			log.info("Data atendimento = " + lista.getDataAtendimento());
		}		
		
		lista.setStatusAtendimento(StatusAtendimento.ATENDIDO);
		
		try {
			BeneficioEventual benef = verificarBeneficio(lista, beneficio);
			if(benef == null) {
				//log.info("bene depois do tratamento = " + benef);
				this.listaDAO.salvarEncerramento(lista);
			}
			else {
				//log.info("bene depois do tratamento = " + benef);
				// gravar atendimento com beneficio
				this.listaDAO.salvarEncerramento(lista, benef);
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
			throw new NegocioException("Problema na gravação do atendimento!");
		}
		
	}
	
	/* Verifica se se trata de beneficio */
	private BeneficioEventual verificarBeneficio(ListaAtendimento lista, BeneficioEventual beneficio) {
		
		log.info("beneficio = " + beneficio);		

		if(lista.getCodigoAuxiliar() == CodigoAuxiliarAtendimento.AUXILIO_FUNERAL) {
			beneficio.setData(lista.getDataAtendimento());
			beneficio.setProntuario(lista.getPessoa().getFamilia().getProntuario());
			beneficio.setRelato(lista.getResumoAtendimento());
			beneficio.setTecnico(lista.getTecnico());
			beneficio.setBeneficio(Beneficio.AUXILIO_FUNERAL);			
		}
		else if(lista.getCodigoAuxiliar() == CodigoAuxiliarAtendimento.AUXILIO_NATALIDADE) {
			beneficio.setData(lista.getDataAtendimento());
			beneficio.setProntuario(lista.getPessoa().getFamilia().getProntuario());
			beneficio.setRelato(lista.getResumoAtendimento());
			beneficio.setTecnico(lista.getTecnico());
			beneficio.setBeneficio(Beneficio.AUXILIO_NATALIDADE);
		}
		else if(lista.getCodigoAuxiliar() == CodigoAuxiliarAtendimento.VULNERABILIDADE_TEMPORARIA_CESTA_BASICA) {
			beneficio = criaBeneficio(lista);
			beneficio.setBeneficio(Beneficio.CESTA_BASICA);
		}
		else if(lista.getCodigoAuxiliar() == CodigoAuxiliarAtendimento.TRANSPORTE_MUNICIPAL_PESSOA_DEFICIENCIA) {
			beneficio = criaBeneficio(lista);
			beneficio.setBeneficio(Beneficio.ITEM_KIT_ESPECIFICO);
		}
		else if(lista.getCodigoAuxiliar() == CodigoAuxiliarAtendimento.OUTROS_BENEFICIOS) {
			beneficio = criaBeneficio(lista);
			beneficio.setBeneficio(Beneficio.OUTROS);
		}
		else {
			beneficio = null;
		}
		return beneficio;		
	}
	
	private BeneficioEventual criaBeneficio(ListaAtendimento lista) {
		
		BeneficioEventual beneficio = new BeneficioEventual();
		beneficio.setData(lista.getDataAtendimento());
		beneficio.setProntuario(lista.getPessoa().getFamilia().getProntuario());
		beneficio.setRelato(lista.getResumoAtendimento());
		beneficio.setTecnico(lista.getTecnico());
		beneficio.setTenant_id(lista.getTenant_id());
		
		return beneficio;
	}
	
	public void salvarResumoRecepcao(ListaAtendimento lista) throws NegocioException {		
		
		if (lista.getDataAtendimento() == null ) {
			lista.setDataAtendimento(new Date());	
			log.info("Data atendimento = " + lista.getDataAtendimento());
		}		
		
		lista.setStatusAtendimento(StatusAtendimento.ATENDIDO);
		
		this.listaDAO.salvarRecepcao(lista);
	}
	
	public void autoSave(ListaAtendimento lista) throws NegocioException {			
			
		if (lista.getDataAtendimento() == null ) {
			lista.setDataAtendimento(new Date());	
			log.info("Auto save = " + lista.getDataAtendimento() + " pessoa: " + lista.getPessoa().getNome());
		}
		
		this.listaDAO.salvar(lista);
	}
	
	public void salvarAlterar(ListaAtendimento item, Usuario usuario) throws NegocioException {
		
		if(usuario.getCodigo().longValue() == item.getTecnico().getCodigo().longValue()) {
			if(new Date().after(DateUtils.plusDays(item.getDataAtendimento(), 7)) )
			{
				throw new NegocioException("Prazo para alteração (7 dias) foi ultrapassado!");
				
			}
			else {
				listaDAO.salvarAlterar(item);
			}
		}
		else {
			throw new NegocioException("Somente o técnico que atendeu pode alterar o atendimento! E isso só pode ser feito antes de 7 dias do registro.");
		}
		
	}
	
	public void excluir(ListaAtendimento item) throws NegocioException {
		listaDAO.excluir(item);
		
	}	
	
	
	// ----------------------------------------------------------------------------
	// Validações
	//
	
	public Boolean verificarMSE(Pessoa pessoa, Long tenantId) {		
		
		return pessoaService.verificarMSE(pessoa, tenantId);
	}
	
	public void verificarAgendamento(Prontuario prontuario, Long tenantId) throws NegocioException {
		if(listaDAO.buscarPorPessoa(prontuario.getFamilia().getPessoaReferencia(), tenantId) != 0)
			throw new NegocioException("Não é possível concluir a operação, pois há agendamento/atendimento pendente!");
	}
	
	
	
	
	
	
	
	public List<ListaAtendimento> consultaFaltas(Pessoa pessoa, Long tenantId) {
		return helper.consultaFaltas(pessoa, tenantId);
	}
	
		
	public List<ListaAtendimento> buscarAtendimentosRole(Usuario usuario, Long tenantId) {
		
		//log.info(usuario.getRole().name());
		
		if(usuario.getRole() == Role.ADVOGADO ||
				usuario.getRole() == Role.ASSISTENTE_SOCIAL ||
				usuario.getRole() == Role.PSICOLOGO ||
				usuario.getRole() == Role.ORIENTADOR_SOCIAL) {
			return listaDAO.buscarAtendimentosTecnicos(usuario.getUnidade(), tenantId);
		}
		return listaDAO.buscarAtendimentosRole(usuario, tenantId);
	}
	
	public List<ListaAtendimento> buscarAtendimentosRecepcao(Usuario usuario, Long tenantId) {
		
		/*
		 * Retorna apenas atendimentos dos ultimos 30 dias
		 */
		Calendar ini = Calendar.getInstance(); 
		ini.add(Calendar.DAY_OF_MONTH, -30);
		Calendar fim = Calendar.getInstance();
		
		log.info("Datas ini: " + ini.getTime() + "--Data fim: " + fim.getTime());
		
		return listaDAO.buscarAtendimentosRecepcao(usuario, ini.getTime(), fim.getTime(), tenantId);
	}

	/* Busca o histórico da pessoa */
	public List<AtendimentoDTO> buscarResumoAtendimentosDTO(Pessoa pessoa, Long tenantId) {	
		
		/* atendimentos individualizados (os coletivos são gravados aqui tambem)*/
		return helper.buscarResumoAtendimentosDTO(pessoa, tenantId);		
	}

	public List<ListaAtendimento> buscarAtendimentosAgendados(Unidade unidade, Long tenantId) {
		return listaDAO.buscarAtendimentosAgendados(unidade, tenantId);
	}
	// usado pelo Agendamento e Atendimento Individualizado
	public List<ListaAtendimento> buscarAtendimentosAgendados(Unidade unidade, Date mesAno, Long tenantId) {
		return listaDAO.buscarAtendimentosAgendados(unidade, mesAno, tenantId);
	}
	
	public List<ListaAtendimento> buscarAtendimentosCodAuxGrafico(Unidade unidade, Date ini, Date fim, Long tenantId) {
		if(ini != null)
			if(fim != null)
				return listaDAO.buscarAtendimentosCodAuxGrafico(unidade, ini, fim, tenantId);
			else
				return listaDAO.buscarAtendimentosCodAuxGrafico(unidade, ini, new Date(), tenantId);
		return listaDAO.buscarAtendimentosCodAuxGrafico(unidade, tenantId);
	}
	
	
	/*
	 * RelatorioAtendimentos CadUnico 
	 */
	
	public List<ListaAtendimento> buscarAtendCadUnicoPeriodo(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return helper.buscarAtendCadUnicoPeriodo(unidade, ini, fim, tenantId);
	}
	public List<ListaAtendimento> buscarAtendCadUnicoPeriodo2(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return helper.buscarAtendCadUnicoPeriodo2(unidade, ini, fim, tenantId);
	}
	
	
	/*
	 * RelatorioAtendimentoFamilia  
	 */
	
	public List<ListaAtendimento> relatorioAtendimentoFamilia(Unidade unidade, Prontuario prontuario, Long tenantId) {
		return listaDAO.buscarAtendimentoFamilia(unidade, prontuario, tenantId);
	}
	
	
	/*
	 * RelatorioAtendimentosG  
	 */
	
	
	
	public List<AtendimentoTO> relatorioAtendimentosTO(Long tenantId) {			
		
		return helper.relatorioAtendimentosTO(tenantId);
	}
	
	public List<AtendimentoTO> relatorioAtendimentosTOCodAux(Unidade unidade, Date ini, Date fim, Long tenantId) {			
		
		return helper.relatorioAtendimentosTOCodAux(unidade, ini, fim, tenantId);
	}
	public List<AtendimentoTO> relatorioAtendimentosTOCodAux (Long tenantId) {			
		
		return helper.relatorioAtendimentosTOCodAux(tenantId);
	}

	
	public AgendamentoIndividualDAO getListaDAO() {
		return listaDAO;
	}
	
}
