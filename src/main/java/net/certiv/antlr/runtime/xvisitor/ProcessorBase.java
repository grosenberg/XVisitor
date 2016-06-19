/*******************************************************************************
 * Copyright (c) 2010-2015 Gerald Rosenberg & others. All rights reserved.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the standard 3-clause BSD License.  A copy of the License 
 * is provided with this distribution in the License.txt file.
 *******************************************************************************/
package net.certiv.antlr.runtime.xvisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import net.certiv.antlr.runtime.xvisitor.util.Reflect;

/**
 * Implements a simple walker that will visit all nodes in the given parse tree.
 * 
 * @author Gbr
 */
public abstract class ProcessorBase extends ParseTreeWalker implements ParseTreeListener {

	protected ParseTree tree;
	protected ProcessorModel model;

	private Parser parser; // reference parser instance
	private List<ParseTree> currentPathNodeList; // for access from a current ActionBlock
	private boolean entering;

	public ProcessorBase(ParseTree tree) {
		super();
		this.tree = tree;
		this.model = new ProcessorModel();
	}

	public void setParser(Parser parser) {
		this.parser = parser;
	}

	protected void execute() {
		model.initActivePaths();
		walk(this, tree);
		executeActionBlock(model.mainRule());
	}

	public void setNewParseTree(ParseTree tree) {
		this.model.dispose();
		this.tree = tree;
		this.model = new ProcessorModel();
	}

	public void reset() {
		this.model.dispose();
		this.model = new ProcessorModel();
	}

	public List<PathSpec> getXPaths() {
		return model.getXPaths();
	}

	//
	// Context related methods

	/**
	 * Returns a list of parse tree path nodes evaluated in the current path. By default, this is
	 * just the non-wildcard'd nodes, <i>i.e.</i>, just the nodes symbolically identified in the rule
	 * path specification. If {@link Processor#keepAllPathContexts(boolean keep) is set, then all
	 * path nodes are recorded, and the result will be equivalent to Processor#ancestors()}.
	 */
	public List<ParseTree> pathNodes() {
		return currentPathNodeList;
	}

	/**
	 * Returns the current context
	 */
	public ParseTree lastPathNode() {
		return currentPathNodeList.get(0);
	}

	/**
	 * Returns the parent chain of parse-tree node ancestors starting with the current node and
	 * ending at the root node, inclusive.
	 */
	public List<ParseTree> ancestors() {
		List<ParseTree> ancList = new ArrayList<>();
		for (ParseTree parent = lastPathNode(); parent != null; parent = lastPathNode()) {
			ancList.add(parent);
		}
		return Collections.unmodifiableList(ancList);
	}

	/**
	 * True when entering any parse tree node
	 */
	public boolean entering() {
		return entering;
	}

	/**
	 * True when leaving a rule context
	 */
	public boolean exiting() {
		return !entering;
	}

	public void executeActionBlock(String name) {}

	protected abstract String[] getTokenNames();

	protected abstract String[] getRuleNames();

	/**
	 * Returns a non-null list of the ParserRuleContext nodes of the given class type that are
	 * children of the current context. <br/>
	 * <br/>
	 * If the current context is not a ParserRuleContext node, an empty list is returned.
	 * 
	 * @param ruleType
	 * @return
	 */
	public <T extends ParserRuleContext> List<T> getNodes(Class<? extends T> ctxType) {
		ParseTree ctx = lastPathNode();
		List<T> nodes = new ArrayList<>();

		for (int idx = 0; idx < ctx.getChildCount(); idx++) {
			ParseTree node = ctx.getChild(idx);
			if (ctxType.isInstance(node)) {
				nodes.add(ctxType.cast(node));
			}
		}
		return nodes;
	}

	/**
	 * Returns a parse-tree node list from a label field having the given field name in the current
	 * context. <br/>
	 * <br/>
	 * If the field is not a List<ParserRuleContext>, an empty list is returned.
	 * 
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends ParserRuleContext> List<T> getNodes(String fieldName) {
		ParseTree ctx = lastPathNode();
		try {
			return (List<T>) Reflect.get(ctx, fieldName);
		} catch (NoSuchFieldException | ClassCastException e) {}
		return new ArrayList<T>();
	}

	/**
	 * Returns a parse-tree node from a label field having the given field name in the current
	 * context. If the field does not exist or is not a ParseTree, null is returned.
	 */
	public ParseTree getNode(String fieldName) {
		ParseTree ctx = lastPathNode();
		try {
			return (ParseTree) Reflect.get(ctx, fieldName);
		} catch (NoSuchFieldException | ClassCastException e) {}
		return null;
	}

	public ParseTree getNode(int ruleIdx, int cnt) {
		ParseTree ctx = lastPathNode();
		if (ctx instanceof TerminalNode && ctx.getParent() != null) {
			ctx = ctx.getParent();
		}
		if (((RuleContext) ctx).getRuleIndex() == ruleIdx) {
			return ctx;
		}
		if (ctx.getParent() != null && ((RuleContext) ctx.getParent()).getRuleIndex() == ruleIdx) {
			return ctx.getParent();
		}

		RuleContext rCtx = (RuleContext) ctx;
		int jdx = 0;
		for (int idx = 0; idx < rCtx.getChildCount(); idx++) {
			if (rCtx.getChild(idx) instanceof RuleContext) {
				RuleContext childCtx = (RuleContext) rCtx.getChild(idx);
				if (childCtx.getRuleIndex() == ruleIdx) {
					if (cnt == jdx) {
						return rCtx.getChild(idx);
					}
					jdx++;
				}
			}
		}
		return null;
	}

	/**
	 * Returns a non-null list of the symbols from the children of the current context having the
	 * given token type. <br/>
	 * <br/>
	 * Where the current context is a parse-tree node of type TerminalNode, an empty list is
	 * returned. <br/>
	 * <br/>
	 * Where the current context is a parse-tree node of type ParserRuleContext, the symbols are
	 * collected from the child TerminalNodes having the given token type.
	 * 
	 * @param ttype
	 * @return
	 */
	public List<Token> getTokens(int ttype) {
		ParseTree ctx = lastPathNode();
		List<Token> tokens = new ArrayList<>();

		for (int idx = 0; idx < ctx.getChildCount(); idx++) {
			ParseTree child = ctx.getChild(idx);
			if (child instanceof TerminalNode) {
				Token symbol = ((TerminalNode) child).getSymbol();
				if (symbol.getType() == ttype) {
					tokens.add(symbol);
				}
			}
		}
		return tokens;
	}

	/**
	 * Returns a token list from a label field having the given field name in the current context.
	 * <br/>
	 * <br/>
	 * If the field is not a List<ParserRuleContext>, an empty list is returned.
	 * 
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Token> getTokens(String fieldName) {
		ParseTree ctx = lastPathNode();
		try {
			return (List<Token>) Reflect.get(ctx, fieldName);
		} catch (NoSuchFieldException | ClassCastException e) {}
		return new ArrayList<Token>();
	}

	/**
	 * Returns the symbol from the current context having the given token type. The 'cnt' determines
	 * the zero-relative instance of the symbol to select. <br/>
	 * <br/>
	 * Where the current context is a parse-tree node of type TerminalNode, the symbol is the
	 * TerminalNode token. Only a 'cnt' value of 0 is allowed.<br/>
	 * <br/>
	 * Where the current context is a parse-tree node of type ParserRuleContext, the symbol is
	 * selected from the 'cnt'th child TerminalNode having the given token type.
	 * 
	 * @param ttype
	 * @param cnt
	 * @return
	 */
	public Token getToken(int ttype, int cnt) {
		ParseTree ctx = lastPathNode();
		if (ctx instanceof TerminalNode) {
			if (cnt > 0) {
				return null;
			}
			Token t = ((TerminalNode) ctx).getSymbol();
			if (t.getType() == ttype) {
				return t;
			}
			return null;
		} else {
			TerminalNode node = ((ParserRuleContext) ctx).getToken(ttype, cnt);
			if (node != null) {
				return node.getSymbol();
			}
			return null;
		}
	}

	/**
	 * Returns a token from a label field having the given field name in the current context.<br/>
	 * <br/>
	 * If the field is not a Token, null is returned.
	 * 
	 * @param fieldName
	 * @return
	 */
	public Token getToken(String fieldName) {
		ParseTree ctx = lastPathNode();
		try {
			return (Token) Reflect.get(ctx, fieldName);
		} catch (NoSuchFieldException | ClassCastException e) {}
		return null;
	}

	// -----------------------------------------------------------------------------
	// Visitor callbacks

	@Override
	public void enterEveryRule(ParserRuleContext ctx) {
		model.foldActivePaths();
		evaluate(ctx);
	}

	@Override
	public void exitEveryRule(ParserRuleContext ctx) {
		postEvaluate(ctx);
		model.unfoldActivePaths();
	}

	@Override
	public void visitTerminal(TerminalNode node) {
		model.foldActivePaths();
		evaluate(node);
		model.unfoldActivePaths();
	}

	@Override
	public void visitErrorNode(ErrorNode node) {
		visitTerminal(node); // TODO: log error?
	}

	// called onEnter of context
	// evaluate the current node against the current set of activePaths
	// relevant state is kept in the active path segment and considered upon folding
	private void evaluate(ParserRuleContext ctx) {
		for (ActivePath aPath : model.activePaths()) {
			if (aPath.matches(ctx)) {
				// handle xpath match completion
				if (aPath.pathCompleted()) {
					boolean dup = model.markPathComplete(ctx, aPath);
					if (!dup) {
						currentPathNodeList = model.pathNodes(aPath);
						entering = true;
						executeActionBlock(model.nameOf(aPath.id()));
					}
					aPath.markDone(); // done with this one
				}
			} else {
				aPath.markDead(); // must be a dead-end
			}
		}
	}

	// called onExit of context
	private void postEvaluate(ParserRuleContext ctx) {
		List<ActivePath> aPaths = model.completedPaths(ctx);
		if (aPaths != null) {
			for (ActivePath aPath : aPaths) {
				currentPathNodeList = model.pathNodes(aPath);
				entering = false;
				executeActionBlock(model.nameOf(aPath.id()));
			}
		}
		model.removeMarked(); // TODO: needed?
	}

	// called for terminal nodes only
	// evaluate this terminal node against the current set of activePaths
	// terminal node has no children, so it is always a path completion or dead-end
	// still have to check for duplicates, though
	private void evaluate(TerminalNode node) {
		for (ActivePath aPath : model.activePaths()) {
			if (aPath.matches(node)) {
				// handle xpath match completion
				if (aPath.pathCompleted()) {
					boolean dup = model.markPathComplete(node, aPath);
					if (!dup) {
						currentPathNodeList = model.pathNodes(aPath);
						entering = true;
						executeActionBlock(model.nameOf(aPath.id()));
					}
					aPath.markDone();
				}
			} else {
				aPath.markDead();
			}
		}
		model.removeMarked();
	}

	@SuppressWarnings("unused")
	private String contextPath(ParserRuleContext ctx) {
		if (parser != null) return ctx.toString(parser);
		return ctx.toString();
	}
}
