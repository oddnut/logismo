/*
	PublisherException.java

	Author: David Fogel

	Copyright 2006 25oz Software, Inc.

	All rights reserved. 
 */

package net.logismo;

/**
 * PublisherException
 *
 * Comment here.  Author: David Fogel
 */
public class PublisherException extends Exception {
	// *** Class Members ***
	static final long serialVersionUID = 1;

	// *** Instance Members ***

	// *** Constructors ***
	public PublisherException(String message) {
		super(message);
	}
	
	public PublisherException(String message, Throwable originalCause) {
		super(message, originalCause);
	}
	
	public PublisherException(Throwable originalCause) {
		super(originalCause);
	}

	// *** Interface Methods ***

	// *** Public Methods ***

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***

	// *** Private Classes ***
}










/* end */