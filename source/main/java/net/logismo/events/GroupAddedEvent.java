/*
	GroupAddedEvent.java

	Author: David Fogel

	Copyright 2005-2009

	All rights reserved. 
 */

package net.logismo.events;

import net.logismo.*;

/**
 * GroupAddedEvent
 *
 * Comment here.  Author: David Fogel
 */
public class GroupAddedEvent extends LogSystemEvent {
	// *** Class Members ***
	private static final long serialVersionUID = 1L;

	// *** Instance Members ***
	private Group theGroup;

	// *** Constructors ***
	public GroupAddedEvent(LogSystem system, Group group) {
		super(system);
		theGroup = group;
	}

	// *** Object Methods ***
	public String toString() {
		return getClass().getName() + ": " + theGroup.getName();
	}

	// *** Public Methods ***
	public Group getGroup() {
		return theGroup;
	}

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***

	// *** Private Classes ***
}










/* end */