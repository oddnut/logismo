/*
	PrefixMatcher.java

	Author: David Fogel

	Copyright 2005-2009

	All rights reserved. 
 */

package net.logismo.matchers;

import net.logismo.*;

/**
 * PrefixMatcher
 *
 * Comment here.  Author: David Fogel
 */
public class PrefixMatcher implements NameMatcher {
	// *** Class Members ***

	// *** Instance Members ***
	
	private String thePrefix;

	// *** Constructors ***
	
	public PrefixMatcher(String prefix) {
		thePrefix = prefix;
	}

	// *** NameMatcher Methods ***
	
	public boolean matchesName(String name) {
		return name.startsWith(thePrefix);
	}

	// *** Public Methods ***

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***

	// *** Private Classes ***
}










/* end */