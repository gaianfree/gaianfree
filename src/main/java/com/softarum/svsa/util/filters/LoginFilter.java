package com.softarum.svsa.util.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.softarum.svsa.modelo.Usuario;

@WebFilter(urlPatterns = "/restricted/*", servletNames = "{Faces Servlet}")
public class LoginFilter extends AbstractFilter implements Filter{
	


	public void init(FilterConfig arg0) throws ServletException {}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

				
		HttpServletRequest req = (HttpServletRequest) request;
		@SuppressWarnings("unused")
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		
		Usuario user = (Usuario) session.getAttribute("usuario");
		
		
		if (session.isNew() || user == null) {
			doLogin(request, response, req);
		} else {
			chain.doFilter(request, response);
		}
	}	
	
	public void destroy() {}
}
