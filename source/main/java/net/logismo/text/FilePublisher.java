/*
	FilePublisher.java

	Author: David Fogel

	Copyright 2005-2009

	All rights reserved. 
 */

package net.logismo.text;


import java.io.*;

import net.logismo.RollingFileProvider;

/**
 * FilePublisher
 *
 * Comment here.  Author: David Fogel
 */
public class FilePublisher extends TextPublisher {
	// *** Class Members ***

	// *** Instance Members ***

	// *** Constructors ***
	
	public FilePublisher(String name, Formatter formatter, File parentDir, String baseFileName) {
		super(name, formatter, new RollingFileProvider(parentDir, baseFileName));
	}
	
	public FilePublisher(	String name, 
						Formatter formatter, 
						File parentDir, 
						String baseFileName,
						String extension,
						int limitInBytes) {
		super(name, formatter, new RollingFileProvider(parentDir, baseFileName, extension, limitInBytes));
	}

	// *** Interface Methods ***

	// *** Public Methods ***

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***

	// *** Private Classes ***
}










/* end */