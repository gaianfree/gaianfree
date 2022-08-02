package com.softarum.svsa.controller.scfv;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
//import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.Servico;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.DiaSemana;
import com.softarum.svsa.modelo.enums.Status;
import com.softarum.svsa.modelo.enums.TipoServico;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.service.ServicoService;
import com.softarum.svsa.service.UnidadeService;
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
public class ManterSCFVCadBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Servico servico;
	private List<DiaSemana> diasSemana;
	private List<TipoServico> tiposServicos;
	private List<Usuario> tecnicos;
	private Unidade unidade;
	private List<Status> status;
	private boolean scfv = true;
		
	@Inject
	private UnidadeService unidadeService;
	@Inject
	private ServicoService servicoService;
	@Inject
	private UsuarioService usuarioService;
	@Inject
	private LoginBean loginBean;	
			
	@PostConstruct
	public void inicializar() {
		
		setUnidade(loginBean.getUsuario().getUnidade());		
		carregarTecnicos();
		this.tiposServicos = carregarTipos();
		this.diasSemana = Arrays.asList(DiaSemana.values());		
		this.setStatus(Arrays.asList(Status.values()));
		
		this.limpar();	
	}	
	
	public void salvar() {
		try {
			
			log.info(" semana "+servico.getDiaSemana());
			
			if(getUnidade().getTipo() == TipoUnidade.SCFV) {
				servico.setUnidade(getUnidade().getUnidadeVinculada());
				servico.setUnidadeExecutora(getUnidade());
			}
			
			LocalDate data = servico.getDataIni().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();			
			servico.setAno(data.getYear());
			servico.setTenant_id(loginBean.getTenantId());
			
			this.servicoService.salvar(servico);
			
			MessageUtil.sucesso("Serviço gravado com sucesso.");			
		
			limpar();
						
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void limpar() {

		this.servico = new Servico();
		this.servico.setUnidade(getUnidade());
		this.servico.setStatus(Status.ATIVO);
		this.servico.setTenant_id(loginBean.getTenantId());
		this.tiposServicos = carregarTipos();
		this.scfv = true;
	}

	public void carregarTecnicos() {

		this.tecnicos = usuarioService.buscarTecnicos(unidade, loginBean.getTenantId());
		
	}
	
	public void carregarTiposServicos() {
		if(isScfv()) {
			log.info(isScfv());
			carregarTipos();
		}
		else {
			this.tiposServicos = Arrays.asList(TipoServico.ATIVIDADES_CARATER_NAO_CONTINUADO, TipoServico.GRUPO_AMBITO_PAIF);
		}
	}
	
	private List<TipoServico> carregarTipos() {
		this.tiposServicos = Arrays.asList(TipoServico.CRIANCAS_ATE_6_ANOS,
				TipoServico.CRIANCAS_E_ADOLECENTES_DE_7_A_14_ANOS,
				TipoServico.ADOLESCENTES_E_JOVENS_DE_15_A_17_ANOS,
				TipoServico.JOVENS_DE_18_A_29_ANOS,
				TipoServico.ADULTOS_DE_30_A_59_ANOS,
				TipoServico.IDOSOS);
		return this.tiposServicos;
	}
}
