/*
	StreamProvider.java

	Author: David Fogel

	Copyright 2006 25oz Software, Inc.

	All rights reserved. 
 */

package net.logismo;

import java.io.*;

/**
 * StreamProvider
 *
 * Comment here.  Author: David Fogel
 */
public interface StreamProvider {
	// *** Class Members ***

	// *** Public Methods ***
	
	public void activate() throws IOException;
	
	public boolean checkRefresh();
	
	public void refreshStream() throws IOException;
	
	public OutputStream getOutputStream() throws IOException; // TODO UNFINISHED - do we need this throws?
	
	public void deactivate() throws IOException;
	
}










/* end */