/*
	LinkAddedEvent.java

	Author: David Fogel

	Copyright 2006 25oz Software, Inc.

	All rights reserved. 
 */

package net.logismo.events;

import net.logismo.*;

/**
 * LinkAddedEvent
 *
 * Comment here.  Author: David Fogel
 */
public class LinkAddedEvent extends LogSystemEvent {
	// *** Class Members ***
	private static final long serialVersionUID = 1L;

	// *** Instance Members ***
	private Link theLink;

	// *** Constructors ***
	public LinkAddedEvent(LogSystem system, Link link) {
		super(system);
		theLink = link;
	}

	// *** Object Methods ***
	public String toString() {
		return getClass().getName() + ": " + theLink;
	}

	// *** Public Methods ***
	public Link getLink() {
		return theLink;
	}

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***

	// *** Private Classes ***
}










/* end */