package com.softarum.svsa.controller.rel;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.PieChartModel;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.dao.lazy.LazyAtendimento;
import com.softarum.svsa.modelo.AgendamentoColetivo;
import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Ano;
import com.softarum.svsa.modelo.enums.Grupo;
import com.softarum.svsa.modelo.enums.Mes;
import com.softarum.svsa.modelo.to.DatasIniFimTO;
import com.softarum.svsa.service.AgendamentoColetivoService;
import com.softarum.svsa.service.AgendamentoIndividualService;
import com.softarum.svsa.service.PessoaService;
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
public class RelatorioAtendimentosBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	
	private LazyAtendimento lazyAtendimentos;	
	private PieChartModel graficoAtendimentos;	
	private Long qdeAtendimentos = 0L;
	private PieChartModel graficoAtendimentosCol;	
	private Long qdeAtendimentosCol = 0L;
	private List<ListaAtendimento> listaAtendimentos = new ArrayList<>();
	private List<ListaAtendimento> listaAtendimentosGrafico = new ArrayList<>();
	private List<AgendamentoColetivo> atendimentosGraficoCol = new ArrayList<>();
	private Unidade unidade;
	private Date dataInicio;
	private Date dataFim;
	private ListaAtendimento itemAlterar = new ListaAtendimento();
	private ListaAtendimento itemExcluir;
	private ListaAtendimento itemMigrar;
	private Long codigoPessoa;
	private String nomePessoa;
	private boolean consultado = false;
	
	private List<String> anos = new ArrayList<>(Arrays.asList(Ano.getAnos()));
	private Integer ano;
	private List<Mes> meses;	
	private Mes mes;
	private DatasIniFimTO datasTO;
		
	
	@Inject
	AgendamentoIndividualService listaAtendimentoService;
	@Inject
	AgendamentoColetivoService agendamentoService;
	@Inject
	PessoaService pessoaService;
	@Inject
	private LoginBean loginBean;
		
	
	@PostConstruct
	public void inicializar() {	
		
		LocalDate data = LocalDate.now();
		setAno(data.getYear());
		setMes(Mes.porCodigo(data.getMonthValue()));
		
		anos = new ArrayList<>(Arrays.asList(Ano.getAnos()));		
		meses = Arrays.asList(Mes.values());

		unidade = loginBean.getUsuario().getUnidade();		
		graficoAtendimentos = new PieChartModel();
		graficoAtendimentosCol = new PieChartModel();
		
		consultarAtendimentos();
	}
	
	public void consultarAtendimentos() {
		
		datasTO = DateUtils.getDatasIniFim(getAno(), getMes());
		lazyAtendimentos = new LazyAtendimento(listaAtendimentoService, unidade, datasTO, loginBean.getTenantId());
		consultado = true;
	}
	
	public void initGraficoAtendimentos() {		
		listaAtendimentosGrafico =  listaAtendimentoService.buscarAtendimentosCodAuxGrafico(unidade, datasTO.getIni(), datasTO.getFim(), loginBean.getTenantId());
		qdeAtendimentos = Long.valueOf(listaAtendimentosGrafico.size()); // listaAtendimentoService.encontrarQuantidade(unidade, dataInicio, dataFim);	
		createPieModel();
	}

	private void createPieModel() {
		
		log.info("Criando grafico  ... ");	
		graficoAtendimentos = new PieChartModel(); 
        
        
        if(listaAtendimentosGrafico != null && listaAtendimentosGrafico.size() > 0) {

        	
        	log.info("Tamanho da lista: " + listaAtendimentosGrafico.size());        	
        	
        	String codigo = listaAtendimentosGrafico.get(0).getCodigoAuxiliar().toString();        	
        	int qde = 0; 
	        for(ListaAtendimento l: listaAtendimentosGrafico) {   
        		
        		
	        	if(!l.getCodigoAuxiliar().toString().equals(codigo)) {
	        		graficoAtendimentos.set(codigo, qde);
	            	
	            	codigo = l.getCodigoAuxiliar().toString();
	            	qde = 1;	            	
	        	}
	        	else {
	        		qde++;
	        	}
	        }
	        log.info("criado o grafico");       
	        graficoAtendimentos.set(codigo, qde);	        
	        graficoAtendimentos.setLegendPosition("e");	        
	        
        }
        else
        	MessageUtil.alerta("Não existe atendimentos realizados.");
        
    }
	
	public void alterar() {
	
		try {
			listaAtendimentoService.salvarAlterar(itemAlterar, loginBean.getUsuario() );
			log.info("Atendimento Alterado por " + loginBean.getUserName());
			
			MessageUtil.sucesso("Atendimento número (" + itemAlterar.getCodigo() + ") alterado com sucesso.");
		} catch (NegocioException e) {
			log.error(e.getMessage());
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void excluir() {
		try {
			listaAtendimentoService.excluir(itemExcluir);
			log.info("Atendimento DELETED por " + loginBean.getUserName());
			this.listaAtendimentos.remove(itemExcluir);
			MessageUtil.sucesso("Atendimento número (" + itemExcluir.getCodigo() + ") excluído com sucesso.");
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void migrarAtendimento(){
		try {
			Pessoa pessoa = pessoaService.buscarPessoa(codigoPessoa, unidade, loginBean.getTenantId());
			if (pessoa != null) {
				

					itemMigrar.setPessoa(pessoa);
					listaAtendimentoService.encerrarAtendimento(itemMigrar);
					log.info("Atendimento MIGRADO para a pessoa " + itemMigrar.getPessoa().getNome());

					limparPessoa();

					MessageUtil.sucesso("Atendimento MIGRADO para a pessoa " + itemMigrar.getPessoa().getNome());
				}
			else {
				MessageUtil.erro("Essa PESSOA não existe ou é de outra unidade!");
			}

		}
		catch (Exception e) {
			MessageUtil.erro("Essa PESSOA não existe ou é de outra unidade!");
		}
	}
	
	private void limparPessoa() {
		codigoPessoa = null;
		nomePessoa = null;
	}
	
	public void buscarNomePessoa() {
		try {
			Pessoa pessoa = pessoaService.buscarPessoa(codigoPessoa, unidade, loginBean.getTenantId());		
					
			if(pessoa != null) {
				setNomePessoa(pessoa.getNome());
			}
			else{
				limparPessoa();
			}
		} catch(Exception e) {
			limparPessoa();
			MessageUtil.erro("Não existe PESSOA com esse código!");
		}
	}
	
	public boolean isCoordenador() {
		if(loginBean.getUsuario().getGrupo() == Grupo.COORDENADORES)
			return true;
		
		return false;
	}

}