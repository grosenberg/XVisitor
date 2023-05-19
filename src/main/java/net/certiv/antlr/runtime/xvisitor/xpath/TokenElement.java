/*******************************************************************************
 * Copyright (c) 2010-2023 Gerald Rosenberg & others. All rights reserved.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the standard 3-clause BSD License.  A copy of the License 
 * is provided with this distribution in the License.txt file.
 *******************************************************************************/
package net.certiv.antlr.runtime.xvisitor.xpath;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class TokenElement extends Element {

	public TokenElement(boolean anywhere, boolean invert, String ident, int tokenType) {
		super(EType.Token, anywhere, invert, ident);
		this.value = tokenType;
	}

	public int getTokenType() {
		return value;
	}

	@Override
	public boolean evaluate(ParseTree node) {
		if (node instanceof TerminalNode) {
			TerminalNode tNode = (TerminalNode) node;
			if (tNode.getSymbol().getType() == getTokenType()) {
				return !isInvert();
			}
			return isInvert();
		}
		return false;
	}
}
