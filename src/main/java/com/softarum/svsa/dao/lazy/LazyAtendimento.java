package com.softarum.svsa.dao.lazy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ComparatorUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.softarum.svsa.dao.AgendamentoIndividualDAO;
import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.to.DatasIniFimTO;
import com.softarum.svsa.service.AgendamentoIndividualService;


/**
 * @author murakamiadmin
 *
 */
public class LazyAtendimento extends LazyDataModel<ListaAtendimento> implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(LazyAtendimento.class);
	
	private List<ListaAtendimento> atendimentos = new ArrayList<ListaAtendimento>();
	private AgendamentoIndividualDAO atendDAO;
	private Unidade unidade;
	private DatasIniFimTO datasTO;
	private Long tenantId;
	
	public LazyAtendimento(AgendamentoIndividualService service, Unidade unidade, DatasIniFimTO datasTO, Long tenantId) {
		
		this.atendDAO = service.getListaDAO();
		this.unidade = unidade;
		this.datasTO = datasTO;
		this.tenantId = tenantId;
	}	
 
	
	@Override
    public ListaAtendimento getRowData(String rowKey) {        
		return atendDAO.buscarPeloCodigo(Long.parseLong(rowKey));
    }
	
	@Override
	public String getRowKey(ListaAtendimento lista) {
        return String.valueOf(lista.getCodigo());
    }
	
	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		
		return this.atendDAO.encontrarQde(unidade, datasTO, tenantId).intValue();
		/*
		 return (int) atendimentos.stream()
		
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
	public List<ListaAtendimento> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filters) {	
		
		String filtro1 = "";
		String filtro2 = "";
		String filtro3 = "";
		
		for (Map.Entry<String, FilterMeta> entrada : filters.entrySet()) { 
            try {
            	FilterMeta meta = entrada.getValue();
            	         
                    if(meta.getField().equals("pessoa.codigo")) {
                    	filtro1 = (String)meta.getFilterValue();
                    } else if(meta.getField().equals("pessoa.nome")) {
                    	filtro2 = (String)meta.getFilterValue();
                    } else if(meta.getField().equals("tecnico.nome")) {
                    	filtro3 = (String)meta.getFilterValue();
                    }                    	
	
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

		
		atendimentos = new ArrayList<ListaAtendimento>();
		int dataSize = 0;
		
		
		if(filtro1 != null && !filtro1.equals("")) {			
			log.debug("filtro por codigo pessoa = :" + filtro1); 
			atendimentos = this.atendDAO.buscarComPaginacao(first, pageSize, unidade, datasTO, filtro1, 1, tenantId);	
			dataSize = this.atendDAO.encontrarQde(unidade, datasTO, filtro1, 1, tenantId).intValue();
			this.setRowCount(dataSize); 
		} else if(filtro2 != null && !filtro2.equals(""))  {
			log.debug("filtro por nome pessoa = :" + filtro2); 
			log.debug("buscarComPaginacao (parametros): " + first + ", " + pageSize + ", " + unidade.getCodigo() + ", " + datasTO.getIni() + ", " + datasTO.getFim());
			atendimentos = this.atendDAO.buscarComPaginacao(first, pageSize, unidade, datasTO, filtro2, 2, tenantId);	
			dataSize = this.atendDAO.encontrarQde(unidade, datasTO, filtro2, 2, tenantId).intValue();
			this.setRowCount(dataSize);
		} else if(filtro3 != null && !filtro3.equals("")) {
			log.debug("filtro por nome tecnico = :" + filtro3);  
			atendimentos = this.atendDAO.buscarComPaginacao(first, pageSize, unidade, datasTO, filtro3, 3, tenantId);	
			dataSize = this.atendDAO.encontrarQde(unidade, datasTO, filtro3, 3, tenantId).intValue();
			this.setRowCount(dataSize);
		} else {	
			log.debug("sem filtro da unidade " + unidade.getNome()); 
			log.debug("buscarComPaginacao (parametros): " + first + ", " + pageSize + ", " + unidade.getCodigo() + ", " + datasTO.getIni() + ", " + datasTO.getFim());
			atendimentos = this.atendDAO.buscarComPaginacao(first, pageSize, unidade, datasTO, tenantId);
			dataSize = this.atendDAO.encontrarQde(unidade, datasTO, tenantId).intValue();
			this.setRowCount(dataSize);
			log.debug("qde = : " + atendimentos.size() + " dataSize: " + dataSize); 
		}

		log.debug("tamanho = :" + dataSize);
		
		// sort
        if (!sortBy.isEmpty()) {
            List<Comparator<ListaAtendimento>> comparators = sortBy.values().stream()
                    .map(o -> new LazyAtendimentoSorter(o.getField(), o.getOrder()))
                    .collect(Collectors.toList());
            Comparator<ListaAtendimento> cp = ComparatorUtils.chainedComparator(comparators); // from apache
            atendimentos.sort(cp);
        }
        
        return atendimentos;
	}
	
}