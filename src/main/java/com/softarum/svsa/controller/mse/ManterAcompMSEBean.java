package com.softarum.svsa.controller.mse;

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
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Pia;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.Ano;
import com.softarum.svsa.modelo.enums.RazaoDesligamento;
import com.softarum.svsa.modelo.enums.StatusPia;
import com.softarum.svsa.modelo.enums.TipoMse;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.modelo.to.PessoaDTO;
import com.softarum.svsa.service.PessoaService;
import com.softarum.svsa.service.PiaService;
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
public class ManterAcompMSEBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Pessoa pessoa;
	private List<Pia> planos = new ArrayList<>();
	private List<String> anos = new ArrayList<>(Arrays.asList(Ano.getAnos()));
	private List<Usuario> tecnicos;
	private List<Usuario> adicionais = new ArrayList<>();
	private DualListModel<Usuario> tecnicosAdicionais;
	private List<RazaoDesligamento> razoes;
	private List<StatusPia> situacoes;
	private List<StatusPia> situacoesEnc;
		
	private List<TipoMse> tipos;
	private Pia plano;
	private Integer ano;
	private Unidade unidade;
	private boolean psc = false;
	
	@Inject
	private PessoaService pessoaService;
	@Inject
	private MetaMSEBean metaBean;
	@Inject
	private PiaService piaService;
	@Inject
	private UsuarioService usuarioService;
	@Inject
	private LoginBean loginBean;

	
	

	@PostConstruct
	public void inicializar() {
		
		log.info("Iniciando ManterAcompaMSEBean ...");
		
		LocalDate data = LocalDate.now();
		setAno(data.getYear());		
		razoes = Arrays.asList(RazaoDesligamento.values());
		tipos = Arrays.asList(TipoMse.values());
		situacoes = Arrays.asList(StatusPia.ATIVO, StatusPia.AGUARDANDO);
		situacoesEnc = Arrays.asList(StatusPia.AGUARDANDO);
		setUnidade(loginBean.getUsuario().getUnidade());
		carregarTecnicos();	
		this.limpar();
	}
	
	public void carregarTecnicos() {

		tecnicos =  usuarioService.buscarTecnicos(getUnidade(), loginBean.getTenantId());
		adicionais.addAll(tecnicos);
		adicionais.remove(loginBean.getUsuario());
		
	}
	
	private void carregarTecnicosAdicionais() {		
		
		List<Usuario>tecsSource = new ArrayList<Usuario>();
		tecsSource = getAdicionais();		
		List<Usuario> tecsTarget = new ArrayList<Usuario>();
       
        tecnicosAdicionais = new DualListModel<Usuario>(tecsSource, tecsTarget);
	}
	
	public void carregarTecnicosPlano(Pia plan) {		
		
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
						
			plano.setAdolescente(getPessoa());			
			
			plano.setTecnicos(tecnicosAdicionais.getTarget());
			
			this.piaService.salvar(plano, loginBean.getTenantId());
			
			MessageUtil.sucesso("Plano de Acompanhamento gravado com sucesso.");
			
			buscarPlanosAno();
		
			limpar();
						
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	
	public void salvarEncerramento() {
		try {
			
			log.info(" encerrando plano "+plano.getCodigo());
			
			this.piaService.salvarEncerramento(plano);
			
			MessageUtil.sucesso("Plano de Acompanhamento encerrado com sucesso. EXTINTO.");
			
			buscarPlanosAno();
		
			limpar();
						
		} catch (NegocioException e) {
			log.error(e.getMessage());
			MessageUtil.erro(e.getMessage());
		}		
	}

	
	public void limpar() {

		plano = new Pia();
		
		plano.setAno(getAno());
		plano.setResponsavel(loginBean.getUsuario());	
		plano.setTenant_id(loginBean.getTenantId());
		carregarTecnicosAdicionais();
		
		metaBean.limpar();
		
	}

	
	
	public void excluir() {
		try {			
			
			this.piaService.excluir(plano);
			//log.info("plano selecionado: " + plano.getCodigo());
			
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
        PrimeFaces.current().dialog().openDynamic("/restricted/agenda/SelecionaPessoa", options, null);
        	
    }	
	

	public void selecionarPessoa(SelectEvent<?> event) {
		
		PessoaDTO dto = (PessoaDTO) event.getObject();		
		Pessoa p = pessoaService.buscarPeloCodigo(dto.getCodigo());
		setPessoa(p);
		
		buscarPlanosAno();
				
		MessageUtil.sucesso("Pessoa Selecionada: " + this.pessoa.getNome());		
		
		limpar();
		
	}
	
	public boolean isPessoaSelecionada() {
        return pessoa != null && pessoa.getCodigo() != null;
    }
	
	public void buscarPlanosAno(){
		planos = piaService.buscarPlanosAno(getAno(), getPessoa().getCodigo(), loginBean.getTenantId());
		metaBean.setMetas(null);
	}	
	
	public boolean isPlanoSelecionado() {
		if(getPlano() != null && getPlano().getCodigo() != null)
			return true;
		return false;
	}
	
		
	public void buscarMetas(){
		log.info("Plano : " + plano.getCodigo());
		metaBean.setPlano(getPlano());
		metaBean.buscarMetas();
		getPlano().setMetas(metaBean.getMetas());
	}

	public void setPlano(Pia plano) {
		this.plano = plano;
		log.debug("(ManteAcompMSEBean) setPlano : " + plano.getCodigo());
		metaBean.setPlano(this.plano);
	}
	
	public Boolean verificaPsc() {
		//log.info("Verifica Psc");
		if(plano.getTipoMse() == TipoMse.PSC || plano.getTipoMse() == TipoMse.LA_e_PSC)
			psc = true;
		else
			psc = false;
		return psc;
	}

	public boolean isCras() {
		if(loginBean.getUsuario().getUnidade().getTipo() != TipoUnidade.CREAS)
			return true;
		return false;
	}
	
}