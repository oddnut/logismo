/*
	StaticMarkerBinder.java

	Author: David Fogel
	Copyright 2009 David Fogel
	All rights reserved.
*/

package org.slf4j.impl;

import org.slf4j.IMarkerFactory;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.spi.MarkerFactoryBinder;

/**
 * StaticMarkerBinder
 * 
 * Comment here.
 */
public class StaticMarkerBinder implements MarkerFactoryBinder {
	// *** Class Members ***
	public static final StaticMarkerBinder SINGLETON = new StaticMarkerBinder();
	
	final IMarkerFactory markerFactory = new BasicMarkerFactory();

	// *** Instance Members ***

	// *** Constructors ***

	// *** MarkerFactoryBinder Methods ***
	public IMarkerFactory getMarkerFactory() {
		return markerFactory;
	}
	
	public String getMarkerFactoryClassStr() {
		return BasicMarkerFactory.class.getName();
	}
	
	// *** Public Methods ***

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***

	// *** Private Classes ***
}
