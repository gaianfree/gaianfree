package com.softarum.svsa.dao.lazy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.softarum.svsa.dao.CapaProntuarioDAO;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.service.CapaProntuarioService;


/**
 * @author murakamiadmin
 *
 */
public class LazyProntuario extends LazyDataModel<Prontuario> implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(LazyProntuario.class);
	
	List<Prontuario> prontuarios = new ArrayList<Prontuario>();
	private CapaProntuarioDAO prontuarioDAO;
	private Unidade unidade;
	private Long tenantId;
	
	public LazyProntuario(CapaProntuarioService prontuarioService, Unidade unidade, Long tenantId) {
		this.prontuarioDAO = prontuarioService.getProntuarioDAO();
		this.unidade = unidade;
		this.tenantId = tenantId;
	}
	
	@Override
    public Prontuario getRowData(String rowKey) {        
 
        return prontuarioDAO.buscarPeloCodigo(Long.parseLong(rowKey));
    }
	
	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		
		return this.prontuarioDAO.encontrarQdeUnid(unidade, tenantId).intValue();
		/*
		return (int) prontuarios.stream()
                .filter(o -> filter(FacesContext.getCurrentInstance(), filterBy.values(), o))
                .count();
        */
	}
	/* Para ser usado com filtros automaticamente.
	 * TO DO no futuro implementar sem filtro HARDCODED (que está no load). Seria retirado e usado esse metodo.
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

	@Override	
	public List<Prontuario> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filters) {	
	
		
		String filtroNome = "";
		String filtroProntuario = "";
		
		/*
		 * Pega os filtros
		 */
		for (Map.Entry<String, FilterMeta> entrada : filters.entrySet()) { 
			log.debug("Key: " + entrada.getKey() + " : " + entrada.getValue()); 
			
			FilterMeta meta = entrada.getValue();
			
			if(meta.getField().equals("codigo")) 
            	filtroProntuario = (String)meta.getFilterValue();
            else 
                if(meta.getField().equals("familia.pessoaReferencia.nome")) 
                	filtroNome = (String)meta.getFilterValue();				
		}
		
		prontuarios = new ArrayList<Prontuario>();
		int dataSize = 0;
		
		if(filtroNome != null && !filtroNome.equals("")) {
			prontuarios = this.prontuarioDAO.buscarComPaginacao(first, pageSize, unidade, filtroNome, tenantId);
			dataSize = this.prontuarioDAO.encontrarQdePR(unidade, filtroNome, tenantId).intValue();
			this.setRowCount(dataSize);
		}
		else if(filtroProntuario != null && !filtroProntuario.equals("")) {
			prontuarios = this.prontuarioDAO.buscarComPaginacao(first, pageSize, unidade, Long.parseLong(filtroProntuario), tenantId);
			dataSize = this.prontuarioDAO.encontrarQdeProntuario(unidade, Long.parseLong(filtroProntuario), tenantId).intValue();
			this.setRowCount(dataSize);
		}			
		else {				
			prontuarios = this.prontuarioDAO.buscarComPaginacao(first, pageSize, unidade, tenantId);
			dataSize = this.prontuarioDAO.encontrarQdeUnid(unidade, tenantId).intValue();
			this.setRowCount(dataSize);
		}

		log.debug("qde filtro:" + prontuarios.size());
		return prontuarios;
	}
	
	
}