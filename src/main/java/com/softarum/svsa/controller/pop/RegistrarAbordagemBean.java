package com.softarum.svsa.controller.pop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.Abordagem;
import com.softarum.svsa.modelo.PessoaAbordagem;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.CodigoAuxiliarAtendimento;
import com.softarum.svsa.modelo.enums.EnumUtil;
import com.softarum.svsa.modelo.enums.Sexo;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.service.AbordagemService;
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
public class RegistrarAbordagemBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Abordagem abordagem;
	private List<Abordagem> abordagens = new ArrayList<>();
	private List<CodigoAuxiliarAtendimento> situacoes;
	private List<Sexo> sexos;
	private Usuario usuarioLogado;	
	private boolean creas = false;
	
	@Inject
	private AbordagemService abordagemService;
	
	@Inject
	private LoginBean loginBean;

	@PostConstruct
	public void inicializar()  {	
		
		try {
			usuarioLogado = loginBean.getUsuario();

			if(usuarioLogado.getUnidade().getTipo() == TipoUnidade.CREAS) {			
				creas = true;
				this.situacoes = EnumUtil.getCodigosAbordagem();
				this.sexos = Arrays.asList(Sexo.values());
				abordagens = abordagemService.buscarTodosDia(loginBean.getTenantId(), loginBean.getUsuario().getUnidade());
			}
			else {
				MessageUtil.info("Essa funcionalidade é apenas para unidades do tipo CREAS!");
			}
			limpar();
		}		
		catch(Exception e){
			log.error("Erro inicializar() Realizar atendimento sem agendamento: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public void salvar() {
		try {
			
			abordagem = this.abordagemService.salvar(abordagem);			
			
			MessageUtil.sucesso("Abordagem salva com sucesso!");			
		
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
		
		abordagens = abordagemService.buscarTodosDia(loginBean.getTenantId(), loginBean.getUsuario().getUnidade());
		this.limpar();
	}
	
	public void excluir() {
		try {
			this.abordagemService.excluir(abordagem);
			abordagens = abordagemService.buscarTodosDia(loginBean.getTenantId(), loginBean.getUsuario().getUnidade());
			MessageUtil.sucesso("Abordagem" + abordagem.getCodigo() + " excluída com sucesso.");
			
			limpar();
				
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	

	public void limpar() {		

		this.abordagem = new Abordagem();
		this.abordagem.setPessoa(new PessoaAbordagem());
		this.abordagem.setTecnico(usuarioLogado);
		this.abordagem.setUnidade(usuarioLogado.getUnidade());
		this.abordagem.setTenant_id(loginBean.getTenantId());
	}
	
	public List<String> buscarNomes(String query) {
        List<String> results = new ArrayList<>();
        
        try {
			results = abordagemService.buscarNomes(query, usuarioLogado.getUnidade(), loginBean.getTenantId());		
		
			
		} catch(Exception e) {
			MessageUtil.alerta("Não existe PESSOA com esse nome!");
		}        
       
        return results;
    }
	
	public void buscarPessoa() {
       
        try {
        	PessoaAbordagem p = abordagemService.buscarPeloNome(abordagem.getPessoa().getNome());
        	abordagem.setPessoa(p);
			
		} catch(Exception e) {
			MessageUtil.alerta("Não existe PESSOA com esse nome!");
		}        
	}
	
	
}