package com.softarum.svsa.util.cdi;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author murakamiadmin/AlgaWorks curso
 *
 */
public class CDIServiceLocator {
	
	private static BeanManager getBeanManager() {
		try {
			InitialContext initialContext = new InitialContext();
			return (BeanManager) initialContext.lookup("java:comp/env/BeanManager");
		} catch (NamingException e) {
			e.printStackTrace();
			throw new RuntimeException("Não pôde encontrar BeanManager no JNDI.");
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz) {
		BeanManager bm = getBeanManager();
		Set<Bean<?>> beans = (Set<Bean<?>>) bm.getBeans(clazz);

		if (beans == null || beans.isEmpty()) {
			return null;
		}

		Bean<T> bean = (Bean<T>) beans.iterator().next();

		CreationalContext<T> ctx = bm.createCreationalContext(bean);
		T o = (T) bm.getReference(bean, clazz, ctx);

		return o;
	}

}