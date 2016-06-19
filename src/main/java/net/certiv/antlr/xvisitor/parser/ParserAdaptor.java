/*******************************************************************************
 * Copyright (c) 2010-2015 Gerald Rosenberg & others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the standard 3-clause BSD License.  A copy of the License
 * is provided with this distribution in the License.txt file.
 *******************************************************************************/
package net.certiv.antlr.xvisitor.parser;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;

public abstract class ParserAdaptor extends Parser {

	public ParserAdaptor(TokenStream input) {
		super(input);
	}
}
