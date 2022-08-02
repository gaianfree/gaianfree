package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.softarum.svsa.dao.AgendamentoIndividualDAO;
import com.softarum.svsa.modelo.BeneficioEventual;
import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Beneficio;
import com.softarum.svsa.modelo.enums.CodigoAuxiliarAtendimento;
import com.softarum.svsa.modelo.enums.StatusAtendimento;
import com.softarum.svsa.util.NegocioException;

/**
 * @author murakamiadmin
 *
 */
public class VisitaService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Logger log = Logger.getLogger(VisitaService.class);
	
	@Inject
	private AgendamentoIndividualDAO listaDAO;	
	 
	
	public void encerrar(ListaAtendimento lista, BeneficioEventual beneficio) throws NegocioException {	
		
		if (lista.getCodigoAuxiliar() == null) {
			throw new NegocioException("O código Auxiliar é obrigatório.");
		}
		if (lista.getResumoAtendimento() == null || lista.getResumoAtendimento().equals("")) {
			throw new NegocioException("O resumo do atendimento é obrigatório.");	
		}
		if (lista.getDataAtendimento() == null ) {			
			lista.setDataAtendimento(new Date());				
			log.info("Data atendimento encerramento visita domiciliar = " + lista.getDataAtendimento());
		}		
		
		lista.setStatusAtendimento(StatusAtendimento.ATENDIDO);		
		
		try {
			BeneficioEventual benef = verificarBeneficio(lista, beneficio);
			if(benef == null) {
				//log.info("bene depois do tratamento = " + benef);
				this.listaDAO.salvarEncerramento(lista);
			}
			else {
				//log.info("bene depois do tratamento = " + benef);
				// gravar atendimento com beneficio
				this.listaDAO.salvarEncerramento(lista, benef);
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
			throw new NegocioException("Problema na gravação do atendimento!");
		}
	}
	
	public void excluir(ListaAtendimento item) throws NegocioException {
		this.listaDAO.excluir(item);
		
	}
	
	public List<ListaAtendimento> buscarAtendimentosPendentes(Unidade unidade, Long tenantId) {			
		
		return this.listaDAO.buscarAtendimentosPendentes(unidade, tenantId);
	}
	
	
	public ListaAtendimento autoSave(ListaAtendimento lista) throws NegocioException {			
		
		if (lista.getResumoAtendimento() == null || lista.getResumoAtendimento().equals("")) {
			throw new NegocioException("O resumo do atendimento é obrigatório.");	
		}
		if (lista.getDataAtendimento() == null ) {			
			lista.setDataAtendimento(new Date());	
			log.info("Auto save visita domiciliar = " + lista.getDataAtendimento() + " pessoa: " + lista.getPessoa().getNome());
		}
		
		lista.setStatusAtendimento(StatusAtendimento.EM_ATENDIMENTO);
		return this.listaDAO.autoSaveVisita(lista);
	}
	
	/* Verifica se se trata de beneficio */
	private BeneficioEventual verificarBeneficio(ListaAtendimento lista, BeneficioEventual beneficio) {
		
		log.info("beneficio = " + beneficio);		

		if(lista.getCodigoAuxiliar() == CodigoAuxiliarAtendimento.AUXILIO_FUNERAL) {
			beneficio.setData(lista.getDataAtendimento());
			beneficio.setProntuario(lista.getPessoa().getFamilia().getProntuario());
			beneficio.setRelato(lista.getResumoAtendimento());
			beneficio.setTecnico(lista.getTecnico());
			beneficio.setBeneficio(Beneficio.AUXILIO_FUNERAL);			
		}
		else if(lista.getCodigoAuxiliar() == CodigoAuxiliarAtendimento.AUXILIO_NATALIDADE) {
			beneficio.setData(lista.getDataAtendimento());
			beneficio.setProntuario(lista.getPessoa().getFamilia().getProntuario());
			beneficio.setRelato(lista.getResumoAtendimento());
			beneficio.setTecnico(lista.getTecnico());
			beneficio.setBeneficio(Beneficio.AUXILIO_NATALIDADE);
		}
		else if(lista.getCodigoAuxiliar() == CodigoAuxiliarAtendimento.VULNERABILIDADE_TEMPORARIA_CESTA_BASICA) {
			beneficio = criaBeneficio(lista);
			beneficio.setBeneficio(Beneficio.CESTA_BASICA);
		}
		else if(lista.getCodigoAuxiliar() == CodigoAuxiliarAtendimento.TRANSPORTE_MUNICIPAL_PESSOA_DEFICIENCIA) {
			beneficio = criaBeneficio(lista);
			beneficio.setBeneficio(Beneficio.ITEM_KIT_ESPECIFICO);
		}
		else if(lista.getCodigoAuxiliar() == CodigoAuxiliarAtendimento.OUTROS_BENEFICIOS) {
			beneficio = criaBeneficio(lista);
			beneficio.setBeneficio(Beneficio.OUTROS);
		}
		else {
			beneficio = null;
		}
		return beneficio;		
	}
	
	private BeneficioEventual criaBeneficio(ListaAtendimento lista) {
		
		BeneficioEventual beneficio = new BeneficioEventual();
		beneficio.setData(lista.getDataAtendimento());
		beneficio.setProntuario(lista.getPessoa().getFamilia().getProntuario());
		beneficio.setRelato(lista.getResumoAtendimento());
		beneficio.setTecnico(lista.getTecnico());
		beneficio.setTenant_id(lista.getTenant_id());
		
		return beneficio;
	}
	
	
}
