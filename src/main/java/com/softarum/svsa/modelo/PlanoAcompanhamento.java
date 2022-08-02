package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.softarum.svsa.modelo.enums.PerfilFamilia;
import com.softarum.svsa.modelo.enums.RazaoDesligamento;

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
	@NamedQuery(name="PlanoAcompanhamento.buscarPlanos", query="select p from PlanoAcompanhamento p "
			+ "where p.prontuario = :prontuario "
			+ "and p.tenant_id = :tenantId  "
			+ "order by p.dataDesligamento ASC" )
})
public class PlanoAcompanhamento implements Serializable {

	private static final long serialVersionUID = -7649948038035071003L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	private Long tenant_id;
	
	@ToString.Include
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataIngresso;
	
	private int prazoMeses;
	
	@Column(length = 512000,columnDefinition="Text")
	@Basic(fetch=FetchType.LAZY)
	private String objetivos;
	
	@Column(length = 512000,columnDefinition="Text")
	@Basic(fetch=FetchType.LAZY)
	private String compromissos;
	
	@ToString.Include
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataDesligamento;
	private Integer ano;
	
	/* enums */
	@Enumerated(EnumType.STRING)
	private RazaoDesligamento razaoDesligamento;
	
	@ElementCollection(targetClass = PerfilFamilia.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "PerfisFamilia", joinColumns = @JoinColumn(name = "codigo_plano"))
	@Column(name = "perfil", nullable = false)
	@Enumerated(EnumType.STRING)
	//@Basic(fetch = FetchType.EAGER)
	private List<PerfilFamilia> perfisFamilia;
	
	@ManyToOne
	@JoinColumn(name="codigo_prontuario")
	private Prontuario prontuario;
	
	@OneToMany (mappedBy="planoAcompanhamento", cascade = CascadeType.ALL)
	private List<MetaPlanoFamiliar> metas;
	
	@ManyToOne
	@JoinColumn(name="codigo_tecnico")
	private Usuario tecnico;
		
	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name="TecnicoPlano", joinColumns={@JoinColumn(name="codigo_plano")}, 
    									inverseJoinColumns={@JoinColumn(name="codigo_tecnico")})
	private List<Usuario> tecnicos;
	
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
