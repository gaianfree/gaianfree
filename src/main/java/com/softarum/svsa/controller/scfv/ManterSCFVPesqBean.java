package com.softarum.svsa.controller.scfv;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
//import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.Atividade;
import com.softarum.svsa.modelo.Frequencia;
import com.softarum.svsa.modelo.Inscricao;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Servico;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Ano;
import com.softarum.svsa.modelo.enums.Prioridade;
import com.softarum.svsa.modelo.enums.TipoServico;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.modelo.to.PessoaDTO;
import com.softarum.svsa.service.PessoaService;
import com.softarum.svsa.service.ServicoService;
import com.softarum.svsa.util.CalculoUtil;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * @author murakamiadmin
 *
 */
@Log4j
@Getter
@Setter
@Named
@ViewScoped   
public class ManterSCFVPesqBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Unidade unidade;
	private List<String> anos = new ArrayList<>(Arrays.asList(Ano.getAnos()));
	private Integer ano;	
	

	private int qdeAtividades = 0;
	LocalDate data = null;
	
	/* Serviço e Planos */
	private Servico servico;
	private List<Servico> servicos = new ArrayList<>();
	private Atividade atividade;
	private Atividade atividadeSelecionada;
	private List<Atividade> atividades = new ArrayList<>();
	
	/* Inscrição */
	private List<Inscricao> inscricoes = new ArrayList<>();
	private List<Inscricao> inscricoesAtivas = new ArrayList<>();
	private Inscricao inscricao;
	private List<Prioridade> prioridades;
	private Pessoa pessoa;
	
	/* Diario */
	private List<Frequencia> frequencias;
	
	@Inject
	private PessoaService pessoaService;
	@Inject
	private ServicoService servicoService;
	@Inject
	private LoginBean loginBean;


	@PostConstruct
	public void inicializar() {		
		
		data = LocalDate.now();
		setAno(data.getYear());		
		setUnidade(loginBean.getUsuario().getUnidade());
				
		this.prioridades = Arrays.asList(Prioridade.values());
		
		buscarServicos();
	}	
	
	public void excluir() {
		try {			
			
			this.servicoService.excluir(servico, loginBean.getTenantId());
			log.info("servico selecionado: " + servico.getNome());
			
			buscarServicos();
			MessageUtil.sucesso("Serviço " + servico.getNome() + " excluído com sucesso.");
			
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}		
	}
	
	public void copiar() {
		try {			
			
			this.servicoService.copiar(servico);
			log.info("servico selecionado: " + servico.getNome());
			
			setAno(data.getYear());
			buscarServicos();
			MessageUtil.sucesso("Serviço " + servico.getNome() + " copiado com sucesso. Edite-o para alterar os dados se necessário.");
			
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}		
	}
	
	
	public void buscarServicos(){
		log.info("buscando serviços: " + getAno() + " da unidade " + getUnidade().getNome());
		if(loginBean.getUsuario().getUnidade().getTipo() == TipoUnidade.SCFV) {
			servicos = servicoService.buscarServicosAnoExecutora(getAno(), getUnidade(), loginBean.getTenantId());
		}
		else {
			servicos = servicoService.buscarServicosAno(getAno(), getUnidade(), loginBean.getTenantId());
		}
		
		limparServico();	
	}
	
	
	
	/*
	 * Plano e Atividades
	 */
	
	public void salvarAtividade() {
		
		try {		
			log.debug("salvarAtividade: descricao = " + atividade.getDescricao());
			atividade.setServico(servico);		
			this.servicoService.salvar(atividade);
			atividades.removeAll(atividades);
			atividades.addAll(servicoService.buscarAtividades(servico, loginBean.getTenantId()));
			
			MessageUtil.sucesso("Atividade gravada com sucesso.");			
		
			limparAtividade();
						
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}	
	}	
	
	public void excluirAtividade() {
		try {			
			
			this.servicoService.excluir(atividade, loginBean.getTenantId());
			log.info("Atividade excluida: " + atividade.getDescricao());
			atividades.removeAll(atividades);
			atividades.addAll(servicoService.buscarAtividades(servico, loginBean.getTenantId()));
			MessageUtil.sucesso("Atividade " + atividade.getDescricao() + " excluída com sucesso.");
			
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
		
	}
	
	public void limparAtividade() {
		log.debug("limparAtividade()");
		atividade = new Atividade();
		atividade.setTenant_id(loginBean.getTenantId());
		atividades.removeAll(atividades);
		atividades.addAll(servicoService.buscarAtividades(servico, loginBean.getTenantId()));
		
	}
	
	public void limparServico() {
		inscricoes = new ArrayList<>();
		inscricoesAtivas = new ArrayList<>();
		atividades = new ArrayList<>();
		atividadeSelecionada = null;
	}
	
	public void buscarAtividades() {
		atividades.removeAll(atividades);
		atividades.addAll(servicoService.buscarAtividades(servico, loginBean.getTenantId()));
		frequencias = new ArrayList<>();
		inscricoesAtivas = new ArrayList<>();
		atividade = null;
		atividadeSelecionada = null;
		inscricao = null;
		buscarInscricoes();
		MessageUtil.sucesso("Serviço " + servico.getNome() + " selecionado!");
	}
	
	public void selecionarAtividade() {
		log.info("atividade selecionada = " + atividade.getCodigo());
		atividadeSelecionada = atividade;
	}
	
	/*
	 * Inscrições
	 */
	
	public void buscarInscricoesAtivas() {
		
		inscricoesAtivas.removeAll(inscricoes);
		inscricoesAtivas.addAll(servicoService.buscarInscricoesAtivas(servico, loginBean.getTenantId()));
	}
	
	public void buscarInscricoes() {
		
		inscricoes.removeAll(inscricoes);
		inscricoes.addAll(servicoService.buscarInscricoes(servico, loginBean.getTenantId()));
	}
	
	public void alterarPrioridade(Inscricao inscricao) {
		try {			
			this.servicoService.alterar(inscricao);
			
			MessageUtil.sucesso("Inscrição alterada com sucesso.");						
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}	
	}
	
	public void cancelarInscricao(Inscricao inscricao) {
		
		try {			
			this.servicoService.cancelar(inscricao);
			inscricoes.removeAll(inscricoes);
			inscricoes.addAll(servicoService.buscarInscricoes(servico, loginBean.getTenantId()));
			
			MessageUtil.sucesso("Inscrição alterada com sucesso.");						
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}	
	}
	
	public void excluirInscricao() {
		try {			
			
			this.servicoService.excluir(inscricao, loginBean.getTenantId());
			log.info("Inscricao selecionada: " + inscricao.getCodigo());
			//this.inscricoes.remove(inscricao);
			inscricoes.removeAll(inscricoes);
			inscricoes.addAll(servicoService.buscarInscricoes(servico, loginBean.getTenantId()));
			MessageUtil.sucesso("Inscrição de " + inscricao.getPessoa().getNome() + " excluída com sucesso.");
			
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
		
	}
	
	/* adiciona pessoa nova */
	public void adicionarPessoa(SelectEvent<?> event) {	
		try {			
			
			this.inscricao = new Inscricao();
		
			PessoaDTO dto = (PessoaDTO) event.getObject();		
			Pessoa p = pessoaService.buscarPeloCodigo(dto.getCodigo());
			inscricao.setPessoa(p);
			
			validaFaixaEtaria(inscricao.getPessoa(), getServico().getTipoServico());
			
			validaInscricao(inscricao.getPessoa());
			
			inscricao.setData(new Date());
			inscricao.setServico(servico);
			inscricao.setTenant_id(loginBean.getTenantId());
			inscricao.setPrioridade(Prioridade.NAO_HA_SITUACAO_PRIORITARIA);
			
			servicoService.adicionar(inscricao);
			inscricoes.removeAll(inscricoes);
			inscricoes.addAll(servicoService.buscarInscricoes(servico, loginBean.getTenantId()));
			
			MessageUtil.sucesso(this.inscricao.getPessoa().getNome() + " inscrito(a) com sucesso.");			
		
		} catch(NegocioException e) {
			MessageUtil.erro(e.getMessage());
		} catch(Exception e) {
			MessageUtil.erro(e.getMessage());
		}
	}	
	
	public void abrirDialogo() {
		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("width", 1000);
        options.put("height", 500);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("draggable", true);
        options.put("responsive", true);
        options.put("closeOnEscape", true);
        
        /* Verifica se é uma unidade do tipo SCFV  */
        if(loginBean.getUsuario().getUnidade().getTipo() == TipoUnidade.SCFV) {
        	PrimeFaces.current().dialog().openDynamic("/restricted/agenda/SelecionaPessoaGeral", options, null);
        }
        else {
        	PrimeFaces.current().dialog().openDynamic("/restricted/agenda/SelecionaPessoa", options, null); 
        }
               	
    }

	private void validaFaixaEtaria(Pessoa pessoa, TipoServico tipo) throws NegocioException {
				
		int idade = CalculoUtil.calcularIdade(pessoa.getDataNascimento());
		
		if (tipo == TipoServico.IDOSOS) {
			if(!(idade > 59)) {
				throw new NegocioException( "Pessoa fora da faixa etária! idade = " + idade);
			}				
		} else if (tipo == TipoServico.ADULTOS_DE_30_A_59_ANOS) {
			if (!(idade < 60 && idade > 29)) {		
				throw new NegocioException( "Pessoa fora da faixa etária! idade = " + idade);
			}
		} else if (tipo == TipoServico.JOVENS_DE_18_A_29_ANOS) {
			if (!(idade < 30 && idade > 17)) {
				throw new NegocioException( "Pessoa fora da faixa etária! idade = " + idade);
			}
		} else if (tipo == TipoServico.ADOLESCENTES_E_JOVENS_DE_15_A_17_ANOS) {
			if (!(idade < 18 && idade > 14)) {
				throw new NegocioException( "Pessoa fora da faixa etária! idade = " + idade);
			}
		} else if (tipo == TipoServico.CRIANCAS_E_ADOLECENTES_DE_7_A_14_ANOS) {
			if (!(idade < 15 && idade > 6)) {
				throw new NegocioException( "Pessoa fora da faixa etária! idade = " + idade);
			}
		} else if (tipo == TipoServico.CRIANCAS_ATE_6_ANOS) {
			if (!(idade < 7)) {
				throw new NegocioException( "Pessoa fora da faixa etária! idade = " + idade);
			}
		}		
	}	
	
	private void validaInscricao(Pessoa pessoa) throws NegocioException {
		
		for(Inscricao i : inscricoes) {
			
			if(i.getPessoa().getCodigo().longValue() == pessoa.getCodigo().longValue())
				throw new NegocioException(" Esta pessoa já está inscrita (" + pessoa.getNome() + ").");
		}

	}

	
	
	/* 
	 * Diário Frequencias 
	 */	
	
	
	public void buscarDiario() {	
		
		try {
			log.info("buscarFrequencias..." + getAtividade().getCodigo());		
			
			frequencias = servicoService.carregarFrequencias(getInscricoes(), getAtividade(), servico, loginBean.getTenantId());
			
			MessageUtil.sucesso("Atividade " + getAtividade().getDescricao() + " selecionada!");
		
		} catch (Exception e) {
			MessageUtil.erro(e.getMessage());
		}
	}	
	

	public void salvarFrequencia(Frequencia frequencia) {
		try {
			servicoService.salvar(frequencia);
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
		MessageUtil.sucesso("Frequência salva com sucesso!");
	}
	
	
	

}