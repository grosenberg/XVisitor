/*******************************************************************************
 * Copyright (c) 2010-2017 Gerald Rosenberg & others. All rights reserved. 
 * This program and the accompanying materials are made available under the 
 * terms of the standard 3-clause BSD License. A copy of the License is 
 * provided with this distribution in the License.txt file.
 *******************************************************************************/
package net.certiv.antlr.xvisitor.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.misc.Interval;

public abstract class LexerAdaptor extends Lexer {

	public LexerAdaptor(CharStream input) {
		super(input);
	}

	protected void handleRightTerminator(int nestedType, int defaultType) {
		popMode();
		if (_modeStack.size() > 0) {
			setType(nestedType);
		} else {
			setType(defaultType);
		}
	}

	/**
	 * Predicate qualifier for default mode line comments - necessary to distinguish
	 * from the 'any' separator
	 * 
	 * @return true if line comment allowed
	 */
	protected boolean lcPrefix() {
		int offset = _input.index();
		boolean ws = false;
		for (int dot = -1; dot > -offset; dot--) {
			char c = (char) _input.LA(dot);
			switch (c) {
				case '\t':
				case '\r':
				case '\n':
				case ' ':
					ws = true;
					break;
				default:
					if (!ws || (ws && c == ':')) return false; // not a line comment
					return true;
			}
		}
		return true;
	}

	/**
	 * Predicate qualifier for default mode block comments - necessary to
	 * distinguish from the '/*' separator & wildcard combination
	 * 
	 * @return true if block comment allowed
	 */
	protected boolean bcSuffix() {
		int offset = _input.index();
		Interval i = new Interval(offset, offset + 2);
		String la = _input.getText(i);
		if (la.equals("/*/")) return false;
		return true;
	}
}
