/*******************************************************************************
 * Copyright (c) 2010-2023 Gerald Rosenberg & others. All rights reserved.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the standard 3-clause BSD License.  A copy of the License 
 * is provided with this distribution in the License.txt file.
 *******************************************************************************/
package net.certiv.antlr.runtime.xvisitor.xpath;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class LiteralElement extends Element {

	private final String shortIdent;

	public LiteralElement(boolean anywhere, boolean invert, String ident) {
		super(EType.Literal, anywhere, invert, ident);
		this.shortIdent = ident.replaceAll("\\s", "");
	}

	public String text() {
		return ident;
	}

	public String shortText() {
		return shortIdent;
	}

	/*
	 * For a rule, the context getText() returns the concatenated token text. The only
	 * certain way to restore whitespace correctly is to accesx the token stream. The
	 * information is simply not present in the parse tree. If whitespece is nominally
	 * significant, the simple alternative is to test against the given literal having all
	 * whitespace removed. Not sure this alternative is appropriate in the long term.
	 */
	@Override
	public boolean evaluate(ParseTree node) {
		if (node instanceof ParserRuleContext) {
			ParserRuleContext ctx = (ParserRuleContext) node;
			if (text().equals(ctx.getText()) || shortText().equals(ctx.getText())) {
				return !isInvert();
			}
			return isInvert();
		}
		TerminalNode tNode = (TerminalNode) node;
		if (text().equals(tNode.getText())) {
			return !isInvert();
		}
		return isInvert();
	}
}
