/*******************************************************************************
 * Copyright (c) 2010-2017 Gerald Rosenberg & others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the standard 3-clause BSD License. A copy of the License is
 * provided with this distribution in the License.txt file.
 *******************************************************************************/
package net.certiv.antlr.xvisitor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.LogManager;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;

import net.certiv.antlr.runtime.xvisitor.util.Reflect;
import net.certiv.antlr.runtime.xvisitor.util.Strings;
import net.certiv.antlr.xvisitor.codegen.CodeGenModel;
import net.certiv.antlr.xvisitor.codegen.CodeGenerator;
import net.certiv.antlr.xvisitor.codegen.ModelBuilder;
import net.certiv.antlr.xvisitor.parser.ParserErrorListener;
import net.certiv.antlr.xvisitor.parser.gen.XVisitorLexer;
import net.certiv.antlr.xvisitor.parser.gen.XVisitorParser;
import net.certiv.antlr.xvisitor.tool.DefaultToolListener;
import net.certiv.antlr.xvisitor.tool.ErrorManager;
import net.certiv.antlr.xvisitor.tool.ErrorType;
import net.certiv.antlr.xvisitor.tool.Level;
import net.certiv.antlr.xvisitor.tool.Messages;

public class Tool implements ITool, IToolListener {

	public static final String VERSION = "4.8.0";
	public static final String GRAMMAR_EXTENSION = ".xv";
	public static final String MSG_FORMAT = "antlr";
	public static final Token INVALID_TOKEN = new CommonToken(Token.INVALID_TYPE);

	// defined options available in the grammar
	public static final String SUPER_CLASS = "superClass";
	public static final String PARSER_CLASS = "parserClass";
	public static final String TOKEN_CLASS = "tokenClass";

	public enum OptionArgType {
		NONE, // also boolean
		STRING
	}

	public static class Option {

		String fieldName;
		String name;
		OptionArgType argType;
		String description;

		public Option(String fieldName, String name, String description) {
			this(fieldName, name, OptionArgType.NONE, description);
		}

		public Option(String fieldName, String name, OptionArgType argType, String description) {
			this.fieldName = fieldName;
			this.name = name;
			this.argType = argType;
			this.description = description;
		}
	}

	// -------------------

	public static Option[] optionDefs = {
			new Option("outputDirectory", "-o", OptionArgType.STRING, "directory where all output is generated"),
			new Option("libDirectory", "-lib", OptionArgType.STRING, "location of grammars, tokens files"),
			new Option("level", "-v", OptionArgType.STRING, "verbosity [quiet|info|warn|error])"), //
	};

	// fields set by option manager
	public String outputDirectory;
	public String libDirectory;
	public String level;

	// -------------------

	private List<IToolListener> listeners = new CopyOnWriteArrayList<>();
	private DefaultToolListener defaultListener;

	public ErrorManager errMgr;
	public LogManager logMgr = new LogManager();

	public Level lvl;

	protected boolean hasOutputDir;
	protected List<String> grammarFiles = new ArrayList<>();
	protected StringWriter stringWriter;

	public static void main(String[] args) {
		Tool xtree = new Tool();
		if (args.length == 0) {
			xtree.help();
			xtree.exit(0);
		}
		boolean ok = xtree.processFlags(args);
		if (!ok) xtree.exit(1);

		xtree.genModel();
		xtree.exit(0);
	}

	/** Create an instance of the tool, pending configuration and use. */
	public Tool() {
		super();
		errMgr = new ErrorManager(this);
		errMgr.setFormat(MSG_FORMAT);
		defaultListener = new DefaultToolListener(this);
	}

	/**
	 * Create an instance of the tool configured using command-line styled arguments
	 * and then execute for file generation.
	 *
	 * @param args command-line styled arguments
	 */
	public Tool(String[] args) {
		this();
		boolean ok = processFlags(args);
		if (ok) genModel();
	}

	/**
	 * Configure the tool using command-line styled arguments.
	 *
	 * @param args command-line styled arguments
	 * @return true iff the command-line styled arguments are valid
	 */
	public boolean processFlags(String[] args) {
		boolean ok = true;
		for (int idx = 0; idx < args.length; idx++) {
			String arg = args[idx];

			if (arg.charAt(0) != '-') { // must be a file name
				if (!grammarFiles.contains(arg)) grammarFiles.add(arg);
				continue;
			}

			boolean found = false;
			for (Option o : optionDefs) {
				if (arg.equals(o.name)) {
					found = true;
					if (o.argType == OptionArgType.STRING) {
						idx++;
						String argValue = args[idx];
						Reflect.set(this, o.fieldName, argValue);
					}
				}
			}
			if (!found) {
				errMgr.toolError(ErrorType.INVALID_CMDLINE_ARG, arg);
				ok = false;
			}
		}

		ok &= setLevel(level);
		ok &= setOutputDirectory(outputDirectory);
		ok &= setLibDirectory(libDirectory);
		return ok;
	}

	// parse the grammar(s) and assemble model data for the code generator
	public void genModel() {
		for (XVisitorParser parser : loadGrammars(grammarFiles)) {
			ParseTree tree = null;
			try {
				tree = parser.grammarSpec();
				if (tree == null || tree instanceof ErrorNode) {
					errMgr.toolError(ErrorType.PARSE_FAILURE, parser.getSourceName());
					continue;
				}
			} catch (RecognitionException e) {
				errMgr.toolError(ErrorType.PARSE_RECOGNITION_ERROR, parser.getSourceName(), e);
				continue;
			}

			// TODO: implement tree validator

			CodeGenModel model = new CodeGenModel(this, parser, tree);
			ModelBuilder builder = new ModelBuilder(model);
			try {
				builder.visit(tree);
			} catch (Exception e) {
				errMgr.toolError(ErrorType.MODEL_BUILD_FAILURE, parser.getSourceName(), e);
				errMgr.toolError(ErrorType.CLASSLOAD_FAILURE, model.getPackages(), e);
				return;
			}
			if (model.validates()) {
				model.update();
				CodeGenerator xgen = new CodeGenerator(model);
				xgen.generate();
			}
		}
	}

	private List<XVisitorParser> loadGrammars(List<String> filenames) {
		List<XVisitorParser> parsers = new ArrayList<>();
		for (String fileName : filenames) {
			try {
				XVisitorParser parser = loadGrammar(fileName);
				if (parser == null) continue; // came back as error
				parsers.add(parser);
			} catch (IOException e) {
				errMgr.toolError(ErrorType.CANNOT_OPEN_FILE, e, fileName);
			}
		}
		return parsers;
	}

	private XVisitorParser loadGrammar(String filename) throws IOException {
		info("Parsing " + filename);

		File file = new File(filename);
		if (!file.exists()) {
			throw new IOException("source grammar does not exist: " + filename);
		}

		CharStream input = CharStreams.fromFileName(filename);
		XVisitorLexer lexer = new XVisitorLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		XVisitorParser parser = new XVisitorParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(new ParserErrorListener());
		return parser;
	}

	public boolean setLevel(String level) {
		this.level = level == null ? level : Level.QUIET.name();
		try {
			lvl = Level.valueOf(level.trim().toUpperCase());
			return true;

		} catch (IllegalArgumentException e) {
			errMgr.toolError(ErrorType.INVALID_VERBOSE_LEVEL, level);
			return false;
		}
	}

	public boolean setOutputDirectory(String out) {
		if (out == null) out = Strings.DOT;
		if (out.endsWith(Strings.SLASH) || out.endsWith(Strings.ESC)) {
			out = out.substring(0, out.length() - 1);
		}
		File dir = new File(out);
		hasOutputDir = dir.isDirectory();
		if (dir.exists() && !dir.isDirectory()) {
			errMgr.toolError(ErrorType.OUTPUT_DIR_IS_FILE, out);
			return false;
		}
		outputDirectory = out;
		if (libDirectory == null) libDirectory = out;
		return true;
	}

	public boolean setLibDirectory(String lib) {
		if (lib == null) lib = Strings.DOT;
		if (lib.endsWith(Strings.SLASH) || lib.endsWith(Strings.ESC)) {
			lib = lib.substring(0, lib.length() - 1);
		}
		File dir = new File(lib);
		if (!dir.isDirectory()) {
			errMgr.toolError(ErrorType.DIR_NOT_FOUND, lib);
			return false;
		}
		libDirectory = lib;
		return true;
	}

	public void setGrammarFiles(List<String> grammarFiles) {
		this.grammarFiles = grammarFiles;
	}

	public void setGrammarFiles(String... grammarFiles) {
		this.grammarFiles = Arrays.asList(grammarFiles);
	}

	@Override
	public String getOutputDirectory() {
		return outputDirectory;
	}

	@Override
	public String getLibDirectory() {
		return libDirectory;
	}

	public String getStringOutput() {
		if (stringWriter != null) return stringWriter.toString();
		return Strings.EMPTY;
	}

	/**
	 * This method is used by all code generators to create new output files. If the
	 * outputDir set by -o is not present it will be created. The final filename is
	 * sensitive to the output directory and the directory where the grammar file
	 * was found. If -o is /tmp and the original grammar file was foo/t.g4 then
	 * output files go in /tmp/foo. The output dir -o spec takes precedence if it's
	 * absolute. E.g., if the grammar file dir is absolute the output dir is given
	 * precendence. "-o /tmp /usr/lib/t.g4" results in "/tmp/T.java" as output
	 * (assuming t.g4 holds T.java). If no -o is specified, then just write to the
	 * directory where the grammar file was found. If outputDirectory==null then
	 * write a Literal.
	 */
	@Override
	public Writer getOutputFileWriter(CodeGenModel model) throws IOException {
		if (outputDirectory == null) {
			if (stringWriter == null) stringWriter = new StringWriter();
			return stringWriter;
		}

		// output directory is a function of where the grammar file lives
		// for subdir/T.g4, you get subdir here. Well, depends on -o etc...
		File outputDir = getOutputDirectory(model.getSourceName());
		File outputFile = new File(outputDir, model.grammarFileName);

		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}
		FileOutputStream fos = new FileOutputStream(outputFile);
		OutputStreamWriter osw = new OutputStreamWriter(fos);

		return new BufferedWriter(osw);
	}

	/**
	 * Return the location where ANTLR will generate output files for a given file.
	 * This is a base directory and output files will be relative to here in some
	 * cases such as when -o option is used and input files are given relative to
	 * the input directory.
	 *
	 * @param fileNameWithPath pathContexts to input source
	 */
	private File getOutputDirectory(String fileNameWithPath) {
		File file = new File(fileNameWithPath);
		File fileDir = file.getParentFile() != null ? file.getParentFile() : new File(Strings.DOT);
		File outputDir;

		if (hasOutputDir) {
			// -o /tmp /var/lib/t.g4 => /tmp/T.java
			// -o subdir/output /usr/lib/t.g4 => subdir/output/T.java
			// -o . /usr/lib/t.g4 => ./T.java
			if (fileDir.isAbsolute() || fileDir.getPath().startsWith("~")) {
				// isAbsolute doesn't count this :(
				// somebody set the dir, it takes precendence; write new file there
				outputDir = new File(outputDirectory);
			} else {
				// -o /tmp subdir/t.g4 => /tmp/subdir/t.g4
				outputDir = new File(outputDirectory, fileDir.getPath());
			}
		} else {
			// they didn't specify a -o dir so just write to location
			// where grammar is, absolute or relative, this will only happen
			// with command line invocation as build tools will always
			// supply an output directory.
			outputDir = fileDir;
		}

		return outputDir;
	}

	@Override
	public ErrorManager getErrMgr() {
		return errMgr;
	}

	protected DefaultToolListener getDefaultListener() {
		return defaultListener;
	}

	public void log(String msg) {
		log(null, msg);
	}

	public void log(String component, String msg) {
		logMgr.log(component, msg);
	}

	@Override
	public void info(String msg) {
		if (skip(Level.INFO)) return;
		if (listeners.isEmpty()) {
			defaultListener.info(msg);
		} else {
			for (IToolListener l : listeners) {
				l.info(msg);
			}
		}
	}

	@Override
	public void warn(Messages msg) {
		if (skip(Level.WARN)) return;
		if (listeners.isEmpty()) {
			defaultListener.warn(msg);
		} else {
			for (IToolListener l : listeners) {
				l.warn(msg);
			}
		}
	}

	@Override
	public void error(Messages msg) {
		if (skip(Level.ERROR)) return;
		if (listeners.isEmpty()) {
			defaultListener.error(msg);
		} else {
			for (IToolListener l : listeners) {
				l.error(msg);
			}
		}
	}

	private boolean skip(Level target) {
		switch (lvl) {
			default:
			case INFO:
				return false;
			case WARN:
				if (target != Level.INFO) return false;
				return true;
			case ERROR:
				if (target == Level.ERROR) return false;
				return true;
			case QUIET:
				return true;
		}
	}

	public int getNumWarnings() {
		return errMgr.getNumWarnings();
	}

	public int getNumErrors() {
		return errMgr.getNumErrors();
	}

	public List<IToolListener> getListeners() {
		return listeners;
	}

	public void addListener(IToolListener tl) {
		if (tl != null) listeners.add(tl);
	}

	public void removeListener(IToolListener tl) {
		listeners.remove(tl);
	}

	public void removeListeners() {
		listeners.clear();
	}

	public void help() {
		version();
		for (Option o : optionDefs) {
			String name = o.name + (o.argType != OptionArgType.NONE ? " ___" : "");
			String s = String.format(" %-19s %s", name, o.description);
			info(s);
		}
	}

	public void version() {
		info("XVisitor CodeGenerator Version " + VERSION);
	}

	public void exit(int e) {
		System.exit(e);
	}
}
