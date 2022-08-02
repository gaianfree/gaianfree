package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import com.softarum.svsa.modelo.enums.CodigoEncaminhamento;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Encaminhamento Externo
 * 
 * @author Murakami
 *
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@NamedQueries({  
	@NamedQuery(name="Encaminhamento.buscarEncaminhamentos", 
		query="select e from Encaminhamento e where e.pessoa = :pessoa "
				+ "and e.tenant_id = :tenantId"),
	@NamedQuery(name="Encaminhamento.buscarEncaminhamentosUnidade", 
		query="select e from Encaminhamento e where e.unidade = :unidade "
				+ "and e.tenant_id = :tenantId"),
	@NamedQuery(name="Encaminhamento.buscarEncaminhamentosPeriodo", 
		query="select e from Encaminhamento e where "
				+ "e.unidade = :unidade "
				+ "and e.tenant_id = :tenantId "
				+ "and e.data between :ini and :fim"),
	@NamedQuery(name="Encaminhamento.buscarEncGraficoUnidade", 
		query="select e from Encaminhamento e where e.unidade = :unidade "
				+ "and e.tenant_id = :tenantId "
				+ "order by e.codigoEncaminhamento"),
	@NamedQuery(name="Encaminhamento.buscarEncGraficoPeriodo", 
		query="select e from Encaminhamento e where "
			+ "e.unidade = :unidade "
			+ "and e.tenant_id = :tenantId "
			+ "and e.data between :ini and :fim "
			+ "order by e.codigoEncaminhamento")
}) 
public class Encaminhamento implements Serializable {
	
	private static final long serialVersionUID = -3276288201376060553L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;

	private Long tenant_id;
	
	@ToString.Include
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	private String orgaoUnidadeDestino;
	
	@Column(length = 512000,columnDefinition="Text")
	private String motivo;
	
	@Column(length = 512000,columnDefinition="Text")
	private String anotacaoComplementar;
	private String enderecoUnidadeDestino;
	private boolean nomeSocial;
	
	/* Enums */
	@Enumerated(EnumType.STRING)
	private CodigoEncaminhamento codigoEncaminhamento;
	
	/* relacionamentos */
	@ManyToOne
	@JoinColumn(name="codigo_tecnico")
	private Usuario tecnico;
	
	@ManyToOne
	@JoinColumn(name="codigo_pessoa")
	private Pessoa pessoa;
	
	@ManyToOne
	@JoinColumn(name="codigo_unidade")
	private Unidade unidade;
	
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
