package com.softarum.svsa.service;

import com.softarum.svsa.modelo.RmaCras;
import com.softarum.svsa.modelo.RmaCreas;
import com.softarum.svsa.modelo.RmaPop;
import com.softarum.svsa.modelo.Vitima;
import com.softarum.svsa.modelo.to.rma.RmaCreasTO;
import com.softarum.svsa.modelo.to.rma.RmaPopTO;
import com.softarum.svsa.modelo.to.rma.RmaTO;
import com.softarum.svsa.modelo.to.rma.VitimaCreasTO;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Getter
@Setter
public class RmaAssembler {
	
	
	/*
	 * CRAS
	 */
	
	
	public RmaCras toCrasEntity(RmaTO rmaTO) {
		return modelMapper(rmaTO);
	}	
	public RmaTO toRmaTO(RmaCras rmaCras) {
		return modelMapper(rmaCras);
	}
	private RmaCras modelMapper(RmaTO rmaTO){
		
		RmaCras rmaCras = new RmaCras();
		
		log.info("RmaAsembler fazendo o mapeamento de TO para Entity...");
		log.info(rmaTO.getMesAnoReferencia());
		log.info(rmaTO.getUnidade().getNome());
		rmaCras.setMesAnoReferencia(rmaTO.getMesAnoReferencia());
		rmaCras.setUnidade(rmaTO.getUnidade());
		
		rmaCras.setBloco1a1(rmaTO.getBloco1().getA1());
		rmaCras.setBloco1a2(rmaTO.getBloco1().getA2());
		
		rmaCras.setBloco1b1(rmaTO.getBloco1().getB1());
		rmaCras.setBloco1b2(rmaTO.getBloco1().getB2());
		rmaCras.setBloco1b3(rmaTO.getBloco1().getB3());
		rmaCras.setBloco1b4(rmaTO.getBloco1().getB4());
		rmaCras.setBloco1b5(rmaTO.getBloco1().getB5());
		rmaCras.setBloco1b6(rmaTO.getBloco1().getB6());
		rmaCras.setBloco1b7(rmaTO.getBloco1().getB7());
		rmaCras.setBloco1b8(rmaTO.getBloco1().getB8());
		rmaCras.setBloco1b9(rmaTO.getBloco1().getB9());
		rmaCras.setBloco1b10(rmaTO.getBloco1().getB10());	
		
		rmaCras.setBloco2c1(rmaTO.getBloco2().getC1());
		rmaCras.setBloco2c2(rmaTO.getBloco2().getC2());
		rmaCras.setBloco2c3(rmaTO.getBloco2().getC3());
		rmaCras.setBloco2c4(rmaTO.getBloco2().getC4());
		rmaCras.setBloco2c5(rmaTO.getBloco2().getC5());
		rmaCras.setBloco2c6(rmaTO.getBloco2().getC6());
		rmaCras.setBloco2c7(rmaTO.getBloco2().getC7());
		rmaCras.setBloco2c8(rmaTO.getBloco2().getC8());
		rmaCras.setBloco2c9(rmaTO.getBloco2().getC9());
				
		rmaCras.setBloco3d1(rmaTO.getBloco3().getD1());
		rmaCras.setBloco3d2(rmaTO.getBloco3().getD2());
		rmaCras.setBloco3d3(rmaTO.getBloco3().getD3());
		rmaCras.setBloco3d4(rmaTO.getBloco3().getD4());
		rmaCras.setBloco3d5(rmaTO.getBloco3().getD5());
		rmaCras.setBloco3d6(rmaTO.getBloco3().getD6());
		rmaCras.setBloco3d7(rmaTO.getBloco3().getD7());
		rmaCras.setBloco3d8(rmaTO.getBloco3().getD8());
		rmaCras.setBloco3d9(rmaTO.getBloco3().getD9());		
		
		return rmaCras;
	}
	private RmaTO modelMapper(RmaCras rmaCras){
		
		RmaTO rmaTO = RmaTO.createRmaTO();
		
		log.info("RmaAsembler fazendo o mapeamento de Entity para TO...");
		log.info(rmaCras.getMesAnoReferencia());
		log.info(rmaCras.getUnidade().getNome());
		rmaTO.setMesAnoReferencia(rmaCras.getMesAnoReferencia());
		rmaTO.setUnidade(rmaCras.getUnidade());
		
		rmaTO.getBloco1().setA1(rmaCras.getBloco1a1());
		rmaTO.getBloco1().setA2(rmaCras.getBloco1a2());
		
		rmaTO.getBloco1().setB1(rmaCras.getBloco1b1());
		rmaTO.getBloco1().setB2(rmaCras.getBloco1b2());
		rmaTO.getBloco1().setB3(rmaCras.getBloco1b3());
		rmaTO.getBloco1().setB4(rmaCras.getBloco1b4());
		rmaTO.getBloco1().setB5(rmaCras.getBloco1b5());
		rmaTO.getBloco1().setB6(rmaCras.getBloco1b6());
		rmaTO.getBloco1().setB7(rmaCras.getBloco1b7());
		rmaTO.getBloco1().setB8(rmaCras.getBloco1b8());
		rmaTO.getBloco1().setB9(rmaCras.getBloco1b9());
		rmaTO.getBloco1().setB10(rmaCras.getBloco1b10());
		
		rmaTO.getBloco2().setC1(rmaCras.getBloco2c1());
		rmaTO.getBloco2().setC2(rmaCras.getBloco2c2());
		rmaTO.getBloco2().setC3(rmaCras.getBloco2c3());
		rmaTO.getBloco2().setC4(rmaCras.getBloco2c4());
		rmaTO.getBloco2().setC5(rmaCras.getBloco2c5());
		rmaTO.getBloco2().setC6(rmaCras.getBloco2c6());
		rmaTO.getBloco2().setC7(rmaCras.getBloco2c7());
		rmaTO.getBloco2().setC8(rmaCras.getBloco2c8());
		rmaTO.getBloco2().setC9(rmaCras.getBloco2c9());
		
		rmaTO.getBloco3().setD1(rmaCras.getBloco3d1());
		rmaTO.getBloco3().setD2(rmaCras.getBloco3d2());
		rmaTO.getBloco3().setD3(rmaCras.getBloco3d3());
		rmaTO.getBloco3().setD4(rmaCras.getBloco3d4());
		rmaTO.getBloco3().setD5(rmaCras.getBloco3d5());
		rmaTO.getBloco3().setD6(rmaCras.getBloco3d6());
		rmaTO.getBloco3().setD7(rmaCras.getBloco3d7());
		rmaTO.getBloco3().setD8(rmaCras.getBloco3d8());
		rmaTO.getBloco3().setD9(rmaCras.getBloco3d9());		
		
		return rmaTO;
	}
	
	
	/*
	 * CREAS
	 */
	
	
	public RmaCreas toCreasEntity(RmaCreasTO rmaCreasTO) {
		return modelMapper(rmaCreasTO);
	}	
	public RmaCreasTO toRmaCreasTO(RmaCreas rmaCreas) {
		return modelMapper(rmaCreas);
	}	
	private RmaCreas modelMapper(RmaCreasTO to){
		
		RmaCreas rma = RmaCreas.createRmaCreas();
		
		log.info("RmaAsembler fazendo o mapeamento de TO para Entity...");
		log.info(to.getMesAnoReferencia());
		log.info(to.getUnidade().getNome());
		rma.setMesAnoReferencia(to.getMesAnoReferencia());
		rma.setUnidade(to.getUnidade());
		
		// bloco1
		rma.setBloco1a1(to.getBloco1().getA1());
		rma.setBloco1a2(to.getBloco1().getA2());
		
		rma.setBloco1b1(to.getBloco1().getB1());
		rma.setBloco1b2(to.getBloco1().getB2());
		rma.setBloco1b3(to.getBloco1().getB3());
		rma.setBloco1b4(to.getBloco1().getB4());
		rma.setBloco1b5(to.getBloco1().getB5());
		rma.setBloco1b6(to.getBloco1().getB6());
		rma.setBloco1b7(to.getBloco1().getB7());
		
		rma.setBloco1c1(to.getBloco1().getC1());
		rma.setBloco1c2(to.getBloco1().getC2());
		rma.setBloco1c3(to.getBloco1().getC3());
		rma.setBloco1c4(to.getBloco1().getC4());
		rma.setBloco1c5(to.getBloco1().getC5());
		rma.setBloco1c6(to.getBloco1().getC6());
		
		rma.setBloco1d1(to.getBloco1().getD1());
		rma.setBloco1d2(to.getBloco1().getD2());
		
		rma.setBloco1e1(to.getBloco1().getE1());
		rma.setBloco1e2(to.getBloco1().getE2());
		
		rma.setBloco1f1(to.getBloco1().getF1());		
		rma.setBloco1g1(to.getBloco1().getG1());		
		rma.setBloco1h1(to.getBloco1().getH1());		
		rma.setBloco1i1(to.getBloco1().getI1());
		
		// bloco 2
		rma.setBloco2m1(to.getBloco2().getM1());
		rma.setBloco2m2(to.getBloco2().getM2());
		rma.setBloco2m3(to.getBloco2().getM3());
		rma.setBloco2m4(to.getBloco2().getM4());
		
		// bloco3
		rma.setBloco3j1(to.getBloco3().getJ1());
		rma.setBloco3j2(to.getBloco3().getJ2());
		rma.setBloco3j3(to.getBloco3().getJ3());
		rma.setBloco3j4(to.getBloco3().getJ4());
		rma.setBloco3j5(to.getBloco3().getJ5());
		rma.setBloco3j6(to.getBloco3().getJ6());
		
		// vitimas
		// bloco1
		rma.setB6vitimaFem(modelMapper(to.getBloco1().getB6vitimasFem()));
		rma.setB6vitimaMasc(modelMapper(to.getBloco1().getB6vitimasMasc()));
		
		rma.setC1vitimaFem(modelMapper(to.getBloco1().getC1vitimasFem()));
		rma.setC1vitimaMasc(modelMapper(to.getBloco1().getC1vitimasMasc()));
		rma.setC2vitimaFem(modelMapper(to.getBloco1().getC2vitimasFem()));
		rma.setC2vitimaMasc(modelMapper(to.getBloco1().getC2vitimasMasc()));
		rma.setC3vitimaFem(modelMapper(to.getBloco1().getC3vitimasFem()));
		rma.setC3vitimaMasc(modelMapper(to.getBloco1().getC3vitimasMasc()));
		rma.setC4vitimaFem(modelMapper(to.getBloco1().getC4vitimasFem()));
		rma.setC4vitimaMasc(modelMapper(to.getBloco1().getC4vitimasMasc()));
		rma.setC5vitimaFem(modelMapper(to.getBloco1().getC5vitimasFem()));
		rma.setC5vitimaMasc(modelMapper(to.getBloco1().getC5vitimasMasc()));
		rma.setC6vitimaFem(modelMapper(to.getBloco1().getC6vitimasFem()));
		rma.setC6vitimaMasc(modelMapper(to.getBloco1().getC6vitimasMasc()));
		
		rma.setD1vitimaFem(modelMapper(to.getBloco1().getD1vitimasMasc()));
		rma.setD1vitimaMasc(modelMapper(to.getBloco1().getD1vitimasMasc()));
		rma.setD2vitimaFem(modelMapper(to.getBloco1().getD2vitimasFem()));
		rma.setD2vitimaMasc(modelMapper(to.getBloco1().getD2vitimasMasc()));
		
		rma.setE1vitimaFem(modelMapper(to.getBloco1().getE1vitimasFem()));
		rma.setE1vitimaMasc(modelMapper(to.getBloco1().getE1vitimasMasc()));
		rma.setE2vitimaFem(modelMapper(to.getBloco1().getE2vitimasFem()));
		rma.setE2vitimaMasc(modelMapper(to.getBloco1().getE2vitimasMasc()));
		
		rma.setF1vitimaFem(modelMapper(to.getBloco1().getF1vitimasFem()));
				
		rma.setG1vitimaFem(modelMapper(to.getBloco1().getG1vitimasFem()));
		rma.setG1vitimaMasc(modelMapper(to.getBloco1().getG1vitimasMasc()));
		rma.setI1vitimaFem(modelMapper(to.getBloco1().getI1vitimasFem()));
		rma.setI1vitimaMasc(modelMapper(to.getBloco1().getI1vitimasMasc()));
		
		// bloco3
		rma.setJ4vitimaFem(modelMapper(to.getBloco3().getJ4vitimasFem()));
		rma.setJ4vitimaMasc(modelMapper(to.getBloco3().getJ4vitimasMasc()));
		rma.setJ5vitimaFem(modelMapper(to.getBloco3().getJ5vitimasFem()));
		rma.setJ5vitimaMasc(modelMapper(to.getBloco3().getJ5vitimasMasc()));
		rma.setJ6vitimaFem(modelMapper(to.getBloco3().getJ6vitimasFem()));
		rma.setJ6vitimaMasc(modelMapper(to.getBloco3().getJ6vitimasMasc()));
	
		return rma;
	}
	private RmaCreasTO modelMapper(RmaCreas rma){
		
		RmaCreasTO to = RmaCreasTO.createRmaCreasTO();
		
		log.info("RmaAsembler fazendo o mapeamento de Entity para TO...");
		log.info(rma.getMesAnoReferencia());
		log.info(rma.getUnidade().getNome());
		to.setMesAnoReferencia(rma.getMesAnoReferencia());
		to.setUnidade(rma.getUnidade());
		
		// bloco1
		to.getBloco1().setA1(rma.getBloco1a1());
		to.getBloco1().setA2(rma.getBloco1a2());
		to.getBloco1().setB1(rma.getBloco1b1());
		to.getBloco1().setB2(rma.getBloco1b2());
		to.getBloco1().setB3(rma.getBloco1b3());
		to.getBloco1().setB4(rma.getBloco1b4());
		to.getBloco1().setB5(rma.getBloco1b5());
		to.getBloco1().setB6(rma.getBloco1b6());
		to.getBloco1().setB7(rma.getBloco1b7());
		
		to.getBloco1().setC1(rma.getBloco1c1());
		to.getBloco1().setC2(rma.getBloco1c2());
		to.getBloco1().setC3(rma.getBloco1c3());
		to.getBloco1().setC4(rma.getBloco1c4());
		to.getBloco1().setC5(rma.getBloco1c5());
		to.getBloco1().setC6(rma.getBloco1c6());
		
		to.getBloco1().setD1(rma.getBloco1d1());
		to.getBloco1().setD2(rma.getBloco1d2());
		
		to.getBloco1().setE1(rma.getBloco1e1());
		to.getBloco1().setE2(rma.getBloco1e2());
		
		to.getBloco1().setF1(rma.getBloco1f1());
		to.getBloco1().setG1(rma.getBloco1g1());
		to.getBloco1().setH1(rma.getBloco1h1());
		to.getBloco1().setI1(rma.getBloco1i1());		
		
		// bloco 2
		to.getBloco2().setM1(rma.getBloco2m1());
		to.getBloco2().setM2(rma.getBloco2m2());
		to.getBloco2().setM3(rma.getBloco2m3());
		to.getBloco2().setM4(rma.getBloco2m4());		
		
		
		// bloco3
		to.getBloco3().setJ1(rma.getBloco3j1());
		to.getBloco3().setJ2(rma.getBloco3j2());
		to.getBloco3().setJ3(rma.getBloco3j3());
		to.getBloco3().setJ4(rma.getBloco3j4());
		to.getBloco3().setJ5(rma.getBloco3j5());
		to.getBloco3().setJ6(rma.getBloco3j6());
		
		
		// vitimas
		// bloco1
		to.getBloco1().setB6vitimasFem(modelMapper(rma.getB6vitimaFem()));
		to.getBloco1().setB6vitimasMasc(modelMapper(rma.getB6vitimaMasc()));
		
		to.getBloco1().setC1vitimasFem(modelMapper(rma.getC1vitimaFem()));
		to.getBloco1().setC1vitimasMasc(modelMapper(rma.getC1vitimaMasc()));
		to.getBloco1().setC2vitimasFem(modelMapper(rma.getC2vitimaFem()));
		to.getBloco1().setC2vitimasMasc(modelMapper(rma.getC2vitimaMasc()));
		to.getBloco1().setC3vitimasFem(modelMapper(rma.getC3vitimaFem()));
		to.getBloco1().setC3vitimasMasc(modelMapper(rma.getC3vitimaMasc()));
		to.getBloco1().setC4vitimasFem(modelMapper(rma.getC4vitimaFem()));
		to.getBloco1().setC4vitimasMasc(modelMapper(rma.getC4vitimaMasc()));
		to.getBloco1().setC5vitimasFem(modelMapper(rma.getC5vitimaFem()));
		to.getBloco1().setC5vitimasMasc(modelMapper(rma.getC5vitimaMasc()));
		to.getBloco1().setC6vitimasFem(modelMapper(rma.getC6vitimaFem()));
		to.getBloco1().setC6vitimasMasc(modelMapper(rma.getC6vitimaMasc()));
		
		to.getBloco1().setD1vitimasFem(modelMapper(rma.getD1vitimaFem()));
		to.getBloco1().setD1vitimasMasc(modelMapper(rma.getD1vitimaMasc()));
		to.getBloco1().setD2vitimasFem(modelMapper(rma.getD2vitimaFem()));
		to.getBloco1().setD2vitimasMasc(modelMapper(rma.getD2vitimaMasc()));
		
		to.getBloco1().setE1vitimasFem(modelMapper(rma.getE1vitimaFem()));
		to.getBloco1().setE1vitimasMasc(modelMapper(rma.getE1vitimaMasc()));
		to.getBloco1().setE2vitimasFem(modelMapper(rma.getE2vitimaFem()));
		to.getBloco1().setE2vitimasMasc(modelMapper(rma.getE2vitimaMasc()));
		
		to.getBloco1().setF1vitimasFem(modelMapper(rma.getF1vitimaFem()));
				
		to.getBloco1().setG1vitimasFem(modelMapper(rma.getG1vitimaFem()));
		to.getBloco1().setG1vitimasMasc(modelMapper(rma.getG1vitimaMasc()));
		
		to.getBloco1().setI1vitimasFem(modelMapper(rma.getI1vitimaFem()));
		to.getBloco1().setI1vitimasMasc(modelMapper(rma.getI1vitimaMasc()));
			
		return to;
	}
		
	
	/*
	 * POP
	 */
	
	
	public RmaPop toPopEntity(RmaPopTO RmaPopTO) {
		return modelMapper(RmaPopTO);
	}	
	public RmaPopTO toRmaPopTO(RmaPop RmaPop) {
		return modelMapper(RmaPop);
	}	
	private RmaPop modelMapper(RmaPopTO to){
		
		RmaPop rma = RmaPop.createRmaPop();
		
		log.info("RmaAsembler fazendo o mapeamento de TO para Entity...");
		log.info(to.getMesAnoReferencia());
		log.info(to.getUnidade().getNome());
		rma.setMesAnoReferencia(to.getMesAnoReferencia());
		rma.setUnidade(to.getUnidade());
		
		//bloco1
		rma.setA1(to.getBloco1().getA1());
		rma.setB1(to.getBloco1().getB1());
		rma.setB2(to.getBloco1().getB2());
		rma.setB3(to.getBloco1().getB3());
		rma.setB4(to.getBloco1().getB4());
		rma.setC1(to.getBloco1().getC1());
		rma.setC2(to.getBloco1().getC2());
		rma.setD1(to.getBloco1().getD1());
		rma.setA1vitimaFem(modelMapper(to.getBloco1().getA1vitimasFem()));
		rma.setA1vitimaMasc(modelMapper(to.getBloco1().getA1vitimasMasc()));
		//bloco2
		rma.setE1(to.getBloco2().getE1());
		rma.setE2(to.getBloco2().getE2());
		rma.setE3(to.getBloco2().getE3());
		rma.setE4(to.getBloco2().getE4());
		rma.setE5(to.getBloco2().getE5());
		rma.setE6(to.getBloco2().getE6());
		rma.setE7(to.getBloco2().getE7());
		rma.setF1(to.getBloco2().getF1());		
		rma.setE1vitimaFem(modelMapper(to.getBloco2().getE1vitimasFem()));
		rma.setE1vitimaMasc(modelMapper(to.getBloco2().getE1vitimasMasc()));
		
		
	
		return rma;
	}
	private RmaPopTO modelMapper(RmaPop rma){
		
		RmaPopTO to = RmaPopTO.createRmaPopTO();
		
		log.info("RmaAsembler fazendo o mapeamento de Entity para TO...");
		log.info(rma.getMesAnoReferencia());
		log.info(rma.getUnidade().getNome());
		to.setMesAnoReferencia(rma.getMesAnoReferencia());
		to.setUnidade(rma.getUnidade());
		
		// bloco1
		to.getBloco1().setA1(rma.getA1());
		to.getBloco1().setB1(rma.getB1());
		to.getBloco1().setB2(rma.getB2());
		to.getBloco1().setB3(rma.getB3());
		to.getBloco1().setB4(rma.getB4());
		to.getBloco1().setC1(rma.getC1());
		to.getBloco1().setC2(rma.getC2());
		to.getBloco1().setD1(rma.getD1());		
		to.getBloco1().setA1vitimasFem(modelMapper(rma.getA1vitimaFem()));
		to.getBloco1().setA1vitimasMasc(modelMapper(rma.getA1vitimaMasc()));
		//bloco2
		to.getBloco2().setE1(rma.getE1());
		to.getBloco2().setE2(rma.getE2());
		to.getBloco2().setE3(rma.getE3());
		to.getBloco2().setE4(rma.getE4());
		to.getBloco2().setE5(rma.getE5());
		to.getBloco2().setE6(rma.getE6());
		to.getBloco2().setE7(rma.getE7());
		to.getBloco2().setF1(rma.getF1());
		to.getBloco2().setE1vitimasFem(modelMapper(rma.getE1vitimaFem()));
		to.getBloco2().setE1vitimasMasc(modelMapper(rma.getE1vitimaMasc()));		
			
		return to;
	}
	
	
	/*
	 * VITIMAS
	 */
	
	private Vitima modelMapper(VitimaCreasTO to){
		
		Vitima vitima = new Vitima();
		vitima.setSexo(to.getSexo());
		vitima.setIdade0a6(to.getIdade0a6());
		vitima.setIdade0a12(to.getIdade0a12());
		vitima.setIdade7a12(to.getIdade7a12());
		vitima.setIdade13a17(to.getIdade13a17());
		vitima.setIdade18a59(to.getIdade18a59());
		vitima.setIdade60mais(to.getIdade60mais());
		vitima.setTotal(to.getTotal());
			
		return vitima;
	}
	private VitimaCreasTO modelMapper(Vitima vitima){
		
		VitimaCreasTO to = new VitimaCreasTO();
		to.setSexo(vitima.getSexo());
		to.setIdade0a6(vitima.getIdade0a6());
		to.setIdade0a12(vitima.getIdade0a12());
		to.setIdade7a12(vitima.getIdade7a12());
		to.setIdade13a17(vitima.getIdade13a17());
		to.setIdade18a59(vitima.getIdade18a59());
		to.setIdade60mais(vitima.getIdade60mais());
		to.setTotal(vitima.getTotal());
			
		return to;
	}
}
