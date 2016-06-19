/*******************************************************************************
 * Copyright (c) 2010-2015 Gerald Rosenberg & others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the standard 3-clause BSD License. A
 * copy of the License is provided with this distribution in the License.txt file. <br>
 * 2014.03.26: First release level code <br>
 * 2014.08.26: Updates, add Tests support <br>
 * 2014.12.14: updated to 4.4, to log4j2, added errorlistener<br>
 * 2015.07.17: updated to 4.5.1
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
import java.util.List;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;

import net.certiv.antlr.runtime.xvisitor.util.Reflect;
import net.certiv.antlr.xvisitor.codegen.CodeGenModel;
import net.certiv.antlr.xvisitor.codegen.CodeGenerator;
import net.certiv.antlr.xvisitor.codegen.ModelBuilder;
import net.certiv.antlr.xvisitor.parser.ParserErrorListener;
import net.certiv.antlr.xvisitor.parser.gen.XVisitorLexer;
import net.certiv.antlr.xvisitor.parser.gen.XVisitorParser;

public class Tool extends ToolBase {

	public static final String VERSION = "4.5.3";
	public static final String GRAMMAR_EXTENSION = ".xv";

	public static enum OptionArgType {
		NONE, // NONE implies boolean
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

	public static Option[] optionDefs = {
			new Option("outputDirectory", "-o", OptionArgType.STRING, "directory where all output is generated"),
			new Option("libDirectory", "-lib", OptionArgType.STRING, "location of grammars, tokens files"),
			new Option("level", "-v", OptionArgType.STRING, "verbosity (one of 'quiet', 'info', 'warn', 'error')"), //
	};

	// fields set by option manager
	public String outputDirectory;
	public String libDirectory;
	public String level;

	protected boolean haveOutputDir;
	protected List<String> grammarFiles = new ArrayList<String>();
	protected StringWriter stringWriter;

	public static void main(String[] args) {
		Tool xtree = new Tool();
		if (args.length == 0) {
			xtree.help();
			xtree.exit(0);
		}
		boolean ok = xtree.processFlags(args);
		if (!ok) {
			xtree.exit(1);
		}
		xtree.genModel();
		xtree.exit(0);
	}

	/** Create an instance of the tool, pending configuration and use. */
	public Tool() {
		super();
	}

	/**
	 * Create an instance of the tool configured using command-line styled arguments and then
	 * execute for file generation.
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

		if (level != null) {
			try {
				getDefaultListener().setLevel(Level.valueOf(level.trim().toUpperCase()));
			} catch (IllegalArgumentException e) {
				errMgr.toolError(ErrorType.INVALID_VERBOSE_LEVEL, level);
			}
		}

		if (outputDirectory != null) {
			if (outputDirectory.endsWith("/") || outputDirectory.endsWith("\\")) {
				outputDirectory = outputDirectory.substring(0, outputDirectory.length() - 1);
			}
			File outDir = new File(outputDirectory);
			this.haveOutputDir = true;
			if (outDir.exists() && !outDir.isDirectory()) {
				errMgr.toolError(ErrorType.OUTPUT_DIR_IS_FILE, outputDirectory);
				libDirectory = ".";
				ok = false;
			}
		} else {
			outputDirectory = ".";
		}
		if (libDirectory != null) {
			if (libDirectory.endsWith("/") || libDirectory.endsWith("\\")) {
				libDirectory = libDirectory.substring(0, libDirectory.length() - 1);
			}
			File outDir = new File(libDirectory);
			if (!outDir.exists()) {
				errMgr.toolError(ErrorType.DIR_NOT_FOUND, libDirectory);
				libDirectory = ".";
				ok = false;
			}
		} else {
			libDirectory = ".";
		}

		return ok;
	}

	// parse the grammar(s) and assemble model data for the code generator
	public void genModel() {
		for (XVisitorParser parser : loadGrammars(grammarFiles)) {
			ParseTree tree = null;
			try {
				tree = parser.grammarSpec();
				if (tree == null || tree instanceof ErrorNode) {
					errMgr.toolError(ErrorType.PARSE_ERROR, parser.getSourceName());
					continue;
				}
			} catch (RecognitionException e) {
				errMgr.toolError(ErrorType.PARSE_FAILURE, parser.getSourceName(), e);
				continue;
			}
			CodeGenModel model = new CodeGenModel(this, parser, tree);
			ModelBuilder builder = new ModelBuilder(model);
			try {
				builder.visit(tree);
			} catch (Exception e) {
				errMgr.toolError(ErrorType.MODEL_BUILD_FAILURE, parser.getSourceName(), e);
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
		CharStream input = new ANTLRFileStream(filename);
		XVisitorLexer lexer = new XVisitorLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		XVisitorParser parser = new XVisitorParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(new ParserErrorListener());
		return parser;
	}

	@Override
	public String getLibDirectory() {
		return libDirectory;
	}

	@Override
	public String getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
		this.haveOutputDir = outputDirectory != null;
	}

	public void setLibDirectory(String libDirectory) {
		this.libDirectory = libDirectory;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setGrammarFiles(List<String> grammarFiles) {
		this.grammarFiles = grammarFiles;
	}

	public String getStringOutput() {
		if (stringWriter != null) return stringWriter.toString();
		return "";
	}

	/**
	 * This method is used by all code generators to create new output files. If the outputDir set
	 * by -o is not present it will be created. The final filename is sensitive to the output
	 * directory and the directory where the grammar file was found. If -o is /tmp and the original
	 * grammar file was foo/t.g4 then output files go in /tmp/foo. The output dir -o spec takes
	 * precedence if it's absolute. E.g., if the grammar file dir is absolute the output dir is
	 * given precendence. "-o /tmp /usr/lib/t.g4" results in "/tmp/T.java" as output (assuming t.g4
	 * holds T.java). If no -o is specified, then just write to the directory where the grammar file
	 * was found. If outputDirectory==null then write a Literal.
	 */
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
	 * Return the location where ANTLR will generate output files for a given file. This is a base
	 * directory and output files will be relative to here in some cases such as when -o option is
	 * used and input files are given relative to the input directory.
	 * 
	 * @param fileNameWithPath pathContexts to input source
	 */
	private File getOutputDirectory(String fileNameWithPath) {
		File file = new File(fileNameWithPath);
		File fileDir = file.getParentFile() != null ? file.getParentFile() : new File(".");
		File outputDir;

		if (haveOutputDir) {
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

		////////////////////////////////////////////
		//
		// String fileDirectory;
		//
		// // Some files are given to us without a PATH but should should
		// // still be written to the output directory in the relative pathContexts of
		// // the output directory. The file directory is either the set of sub directories
		// // or just or the relative pathContexts recorded for the parent grammar. This
		// // means that when we write the tokens files, or the .java files for imported grammars
		// // that we will write them in the correct place.
		// fileNameWithPath = fileNameWithPath.replaceAll("\\\\", "/");
		// if (fileNameWithPath.lastIndexOf('/') == -1) {
		// // No pathContexts is included in the file name, so make the file
		// // directory the same as the parent grammar (which might sitll be just ""
		// // but when it is not, we will write the file in the correct place.
		// fileDirectory = ".";
		//
		// } else {
		// fileDirectory = fileNameWithPath.substring(0, fileNameWithPath.lastIndexOf('/'));
		// }
		// if (haveOutputDir) {
		// // -o /tmp /var/lib/t.g4 => /tmp/T.java
		// // -o subdir/output /usr/lib/t.g4 => subdir/output/T.java
		// // -o . /usr/lib/t.g4 => ./T.java
		// if (fileDirectory != null && (new File(fileDirectory).isAbsolute() ||
		// fileDirectory.startsWith("~"))) {
		// // isAbsolute doesn't count this :(
		// // somebody set the dir, it takes precendence; write new file there
		// outputDir = new File(outputDirectory);
		// } else {
		// // -o /tmp subdir/t.g4 => /tmp/subdir/t.g4
		// if (fileDirectory != null) {
		// outputDir = new File(outputDirectory, fileDirectory);
		// } else {
		// outputDir = new File(outputDirectory);
		// }
		// }
		// } else {
		// // they didn't specify a -o dir so just write to location
		// // where grammar is, absolute or relative, this will only happen
		// // with command line invocation as build tools will always
		// // supply an output directory.
		// outputDir = new File(fileDirectory);
		// }
		return outputDir;
	}

	public void help() {
		version();
		for (Option o : optionDefs) {
			String name = o.name + (o.argType != OptionArgType.NONE ? " ___" : "");
			String s = String.format(" %-19s %s", name, o.description);
			info(s);
		}
	}

	@Override
	public void version() {
		info("XVisitor CodeGenerator Version " + VERSION);
	}

	public void exit(int e) {
		System.exit(e);
	}
}
