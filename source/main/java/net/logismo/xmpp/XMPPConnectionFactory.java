/*
	XMPPConnectionFactory.java

	Author: David Fogel

	Copyright 2005-2009

	All rights reserved. 
 */

package net.logismo.xmpp;

import org.jivesoftware.smack.*;

/**
 * XMPPConnectionFactory
 *
 * Comment here.  Author: David Fogel
 */
public interface XMPPConnectionFactory {
	// *** Class Members ***

	// *** Public Methods ***
	public XMPPConnection createXMPPConnection() throws XMPPException;
}










/* end */