/*
	PublisherAddedEvent.java

	Author: David Fogel

	Copyright 2006 25oz Software, Inc.

	All rights reserved. 
 */

package net.logismo.events;

import net.logismo.*;

/**
 * PublisherAddedEvent
 *
 * Comment here.  Author: David Fogel
 */
public class PublisherAddedEvent extends LogSystemEvent {
	// *** Class Members ***
	private static final long serialVersionUID = 1L;

	// *** Instance Members ***
	private Publisher thePublisher;

	// *** Constructors ***
	public PublisherAddedEvent(LogSystem system, Publisher publisher) {
		super(system);
		thePublisher = publisher;
	}

	// *** Object Methods ***
	public String toString() {
		return getClass().getName() + ": " + thePublisher.getName();
	}

	// *** Public Methods ***
	public Publisher getPublisher() {
		return thePublisher;
	}

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***

	// *** Private Classes ***
}










/* end */