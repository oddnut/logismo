/*
	XMPPPublisher.java

	Author: David Fogel

	Copyright 2005-2009

	All rights reserved. 
 */

package net.logismo.xmpp;

import net.logismo.*;

import org.jivesoftware.smack.*;

/**
 * XMPPPublisher
 *
 * Comment here.  Author: David Fogel
 */
abstract public class XMPPPublisher extends Publisher {
	// *** Class Members ***

	// *** Instance Members ***
	
	private XMPPConnectionFactory theFactory;
	private XMPPConnection theConnection;

	// *** Constructors ***
	
	public XMPPPublisher(String publisherName, XMPPConnectionFactory factory) {
		super(publisherName);
		
		theFactory = factory;
		theConnection = null;
	}

	// *** Publisher Methods ***
	
	protected void activate() throws PublisherException {
		super.activate();
		ensureXMPPConnection();
	}
	
	protected void deactivate() throws PublisherException {
		if (theConnection != null) {
			theConnection.disconnect();
			theConnection = null;
		}
		super.deactivate();
	}

	// *** Public Methods ***
	
	public XMPPConnectionFactory getXMPPConnectionFactory() {
		return theFactory;
	}
	
	public void ensureXMPPConnection() throws PublisherException {
		getXMPPConnection();
	}
	
	public XMPPConnection getXMPPConnection() throws PublisherException {
		
		if (theConnection != null && (!theConnection.isConnected() || !theConnection.isAuthenticated())) {
			theConnection.disconnect(); // probably does nothing
			theConnection = null;
			// TODO UNFINISHED - should we log this somewhere?
		}
		
		if (theConnection == null) {
			try {
				
				theConnection = theFactory.createXMPPConnection();
				
				// TODO - Don't really know what I'm doing here!!!
				theConnection.connect();
				
			} catch (XMPPException xe) {
				
				throw new PublisherException("XMPPConnectionFactory couldn't create XMPPConnection", xe);
			}
			
			if ( ! theConnection.isConnected()) {
				theConnection.disconnect();
				theConnection = null;
				throw new PublisherException("XMPPConnectionFactory failed to connect");
			}
			
			if ( ! theConnection.isAuthenticated()) {
				theConnection.disconnect();
				theConnection = null;
				throw new PublisherException("XMPPConnectionFactory failed to authenticate user");
			}
			
			handleNewXMPPConnection(theConnection);
		}
		
		return theConnection;
	}

	// *** Protected Methods ***
	
	abstract protected void handleNewXMPPConnection(XMPPConnection connection) throws PublisherException;

	// *** Package Methods ***

	// *** Private Methods ***

	// *** Private Classes ***
}










/* end */