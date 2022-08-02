package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Encaminhamento Interno CRAS e CREAS
 * 
 * @author murakamiadmin
 *
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@NamedQueries({
	@NamedQuery(name="HistoricoEncaminhamento.buscarTodos", query="select h from HistoricoEncaminhamento h where "
			+ "h.tenant_id = :tenantId"),
	@NamedQuery(name="HistoricoEncaminhamento.buscarTodosProntuario", 
		query="select h from HistoricoEncaminhamento h where h.prontuario = :prontuario "
				+ "and h.tenant_id = :tenantId"),
	@NamedQuery(name="HistoricoEncaminhamento.buscarEncaminhamentoAberto", 
		query="select h from HistoricoEncaminhamento h where h.prontuario = :prontuario "
				+ "and h.tenant_id = :tenantId "
				+ "and h.dataRecebimento is null"),
	@NamedQuery(name="HistoricoEncaminhamento.buscarEncaminhamentosPendentes", 
		query="select h from HistoricoEncaminhamento h where h.unidadeDestino = :unidade "
				+ "and h.tenant_id = :tenantId "
				+ "and h.dataRecebimento is null"),
	@NamedQuery(name="HistoricoEncaminhamento.buscarEncaminhamentosRecebidos", 
		query="select h from HistoricoEncaminhamento h where h.unidadeEncaminhou = :unidade "
				+ "and h.tenant_id = :tenantId "
				+ "and h.dataCiencia is null"),
	// encaminhamentos realizados pela unidade
	@NamedQuery(name="HistoricoEncaminhamento.buscarEncaminhamentos", 
		query="select h from HistoricoEncaminhamento h where h.unidadeEncaminhou = :unidade "
				+ "and h.tenant_id = :tenantId"),
	@NamedQuery(name="HistoricoEncaminhamento.buscarEncaminhamentosPeriodo", 
		query="select h from HistoricoEncaminhamento h where "
			+ "h.unidadeEncaminhou = :unidade "
			+ "and h.tenant_id = :tenantId "
			+ "and h.dataEncaminhamento between :ini and :fim"),
	// encaminhamentos recebidos pela unidade
	@NamedQuery(name="HistoricoEncaminhamento.buscarRecebidos", 
		query="select h from HistoricoEncaminhamento h where h.unidadeDestino = :unidade "
				+ "and h.tenant_id = :tenantId "),
	@NamedQuery(name="HistoricoEncaminhamento.buscarRecebidosPeriodo", 
		query="select h from HistoricoEncaminhamento h where "
			+ "h.unidadeDestino = :unidade "
			+ "and h.tenant_id = :tenantId "
			+ "and h.dataEncaminhamento between :ini and :fim")
})
public class HistoricoEncaminhamento implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	private Long tenant_id;
	
	@ToString.Include
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEncaminhamento;
	
	@ToString.Include
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataRecebimento;
	
	@ToString.Include
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCiencia;
	
	@Column(length = 512000,columnDefinition="Text")
	@Basic(fetch=FetchType.LAZY)
	private String motivo;
	
	@Column(length = 512000,columnDefinition="Text")
	@Basic(fetch=FetchType.LAZY)
	private String despacho;
	
	@ManyToOne
	@JoinColumn(name="usuario_encaminhou")
	private Usuario usuarioEncaminhou;
	
	@ManyToOne
	@JoinColumn(name="usuario_recebeu")
	private Usuario usuarioRecebeu;
	
	@ManyToOne
	@JoinColumn(name="unidade_encaminhou")
	private Unidade unidadeEncaminhou;
	
	@ManyToOne
	@JoinColumn(name="unidade_destino")
	private Unidade unidadeDestino;
	
	@ManyToOne
	@JoinColumn(name="codigo_prontuario")
	private Prontuario prontuario;
	
	/*
	 * Datas de Criação e Modificação
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao;	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataModificacao;

	@PrePersist
	@PreUpdate
	public void configuraDatasCriacaoAlteracao() {
		this.setDataModificacao( new Date() );
				
		if (this.getDataCriacao() == null) {
			this.setDataCriacao( new Date() );
		}		
	}
}
