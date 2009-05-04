/*
	ChatPublisher.java

	Author: David Fogel

	Copyright 2006 25oz Software, Inc.

	All rights reserved. 
 */

package net.logismo.xmpp;


import net.logismo.*;
import net.logismo.text.Formatter;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.*;
import java.util.*;
import java.io.*;

/**
 * ChatPublisher
 *
 * Comment here.  Author: David Fogel
 */
public class ChatPublisher extends XMPPPublisher {
	// *** Class Members ***

	// *** Instance Members ***
	
	private Formatter theFormatter;
	private Roster theRoster;
	private Map<String, ChatHandler> theChatHandlers;

	// *** Constructors ***
	
	public ChatPublisher(String xmppAddress, String xmppPassword, Formatter formatter) {
		this(xmppAddress, new StandardXMPPConnectionFactory(xmppAddress, xmppPassword), formatter);
	}
	
	public ChatPublisher(String publisherName, XMPPConnectionFactory factory, Formatter formatter) {
		super(publisherName, factory);
		
		theFormatter = formatter;
		theRoster = null;
		theChatHandlers = new HashMap<String, ChatHandler>();
	}

	// *** XMPPPublisher Methods ***
	
	protected void handleNewXMPPConnection(XMPPConnection connection) throws PublisherException {
		
		Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.reject_all);
		
		theRoster = connection.getRoster(); // this may take a while, we wait for server
		
		if (theRoster == null)
			throw new PublisherException("Couldn't get Roster from XMPPConnection");
		
		Map<String, ChatHandler> newChatHandlers = new HashMap<String, ChatHandler>();
		
		Iterator<RosterEntry> rei = theRoster.getEntries().iterator();
		while(rei.hasNext()) {
			RosterEntry re = (RosterEntry) rei.next();
			String user = re.getUser();
			Iterator<Presence> pi = theRoster.getPresences(user);
			if (pi == null)
				continue;
			while(pi.hasNext()) {
				Presence p = pi.next();
				if (p.getType() == Presence.Type.available) {
					String target = p.getFrom();
					ChatHandler oldHandler = (ChatHandler) theChatHandlers.get(target);
					boolean on = oldHandler != null && oldHandler.isOn();
					ChatHandler handler = new ChatHandler(connection, target, on);
					newChatHandlers.put(target, handler);
					handler.sendGreeting();
					handler.sendStatus();
				}
			}
		}
		
		theRoster.addRosterListener(new RListener(connection));
		
		theChatHandlers.clear();
		theChatHandlers = newChatHandlers;
	}
	
	// *** Publisher Methods ***
	
	public void publish(LogEvent e) throws PublisherException {
		throw new UnsupportedOperationException("Must use publish(List logEvents) instead!");
	}
	
	public void publish(List<LogEvent> logEvents) throws PublisherException {
		
		ensureXMPPConnection();
		
		if ( ! areActiveChatHandlers())
			return;
		
		try {
		
			CharArrayWriter out = new CharArrayWriter();
			int size = logEvents.size();
			for (int i = 0 ; i < size ; i++) {
				
				LogEvent e = (LogEvent) logEvents.get(i);
				// since events that have stacktraces are long, we send them as separate messages
				// otherwise, we append log events into a single message
				if (e.getThrowable() != null) {
					// send whatever's already there:
					if (out.size() > 0) {
						sendMessageToAll(out.toString());
						out.reset();
					}
					// format and send the log event with throwable
					theFormatter.format(e, out);
					sendMessageToAll(out.toString());
					out.reset();
					
				} else {
					if (out.size() > 0)
						out.write('\n'); // separate entries with a newline
					theFormatter.format(e, out);
				}
			}
			
			if (out.size() > 0) {
				sendMessageToAll(out.toString());
				out.reset();
			}
		} catch (XMPPException xe) {
			throw new PublisherException("Couldn't send messages to group chat", xe);
		} catch (IOException ioe) {
			// actually, since we're using a ArrayWriter, this shouldn't ever happen, but
			throw new PublisherException(ioe);
		}
	}

	// *** Public Methods ***
	
	public Formatter getFormatter() {
		return theFormatter;
	}

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***
	
	private boolean areActiveChatHandlers() {
		Iterator<ChatHandler> i = theChatHandlers.values().iterator();
		while(i.hasNext()) {
			ChatHandler h = (ChatHandler) i.next();
			if (h.isOn())
				return true;
		}
		return false;
	}
	
	private void sendMessageToAll(String message) throws XMPPException {
		
		Iterator<ChatHandler> i = theChatHandlers.values().iterator();
		while(i.hasNext()) {
			ChatHandler handler = (ChatHandler) i.next();
			handler.sendMessage(message);
		}
	}

	// *** Private Classes ***
	
	private class RListener implements RosterListener {
		
		private XMPPConnection theConnection;
		
		public RListener(XMPPConnection connection) {
			theConnection = connection;
		}

		public void entriesAdded(Collection<String> addresses) {
			// do nothing
		}

		public void entriesDeleted(Collection<String> addresses) {
			// do nothing
		}

		public void entriesUpdated(Collection<String> addresses) {
			// do nothing
		}

		public void presenceChanged(Presence p) {
			
			synchronized(getServiceLock()) {
			
				// TODO UNFINISHED - I changed this without knowing if getFrom is the right thing here
				String address = p.getFrom();
				
				ChatHandler handler = (ChatHandler) theChatHandlers.get(address);
				if (p != null && p.getType() == Presence.Type.available) {
					if (handler == null) { // and it should...
						handler = new ChatHandler(theConnection, address, false);
						theChatHandlers.put(address, handler);
						handler.sendGreeting();
						handler.sendStatus();
					} else {
						// well, this is probably just a duplicate.  I guess that's okay
					}
				} else {
					if (handler != null) {
						handler.close();
						theChatHandlers.remove(address);
					}
				}
			}
		}
	}
	
	private class ChatHandler implements MessageListener {
		
		private String theUser;
		private Chat theChat;
		private boolean isOn;
		
		public ChatHandler(XMPPConnection connection, String user, boolean on) {
			
			theUser = user;
			theChat = connection.getChatManager().createChat(theUser, this);
			isOn = on;
			theChat.addMessageListener(this);
		}
		
		public void processMessage(Chat c, Message m) {
			synchronized(getServiceLock()) {
				
				if (m.getType() != Message.Type.chat) return;
				String body = m.getBody();
				if (body.equalsIgnoreCase("stop")) {
					isOn = false;
					sendMessage("Delivery stopped.");
				} else if (body.equalsIgnoreCase("start")) {
					isOn = true;
					sendMessage("Delivery started.");
				} else {
					sendMessage("You must type either [start] or [stop].");
				}
			}
		}
		
		void sendGreeting() {
			sendMessage("Hello from Logismo ChatPublisher " + getName() + "\n");
		}
		
		void sendStatus() {
			if (isOn) {
				sendMessage("You ARE currently receiving log events.\n" +
							"\nType [stop] to stop receiving logging events.");
			} else {
				sendMessage("You ARE NOT currently receiving log events.\n" +
							"\nType [start] to start receiving logging events.");
			}
		}
		
		public boolean isOn() {
			return isOn;
		}
		
		public void sendMessage(String text) {
			try {
				theChat.sendMessage(text);
			} catch (XMPPException xe) {
				// TODO - maybe log this?
			}
		}
		
		public void close() {
			theChat.removeMessageListener(this);
		}
	}
}










/* end */