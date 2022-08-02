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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Murakami
 *
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@NamedQueries({  
	@NamedQuery(name="IndicadorFamilia.buscarIndicadores", query="select if from IndicadorFamilia if "
			+ "where if.familia = :familia and if.tenant_id = :tenantId") //MONTH(if.dataCriacao) = MONTH(:date )
}) 
public class IndicadorFamilia implements Serializable {

	private static final long serialVersionUID = -3276288201376060553L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	private Long tenant_id;
		
	/* relacionamentos */
	
	@ManyToOne
	@JoinColumn(name="codigo_familia")
	private Familia familia;
	
	@ManyToOne
	@JoinColumn(name="codigo_indicador")
	private IndProtecao indProtecao;
	
	public IndicadorFamilia() {}
	
	public IndicadorFamilia(Familia familia, IndProtecao indProtecao) {
		super();
		this.familia = familia;
		this.indProtecao = indProtecao;
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
