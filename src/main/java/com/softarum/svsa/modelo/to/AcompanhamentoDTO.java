package com.softarum.svsa.modelo.to;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe usada para projetar dados de familias em acompanhamento PAIF/PAEFI
 * Criada para otimizar as buscas das familias em acompanhamento
 * 
 * @author murakamiadmin
 *
 */
@Getter
@Setter
public class AcompanhamentoDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long codigo;
	private Date dataIngresso;
	private Integer prazoMeses;
	private String tecnicoResponsavel;
	private Long codigoProntuario;
	private String prontuarioFisico;
	private String pessoaReferencia;
	private Long codigoPessoa;
	private String bairro;
	
	
	public AcompanhamentoDTO() {}
	
	public AcompanhamentoDTO(Long codigo, Date dataIngresso, Integer prazoMeses, String tecnicoResponsavel, 
							Long codigoProntuario, String prontuarioFisico, String pessoaReferencia, String bairro) {
		this.codigo = codigo;
		this.dataIngresso = dataIngresso;
		this.prazoMeses = prazoMeses;
		this.tecnicoResponsavel = tecnicoResponsavel;
		this.codigoProntuario = codigoProntuario;
		this.prontuarioFisico = prontuarioFisico;
		this.pessoaReferencia = pessoaReferencia;
		this.bairro = bairro;
	}
	
	public AcompanhamentoDTO(Long codigo, Date dataIngresso, Integer prazoMeses, String tecnicoResponsavel,
			Long codigoProntuario, String prontuarioFisico, String pessoaReferencia, Long codigoPessoa, String bairro) {
		this.codigo = codigo;
		this.dataIngresso = dataIngresso;
		this.prazoMeses = prazoMeses;
		this.tecnicoResponsavel = tecnicoResponsavel;
		this.codigoProntuario = codigoProntuario;
		this.prontuarioFisico = prontuarioFisico;
		this.pessoaReferencia = pessoaReferencia;
		this.codigoPessoa = codigoPessoa;
		this.bairro = bairro;
	}
	
	public AcompanhamentoDTO(Long codigo, String pessoa, Date dataEncaminhamento, String tecnicoResponsavel) {
		this.codigo = codigo;
		this.pessoaReferencia = pessoa;
		this.dataIngresso = dataEncaminhamento;		
		this.tecnicoResponsavel = tecnicoResponsavel;		
	}

}
	