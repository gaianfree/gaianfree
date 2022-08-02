package com.softarum.svsa.controller.rel;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Ano;
import com.softarum.svsa.modelo.enums.Grupo;
import com.softarum.svsa.modelo.enums.Mes;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.modelo.to.rma.RmaPopTO;
import com.softarum.svsa.service.RMAService;
import com.softarum.svsa.service.RelatorioRMAPopService;
import com.softarum.svsa.service.UnidadeService;
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
public class RelatorioRMAPopBean implements Serializable {

	private static final long serialVersionUID = 1777574958848646094L;
	
	private List<Unidade> unidades = new ArrayList<>();	
	private Mes mes;
	private List<Mes> mesesFechar;	
	private Mes mesFechar;
	private List<String> anos = new ArrayList<>(Arrays.asList(Ano.getAnos()));
	private Integer ano;
	private Unidade unidadeLogado;
	private Unidade unidade;
	private String mesAnoRef;
	private boolean gestor = false;
	
	private List<String> rmasFechados = new ArrayList<>();
	private RmaPopTO rma = new RmaPopTO();
		
	
	@Inject
	private UnidadeService unidadeService;
	@Inject
	private RelatorioRMAPopService rmaPopService;
	@Inject
	RMAService rmaService;
	@Inject
	private LoginBean loginBean;

		
	
	@PostConstruct
	public void inicializar() {	
		
		LocalDate data = LocalDate.now();
		setAno(data.getYear());
		int codigo = data.getMonthValue();
		setMes(Mes.porCodigo(codigo));
		
		unidades = unidadeService.buscarCREAS(loginBean.getTenantId());
						
		/* Se for gestor ou CRAS não permite gerar RMA so visualizar gravados. */
		if(loginBean.getUsuario().getGrupo() == Grupo.GESTORES || loginBean.getUsuario().getUnidade().getTipo() == TipoUnidade.CRAS) {
			gestor = true;			
		}
		else {	
		
			unidadeLogado = loginBean.getUsuario().getUnidade();
			
			this.rma = rmaPopService.gerarRelatorioRMA(unidadeLogado, getAno(), getMes(), loginBean.getTenantId());
			
			rmasFechados = rmaService.buscarRmasPopFechados(unidadeLogado, loginBean.getTenantId());
			
			recuperarMesAberto(rmasFechados);
		}
		
		setUnidade(unidadeLogado);
	}
	// recupera os meses com RMA ainda aberto
	private void recuperarMesAberto(List<String> rmasFechados){
		
		setMesesFechar(new LinkedList<>(Arrays.asList(Mes.values())));
		
		for(String mesAno : rmasFechados) {
			StringTokenizer st = new StringTokenizer(mesAno);
			String mes = st.nextToken("/");
			String ano = st.nextToken("/");
			String anoCorrente = String.valueOf(getAno());
			// remove mes já fechado
			if(anoCorrente.equals(ano)) {
				log.info("fechado RMA mes/ano = " + mes + "/" + ano);
				log.debug("valueof " + Mes.valueOf(mes) ); 				
				getMesesFechar().remove( Mes.valueOf(mes) );
			}
		}
	}
	

	public void gerarRMA() {
		log.info("Gerando RMA do mes = " + getMes()  + "  unidade = " + unidadeLogado);
		this.rma = rmaPopService.gerarRelatorioRMA(unidadeLogado, getAno(), getMes(), loginBean.getTenantId());
		MessageUtil.info("RMA do mês " + getMes() + " gerado com sucesso!") ;
	}
	
	public void fecharRma() throws NegocioException {
		
		log.info("fechando RMA do mes = " + getMes()  + "  unidade = " + unidadeLogado);
		
		try {
			
			this.rma = rmaPopService.gerarRelatorioRMA(unidadeLogado, getAno(), getMesFechar(), loginBean.getTenantId());
			
			rmaService.salvarPop(rma, loginBean.getTenantId());
			rmasFechados = rmaService.buscarRmasPopFechados(unidadeLogado, loginBean.getTenantId());
			
			recuperarMesAberto(rmasFechados);
			
			MessageUtil.info("RMA do mês " + getMes() + " fechado com sucesso!") ;
			
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void buscarRmaFechado() {
		
		try {
			log.info("Recuperando Rma fechado = " + mesAnoRef  + "  unidade = " + unidade);
			
			this.rma = rmaService.buscarRmaPopFechado(unidade, mesAnoRef, loginBean.getTenantId());
					
			MessageUtil.info("RMA do mês " + mesAnoRef + " recuperado com sucesso!") ;
		}
		catch(Exception e) {
			MessageUtil.erro("Não há RMA fechado!");
		}
	}
	
	public void buscarRmasFechados() {
		
		try {
			log.info("Recuperando Rmas fechados "  + "  unidade = " + unidade);
			
			rmasFechados = rmaService.buscarRmasPopFechados(unidade, loginBean.getTenantId());
			
			MessageUtil.info("RMAs fechados recuperados!") ;
		}
		catch(Exception e) {
			MessageUtil.erro("Não há RMAs fechados!");
		}
	}
	
	public boolean isCoordenador() {
		if(loginBean.getUsuario().getGrupo() == Grupo.COORDENADORES) {
			return true;
		}
		return false;
	}
	
}