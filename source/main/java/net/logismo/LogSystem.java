/*
	LogSystem.java

	Author: David Fogel

	Copyright 2005-2009

	All rights reserved. 
 */

package net.logismo;

import net.logismo.events.*;

import org.slf4j.*;
import org.slf4j.bridge.SLF4JBridgeHandler;

import us.fogel.events.*;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * LogSystem
 *
 * Comment here.  Author: David Fogel
 */
public class LogSystem implements ILoggerFactory{
	// *** Class Members ***
	
	private static LogSystem theSystem = new LogSystem();
	
	public static LogSystem getLogSystem() {
		return theSystem; // UNFINISHED: does this have a singleton pattern problem?
	}
	
	private static final int DEFAULT_POOL_SIZE = 3;

	// *** Instance Members ***
	
	private Map<String, Log> theLogs;
	private List<Publisher> thePublishers;
	private List<Group> theGroups;
	private List<Translator> theTranslators;
	
	private ScheduledExecutorService theService;
	
	private Object theChangeLock;
	
	private EventSource theEvents;

	// *** Constructors ***
	
	LogSystem() {
		theLogs = new TreeMap<String, Log>();
		thePublishers = new ArrayList<Publisher>();
		theGroups = new ArrayList<Group>();
		theTranslators = new ArrayList<Translator>();
		theChangeLock = new Object();
		theEvents = new StandardEventSource();
		
		theService = Executors.newScheduledThreadPool(DEFAULT_POOL_SIZE);
		
		SLF4JBridgeHandler.install();
	}

	// *** ILoggerFactory Methods ***
	public Logger getLogger(String name) {
		return getLog(name);
	}

	// *** Public Methods ***
	
	public Log getLog(String name) {
		// first, if it's there, get it without synchronizing
		Log log = (Log) theLogs.get(name);
		if (log != null)
			return log;
		
		synchronized(theChangeLock) {
			// now, we try again (just in case it was created using a different thread)
			log = (Log) theLogs.get(name);
			if (log != null)
				return log;
			
			// create a new one:
			log = new Log(name);
			theLogs.put(name, log);
			
			Iterator<Group> gi = theGroups.iterator();
			while (gi.hasNext()) {
				Group g = gi.next();
				g.logAdded(log);
			}
			
			Iterator<Translator> ti = theTranslators.iterator();
			while(ti.hasNext()) {
				Translator t = ti.next();
				t.logAdded(log);
			}
			
			if (theEvents.hasEventTargetsFor(LogAddedEvent.class))
				theEvents.dispatchEvent(new LogAddedEvent(this, log));
		}
		
		return log;
	}
	
	public Set<String> getLogNames() {
		return Collections.unmodifiableSet(theLogs.keySet());
	}
	
	public List<Publisher> getPublishers() {
		return Collections.unmodifiableList(thePublishers);
	}
	
	public void addPublisher(Publisher publisher) {
		synchronized(theChangeLock) {
			String name = publisher.getName();
			
			Iterator<Publisher> pi = thePublishers.iterator();
			while (pi.hasNext()) {
				Publisher p = pi.next();
				if (p == publisher)
					throw new IllegalArgumentException("Publisher " + name + " already in LogSystem");
				if (name.equals(p.getName()))
					throw new IllegalArgumentException("Publisher name " + name + " is already taken by an existing Publisher");
			}
			
			thePublishers.add(publisher);
			
			publisher.connected(this);
			
			Iterator<Group> gi = theGroups.iterator();
			while (gi.hasNext()) {
				Group g = gi.next();
				g.publisherAdded(publisher);
			}
			
			if (theEvents.hasEventTargetsFor(PublisherAddedEvent.class))
				theEvents.dispatchEvent(new PublisherAddedEvent(this, publisher));
		}
	}
	
	public void removePublisher(Publisher publisher) {
		synchronized(theChangeLock) {
			
			boolean contained = thePublishers.remove(publisher);
			
			if (!contained)
				throw new IllegalArgumentException("No such publisher in LogSystem");
			
			Iterator<Group> gi = theGroups.iterator();
			while (gi.hasNext()) {
				Group g = gi.next();
				g.publisherRemoved(publisher);
			}
			
			publisher.disconnected();
			
			if (theEvents.hasEventTargetsFor(PublisherRemovedEvent.class))
				theEvents.dispatchEvent(new PublisherRemovedEvent(this, publisher));
		}
	}
	
	public List<Group> getGroups() {
		return Collections.unmodifiableList(theGroups);
	}
	
	public void addGroup(Group group) {
		synchronized(theChangeLock) {
			String name = group.getName();
			
			Iterator<Group> gi = theGroups.iterator();
			while (gi.hasNext()) {
				Group g = gi.next();
				if (g == group)
					throw new IllegalArgumentException("Group " + name + "is already in LogSystem");
				if (name.equals(g.getName()))
					throw new IllegalArgumentException("Group name " + name + " is already taken by an existing Group");
			}
			
			theGroups.add(group);
			
			group.connected(this);
			
			if (theEvents.hasEventTargetsFor(GroupAddedEvent.class))
				theEvents.dispatchEvent(new GroupAddedEvent(this, group));
		}
	}
	
	public void removeGroup(Group group) {
		synchronized(theChangeLock) {
			
			boolean contained = theGroups.remove(group);
			
			if (!contained)
				throw new IllegalArgumentException("No such Group in LogSystem");
			
			group.disconnected();
			
			if (theEvents.hasEventTargetsFor(GroupRemovedEvent.class))
				theEvents.dispatchEvent(new GroupRemovedEvent(this, group));
		}
	}
	
	public List<Translator> getTranslators() {
		return Collections.unmodifiableList(theTranslators);
	}
	
	public void addTranslator(Translator translator) {
		synchronized(theChangeLock) {
			if (theTranslators.contains(translator))
				throw new IllegalArgumentException("Translator is already in LogSystem");
			
			theTranslators.add(translator);
			
			translator.connected(this);
			
			if (theEvents.hasEventTargetsFor(TranslatorAddedEvent.class))
				theEvents.dispatchEvent(new TranslatorAddedEvent(this, translator));
		}
	}
	
	public void removeTranslator(Translator translator) {
		synchronized(theChangeLock) {
			
			boolean contained = theTranslators.remove(translator);
			
			if (!contained)
				throw new IllegalArgumentException("No such Translator in LogSystem");
			
			translator.disconnected();
			
			if (theEvents.hasEventTargetsFor(TranslatorRemovedEvent.class))
				theEvents.dispatchEvent(new TranslatorRemovedEvent(this, translator));
		}
	}
	
	public EventSource events() {
		return theEvents;
	}

	// *** Protected Methods ***

	// *** Package Methods ***
	
	Object getChangeLock() {
		return theChangeLock;
	}
	
	Map<String, Log> getLogs() {
		return theLogs;
	}
	
	ScheduledExecutorService getService() {
		return theService;
	}

	// *** Private Methods ***

	// *** Private Classes ***
}










/* end */