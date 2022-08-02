package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.softarum.svsa.modelo.enums.TipoUnidade;

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
@Audited
@NamedQueries({
	@NamedQuery(name="Unidade.buscarTodos", query="select u from Unidade u where u.tenant_id = :tenantId"),
	@NamedQuery(name="Unidade.buscarNomesUnidades", query="select u.nome from Unidade u where u.tenant_id = :tenantId"),
	@NamedQuery(name="Unidade.buscarTodosCRAS", query="select u from Unidade u where u.tipo = :tipo and u.tenant_id = :tenantId"),
	@NamedQuery(name="Unidade.buscarTodosCREAS", query="select u from Unidade u where u.tipo = :tipo and u.tenant_id = :tenantId"),
	@NamedQuery(name="Unidade.buscarTodosCRAS2", query="select u from Unidade u where u.tipo = :tipo"),
	@NamedQuery(name="Unidade.buscarTodosCREAS2", query="select u from Unidade u where u.tipo = :tipo")
})
public class Unidade implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5526059262907035239L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	private Long tenant_id;
	
	@NotBlank(message="O nome da unidade é obrigatório.")
	private String nome;
	
	private String contato;
	
	@Enumerated(EnumType.STRING)
	private TipoUnidade tipo;
	
	@ManyToOne
	@JoinColumn(name="codigo_vinculada")
	private Unidade unidadeVinculada;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_endereco")
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private Endereco endereco;
	
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
