package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import com.softarum.svsa.modelo.enums.ResultadoMeta;

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
	@NamedQuery(name="MetaPlanoFamiliar.buscarTodos", query="select a from MetaPlanoFamiliar a where a.tenant_id = :tenantId  "),
	@NamedQuery(name="MetaPlanoFamiliar.buscarMetas", query="select a from MetaPlanoFamiliar a where a.planoAcompanhamento = :plano "
			+ "and a.tenant_id = :tenantId  "
			+ "order by a.data DESC")
})
public class MetaPlanoFamiliar implements Serializable {

	private static final long serialVersionUID = -2075219941072513026L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	private Long tenant_id;
	
	@ToString.Include
	@Temporal(TemporalType.TIMESTAMP)
	private	Date data;
	
	@Column(length = 512000,columnDefinition="Text")
	@Basic(fetch=FetchType.LAZY)
	private	String resumo;
	
	/* enums */	
	@Enumerated(EnumType.STRING)
	private ResultadoMeta atendimentoEfetivo;	
	
	/* relacionamentos */
	@ManyToOne
	@JoinColumn(name="codigo_plano")
	private PlanoAcompanhamento planoAcompanhamento;
	
	@ManyToOne
	@JoinColumn(name="codigo_tecnico")
	private Usuario tecnico;

	/*
	 * Datas de Cria????o e Modifica????o
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
