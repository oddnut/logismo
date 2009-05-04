/*
	GroupRemovedEvent.java

	Author: David Fogel

	Copyright 2006 25oz Software, Inc.

	All rights reserved. 
 */

package net.logismo.events;

import net.logismo.*;

/**
 * GroupRemovedEvent
 *
 * Comment here.  Author: David Fogel
 */
public class GroupRemovedEvent extends LogSystemEvent {
	// *** Class Members ***
	private static final long serialVersionUID = 1L;

	// *** Instance Members ***
	private Group theGroup;

	// *** Constructors ***
	public GroupRemovedEvent(LogSystem system, Group group) {
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