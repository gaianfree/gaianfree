package com.softarum.svsa.controller.rede;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
//import javax.faces.bean.ViewScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.controller.pront.MPComposicaoFamiliarBean;
import com.softarum.svsa.modelo.Endereco;
import com.softarum.svsa.modelo.Familia;
import com.softarum.svsa.modelo.FormaIngresso;
import com.softarum.svsa.modelo.HistProntuarioUV;
import com.softarum.svsa.modelo.Pais;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.TipoDocumento;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.CorRaca;
import com.softarum.svsa.modelo.enums.Genero;
import com.softarum.svsa.modelo.enums.Parentesco;
import com.softarum.svsa.modelo.enums.Sexo;
import com.softarum.svsa.modelo.enums.Status;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.modelo.enums.Uf;
import com.softarum.svsa.modelo.to.EnderecoTO;
import com.softarum.svsa.modelo.to.MunicipioTO;
import com.softarum.svsa.service.CapaProntuarioService;
import com.softarum.svsa.service.HistoricoUVService;
import com.softarum.svsa.service.MPComposicaoService;
import com.softarum.svsa.service.PessoaService;
import com.softarum.svsa.service.UnidadeService;
import com.softarum.svsa.service.pdf.ProntuarioPDFService;
import com.softarum.svsa.service.rest.BuscaCEPService;
import com.softarum.svsa.service.rest.RestService;
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
@Named(value="cadastroProntRedeBean")
@ViewScoped
public class CadastroCapaProntRedeBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Prontuario prontuario;
	private PessoaReferencia pessoaReferencia;
	private String nrProntuario;
	//private List<Unidade> unidades;
	private List<Sexo> sexos;
	private List<Genero> generos;
	private List<CorRaca> corRacas;
	private List<Uf> ufs;
	private List<MunicipioTO> municipioList;
	private List<Status> status;
	private EnderecoTO enderecoTO;
	private Unidade unidade;
	private List<Pais> paises;
	@SuppressWarnings("unused")
	private boolean edicao = false;
	private boolean composicao = false;
	
	private Long prontuarioVinculado;
	private Prontuario prontuarioCras;
	private String nomePessoaRef;
	
	
	@Inject
	private CapaProntuarioService cadastroProntuarioService;	
	@Inject
	private BuscaCEPService buscaCEPService;	
	@Inject
	private UnidadeService unidadeService;	
	@Inject
	private MPComposicaoService composicaoService;
	@Inject
	private PessoaService pessoaService;
	@Inject
	private ProntuarioPDFService pdfService;
	@Inject
	private RestService restService;
	@Inject
	private HistoricoUVService historicoUVService;
	
	@Inject
	private MPComposicaoFamiliarBean mpComposicaoBean;
	
	
	@Inject
	private LoginBean loginBean;
	
		
	@PostConstruct
	public void inicializar() {
		
		this.limpar();		
		//this.unidades = this.unidadeService.buscarTodos(loginBean.getTenantId());
		this.paises = this.pessoaService.buscarTodosPaises();
		this.sexos = Arrays.asList(Sexo.values());
		this.generos = Arrays.asList(Genero.values());
		this.corRacas = Arrays.asList(CorRaca.values());
		this.ufs = Arrays.asList(Uf.values());		
		this.status = Arrays.asList(Status.values());
		this.unidade = loginBean.getUsuario().getUnidade().getUnidadeVinculada();		
		
	}
	
	public List<String> completeText(String query) {
		
		List<String> results = new ArrayList<String>(); 
		
		try {	        
	        this.composicaoService.validarCadastro(query, loginBean.getTenantId());
	        //results = composicaoService.pesquisarNomesPR(query); 
	        return results;
		} catch (NegocioException e) {
			log.error(e.getMessage());
			MessageUtil.alerta(e.getMessage());
		}
        return results;
    }
	
	
	public void salvar() {
		try {
			//log.debug("Prontuario a ser gerado: " + getNrProntuario());
			
			prontuario.setUnidade(unidade);
			prontuario.getFamilia().setProntuario(prontuario);
			//prontuario.getFamilia().getPessoaReferencia().setParentescoPessoaReferencia(Parentesco.PESSOA_REFERENCIA);
			prontuario.getFamilia().getPessoaReferencia().setFamilia(prontuario.getFamilia());
			if(prontuario.getFamilia().getPessoaReferencia().getTipoDocumento() == null)
				prontuario.getFamilia().getPessoaReferencia().setTipoDocumento(new TipoDocumento());
			if(prontuario.getFamilia().getPessoaReferencia().getFormaIngresso() == null)
				prontuario.getFamilia().getPessoaReferencia().setFormaIngresso(new FormaIngresso());
			prontuario.getFamilia().getPessoaReferencia().setDataRegistroComposicaoFamiliar(Calendar.getInstance());
			
			// registra o nome e email de quem criou o prontuário
			String  criador = loginBean.getUsuario().getNome().concat(":").concat(loginBean.getEmail());
			prontuario.setCriador(criador);			
			
			log.info("Prontuario.cofigo = " + prontuario.getCodigo());	
			// registra histórico apenas para criação
			if(prontuario.getCodigo() == null) {
				Prontuario p = this.cadastroProntuarioService.salvar(prontuario);
				log.info("Prontuario novo: ");				
				HistProntuarioUV hist = new HistProntuarioUV();
				hist.setProntuario(p);
				hist.setTenant_id(p.getTenant_id());
				hist.setUnidade(loginBean.getUsuario().getUnidade());
				hist.setUsuario(loginBean.getUsuario());
				historicoUVService.salvar(hist);
			}
			else {
				this.cadastroProntuarioService.salvar(prontuario);
			}				
			
			log.info("Prontuario criado por: " + criador);
			
			MessageUtil.sucesso("Prontuário salvo com sucesso!");
			this.limpar();
			
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}		
	}
	
	
	public void limpar() {
		this.prontuario = new Prontuario();	
		this.prontuario.setTenant_id(loginBean.getTenantId());
		this.prontuario.setStatus(Status.ATIVO);
		this.prontuario.setProntuario(getNrProntuario());
		this.prontuario.setFamilia(new Familia());
		this.prontuario.getFamilia().setTenant_id(loginBean.getTenantId());
		PessoaReferencia pr = new PessoaReferencia();
		pr.setParentescoPessoaReferencia(Parentesco.RESPONSAVEL_FAMILIAR);
		pr.setTenant_id(loginBean.getTenantId());
		pr.setPaisOrigem(pessoaService.buscarPais(76L)); //Brazil
		this.prontuario.getFamilia().setPessoaReferencia(pr);		
		this.prontuario.getFamilia().setEndereco(new Endereco());
		this.prontuario.getFamilia().getEndereco().setTenant_id(loginBean.getTenantId());
		setEdicao(false);
		setComposicao(false);
		setProntuarioVinculado(null);
		setNomePessoaRef(null);
	}
	
	public void listarMunicipiosEnd() throws Exception {
		
		try {
			setMunicipioList(restService.listarMunicipios(prontuario.getFamilia().getEndereco().getUf()));
		}
		catch(Exception e){
			MessageUtil.sucesso("Problema na recuperação dos municípios.");
		}
	}

	public void buscaEnderecoPorCEP() {
		
        try {
			enderecoTO  = buscaCEPService.buscaEnderecoPorCEP(prontuario.getFamilia().getEndereco().getCep());
			
			/*
	         * Preenche o Endereco do prontuario com os dados buscados
	         */	 
			
	        prontuario.getFamilia().getEndereco().setEndereco(enderecoTO.getTipoLogradouro().
	        		                concat(" ").concat(enderecoTO.getLogradouro()));
	        prontuario.getFamilia().getEndereco().setNumero(null);
	        prontuario.getFamilia().getEndereco().setBairro(enderecoTO.getBairro());
	        prontuario.getFamilia().getEndereco().setMunicipio(enderecoTO.getCidade());
	        prontuario.getFamilia().getEndereco().setUf(enderecoTO.getEstado());
	        
	        if (enderecoTO.getResultado() != 1) {
	        	MessageUtil.erro("Endereço não encontrado para o CEP fornecido.");
	        }
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());		            
		}       
	}
	
	public void buscarNomePessoa() {
			
		log.debug("Capa Prontuario creas = " + prontuario.getCodigo());
		
		if(prontuarioVinculado != null) {
			log.info("Capa Prontuario buscando prontuario digitado = " + prontuarioVinculado);
			
			try {
				setProntuarioCras(cadastroProntuarioService.buscarProntuarioCRAS(prontuarioVinculado, loginBean.getTenantId()));
				if(prontuarioCras != null) {
					log.info("Capa Prontuario cras digitado = " + prontuarioCras.getCodigo());
					if(prontuarioCras.getProntuarioVinculado() == null) {	
						setNomePessoaRef(prontuarioCras.getCodigo() + "-" + prontuarioCras.getFamilia().getPessoaReferencia().getNome() + " [" + prontuarioCras.getUnidade().getNome() + "]");
					}
					else {
						MessageUtil.alerta("Esse prontuário já está vinculado a outro prontuário CREAS");
					}						
				}
			}catch(Exception e) {
				MessageUtil.erro("Prontuário não existe ou não é de CRAS!");
			}						
		}
		else {
			MessageUtil.alerta("O Prontuário digitado é inválido ou não existe!");
		}	
	}
	
	/* Busca prontuario CRAS para vincular ao prontuario CREAS - Só para CREAS) */
	public void vincularProntuario() {		
		
		/* Para prontuario novo
		 * Verificar o vinculo é para prontuario novo, o que está sendo criado  
		 */
		if(prontuario.getCodigo() == null) {
			if(prontuarioCras != null) {
				if(prontuarioCras.getProntuarioVinculado() == null) {
					prontuarioCras.setProntuarioVinculado(prontuario);
					prontuario.setProntuarioVinculado(prontuarioCras);
				}				
			}
			else {
				MessageUtil.alerta("Selecione um prontuário a ser vinculado!");
			}
		}
		else {			
			
			// Verifica se o prontuário ainda não tem vínculo
			log.info("vinculando... " + prontuarioVinculado);
			if(prontuario.getProntuarioVinculado() == null) {				
				if(prontuarioVinculado != null) {
					log.info("ProntuarioVinculado  = " + prontuarioVinculado);
					
					try {
						//prontuarioCras = cadastroProntuarioService.buscarProntuarioCRAS(prontuarioVinculado);
						if(prontuarioCras != null) {
							log.info("Capa Prontuario cras digitado = " + prontuarioCras.getCodigo());
							if(prontuarioCras.getProntuarioVinculado() == null) {	
								setNomePessoaRef(prontuarioCras.getFamilia().getPessoaReferencia().getNome());
								// vinculo de volta CRAS->CREAS
								prontuarioCras.setProntuarioVinculado(prontuario);
								// vinculo de ida CREAS -> CRAS
								prontuario.setProntuarioVinculado(prontuarioCras);
								
								cadastroProntuarioService.salvar(prontuario);
							}
							else {
								MessageUtil.alerta("Esse prontuário já está vinculado a outro prontuário CREAS");
							}						
						}
					}catch(Exception e) {
						MessageUtil.erro("Prontuário não existe ou não é de CRAS!");
					}						
				}
				else {
					MessageUtil.alerta("O Prontuário digitado é inválido ou não existe!");
				}
			}
			else {
				log.info("Capa Prontuario substituindo o vinculo existente do prontuario creas = " + prontuario.getProntuarioVinculado().getCodigo());
				
				if(prontuarioVinculado != null) {
					log.info("Capa Prontuario buscando prontuario digitado = " + prontuarioVinculado);
					
					try {
						//prontuarioCras = cadastroProntuarioService.buscarProntuarioCRAS(prontuarioVinculado);
						if(prontuarioCras != null) {
							
							if(prontuarioCras.getProntuarioVinculado() == null) {
								log.info("Capa Prontuario prontuario cras = " + prontuarioCras.getProntuarioVinculado());
								setNomePessoaRef(prontuarioCras.getFamilia().getPessoaReferencia().getNome());
								
								Long codigoSubstituido = prontuario.getProntuarioVinculado().getCodigo();
								// substitui o vinculo
								prontuarioCras.setProntuarioVinculado(prontuario);	// cras aponta para creas						
								prontuario.setProntuarioVinculado(prontuarioCras);  // creas aponta para cras
								cadastroProntuarioService.salvar(prontuario);
								
								// limpa o vinculo do substituido							
								Prontuario prontuarioCrasSubstituido = cadastroProntuarioService.buscarProntuarioCRAS(codigoSubstituido, loginBean.getTenantId());
								log.info("codigo " + codigoSubstituido + "substituido por null" );
								prontuarioCrasSubstituido.setProntuarioVinculado(null);
								cadastroProntuarioService.salvar(prontuarioCrasSubstituido);
								
							}
							else {
								MessageUtil.alerta("Esse prontuário já está vinculado a outro prontuário CREAS");
							}						
						}
					}catch(Exception e) {
						MessageUtil.erro("Prontuário não existe ou não é de CRAS!");
					}						
				}
				else {
					MessageUtil.alerta("O Prontuário digitado é inválido!");
				}
			}
		}
	}
	
	
	/* 
	 * 
	 * Impressão da capa do prontuário 
	 * 
	 * 
	 * */
	
	public void showPDF() {
		
		FacesContext context = FacesContext.getCurrentInstance(); 
	    HttpServletResponse response = (HttpServletResponse)context.getExternalContext().getResponse();  
	    response.setContentType("application/pdf");    
	    response.setHeader("Content-disposition",  "inline=filename=file.pdf");
	    
	    try {	        
	    	// Creating a PdfWriter 
	        ByteArrayOutputStream baos = pdfService.generateStream(prontuario, loginBean.getUsuario().getTenant().getS3Key());	        

	        // setting some response headers
	        response.setHeader("Expires", "0");
	        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
	        response.setHeader("Pragma", "public");
	        // setting the content type
	        response.setContentType("application/pdf");
	        // the contentlength
	        response.setContentLength(baos.size());
	        // write ByteArrayOutputStream to the ServletOutputStream
	        ServletOutputStream os = response.getOutputStream();
	        
	        baos.writeTo(os);
	        os.flush();
	        os.close();
	    }
	    catch (IOException e) {
	    	log.error("error: "+e);
	    	MessageUtil.erro("Problema na escrita do PDF.");
	    }
	    catch (Exception ex) {
	    	log.error("error: " + ex.getMessage());
	    	MessageUtil.erro("Problema na geração do PDF.");
	    }
	    context.responseComplete();
	    log.info("PDF gerado!");
	}	
	
	public boolean isPessoaReferenciaSelecionada() {

    	if(getPessoaReferencia() != null && getPessoaReferencia().getCodigo() != null) {    		
    		return true;
    	}
        return false;
        		
    }	
	
	public void setPessoaReferencia() {

    	if(getProntuario() != null && getProntuario().getCodigo() != null) {
    		setPessoaReferencia(getProntuario().getFamilia().getPessoaReferencia());
    		mpComposicaoBean.setPessoaReferencia(getPessoaReferencia());
    		setComposicao(true);
    	}        		
    }

	public boolean isEdicao() {
		
		if(prontuario.getFamilia() != null && prontuario.getFamilia().getCodigo() != null) {
			return true;
		}
		return false;
	}
	
	public boolean isCreas() {
		if(loginBean.getUsuario().getUnidade().getTipo() == TipoUnidade.CREAS)
			return true;
		return false;
	}
	
}
