package com.softarum.svsa.controller.pront;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.PieChartModel;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.Acao;
import com.softarum.svsa.modelo.FormaIngresso;
import com.softarum.svsa.modelo.HistPessoaUV;
import com.softarum.svsa.modelo.HistoricoTransferencia;
import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.modelo.ObsComposicaoFamiliar;
import com.softarum.svsa.modelo.Pais;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.TipoDocumento;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.CorRaca;
import com.softarum.svsa.modelo.enums.EnumUtil;
import com.softarum.svsa.modelo.enums.FormaAcesso;
import com.softarum.svsa.modelo.enums.Genero;
import com.softarum.svsa.modelo.enums.Parentesco;
import com.softarum.svsa.modelo.enums.ProgramaSocial;
import com.softarum.svsa.modelo.enums.Role;
import com.softarum.svsa.modelo.enums.Sexo;
import com.softarum.svsa.modelo.enums.Status;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.modelo.enums.TipoPcD;
import com.softarum.svsa.modelo.enums.Uf;
import com.softarum.svsa.modelo.to.EnderecoTO;
import com.softarum.svsa.modelo.to.MunicipioTO;
import com.softarum.svsa.modelo.to.PerfilEtarioTO;
import com.softarum.svsa.service.HistoricoTransferenciaService;
import com.softarum.svsa.service.HistoricoUVService;
import com.softarum.svsa.service.MPComposicaoService;
import com.softarum.svsa.service.PessoaService;
import com.softarum.svsa.service.rest.BuscaCEPService;
import com.softarum.svsa.service.rest.RestService;
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
@Named(value="mPComposicaoFamiliarBean")
@ViewScoped
public class MPComposicaoFamiliarBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;

	private PessoaReferencia pessoaReferencia;
	private Pessoa novaPessoaReferencia;		
	private List<Pessoa> pessoas;
	private Pessoa pessoa;

	private Pessoa pessoaSelecionada;	
	private ObsComposicaoFamiliar obsComposicaoFamiliar;
	private List<ObsComposicaoFamiliar> observacoes;
	private TipoDocumento tipoDocumento;	
	private PerfilEtarioTO perfilEtarioTO;
	private PieChartModel graficoPerfil;	
	//private List<AtendimentoDTO> resumoAtendimentos = new ArrayList<>();
	private List<Acao> acoes = new ArrayList<>();
	private List<ListaAtendimento> listaFaltas = new ArrayList<>();
	private List<HistoricoTransferencia> listaTransferencias = new ArrayList<>();
	private List<Pais> paises;
	private EnderecoTO enderecoTO;
	
	// Ufs e Municipios IBGE
	private List<Uf> ufs;
	private String nomeMunicipio; 
	private List<MunicipioTO> municipioList;
	private List<MunicipioTO> municipioListEnd;
	
	private Usuario usuarioLogado;
	private boolean administrativo;
	private boolean tipoSCFV = false;
	private boolean unidadeDoUsuario = false;
	private Long prontuarioDestino;	
	private String nomePessoaRef;
	private Uf uf = null;
		
	// Enuns
	private List<Sexo> sexos;
	private List<Genero> generos;
	private List<Status> status;
	private List<TipoPcD> tiposPcD;
	private List<CorRaca> corRacas;
	private List<Parentesco> parentescos;
	private List<FormaAcesso> formasAcesso;
	private List<ProgramaSocial> programasSociais;
	private FormaIngresso formaIngresso;
	
	@Inject
	private MPComposicaoService composicaoService;
	@Inject
	private HistoricoTransferenciaService transferenciaService;
	@Inject
	private LoginBean loginBean;	
	@Inject
	private RestService restService;
	@Inject
	private PessoaService pessoaService;
	@Inject
	private BuscaCEPService buscaCEPService;
	@Inject
	private HistoricoUVService uvService;
	
			
	
	@PostConstruct
	public void inicializar() {

		/*
		 * usuario logado
		 */
		usuarioLogado = loginBean.getUsuario();
		if(usuarioLogado.getRole() == Role.ADMINISTRATIVO 
				|| usuarioLogado.getRole() == Role.CADASTRADOR
				|| usuarioLogado.getRole() == Role.AGENTE_SOCIAL) {
			setAdministrativo(true);
		}
		else if(usuarioLogado.getUnidade().getTipo() == TipoUnidade.SCFV) {
			setAdministrativo(true);
			setTipoSCFV(true);
		}
		else {
			setAdministrativo(false);
		}
		
			
		
		this.ufs = Arrays.asList(Uf.values());		
		this.sexos = Arrays.asList(Sexo.values());
		this.tiposPcD = Arrays.asList(TipoPcD.values());
		this.generos = Arrays.asList(Genero.values());
		this.status = Arrays.asList(Status.values());
		this.setCorRacas(Arrays.asList(CorRaca.values()));
		this.parentescos = EnumUtil.getCodigosParentescoMembro();		
		this.formasAcesso = Arrays.asList(FormaAcesso.values());
		this.programasSociais = Arrays.asList(ProgramaSocial.values());
		this.paises = this.pessoaService.buscarTodosPaises();
				
		graficoPerfil = new PieChartModel();
		
		this.limpar();
	}
	
	
	public void pesquisarMembros() {
		if(this.pessoaReferencia != null) {
			pessoas = composicaoService.buscarTodosMembros(this.pessoaReferencia, loginBean.getTenantId());

			for(Pessoa p: pessoas) {
				if(pessoaService.verificarMSE(p, loginBean.getTenantId())) {
					p.setMse("MSE");
				}
				//log.info("Familia da pessoa: " + p.getFamilia());
			}
		}
		else
			MessageUtil.erro("É necessária a pessoa de referência.");
	}
	
	public void pesquisarObsercacoes() {
		if(this.pessoaReferencia != null)
			observacoes = composicaoService.buscarTodasObservacoes(this.pessoaReferencia.getFamilia().getProntuario(), loginBean.getTenantId());
		else
			MessageUtil.erro("É necessária a pessoa de referência.");
	}
	
	/*
	public void consultarResumoAtendimentos() {
		
		this.resumoAtendimentos = composicaoService.consultarResumoAtendimentos(pessoa, loginBean.getTenantId());
		
	}
	*/
	
	public void trocarPessoaReferencia() {

		try {
			log.info("trocando pessoa referencia...1");
			this.pessoaReferencia = composicaoService.trocarPessoaReferencia(getPessoaReferencia(), getNovaPessoaReferencia());	
			pesquisarMembros(); 
			MessageUtil.sucesso("Pessoa de Referência trocada com sucesso!");
			//limpar();
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
		//return "/restricted/agenda/ManterProntuario.xhtml?faces-redirect=true";
		//return "/agenda/ManterProntuario.xhtml";
	}
	
	
		
	/*
	 * Cadastro de Pessoas membros
	 */
	public void salvarMembro() {
		try {	
			if(!isUnidadeDoUsuario() && !isTipoSCFV())
				throw new NegocioException("Operação inválida! O prontuário não é da sua unidade.");			
			
			// novo membro - se é alteração não seta a familia novamente
			if(pessoa.getCodigo() == null) {
				pessoa.setFamilia(pessoaReferencia.getFamilia());				
			}
			
			pessoa.getFamilia().getEndereco().setMunicipio(pessoa.getFamilia().getEndereco().getMunicipio());
			pessoa.setMunicipioNascimento(pessoa.getMunicipioNascimento());
			
			if(pessoa.getCodigo() == null && isTipoSCFV()) {				
				Pessoa p = this.composicaoService.salvar(pessoa);
				HistPessoaUV hist = new HistPessoaUV();
				hist.setPessoa(p);
				hist.setTenant_id(p.getTenant_id());
				hist.setUnidade(loginBean.getUsuario().getUnidade());
				hist.setUsuario(loginBean.getUsuario());				
				uvService.salvar(hist);				
			}
			else {
				this.composicaoService.salvar(pessoa);
			}
			
			MessageUtil.sucesso("Membro " + pessoa.getNome() + " incluído/alterado com sucesso.");
			pesquisarMembros();
			limpar();			
						
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	/*
	 * Manipulação de membros
	 */
	
	/* Cria novo prontuario para a pessoa selecionada */
	public void criarProntuario() {
		try {
			
			if(!isUnidadeDoUsuario() && !isTipoSCFV())
				throw new NegocioException("Operação inválida! O prontuário não é da sua unidade.");
			
			log.info("criando prontuario novo: " + pessoa.getNome());
			composicaoService.criarProntuario(pessoa, usuarioLogado, loginBean.getTenantId());
			pesquisarMembros();
			pessoa = null;
			
			MessageUtil.sucesso("Prontuário criado com sucesso!");

		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	/* Exclui membro da familia	*/
	public void excluirMembro() {
		try {
			
			if(!isUnidadeDoUsuario() && !isTipoSCFV())
				throw new NegocioException("Operação inválida! O prontuário não é da sua unidade.");
		
			composicaoService.excluirMembro(pessoa);
			pesquisarMembros();
			
			MessageUtil.sucesso("Membro excluído com sucesso!");
		}
		catch (NegocioException e) {
			MessageUtil.erro(e.getMessage());
		}
	}
	
	/* Inativa o membro */
	public void inativarMembro() {
		try {
			
			if(!isUnidadeDoUsuario() && !isTipoSCFV())
				throw new NegocioException("Operação inválida! O prontuário não é da sua unidade.");
			
			composicaoService.inativarMembro(pessoa);
			
			log.info("pessoa INATIVADA: " + pessoa.getNome());
			pesquisarMembros();
			
			pessoa = null;
			
			MessageUtil.sucesso("Membro INTIVADO com sucesso!");

		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	/* Transfere membro para outra família */
	public void transferirMembro() {
		try {
			
			composicaoService.transferirMembro(pessoa, prontuarioDestino);
			
			log.info("pessoa transferida: " + pessoa.getNome());
			pesquisarMembros();
			
			pessoa = null;
			
			MessageUtil.sucesso("Membro transferido com sucesso!");

		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void buscarNomePessoa() {
		
		log.debug("buscar nome ");
		
		if(prontuarioDestino != null) {
			log.info("buscando prontuario digitado = " + prontuarioDestino);
			
			try {
				Prontuario prontuario = composicaoService.buscarProntuario(prontuarioDestino, usuarioLogado.getUnidade(), loginBean.getTenantId());
				
				if(prontuario != null) {
					setNomePessoaRef(prontuario.getCodigo() + " - " + prontuario.getFamilia().getPessoaReferencia().getNome());
				}				
					
			}catch(Exception e) {
				MessageUtil.erro("Prontuário não existe ou não é da sua unidade!");
			}						
		}
		else {
			MessageUtil.alerta("O Prontuário digitado é inválido ou não existe!");
		}	
	}

	/*
	 * Fim manipulação de membros
	 */
	
	/*
	 * Cadastro de Observacoes
	 */
	public void salvarObservacao() {
		try {	
			
			if(!isUnidadeDoUsuario() && !isTipoSCFV())
				throw new NegocioException("Operação inválida! O prontuário não é da sua unidade.");
			
			obsComposicaoFamiliar.setUsuario(usuarioLogado);	
			obsComposicaoFamiliar.setTenant_id(loginBean.getTenantId());
			obsComposicaoFamiliar.setProntuario(pessoaReferencia.getFamilia().getProntuario());
			this.composicaoService.salvarObservacao(obsComposicaoFamiliar);
			MessageUtil.sucesso("Observação incluída com sucesso.");
			pesquisarMembros();
			
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	
	public void limpar() {	
			
			this.pessoa = new Pessoa();	
			this.tipoDocumento = new TipoDocumento();
			this.tipoDocumento.setTenant_id(loginBean.getTenantId());
			this.formaIngresso = new FormaIngresso();
			this.formaIngresso.setTenant_id(loginBean.getTenantId());
			this.pessoa.setTipoDocumento(this.tipoDocumento);
			this.pessoa.setFormaIngresso(this.formaIngresso);
			this.pessoa.setTenant_id(loginBean.getTenantId());
			//Pessoa pessoa = new Pessoa();
			pessoa.setPaisOrigem(pessoaService.buscarPais(76L)); //Brazil
			uf = null;
	}
	
	public void limparObservacao() {
		try {
			if(!isUnidadeDoUsuario() && !isTipoSCFV())
				throw new NegocioException("Operação inválida! O prontuário não é da sua unidade.");
			
			this.obsComposicaoFamiliar = new ObsComposicaoFamiliar();
			this.obsComposicaoFamiliar.setTenant_id(loginBean.getTenantId());

		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void consultaFaltas() {		
		
		setListaFaltas(composicaoService.consultaFaltas(pessoa, loginBean.getTenantId()));		
	}
	
	public void consultaTransferencias() {		
		
		setListaTransferencias(transferenciaService.buscarTodos(pessoaReferencia.getFamilia().getProntuario(), loginBean.getTenantId()));
	}
	
	public boolean isPessoaSelecionada() {			
		//log.info("isPessoaSelecionada()");
        return pessoa != null && pessoa.getCodigo() != null;
    }
	
	public boolean isPessoaReferenciaSelecionada() {		
		//log.info("isPessoaReferenciaSelecionada()");
        return pessoaReferencia != null && pessoaReferencia.getCodigo() != null;
    }	
	
	public void setPessoaReferencia(PessoaReferencia pr) {		
		
		long codigo = pr.getFamilia().getProntuario().getUnidade().getCodigo();
		long codigo2 = usuarioLogado.getUnidade().getCodigo();
		
		if( codigo == codigo2 ) {
			log.info("mesma unidade ");
			setUnidadeDoUsuario(true);
		}
		else {
			setUnidadeDoUsuario(false);
		}			
		
		//log.info("Unidade do usuario = unidade do prontuario?  " + isUnidadeDoUsuario());
				
		this.pessoaReferencia = pr;
		pesquisarMembros();
		//inicializar();
		limpar();
	} 

	/*
	 * Grafico perfil etario
	 */	
	public void initGraficoPerfil() {
		
		log.info("initGraficoPerfil chamado...");
		
		calcularIdades();
		createPieModel();
	}
	
	private void calcularIdades() {		
		
		perfilEtarioTO = new PerfilEtarioTO();
		
		List<Pessoa> pessoasIdades;		
		
		if(this.pessoaReferencia != null) {
			if(this.pessoas != null) {
				pessoasIdades = composicaoService.buscarTodosMembros(this.pessoaReferencia, loginBean.getTenantId());
				log.info("Calculando idades de " + String.valueOf(pessoasIdades.size()));
			}
			else {
				pessoasIdades = this.pessoas;				
			}
		
			int idade = 0;
			for (Pessoa p : pessoasIdades) {
				idade = CalculoUtil.calcularIdade(p.getDataNascimento());
				
				if(idade < 7) {
					perfilEtarioTO.setQde0a6anos(perfilEtarioTO.getQde0a6anos() + 1);
				} else if(idade < 15) {
					perfilEtarioTO.setQde7a14anos(perfilEtarioTO.getQde7a14anos() + 1);
				} else if(idade < 18) {
					perfilEtarioTO.setQde15a17anos(perfilEtarioTO.getQde15a17anos() + 1);
				} else if(idade < 30) {
					perfilEtarioTO.setQde18a29anos(perfilEtarioTO.getQde18a29anos() + 1);
				} else if(idade < 60) {
					perfilEtarioTO.setQde30a59anos(perfilEtarioTO.getQde30a59anos() + 1);
				} else if(idade < 65) {
					perfilEtarioTO.setQde60a64anos(perfilEtarioTO.getQde60a64anos() + 1);	
				} else if(idade < 70) {
					perfilEtarioTO.setQde65a69anos(perfilEtarioTO.getQde65a69anos() + 1);
				} else if(idade < 29) {
					perfilEtarioTO.setQde18a29anos(perfilEtarioTO.getQde18a29anos() + 1);	
				} else {
					perfilEtarioTO.setMais70anos(perfilEtarioTO.getMais70anos() + 1);
				}
			}			
		}		
		else
			MessageUtil.erro("É necessária a pessoa de Referência.");
	}
	
	private void createPieModel() {
		
		//log.info("Criando grafico");
		
        graficoPerfil = new PieChartModel();     
        
		
        graficoPerfil.set("0 a 6 ("+perfilEtarioTO.getQde0a6anos()+")", perfilEtarioTO.getQde0a6anos());
        graficoPerfil.set("7 a 14 ("+perfilEtarioTO.getQde7a14anos()+")", perfilEtarioTO.getQde7a14anos());
        graficoPerfil.set("15 a 17 ("+perfilEtarioTO.getQde15a17anos()+")", perfilEtarioTO.getQde15a17anos());
        graficoPerfil.set("18 a 29 ("+perfilEtarioTO.getQde18a29anos()+")", perfilEtarioTO.getQde18a29anos());
        graficoPerfil.set("30 a 59 ("+perfilEtarioTO.getQde30a59anos()+")", perfilEtarioTO.getQde30a59anos());
        graficoPerfil.set("60 a 64 ("+perfilEtarioTO.getQde60a64anos()+")", perfilEtarioTO.getQde60a64anos());
        graficoPerfil.set("65 a 69 ("+perfilEtarioTO.getQde65a69anos()+")", perfilEtarioTO.getQde65a69anos());
        graficoPerfil.set("+ 70 ("+perfilEtarioTO.getMais70anos()+")", perfilEtarioTO.getMais70anos());
        
        graficoPerfil.setLegendPosition("e");
        
    }	

	
	/*
	public void listarMunicipiosNasc() throws Exception {
		
		try {
			log.info(pessoa.getUfNascimento());
			log.info(uf);
			municipioList = restService.listarMunicipios(uf.name());
		}
		catch(Exception e){
			MessageUtil.sucesso("Problema na recuperação dos municípios.");
		}
	}
	
	
		
						
					<p:outputLabel value="UF de Nascimento" for="ufNasc" />
					<h:panelGroup>
						<p:selectOneMenu id="ufNasc" 
							value="#{manterProntuarioBean.mpComposicaoBean.pessoa.ufNascimento}">
							<f:selectItem itemLabel="Selecione a Uf" />
							<f:selectItems value="#{manterProntuarioBean.mpComposicaoBean.ufs}" 
								var="ufNa" 
								itemLabel="#{ufNa}" itemValue="#{ufNa}"/>							
							<p:ajax listener="#{manterProntuarioBean.mpComposicaoBean.listarMunicipiosNasc}" 
								update="municipioNasc" event="change" process="ufNasc municipioNasc"/>
						</p:selectOneMenu>
						<p:spacer width="10px" />						
					
						<p:outputLabel for="municipioNasc" value="Município de Nascimento" />
						<p:selectOneMenu id="municipioNasc" value="#{manterProntuarioBean.mpComposicaoBean.pessoa.municipioNascimento}">
							<f:selectItem itemLabel="Selecione o Município" />
							<f:selectItems value="#{manterProntuarioBean.mpComposicaoBean.municipioList}" var="municipioNa" 
								itemLabel="#{municipioNa.nome}" itemValue="#{municipioNa.nome}"/>
						</p:selectOneMenu>
					</h:panelGroup>
					
					
					
	public void listarMunicipiosEnd() throws Exception {
		
		try {
			log.info(pessoa.getFamilia().getEndereco().getUf());
			log.info(uf);
			municipioListEnd = restService.listarMunicipios(uf.name());
		}
		catch(Exception e){
			MessageUtil.sucesso("Problema na recuperação dos municípios.");
		}
	}
	
	
	
	<p:outputLabel value="Novo Município" for="uf" />
						<h:panelGroup>
							<p:selectOneMenu id="uf" 
								value="manterProntuarioBean.mpComposicaoBean.pessoa.familia.endereco.uf"
								disabled="#{manterProntuarioBean.mpComposicaoBean.pessoa.codigo ne 
											manterProntuarioBean.mpComposicaoBean.pessoaReferencia.codigo}">
								<f:selectItem itemLabel="Selecione a Uf" />
								<f:selectItems value="#{manterProntuarioBean.mpComposicaoBean.ufs}" 
									var="uf" 
									itemLabel="#{uf}" itemValue="#{uf}"/>							
								<p:ajax listener="#{manterProntuarioBean.mpComposicaoBean.listarMunicipiosEnd}" 
									update="municipio" event="change" process="uf municipio"/>
							</p:selectOneMenu>
							<p:spacer width="10px" />
						
							<p:outputLabel for="municipio" value="Município" />								
							<p:selectOneMenu id="municipio" value="#{manterProntuarioBean.mpComposicaoBean.pessoa.familia.endereco.municipio}"
								disabled="#{manterProntuarioBean.mpComposicaoBean.pessoa.codigo ne 
											manterProntuarioBean.mpComposicaoBean.pessoaReferencia.codigo}">
								<f:selectItem itemLabel="Selecione o Município" />
								<f:selectItems value="#{manterProntuarioBean.mpComposicaoBean.municipioList}" var="municipio" 
									itemLabel="#{municipio.nome}" itemValue="#{municipio.nome}"/>
							</p:selectOneMenu>
						</h:panelGroup>			
	
	*/
	
	public void buscaEnderecoPorCEP() {
			
	        try {
				enderecoTO  = buscaCEPService.buscaEnderecoPorCEP(pessoa.getFamilia().getEndereco().getCep());
				
				/*
		         * Preenche o Endereco do prontuario com os dados buscados
		         */	 
				
				pessoa.getFamilia().getEndereco().setEndereco(enderecoTO.getTipoLogradouro().
		        		                concat(" ").concat(enderecoTO.getLogradouro()));
				pessoa.getFamilia().getEndereco().setNumero(null);
				pessoa.getFamilia().getEndereco().setBairro(enderecoTO.getBairro());
				pessoa.getFamilia().getEndereco().setMunicipio(enderecoTO.getCidade());
				pessoa.getFamilia().getEndereco().setUf(enderecoTO.getEstado());
		        
		        if (enderecoTO.getResultado() != 1) {
		        	MessageUtil.erro("Endereço não encontrado para o CEP fornecido.");
		        }
			} catch (NegocioException e) {
				e.printStackTrace();
				MessageUtil.erro(e.getMessage());		            
			}       
		}

}