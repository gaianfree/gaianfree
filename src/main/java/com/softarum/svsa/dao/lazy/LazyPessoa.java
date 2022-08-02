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
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.service.PessoaService;

/**
 * @author murakamiadmin
 *
 */
public class LazyPessoa extends LazyDataModel<Pessoa> implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(LazyPessoa.class);
	
	List<Pessoa> pessoas = new ArrayList<Pessoa>();
	private PessoaDAO pessoaDAO;
	private Long tenantId;
	
	public LazyPessoa(PessoaService pessoaService, Long tenantId) {
		this.pessoaDAO = pessoaService.getPessoaDAO();
		this.tenantId = tenantId;
	}
	
	@Override
	public List<Pessoa> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filters) {	
		
		
		String filtroNome = "";
		String filtroNomeSocial = "";
		String filtroMae = "";
		String filtroEndereco = "";
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
                else if(meta.getField().equals("nomeSocial")) { //2
                	filtroNomeSocial = (String)meta.getFilterValue();
                	log.debug("property : " + meta.getField() + "-" + (String)meta.getFilterValue());
                }                     	
                else if(meta.getField().equals("nomeMae")) { //3
                	filtroMae = (String)meta.getFilterValue();
                	log.debug("property : " + meta.getField() + "-" + (String)meta.getFilterValue());
                }                		
            	else if(meta.getField().equals("familia.endereco.endereco")) { //4
            		filtroEndereco = (String)meta.getFilterValue();
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
			pessoas = this.pessoaDAO.buscarComPaginacao(first, pageSize, filtroNome, 1, tenantId);
			dataSize = this.pessoaDAO.encontrarQdePessoas(filtroNome, 1, tenantId).intValue();
			this.setRowCount(dataSize);
		} 
		else if(filtroNomeSocial != null && !filtroNomeSocial.equals("")) {			
			pessoas = this.pessoaDAO.buscarComPaginacao(first, pageSize, filtroNomeSocial, 2, tenantId);
			dataSize = this.pessoaDAO.encontrarQdePessoas(filtroNomeSocial, 2, tenantId).intValue();
			this.setRowCount(dataSize);
		} 
		else if(filtroMae != null && !filtroMae.equals("")) {			
			pessoas = this.pessoaDAO.buscarComPaginacao(first, pageSize, filtroMae, 3, tenantId);
			dataSize = this.pessoaDAO.encontrarQdePessoas(filtroMae, 3, tenantId).intValue();
			this.setRowCount(dataSize);
		} 
		else if(filtroEndereco != null && !filtroEndereco.equals("")) {	
			pessoas = this.pessoaDAO.buscarComPaginacao(first, pageSize, filtroEndereco, 4, tenantId);
			dataSize = this.pessoaDAO.encontrarQdePessoas(filtroEndereco, 4, tenantId).intValue();
			this.setRowCount(dataSize);
		} 
		else if(filtroCodigoProntuario != null && !filtroCodigoProntuario.equals("")) {			
			pessoas = this.pessoaDAO.buscarComPaginacao(first, pageSize, filtroCodigoProntuario, 5, tenantId);
			dataSize = this.pessoaDAO.encontrarQdePessoas(filtroCodigoProntuario, 5, tenantId).intValue();
			this.setRowCount(dataSize);
		} 
		else if(filtroFisico != null && !filtroFisico.equals("")) {			
			pessoas = this.pessoaDAO.buscarComPaginacao(first, pageSize, filtroFisico, 6, tenantId);
			dataSize = this.pessoaDAO.encontrarQdePessoas(filtroFisico, 6, tenantId).intValue();
			this.setRowCount(dataSize);
		}
		else if(filtroCodigoPessoa != null && !filtroCodigoPessoa.equals("")) {			
			pessoas = this.pessoaDAO.buscarComPaginacao(first, pageSize, filtroCodigoPessoa, 7, tenantId);
			dataSize = 1;
			this.setRowCount(dataSize);
		} 
		else {			
			pessoas = this.pessoaDAO.buscarComPaginacao(first, pageSize, "todos", 0, tenantId);					
			dataSize = this.pessoaDAO.encontrarQdePessoas("todos", 0, tenantId).intValue();
			this.setRowCount(dataSize);
		}
		
		return pessoas;
	}

	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		
		return this.pessoaDAO.encontrarQdePessoas("todos", 0, tenantId).intValue();
		/*
		return (int) pessoas.stream()
                .filter(o -> filter(FacesContext.getCurrentInstance(), filterBy.values(), o))
                .count();
                
        */
	}
	/*
	private boolean filter(FacesContext context, Collection<FilterMeta> filterBy, Object o) {
        boolean matching = true;

        for (FilterMeta filter : filterBy) {
            FilterConstraint constraint = filter.getConstraint();
            Object filterValue = filter.getFilterValue();

            try {
                Object columnValue = String.valueOf(o.getClass().getField(filter.getField()).get(o));
                matching = constraint.isMatching(context, columnValue, filterValue, LocaleUtils.getCurrentLocale());
            }
            catch (ReflectiveOperationException e) {
                matching = false;
            }

            if (!matching) {
                break;
            }
        }

        return matching;
    }
    */
}