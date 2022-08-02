package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.softarum.svsa.modelo.enums.ConvivenciaIntrafamiliar;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author gabrielrodrigues
 *
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@NamedQueries({	
	@NamedQuery(name="ConvivenciaFamiliar.obterConvivenciaFamiliar", query="select c from ConvivenciaFamiliar c where "
			+ "c.prontuario = :prontuario "
			+ "and c.tenant_id = :tenantId")
})
public class ConvivenciaFamiliar implements Serializable {

	private static final long serialVersionUID = -3276288201376060553L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	private Long tenant_id;
	private Integer tempoEstado;
	private Integer tempoMunicipio;
	private Integer tempoBairro;
	private boolean sempreMorouEstado;
	private boolean sempreMorouMunicipio;
	private boolean sempreMorouBairro;
	private boolean ameacaComunidade;
	private boolean possuiParenteProximo;
	private boolean possuiVizinhoSolidario;
	private boolean participaGrupoReligioso;
	private boolean participaOrganizacaoComunitaria;
	private boolean criancaSemAcessoLazer;
	private boolean idosoSemAcessoLazer;
	private boolean dpteSemCompanhiaDia;
	
	/* enums */
	private ConvivenciaIntrafamiliar relacaoConjugal;
	private ConvivenciaIntrafamiliar relacaoPaisFilhos;
	private ConvivenciaIntrafamiliar relacaoEntreIrmaos;
	
	/* relacionamentos */
	@OneToOne
	private Prontuario prontuario;
	
	@ManyToOne
	@JoinColumn(name="codigo_tecnico")
	private Usuario tecnico;
	
	@OneToMany (mappedBy="convivenciaFamiliar", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<ObsConvivenciaFamiliar> observacoes;
	
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