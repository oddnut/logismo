/*
	TranslatorRemovedEvent.java

	Author: David Fogel

	Copyright 2005-2009

	All rights reserved. 
 */

package net.logismo.events;

import net.logismo.*;

/**
 * TranslatorRemovedEvent
 *
 * Comment here.  Author: David Fogel
 */
public class TranslatorRemovedEvent extends LogSystemEvent {
	// *** Class Members ***
	private static final long serialVersionUID = 1L;
	
	// *** Instance Members ***
	private Translator theTranslator;

	// *** Constructors ***
	public TranslatorRemovedEvent(LogSystem system, Translator translator) {
		super(system);
		theTranslator = translator;
	}

	// *** Object Methods ***
	public String toString() {
		return getClass().getName() + ": " + theTranslator;
	}

	// *** Public Methods ***
	
	public Translator getTranslator() {
		return theTranslator;
	}

	// *** Protected Methods ***

	// *** Package Methods ***

	// *** Private Methods ***

	// *** Private Classes ***
}










/* end */