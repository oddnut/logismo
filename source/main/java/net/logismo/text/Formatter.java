/*
	Formatter.java

	Author: David Fogel

	Copyright 2006 25oz Software, Inc.

	All rights reserved. 
 */

package net.logismo.text;

import java.io.*;

import net.logismo.LogEvent;

/**
 * Formatter
 *
 * Comment here.  Author: David Fogel
 */
public interface Formatter {
	// *** Class Members ***

	// *** Public Methods ***
	public void format(LogEvent e, Writer out) throws IOException;
}










/* end */