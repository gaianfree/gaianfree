package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import com.softarum.svsa.modelo.enums.CodigoEncaminhamento;
import com.softarum.svsa.service.s3.AmazonS3Keys;

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
	
	/* consultas de oficios recebidos */
	@NamedQuery(name="Oficio.buscarTodos", query="select o from Oficio o where o.tenant_id = :tenantId "),
	@NamedQuery(name="Oficio.buscarTodosOficios", query="select o from Oficio o "
			+ "where o.unidade = :unidade "
			+ "and o.tenant_id = :tenantId"),
	@NamedQuery(name="Oficio.buscarOficiosRecebidos", query="select o from Oficio o "
			+ "where o.dataResposta is null "
			+ "and o.tenant_id = :tenantId "
			+ "and o.resposta = true "
			+ "and o.unidade = :unidade "
			+ "order by o.dataRecebimento"),
	
	/* usado para Histórico da pessoa */
	@NamedQuery(name="Oficio.buscarOficiosHist", query="select o from Oficio o "
			+ "where o.pessoa = :pessoa "
			+ "and o.tenant_id = :tenantId"),
	
	/* Consulta para verificar ofícios enviados pela SASC */
	@NamedQuery(name="Oficio.buscarTodosOficiosSasc", query="select o from Oficio o "
			+ "where o.sasc is not null "
			+ "and o.tenant_id = :tenantId"),
	@NamedQuery(name="Oficio.buscarOficiosSasc", query="select o from Oficio o "
			+ "where o.unidade = :unidade "
			+ "and o.tenant_id = :tenantId "
			+ "and o.sasc is not null"),
	@NamedQuery(name="Oficio.buscarOficiosSascPeriodo", query="select o from Oficio o "
			+ "where o.dataRecebimento between :ini and :fim "
			+ "and o.unidade = :unidade "
			+ "and o.tenant_id = :tenantId "
			+ "and o.sasc is not null"),
	@NamedQuery(name="Oficio.buscarOficiosUnidade", query="select o from Oficio o "
			+ "where o.unidade = :unidade "
			+ "and o.tenant_id = :tenantId"),
	@NamedQuery(name="Oficio.buscarOficiosUnidadePeriodo", query="select o from Oficio o "
			+ "where o.dataRecebimento between :ini and :fim "
			+ "and o.unidade = :unidade "
			+ "and o.tenant_id = :tenantId"),
	
	/* Buscas de gráficos */
	@NamedQuery(name="Oficio.buscarOfGraficoUnidade", query="select o from Oficio o "
			+ "where o.unidade = :unidade "
			+ "and o.tenant_id = :tenantId "
			+ "order by o.codigoEncaminhamento"),
	@NamedQuery(name="Oficio.buscarOfGraficoPeriodo", query="select o from Oficio o "
			+ "where o.unidade = :unidade "
			+ "and o.tenant_id = :tenantId "
			+ "and o.dataRecebimento between :ini and :fim "
			+ "order by o.codigoEncaminhamento"),
	@NamedQuery(name="Oficio.buscarOfGraficoSasc", query="select o from Oficio o "
			+ "where o.unidade = :unidade "
			+ "and o.tenant_id = :tenantId "
			+ "and o.sasc is not null"),
	@NamedQuery(name="Oficio.buscarOfGraficoSascPeriodo", query="select o from Oficio o "
			+ "where o.unidade = :unidade "
			+ "and o.tenant_id = :tenantId "
			+ "and o.dataRecebimento between :ini and :fim "
			+ "and o.sasc is not null"),
	
	@NamedQuery(name="Oficio.buscarOficiosPendentes", query="select o from Oficio o "
			+ "where o.unidade = :unidade "
			+ "and o.tenant_id = :tenantId "
			+ "and o.resposta = true "
			+ "and o.dataResposta is null "
			+ "order by o.dataRecebimento")
	/* usado para ofícios da sasc */
	
	
})
public class Oficio implements Serializable {


	private static final long serialVersionUID = 1L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	private Long tenant_id;
	
	@ToString.Include
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataRecebimento;
	
	@ToString.Include
	@NotBlank(message="O número do ofício é obrigatório.")
	@Column(unique=true)
	private String nrOficio;
	
	@ToString.Include
	private String nrProcesso;
	
	@ToString.Include
	@Column(length = 512000,columnDefinition="Text")
	private String assunto;
	
	@ToString.Include
	private String nomeOrgao;
	
	@ToString.Include
	private String endereco;
	
	@ToString.Include
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataResposta;
	
	@ToString.Include
	@Column(unique=true)
	private String nrOficioResp;
	
	private String s3Key = null;
	
	private Boolean sasc;
	
	private Boolean resposta = true;

	@Enumerated(EnumType.STRING)
	private CodigoEncaminhamento codigoEncaminhamento;	

	@ManyToOne
	@JoinColumn(name="codigo_coordenador")
	private Usuario coordenador;
	
	@ManyToOne
	@JoinColumn(name="codigo_tecnico")
	private Usuario tecnico;
	
	@ManyToOne
	@JoinColumn(name="codigo_unidade")
	private Unidade unidade;
	
	@ManyToOne
	@JoinColumn(name="codigo_pessoa")
	private Pessoa pessoa;
	
	@ManyToOne
	@JoinColumn(name="codigo_oficio_emitido")
	private OficioEmitido oficioEmitido;
	
	@Transient
	public String getUrlAnexo() {
		return AmazonS3Keys.URL_S3_BUCKET + getS3Key();
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
