/*
	PatternMatcher.java

	Author: David Fogel

	Copyright 2005-2009

	All rights reserved. 
 */

package net.logismo.matchers;

import java.util.regex.*;

import net.logismo.*;

/**
 * PatternMatcher
 *
 * Comment here.  Author: David Fogel
 */
public class PatternMatcher implements NameMatcher {
	// *** Class Members ***

	// *** Instance Members ***
	
	private Pattern thePattern;

	// *** Constructors ***
	
	public PatternMatcher(String pattern) {
		thePattern = Pattern.compile(pattern);
	}
	
	public PatternMatcher(Pattern pattern) {
		thePattern = pattern;
	}

	// *** NameMatcher Methods ***
	
	public boolean matchesName(String name) {
		return thePattern.matcher(name).matches();
	}

	// *** Public Methods ***

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***

	// *** Private Classes ***
}










/* end */