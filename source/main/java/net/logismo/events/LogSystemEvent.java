/*
	LogSystemEvent.java

	Author: David Fogel

	Copyright 2006 25oz Software, Inc.

	All rights reserved. 
 */

package net.logismo.events;

import java.util.*;

import net.logismo.*;

/**
 * LogSystemEvent
 *
 * Comment here.  Author: David Fogel
 */
abstract public class LogSystemEvent extends EventObject {
	// *** Class Members ***
	private static final long serialVersionUID = 1L;

	// *** Instance Members ***

	// *** Constructors ***
	public LogSystemEvent(LogSystem system) {
		super(system);
	}

	// *** Interface Methods ***

	// *** Public Methods ***
	
	public LogSystem getLogSystem() {
		return (LogSystem) source;
	}

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***

	// *** Private Classes ***
}










/* end */