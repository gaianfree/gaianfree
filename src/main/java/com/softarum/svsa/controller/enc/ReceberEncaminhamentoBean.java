package com.softarum.svsa.controller.enc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.HistoricoEncaminhamento;
import com.softarum.svsa.modelo.HistoricoTransferencia;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.service.CapaProntuarioService;
import com.softarum.svsa.service.HistoricoEncaminhamentoService;
import com.softarum.svsa.service.HistoricoTransferenciaService;
import com.softarum.svsa.service.MPComposicaoService;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * @author murakamiadmin
 *
 */
@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class ReceberEncaminhamentoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private HistoricoEncaminhamento historico;
	private List<Prontuario> prontuariosEncontrados = new ArrayList<>();
	private Usuario usuarioLogado;
	private boolean hist = true;
	private boolean stopPoll = false;
	
	@Inject
	private HistoricoEncaminhamentoService histEncaminhamentoService;
	@Inject
	HistoricoTransferenciaService historicoTransferenciaService;
	@Inject
	CapaProntuarioService capaProntuarioService;
	@Inject
	MPComposicaoService composicaoService;
	@Inject
	private LoginBean loginBean;
	
	
	
	@PostConstruct
	public void inicializar() {
		
		this.usuarioLogado = loginBean.getUsuario();		
		log.info("receberEncaminhamentoBean ... ");		
		
	}
	
	private void limpar() {
		hist = false;
	}
	
	public void receberCriar2() {
		
		// cria prontuario e plano PAIF/PAEFI
		try {
			
			if(historico.getUnidadeEncaminhou().getTipo() == TipoUnidade.CRAS && 
					historico.getUnidadeDestino().getTipo() == TipoUnidade.CRAS) {
				
				MessageUtil.erro("Use a opção Receber SEM Criar Prontuário e o prontuário será transferido em vez disso.");
			}
			else {	
			
				// se existir algum, arremessa um NegocioException
				//verificarExistenciaProntuario(historico.getProntuario().getFamilia().getPessoaReferencia());
				
				historico.setDataRecebimento(new Date());
				historico.setUsuarioRecebeu(getUsuarioLogado());
				historico.setDespacho("Recebido com sucesso.");
				historico.setTenant_id(loginBean.getTenantId());
			
				this.histEncaminhamentoService.salvarRecebimento2(historico);
				MessageUtil.sucesso("Recebimento realizado com sucesso, prontuário e plano criados!");
				
				limpar();			
				
			}
			
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage() + "Verifique se já está em acompanhamento, se sim Recuse, senão Receba sem criar prontuário.");
		}
		catch (Exception e) {
			MessageUtil.erro("Não foi possível salvar recebimento ou criar prontuário PAEFI!");
			e.printStackTrace();
		}
	}
	
	public void receberCriar1() {
		
		// cria so prontuario
		try {
			
			if(historico.getUnidadeEncaminhou().getTipo() == TipoUnidade.CRAS && 
					historico.getUnidadeDestino().getTipo() == TipoUnidade.CRAS) {
				
				MessageUtil.erro("Use a opção Receber SEM Criar Prontuário e o prontuário será transferido em vez disso.");
			}
			else {	
			
				// se existir algum, arremessa um NegocioException
				//verificarExistenciaProntuario(historico.getProntuario().getFamilia().getPessoaReferencia());
				
				historico.setDataRecebimento(new Date());
				historico.setUsuarioRecebeu(getUsuarioLogado());
				historico.setDespacho("Recebido com sucesso.");
				historico.setTenant_id(loginBean.getTenantId());
				
			
				this.histEncaminhamentoService.salvarRecebimento1(historico);
				MessageUtil.sucesso("Recebimento realizado com sucesso e prontuário criado!");
				
				limpar();			
				
			}
			
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage() + "Verifique se já está em acompanhamento, se sim Recuse, se não Receba sem criar prontuário e crie o plano.");
		}
		catch (Exception e) {
			MessageUtil.erro("Não foi possível salvar recebimento ou criar prontuário PAEFI!");
			e.printStackTrace();
		}
	}
	
	
	public void receberSem() {
		
		// recebe familia sem criar prontuario e plano
		try {			
			
			log.info("Receber sem.");
			
			historico.setDataRecebimento(new Date());
			historico.setUsuarioRecebeu(getUsuarioLogado());
			historico.setDespacho("Recebido com sucesso.");		
			historico.setTenant_id(loginBean.getTenantId());
			
			log.info(historico.getUnidadeEncaminhou().getTipo() + "");
			log.info(historico.getUnidadeDestino() + " " + historico.getUnidadeDestino().getTipo());
			
			if(historico.getUnidadeEncaminhou().getTipo() == TipoUnidade.CRAS && 
					historico.getUnidadeDestino().getTipo() == TipoUnidade.CRAS) {
				log.info("CRAS pra CRAS.");
				salvarHistoricoTransferencia();
			}
			
			this.histEncaminhamentoService.salvarHE(historico);
			MessageUtil.sucesso("Recebimento registrado com sucesso.");
			
			limpar();
			
		} catch (Exception e) {			
			MessageUtil.erro("Não foi possível registrar o recebimento!");
			e.printStackTrace();
		}
	}
	
	private void salvarHistoricoTransferencia() {
		try {
			log.info("Salvar Historico Transferencia.");
			
			HistoricoTransferencia historicoTransferencia = new HistoricoTransferencia();
			historicoTransferencia.setUnidade(historico.getProntuario().getUnidade());
			historicoTransferencia.setTenant_id(loginBean.getTenantId());
			
			Prontuario p = this.capaProntuarioService.buscarPeloCodigo(historico.getProntuario().getCodigo());
			
			log.info("Salvar Historico Transferencia prontuario = " + p.getCodigo());
			
			// unidade nova
			p.setUnidade(usuarioLogado.getUnidade());
		
			this.capaProntuarioService.salvar(p);
			
			historicoTransferencia.setProntuario(p);
			historicoTransferencia.setUsuario(loginBean.getUsuario());		
			historicoTransferencia.setDataTransferencia(new Date());
			
			historicoTransferencia.setMotivo(historico.getMotivo());
			
			historicoTransferenciaService.salvar(historicoTransferencia);
			
			log.info("Historico Transferencia criado. criado.");
			
		} catch (NegocioException e) {
			MessageUtil.erro(e.getMessage());
			e.printStackTrace();
		}
		
		
	}	
	
	public void verificarExistenciaProntuarios() throws NegocioException {
		
		List<PessoaReferencia> pessoasReferencia = composicaoService.pesquisar(
				historico.getProntuario().getFamilia().getPessoaReferencia().getNome(), 
				usuarioLogado.getUnidade(), 
				loginBean.getTenantId());
		
		if(pessoasReferencia.size() > 0) {			
			for(PessoaReferencia p : pessoasReferencia) {
				prontuariosEncontrados.add(p.getFamilia().getProntuario());
				MessageUtil.alerta("Prontuário com Referência Familiar semelhante: " + p.getFamilia().getProntuario().getCodigo() + " - " + p.getNome());
			}			
		}
		setStopPoll(true);
	}
	
	public void recusar() {
		
		historico.setDataRecebimento(new Date());
		historico.setUsuarioRecebeu(getUsuarioLogado());
		
		try {
			this.histEncaminhamentoService.salvarHE(historico);
			MessageUtil.sucesso("Encaminhamento recusado!");			
			limpar();
			
		} catch (NegocioException e) {
			MessageUtil.erro(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public boolean isCreas() {
		if(usuarioLogado.getUnidade().getTipo() != TipoUnidade.CRAS)
			return true;
		return false;
	}
	
}
