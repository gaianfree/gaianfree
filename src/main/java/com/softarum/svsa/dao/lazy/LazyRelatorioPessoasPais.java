package com.softarum.svsa.dao.lazy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.softarum.svsa.dao.PessoaDAO;
import com.softarum.svsa.modelo.Pais;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.service.PessoaService;

/**
 * @author Talita
 *
 */
public class LazyRelatorioPessoasPais extends LazyDataModel<Pessoa> implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(LazyRelatorioPessoasPais.class);
	
	List<Pessoa> pessoas = new ArrayList<Pessoa>();
	private PessoaDAO pessoaDAO;
	private Long tenantId;
	private Unidade unidade = null;
	private Pais pais;
	
	public LazyRelatorioPessoasPais(PessoaService pessoaService, Unidade unidade, Pais pais, Long tenantId) {
		this.pessoaDAO = pessoaService.getPessoaDAO();
		this.tenantId = tenantId;
		this.unidade = unidade;
		this.pais = pais;
	}
	
	@Override
	public List<Pessoa> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filters) {	
		
		
		String filtroNome = "";
		String filtroMae = "";
		String filtroCodigoProntuario = "";
		String filtroCodigoPessoa = "";
		String filtroFisico = "";
		
			
		for (Map.Entry<String, FilterMeta> entrada : filters.entrySet()) { 
            try {
            	FilterMeta meta = entrada.getValue();                 
               
                if(meta.getField().equals("nome")) { //1
                	filtroNome = (String)meta.getFilterValue();
                	log.debug("property : " + meta.getField() + "-" + (String)meta.getFilterValue());
                }                    	
                else if(meta.getField().equals("nomeMae")) { //3
                	filtroMae = (String)meta.getFilterValue();
                	log.debug("property : " + meta.getField() + "-" + (String)meta.getFilterValue());
                }                		
                else if(meta.getField().equals("familia.prontuario.codigo")) { //5              	
                	filtroCodigoProntuario = (String)meta.getFilterValue();
                	log.debug("property : " + meta.getField() + "-" + (String)meta.getFilterValue());
                }                   	
                else if(meta.getField().equals("familia.prontuario.prontuario")) { //6               	
                	filtroFisico = (String)meta.getFilterValue();
                	log.debug("property : " + meta.getField() + "-" + (String)meta.getFilterValue());
                }
                else if(meta.getField().equals("codigo")) { 	//7               	
                	filtroCodigoPessoa = (String)meta.getFilterValue();
                	log.debug("property : " + meta.getField() + "-" + (String)meta.getFilterValue());
                }
		
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

		pessoas = new ArrayList<Pessoa>();
		int dataSize = 0;
		
		if(filtroNome != null && !filtroNome.equals("")) {	
			if(unidade!=null){
				pessoas = this.pessoaDAO.buscarComPaginacao(first, pageSize, filtroNome, 1, unidade, pais, tenantId);
				dataSize = this.pessoaDAO.encontrarQdePessoas(filtroNome, 1, unidade, pais, tenantId).intValue();
			}
			else {
				pessoas = this.pessoaDAO.buscarComPaginacao(first, pageSize, filtroNome, 1, pais, tenantId);
				dataSize = this.pessoaDAO.encontrarQdePessoas(filtroNome, 1, pais, tenantId).intValue();
			}
			this.setRowCount(dataSize);
		} 
		else if(filtroMae != null && !filtroMae.equals("")) {	
			if(unidade!=null){
				pessoas = this.pessoaDAO.buscarComPaginacao(first, pageSize, filtroMae, 3, unidade, pais, tenantId);
				dataSize = this.pessoaDAO.encontrarQdePessoas(filtroMae, 3, unidade, pais, tenantId).intValue();
			}
			else {
				pessoas = this.pessoaDAO.buscarComPaginacao(first, pageSize, filtroMae, 3, pais, tenantId);
				dataSize = this.pessoaDAO.encontrarQdePessoas(filtroMae, 3, pais, tenantId).intValue();
			}
			this.setRowCount(dataSize);
		}
		else if(filtroCodigoProntuario != null && !filtroCodigoProntuario.equals("")) {
			if(unidade!=null){
				pessoas = this.pessoaDAO.buscarComPaginacao(first, pageSize, filtroCodigoProntuario, 5, unidade, pais, tenantId);
				dataSize = this.pessoaDAO.encontrarQdePessoas(filtroCodigoProntuario, 5, unidade, pais, tenantId).intValue();
			}
			else {
				pessoas = this.pessoaDAO.buscarComPaginacao(first, pageSize, filtroCodigoProntuario, 5, pais, tenantId);
				dataSize = this.pessoaDAO.encontrarQdePessoas(filtroCodigoProntuario, 5, pais, tenantId).intValue();
			}
			this.setRowCount(dataSize);
		} 
		else if(filtroFisico != null && !filtroFisico.equals("")) {		
			if(unidade!=null){
				pessoas = this.pessoaDAO.buscarComPaginacao(first, pageSize, filtroFisico, 6, unidade, pais, tenantId);
				dataSize = this.pessoaDAO.encontrarQdePessoas(filtroFisico, 6, unidade, pais, tenantId).intValue();
			}
			else {
				pessoas = this.pessoaDAO.buscarComPaginacao(first, pageSize, filtroFisico, 6, pais, tenantId);
				dataSize = this.pessoaDAO.encontrarQdePessoas(filtroFisico, 6, pais, tenantId).intValue();
			}
			this.setRowCount(dataSize);
		}
		else if(filtroCodigoPessoa != null && !filtroCodigoPessoa.equals("")) {			
			if(unidade!=null){
				pessoas = this.pessoaDAO.buscarComPaginacao(first, pageSize, filtroCodigoPessoa, 7, unidade, pais, tenantId);
				dataSize = 1;
			}
			else {
				pessoas = this.pessoaDAO.buscarComPaginacao(first, pageSize, filtroCodigoPessoa, 7, pais, tenantId);
				dataSize = 1;
			}
			this.setRowCount(dataSize);
		} 
		else {			
			if(unidade!=null){
				pessoas = this.pessoaDAO.buscarComPaginacao(first, pageSize, "todos", 0, unidade, pais, tenantId);
				dataSize = this.pessoaDAO.encontrarQdePessoas("todos", 0, unidade, pais, tenantId).intValue();
			}
			else {
				pessoas = this.pessoaDAO.buscarComPaginacao(first, pageSize, "todos", 0, pais, tenantId);
				dataSize = this.pessoaDAO.encontrarQdePessoas("todos", 0, pais, tenantId).intValue();
			}
			this.setRowCount(dataSize);
		}
		
		return pessoas;
	}

	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		if(unidade==null){
			return this.pessoaDAO.encontrarQdePessoas("todos", 0, pais, tenantId).intValue();
		}
		else {
			return this.pessoaDAO.encontrarQdePessoas("todos", 0, unidade, pais, tenantId).intValue();
		}
	}
}