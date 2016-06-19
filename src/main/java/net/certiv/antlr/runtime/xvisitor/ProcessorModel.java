/*******************************************************************************
 * Copyright (c) 2010-2015 Gerald Rosenberg & others. All rights reserved.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the standard 3-clause BSD License.  A copy of the License 
 * is provided with this distribution in the License.txt file.
 *******************************************************************************/
package net.certiv.antlr.runtime.xvisitor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import net.certiv.antlr.runtime.xvisitor.xpath.Element;

/**
 * The model is
 * <ul>
 * <li>a fixed set of xpaths, each a list of elements
 * <li>a generational stack, each generation a set of active path segments,
 * <li>each segment being testable for a match against a parse tree node,
 * <li>each segment being identified with an element of an xpath
 * </ul>
 * 
 * @author Gbr
 */
public class ProcessorModel {

	private String mainRule;

	private List<PathSpec> xpaths;					// the set of xpaths to evaluate for matches
	private Stack<List<ActivePath>> generations;	// accumulated generations of active path sets
	private List<ActivePath> activePaths;			// the set of current trace-points in the set of
														// xpaths

	private Map<ParseTree, List<ActivePath>> completedPaths;
	private boolean keep;

	public ProcessorModel() {
		super();
		xpaths = new ArrayList<PathSpec>();
		generations = new Stack<>();
		activePaths = new ArrayList<ActivePath>();
		completedPaths = new LinkedHashMap<ParseTree, List<ActivePath>>();
	}

	// all paths start at the root
	public void initActivePaths() {
		for (PathSpec xpath : xpaths) {
			if (xpath.isActive()) {
				activePaths.add(new ActivePath(this, null, xpath.id(), 0));
			}
		}
	}

	public String mainRule() {
		return mainRule;
	}

	public void mainRule(String mainRule) {
		this.mainRule = mainRule;
	}

	public void keepAllPathContexts(boolean keep) {
		this.keep = keep;
	}

	public List<PathSpec> getXPaths() {
		return xpaths;
	}

	public PathSpec xPath(int id) {
		return xpaths.get(id);
	}

	public String nameOf(int id) {
		return xPath(id).getName();
	}

	public List<ActivePath> activePaths() {
		return activePaths;
	}

	/**
	 * Produce list of contexts in the matched path. Context order is from the terminal context to
	 * the root context of the parse tree
	 * 
	 * @return
	 */
	public List<ParseTree> pathNodes(ActivePath aPath) {
		List<ParseTree> nodes = new ArrayList<>();
		ActivePath segment = aPath;
		while (segment.parent() != null) {
			if (keep) {
				nodes.add(segment.pathContext());
			} else {
				PathSpec ps = xPath(segment.id());
				Element elem = ps.get(segment.elemIdx());
				if (!elem.isAny() || (elem.isAny() && segment.valueMatch())) {
					nodes.add(segment.pathContext());
				}
			}
			segment = segment.parent();
		}
		return nodes;
	}

	// on step to a parse tree node,
	// 1) prep filtered list of active paths that will be evaluated against the current node
	// 2) push the (now) parent generation of active path segments
	// 3) set the child list as the current generation
	public void foldActivePaths() {
		List<ActivePath> children = new ArrayList<>();
		for (ActivePath apath : activePaths) {
			if (apath.needsSplit()) {
				children.add(apath.split());
			}
			if (!apath.markedForRelease()) {
				children.add(apath.nextGen());
			}
		}
		generations.push(activePaths);
		activePaths = children;

		// if match is value-based, but could have been by 'any',
		// mark to add a greedy alt for the next generation
		// if (aPath.valueMatch() && aPath.hasAnySep()) {
		// aPath.markForSplit();
		// }

	}

	// on leaving a parse tree node, the list of active contexts
	// should only contain paths that are matched to this
	// node, but have no child nodes to consider as possibly
	// completing the paths. So, these partial path matches fail
	// by definition, and should be dropped.
	public void unfoldActivePaths() {
		activePaths.clear();
		activePaths = generations.pop();
	}

	/**
	 * Create and add path splits to the current set of active paths
	 */
	// TODO: was concurrent use exception in lists fixed?
	public void handleSplits() {
		List<ActivePath> splitPaths = new ArrayList<ActivePath>();
		for (ActivePath a : activePaths) {
			if (a.needsSplit()) {
				splitPaths.add(a.split());
			}
		}
		activePaths.addAll(splitPaths);
	}

	/**
	 * Collect list of matched paths. Screen for duplicate matches of the same xpath to the same
	 * terminal node.
	 * 
	 * @param ctx
	 * @param aPath
	 * @return
	 */
	public boolean markPathComplete(ParseTree ctx, ActivePath aPath) {
		List<ActivePath> aPaths = completedPaths.get(ctx);
		if (aPaths == null) {
			aPaths = new ArrayList<>();
			completedPaths.put(ctx, aPaths);
		}
		for (ActivePath ePath : aPaths) {
			if (ePath.id() == aPath.id() && ePath.elemIdx() == aPath.elemIdx()
					&& ePath.pathContext() == aPath.pathContext()) {
				return true; // it is a dup
			}
		}
		aPaths.add(aPath);
		return false;
	}

	public List<ActivePath> completedPaths(ParserRuleContext ctx) {
		return completedPaths.remove(ctx);
	}

	/**
	 * Remove marked path segments - those that do not match at the current node, remove them from
	 * the current list of active paths that list will then become the new parent list when
	 * evaluating children
	 */
	public void removeMarked() {
		List<ActivePath> validPaths = new ArrayList<ActivePath>();
		for (ActivePath a : activePaths) {
			if (!a.markedForRelease()) {
				validPaths.add(a);
			}
		}
		activePaths = validPaths;
	}

	public void dispose() {
		xpaths.clear();
		completedPaths.clear();
		generations.clear();
		for (ActivePath path : activePaths) {
			path.dispose();
		}
		activePaths.clear();
	}
}
