/*
	ExactMatcher.java

	Author: David Fogel

	Copyright 2005-2009

	All rights reserved. 
 */

package net.logismo.matchers;

import net.logismo.*;

/**
 * ExactMatcher
 *
 * Comment here.  Author: David Fogel
 */
public class ExactMatcher implements NameMatcher {
	// *** Class Members ***

	// *** Instance Members ***
	
	private String theName;

	// *** Constructors ***
	
	public ExactMatcher(String name) {
		theName = name;
	}

	// *** NameMatcher Methods ***
	
	public boolean matchesName(String name) {
		return theName.equals(name);
	}

	// *** Public Methods ***

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***

	// *** Private Classes ***
}










/* end */