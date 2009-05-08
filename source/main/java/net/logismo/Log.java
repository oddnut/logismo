/*
	Log.java

	Author: David Fogel

	Copyright 2005-2009

	All rights reserved. 
 */

package net.logismo;

import org.slf4j.*;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;
import java.util.*;

/**
 * Log
 *
 * Comment here.  Author: David Fogel
 */
public class Log extends MarkerIgnoringBase implements Logger {
	// *** Class Members ***
	
	private static final long serialVersionUID = 1L;

	private static final int ARGS_NONE = 0;
	private static final int ARGS_ONE = 1;
	private static final int ARGS_TWO = 2;
	private static final int ARGS_ARRAY = 3;

	// *** Instance Members ***
	
	private List<Link> theActiveLinks;
	private List<Translator> theActiveTranslators;
	private Link[] theLinksCache;
	private Translator[] theTranslatorsCache;

	// *** Constructors ***
	
	Log(String name) {
		this.name = name;
		theActiveLinks = new ArrayList<Link>();
		theActiveTranslators = new ArrayList<Translator>();
		theLinksCache = null;
		theTranslatorsCache = null;
	}

	// *** Logger Methods ***
	
	public boolean isTraceEnabled() {
		return isLevelEnabled(Level.DEBUG);
	}
	
	public void trace(String msg) {
		log(Level.DEBUG, msg, ARGS_NONE, null, null, null);
	}
	
	public void trace(String format, Object arg) {
		log(Level.DEBUG, format, ARGS_ONE, arg, null, null);
	}

	public void trace(String format, Object arg1, Object arg2) {
		log(Level.DEBUG, format, ARGS_TWO, arg1, arg2, null);
	}
	
	public void trace(String format, Object[] args) {
		log(Level.DEBUG, format, ARGS_ARRAY, args, null, null);
	}

	public void trace(String msg, Throwable t) {
		log(Level.DEBUG, msg, ARGS_NONE, null, null, t);
	}
	
	public boolean isDebugEnabled() {
		return isLevelEnabled(Level.DEBUG);
	}
	
	public void debug(String msg) {
		log(Level.DEBUG, msg, ARGS_NONE, null, null, null);
	}
	
	public void debug(String format, Object arg) {
		log(Level.DEBUG, format, ARGS_ONE, arg, null, null);
	}

	public void debug(String format, Object arg1, Object arg2) {
		log(Level.DEBUG, format, ARGS_TWO, arg1, arg2, null);
	}
	
	public void debug(String format, Object[] args) {
		log(Level.DEBUG, format, ARGS_ARRAY, args, null, null);
	}

	public void debug(String msg, Throwable t) {
		log(Level.DEBUG, msg, ARGS_NONE, null, null, t);
	}
	
	public boolean isInfoEnabled() {
		return isLevelEnabled(Level.INFO);
	}
	
	public void info(String msg) {
		log(Level.INFO, msg, ARGS_NONE, null, null, null);
	}
	
	public void info(String format, Object arg) {
		log(Level.INFO, format, ARGS_ONE, arg, null, null);
	}
	
	public void info(String format, Object arg1, Object arg2) {
		log(Level.INFO, format, ARGS_TWO, arg1, arg2, null);
	}
	
	public void info(String format, Object[] args) {
		log(Level.INFO, format, ARGS_ARRAY, args, null, null);
	}

	public void info(String msg, Throwable t) {
		log(Level.INFO, msg, ARGS_NONE, null, null, t);
	}

	public boolean isWarnEnabled() {
		return isLevelEnabled(Level.WARN);
	}

	public void warn(String msg) {
		log(Level.WARN, msg, ARGS_NONE, null, null, null);
	}
	
	public void warn(String format, Object arg) {
		log(Level.WARN, format, ARGS_ONE, arg, null, null);
	}
	
	public void warn(String format, Object arg1, Object arg2) {
		log(Level.WARN, format, ARGS_TWO, arg1, arg2, null);
	}
	
	public void warn(String format, Object[] args) {
		log(Level.WARN, format, ARGS_ARRAY, args, null, null);
	}
	
	public void warn(String msg, Throwable t) {
		log(Level.WARN, msg, ARGS_NONE, null, null, t);
	}

	public boolean isErrorEnabled() {
		return isLevelEnabled(Level.ERROR);
	}

	public void error(String msg) {
		log(Level.ERROR, msg, ARGS_NONE, null, null, null);
	}
	
	public void error(String format, Object arg) {
		log(Level.ERROR, format, ARGS_ONE, arg, null, null);
	}

	public void error(String format, Object arg1, Object arg2) {
		log(Level.ERROR, format, ARGS_TWO, arg1, arg2, null);
	}
	
	public void error(String format, Object[] args) {
		log(Level.ERROR, format, ARGS_ARRAY, args, null, null);
	}

	public void error(String msg, Throwable t) {
		log(Level.ERROR, msg, ARGS_NONE, null, null, t);
	}

	// *** Public Methods ***
	
	public List<Link> getActiveLinks() {
		return Collections.unmodifiableList(theActiveLinks);
	}
	
	public List<Translator> getActiveTranslators() {
		return Collections.unmodifiableList(theActiveTranslators);
	}

	// *** Protected Methods ***

	// *** Package Methods ***
	
	void activateLink(Link l) {
		synchronized(theActiveLinks) {
			theActiveLinks.add(l);
			theLinksCache = null;
		}
	}
	
	void deactivateLink(Link l) {
		synchronized(theActiveLinks) {
			theActiveLinks.remove(l);
			theLinksCache = null;
		}
	}
	
	void activateTranslator(Translator t) {
		synchronized(theActiveTranslators) {
			theActiveTranslators.add(t);
			theTranslatorsCache = null;
		}
	}
	
	void deactivateTranslator(Translator t) {
		synchronized(theActiveTranslators) {
			theActiveTranslators.remove(t);
			theTranslatorsCache = null;
		}
	}

	// *** Private Methods ***
	
	private Link[] getLinksSafely() {
		Link[] la = theLinksCache;
		if (la != null)
			return la;
		synchronized(theActiveLinks) {
			if (theLinksCache == null)
				theLinksCache = (Link[]) theActiveLinks.toArray(new Link[theActiveLinks.size()]);
			return theLinksCache;
		}
	}
	
	private Translator[] getTranslatorsSafely() {
		Translator[] ta = theTranslatorsCache;
		if (ta != null)
			return ta;
		synchronized(theActiveTranslators) {
			if (theTranslatorsCache == null)
				theTranslatorsCache = (Translator[]) theActiveTranslators.toArray(new Translator[theActiveTranslators.size()]);
			return theTranslatorsCache;
		}
	}
	
	private boolean isLevelEnabled(Level level) {
		int levelVal = level.intValue();
		Link[] links = getLinksSafely();
		for (int i = 0 ; i < links.length ; i++) {
			if (levelVal >= links[i].getThreshold().intValue())
				return true;
		}
		return false;
	}
	
	private LogEvent createLogEvent(Level level, String rawMessage, int argtype, Object o1, Object o2, Throwable t) {
		
		Thread thread = Thread.currentThread();
		if (rawMessage == null)
			return new LogEvent(name, level, null, t, System.currentTimeMillis(), thread.getName());
		
		// first traslate if necessary
		String translated = null;
		Translator[] translators = getTranslatorsSafely();
		for (int i = 0 ; i < translators.length ; i++) {
			ResourceBundle rb = translators[i].getResourceBundle();
			try {
				translated = rb.getString(rawMessage);
			} catch (MissingResourceException mre) {}
		}
		if (translated == null)
			translated = rawMessage;
		
		// next format message:
		String formatted = null;
		switch (argtype) {
		case ARGS_ONE:
			formatted = MessageFormatter.format(translated, o1);
			break;
		case ARGS_TWO:
			formatted = MessageFormatter.format(translated, o1, o2);
			break;
		case ARGS_ARRAY:
			formatted = MessageFormatter.arrayFormat(translated, (Object[])o1);
			break;
		case ARGS_NONE:
		default:
			formatted = translated;
		}
		
		return new LogEvent(name, level, formatted, t, System.currentTimeMillis(), thread.getName());
	}
	
	private void log(Level level, String rawMessage, int argtype, Object o1, Object o2, Throwable t) {
		int levelVal = level.intValue();
		Link[] links = getLinksSafely();
		LogEvent le = null;
		for (int i = 0 ; i < links.length ; i++) {
			if (levelVal < links[i].getThreshold().intValue())
				continue;
			if (le == null)
				le = createLogEvent(level, rawMessage, argtype, o1, o2, t);
			links[i].publish(le);
		}
	}

	// *** Private Classes ***
}










/* end */