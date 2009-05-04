/*
	Publisher.java

	Author: David Fogel

	Copyright 2005 25oz Software, Inc.

	All rights reserved. 
 */

package net.logismo;

import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Publisher
 *
 * Comment here.  Author: David Fogel
 */
public abstract class Publisher {
	// *** Class Members ***
	
	private static final int INITIAL_QUEUE_SIZE = 100;
	private static final int RESET_QUEUE_SIZE = 1000;
	
	private static final long DEFAULT_SERVICE_DELAY_MS = 300;
	
	private static final Log LOG = LogSystem.getLogSystem().getLog(Publisher.class.getName());

	// *** Instance Members ***
	
	private String theName;
	private List<LogEvent> theQueue;
	private List<LogEvent> theSpareQueue;
	private Object theQueueLock;
	private Object theServiceLock;
	
	private long theServiceDelay;
	private ScheduledFuture<?> theScheduledFuture;
	private Runnable theRunner;
	private boolean shouldActivate;
	
	private boolean isEnabled;
	private LogSystem theSystem;

	// *** Constructors ***
	
	protected Publisher(String name) {
		theName = name;
		theQueue = new ArrayList<LogEvent>(INITIAL_QUEUE_SIZE); // these sizes could be properties, I guess
		theSpareQueue = new ArrayList<LogEvent>(INITIAL_QUEUE_SIZE);
		theQueueLock = new Object();
		theServiceLock = new Object();
		
		theServiceDelay = DEFAULT_SERVICE_DELAY_MS;
		theScheduledFuture = null;
		theRunner = new Runnable() {
			public void run() {
				service();
			}
		};
		shouldActivate = true;
		
		isEnabled = true;
		theSystem = null;
	}

	// *** Interface Methods ***

	// *** Public Methods ***
	
	public String getName() {
		return theName;
	}
	
	public void setServiceDelayMS(long serviceDelayMS) {
		theServiceDelay = serviceDelayMS;
	}
	
	public long getServiceDelayMS() {
		return theServiceDelay;
	}
	
	abstract public void publish(LogEvent e) throws PublisherException;
	
	public void publish(List<LogEvent> logEvents) throws PublisherException {
		// default is to publish them individually:
		int size = logEvents.size();
		for (int i = 0 ; i < size ; i++) {
			publish(logEvents.get(i));
		}
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public void setEnabled(boolean enabled) {
		
		synchronized(getLock()) {
			
			if (isEnabled == enabled)
				return;
			
			isEnabled = enabled; // TODO UNFINISHED need to a) notify people, and b) flush events? (need to sync)
			
			if (isConnected()) {
				if (isEnabled)
					scheduleService();
				else
					cancelService();
			}
		}
	}
	
	public boolean isConnected() {
		return theSystem != null;
	}

	// *** Protected Methods ***
	
	protected void activate() throws PublisherException {
		// default does nothing
	}
	
	protected void deactivate() throws PublisherException {
		// default does nothing
	}
	
	protected Object getServiceLock() {
		return theServiceLock;
	}

	// *** Package Methods ***
	
	void addLogEvent(LogEvent e) {
		synchronized(theQueueLock) {
			theQueue.add(e);
		}
	}
	
	void service() {
		synchronized(theServiceLock) { // service lock is probably unnecesary, 
									// since the LogSystem should guarantee single thread access...
			
			if (shouldActivate) {
				// this is our first service call, we need to activate:
				try {
					
					activate();
					
				} catch (PublisherException pe) {
					LOG.error("Couldn't activate publisher: " + theName, pe);
					setEnabled(false);
					return;
				}
				
				shouldActivate = false;
				return;
			}
			
			List<LogEvent> queue;
			synchronized(theQueueLock) {
				if (theQueue.isEmpty())
					return; // nothing to do
				
				queue = theQueue;
				theQueue = theSpareQueue; // swap in the spare so loggers can use it while we publish
			}
			
			try {
				
				publish(queue);
				
			} catch (PublisherException pe) { 
				//TODO UNFINISHED: Careful, we don't want to start an endless recursion of log messages...
				LOG.error("PublisherException occurred during publishing for Publisher named " + theName, pe);
				
			} catch (RuntimeException re) {
				// TODO UNFINISHED: Careful, we don't want to start an endless recursion of log messages...
				LOG.error("RuntimeException occurred during publishing for Publisher named " + theName, re);
				
			} finally {
				
				queue.clear();
				
				if (queue.size() >= RESET_QUEUE_SIZE)
					queue = new ArrayList<LogEvent>(INITIAL_QUEUE_SIZE);
				
				theSpareQueue = queue;
			}
		}
	}
	
	void connected(LogSystem system) {
		
		theSystem = system;
		if (isEnabled)
			scheduleService();
	}
	
	void disconnected() {
		
		if (isEnabled)
			cancelService();
		theSystem = null;
	}
	
	void scheduleService() {
		shouldActivate = true;
		theScheduledFuture = 
			theSystem.getService().scheduleWithFixedDelay(theRunner, 0, theServiceDelay, TimeUnit.MILLISECONDS);
	}
	
	void cancelService() {
		theScheduledFuture.cancel(false);
		theScheduledFuture = null;
		shouldActivate = false;
		try {	
			synchronized(theServiceLock) { // I guess we should wait until the current batch of events is done..
				deactivate();
			}
		} catch (PublisherException pe) {
			LOG.error("Error deactivating publisher: " + theName, pe);
		}
	}

	// *** Private Methods ***
	
	private Object getLock() {
		return theSystem != null ? theSystem.getChangeLock() : this;
	}

	// *** Private Classes ***
}










/* end */