package com.softarum.svsa.service;




import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.softarum.svsa.modelo.RmaCras;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.to.rma.Bloco1TO;
import com.softarum.svsa.modelo.to.rma.Bloco2TO;
import com.softarum.svsa.modelo.to.rma.Bloco3TO;
import com.softarum.svsa.modelo.to.rma.RmaTO;
import com.softarum.svsa.util.NegocioException;

import lombok.extern.log4j.Log4j;

@Log4j
public class RMAServiceTest {
	
	final RMAService service = new RMAService();
	final RmaAssembler rmaAssembler = new RmaAssembler();
	static RmaTO rmaTO = new RmaTO();
	static RmaCras rmaCras = new RmaCras();
	
	@BeforeAll
	static void setup() {
	    log.info("@BeforeAll - executes once before all test methods in this class");
	    
		
		final Unidade unidade = new Unidade();
		unidade.setNome("Unidade de Teste");
		
		rmaTO.setUnidade(unidade);
		
		final Bloco1TO bloco1 = new Bloco1TO();
		bloco1.setA1(23);
		bloco1.setB1(3);
		
		rmaTO.setBloco1(bloco1);
		
		final Bloco2TO bloco2 = new Bloco2TO();
		rmaTO.setBloco2(bloco2);
		final Bloco3TO bloco3 = new Bloco3TO();
		rmaTO.setBloco3(bloco3);
	}

	@Test
	public void testTOtoEntity() {		
		
		rmaCras = rmaAssembler.toCrasEntity(rmaTO);
		
		assertEquals(rmaTO.getBloco1().getA1(), rmaCras.getBloco1a1());
		assertEquals(rmaTO.getBloco1().getB1(), rmaCras.getBloco1b1());
		
	}
	
	@Test
	public void testEntitytoTO() {		
		
		rmaTO = rmaAssembler.toRmaTO(rmaCras);
		
		assertEquals(rmaCras.getBloco1a1(), rmaTO.getBloco1().getA1());
		assertEquals(rmaCras.getBloco1b1(), rmaTO.getBloco1().getB1());
		
	}
	
	@Test
	void shouldThrowException() {
	    Throwable exception = assertThrows(NegocioException.class, () -> {
	      throw new NegocioException("Erro de Negocio");
	    });
	    assertEquals(exception.getMessage(), "Erro de Negocio");
	}

	@Test
	void assertThrowsException() {
	    String str = null;
	    assertThrows(IllegalArgumentException.class, () -> {
	      Integer.valueOf(str);
	    });
	}
}
