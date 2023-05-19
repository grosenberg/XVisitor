/*******************************************************************************
 * Copyright (c) 2010-2023 Gerald Rosenberg & others. All rights reserved.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the standard 3-clause BSD License.  A copy of the License 
 * is provided with this distribution in the License.txt file.
 *******************************************************************************/
package net.certiv.antlr.runtime.xvisitor.xpath;

import org.antlr.v4.runtime.tree.ParseTree;

public class WildcardElement extends Element {

	public WildcardElement(boolean anywhere, boolean invert) {
		super(EType.Wildcard, anywhere, invert, "*");
	}

	@Override
	public boolean evaluate(ParseTree node) {
		return !isInvert();
	}
}
