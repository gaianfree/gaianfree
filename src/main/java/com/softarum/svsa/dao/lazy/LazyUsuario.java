package com.softarum.svsa.dao.lazy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.softarum.svsa.dao.UsuarioDAO;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.service.UsuarioService;



/**
 * @author murakamiadmin
 *
 */
public class LazyUsuario extends LazyDataModel<Usuario> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	List<Usuario> usuarios = new ArrayList<Usuario>();
	private UsuarioDAO usuarioDAO;
	private Unidade unidade;
	private Long tenantId;
	
	private Logger log = Logger.getLogger(LazyUsuario.class);
	
	public LazyUsuario(UsuarioService usuarioService, Usuario usuario, Long tenantId) {
		this.usuarioDAO = usuarioService.getUsuarioDAO();
		this.unidade = usuario.getUnidade();
		this.tenantId = tenantId;
	}	
			
	@Override	
	public List<Usuario> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filters) {	
		
		String nomePessoa = "";
		
		/*
		 * Apenas para 1 filtro (verificar na página =  nome
		 */
		for (Map.Entry<String, FilterMeta> entrada : filters.entrySet()) { 
			log.info("Key: " + entrada.getKey() + " : " + entrada.getValue()); 
			
			FilterMeta meta = entrada.getValue();
			
			if(meta.getField().equals("nome")) {
				nomePessoa = (String) meta.getFilterValue();
			}			
		}
		
		usuarios = new ArrayList<Usuario>();
		
		if (unidade.getTipo() == TipoUnidade.SASC) {
			if(nomePessoa != null && !nomePessoa.equals("")) {
				usuarios = this.usuarioDAO.buscarComPaginacao(first, pageSize, nomePessoa, tenantId);
				this.setRowCount(this.usuarioDAO.encontrarQuantidadeDeUsuarios(nomePessoa,tenantId).intValue());
			}
			else {
				usuarios = this.usuarioDAO.buscarComPaginacao(first, pageSize, tenantId);
				this.setRowCount(this.usuarioDAO.encontrarQuantidadeDeUsuarios(tenantId).intValue());
			}
		}else {
			if(nomePessoa != null && !nomePessoa.equals("")) {
				usuarios = this.usuarioDAO.buscarComPaginacao(first, pageSize, unidade, nomePessoa, tenantId);
				this.setRowCount(this.usuarioDAO.encontrarQuantidadeDeUsuarios(unidade, nomePessoa, tenantId).intValue());
			}
			else {
				usuarios = this.usuarioDAO.buscarComPaginacao(first, pageSize, unidade, tenantId);
				this.setRowCount(this.usuarioDAO.encontrarQuantidadeDeUsuarios(unidade, tenantId).intValue());
			}
		}
						
		return usuarios;
	}	
	
	
	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		
		if (unidade.getTipo() == TipoUnidade.SASC) {			
			return this.usuarioDAO.encontrarQuantidadeDeUsuarios(tenantId).intValue();
		} else {
			log.info("tenant_id: " + tenantId);
			return this.usuarioDAO.encontrarQuantidadeDeUsuarios(unidade, tenantId).intValue();
		}
		
		/*
		return (int) usuarios.stream()
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