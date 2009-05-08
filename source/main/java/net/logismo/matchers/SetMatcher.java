/*
	SetMatcher.java

	Author: David Fogel

	Copyright 2005-2009

	All rights reserved. 
 */

package net.logismo.matchers;

import java.util.*;

import net.logismo.*;

/**
 * SetMatcher
 *
 * Comment here.  Author: David Fogel
 */
public class SetMatcher implements NameMatcher {
	// *** Class Members ***

	// *** Instance Members ***
	
	private HashSet<String> theNames;

	// *** Constructors ***
	
	public SetMatcher(String[] names) {
		theNames = new HashSet<String>(Arrays.asList(names));
	}
	
	public SetMatcher(Collection<String> names) {
		theNames = new HashSet<String>(names);
	}

	// *** NameMatcher Methods ***
	
	public boolean matchesName(String name) {
		return theNames.contains(name);
	}

	// *** Public Methods ***

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***

	// *** Private Classes ***
}










/* end */