package com.softarum.svsa.service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.apache.log4j.Logger;

import com.softarum.svsa.dao.HistoricoEncaminhamentoDAO;
import com.softarum.svsa.modelo.Endereco;
import com.softarum.svsa.modelo.Familia;
import com.softarum.svsa.modelo.FormaIngresso;
import com.softarum.svsa.modelo.HistoricoEncaminhamento;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.modelo.PlanoAcompanhamento;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.TipoDocumento;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Status;
import com.softarum.svsa.util.NegocioException;

/**
 * @author murakamiadmin
 *
 */
public class HistoricoEncaminhamentoService implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(HistoricoEncaminhamentoService.class);
	
	@Inject
	private HistoricoEncaminhamentoDAO historicoDAO;
	
	
	
	
	
	/* 
	 * Historico encaminhamento para o CREAS 
	 */
	
	
	
	public void salvarHE(HistoricoEncaminhamento historico) throws NegocioException{		

		this.historicoDAO.salvarHE(historico);
	}	

	public List<HistoricoEncaminhamento> buscarTodosHE(Prontuario prontuario, Long tenantId) {
		return historicoDAO.buscarTodosHE(prontuario, tenantId);
	}

	public void verificarEncaminhamento(Prontuario prontuario, Long tenantId) throws NegocioException {
		
		try {
			HistoricoEncaminhamento historico = historicoDAO.buscarEncaminhamentoAberto(prontuario, tenantId);
			
			if(historico.getDataRecebimento() == null)
				throw new NegocioException("Não foi possível completar a operação porque existe encaminhamento não recebido pelo CREAS!");
			 
		}
		catch(NoResultException e) {}
	}

	public void salvarRecebimento2(HistoricoEncaminhamento historico) throws NegocioException {
		
		log.info("Prontuario rec2: " + historico.getProntuario().getFamilia().getPessoaReferencia().getNome());
		
		//log.info("Unidade: " + historico.getProntuario().getUnidade().getNome());
		
		Prontuario prontuarioVinculado = criarProntuarioVinculado(historico, true);	// true == criar com plano	
		PlanoAcompanhamento plano = criarPlano(historico, prontuarioVinculado);
		
		//log.info("Prontuario Vinculado: " + prontuarioVinculado.getFamilia().getPessoaReferencia().getNome());
		
		//log.info("Unidade vinculado: " + prontuarioVinculado.getUnidade().getNome());
		
		this.historicoDAO.salvarRecebimento(historico, prontuarioVinculado, plano);
	}
	
	public void salvarRecebimento1(HistoricoEncaminhamento historico) throws NegocioException {
		
		log.info("Prontuario rec1: " + historico.getProntuario().getFamilia().getPessoaReferencia().getNome());
		
		//log.info("Unidade: " + historico.getProntuario().getUnidade().getNome());
		
		Prontuario prontuarioVinculado = criarProntuarioVinculado(historico, false);	// false == criar sem plano
				
		//log.info("Prontuario Vinculado: " + prontuarioVinculado.getFamilia().getPessoaReferencia().getNome());
		
		//log.info("Unidade vinculado: " + prontuarioVinculado.getUnidade().getNome());
		
		this.historicoDAO.salvarRecebimento(historico, prontuarioVinculado, null);
	}
	
	
	/* cria um prontuario completo/inteiro para o CREAS e vincula o prontuario encaminhado 	*/
	private Prontuario criarProntuarioVinculado(HistoricoEncaminhamento historico, boolean comPlano) throws NegocioException {
		
		Prontuario pront = null;
		PessoaReferencia pessoa = null;
		Endereco endereco = null;
		Familia familia = null;
		
		
		try {
			
			// criação de clones
			pront = historico.getProntuario().clone();			
			pessoa = historico.getProntuario().getFamilia().getPessoaReferencia().clone();
			endereco =  historico.getProntuario().getFamilia().getEndereco().clone();
			familia = historico.getProntuario().getFamilia().clone();
			
			// pessoa de referencia			
			pessoa.setCodigo(null);
			pessoa.setStatus(Status.ATIVO);
			pessoa.setFamilia(familia);
			pessoa.setTipoDocumento(new TipoDocumento());
			pessoa.getTipoDocumento().setTenant_id(pessoa.getTenant_id());
			pessoa.setFormaIngresso(new FormaIngresso());
			pessoa.getFormaIngresso().setTenant_id(pessoa.getTenant_id());
			pessoa.setDataRegistroComposicaoFamiliar(Calendar.getInstance());
			if(pessoa.getNomeMae() == null || pessoa.getNomeMae().contentEquals("")) {
				pessoa.setNomeMae("Nao informado");
			}
			
			// endereco
			endereco.setCodigo(null);			
						
			// familia			
			familia.setCodigo(null);
			familia.setEndereco(endereco);
			familia.setPessoaReferencia(pessoa);
			familia.setProntuario(pront);
				
			// membros
			List<Pessoa> membros = familia.getMembros();
			
			membros.remove(historico.getProntuario().getFamilia().getPessoaReferencia());
			
			List<Pessoa> membrosNovos = new ArrayList<>();			
			
			
			if(membros != null && !membros.isEmpty()) {
				log.info("criando membros..." + membros.size());
				
				for(Pessoa p : membros) {
					Pessoa membro = p.clone();
					membro.setCodigo(null);
					membro.setFamilia(familia);						
					if(membro.getNomeMae() == null || membro.getNomeMae().contentEquals("")) {
						membro.setNomeMae("Nao informado");
					}
					membrosNovos.add(membro);
					//log.info("membro adicionado..." + membro.getNome());
				}
				familia.setMembros(membrosNovos);
				log.info("criando nova familia com membros = " + familia.getMembros().size());
			}
						
			// prontuário			
			pront.setCodigo(null);
			pront.setProntuario(null);
			// seta o prontuario vinculado de quem recebe (vinculo contrario)
			pront.setProntuarioVinculado(historico.getProntuario());
			pront.setDataEntrada(new Date());
			pront.setStatus(Status.ATIVO);
			pront.setUnidade(historico.getUnidadeDestino());	
			pront.setFamilia(familia);
			// registra o nome e email de quem criou o prontuário
			String  criador = historico.getUsuarioRecebeu().getNome()
					.concat(":").concat(historico.getUsuarioRecebeu().getEmail())
					.concat("(").concat("encaminhamento").concat(")");
			pront.setCriador(criador);	
			
		}
		catch(Exception e) {
			throw new NegocioException("Problema na cópia do prontuário (clone)");
		}
		
		return pront;
	}
	
	/* cria um plano para o prontuario do CREAS e vincula o prontuario encaminhado 	*/
	private PlanoAcompanhamento criarPlano(HistoricoEncaminhamento historico, Prontuario prontuario)  {
		
		PlanoAcompanhamento plano = new PlanoAcompanhamento();
		
		LocalDate data = LocalDate.now();
		plano.setAno(data.getYear());
		plano.setDataIngresso(prontuario.getDataEntrada());
		plano.setObjetivos(historico.getMotivo());
		plano.setProntuario(prontuario);
		plano.setTecnico(historico.getUsuarioRecebeu());
		plano.setTenant_id(historico.getTenant_id());
		
		return plano;
	}

	public List<HistoricoEncaminhamento> buscarEncaminhamentos(Unidade unidade, Date dataInicio, Date dataFim, Boolean recebidos, Long tenantId) {
	
		if(dataInicio != null) {
			if(dataFim != null) {
				return (recebidos) ? historicoDAO.buscarRecebidos(unidade, dataInicio, dataFim, tenantId) 
						: historicoDAO.buscarEncaminhamentos(unidade, dataInicio, dataFim, tenantId);
			}
			else {
				return (recebidos) ? historicoDAO.buscarRecebidos(unidade, dataInicio,  new Date(), tenantId) 
						: historicoDAO.buscarEncaminhamentos(unidade, dataInicio,  new Date(), tenantId);
			}
		}
		return (recebidos) ? historicoDAO.buscarRecebidos(unidade, tenantId) 
				: historicoDAO.buscarEncaminhamentos(unidade, tenantId);
	}
	
}
