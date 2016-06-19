/*******************************************************************************
 * Copyright (c) 2010-2015 Gerald Rosenberg & others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the standard 3-clause BSD License.  A copy of the License
 * is provided with this distribution in the License.txt file.
 *******************************************************************************/
package net.certiv.antlr.xvisitor.parser;

import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;

public class ParserErrorListener extends BaseErrorListener {

	public int lastError = -1;

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
			String msg, RecognitionException e) {

		Parser parser = (Parser) recognizer;
		TokenStream tokens = parser.getInputStream();

		Token offSymbol = (Token) offendingSymbol;
		int thisError = offSymbol.getTokenIndex();
		if (offSymbol.getType() == -1 && thisError == tokens.size() - 1) {
			return;
		}
		if (thisError > lastError + 10) {
			lastError = thisError - 10;
		}
		lastError = thisError;

		List<String> stack = parser.getRuleInvocationStack();
		Collections.reverse(stack);
	}
}
