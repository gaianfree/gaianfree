package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.softarum.svsa.modelo.enums.AcessoEnergiaEletrica;
import com.softarum.svsa.modelo.enums.ColetaLixo;
import com.softarum.svsa.modelo.enums.EscoamentoSanitario;
import com.softarum.svsa.modelo.enums.FormaAbastecimento;
import com.softarum.svsa.modelo.enums.MaterialParede;
import com.softarum.svsa.modelo.enums.PossuiAcessibilidade;
import com.softarum.svsa.modelo.enums.TipoResidencia;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author gabrielrodrigues  - refactored by Murakami
 *
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@NamedQueries({	
	@NamedQuery(name="CondicaoHabitacional.obterCondicaoHabitacional", query="select c from CondicaoHabitacional c where "
			+ "c.prontuario = :prontuario "
			+ "and c.tenant_id = :tenantId")	
})
public class CondicaoHabitacional implements Serializable {


	private static final long serialVersionUID = -3276288201376060553L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;

	private Long tenant_id;
	//--
	private Integer qtdComodo;
	private Integer qtdDormitorio;
	private Boolean aguaCanalizada;
	private Boolean riscoDesabamento;
	private Boolean dificilAcessoDemografico;
	private Boolean areaViolenta;

	/* Enums */
	@Enumerated(EnumType.STRING)
	private TipoResidencia tipoResidencia;
	
	@Enumerated(EnumType.STRING)
	private FormaAbastecimento formaAbastecimento;
	
	@Enumerated(EnumType.STRING)
	private AcessoEnergiaEletrica acessoEnergiaEletrica;
	
	@Enumerated(EnumType.STRING)
	private MaterialParede materialParede;
	
	@Enumerated(EnumType.STRING)
	private ColetaLixo coletaLixo;
	
	@Enumerated(EnumType.STRING)
	private PossuiAcessibilidade possuiAcessibilidade;
	
	@Enumerated(EnumType.STRING)
	private EscoamentoSanitario escoamentoSanitario;
	
	/* relacionamentos */
	@OneToOne
	private Prontuario prontuario;
	
	@ManyToOne
	@JoinColumn(name="codigo_tecnico")
	private Usuario tecnico;
	
	@OneToMany (mappedBy="condicaoHabitacional", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<ObsCondicaoHabitacional> observacoes;
	
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