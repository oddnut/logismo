/*
	JDKXMLPublisher.java

	Author: David Fogel

	Copyright 2006 25oz Software, Inc.

	All rights reserved. 
 */

package net.logismo.xml;

import net.logismo.*;
import net.markout.*;
//import net.markout.support.DefaultElementWhitespacePolicy;
//import net.markout.support.DefaultWhitespacePolicy;
import net.markout.types.*;
import java.io.*;
import java.text.*;
import java.util.*;

/**
 * JDKXMLPublisher
 *
 * Comment here.  Author: David Fogel
 */
public class JDKXMLPublisher extends XMLPublisher {
	// *** Class Members ***
	
	private static final SystemLiteral LOGGER_DTD = new SystemLiteral("logger.dtd");
	private static final Name LOG = new Name("log");
	private static final Name RECORD = new Name("record");
	private static final Name DATE = new Name("date");
	private static final Name MILLIS = new Name("millis");
	private static final Name SEQUENCE = new Name("sequence");
	private static final Name LOGGER = new Name("logger");
	private static final Name LEVEL = new Name("level");
	private static final Name CLASS = new Name("class");
	private static final Name METHOD = new Name("method");
	//private static final Name THREAD = new Name("thread");
	private static final Name MESSAGE = new Name("message");
	//private static final Name KEY = new Name("key");
	//private static final Name CATALOG = new Name("catalog");
	//private static final Name PARAM = new Name("param");
	private static final Name EXCEPTION = new Name("exception");
	private static final Name FRAME = new Name("frame");
	private static final Name LINE = new Name("line");
	/*
	private static final WhitespacePolicy WSP = new DefaultWhitespacePolicy() {
		public ElementWhitespacePolicy forElement(Name elementType) {
			if (elementType.equals(RECORD)) return RECORD_WSP;
			if (elementType.equals(EXCEPTION)) return MULTI_LINE_WSP;
			if (elementType.equals(FRAME)) return MULTI_LINE_WSP;
			return SINGLE_LINE_WSP;
		}
	};
	
	private static final Whitespace NEWLINE_2 = new Whitespace("\n\n");
	
	private static final ElementWhitespacePolicy RECORD_WSP = new DefaultElementWhitespacePolicy() {
		public Whitespace beforeElement(int elementDepth) {
			return NEWLINE_2;
		}
		public Whitespace beforeEndTag(int elementDepth) {
			return Whitespace.NEW_LINE;
		}
	};
	
	private static final ElementWhitespacePolicy SINGLE_LINE_WSP = new DefaultElementWhitespacePolicy() {
		public Whitespace beforeElement(int elementDepth) {
			return Whitespace.NEW_LINE;
		}
	};
	
	private static final ElementWhitespacePolicy MULTI_LINE_WSP = new DefaultElementWhitespacePolicy() {
		public Whitespace beforeElement(int elementDepth) {
			return Whitespace.NEW_LINE;
		}
		public Whitespace beforeEndTag(int elementDepth) {
			return Whitespace.NEW_LINE;
		}
	};
	*/
	
	// *** Instance Members ***
	
	private DateFormat theISO_8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Date theTempDate;

	// *** Constructors ***
	public JDKXMLPublisher(String name, StreamProvider provider) {
		super(name, provider);
		
		theTempDate = new Date();
	}

	// *** XMLPublisher Methods ***
	
	protected ContentWriter startDocument(DocumentWriter doc) throws IOException {
		
		// TODO UNFINISHED - revisit when we add the whitespace policy back
		//doc.setWhitespacePolicy(WSP);
		
		doc.dtd(LOG, null, LOGGER_DTD);
		ContentWriter cw = doc.rootElement(LOG);
		
		return cw;
	}
	
	protected void publish(LogEvent e, ContentWriter out) throws IOException {
		/*
		<?xml version="1.0" encoding="UTF-8" standalone="no"?>
		<!DOCTYPE log SYSTEM "logger.dtd">
		<log>
		<record>
		  <date>2000-08-23 19:21:05</date>
		  <millis>967083665789</millis>
		  <sequence>1256</sequence>
		  <logger>kgh.test.fred</logger>
		  <level>INFO</level>
		  <class>kgh.test.XMLTest</class>
		  <method>writeLog</method>
		  <thread>10</thread>
		  <message>Hello world!</message>
		</record>
		</log>
		*/
		/*
		 * <!-- Each logging call is described by a record element. -->
<!ELEMENT record (date, millis, sequence, logger?, level,
class?, method?, thread?, message, key?, catalog?, param*, exception?)>
		 */
		ContentWriter record = out.element(RECORD);
		
		theTempDate.setTime(e.getTime());
		record.element(DATE).text(theISO_8601Format.format(theTempDate));
		
		record.element(MILLIS).text(String.valueOf(e.getTime()));
		
		record.element(SEQUENCE).text(String.valueOf(e.getSequenceNumber()));
		
		record.element(LOGGER).text(e.getLogName());
		
		record.element(LEVEL).text(e.getLevel().getName());
		
		// only do CLASS and METHOD if there is a throwable in the event:
		Throwable t = e.getThrowable();
		if (t != null) {
			StackTraceElement[] st = t.getStackTrace();
			if (st != null && st.length > 0) {
				record.element(CLASS).text(st[0].getClassName());
				record.element(METHOD).text(st[0].getMethodName());
			}
		}
		
		// no THREAD id, (option)
		
		record.element(MESSAGE).text(e.getMessage());
		
		// no KEY, CATALOG, PARAM (optional)
		
		if (t != null) {
			ContentWriter ex = record.element(EXCEPTION);
			
			ex.element(MESSAGE).text(t.getMessage());
			
			StackTraceElement[] st = t.getStackTrace();
			for (int i = 0 ; i < st.length ; i++) {
				ContentWriter frame = ex.element(FRAME);
				frame.element(CLASS).text(st[i].getClassName());
				frame.element(METHOD).text(st[i].getMethodName());
				frame.element(LINE).text(String.valueOf(st[i].getLineNumber()));
			}
		}
	}
	
	protected void endDocument(DocumentWriter doc) throws IOException {
		// nothing to do here, will be closed by XMLPublisher superclass.
	}

	// *** Public Methods ***

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***

	// *** Private Classes ***
	
}










/* end */