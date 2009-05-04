/*
	LogAddedEvent.java

	Author: David Fogel

	Copyright 2006 25oz Software, Inc.

	All rights reserved. 
 */

package net.logismo.events;

import net.logismo.*;

/**
 * LogAddedEvent
 *
 * Comment here.  Author: David Fogel
 */
public class LogAddedEvent extends LogSystemEvent {
	// *** Class Members ***
	private static final long serialVersionUID = 1L;
	
	// *** Instance Members ***
	private Log theLog;

	// *** Constructors ***
	public LogAddedEvent(LogSystem system, Log log) {
		super(system);
		theLog = log;
	}
	
	// *** Object Methods ***
	public String toString() {
		return getClass().getName() + ": " + theLog.getName();
	}

	// *** Public Methods ***
	
	public Log getLog() {
		return theLog;
	}

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***

	// *** Private Classes ***
}










/* end */