package com.softarum.svsa.modelo.to;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatasIniFimTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Date ini;
	private Date fim;
	
}
