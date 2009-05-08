/*
	Group.java

	Author: David Fogel

	Copyright 2005-2009

	All rights reserved. 
 */

package net.logismo;

import java.util.*;

import net.logismo.events.*;

/**
 * Group
 *
 * Comment here.  Author: David Fogel
 */
public class Group {
	// *** Class Members ***

	// *** Instance Members ***
	
	private String theName;
	private List<Link> theLinks;
	private boolean isEnabled;
	private LogSystem theSystem;

	// *** Constructors ***
	
	public Group(String name) {
		theName = name;
		theLinks = new ArrayList<Link>();
		isEnabled = true;
		theSystem = null;
	}

	// *** Interface Methods ***

	// *** Public Methods ***
	
	public String getName() {
		return theName;
	}
	
	public List<Link> getLinks() {
		return Collections.unmodifiableList(theLinks);
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public void setEnabled(boolean enabled) {
		
		synchronized(getLock()) {
			
			if (enabled == isEnabled)
				return;
			
			isEnabled = enabled;
			Iterator<Link> i = theLinks.iterator();
			while(i.hasNext()) {
				Link l = i.next();
				l.setGroupEnabled(isEnabled);
			}
		}
	}
	
	public boolean isConnected() {
		return theSystem != null;
	}
	
	public void addLink(Link link) {
		
		synchronized(getLock()) {
			if (theLinks.contains(link))
				throw new IllegalArgumentException("Group already contains this link");
			Group oldGroup = link.getGroup();
			if (oldGroup != null) // should probably throw exception, but just remove it
				oldGroup.removeLink(link);
			theLinks.add(link);
			link.setGroup(this);
			if (isConnected()) {
				
				link.connected(theSystem);
				
				if (theSystem.events().hasEventTargetsFor(LinkAddedEvent.class))
					theSystem.events().dispatchEvent(new LinkAddedEvent(theSystem, link));
			}
		}
	}
	
	public void removeLink(Link link) {
		
		synchronized(getLock()) {
			if ( ! theLinks.contains(link))
				throw new IllegalArgumentException("Group doesn't contain this link");
			
			theLinks.remove(link);
			link.setGroup(null);
			if (isConnected()) {
				
				link.disconnected();
				
				if (theSystem.events().hasEventTargetsFor(LinkRemovedEvent.class))
					theSystem.events().dispatchEvent(new LinkRemovedEvent(theSystem, link));
			}
		}
	}

	// *** Protected Methods ***

	// *** Package Methods ***
	
	void logAdded(Log log) {
		Iterator<Link> i = theLinks.iterator();
		while (i.hasNext()) {
			Link l = i.next();
			l.logAdded(log);
		}
	}
	
	void publisherAdded(Publisher publisher) {
		Iterator<Link> i = theLinks.iterator();
		while (i.hasNext()) {
			Link l = i.next();
			l.publisherAdded(publisher);
		}
	}
	
	void publisherRemoved(Publisher publisher) {
		Iterator<Link> i = theLinks.iterator();
		while (i.hasNext()) {
			Link l = i.next();
			l.publisherRemoved(publisher);
		}
	}
	
	void connected(LogSystem system) {
		theSystem = system;
		
		Iterator<Link> i = theLinks.iterator();
		while (i.hasNext()) {
			Link link = i.next();
			link.connected(theSystem);
			
			if (theSystem.events().hasEventTargetsFor(LinkAddedEvent.class))
				theSystem.events().dispatchEvent(new LinkAddedEvent(theSystem, link));
		}
	}
	
	void disconnected() {
		
		Iterator<Link> i = theLinks.iterator();
		while(i.hasNext()) {
			Link link = i.next();
			link.disconnected();
			
			if (theSystem.events().hasEventTargetsFor(LinkRemovedEvent.class))
				theSystem.events().dispatchEvent(new LinkRemovedEvent(theSystem, link));
		}
		theSystem = null;
	}

	// *** Private Methods ***
	
	private Object getLock() {
		return theSystem != null ? theSystem.getChangeLock() : this;
	}

	// *** Private Classes ***
}










/* end */