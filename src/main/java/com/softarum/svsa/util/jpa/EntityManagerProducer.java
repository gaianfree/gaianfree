package com.softarum.svsa.util.jpa;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

import org.apache.log4j.Logger;

//import org.apache.log4j.Logger;

/**
 * @author murakamiadmin/AlgaWorks curso
 *
 */
@ApplicationScoped
public class EntityManagerProducer implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final EntityManagerProducer INSTANCE = new EntityManagerProducer();
	
	@PersistenceUnit
	private EntityManagerFactory factory;
	
	private Logger log = Logger.getLogger(EntityManagerProducer.class);

	
	public EntityManagerProducer() {
		log.debug("EntityManagerProducer: SvsaPU");
		this.factory = Persistence.createEntityManagerFactory("svsaPU");
	}
	

	@Produces
	@RequestScoped
	public EntityManager create() {		
		log.debug("Criou o EntityManager");
		return getFactory().createEntityManager();
		
	}

	public void close(@Disposes EntityManager manager) {		
		if (manager.isOpen()) {
			manager.close();
			log.debug("Fechou o EntityManager");
		}		
	}
	
	public EntityManagerFactory getFactory() {
        return factory;
    }
	
	public static EntityManagerProducer getInstance() {
        return INSTANCE;
    }
	
}