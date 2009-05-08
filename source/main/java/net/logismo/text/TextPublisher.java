/*
	TextPublisher.java

	Author: David Fogel

	Copyright 2005-2009

	All rights reserved. 
 */

package net.logismo.text;

import java.io.*;
import java.util.*;
import java.nio.charset.*;

import net.logismo.*;

/**
 * TextPublisher
 *
 * Comment here.  Author: David Fogel
 */
public class TextPublisher extends Publisher {
	// *** Class Members ***
	
	private static final Charset UTF8 = Charset.forName("UTF-8");

	// *** Instance Members ***
	private Formatter theFormatter;
	private StreamProvider theProvider;
	private Charset theCharset;
	private OutputStream theCurrentStream;
	private Writer theCurrentWriter;

	// *** Constructors ***
	public TextPublisher(String name, Formatter formatter, OutputStream out) {
		this(name, formatter, out, null);
	}
	
	public TextPublisher(String name, Formatter formatter, final OutputStream out, Charset charset) {
		this(name, formatter, new StreamProvider() {
			public void activate() {}
			public boolean checkRefresh() {return false;}
			public void refreshStream() throws IOException {}
			public OutputStream getOutputStream() throws IOException { return out;}
			public void deactivate() {}
		}, charset);
	}
	
	public TextPublisher(String name, Formatter formatter, StreamProvider destination) {
		this(name, formatter, destination, null);
	}
	
	public TextPublisher(String name, Formatter formatter, StreamProvider provider, Charset charset) {
		super(name);
		
		theFormatter = formatter;
		theProvider = provider;
		theCharset = charset != null ? charset : UTF8;
		theCurrentStream = null;
		theCurrentWriter = null;
	}

	// *** Publisher Methods ***
	
	public void publish(LogEvent e) throws PublisherException {
		try {
			Writer out = getWriter();
			if (out == null)
				return;
			
			theFormatter.format(e, out);
			out.flush();
			
		} catch (IOException ioe) {
			throw new PublisherException(ioe);
		}
	}
	
	public void publish(List<LogEvent> logEvents) throws PublisherException {
		
		try {
		
			Writer out = getWriter();
			if (out == null) return;
			
			int size = logEvents.size();
			for (int i = 0 ; i < size ; i++) {
				
				if ((i % 10) == 0) { // TODO should probably document this batchsize somewhere
					
					out.flush();
					if (theProvider.checkRefresh()) {
						theProvider.refreshStream();
						out = getWriter();
						if (out == null)
							return;
					}
				}
				
				theFormatter.format((LogEvent) logEvents.get(i), out);
				out.write('\n');
			}
			out.flush();
		
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
		
		try {
			
			theProvider.deactivate();
			
		} catch (IOException ioe) {
			throw new PublisherException(ioe);
		}
	}

	// *** Public Methods ***
	
	public StreamProvider getProvider() {
		return theProvider;
	}

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***
	
	private Writer getWriter() throws IOException {
		OutputStream os = theProvider.getOutputStream();
		if (theCurrentStream == os)
			return theCurrentWriter;
		theCurrentStream = os;
		theCurrentWriter = new OutputStreamWriter(theCurrentStream, theCharset);
		return theCurrentWriter;
	}

	// *** Private Classes ***
}










/* end */