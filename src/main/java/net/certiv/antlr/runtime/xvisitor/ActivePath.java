/*******************************************************************************
 * Copyright (c) 2010-2015 Gerald Rosenberg & others. All rights reserved.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the standard 3-clause BSD License.  A copy of the License 
 * is provided with this distribution in the License.txt file.
 *******************************************************************************/
package net.certiv.antlr.runtime.xvisitor;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Represents one segment of a single active path
 * 
 * @author Gbr
 */
public class ActivePath {

	private ProcessorModel model;
	private ActivePath parent;

	private int xpathId;	// id of PathSpec in ProcessorModel.xpaths (same as index in xpaths)
	private int elemIdx;	// index into PathSpec.path -> Element

	private boolean isAny;		// has an 'any' separator
	private boolean matched;	// evaluation result
	private boolean valueMatch; // match occurred against the value of an 'any' element
	private ParseTree pathNode; // matched node (rule or terminal) in this active path segment

	private boolean pathTerminal;
	private boolean doneMarker;
	private boolean deadMarker;
	private boolean splitMarker;
	private PathSpec xpathSpec;

	/**
	 * Creates an active path segment.
	 * 
	 * @param model the processor model
	 * @param parent the 'soon to be' parent segment
	 * @param xpathId index of an XPath specification (PathSpec)
	 * @param elemIdx index of the 'current' element of the XPath
	 */
	public ActivePath(ProcessorModel model, ActivePath parent, int xpathId, int elemIdx) {
		this.model = model;
		this.parent = parent;
		this.xpathId = xpathId;
		this.elemIdx = elemIdx;
		init();
	}

	/**
	 * Creates a 'next' active path segment. Used when folding out a new generation of active path
	 * segments. Should be for internal use only
	 * 
	 * @param model the processor model
	 * @param parent the parent segment
	 * @param next to increment (or not) the xpath element index
	 */
	public ActivePath(ProcessorModel model, ActivePath parent, boolean next) {
		this.model = model;
		this.parent = parent;
		this.xpathId = parent.xpathId;
		this.elemIdx = parent.elemIdx;
		if (next) this.elemIdx++;
		init();
	}

	private void init() {
		xpathSpec = model.xPath(xpathId);
		isAny = xpathSpec.get(elemIdx).isAny();
	}

	/**
	 * Create and return a child segment based on the current active path segment
	 */
	public ActivePath nextGen() {
		boolean next = false;
		if (parent != null) {
			if (!isAny || (isAny && valueMatch)) {
				next = true;
			}
		}
		return new ActivePath(model, this, next);
	}

	public ActivePath split() {
		return new ActivePath(model, parent, xpathId, elemIdx);
	}

	public boolean matches(ParseTree node) {
		if (!xpathSpec.isActive()) return false;

		pathTerminal = (elemIdx == xpathSpec.size() - 1);
		matched = xpathSpec.get(elemIdx).evaluate(node);
		valueMatch = matched; 		// the actual match by value state

		if (!matched && isAny) {
			matched = true; 		// force match due to an 'any' separator
			pathTerminal = false;	// but any terminal match must be by value
		}
		if (valueMatch && isAny) {
			splitMarker = true; 	// mark to add greedy alt
		}
		if (matched) pathNode = node;

		return matched;
	}

	/**
	 * Returns the node (or context) matched by this segment instance or null if no match
	 */
	public ParseTree pathContext() {
		return pathNode;
	}

	public ActivePath parent() {
		return parent;
	}

	public int id() {
		return xpathId;
	}

	public int elemIdx() {
		return elemIdx;
	}

	/**
	 * Returns true iff last match occured against value of an 'any' element
	 */
	public boolean valueMatch() {
		return valueMatch;
	}

	// true iff last match completed the full xpath
	public boolean pathCompleted() {
		return pathTerminal;
	}

	public boolean needsSplit() {
		return splitMarker;
	}

	// are we done yet?
	public boolean markedForRelease() {
		return doneMarker || deadMarker;
	}

	// when all branches have been exhausted in the current fold
	public void markDone() {
		doneMarker = true;
	}

	public void markDead() {
		deadMarker = true;
	}

	public void dispose() {
		pathNode = null; // TODO: needed?
	}

	@Override
	public String toString() {
		String result = matched ? "Matched" : "No match";
		String qual = valueMatch ? " by value" : "";
		return result + qual;
	}
}
