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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.softarum.svsa.modelo.enums.TipoDeficiencia;

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
	@NamedQuery(name="CondicaoSaude.obterCondicaoSaude", query="select c from CondicaoSaude c where "
			+ "c.pessoa = :pessoa "
			+ "and c.tenant_id = :tenantId")	
})
public class CondicaoSaude implements Serializable {


	private static final long serialVersionUID = -3276288201376060553L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	private Long tenant_id;
	private boolean pessoaNecessitada;
	private boolean insuficienciaAlimentar;
	private boolean portadorDoencaGrave;
	private boolean usoRemedioControlado;
	private boolean usoAbusivoAlcool;
	private boolean usoAbusivoDroga;
	private String responsavelPeloCuidado;
	private boolean referenciaSaude;
	private String local;
	private Integer qtdMesesGestacao;
	private Boolean iniciouPreNatal;

	/* relacionamentos */
	@OneToOne
	private Pessoa pessoa;
	
	@ManyToOne
	@JoinColumn(name="codigo_tecnico")
	private Usuario tecnico;
	
	/* Enums */
	@Enumerated(EnumType.STRING)
	private TipoDeficiencia tipoDeficiencia;
	
	public Boolean ehDeficiente() {
		if (tipoDeficiencia != null) {
			return true;
		}
		return false;
	}
	
	public Boolean ehGestante() {
		if (qtdMesesGestacao != null || iniciouPreNatal == true) {
			return true;
		}
		return false;
	}
	
	public Boolean ehCuidadoConstante() {
		if (responsavelPeloCuidado != "") {
			return true;
		}
		return false;
	}

	
	public boolean isPessoaNecessitada() {
		return pessoaNecessitada;
	}


	public boolean isInsuficienciaAlimentar() {
		return insuficienciaAlimentar;
	}


	public boolean isPortadorDoencaGrave() {
		return portadorDoencaGrave;
	}


	public boolean isUsoRemedioControlado() {
		return usoRemedioControlado;
	}

	public boolean isUsoAbusivoAlcool() {
		return usoAbusivoAlcool;
	}

	public boolean isUsoAbusivoDroga() {
		return usoAbusivoDroga;
	}

	public boolean isReferenciaSaude() {
		return referenciaSaude;
	}

	// Relacionamentos
	
	
	
	@Enumerated(EnumType.STRING)
	public TipoDeficiencia getTipoDeficiencia() {
		return tipoDeficiencia;
	}

	public void setTipoDeficiencia(TipoDeficiencia tipoDeficiencia) {
		this.tipoDeficiencia = tipoDeficiencia;
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