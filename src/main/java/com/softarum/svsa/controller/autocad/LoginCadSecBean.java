package com.softarum.svsa.controller.autocad;

import java.io.Serializable;
import java.time.LocalDateTime;
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
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.xalan.xsltc.compiler.util.ErrorMessages_es;
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
public class LoginCadSecBean implements Serializable {

	private static final long serialVersionUID = 1L;


	private AutoCadSecService autocadService;
	private UsuarioTemp usuarioTemp;
	private UsuarioService usuarioService;
	private UserTemp userTemp = null;

	private Long id=(long) 1;
	private String email;
	private String senha;
	private String pwRecuperada;

	@PostConstruct
	public void inicializar(){
		this.limpar();
		log.info("LoginCadSecBean inicializando...");
		

	}

	private UserTemp autenticaCadSec() throws NegocioException {
		log.info("autenticando " );
		userTemp = autocadService.buscarPeloEmail(email);

		if (userTemp != null) {
			
			if (userTemp.getStatus() == Status.INATIVO) {
				throw new NegocioException("Usuario INATIVO!");
			} else {
				String pwDigitada = senha;					
				String pwRecuperada = userTemp.getSenha();
				
		//		log.info("senha: " + autocadTO.getUserTemp().getSenha());

				if(!BCrypt.checkpw(pwDigitada, pwRecuperada)) {						
					throw new NegocioException("Senha inválida!");
				}
				
				if(userTemp != null) {
					if (userTemp.getEmail() != email) {
						throw new NegocioException("Email invalido !");
			
					}else {
						return userTemp;
					}

				}

			}
		} else {
			throw new NegocioException("E-mail inválido!");
		}

		return userTemp;
		
	}

	public String entrar() throws NegocioException {
		
		log.info("entrar ");
		
		
		try {
			
			HttpSession session = getSession(); // creates new empty session
			
			userTemp = (UserTemp)session.getAttribute("usertemp");
			
			if(userTemp == null) {
				
				userTemp = autenticaCadSec();
				
				if (userTemp != null) {
					//userTemp.setEmail(email);
					//userTemp.setSenha(senha);
					
					if(userTemp.getEmail() == email) {
						session.setAttribute("userTemp", userTemp);
						return "/autocad/restricted/AutoCadSec.xhtml";
					}
					
						
					
					
				} else {
					MessageUtil.erro("Não exite!!");
					return "/autocad/form_registro.xhtml";	
				}
			}
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
		
		return null;

	}
	
	


	
	public void limpar() {
		userTemp = new UserTemp();
		
	
	}
	

	private HttpSession getSession() {

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession session = request.getSession();

		return session;
	}
	
	
	public void setUserTemp(UserTemp userTemp) {
		this.userTemp = userTemp;
		
	}

}