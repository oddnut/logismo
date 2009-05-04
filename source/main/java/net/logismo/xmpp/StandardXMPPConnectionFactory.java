/*
	StandardXMPPConnectionFactory.java

	Author: David Fogel

	Copyright 2006 25oz Software, Inc.

	All rights reserved. 
 */

package net.logismo.xmpp;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.util.*;

/**
 * StandardXMPPConnectionFactory
 *
 * Comment here.  Author: David Fogel
 */
public class StandardXMPPConnectionFactory implements XMPPConnectionFactory {
	// *** Class Members ***
	
	private static final String DEFAULT_RESOURCE = "Logismo";

	// *** Instance Members ***
	private String theHost;
	private int thePort;
	private String theUser;
	private String thePassword;
	private String theResource;

	// *** Constructors ***
	
	public StandardXMPPConnectionFactory(String address, String password) {
		theHost = StringUtils.parseServer(address);
		thePort = -1;
		theUser = StringUtils.parseName(address);
		thePassword = password;
		theResource = StringUtils.parseResource(address);
		if (theResource == null || theResource.equals(""))
			theResource = DEFAULT_RESOURCE;
	}
	
	public StandardXMPPConnectionFactory(String serviceName, String user, String password, String resource) {
		theHost = serviceName;
		thePort = -1;
		theUser = user;
		thePassword = password;
		theResource = resource;
	}
	
	public StandardXMPPConnectionFactory(String host, int port, String user, String password, String resource) {
		theHost = host;
		thePort = port;
		theUser = user;
		thePassword = password;
		theResource = resource;
	}

	// *** XMPPConnectionFactory Methods ***
	
	public XMPPConnection createXMPPConnection() throws XMPPException {
		XMPPConnection con;
		
		// TODO - not sure what this should be doing
		//if (thePort <= 0)
			con = new XMPPConnection(theHost);
		//else
			//con = new XMPPConnection(theHost, thePort);
		
		con.login(theUser, thePassword, theResource);
		
		return con;
	}

	// *** Public Methods ***
	
	public String getHost() {
		return theHost;
	}
	
	public int getPort() {
		return thePort;
	}
	
	public String getUser() {
		return theUser;
	}
	
	public String getPassword() {
		return thePassword;
	}
	
	public String getResource() {
		return theResource;
	}
	
	public String toString() {
		return super.toString() + " " + (new String[] {
				"host = " + theHost,
				"port = " + thePort,
				"user = " + theUser,
				"resource = " + theResource,
				"password = " + thePassword
		}).toString();
	}

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***

	// *** Private Classes ***
}










/* end */