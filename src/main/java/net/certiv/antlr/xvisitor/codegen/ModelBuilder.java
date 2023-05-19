/*******************************************************************************
 * Copyright (c) 2010-2023 Gerald Rosenberg & others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the standard 3-clause BSD License. A
 * copy of the License is provided with this distribution in the License.txt file.
 *******************************************************************************/
package net.certiv.antlr.xvisitor.codegen;

import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import net.certiv.antlr.runtime.xvisitor.util.Reflect;
import net.certiv.antlr.runtime.xvisitor.util.Strings;
import net.certiv.antlr.xvisitor.parser.gen.XVisitorParser.ActionBlockContext;
import net.certiv.antlr.xvisitor.parser.gen.XVisitorParser.ActionContext;
import net.certiv.antlr.xvisitor.parser.gen.XVisitorParser.GrammarSpecContext;
import net.certiv.antlr.xvisitor.parser.gen.XVisitorParser.OptionContext;
import net.certiv.antlr.xvisitor.parser.gen.XVisitorParser.OptionValueContext;
import net.certiv.antlr.xvisitor.parser.gen.XVisitorParser.OptionsSpecContext;
import net.certiv.antlr.xvisitor.parser.gen.XVisitorParser.ReferenceContext;
import net.certiv.antlr.xvisitor.parser.gen.XVisitorParser.SeparatorContext;
import net.certiv.antlr.xvisitor.parser.gen.XVisitorParser.TextContext;
import net.certiv.antlr.xvisitor.parser.gen.XVisitorParser.WordContext;
import net.certiv.antlr.xvisitor.parser.gen.XVisitorParser.XgroupContext;
import net.certiv.antlr.xvisitor.parser.gen.XVisitorParser.XpathContext;
import net.certiv.antlr.xvisitor.parser.gen.XVisitorParser.XpathSpecContext;
import net.certiv.antlr.xvisitor.parser.gen.XVisitorParserBaseVisitor;
import net.certiv.antlr.xvisitor.tool.ErrorType;

public class ModelBuilder extends XVisitorParserBaseVisitor<CodeGenModel> {

	private CodeGenModel model; // the builder model
	private ReferenceParser refParser; // the parser builder

	private String abName; // xvisitor/xpath abName for 'current' actionBlock
	private StringBuilder abText; // text accumulator for the 'current' actionBlock

	private boolean flagAction;

	public ModelBuilder(CodeGenModel model) {
		super();
		this.model = model;
	}

	@Override
	public CodeGenModel visitGrammarSpec(GrammarSpecContext ctx) {
		model.setGrammarName(ctx.ID().getText());
		return super.visitGrammarSpec(ctx);
	}

	@Override
	public CodeGenModel visitOptionsSpec(OptionsSpecContext ctx) {
		CodeGenModel ret = super.visitOptionsSpec(ctx);
		refParser = new ReferenceParser(model);
		boolean ok = refParser.loadRefParser();
		if (ok) {
			refParser.loadRuleNames();
			refParser.loadTokenNames();
		}
		return ret;
	}

	@Override
	public CodeGenModel visitOption(OptionContext ctx) {
		String lhs = ctx.ID().getText();
		OptionValueContext vctx = ctx.optionValue();

		StringBuilder sb = new StringBuilder();
		for (TerminalNode id : vctx.ID()) {
			sb.append(id + ".");
		}
		model.addOption(lhs, sb.substring(0, sb.length() - 1));
		return super.visitOption(ctx);
	}

	@Override
	public CodeGenModel visitAction(ActionContext ctx) {
		abName = ctx.ID().getText();
		flagAction = true;
		return super.visitAction(ctx);
	}

	@Override
	public CodeGenModel visitXgroup(XgroupContext ctx) {
		abName = ctx.name.getText();
		model.setMain(abName);
		for (TerminalNode id : ctx.ID()) {
			model.addPathName(id.getText());
		}
		return super.visitXgroup(ctx);
	}

	@Override
	public CodeGenModel visitXpath(XpathContext ctx) {
		abName = ctx.name.getText();
		return super.visitXpath(ctx);
	}

	@Override
	public CodeGenModel visitXpathSpec(XpathSpecContext ctx) {
		XpathContext parent = (XpathContext) ctx.getParent();
		String name = parent.name.getText();
		model.startPath(name);
		CodeGenModel ret = super.visitXpathSpec(ctx);
		model.finishPath();
		return ret;
	}

	@Override
	public CodeGenModel visitActionBlock(ActionBlockContext ctx) {
		abText = new StringBuilder();
		CodeGenModel ret = null;
		if ((ctx.ONENTRY() == null && ctx.ONEXIT() == null)) {
			if (ctx.getParent() instanceof XpathContext) {
				abText.append("if (entering()) {");
				ret = super.visitActionBlock(ctx);
				abText.append("}\n");
			} else {
				ret = super.visitActionBlock(ctx);
			}
		} else {
			if (ctx.ONENTRY() != null) {
				abText.append("if (entering()) {");
			} else {
				abText.append("if (exiting()) {");
			}
			ret = super.visitActionBlock(ctx);
			abText.append("}\n");
		}
		if (flagAction) {
			model.addAction(abName, abText.toString());
			flagAction = false;
		} else {
			model.addActionBlock(abName, abText.toString());
		}
		return ret;
	}

	@Override
	public CodeGenModel visitText(TextContext ctx) {
		abText.append(ctx.getText());
		return super.visitText(ctx);
	}

	// REFERENCE is '$a.b', where
	// -- a is a rule/token/label ident
	// -- b is one of a defined set of activities [text|type|...]
	// a is a list if its method return or field type is List<?>
	// a is a token reference if its method return or field type is TerminalNode
	// a is a rule reference otherwise if it has a method return or field type
	// a is a label if it is not an accessible method or field
	// -- label can be Token, Context, List<Token>, List<Context>

	// Thinking:
	// Types of XPath terminals that can be specified to identify a parse-tree node that,
	// in turn, can be the target context of a reference: rule context id, terminal node
	// id, token type, & literal.
	//
	// A reference, within the context of a parse-tree node, can refer to a sub-context
	// (either a ParseTreeContext instance or a List thereof), a token (TerminalNode
	// instance or List thereof), or a label identified Field represnting either a
	// sub-context or token.
	//
	// For an XPath terminal rule context id or terminal nodeid, the only way to be
	// certain of the nature of the reference is to reflect if it is a method or a field,
	// then to discern if it is of a list or instance value.
	//
	// For an XPath terminal of token type, which can be detected by resolving against the
	// list of known parser token types, the relevant parse-tree context for any reference
	// is effectively parent node of the token.
	//
	// For an XPath terminal of literal type, which can be detected by detecting the
	// guarding single quote characters, the relevant parse-tree context for any reference
	// is effectively parent node of the token whose text corresponds to the literal
	// value.
	//

	// TODO: the references to model.getParserName() should be to the generated XVisitor
	// processor - also, the rule and token name blocks should include the int name
	// associations

	@Override
	public CodeGenModel visitReference(ReferenceContext ctx) {

		// model.inprocLastPathContext is the context name that is or containes the REFERENCE. The
		// name is the REFERENCE of the name corresponds to a token name. Otherwise, the name
		// corresponds to a context class that contains the REFERENCE.

		// refs[0] is the name of the context class method identified by the REFERENCE
		// refs[1] is any qualifier (i.e., '.text') to be applied to the REFERENCE method
		// analyze reference - strip '$' and split on '.'
		String[] refs = ctx.REFERENCE().getSymbol().getText().substring(1).split("\\.", 2);

		// Check for special context variable name
		if (refs[0].equals("ctx")) {
			abText.append("lastPathNode()");
			evalRulePostfix(refs);
			return super.visitReference(ctx);
		}

		// Check for context method return types
		List<String> ret = discernReferenceMethodType(model.getLastContextName(), refs[0]);
		if (ret != null && ret.size() > 0) { // method
			if (ret.get(0).equals("List")) {
				if (ret.get(1).equals("TerminalNode")) {
					abText.append("getTokens(" + model.getParserName() + "." + refs[0] + ")");
				} else {
					String ref = Strings.titleCase(refs[0]);
					abText.append("getNodes(" + ref + "Context.class)");
				}
				evalListPostfix(refs);
			} else if (ret.get(0).equals("TerminalNode")) {
				abText.append("getToken(" + model.getParserName() + "." + refs[0] + ", 0)");
				evalTokenPostfix(refs);
			} else {
				abText.append("getNode(" + model.getParserName() + ".RULE_" + refs[0] + ", 0)");
				evalRulePostfix(refs);
			}

			return super.visitReference(ctx);
		}

		// Check for context fields
		ret = discernReferenceVarType(model.getLastContextName(), refs[0]);
		if (ret != null && ret.size() > 0) { // label var
			if (ret.get(0).equals("List")) {
				if (ret.get(1).equals("Token")) {
					abText.append("getTokens(\"" + refs[0] + "\")");
				} else {
					abText.append("getNodes(\"" + refs[0] + "\")");
				}
				evalListPostfix(refs);
			} else if (ret.get(0).equals("Token")) {
				abText.append("getToken(\"" + refs[0] + "\")");
				evalTokenPostfix(refs);
			} else {
				abText.append("getNode(\"" + refs[0] + "\")");
				evalRulePostfix(refs);
			}

			return super.visitReference(ctx);
		}

		// Check for token type and literal value
		String token = discernTokenOrLiteralType(model.getLastContextName());
		if (token != null) {
			abText.append("getToken(" + model.getParserName() + "." + token + ", 0)");
			evalTokenPostfix(refs);

			return super.visitReference(ctx);
		}

		// check if the name corresponds to the context itself
		String rule = discernRuleType(model.getLastContextName(), refs[0]);
		if (rule != null) {
			abText.append("lastPathNode()");
			evalRulePostfix(refs);

			return super.visitReference(ctx);
		}

		// error
		abText.append("<Unknown or inaccessible reference: " + refs[0] + ">");

		return super.visitReference(ctx);
	}

	/*
	 * Gets a list of simple return type names for the given methodName in the class of the given
	 * context name. Returns an empty list if the context class or method does not exist.
	 */
	private List<String> discernReferenceMethodType(String contextName, String methodName) {
		ParserRuleContext ctxClass = refParser.loadRefContext(Strings.titleCase(contextName) + "Context");
		if (ctxClass == null) return Collections.emptyList();

		List<String> desc = Reflect.getMethodReturnDesc(ctxClass, methodName);
		for (int idx = 0; idx < desc.size(); idx++) {
			desc.set(idx, Strings.simpleName(desc.get(idx)));
		}
		return desc;
	}

	/*
	 * Gets a list of simple type names for the given fieldName in the class of the given context name.
	 * Returns an empty list if the context class or field does not exist.
	 */
	private List<String> discernReferenceVarType(String contextName, String fieldName) {
		ParserRuleContext ctxClass = refParser.loadRefContext(Strings.titleCase(contextName) + "Context");
		if (ctxClass == null) return Collections.emptyList();

		List<String> desc = Reflect.getFieldTypeNames(ctxClass, fieldName);
		for (int idx = 0; idx < desc.size(); idx++) {
			desc.set(idx, Strings.simpleName(desc.get(idx)));
		}
		return desc;
	}

	/*
	 * Gets a token type for the given type or literal name. The runtime processor will have to figure
	 * out the relevant context. Returns -1 if the token name does not correspond to a known parser
	 * token type.
	 */
	private String discernTokenOrLiteralType(String name) {
		String token = name;
		// convert literal to token name
		if (token.charAt(0) == '\'') {
			int end = token.length() > 1 ? token.length() - 1 : 0;
			if (end > 0 && token.charAt(end) == '\'') {
				token = token.substring(1, end);
			}
		}
		// check convert token name to token type
		Integer ttype = model.tokens.get(token);
		if (ttype == null) return null;
		return token;
	}

	/*
	 * Gets a rulename for the given literal name. The runtime processor will have to figure out the
	 * relevant context. Returns -1 if the rule name does not correspond to a known parser rule.
	 */
	private String discernRuleType(String contextName, String refName) {
		if (!contextName.equals(refName)) return null;
		Integer rIndex = model.rules.get(contextName);
		if (rIndex == null) return null;
		return contextName;
	}

	private void evalListPostfix(String[] refs) {
		if (refs.length == 2) {
			switch (refs[1]) {
				case "text":
					abText.append(".getText()");
					break;
				default:
					abText.append("." + refs[1]);
			}
		}
	}

	private void evalRulePostfix(String[] refs) {
		if (refs.length == 2) {
			switch (refs[1]) {
				case "text":
					abText.append(".getText()");
					break;
				case "line":
					abText.append(".getLine()");
					break;
				case "pos":
					abText.append(".getStart().getCharPositionInLine()");
					break;
				default:
					abText.append("." + refs[1]);
			}
		}
	}

	private void evalTokenPostfix(String[] refs) {
		if (refs.length == 2) {
			switch (refs[1]) {
				case "text":
					abText.append(".getText()");
					break;
				case "type":
					abText.append(".getType()");
					break;
				case "line":
					abText.append(".getLine()");
					break;
				case "pos":
					abText.append(".getCharPositionInLine()");
					break;
				case "index":
					abText.append(".getTokenIndex()");
					break;
				case "channel":
					abText.append(".getChannel()");
					break;
				default:
					abText.append("." + refs[1]);
			}
		}
	}

	@Override
	public CodeGenModel visitSeparator(SeparatorContext ctx) {
		model.addSeparator(ctx.ANY() != null, ctx.NOT() != null);
		return super.visitSeparator(ctx);
	}

	@Override
	public CodeGenModel visitWord(WordContext ctx) {
		if (ctx.ID() != null) {
			String name = ctx.ID().getText();
			model.addWordId(name);
			if (!model.isValidId(name)) {
				ErrorType eType = ErrorType.UNKNOWN_PARSER_RULE_REF;
				if (model.isTokenId(name)) {
					eType = ErrorType.UNKNOWN_LEXER_RULE_REF;
				}
				model.tool.getErrMgr().grammarError(eType, model.grammarFileName, ctx.ID().getSymbol(), name);
			}
		} else if (ctx.LITERAL() != null) {
			model.addWordLiteral(ctx.LITERAL().getText());
		} else if (ctx.STAR() != null) {
			model.addWordStar();
		}
		return super.visitWord(ctx);
	}
}
