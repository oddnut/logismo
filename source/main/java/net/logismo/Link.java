 /*
	Link.java

	Author: David Fogel

	Copyright 2005 25oz Software, Inc.

	All rights reserved. 
 */

package net.logismo;

//import net.events.*;
import java.util.*;

/**
 * Link
 *
 * Comment here.  Author: David Fogel
 */
public class Link {
	// *** Class Members ***

	// *** Instance Members ***
	
	private NameMatcher theLogMatcher;
	private Level theThreshold;
	private NameMatcher thePublisherMatcher;
	
	private List<Log> theMatchedLogs;
	private List<Publisher> theMatchedPublishers;
	private Publisher[] thePublishersCache;
	
	private boolean isEnabled;
	private boolean isGroupEnabled;
	private boolean isActive;
	private LogSystem theSystem;
	private Group theGroup;

	// *** Constructors ***
	
	public Link(NameMatcher logMatcher, Level threshold, NameMatcher publisherMatcher) {
		
		theLogMatcher = logMatcher;
		theThreshold = threshold;
		thePublisherMatcher = publisherMatcher;
		
		theMatchedLogs = new ArrayList<Log>();
		theMatchedPublishers = new ArrayList<Publisher>();
		thePublishersCache = null;
		
		isEnabled = true;
		isGroupEnabled = false;
		isActive = false;
		theSystem = null;
		theGroup = null;
	}

	// *** Interface Methods ***

	// *** Public Methods ***
	
	public NameMatcher getLogMatcher() {
		return theLogMatcher;
	}
	
	public Level getThreshold() {
		return theThreshold;
	}
	
	public NameMatcher getPublisherMatcher() {
		return thePublisherMatcher;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public boolean isConnected() {
		return theSystem != null;
	}
	
	public List<Log> getMatchedLogs() {
		return Collections.unmodifiableList(theMatchedLogs);
	}
	
	public List<Publisher> getMatchedPublishers() {
		return Collections.unmodifiableList(theMatchedPublishers);
	}
	
	public void setEnabled(boolean enabled) {
		
		synchronized(getLock()) {
			
			if (enabled == isEnabled)
				return;
			
			isEnabled = enabled;
			updateActive();
		}
	}
	
	public Group getGroup() {
		return theGroup;
	}

	// *** Protected Methods ***

	// *** Package Methods ***
	
	void publish(LogEvent e) {
		Publisher[] publishers = getPublishersSafely();
		for (int i = 0 ; i < publishers.length ; i++) {
			Publisher p = publishers[i];
			if (p.isEnabled())
				p.addLogEvent(e);
		}
	}
	
	void connected(LogSystem system) {
		theSystem = system;
		
		Iterator<Log> li = theSystem.getLogs().values().iterator();
		while(li.hasNext()) {
			Log log = li.next();
			if (theLogMatcher.matchesName(log.getName()))
				theMatchedLogs.add(log);
		}
		
		synchronized(theMatchedPublishers) {
			Iterator<Publisher> pi = theSystem.getPublishers().iterator();
			while(pi.hasNext()) {
				Publisher p = pi.next();
				if (thePublisherMatcher.matchesName(p.getName()))
					theMatchedPublishers.add(p);
			}
		}
		
		updateActive();
	}
	
	void setGroup(Group group) {
		theGroup = group;
		isGroupEnabled = group != null ? group.isEnabled() : false;
	}
	
	void disconnected() {
		
		theSystem = null;
		updateActive();
		
		theMatchedLogs.clear();
		theMatchedPublishers.clear();
		thePublishersCache = null;
	}
	
	void setGroupEnabled(boolean groupEnabled) {
		isGroupEnabled = groupEnabled;
		updateActive();
	}
	
	void logAdded(Log l) {
		if (theLogMatcher.matchesName(l.getName())) {
			theMatchedLogs.add(l);
			if (isActive)
				l.activateLink(this);
		}
	}
	
	void publisherAdded(Publisher p) {
		if (thePublisherMatcher.matchesName(p.getName())) {
			synchronized(theMatchedPublishers) {
				theMatchedPublishers.add(p);
				thePublishersCache = null;
			}
			updateActive();
		}
	}
	
	void publisherRemoved(Publisher p) {
		if (thePublisherMatcher.matchesName(p.getName())) {
			synchronized(theMatchedPublishers) {
				theMatchedPublishers.remove(p);
				thePublishersCache = null;
			}
			updateActive();
		}
	}

	// *** Private Methods ***
	
	private Object getLock() {
		return theSystem != null ? theSystem.getChangeLock() : this;
	}
	
	private void updateActive() {
		boolean shouldBeActive = 	theSystem != null && 
								isEnabled && 
								isGroupEnabled && 
								theMatchedPublishers.size() > 0;
		
		if (isActive == shouldBeActive)
			return;
		
		isActive = shouldBeActive;
		
		if (isActive) {
			
			Iterator<Log> i = theMatchedLogs.iterator();
			while(i.hasNext()) {
				Log l = i.next();
				l.activateLink(this);
			}
			
		} else {
			
			Iterator<Log> i = theMatchedLogs.iterator();
			while(i.hasNext()) {
				Log l = i.next();
				l.deactivateLink(this);
			}
			
		}
	}
	
	private Publisher[] getPublishersSafely() {
		Publisher[] pa = thePublishersCache;
		if (pa != null)
			return pa;
		synchronized(theMatchedPublishers) {
			if (thePublishersCache == null)
				thePublishersCache = (Publisher[]) theMatchedPublishers.toArray(new Publisher[theMatchedPublishers.size()]);
			return thePublishersCache;
		}
	}

	// *** Private Classes ***
}










/* end */