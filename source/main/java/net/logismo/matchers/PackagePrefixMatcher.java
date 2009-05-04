/*
	PackagePrefixMatcher.java

	Author: David Fogel

	Copyright 2005 25oz Software, Inc.

	All rights reserved. 
 */

package net.logismo.matchers;

import net.logismo.*;

/**
 * PackagePrefixMatcher
 *
 * Comment here.  Author: David Fogel
 */
public class PackagePrefixMatcher implements NameMatcher {
	// *** Class Members ***

	// *** Instance Members ***

	private String thePackagePrefix;

	// *** Constructors ***
	
	public PackagePrefixMatcher(String packagePrefix) {
		thePackagePrefix = packagePrefix;
		if (thePackagePrefix.endsWith("."))
			thePackagePrefix = thePackagePrefix.substring(0, thePackagePrefix.length() - 1); // chop off trailing "."s.
	}

	// *** NameMatcher Methods ***
	
	public boolean matchesName(String name) {
		
		// qualifying names must either match the prefix, or start with prefix + "."
		
		if (name.equals(thePackagePrefix))
			return true;
		return (name.startsWith(thePackagePrefix) && name.charAt(thePackagePrefix.length()) == '.');
	}

	// *** Public Methods ***

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***

	// *** Private Classes ***
}










/* end */