package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.util.Date;

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

import com.softarum.svsa.modelo.enums.Prioridade;

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
	@NamedQuery(name="Inscricao.buscarTodos", query="select i from Inscricao i where i.tenant_id = :tenantId"),
	@NamedQuery(name="Inscricao.buscarInscricoes", query="select i from Inscricao i "
			+ "where i.servico = :servico and i.tenant_id = :tenantId"),
	@NamedQuery(name="Inscricao.buscarInscricoesAtivas", query="select i from Inscricao i "
			+ "where i.servico = :servico and i.tenant_id = :tenantId and i.desistente = false"),
	@NamedQuery(name="Inscricao.qdeInscricoesAtivas", query="select count(i) from Inscricao i "
			+ "where i.servico = :servico and i.tenant_id = :tenantId and i.desistente = false")
})
public class Inscricao implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2973143038107769905L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	private Long tenant_id;
	
	@ToString.Include
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	/* enums */
	@Enumerated(EnumType.STRING)
	private Prioridade prioridade;
	
	/* relacionamentos */
	@ManyToOne
	@JoinColumn(name="codigo_pessoa")
	private Pessoa pessoa;
	
	@ManyToOne
	@JoinColumn(name="codigo_servico")
	private Servico servico;
	private boolean desistente = false;
	
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
