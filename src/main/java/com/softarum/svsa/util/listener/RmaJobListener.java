package com.softarum.svsa.util.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.softarum.svsa.util.jobs.RmaCronTrigger;

import lombok.extern.log4j.Log4j;

@Log4j
//@WebListener
public class RmaJobListener implements ServletContextListener {
	
	@Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("RmaListener Starting up!");
        try {
            RmaCronTrigger.start();
            log.info("RmaListener (RMA) started!");
        } catch (Exception e) {
        	log.error("RmaListener (RMA) NOT started!");
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("RmaListener Shutting down!");
    }
}


/*
@Log4j
@Named(eager=true)
@ApplicationScoped
public class RmaJobListener implements ServletContextListener {
	
	 @PostConstruct
	    public void init() {
        log.info("RmaListener Starting up!");
        try {
            RmaCronTrigger.start();
            log.info("RmaListener (RMA) started!");
        } catch (Exception e) {
        	log.error("RmaListener (RMA) NOT started!");
            e.printStackTrace();
        }
    }

	 @PreDestroy
	    public void destroy() {
        log.info("RmaListener Shutting down!");
    }
}
*/