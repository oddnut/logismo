/*
	StaticMDCBinder.java

	Author: David Fogel
	Copyright 2009 David Fogel
	All rights reserved.
*/

package org.slf4j.impl;

import org.slf4j.helpers.BasicMDCAdapter;
import org.slf4j.spi.MDCAdapter;

/**
 * StaticMDCBinder
 * 
 * Comment here.
 */
public class StaticMDCBinder {
	// *** Class Members ***
	public static final StaticMDCBinder SINGLETON = new StaticMDCBinder();

	// *** Instance Members ***

	// *** Constructors ***

	// *** Interface Methods ***

	// *** Public Methods ***
	public MDCAdapter getMDCA() {
		// note that this method is invoked only from within the static initializer of 
		// the org.slf4j.MDC class.
		return new BasicMDCAdapter();
	}
  
	public String  getMDCAdapterClassStr() {
		return BasicMDCAdapter.class.getName();
	}

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***

	// *** Private Classes ***
}
