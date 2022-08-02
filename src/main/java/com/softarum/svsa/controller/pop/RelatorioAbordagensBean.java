package com.softarum.svsa.controller.pop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.Abordagem;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.CodigoAuxiliarAtendimento;
import com.softarum.svsa.modelo.enums.EnumUtil;
import com.softarum.svsa.modelo.enums.Grupo;
import com.softarum.svsa.modelo.enums.Sexo;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.service.AbordagemService;
import com.softarum.svsa.util.DateUtils;
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
public class RelatorioAbordagensBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	
	private Long qdeAbordagens = 0L;	
	private List<Abordagem> abordagens = new ArrayList<>();
	private List<CodigoAuxiliarAtendimento> situacoes;
	private List<Sexo> sexos;
	private boolean creas = false;
	
	private Unidade unidade;
	private Date dataInicio;
	private Date dataFim;
	private Abordagem abordagem = new Abordagem();

	
	private Long codigoUsuarioLogado = null;
		
	
	@Inject
	AbordagemService abordagemService;	
	@Inject
	private LoginBean loginBean;
		
	
	@PostConstruct
	public void inicializar() {		

		codigoUsuarioLogado = loginBean.getUsuario().getCodigo();
		unidade = loginBean.getUsuario().getUnidade();
		dataInicio = DateUtils.minusDay(new Date());
		this.situacoes = EnumUtil.getCodigosAbordagem();
		this.sexos = Arrays.asList(Sexo.values());
		
		if(loginBean.getUsuario().getUnidade().getTipo() == TipoUnidade.CREAS) {			
			creas = true;
			this.situacoes = EnumUtil.getCodigosAbordagem();
			this.sexos = Arrays.asList(Sexo.values());
			abordagens = abordagemService.buscarTodosDia(loginBean.getTenantId(), unidade);
		}
		else {
			MessageUtil.info("Essa funcionalidade é apenas para unidades do tipo CREAS!");
		}
		
		abordagens =  abordagemService.buscarAbordagensPeriodo(unidade, dataInicio, dataFim, loginBean.getTenantId());		
	}	
	
	public void consultarPeriodo() {
		log.info("Consultando  ... ");	
		abordagens =  abordagemService.buscarAbordagensPeriodo(unidade, dataInicio, dataFim, loginBean.getTenantId());
		log.info("size "+abordagens.size());
		qdeAbordagens = (long) abordagens.size(); 
		log.info("count " + qdeAbordagens);
	}		
	
	public void excluir() {
		try {
			abordagemService.excluir(abordagem);
			log.info("Abordagem DELETED por " + loginBean.getUserName());
			this.abordagens.remove(abordagem);
			MessageUtil.sucesso("Abordagem número (" + abordagem.getCodigo() + ") excluída com sucesso.");
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void alterar() {
		try {
			abordagemService.salvarAlterar(abordagem, codigoUsuarioLogado );
			log.info("Abordagem Alterada por " + loginBean.getUserName());
			
			MessageUtil.sucesso("Abordagem número (" + abordagem.getCodigo() + ") alterada com sucesso.");
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
		
	public boolean isCoordenador() {
		if(loginBean.getUsuario().getGrupo() == Grupo.COORDENADORES)
			return true;
		
		return false;
	}

}