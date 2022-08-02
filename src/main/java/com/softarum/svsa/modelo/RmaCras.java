package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
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
import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author murakamiadmin
 *
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@NamedQueries({	
	@NamedQuery(name="RmaCras.buscarTodos", query="select rma from RmaCras rma where rma.unidade = :unidade and rma.tenant_id = :tenantId"),
	@NamedQuery(name="RmaCras.buscarRmaFechado", query="select rma from RmaCras rma "
			+ "where rma.unidade = :unidade "
			+ "and rma.mesAnoReferencia = :mesAnoRef "
			+ "and rma.tenant_id = :tenantId"),
	@NamedQuery(name="RmaCras.buscarRmasFechados", query="select rma.mesAnoReferencia from RmaCras rma "
			+ "where rma.unidade = :unidade "
			+ "and rma.tenant_id = :tenantId")
})
public class RmaCras implements Serializable {

	private static final long serialVersionUID = 82375949344894033L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@ManyToOne
	@JoinColumn(name="codigo_unidade")
	private Unidade unidade;
	
	private Long tenant_id;
	
	@NotBlank(message="O mes e ano de referencia é obrigatório")
	private String mesAnoReferencia;
	
	private Integer bloco1a1 = 0;
	private Integer bloco1a2 = 0;
	
	private Integer bloco1b1 = 0;
	private Integer bloco1b2 = 0;
	private Integer bloco1b3 = 0;
	private Integer bloco1b4 = 0;
	private Integer bloco1b5 = 0;
	private Integer bloco1b6 = 0;
	private Integer bloco1b7 = 0;
	private Integer bloco1b8 = 0;
	private Integer bloco1b9 = 0;
	private Integer bloco1b10 = 0;
	
	private Integer bloco2c1 = 0;
	private Integer bloco2c2 = 0;
	private Integer bloco2c3 = 0;
	private Integer bloco2c4 = 0;
	private Integer bloco2c5 = 0;
	private Integer bloco2c6 = 0;
	private Integer bloco2c7 = 0;
	private Integer bloco2c8 = 0;
	private Integer bloco2c9 = 0;
	
	private Integer bloco3d1 = 0;
	private Integer bloco3d2 = 0;
	private Integer bloco3d3 = 0;
	private Integer bloco3d4 = 0;
	private Integer bloco3d5 = 0;
	private Integer bloco3d6 = 0;
	private Integer bloco3d7 = 0;
	private Integer bloco3d8 = 0;
	private Integer bloco3d9 = 0;
	
	
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
