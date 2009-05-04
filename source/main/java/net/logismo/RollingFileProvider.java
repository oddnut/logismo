/*
	RollingFileProvider.java

	Author: David Fogel

	Copyright 2006 25oz Software, Inc.

	All rights reserved. 
 */

package net.logismo;


import java.io.*;
import java.text.*;
import java.util.*;

/**
 * RollingFileProvider
 *
 * Comment here.  Author: David Fogel
 */
public class RollingFileProvider implements StreamProvider {
	// *** Class Members ***
	
	private static final int DEFAULT_SIZE_LIMIT_BYTES = 100000; //100kb
	private static final String DEFAULT_EXTENSION = ".log";
	private static final int BUFFER_SIZE = 1024;
	private static final String ROLL_DATE_FORMAT_PATTERN = "_yyyy.MM.dd-HH.mm.ss";

	// *** Instance Members ***
	private File theDir;
	private String theBaseName;
	private String theExtension;
	private int theLimit;
	
	private File theFile;
	private OutputStream theStream;
	
	private DateFormat theRollDateFormat = new SimpleDateFormat(ROLL_DATE_FORMAT_PATTERN);

	// *** Constructors ***
	
	public RollingFileProvider( File dir, String baseName) {
		this(dir, baseName, DEFAULT_EXTENSION, DEFAULT_SIZE_LIMIT_BYTES);
	}
	
	public RollingFileProvider(	File dir, 
							String baseName, 
							String extension,
							int limitInBytes) {
		theDir = dir;
		theBaseName = baseName;
		theExtension = extension == null ? null : (extension.startsWith(".") ? extension : "." + extension);
		theLimit = limitInBytes;
		
		if ( ! theDir.exists() && ! theDir.mkdirs()) // try to create it if it doesn't exist
			throw new IllegalArgumentException("Log directory " + theDir.getAbsolutePath() + "does not exist");
		
		if ( ! theDir.isDirectory())
			throw new IllegalArgumentException("Log directory " + theDir.getAbsolutePath() + "is not a directory");
		
		theFile = new File(theDir, addExtension(theBaseName));
	}

	// *** StreamProvider Methods ***
	public void activate() throws IOException {
		
		roll();
	}
	
	public boolean checkRefresh() {
		
		return theFile.length() >= theLimit;
	}
	
	public void refreshStream() throws IOException {
		
		roll();
	}
	
	public OutputStream getOutputStream() throws IOException {
		
		return theStream;
	}
	
	public void deactivate() throws IOException {
		try {
			theStream.close();
		} finally {
			theStream = null;
		}
	}

	// *** Public Methods ***

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***
	
	private String addExtension(String base) {
		return theExtension == null ? base : base + theExtension;
	}
	
	private OutputStream openStream(File file) throws FileNotFoundException {
			
		return new BufferedOutputStream(new FileOutputStream(file, true), BUFFER_SIZE);
	}
	
	private void roll() throws IOException {
		
		if (theStream != null)
			theStream.close();
		
		if (theFile.exists()) {
			String newName = addExtension(theBaseName + theRollDateFormat.format(new Date(theFile.lastModified())));
			File newFile = new File(theDir, newName);
			theFile.renameTo(newFile);
		}
		
		theStream = openStream(theFile);
	}

	// *** Private Classes ***
}










/* end */