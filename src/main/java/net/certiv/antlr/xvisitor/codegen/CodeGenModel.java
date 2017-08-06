/*******************************************************************************
 * Copyright (c) 2010-2015 Gerald Rosenberg & others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the standard 3-clause BSD License.  A copy of the License
 * is provided with this distribution in the License.txt file.
 *******************************************************************************/
package net.certiv.antlr.xvisitor.codegen;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;

import net.certiv.antlr.runtime.xvisitor.util.Strings;
import net.certiv.antlr.runtime.xvisitor.xpath.Element;
import net.certiv.antlr.runtime.xvisitor.xpath.LiteralElement;
import net.certiv.antlr.runtime.xvisitor.xpath.RuleElement;
import net.certiv.antlr.runtime.xvisitor.xpath.TokenElement;
import net.certiv.antlr.runtime.xvisitor.xpath.WildcardElement;
import net.certiv.antlr.xvisitor.Tool;
import net.certiv.antlr.xvisitor.tool.ErrorType;
import net.certiv.antlr.xvisitor.tool.ITool;

public class CodeGenModel {

	private static final int BAD_TYPE = -2;

	public ITool tool; // codegen tool
	public Parser parser; // codegen parser
	public ParseTree tree; // codegen parse tree

	public String grammarName; // given name of the input grammar
	public String parserName; // parser name according to the options value
	public String fqPackageName; // fully qualified package name
	public String fqParserName; // fully qualified parser name
	public Parser refParser; // reference parser - builds the runtime parse tree

	// public ParserRuleContext ctxClass; // current reference context class

	public Map<String, Integer> rules; // reference rule names read from parser
	public Map<String, Integer> tokens; // reference tokens read from parser
	// public Map<String, Class<?>> classes; // classes declared in the ref parser

	public Map<String, String> options = new LinkedHashMap<>();
	public Map<String, String> actions = new LinkedHashMap<>();
	public Map<String, String> actionBlocks = new LinkedHashMap<>();

	public String mainRule; // name of the mainRule rule
	public Map<String, List<Element>> paths; // named lists of xpath elements

	// values being collected in the process of creatng a new path spec
	private String inprocName;
	private List<Element> inprocPath;
	private boolean inprocAny;
	private boolean inprocInvert;
	private String inprocLastPathContext;

	public CodeGenModel(ITool tool, Parser parser, ParseTree tree) {
		super();
		this.tool = tool;
		this.parser = parser;
		this.tree = tree;
		this.paths = new LinkedHashMap<>();
	}

	// ====== Model Use Methods =========================================================

	public String sourceFileName;
	public String grammarFileName;
	public String version;
	public String header;
	public String superClass;
	public String grammarClass;
	public String members;

	public void update() {
		sourceFileName = parser.getSourceName(); // ??
		grammarClass = grammarName + "Visitor";
		grammarFileName = grammarClass + ".java";
		version = Tool.VERSION;
		header = Strings.trimBraces(actions.get("header"));
		members = Strings.trimBraces(actions.get("members"));

		superClass = options.get(Tool.SUPER_CLASS);
		if (superClass == null) {
			superClass = "Processor";
		}
	}

	public String getSourceName() {
		return parser.getSourceName();
	}

	public String getPackageName() {
		return fqPackageName;
	}

	public String getParserName() {
		if (parserName == null || parserName.length() == 0) {
			parserName = options.get(Tool.PARSER_CLASS);
			if (parserName == null || parserName.length() == 0) {
				tool.getErrMgr().toolError(ErrorType.INVALID_CMDLINE_ARG,
						"Reference parser name not specified in the options");
				parserName = "<Unknown parser name>";
			}
		}
		return parserName;
	}

	/**
	 * Returns the last path segment name.
	 */
	public String getLastContextName() {
		return inprocLastPathContext;
	}

	/**
	 * Returns the path segment name at the given prior number of segments from the last. If prior
	 * is '0', returns the same segment as getLastContextName(). If prior references a non-existant
	 * segment, returns the first segment name.
	 */
	public String getLastContextName(int prior) {
		int idx = Math.abs(prior);
		idx = Math.min(idx, inprocPath.size() - 1);
		Element ePrior = inprocPath.get(inprocPath.size() - 1 - idx);
		return ePrior.getIdent();
	}

	// ====== Model Creation Methods ====================================================

	public void setGrammarName(String name) {
		this.grammarName = name;
		tool.getErrMgr().info("Grammar name: " + name);
	}

	public void addOption(String lhs, String rhs) {
		options.put(lhs, rhs);
		tool.getErrMgr().info("Option set: " + lhs + " => " + rhs);
	}

	public void addAction(String name, String value) {
		actions.put(name, value);
		tool.getErrMgr().info("Action: " + name + "=" + value);
	}

	public void addActionBlock(String name, String content) {
		String ex = actionBlocks.get(name);
		if (ex == null) {
			ex = "";
		} else {
			ex = ex + "\r\n";
		}
		actionBlocks.put(name, ex + Strings.trimBraces(content));
		tool.getErrMgr().info("ActionBlock: " + name + "[[" + ex + content + "]]");
	}

	public void setMain(String name) {
		this.mainRule = name;
		tool.getErrMgr().info("Main rule name: " + name);
	}

	public void addPathName(String name) {
		ArrayList<Element> path = new ArrayList<>();
		paths.put(name, path);
		tool.getErrMgr().info("PathName: " + name);
	}

	public void startPath(String name) {
		this.inprocName = name;
		tool.getErrMgr().info("StartPath: " + name);
		this.inprocPath = paths.get(name);
		if (this.inprocPath == null) {
			tool.getErrMgr().info("Main rule alts do not include: " + name);
			addPathName(name);
			this.inprocPath = paths.get(name);
		}
	}

	public void addSeparator(boolean gap, boolean invert) {
		this.inprocAny = gap;
		this.inprocInvert = invert;
		String g = gap ? "//" : "/";
		String i = invert ? "!" : "";
		tool.getErrMgr().info("Separator: " + g + i);
	}

	public void addWordId(String name) {
		if (isTokenId(name)) {
			Integer value = tokens.get(name);
			if (value == null) {
				value = tokens.get(name.toUpperCase());
				if (value == null) {
					value = BAD_TYPE;
				}
			}
			inprocPath.add(new TokenElement(inprocAny, inprocInvert, name, value));
			tool.getErrMgr().info("Token: " + name + "[" + value + "]");
		} else {
			Integer value = rules.get(name);
			if (value == null) {
				value = rules.get(name.toLowerCase());
				if (value == null) {
					value = BAD_TYPE;
				}
			}
			inprocPath.add(new RuleElement(inprocAny, inprocInvert, name, value));
			tool.getErrMgr().info("Rule: " + name + "[" + value + "]");
		}
	}

	public boolean isValidId(String name) {
		return isValidParserId(name) || isValidLexerId(name);
	}

	/**
	 * Determine if the given parser rule name is known.
	 * 
	 * @param name an ID name
	 * @return true if a known rule name
	 */
	public boolean isValidParserId(String name) {
		if (isRuleId(name)) {
			Integer value = rules.get(name);
			if (value != null) return true;
		}
		return false;
	}

	public boolean isValidLexerId(String name) {
		if (isTokenId(name)) {
			Integer value = tokens.get(name);
			if (value != null) return true;
		}
		return false;
	}

	public boolean isRuleId(String name) {
		if (!Character.isUpperCase(name.charAt(0))) return true;
		return false;
	}

	public boolean isTokenId(String name) {
		if (Character.isUpperCase(name.charAt(0))) return true;
		return false;
	}

	public String getTokenName(int ttype) {
		for (Entry<String, Integer> entry : tokens.entrySet()) {
			if (entry.getValue() == ttype) return entry.getKey();
		}
		return null;
	}

	public void addWordLiteral(String literal) {
		inprocPath.add(new LiteralElement(inprocAny, inprocInvert, Strings.trimQuotes(literal)));
		tool.getErrMgr().info("Literal: " + Strings.trimQuotes(literal));
	}

	public void addWordStar() {
		inprocPath.add(new WildcardElement(inprocAny, inprocInvert));
		tool.getErrMgr().info("Wildcard");
	}

	public void finishPath() {
		if (inprocPath.size() == 0) {
			paths.remove(inprocName);
		} else {
			paths.put(inprocName, inprocPath);
			// need to preserve last element of inprocPath as terminal spec context name
			// need to evaluate this name when processing a REFERENCE to determine the
			// type of the REFERENCE value
			Element last = inprocPath.get(inprocPath.size() - 1);
			inprocLastPathContext = last.getIdent();
		}
		tool.getErrMgr().info("Done.");
	}

	public boolean validates() {
		for (String name : paths.keySet()) {
			for (Element e : paths.get(name)) {
				if (e instanceof RuleElement || e instanceof TokenElement) {
					if (e.getValue() == BAD_TYPE) {
						tool.getErrMgr().info("Model validation failed.  Unknown type: " + e.getIdent());
						return false;
					}
				}
			}
		}
		return true;
	}
}
