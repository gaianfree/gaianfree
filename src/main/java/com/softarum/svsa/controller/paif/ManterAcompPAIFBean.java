package com.softarum.svsa.controller.paif;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.modelo.PlanoAcompanhamento;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.Ano;
import com.softarum.svsa.modelo.enums.PerfilFamilia;
import com.softarum.svsa.modelo.enums.Programa;
import com.softarum.svsa.modelo.enums.RazaoDesligamento;
import com.softarum.svsa.modelo.to.PessoaDTO;
import com.softarum.svsa.service.PessoaService;
import com.softarum.svsa.service.PlanoAcompanhamentoService;
import com.softarum.svsa.service.UsuarioService;
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
public class ManterAcompPAIFBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private PessoaReferencia pessoaReferencia;
	private List<PlanoAcompanhamento> planos = new ArrayList<>();	
	private List<String> anos = new ArrayList<>(Arrays.asList(Ano.getAnos()));
	private List<Usuario> tecnicos;
	private List<Usuario> adicionais = new ArrayList<>();
	private DualListModel<Usuario> tecnicosAdicionais;
	private List<Programa> programas;
	private List<RazaoDesligamento> razoes;
	private List<PerfilFamilia> perfis;
	private PlanoAcompanhamento plano;
	private Prontuario prontuario;
	private Integer ano;
	private Unidade unidade;
	
	@Inject
	private PessoaService pessoaService;
	@Inject
	private MetaPAIFBean metaBean;
	@Inject
	private PlanoAcompanhamentoService planoService;
	@Inject
	private UsuarioService usuarioService;
	@Inject
	private LoginBean loginBean;

	@PostConstruct
	public void inicializar() {	
		LocalDate data = LocalDate.now();
		setAno(data.getYear());
		this.programas = Arrays.asList(Programa.values());	
		this.razoes = Arrays.asList(RazaoDesligamento.values());	
		this.perfis = Arrays.asList(PerfilFamilia.values());
		setUnidade(loginBean.getUsuario().getUnidade());
		carregarTecnicos();	
		this.limpar();
	}
	
	public void carregarTecnicos() {

		this.tecnicos =  usuarioService.buscarTecnicos(getUnidade(), loginBean.getTenantId());
		adicionais.addAll(tecnicos);
		adicionais.remove(loginBean.getUsuario());
	}
	
	
	private void carregarTecnicosAdicionais() {		
		
		List<Usuario>tecsSource = new ArrayList<Usuario>();
		tecsSource = getAdicionais();		
		List<Usuario> tecsTarget = new ArrayList<Usuario>();
       
        tecnicosAdicionais = new DualListModel<Usuario>(tecsSource, tecsTarget);
        
	}
	
	public void carregarTecnicosPlano(PlanoAcompanhamento plan) {
		
		List<Usuario>tecsSource = getAdicionais();
		List<Usuario> tecsTarget = new ArrayList<Usuario>();
		tecsTarget = plan.getTecnicos();
		tecsSource.removeAll(tecsTarget);
       
        tecnicosAdicionais = new DualListModel<Usuario>(tecsSource, tecsTarget);
	}
	
	public void onTransfer(TransferEvent event) {

        for(Object tecnico : event.getItems()) {        	
        	MessageUtil.sucesso("Técnico " + ((Usuario) tecnico).getNome() + " selecionado.");
        }         
    }
	
	public void salvar() {
		try {
			
			log.info(" plano "+plano.getCodigo());
			
			plano.setProntuario(getProntuario());			
			
			plano.setTecnicos(tecnicosAdicionais.getTarget());
			
			this.planoService.salvar(plano, loginBean.getTenantId());
			
			MessageUtil.sucesso("Plano de Acompanhamento gravado com sucesso.");
			
			buscarPlanos();
		
			limpar();
						
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void salvarEncerramento() {
		try {
			
			log.info(" encerrando plano "+plano.getCodigo());				
			
			/*
			metaBean.getMeta().setData(new Date());
			metaBean.getMeta().setPlanoAcompanhamento(plano);
			metaBean.getMeta().setTecnico(loginBean.getUsuario());		
			
			this.planoService.salvarEncerramento(plano, metaBean.getMeta());
			*/
			
			this.planoService.salvarEncerramento(plano);
			MessageUtil.sucesso("Plano de Acompanhamento encerrado com sucesso.");
			
			buscarPlanos();
		
			limpar();
			metaBean.setMetas(null);
						
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void limpar() {

		plano = new PlanoAcompanhamento();
		
		LocalDate data = LocalDate.now();
		plano.setAno(data.getYear());
		plano.setTecnico(loginBean.getUsuario());
		plano.setTenant_id(loginBean.getTenantId());
		
		carregarTecnicosAdicionais();
		
		metaBean.limpar();
		
	}

	
	public void excluir() {
		try {			
			
			this.planoService.excluir(plano);
			log.info("plano selecionado: " + plano.getCodigo());
			
			this.planos.remove(plano);
			MessageUtil.sucesso("Plano " + plano.getCodigo() + " excluído com sucesso.");
			
		} catch (NegocioException e) {
			e.printStackTrace();
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
        PrimeFaces.current().dialog().openDynamic("/restricted/agenda/SelecionaPessoaReferencia", options, null);
        	
    }
	
	public void selecionarPessoaReferencia(SelectEvent<?> event) {
		
		PessoaDTO dto = (PessoaDTO) event.getObject();		
		this.pessoaReferencia = pessoaService.buscarPFPeloCodigo(dto.getCodigo());
		
		setProntuario(this.pessoaReferencia.getFamilia().getProntuario());
		
		buscarPlanos();
				
		MessageUtil.sucesso("Pessoa Referencia Selecionada: " + this.pessoaReferencia.getNome());		
		
		limpar();
		
		metaBean.setMetas(null);
	}
	
	public boolean isPessoaReferenciaSelecionada() {
        return pessoaReferencia != null && pessoaReferencia.getCodigo() != null;
    }
	
	public void buscarPlanos(){
		planos = planoService.buscarPlanos(getProntuario(), loginBean.getTenantId());
	}	
	
	public boolean isProntuarioSelecionado() {
		if(getProntuario() != null && getProntuario().getCodigo() != null)
			return true;
		return false;
	}
	
	public boolean isPlanoSelecionado() {
		if(getPlano() != null && getPlano().getCodigo() != null)
			return true;
		return false;
	}

	public void setPlano(PlanoAcompanhamento plano) {
		this.plano = plano;
		log.info("(ManteAcompPAIFBean) setPlano : " + plano.getCodigo());
		metaBean.setPlano(this.plano);
	}
	
}