/*
	StaticLoggerBinder.java

	Author: David Fogel
	Copyright 2009 David Fogel
	All rights reserved.
*/

package org.slf4j.impl;

import net.logismo.LogSystem;

import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

/**
 * StaticLoggerBinder
 * 
 * Comment here.
 */
public class StaticLoggerBinder implements LoggerFactoryBinder {
	// *** Class Members ***
	private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();
	
	public static final StaticLoggerBinder getSingleton() {
		return SINGLETON;
	}
	
	/**
	* Declare the version of the SLF4J API this implementation is compiled against. 
	* The value of this field is usually modified with each release. 
	*/
	// to avoid constant folding by the compiler, this field must *not* be final
	public static String REQUESTED_API_VERSION = "1.5.6";  // !final
	
	private static final String CLASS_NAME = LogSystem.class.getName();

	// *** Instance Members ***

	// *** Constructors ***
	public StaticLoggerBinder() {
		
	}

	// *** LoggerFactoryBinder Methods ***

	public ILoggerFactory getLoggerFactory() {
		return LogSystem.getLogSystem();
	}

	public String getLoggerFactoryClassStr() {
		return CLASS_NAME;
	}

	// *** Public Methods ***

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***

	// *** Private Classes ***
}
