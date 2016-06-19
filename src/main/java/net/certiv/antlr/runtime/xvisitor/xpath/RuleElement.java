/*******************************************************************************
 * Copyright (c) 2010-2015 Gerald Rosenberg & others. All rights reserved.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the standard 3-clause BSD License.  A copy of the License 
 * is provided with this distribution in the License.txt file.
 *******************************************************************************/
package net.certiv.antlr.runtime.xvisitor.xpath;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

public class RuleElement extends Element {

	public RuleElement(boolean anywhere, boolean invert, String ident, int ruleIndex) {
		super(EType.Rule, anywhere, invert, ident);
		this.value = ruleIndex;
	}

	public int getRuleIndex() {
		return value;
	}

	@Override
	public boolean evaluate(ParseTree node) {
		if (node instanceof ParserRuleContext) {
			ParserRuleContext ctx = (ParserRuleContext) node;
			if (ctx.getRuleIndex() == getRuleIndex()) {
				return !isInvert();
			}
			return isInvert();
		}
		return false;
	}
}
