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
import javax.persistence.Transient;

import com.softarum.svsa.modelo.enums.CodigoEncaminhamento;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Lauro;
 *
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@NamedQueries({
	
	/* consultas de oficíos emitidos */
	@NamedQuery(name="OficioEmitido.buscarOficiosEmitidos", query="select o from OficioEmitido o "
			+ "where o.unidade = :unidade "
			+ "and o.tenant_id = :tenantId "
			+ "and o.excluido = :exc "
			+ "order by o.dataEmissao desc"),
	@NamedQuery(name="OficioEmitido.buscarOficiosEmitidosUnidade", query="select o from OficioEmitido o "
			+ "where o.unidade = :unidade "
			+ "and o.tenant_id = :tenantId "
			+ "and o.excluido = :exc "
			+ "order by o.dataEmissao desc"),
	@NamedQuery(name="OficioEmitido.buscarOficiosEmitidosUnidadePeriodo", query="select o from OficioEmitido o "
			+ "where o.dataEmissao between :ini and :fim "
			+ "and o.unidade = :unidade "
			+ "and o.tenant_id = :tenantId "
			+ "and o.excluido = :exc "),
	/* Consultas para hist pessoa */
	@NamedQuery(name="OficioEmitido.buscarOficiosEmitidosHist", query="select o from OficioEmitido o "
			+ "where o.pessoa = :pessoa "
			+ "and o.tenant_id = :tenantId "
			+ "and o.excluido = :exc "),
	/* Consultas para Graficos */
	@NamedQuery(name="OficioEmitido.buscarOfGraficoEmitidosUnidade", query="select o from OficioEmitido o "
			+ "where o.unidade = :unidade "
			+ "and o.tenant_id = :tenantId "
			+ "and o.excluido = :exc "
			+ "order by o.codigoEncaminhamento"),
	@NamedQuery(name="OficioEmitido.buscarOfGraficoEmitidosPeriodo", query="select o from OficioEmitido o "
			+ "where o.unidade = :unidade "
			+ "and o.tenant_id = :tenantId "
			+ "and o.dataEmissao between :ini and :fim "
			+ "and o.excluido = :exc "
			+ "order by o.codigoEncaminhamento"),
	/*@NamedQuery(name="OficioEmitido.obterNrOficioEmitido", query="select (count(o) + 1) from OficioEmitido o "
			+ "where o.unidade = :unidade"),*/
	@NamedQuery(name="OficioEmitido.obterNrOficioEmitido", query="SELECT MAX( o.numero ) From OficioEmitido o "
			+ "where o.unidade = :unidade "
			+ "and o.tenant_id = :tenantId "
			+ "and o.ano = :ano "
			+ "and o.excluido = :exc ")	
})
public class OficioEmitido implements Serializable {


	private static final long serialVersionUID = 1L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	private Long tenant_id;
	
	@ToString.Include
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEmissao;
	
	@ToString.Include
	private Long numero;
	
	@ToString.Include
	private Integer ano;
	
	@ToString.Include
	@Column(length = 512000,columnDefinition="Text")
	private String assunto;
	
	@ToString.Include
	private String nome;
	
	@ToString.Include
	private boolean nomeSocial;
	
	@ToString.Include
	private String nomeOrgao;
	
	@ToString.Include
	private String endereco;
	
	private Boolean excluido = false;

	@Enumerated(EnumType.STRING)
	private CodigoEncaminhamento codigoEncaminhamento;	

	@ManyToOne
	@JoinColumn(name="codigo_tecnico")
	private Usuario tecnico;
	
	@ManyToOne
	@JoinColumn(name="codigo_unidade")
	private Unidade unidade;
	
	@ManyToOne
	@JoinColumn(name="codigo_pessoa")
	private Pessoa pessoa;
	
	@Transient
	public String getNrOficioEmitido() {
		return numero + "/" + ano;
	}
	
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
