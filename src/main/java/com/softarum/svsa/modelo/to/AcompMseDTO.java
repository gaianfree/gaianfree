package com.softarum.svsa.modelo.to;

import java.io.Serializable;
import java.util.Date;

import com.softarum.svsa.modelo.enums.TipoMse;

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
public class AcompMseDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long codigo;
	private String nrProcesso;
	private Date dataEncaminhamento;
	private Date dataHomologacao;
	private TipoMse tipoMse;
	private String responsavel;
	private String adolescente;

	
	public AcompMseDTO() {}
	
	public AcompMseDTO(Long codigo, String nrProcesso, Date dataEncaminhamento, Date dataHomologacao,
			TipoMse tipoMse, String responsavel, String adolescente) {
		
		this.codigo = codigo;
		this.nrProcesso = nrProcesso;
		this.dataEncaminhamento = dataEncaminhamento;
		this.dataHomologacao = dataHomologacao;
		this.setTipoMse(tipoMse);
		this.responsavel = responsavel;
		this.adolescente = adolescente;

	}
}
	