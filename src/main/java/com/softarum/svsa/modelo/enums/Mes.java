package com.softarum.svsa.modelo.enums;

/**
 * @author murakamiadmin
 *
 */
public enum Mes {
    JANEIRO(1),
    FEVEREIRO(2),
    MARÇO(3),
    ABRIL(4),
    MAIO(5),
    JUNHO(6),
    JULHO(7),
    AGOSTO(8),
    SETEMBRO(9),
    OUTUBRO(10),
    NOVEMBRO(11),
    DEZEMBRO(12);
	
	private final int codigo;

    Mes(int codigo) { 
    	this.codigo = codigo; 
    }

    int codigo() { 
    	return codigo; 
    }

    public static Mes porCodigo(int codigo) {
        for (Mes mes: Mes.values()) {
            if (codigo == mes.codigo()) 
            	return mes;
        }
        throw new IllegalArgumentException("codigo invalido");
    }

}
