/*******************************************************************************
 * Copyright (c) 2010-2015 Gerald Rosenberg & others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the standard 3-clause BSD License. A
 * copy of the License is provided with this distribution in the License.txt file.
 *******************************************************************************/
package net.certiv.antlr.xvisitor.codegen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Vocabulary;

import net.certiv.antlr.runtime.xvisitor.util.Strings;
import net.certiv.antlr.xvisitor.tool.ErrorType;

public class ReferenceParser {

	private static final int flags = Pattern.MULTILINE | Pattern.DOTALL;
	private static final Pattern packageDef = Pattern.compile("^\\s+package\\s+([\\w\\.]+)\\s*?;", flags);

	private CodeGenModel model;

	public ReferenceParser(CodeGenModel model) {
		this.model = model;
	}

	public boolean loadRefParser() {
		try {
			model.fqPackageName = fqPackage(model.getParserName());
			model.fqParserName = fqName(model.getPackageName(), model.getParserName());
			model.refParser = instantiate(model.fqParserName);
		} catch (IOException e) {
			model.tool.getErrMgr().toolError(ErrorType.CANNOT_FIND_IMPORTED_GRAMMAR, model.fqParserName);
			return false;
		}
		return true;
	}

	public boolean loadRuleNames() {
		int idx = 0;
		model.rules = new LinkedHashMap<>();
		for (String name : model.refParser.getRuleNames()) {
			model.rules.put(name, idx++);
		}
		model.tool.getErrMgr().info("Rules: " + model.rules.toString());
		return true;
	}

	public boolean loadTokenNames() {
		model.tokens = new LinkedHashMap<>();
		Vocabulary vocab = model.refParser.getVocabulary();
		int max = vocab.getMaxTokenType();
		for (int tt = 0; tt <= max; tt++) {
			String name = vocab.getSymbolicName(tt);
			if (name == null || name.isEmpty()) {
				name  = "<INVALID>";
			}
			model.tokens.put(name, tt);
		}
		model.tool.getErrMgr().info("Tokens: " + model.tokens.toString());
		return true;
	}

	public ParserRuleContext loadRefContext(String ctxName) {
		try {
			ParserRuleContext ctxClass = instantiate(model.fqParserName, ctxName);
			return ctxClass;
		} catch (IOException e) {
			model.tool.getErrMgr().toolError(ErrorType.UNDEFINED_RULE_REF, model.fqParserName + ":" + ctxName);
		}
		return null;
	}

	private String fqPackage(String name) throws IOException {
		String content = getJavaFileContents(name);
		String packagePath = regexPackage(content);
		if (packagePath != null) {
			return packagePath;
		}
		return "";
	}

	private String fqName(String pkgPath, String name) throws IOException {
		if (pkgPath.length() > 0) {
			return pkgPath + "." + name;
		}
		return name;
	}

	private Parser instantiate(String fqName) throws IOException {
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			Class<?> pClass = Class.forName(fqName, true, loader);
			Constructor<?> constructor = pClass.getConstructor(TokenStream.class);
			return (Parser) constructor.newInstance((TokenStream) null);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException
				| SecurityException | IllegalArgumentException | InvocationTargetException e) {
			throw new IOException("Reference parser instantiation failed (not on classpath)", e);
		}
	}

	/*
	 * Try to instantiate an inner class context of the parser. ClassNotFound,
	 * NoSuchMethod, and ClassCast exceptions are silently reported by returning a
	 * null value.
	 */
	private ParserRuleContext instantiate(String fqPackage, String contextName) throws IOException {
		try {
			String fqName = fqPackage.length() > 0 ? fqPackage + "$" + contextName : contextName;
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			Class<? extends ParserRuleContext> inner = Class.forName(fqName, true, loader)
					.asSubclass(ParserRuleContext.class);
			Constructor<? extends ParserRuleContext> ctor = inner.getConstructor(ParserRuleContext.class, int.class);
			return ctor.newInstance((ParserRuleContext) null, 0);
		} catch (ClassNotFoundException | NoClassDefFoundError | NoSuchMethodException | ClassCastException e) {
			return null;
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | IllegalArgumentException
				| SecurityException e) {
			throw new IOException("Reference parser context instantiation failed", e);
		}
	}

	private String regexPackage(String content) {
		Matcher m = packageDef.matcher(content);
		if (m.find()) {
			return m.group(1);
		}
		return null;
	}

	private String getJavaFileContents(String filename) throws IOException {
		File f = new File(model.tool.getLibDirectory(), File.separator + filename + ".java");
		if (f.exists()) {
			return Strings.readFile(f);
		}
		f = new File(model.tool.getOutputDirectory(), File.separator + filename + ".java");
		if (f.exists()) {
			return Strings.readFile(f);
		}
		throw new FileNotFoundException("Reference parser source file not found");
	}
}
