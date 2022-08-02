package com.softarum.svsa.util.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import lombok.extern.log4j.Log4j;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.ArrayList;

@Log4j
@WebListener
public class SessionCounter implements HttpSessionListener {
    public static final String COUNTER = "SESSION-COUNTER";
    private final List<String> sessions = new ArrayList<>();

    public void sessionCreated(HttpSessionEvent event) {
        log.info("SessionCounter.sessionCreated");
        HttpSession session = event.getSession();
        sessions.add(session.getId());
        session.setAttribute(SessionCounter.COUNTER, this);
    }

    public void sessionDestroyed(HttpSessionEvent event) {
    	log.info("SessionCounter.sessionDestroyed");
        HttpSession session = event.getSession();
        sessions.remove(session.getId());
        session.setAttribute(SessionCounter.COUNTER, this);
    }

    public int getActiveSessionNumber() {
    	log.info("SessionCounter size = " + sessions.size());
        return sessions.size();
    }
}