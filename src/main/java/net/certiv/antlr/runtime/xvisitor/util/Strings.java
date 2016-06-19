/*******************************************************************************
 * Copyright (c) 2008-2014 G Rosenberg. All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package net.certiv.antlr.runtime.xvisitor.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Strings {

	public static final String CLASSPATH = System.getProperty("java.class.path");
	public static final String tmpDir = System.getProperty("java.io.tmpdir");
	public static final String lineSep = System.getProperty("line.separator");
	public static final String pathSep = System.getProperty("pathContexts.separator");

	private Strings() {}

	public static String readFile(String pathname) throws IOException {
		return readFile(new File(pathname));
	}

	public static String readFile(File file) throws IOException {
		StringBuilder fileContents = new StringBuilder((int) file.length());

		try (Scanner scanner = new Scanner((Readable) new BufferedReader(new FileReader(file)))) {
			while (scanner.hasNextLine()) {
				fileContents.append(scanner.nextLine()).append(lineSep);
			}
			return fileContents.toString();
		}
	}

	public static String trimQuotes(String literal) {
		if (literal == null) return "";
		String str = literal.trim();
		if (str.startsWith("'") || str.startsWith("\"")) {
			str = str.substring(1);
			if (str.endsWith("'") || str.endsWith("\"")) {
				str = str.substring(0, str.length() - 1);
			}
		}
		return str.trim();
	}

	/**
	 * Only trims a trailing brace if a leading brace was found.
	 * 
	 * @param block
	 * @return
	 */
	public static String trimBraces(String block) {
		if (block == null) return "";
		String str = block.trim();
		if (str.startsWith("{")) {
			str = str.substring(1);
			if (str.endsWith("}")) {
				str = str.substring(0, str.length() - 1);
			}
		}
		return str.trim();
	}

	public static String getCurrentDirectory() {
		File f = new File("");
		return f.getAbsolutePath();
	}

	// does not appear to work, at least within an Eclipse launch
	public static boolean setCurrentDirectory(String directory_name) {
		boolean result = false;
		File directory = new File(directory_name).getAbsoluteFile();
		if (directory.exists() || directory.mkdirs()) {
			result = (System.setProperty("user.dir", directory.getAbsolutePath()) != null);
		}
		return result;
	}

	public static String tmpDir(Object target) {
		long time = System.currentTimeMillis();
		File f = new File(tmpDir, target.getClass().getSimpleName() + "-" + time);
		if (f.exists() || f.mkdirs()) {
			return f.getAbsolutePath();
		}
		return "/tmp";
	}

	public static String titleCase(String word) {
		if (word == null || word.length() == 0) return "";
		if (word.length() == 1) return word.toUpperCase();
		return word.substring(0, 1).toUpperCase() + word.substring(1);
	}

	/**
	 * Return the simple name of a fully qualified name.
	 * 
	 * @param fqName
	 * @return
	 */
	public static String simpleName(String fqName) {
		if (fqName == null || fqName.length() == 0) return "";
		int dot = fqName.lastIndexOf('.');
		if (fqName.length() - 1 == dot) return "";
		return fqName.substring(dot + 1);
	}
}
