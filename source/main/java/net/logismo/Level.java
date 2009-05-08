/*
	Level.java

	Author: David Fogel

	Copyright 2005-2009

	All rights reserved. 
 */

package net.logismo;

import java.io.Serializable;

/**
 * Level
 *
 * Comment here.  Author: David Fogel
 */
public class Level implements Serializable {
	// *** Class Members ***
	
	static final long serialVersionUID = 1;
	
	public static final Level OFF = new Level("OFF", Integer.MAX_VALUE);
	public static final Level ERROR = new Level("ERROR", 1000);
	public static final Level WARN = new Level("WARN", 900);
	public static final Level INFO = new Level("INFO", 800);
	public static final Level DEBUG = new Level("DEBUG", 500);
	public static final Level TRACE = new Level("TRACE", 100);
	public static final Level ALL = new Level("ALL", 0);
	
	public static Level forValue(int value) { // note: should probably replace these methods with static Maps, for extension.
		switch (value) {
		case 0: return ALL;
		case 500: return DEBUG;
		case 800: return INFO;
		case 900: return WARN;
		case 1000: return ERROR;
		case Integer.MAX_VALUE: return OFF;
		}
		return null;
	}
	
	public static Level forName(String name) { // note: should probably replace these methods with static Maps, for extension.
		if ("ALL".equalsIgnoreCase(name)) return ALL;
		if ("DEBUG".equalsIgnoreCase(name)) return DEBUG;
		if ("INFO".equalsIgnoreCase(name)) return INFO;
		if ("WARN".equalsIgnoreCase(name)) return WARN;
		if ("ERROR".equalsIgnoreCase(name)) return ERROR;
		if ("OFF".equalsIgnoreCase(name)) return OFF;
		return null;
	}

	// *** Instance Members ***
	
	private String theName;
	private int theValue;

	// *** Constructors ***
	
	protected Level(String name, int value) {
		theName = name;
		theValue = value;
	}

	// *** Interface Methods ***

	// *** Public Methods ***
	
	public String getName() {
		return theName;
	}
	
	public int intValue() {
		return theValue;
	}
	
	public int hashCode() {
		return theValue;
	}
	
	public boolean equals(Object o) {
		return o instanceof Level && ((Level)o).theValue == theValue;
	}
	
	public String toString() {
		return theName;
	}

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***

	// *** Private Classes ***
}










/* end */