package com.softarum.svsa.modelo.to;

import java.io.Serializable;

import com.softarum.svsa.modelo.enums.CodigoAuxiliarAtendimento;
import com.softarum.svsa.modelo.enums.PerfilFamilia;
import com.softarum.svsa.modelo.enums.Role;
import com.softarum.svsa.modelo.enums.TipoServico;

import lombok.Getter;
import lombok.Setter;

/**
 * @author murakamiadmin
 *
 */
@Getter
@Setter
public class AtendimentoTO implements Serializable, Comparable<AtendimentoTO> {
	
	private static final long serialVersionUID = 1L;
	
	private String nome = "";	
	private String unidade = "";
	private Role role;
	private Long qdeAtendimentos = 0L;
	private Float percentual = 0.0f;
	private Float media = 0.0f;
	
	public AtendimentoTO() {}
	
	
	/* usado por DashBoard */
	public AtendimentoTO(CodigoAuxiliarAtendimento codAux) {		
		this.nome = codAux.name();		
	}
	public AtendimentoTO(TipoServico ts) {		
		this.nome = ts.name();		
	}
	public AtendimentoTO(PerfilFamilia pf) {		
		this.nome = pf.name();		
	}
	
	/* usado por ProdutividadeService */
	public AtendimentoTO(String nome, String unidade) {		
		this.nome = nome;	
		this.unidade = unidade;
	}
	public AtendimentoTO(String nome, String unidade, Role perfil) {		
		this.nome = nome;	
		this.unidade = unidade;
		this.role = perfil;
	}
	/* usado por ProdutividadeService */


	@Override
    public int compareTo(AtendimentoTO atend) {
        return this.getQdeAtendimentos().compareTo(atend.getQdeAtendimentos());
    }
	

}
