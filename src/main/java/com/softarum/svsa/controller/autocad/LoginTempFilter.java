package com.softarum.svsa.controller.autocad;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.softarum.svsa.modelo.UserTemp;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.util.filters.AbstractFilter;

@WebFilter(urlPatterns = "/autocad/restricted/*", servletNames = "{Faces Servlet}")
public class LoginTempFilter extends AbstractFilter implements Filter{
	public void doLogin(ServletRequest request, ServletResponse response, HttpServletRequest req)
			throws IOException, ServletException {

		RequestDispatcher dispatcher = req.getRequestDispatcher("/autocad/restricted/LoginCadSec.xhtml");
		dispatcher.forward(request,  response);
	}
	
	public void acessoNegado(ServletRequest request, ServletResponse response, HttpServletRequest req) 
			throws IOException, ServletException {

		RequestDispatcher dispatcher = req.getRequestDispatcher("acessoNegado");
		dispatcher.forward(request,  response);
	}



	public void init(FilterConfig arg0) throws ServletException {}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

				
		HttpServletRequest req = (HttpServletRequest) request;
		@SuppressWarnings("unused")
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		
		UserTemp user = (UserTemp) session.getAttribute("userTemp");
		
		
		if (session.isNew() || user == null) {
			doLogin(request, response, req);
		} else {
			chain.doFilter(request, response);
		}
	}	
	
	public void destroy() {}
}
