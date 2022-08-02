package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.softarum.svsa.dao.PlanoAcompanhamentoDAO;
import com.softarum.svsa.modelo.MetaPlanoFamiliar;
import com.softarum.svsa.modelo.PlanoAcompanhamento;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.to.AcompanhamentoDTO;
import com.softarum.svsa.modelo.to.DatasIniFimTO;
import com.softarum.svsa.util.NegocioException;

/**
 * @author murakamiadmin
 *
 */
public class PlanoAcompanhamentoService implements Serializable {

	private static final long serialVersionUID = 1L;
	//private LogUtil logUtil = new LogUtil(PlanoAcompanhamentoService.class);
	
	@Inject
	private PlanoAcompanhamentoDAO planoDAO;
	
	/*
	 * Plano
	 */
	
	public void salvar(PlanoAcompanhamento plano, Long tenantId) throws NegocioException {			
							
		if(plano.getCodigo() == null) {
			if(this.planoDAO.verificarPlanoAtivo(plano.getProntuario().getCodigo(), tenantId) > 0) {
				throw new NegocioException("Já existe um plano em andamento. Encerre-o antes de criar um novo.");
			}
		}
		
		this.planoDAO.salvar(plano);
	}
	
	public void salvarEncerramento(PlanoAcompanhamento plano) throws NegocioException {		

		/*
		PlanoAcompanhamento p = planoDAO.buscarPeloCodigo(plano.getCodigo());			
		
		p.setDataDesligamento(plano.getDataDesligamento());
		p.setRazaoDesligamento(plano.getRazaoDesligamento());
		// retira o status PAIF ou PAEFI em acompanhamento
		p.getProntuario().setPrograma(null);
		this.planoDAO.salvar(plano);	
		*/
		
		PlanoAcompanhamento p = planoDAO.buscarPeloCodigo(plano.getCodigo());
		
		if(p.getMetas().isEmpty()) {
			throw new NegocioException("Este plano não possui metas concluídas. Defina e avalie pelo menos uma meta.");	
		}
		else {		
			for(MetaPlanoFamiliar meta: p.getMetas()) {
				if(meta.getAtendimentoEfetivo() == null) {
					throw new NegocioException("Existe pelo menos 1 meta sem avaliação. Para encerrar avalie todas as metas.");					
				}
			}
		}
		plano.setDataDesligamento(new Date());
		this.planoDAO.salvar(plano);	
	}
	
	public void excluir(PlanoAcompanhamento plano) throws NegocioException {
		
		plano = planoDAO.buscarPeloCodigo(plano.getCodigo());
		
		if(plano.getMetas().size() > 0) {
			throw new NegocioException("Este plano possui avaliação(ões). Se deseja excluir mesmo, exclua a(s) avaliação(ões) antes.");					
		}
		
		planoDAO.excluir(plano);
		
	}
	
	public PlanoAcompanhamento buscarPeloCodigo(long codigo) {
		return this.planoDAO.buscarPeloCodigo(codigo);
	}

	/*
	public List<PlanoAcompanhamento> buscarPlanosAno(Integer ano, long prontuario) {
		return planoDAO.buscarPlanosAno(ano, prontuario);
	}
	*/
	
	public List<PlanoAcompanhamento> buscarPlanos(Prontuario prontuario, Long tenantId) {
		return planoDAO.buscarPlanos(prontuario, tenantId);
	}
	
	public List<AcompanhamentoDTO> buscarFamiliasAcompanhamentoDTO(Unidade unidade, Long tenantId) {	
		
		return planoDAO.buscarFamiliasAcompanhamentoDTO(unidade, tenantId);
	}
	
	/* usado em Relatorio Plano Acompanhamento */
	public List<AcompanhamentoDTO> buscarFamiliasAcompanhamentoDTO(Unidade unidade, DatasIniFimTO datasTO, Long tenantId) {	
		
		return planoDAO.buscarFamiliasAcompanhamentoDTO(unidade, datasTO, tenantId);
	}
	
	public List<AcompanhamentoDTO> buscarAdolAcompanhamentoDTO(Unidade unidade, Long tenantId) {	
		
		return planoDAO.buscarAdolAcompanhamentoDTO(unidade, tenantId);
	}
	
	
	
	/*
	 * Meta
	 */
	
	/*
	 * Metas
	 */
	
	public MetaPlanoFamiliar salvar(MetaPlanoFamiliar meta) throws NegocioException {
		
		if(meta.getPlanoAcompanhamento() == null || meta.getPlanoAcompanhamento().getCodigo() == null) {
			throw new NegocioException("Selecione um plano de atendimento, é obrigatório.");
		}
		
		return this.planoDAO.salvarMeta(meta);
	}
	
	
	public void excluir(MetaPlanoFamiliar meta) throws NegocioException {
		
		//MetaPlanoFamiliar m = piaDAO.buscarMetaCodigo(meta.getCodigo());
		this.planoDAO.excluirMeta(meta);		
	}	
	
	public List<MetaPlanoFamiliar> buscarMetas(PlanoAcompanhamento plano, Long tenantId) {
		return this.planoDAO.buscarMetas(plano, tenantId);
	}
	
}