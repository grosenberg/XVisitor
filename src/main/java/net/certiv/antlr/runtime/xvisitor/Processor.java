/*******************************************************************************
 * Copyright (c) 2010-2023 Gerald Rosenberg & others. All rights reserved.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the standard 3-clause BSD License.  A copy of the License 
 * is provided with this distribution in the License.txt file.
 *******************************************************************************/
package net.certiv.antlr.runtime.xvisitor;

import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import net.certiv.antlr.runtime.xvisitor.xpath.EType;
import net.certiv.antlr.runtime.xvisitor.xpath.LiteralElement;
import net.certiv.antlr.runtime.xvisitor.xpath.RuleElement;
import net.certiv.antlr.runtime.xvisitor.xpath.TokenElement;
import net.certiv.antlr.runtime.xvisitor.xpath.WildcardElement;

/**
 * Parallel executed matcher for a set of XPaths.
 * 
 * @author Gbr
 */
public abstract class Processor extends ProcessorBase {

	public Processor(ParseTree tree) {
		super(tree);
	}

	// --------------------------------------------------
	// Runtime entry points -----------------------------

	/**
	 * Contexts matched by a wildcard or 'any' are not kept by default in the list of contexts
	 * returned by ProcessorBase.pathContexts(). Set to true to get a complete list of all contexts
	 * in matched paths.
	 * 
	 * @param keep
	 */
	public void keepAllPathContexts(boolean keep) {
		model.keepAllPathContexts(keep);
	}

	/**
	 * Invokes the visitor for all XPath rules listed in the main rule of the visitor grammar.
	 */
	public void findAll() {
		for (PathSpec path : getXPaths()) {
			path.setActive(true);
		}
		execute();
	}

	/**
	 * Invokes the visitor for an active subset of the XPath rules listed in the main rule of the
	 * visitor grammar. The argument names of XPath rules define the active subset. The remaining
	 * rules are inactive and are not considered in the operation of the visitor.
	 * 
	 * @param names Names of the XPath rules to set as active
	 */
	public void find(String... names) {
		List<String> nameList = Arrays.asList(names);
		for (PathSpec path : getXPaths()) {
			if (nameList.contains(path.getName())) {
				path.setActive(true);
			} else {
				path.setActive(false);
			}
		}
		execute();
	}

	// --------------------------------------------------
	// Runtime pathContexts construction ----------------

	private PathSpec path;

	public void mainRule(String name) {
		model.mainRule(name);
	}

	/**
	 * Start construction of a path
	 * 
	 * @param name
	 */
	public void createPathSpec(String name) {
		if (path != null) {
			path.setValid(false); // construction did not complete successfully
		}
		path = new PathSpec(name);
	}

	/**
	 * Complete construction of a path
	 */
	public void completePathSpec() {
		if (path != null) {
			path.id(getXPaths().size());
			getXPaths().add(path);
			path = null;
		}
	}

	/**
	 * Add elements (segments) to the path currently being constructed
	 * 
	 * @param type
	 * @param anywhere
	 * @param invert
	 * @param ident
	 * @param value
	 */
	public void addElement(EType type, boolean anywhere, boolean invert, String ident, int value) {
		switch (type) {
			case Literal:
				addLiteralElement(anywhere, invert, ident);
				break;
			case Rule:
				addRuleElement(anywhere, invert, ident, value);
				break;
			case Token:
				addTokenElement(anywhere, invert, ident, value);
				break;
			case Wildcard:
				addWildcardElement(anywhere, invert);
				break;
		}
	}

	private void addRuleElement(boolean anywhere, boolean invert, String ident, int ruleIndex) {
		RuleElement re = new RuleElement(anywhere, invert, ident, ruleIndex);
		if (path != null) path.add(re);
	}

	private void addTokenElement(boolean anywhere, boolean invert, String ident, int tokenType) {
		TokenElement te = new TokenElement(anywhere, invert, ident, tokenType);
		if (path != null) path.add(te);
	}

	private void addWildcardElement(boolean anywhere, boolean invert) {
		WildcardElement we = new WildcardElement(anywhere, invert);
		if (path != null) path.add(we);
	}

	private void addLiteralElement(boolean anywhere, boolean invert, String literal) {
		LiteralElement le = new LiteralElement(anywhere, invert, literal);
		if (path != null) path.add(le);
	}
}
