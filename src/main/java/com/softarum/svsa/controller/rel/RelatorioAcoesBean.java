package com.softarum.svsa.controller.rel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.Acao;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Grupo;
import com.softarum.svsa.service.AcaoService;
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
public class RelatorioAcoesBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	
	private Long qdeAcoes = 0L;	
	private List<Acao> listaAcoes = new ArrayList<>();
	
	private Unidade unidade;
	private Date dataInicio;
	private Date dataFim;
	private Acao itemExcluir;
	private Acao itemMigrar;
	private Acao itemAlterar;
	private Long codigoPessoa;
	private String nomePessoa;
	
	private Long codigoUsuarioLogado = null;
		
	
	@Inject
	AcaoService acaoService;	
	@Inject
	PessoaService pessoaService;
	@Inject
	private LoginBean loginBean;
		
	
	@PostConstruct
	public void inicializar() {		

		codigoUsuarioLogado = loginBean.getUsuario().getCodigo();
		unidade = loginBean.getUsuario().getUnidade();
		dataInicio = DateUtils.minusDay(new Date());
		
		listaAcoes =  acaoService.buscarAcoesPeriodo(unidade, dataInicio, dataFim, loginBean.getTenantId());		
	}	
	
	public void consultarPeriodo() {
		log.debug("Consultando  ... ");	
		listaAcoes =  acaoService.buscarAcoesPeriodo(unidade, dataInicio, dataFim, loginBean.getTenantId());
		log.debug("size "+listaAcoes.size());
		qdeAcoes = (long) listaAcoes.size(); 
		log.info("count " + qdeAcoes);
	}		
	
	public void excluir() {
		try {
			acaoService.excluir(itemExcluir);
			log.info("Ação DELETED por " + loginBean.getUserName());
			this.listaAcoes.remove(itemExcluir);
			MessageUtil.sucesso("Ação número (" + itemExcluir.getCodigo() + ") excluída com sucesso.");
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void alterar() {
		try {
			acaoService.salvarAlterar(itemAlterar, codigoUsuarioLogado );
			log.info("Ação Alterada por " + loginBean.getUserName());
			
			MessageUtil.sucesso("Ação número (" + itemAlterar.getCodigo() + ") alterada com sucesso.");
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void migrarAcao() {
		try {
			Pessoa pessoa = pessoaService.buscarPessoa(codigoPessoa, unidade, loginBean.getTenantId());
			
			if(pessoa != null) {
				
				if(itemMigrar.getPessoas() != null && itemMigrar.getPessoas().size() > 1) {
					log.info("tentativa de migração de ação coletiva.");					
					MessageUtil.erro("Não foi possível migrar. Esta é uma ação coletiva.");
				} else if(itemMigrar.getPessoas() != null && itemMigrar.getPessoas().size() == 1){
					itemMigrar.setPessoa(itemMigrar.getPessoas().get(0));
					itemMigrar.setPessoas(null);
					log.info("Ação MIGRADA para a pessoa " + itemMigrar.getPessoa().getNome());
					acaoService.salvar(itemMigrar);
					MessageUtil.sucesso("Ação MIGRADA para a pessoa " + itemMigrar.getPessoa().getNome());
				} else if(itemMigrar.getPessoa() != null) {
						itemMigrar.setPessoa(pessoa);
						itemMigrar.setPessoas(null);
						log.info("Ação MIGRADA para a pessoa " + itemMigrar.getPessoa().getNome());
						acaoService.salvar(itemMigrar);						
						MessageUtil.sucesso("Ação MIGRADA para a pessoa " + itemMigrar.getPessoa().getNome());
				}
				limparPessoa();
			} else {
				MessageUtil.erro("Essa PESSOA não existe ou é de outra unidade!");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
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
		} catch(Exception e) {
			MessageUtil.sucesso("Não existe PESSOA com esse código!");
		}
	}
	
	public boolean isCoordenador() {
		if(loginBean.getUsuario().getGrupo() == Grupo.COORDENADORES)
			return true;
		
		return false;
	}

}