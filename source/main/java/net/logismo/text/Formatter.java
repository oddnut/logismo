/*
	Formatter.java

	Author: David Fogel

	Copyright 2005-2009

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