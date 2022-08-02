package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
	@NamedQuery(name="RmaPop.buscarTodos", query="select rma from RmaPop rma where rma.unidade = :unidade and rma.tenant_id = :tenantId"),
	@NamedQuery(name="RmaPop.buscarRmaFechado", query="select rma from RmaPop rma "
			+ "where rma.unidade = :unidade "
			+ "and rma.mesAnoReferencia = :mesAnoRef "
			+ "and rma.tenant_id = :tenantId"),
	@NamedQuery(name="RmaPop.buscarRmasFechados", query="select rma.mesAnoReferencia from RmaPop rma "
			+ "where rma.unidade = :unidade "
			+ "and rma.tenant_id = :tenantId")
})
public class RmaPop implements Serializable {

	private static final long serialVersionUID = 82375949344894033L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@ManyToOne
	@JoinColumn(name="codigo_unidade")
	private Unidade unidade;
	
	private Long tenant_id;
	
	@NotBlank(message="O mes e ano de referencia é obrigatório")
	private String mesAnoReferencia;
	
	/*
	 * Bloco 1
	 */
	
	private Integer a1 = 0;  // Quantidade e perfil das pessoas em situação de rua atendida no mes de referência.
	private Integer b1 = 0;  // Pessoas usuárias de crack ou outras drogas ilícitas
	private Integer b2 = 0;  // Migrantes
	private Integer b3 = 0;  // Pessoas com doença ou transtorno mental
	private Integer b4 = 0;  // Usuárias de alcool	
	private Integer c1 = 0;  // Pessoas que foram incluídas no Cadastro Único para Programas Sociais, no mês.
	private Integer c2 = 0;  // Pessoas que realizaram atualização no Cadastro Único para Programas Sociais, no mês.
	private Integer d1 = 0;  // Quantidade total de atendimentos realizados (compreendida como a soma do número de atendimentos realizados a cada dia, durante o mês de referência)
	
	/*
	 * Bloco 2
	 */
	
	private Integer e1 = 0;  //E.1. Quantidade e perfil das pessoas abordadas pela equipe do Serviço de Abordagem, no mês de referência.
	private Integer e2 = 0;  //E.2. Crianças ou adolescentes em situação de trabalho infantil (até 15 anos)
	private Integer e3 = 0;  //E.3. Crianças ou adolescentes em situação de exploração sexual
	private Integer e4 = 0;  //E.4. Crianças ou adolescentes usuárias de crack ou outras drogras
	private Integer e5 = 0;  //E.5. Pessoas adultas usuárias de crack ou outras drogas ilícitas
	private Integer e6 = 0;  //E.6. Migrantes
	private Integer e7 = 0;  //E.7. Usuárias de alcool	
	private Integer f1 = 0;  //F.1. Quantidade total de abordagens realizadas (compreendida como o número de pessoas abordadas, multiplicado pelo número de vezes em que foram abordadas durante o mês)
	
	
	
	/* Vitimas */
	
	//a1
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_a1masc")
	private Vitima a1vitimaMasc;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_a1fem")
	private Vitima a1vitimaFem;	
	
	//e1
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_e1masc")
	private Vitima e1vitimaMasc;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_e1fem")
	private Vitima e1vitimaFem;	
	
	
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
	
	public static RmaPop createRmaPop() {
		
		RmaPop rma = new RmaPop();
		
		rma.setA1vitimaFem(new Vitima());
		rma.setA1vitimaMasc(new Vitima());
		
		rma.setE1vitimaFem(new Vitima());
		rma.setE1vitimaMasc(new Vitima());		
	
		return rma;
	}
}
