/*
 * [The "BSD license"] Copyright (c) 2012 Terence Parr Copyright (c) 2012 Sam Harwell All rights
 * reserved. Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met: 1. Redistributions of source code must
 * retain the above copyright notice, this list of conditions and the following disclaimer. 2.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. 3. The name of the author may not be used to endorse or promote products derived
 * from this software without specific prior written permission. THIS SOFTWARE IS PROVIDED BY THE
 * AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 */
package net.certiv.antlr.xvisitor;

import java.util.Arrays;

import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.tool.Grammar;
import org.stringtemplate.v4.ST;

public class Messages {

	private static final Object[] EMPTY_ARGS = new Object[0];

	private final ErrorType errorType;
	private final Object[] args;
	private final Throwable e;

	// used for location template
	public String fileName;
	public int line = -1;
	public int charPosition = -1;
	public Grammar g;
	public Token offendingToken;

	public Messages(ErrorType errorType) {
		this(errorType, (Throwable) null, Tool.INVALID_TOKEN);
	}

	public Messages(ErrorType errorType, Token offendingToken, Object... args) {
		this(errorType, null, offendingToken, args);
	}

	public Messages(ErrorType errorType, Throwable e, Token offendingToken, Object... args) {
		this.errorType = errorType;
		this.e = e;
		this.args = args;
		this.offendingToken = offendingToken;
	}

	public ErrorType getErrorType() {
		return errorType;
	}

	public Object[] getArgs() {
		if (args == null) {
			return EMPTY_ARGS;
		}
		return args;
	}

	public ST getMessageTemplate(boolean verbose) {
		ST messageST = new ST(getErrorType().msg);
		messageST.impl.name = errorType.name();
		messageST.add("verbose", verbose);
		Object[] args = getArgs();
		for (int i = 0; i < args.length; i++) {
			String attr = "arg";
			if (i > 0) attr += i + 1;
			messageST.add(attr, args[i]);
		}
		if (args.length < 2) messageST.add("arg2", null); // some messages ref arg2

		Throwable cause = getCause();
		if (cause != null) {
			messageST.add("exception", cause);
			messageST.add("stackTrace", cause.getStackTrace());
		} else {
			messageST.add("exception", null); // avoid ST error msg
			messageST.add("stackTrace", null);
		}

		return messageST;
	}

	public Throwable getCause() {
		return e;
	}

	@Override
	public String toString() {
		return "Message{" + "errorType=" + getErrorType() + ", args=" + Arrays.asList(getArgs()) + ", e=" + getCause()
				+ ", fileName='" + fileName + '\'' + ", line=" + line + ", charPosition=" + charPosition + '}';
	}

	/**
	 * A generic message from the tool such as "file not found" type errors; there is no reason to
	 * create a special object for each error unlike the grammar errors, which may be rather
	 * complex. Sometimes you need to pass in a filename or something to say it is "bad". Allow a
	 * generic object to be passed in and the string template can deal with just printing it or
	 * pulling a property out of it.
	 */
	public static class ToolMessage extends Messages {
		public ToolMessage(ErrorType errorType) {
			super(errorType);
		}

		public ToolMessage(ErrorType errorType, Object... args) {
			super(errorType, null, Tool.INVALID_TOKEN, args);
		}

		public ToolMessage(ErrorType errorType, Throwable e, Object... args) {
			super(errorType, e, Tool.INVALID_TOKEN, args);
		}
	}

	/**
	 * A problem with the syntax of your antlr grammar such as
	 * "The '{' came as a complete surprise to me at this point in your program"
	 */
	public static class GrammarSyntaxMessage extends Messages {

		public GrammarSyntaxMessage(ErrorType etype, String fileName, Token offendingToken,
				RecognitionException antlrException, Object... args) {
			super(etype, antlrException, offendingToken, args);
			this.fileName = fileName;
			this.offendingToken = offendingToken;
			if (offendingToken != null) {
				line = offendingToken.getLine();
				charPosition = offendingToken.getCharPositionInLine();
			}
		}

		@Override
		public RecognitionException getCause() {
			return (RecognitionException) super.getCause();
		}
	}

	/**
	 * A problem with the symbols and/or meaning of a grammar such as rule redefinition. Any msg
	 * where we can point to a location in the grammar.
	 */
	public static class GrammarSemanticsMessage extends Messages {

		public GrammarSemanticsMessage(ErrorType etype, String fileName, Token offendingToken, Object... args) {
			super(etype, offendingToken, args);
			this.fileName = fileName;
			if (offendingToken != null) {
				line = offendingToken.getLine();
				charPosition = offendingToken.getCharPositionInLine();
			}
		}
	}
}
