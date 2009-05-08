/*
	LogEvent.java

	Author: David Fogel

	Copyright 2005-2009

	All rights reserved. 
 */

package net.logismo;

import java.util.EventObject;
import java.util.concurrent.atomic.AtomicLong;

/**
 * LogEvent
 *
 * Comment here.  Author: David Fogel
 */
public class LogEvent extends EventObject {
	// *** Class Members ***
	static final long serialVersionUID = 1;
	
	private static AtomicLong theSequenceCounter = new AtomicLong(0);
	
	// *** Instance Members ***
	
	private Level theLevel;
	private String theMessage;
	private Throwable theThrowable;
	private long theTime;
	private long theSequenceNumber;
	private String theThreadName;
	// private int theThreadID; // only in java 1.5
	

	// *** Constructors ***
	
	LogEvent(	String logName,
				Level level,
				String message,
				Throwable throwable,
				long time,
				String threadName) {
		super(logName);
		theLevel = level;
		theMessage = message;
		theThrowable = throwable;
		theTime = time;
		theSequenceNumber = theSequenceCounter.getAndIncrement();
		theThreadName = threadName;
	}

	// *** Interface Methods ***

	// *** Public Methods ***
	
	public String getLogName() {
		return (String) source;
	}
	
	public Level getLevel() {
		return theLevel;
	}
	
	public String getMessage() {
		return theMessage;
	}
	
	public Throwable getThrowable() {
		return theThrowable;
	}
	
	public long getTime() {
		return theTime;
	}
	
	public long getSequenceNumber() {
		return theSequenceNumber;
	}
	
	public String getThreadName() {
		return theThreadName;
	}
	
	public String toString() {
		return "[" + getClass().getName() + ": " + new Object[] {
			source, theLevel, theMessage, theThrowable, new java.util.Date(theTime), new Long(theSequenceNumber), theThreadName
		} + "]";
	}

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***

	// *** Private Classes ***
}










/* end */