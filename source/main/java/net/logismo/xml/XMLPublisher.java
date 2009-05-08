/*
	XMLPublisher.java

	Author: David Fogel

	Copyright 2005-2009

	All rights reserved. 
 */

package net.logismo.xml;

import net.logismo.*;
import net.markout.*;
import net.markout.types.*;
import java.io.*;
import java.util.*;

/**
 * XMLPublisher
 *
 * Comment here.  Author: David Fogel
 */
public abstract class XMLPublisher extends Publisher {
	// *** Class Members ***
	
	private static final EncName UTF8 = new EncName("UTF-8");

	// *** Instance Members ***
	
	private StreamProvider theProvider;
	private OutputStream theCurrentStream;
	private DocumentWriter theCurrentDoc;
	private ContentWriter theContentWriter;

	// *** Constructors ***
	public XMLPublisher(String name, StreamProvider streamProvider) {
		super(name);
		
		theProvider = streamProvider;
		theCurrentStream = null;
		theCurrentDoc = null;
		theContentWriter = null;
	}

	// *** Publisher Methods ***
	
	public void publish(LogEvent e) throws PublisherException {
		throw new UnsupportedOperationException("Must use publish(List logEvents) instead!");
	}
	
	public void publish(List<LogEvent> logEvents) throws PublisherException {
		
		try {
		
			if (theContentWriter == null) {
				theCurrentStream = theProvider.getOutputStream();
				theCurrentDoc = XML.documentWriter(theCurrentStream, "UTF-8");
				theCurrentDoc.xmlVersion(UTF8);
				
				theContentWriter = startDocument(theCurrentDoc);
			}
			
			int size = logEvents.size();
			for (int i = 0 ; i < size ; i++) {
				
				if ((i % 10) == 0) { // TODO should probably document this batchsize somewhere
					
					if (theProvider.checkRefresh()) {
						
						endDocument(theCurrentDoc);
						theCurrentDoc.close();
						
						theProvider.refreshStream();
						theCurrentStream = theProvider.getOutputStream();
						theCurrentDoc = XML.documentWriter(theCurrentStream, "UTF-8");
						theCurrentDoc.xmlVersion(UTF8);
						
						theContentWriter = startDocument(theCurrentDoc);
					}
				}
				
				publish((LogEvent) logEvents.get(i), theContentWriter);
			}
		
		} catch (IOException ioe) {
			throw new PublisherException(ioe);
		}
	}
	
	protected void activate() throws PublisherException {
		super.activate();
		
		try {
			
			theProvider.activate();
			
		} catch (IOException ioe) {
			throw new PublisherException(ioe);
		}
	}
	
	protected void deactivate() throws PublisherException {
		super.deactivate();
		
		if (theContentWriter != null) {
			try {
				endDocument(theCurrentDoc);
				theCurrentDoc.close();
			} catch (IOException ioe) {
				// hmmm
			}
			theCurrentDoc = null;
			theCurrentStream = null;
			theContentWriter = null;
		}
		
		try {
			
			theProvider.deactivate();
			
		} catch (IOException ioe) {
			throw new PublisherException(ioe);
		}
	}

	// *** Public Methods ***

	// *** Protected Methods ***
	
	abstract protected ContentWriter startDocument(DocumentWriter doc) throws IOException;
	
	abstract protected void publish(LogEvent e, ContentWriter out) throws IOException;
	
	abstract protected void endDocument(DocumentWriter doc) throws IOException;

	// *** Package Methods ***

	// *** Private Methods ***

	// *** Private Classes ***
}










/* end */