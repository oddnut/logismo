/*
	Translator.java

	Author: David Fogel

	Copyright 2005-2009

	All rights reserved. 
 */

package net.logismo;

import java.util.*;

/**
 * Translator
 *
 * Comment here.  Author: David Fogel
 */
public class Translator {
	// *** Class Members ***

	// *** Instance Members ***
	
	private NameMatcher theMatcher;
	private ResourceBundle theResourceBundle;
	
	private List<Log> theMatchedLogs;
	
	private boolean isEnabled;
	private LogSystem theSystem;

	// *** Constructors ***
	
	public Translator(NameMatcher matcher, ResourceBundle resourceBundle) {
		theMatcher = matcher;
		theResourceBundle = resourceBundle;
		theMatchedLogs = new ArrayList<Log>();
		isEnabled = true;
		theSystem = null;
	}
	
	public Translator(NameMatcher matcher, String baseName, Locale locale, ClassLoader loader) 
		throws MissingResourceException {
		
		theMatcher = matcher;
		if (locale == null)
			locale = Locale.getDefault();
		if (loader == null)
			loader = this.getClass().getClassLoader();
		theResourceBundle = ResourceBundle.getBundle(baseName, locale, loader);
		isEnabled = true;
		theSystem = null;
	}

	// *** Interface Methods ***

	// *** Public Methods ***
	
	public NameMatcher getMatcher() {
		return theMatcher;
	}
	
	public ResourceBundle getResourceBundle() {
		return theResourceBundle;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public boolean isConnected() {
		return theSystem != null;
	}
	
	public List<Log> getMatchedLogs() {
		return Collections.unmodifiableList(theMatchedLogs);
	}
	
	public void setEnabled(boolean enabled) {
		
		synchronized(getLock()) {
			
			if (enabled == isEnabled)
				return;
			
			if (enabled) {
				
				Iterator<Log> i = theMatchedLogs.iterator();
				while(i.hasNext()) {
					Log l = (Log) i.next();
					l.activateTranslator(this);
				}
				isEnabled = true;
				
			} else {
				
				Iterator<Log> i = theMatchedLogs.iterator();
				while(i.hasNext()) {
					Log l = (Log) i.next();
					l.deactivateTranslator(this);
				}
				isEnabled = false;
				
			}
		}
	}

	// *** Protected Methods ***

	// *** Package Methods ***
	
	void connected(LogSystem system) {
		theSystem = system;
		
		Iterator<Log> i = theSystem.getLogs().values().iterator();
		while(i.hasNext()) {
			Log l = (Log) i.next();
			if (theMatcher.matchesName(l.getName())) {
				theMatchedLogs.add(l);
				if (isEnabled)
					l.activateTranslator(this);
			}
		}
	}
	
	void disconnected() {
		if (isEnabled) {
			Iterator<Log> i = theMatchedLogs.iterator();
			while(i.hasNext()) {
				Log log = (Log) i.next();
				log.deactivateTranslator(this);
			}
		}
		theMatchedLogs.clear();
		theSystem = null;
	}
	
	void logAdded(Log l) {
		if (theMatcher.matchesName(l.getName())) {
			theMatchedLogs.add(l);
			if (isEnabled)
				l.activateTranslator(this);
		}
	}

	// *** Private Methods ***
	
	private Object getLock() {
		return theSystem != null ? theSystem.getChangeLock() : this;
	}

	// *** Private Classes ***
}










/* end */