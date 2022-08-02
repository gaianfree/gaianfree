package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.StringJoiner;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.softarum.svsa.dao.CapaProntuarioDAO;
import com.softarum.svsa.dao.MPComposicaoDAO;
import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.modelo.ObsComposicaoFamiliar;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.to.AtendimentoDTO;
import com.softarum.svsa.util.NegocioException;


/**
 * @author murakamiadmin
 *
 */
public class MPComposicaoService implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(MPComposicaoService.class);
	
	@Inject
	private MPComposicaoDAO composicaoDAO;
	@Inject
	private CapaProntuarioDAO prontuarioDAO;
	@Inject
	private AgendamentoIndividualService listaService;
	@Inject
	private ProntuarioService prontService;	
		
	
	public Pessoa salvar(Pessoa pessoa) throws NegocioException {
		
		if (pessoa.getFormaIngresso() == null) 
			throw new NegocioException("A forma de ingresso na unidade é obrigatória");			
		
		pessoa.setDataRegistroComposicaoFamiliar(Calendar.getInstance());
		
		/* grava municipio em maiusculo*/
		pessoa.getFamilia().getEndereco().setMunicipio(pessoa.getFamilia().getEndereco().getMunicipio());
		
		return this.composicaoDAO.salvar(pessoa);		
		
	}

	public void salvarObservacao(ObsComposicaoFamiliar obsComposicaoFamiliar) throws NegocioException {
		
		if (obsComposicaoFamiliar.getObservacao() == null)
			throw new NegocioException("A observação é obrigatória");	
				
		this.composicaoDAO.salvarObservacao(obsComposicaoFamiliar);
		
	}

	/*
	 * MPComposicaoFamiliar
	 * Criação de prontuario/familia nova com exclusão da pessoa da atual composição familiar 
	 */
	public void criarProntuario(Pessoa pessoa, Usuario tecnico, Long tenantId) throws NegocioException {
		
		if(pessoa instanceof PessoaReferencia)
			throw new NegocioException("Pessoa de Referência não pode ser excluída da família!");	
				
		// validação se existe um nome igual como pessoaReferencia
		verificarExistenciaProntuario(pessoa, pessoa.getFamilia().getProntuario().getUnidade(), tenantId);
				 
		log.debug("endereco da pessoa: " + pessoa.getFamilia().getEndereco().getEndereco());
				
		prontService.criarProntuario(pessoa, tecnico, tenantId);
		
	}
	public void inativarMembro(Pessoa pessoa) throws NegocioException {
		
		if(pessoa instanceof PessoaReferencia)
			throw new NegocioException("Pessoa de Referência não pode ser INATIVADA!");	
		
		this.composicaoDAO.salvar(pessoa);		
	}
	
	public void transferirMembro(Pessoa pessoa, Long codigoProntDestino) throws NegocioException {
		
		if(pessoa instanceof PessoaReferencia)
			throw new NegocioException("Pessoa de Referência não pode ser transferida!");	
				
		prontService.transferirMembro(pessoa, codigoProntDestino);
	}
	
	public void excluirMembro(Pessoa pessoa) throws NegocioException {
		
		if(pessoa instanceof PessoaReferencia)
			throw new NegocioException("Pessoa de Referência não pode ser excluída da família!");
		
		pessoa.setExcluida(true);
		this.composicaoDAO.salvar(pessoa);
	}

	public Prontuario buscarProntuario(Long prontuarioDestino, Unidade unidade, Long tenantId) {
		
		return prontService.buscarProntuario(prontuarioDestino, unidade, tenantId);
	}
	
	public void verificarExistenciaProntuario(Pessoa pessoa, Unidade unidade, Long tenantId) throws NegocioException {
		
		List<PessoaReferencia> pessoasReferencia = composicaoDAO.pesquisar(pessoa.getNome(), unidade, tenantId);
				
		if(pessoasReferencia.size() > 0) {
			
			for(PessoaReferencia p : pessoasReferencia) {
				if(p.getCodigo() == pessoa.getCodigo()) {
					throw new NegocioException("Essa pessoa já tem prontuário. ");
				}
			}
			throw new NegocioException("Existem prontuários com nomes semelhantes ao desta pessoa. ");
		}		
	}

	
	
	
	/*
	 * FIM da Criação de prontuario a partir da composição familiar 
	 */
	
	public List<PessoaReferencia> todasPessoasReferencia(Long tenantId) {
		return composicaoDAO.todasPessoasReferencia(tenantId);
	}

	public List<Pessoa> buscarTodosMembros(PessoaReferencia pessoaReferencia, Long tenantId) {
		return composicaoDAO.buscarTodosMembros(pessoaReferencia, tenantId);
	}
	
	public List<Pessoa> buscarTodosMembros(Prontuario prontuario, Long tenantId) {
		return composicaoDAO.buscarTodosMembros(prontuario, tenantId);
	}

	public List<ObsComposicaoFamiliar> buscarTodasObservacoes(Prontuario prontuario, Long tenantId) {
		return composicaoDAO.buscarTodasObservacoes(prontuario, tenantId);
	}
	
	public MPComposicaoDAO getComposicaoDAO() {
		return composicaoDAO;
	}

	
	public List<PessoaReferencia> pesquisar(String termoPesquisa, Unidade unidade, Long tenantId) {
		return composicaoDAO.pesquisar(termoPesquisa, unidade, tenantId);
	}
	
	
	public List<String> pesquisarNomesPR(String query, Long tenantId) {
		return composicaoDAO.pesquisarNomesPR(query, tenantId);
	}
	public void validarCadastro(String query, Long tenantId) throws NegocioException {
		
		List<Prontuario> prontuarios = composicaoDAO.pesquisarExistente(query, tenantId);
		StringJoiner message = new StringJoiner(", ").add("CUIDADO! Já existe prontuário com esse nome");
		for(Prontuario p : prontuarios) {			
			
			String s = p.getCodigo() + " - " + p.getFamilia().getPessoaReferencia().getNome() + " - " + p.getUnidade().getNome();
			log.info(s);
			message.add(s);			
		}
		message.add("PORÉM, isso não impede o cadastro duplicado.");
		
		if(prontuarios.size() > 0) {
			log.info(message.toString());
			throw new NegocioException(message.toString());
		}
	}
	
	
	
	
	
	
	public List<AtendimentoDTO> consultarResumoAtendimentos(Pessoa pessoa, Long tenantId) {		

		/* atendimentos individualizados, coletivos, ações, etc.) */
		List<AtendimentoDTO> atendIndiv = listaService.buscarResumoAtendimentosDTO(pessoa, tenantId);
		
		return atendIndiv;
	}	
	
	public List<ListaAtendimento> consultaFaltas(Pessoa pessoa, Long tenantId) {
		
		return listaService.consultaFaltas(pessoa, tenantId);
		
	}

	public PessoaReferencia trocarPessoaReferencia(PessoaReferencia pessoaReferencia, Pessoa novaPessoaReferencia) throws NegocioException {
		
		
		Prontuario prontuario = prontuarioDAO.buscarPeloCodigo(pessoaReferencia.getFamilia().getProntuario().getCodigo());
		
		// trocando PessoaReferencia na mesma transacao
		log.info("trocando pessoa referencia...2");
		this.prontuarioDAO.trocarPR(prontuario.getFamilia(), prontuario.getFamilia().getPessoaReferencia(), novaPessoaReferencia);			
		
		return this.composicaoDAO.buscarPFPeloCodigo(novaPessoaReferencia.getCodigo());
		
	}

		

}