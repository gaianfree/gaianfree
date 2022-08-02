package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.softarum.svsa.dao.PiaDAO;
import com.softarum.svsa.modelo.MetaPia;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Pia;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.StatusPia;
import com.softarum.svsa.modelo.enums.TipoMse;
import com.softarum.svsa.modelo.to.AcompMseDTO;
import com.softarum.svsa.modelo.to.DatasIniFimTO;
import com.softarum.svsa.util.NegocioException;

import lombok.extern.log4j.Log4j;

/**
 * @author murakamiadmin
 *
 */
@Log4j
public class PiaService implements Serializable {

	private static final long serialVersionUID = 1L;
	//private LogUtil logUtil = new LogUtil(PiaService.class);
	
	@Inject
	private PiaDAO piaDAO;
				
	/*
	 * Plano
	 */
	
	public void salvar(Pia plano, Long tenantId) throws NegocioException {			
		
		if(plano.getCodigo() == null) {
			
			List<Pia> listaPia = piaDAO.buscarPlanoAtivo(plano.getAdolescente().getCodigo(), tenantId);
			
			/* validação para permitir um segundo plano Pia */
			if(listaPia != null) {
	
				for(Pia pia : listaPia) {
					
					if(plano.getTipoMse() == TipoMse.LA_e_PSC) {
						throw new NegocioException("Já existe um plano " + pia.getTipoMse() + " em andamento." );
					}
					else {
						if(pia.getTipoMse() == plano.getTipoMse()) {
							throw new NegocioException("Já existe um plano " + pia.getTipoMse() + " em andamento." );
						}
					}					
				}				
			}			
		}		
		if((plano.getTipoMse() == TipoMse.PSC) && 
				(plano.getResponsavelExterno() == null || plano.getResponsavelExterno().isEmpty() ||
				plano.getLocalPSC() == null || plano.getLocalPSC().isEmpty()) ){	
			
			throw new NegocioException("O local e o responsável externo são obrigatórios para PSC!");
		}		
		
		this.piaDAO.salvar(plano);
	}
	
	public void salvarEncerramento(Pia plano) throws NegocioException {		

		Pia p = piaDAO.buscarPeloCodigo(plano.getCodigo());
		
		if(p.getMetas().isEmpty()) {
			throw new NegocioException("Este plano não possui metas concluídas. Defina e avalie pelo menos uma meta.");	
		}
		else {
			for(MetaPia meta: p.getMetas()) {
				if(meta.getAvaliacao() == null) {
					throw new NegocioException("Existe pelo menos 1 meta sem avaliação. Para encerrar avalie todas as metas.");					
				}
			}
		}
		plano.setDataDesligamento(new Date());
		plano.setSituacao(StatusPia.EXTINTO);
		log.info("dataDesligamento" + plano.getDataDesligamento());
		this.piaDAO.salvar(plano);	
	}
	
	public void excluir(Pia plano) throws NegocioException {
		
		try {
			Pia p = piaDAO.buscarPeloCodigo(plano.getCodigo());
			
			for(MetaPia meta: p.getMetas()) {
				if(meta.getAvaliacao() == null) {
					throw new NegocioException("Existe pelo menos 1 meta sem avaliação. Exclua a avaliação primeiro.");					
				}
			}
			piaDAO.excluir(plano);
		} catch (Exception e ) {
			throw new NegocioException("O plano não pôde ser excluído.");	
		}
		
	}
	
	public Pia buscarPeloCodigo(long codigo) {
		return this.piaDAO.buscarPeloCodigo(codigo);
	}
	
	public List<Pia> buscarPlanosAno(Integer ano, long prontuario, Long tenantId) {
		return piaDAO.buscarPlanosAno(ano, prontuario, tenantId);
	}
	
	public List<Pia> buscarPlanos(Pessoa pessoa, Long tenantId) {
		return piaDAO.buscarPlanos(pessoa, tenantId);
	}
	
	/* usado em Relatorio PIA */
	public List<AcompMseDTO> buscarAdolAcompanhamentoDTO(Unidade unidade, Long tenantId) {			
		return piaDAO.buscarAdolAcompanhamentoDTO(unidade, tenantId);
	}	
	public List<AcompMseDTO> buscarAdolAcompanhamentoDTO(Unidade unidade, DatasIniFimTO datasTO, Long tenantId) {	
		
		return piaDAO.buscarAdolAcompanhamentoDTO(unidade, datasTO, tenantId);
	}
	/* fim em Relatorio PIA */
	
	
	/*
	 * Avaliação Meta
	 
	
	public void salvar(AvaliacaoPia avaliacao) throws NegocioException {
		
		if(avaliacao.getPia() == null || avaliacao.getPia().getCodigo() == null) {
			throw new NegocioException("Selecione um plano de atendimento, é obrigatório.");
		}
		
		this.avaliacaoDAO.salvar(avaliacao);
	}	
	
	public void excluir(AvaliacaoPia avalicao) throws NegocioException {		
		this.avaliacaoDAO.excluir(avalicao);		
	}
	
	public AvaliacaoPia buscarAvaliacaoPeloCodigo(long codigo) {
		return this.avaliacaoDAO.buscarPeloCodigo(codigo);
	}
	
	public List<AvaliacaoPia> buscarAvaliacoes(Pia plano) {
		return this.avaliacaoDAO.buscarAvaliacoes(plano);
	}
	*/
	
	
	
	/*
	 * Metas
	 */
	
	public MetaPia salvar(MetaPia meta) throws NegocioException {
		
		if(meta.getPia() == null || meta.getPia().getCodigo() == null) {
			throw new NegocioException("Selecione um plano de atendimento, é obrigatório.");
		}
		
		return this.piaDAO.salvarMeta(meta);
	}
	
	
	public void excluir(MetaPia meta) throws NegocioException {
		
		//MetaPia m = piaDAO.buscarMetaCodigo(meta.getCodigo());
		this.piaDAO.excluirMeta(meta);		
	}	
	
	public List<MetaPia> buscarMetas(Pia plano, Long tenantId) {
		return this.piaDAO.buscarMetas(plano, tenantId);
	}
	
	
}