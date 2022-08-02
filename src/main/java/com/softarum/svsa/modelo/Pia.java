package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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

import com.softarum.svsa.modelo.enums.RazaoDesligamento;
import com.softarum.svsa.modelo.enums.StatusPia;
import com.softarum.svsa.modelo.enums.TipoMse;

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
	@NamedQuery(name="Pia.buscarTodos", query="select p from Pia p where p.tenant_id = :tenantId"),
	@NamedQuery(name="Pia.buscarPlanosAno", query="select p from Pia p "
			+ "where p.adolescente.codigo = :pessoa and p.ano = :ano "
			+ "and p.tenant_id = :tenantId "
			+ "order by p.dataDesligamento ASC" ),
	@NamedQuery(name="Pia.buscarPlanos", query="select p from Pia p "
			+ "where p.adolescente = :pessoa "
			+ "and p.tenant_id = :tenantId "
			+ "order by p.dataDesligamento ASC" )
})
public class Pia implements Serializable {

	private static final long serialVersionUID = -7649948038035071003L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	private Long tenant_id;
	
	private Integer ano;
	
	private String nrProcesso;
	
	@Column(length = 512000,columnDefinition="Text")
	@Basic(fetch=FetchType.LAZY)
	private String avalInterdisciplinar;
	
	@Column(length = 512000,columnDefinition="Text")
	@Basic(fetch=FetchType.LAZY)
	private String responsAdolescente;
	
	@Column(length = 512000,columnDefinition="Text")
	@Basic(fetch=FetchType.LAZY)
	private String responsFamilia;
	
	@Column(length = 512000,columnDefinition="Text")
	@Basic(fetch=FetchType.LAZY)
	private String responsEquipe;
	
	/* preenchido apenas qdo o tipoMse for PSC */
	private String localPSC;
	
	private String responsavelExterno;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEncaminhamento;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataHomologacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataDesligamento;	

	@Enumerated(EnumType.STRING)
	private RazaoDesligamento razaoDesligamento;
	
	@Enumerated(EnumType.STRING)
	private TipoMse tipoMse;
	
	@Enumerated(EnumType.STRING)
	private StatusPia situacao = StatusPia.ATIVO;
	
	private Boolean reincidente = false;
	
	/* relacionamentos */
	@ManyToOne
	@JoinColumn(name="codigo_pessoa")
	private Pessoa adolescente;
	
	//private List<AvaliacaoPia> avaliacoes;  // retirado
	@OneToMany (mappedBy="pia", cascade = CascadeType.ALL)
	private List<MetaPia> metas;
	
	@ManyToOne
	@JoinColumn(name="codigo_responsavel")
	private Usuario responsavel;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name="TecnicoPia", joinColumns={@JoinColumn(name="codigo_pia")}, 
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
