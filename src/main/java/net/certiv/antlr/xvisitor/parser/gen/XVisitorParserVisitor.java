// Generated from D:/DevFiles/Eclipse/Tools/XVisitor/net.certiv.xvisitor/src/main/java/net/certiv/antlr/xvisitor/parser/XVisitorParser.g4 by ANTLR 4.12.0

	package net.certiv.antlr.xvisitor.parser.gen;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link XVisitorParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface XVisitorParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link XVisitorParser#grammarSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrammarSpec(XVisitorParser.GrammarSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link XVisitorParser#optionsSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptionsSpec(XVisitorParser.OptionsSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link XVisitorParser#option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOption(XVisitorParser.OptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link XVisitorParser#optionValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptionValue(XVisitorParser.OptionValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link XVisitorParser#action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAction(XVisitorParser.ActionContext ctx);
	/**
	 * Visit a parse tree produced by {@link XVisitorParser#xgroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXgroup(XVisitorParser.XgroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link XVisitorParser#xpath}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXpath(XVisitorParser.XpathContext ctx);
	/**
	 * Visit a parse tree produced by {@link XVisitorParser#xpathSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXpathSpec(XVisitorParser.XpathSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link XVisitorParser#actionBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionBlock(XVisitorParser.ActionBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link XVisitorParser#text}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitText(XVisitorParser.TextContext ctx);
	/**
	 * Visit a parse tree produced by {@link XVisitorParser#reference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReference(XVisitorParser.ReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link XVisitorParser#separator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeparator(XVisitorParser.SeparatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XVisitorParser#word}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWord(XVisitorParser.WordContext ctx);
}