package com.softarum.svsa.controller.autocad;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FlowEvent;

import com.softarum.svsa.modelo.Endereco;
import com.softarum.svsa.modelo.Tenant;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.UserTemp;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.UsuarioTemp;
import com.softarum.svsa.modelo.enums.Grupo;
import com.softarum.svsa.modelo.enums.Role;
import com.softarum.svsa.modelo.enums.Status;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.modelo.enums.Uf;
import com.softarum.svsa.modelo.to.AutoCadSecTO;
import com.softarum.svsa.modelo.to.EnderecoTO;
import com.softarum.svsa.service.AutoCadSecService;
import com.softarum.svsa.service.ThemeService;
import com.softarum.svsa.service.UsuarioService;
import com.softarum.svsa.service.ThemeService.Theme;
import com.softarum.svsa.service.rest.BuscaCEPService;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * @author isadora
 *
 */
@Log4j
@Getter
@Setter
@Named
@SessionScoped
public class AutoCadSecBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private AutoCadSecTO autocadTO;
	private EnderecoTO enderecoTO;
	private UsuarioTemp usuarioTemp;
	private Usuario usuario;
	private UsuarioService usuarioService;
	private UserTemp userTemp = null;


	@Inject
	private ThemeService themeService;

	private Theme theme;

	@Inject
	private AutoCadSecService autocadService;

	@Inject
	private BuscaCEPService buscaCEPService;

	private List<Uf> ufs;
	private boolean skip;

	@PostConstruct
	public void inicializar(){
		usuario = new Usuario();
		this.limpar();
		this.ufs = Arrays.asList(Uf.values());

	}

	/*private UserTemp autenticaCadSec() throws NegocioException {
		log.info("autenticando");
		//userTemp = autocadService.buscarPeloEmail(email);
		userTemp = autocadService.buscarPeloEmail(email);
		log.info("email:" + autocadTO.getUserTemp().getEmail());

		if (userTemp != null) {

			if (userTemp.getStatus() == Status.INATIVO) {
				throw new NegocioException("Usuario INATIVO!");
			} else {
				String pwDigitada = senha;
			    autocadTO.getUserTemp().setSenha(pwDigitada);
				log.info("senha: " + autocadTO.getUserTemp().getSenha());

				if (pwDigitada.compareTo(this.pwRecuperada) != 0) {
					throw new NegocioException("Senha inválida!");
				}

			}
		} else {
			throw new NegocioException("E-mail inválido!");
		}

		return userTemp;
		
	}*/

	/*public String entrar() throws NegocioException {
		
		log.info("entrar ");
		
	HttpSession session = getSession(); // creates new empty session
		
		userTemp = (UserTemp)session.getAttribute("usertemp");
		
		
		if (userTemp == null) {
			
			userTemp = autenticaCadSec();

			if (userTemp != null) {
					return "/autocad/AutoCadSec.xhtml";
					

			} else {
				return "/restricted/home/SvsaHome.xhtml";
			}
		}
		return null;
		
		
		try {
			
			HttpSession session = getSession(); // creates new empty session
			
			userTemp = (UserTemp)session.getAttribute("usertemp");
			
			if(userTemp == null) {
				
				userTemp = autenticaCadSec();
				
				if (userTemp != null) {
					
					session.setAttribute("usuario", usuario);
					return "/autocad/AutoCadSec.xhtml";
					
				
				} else {
					return "INVALIDO";	
				}
			}
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
		
		return null;

	}*/

	public void salvarTenant() {

		try {

			Tenant secretaria = this.autocadService.salvarTenant(autocadTO.getSecretaria());
			autocadTO.setSecretaria(secretaria);

		} catch (NegocioException e) {
			e.printStackTrace();

		}

	}

	public void salvarUnidade() {

		try {

			autocadTO.getUnidade().setTenant_id(autocadTO.getSecretaria().getCodigo());
			autocadTO.getUnidade().setTipo(TipoUnidade.CRAS);
			Unidade unidade = this.autocadService.salvarUnidade(autocadTO.getUnidade());
			autocadTO.setUnidade(unidade);

		} catch (NegocioException e) {
			e.printStackTrace();
		}

	}

	public void salvarUsuario() {

		try {

			autocadTO.getUsuario().setUnidade(autocadTO.getUnidade());
			autocadTO.getUsuario().setTenant(autocadTO.getSecretaria());
			autocadTO.getUsuario().setRole(Role.ASSISTENTE_SOCIAL);
			autocadTO.getUsuario().setGrupo(Grupo.COORDENADORES);
			Usuario usuario = this.autocadService.salvarUsuario(autocadTO.getUsuario());
			autocadTO.setUsuario(usuario);

		} catch (NegocioException e) {
			e.printStackTrace();

		}

	}

	public void limpar() {
		this.autocadTO = new AutoCadSecTO();

		autocadTO.setSecretaria(new Tenant());

		autocadTO.setUnidade(new Unidade());
		autocadTO.getUnidade().setEndereco(new Endereco());

		// TODO setar o usuario recebido
	//	 usuarioTemp = new UsuarioTemp();
		 
		
	//	 HttpSession session = getSession(); // creates new empty session
	//	 usuarioTemp = (UsuarioTemp)session.getAttribute("usuariotemp");
		 
		 autocadTO.setUsuario(new Usuario());
	//	 autocadTO.getSecretaria().setSecretaria(usuarioTemp.getEmail());
	//	 autocadTO.getSecretaria().setTenant(usuarioTemp.getNome());
		 
		 autocadTO.setUserTemp(new UserTemp());
		 
		 
	}

	/*
	 * public String confirmar() { ExternalContext ec =
	 * FacesContext.getCurrentInstance().getExternalContext(); try { this.limpar();
	 * ec.redirect(ec.getRequestContextPath() + "/restricted/home/SvsaHome.xhtml");
	 * //FacesContext.getCurrentInstance().getExternalContext().redirect(
	 * "/svsafree/autocad/AutoCadSec.xhtml"); log.info("Retorno?" + ec);
	 * 
	 * } catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } return "/restricted/home/SvsaHome.xhtml";
	 * 
	 * }
	 */

	public String onFlowProcess(FlowEvent event) {

		if (event.getOldStep().equals("secretaria")) {
			log.info(event.getOldStep());
			this.salvarTenant();
			MessageUtil.sucesso("Secretaria salva com sucesso!");

		} else if (event.getOldStep().equals("unidade")) {
			this.salvarUnidade();
			MessageUtil.sucesso("Unidade salva com sucesso!");
		}

		else {
			this.salvarUsuario();
			MessageUtil.sucesso("Usuário salvo com sucesso!");
		}

		return event.getNewStep();
	}

	public void buscaEnderecoPorCEP() {

		try {
			enderecoTO = buscaCEPService.buscaEnderecoPorCEP(autocadTO.getUnidade().getEndereco().getCep());

			/*
			 * Preenche o Endereco com os dados buscados
			 */
			// unidade.getEndereco().setCep(cep);
			autocadTO.getUnidade().getEndereco()
					.setEndereco(enderecoTO.getTipoLogradouro().concat(" ").concat(enderecoTO.getLogradouro()));
			autocadTO.getUnidade().getEndereco().setUf(enderecoTO.getEstado());

			if (enderecoTO.getResultado() != 1) {
				MessageUtil.erro("Endereço não encontrado para o CEP fornecido.");
			}
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}

	/*private HttpSession getSession() {

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession session = request.getSession();

		return session;
	}*/

}