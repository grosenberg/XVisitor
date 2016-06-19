/*******************************************************************************
 * Copyright (c) 2010-2015 Gerald Rosenberg & others. All rights reserved.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the standard 3-clause BSD License.  A copy of the License 
 * is provided with this distribution in the License.txt file.
 *******************************************************************************/
package net.certiv.antlr.runtime.xvisitor.xpath;

import org.antlr.v4.runtime.tree.ParseTree;

public abstract class Element {

	protected EType type;
	protected String ident;		// rule or token name
	protected int value; 		// rule index or token id

	protected boolean any;		// qualifiers
	protected boolean invert;

	protected boolean active;	// TODO: needed?

	public Element(EType type, boolean any, boolean invert, String ident) {
		this.type = type;
		this.any = any;
		this.invert = invert;
		this.ident = ident;
		this.active = true;
	}

	public EType getType() {
		return type;
	}

	public String getTypeStr() {
		return "EType." + type.toString();
	}

	public String getIdent() {
		return ident;
	}

	public int getValue() {
		return value;
	}

	public boolean isAny() {
		return any;
	}

	public boolean isInvert() {
		return invert;
	}

	public boolean isActive() {
		return active;
	}

	public void activate(boolean state) {
		this.active = state;
	}

	public abstract boolean evaluate(ParseTree node);

	@Override
	public String toString() {
		String any = this.any ? "//" : "/";
		String inv = invert ? "!" : "";
		return any + inv + ident;
	}
}
