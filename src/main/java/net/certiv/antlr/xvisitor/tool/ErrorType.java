package net.certiv.antlr.xvisitor.tool;

/**
 * A complex enumeration of all the error messages that the tool can issue.
 */
public enum ErrorType {

	CANNOT_WRITE_FILE(1, "cannot write file <arg>: <arg2>", ErrorSeverity.ERROR),
	INVALID_CMDLINE_ARG(2, "unknown command-line option <arg>", ErrorSeverity.ERROR),

	DIR_NOT_FOUND(5, "directory not found: <arg>", ErrorSeverity.ERROR),
	OUTPUT_DIR_IS_FILE(6, "output directory is a file: <arg>", ErrorSeverity.ERROR),
	CANNOT_OPEN_FILE(7, "cannot find or open file: <arg><if(exception&&verbose)>; reason: <exception><endif>",
			ErrorSeverity.ERROR),

	PARSE_FAILURE(10, "Failed to parse: <arg>", ErrorSeverity.ERROR),
	PARSE_RECOGNITION_ERROR(11, "Error in parse: <arg>", ErrorSeverity.ERROR),
	MODEL_BUILD_FAILURE(12, "Failed model construction: <arg>", ErrorSeverity.ERROR),
	INVALID_VERBOSE_LEVEL(13, "Invalid verbosity level: <arg>", ErrorSeverity.ERROR),

	GRAMMAR_NAME_MISMATCH(15, "grammar name <arg> and file name <arg2> differ", ErrorSeverity.ERROR),
	GRAMMAR_IMPORT_ERROR(16, "error reading imported grammar <arg> referenced in <arg2>", ErrorSeverity.ERROR),

	REFERENCE_PARSER_LOAD_FAILED(17, "failure in loading the reference parser <arg>", ErrorSeverity.ERROR),
	CLASSLOAD_FAILURE(18, "Classload failed: <arg>", ErrorSeverity.ERROR),

	INTERNAL_ERROR(20,
			"internal error: <arg> <arg2><if(exception&&verbose)>: <exception>"
					+ "<stackTrace; separator=\"\\n\"><endif>",
			ErrorSeverity.ERROR),

	STRING_TEMPLATE_WARNING(22,
			"template error: <arg> <arg2><if(exception&&verbose)>: <exception>"
					+ "<stackTrace; separator=\"\\n\"><endif>",
			ErrorSeverity.WARNING),

	/* Code generation errors */

	CODE_GEN_TEMPLATES_MISSING(30, "can't find code generation templates: <arg>", ErrorSeverity.ERROR),
	CODE_GEN_TEMPLATE_ARG_ISSUE(32,
			"code generation template <arg> has missing, misnamed, or incomplete arg list; missing <arg2>",
			ErrorSeverity.ERROR),
	CODE_GEN_TEMPLATES_INCOMPLETE(33, "missing code generation template <arg>", ErrorSeverity.ERROR),

	/* Grammar errors */

	SYNTAX_ERROR(50, "syntax error: <arg>", ErrorSeverity.ERROR),
	REDEFINITION(51, "<arg> redefinition; previous at line <arg2>", ErrorSeverity.ERROR),

	UNKNOWN_REF(56, "reference undefined: <arg>", ErrorSeverity.ERROR),
	UNKNOWN_PARSER_RULE_REF(57, "parser rule reference undefined: <arg>", ErrorSeverity.ERROR),
	UNKNOWN_LEXER_RULE_REF(58, "lexer rule reference undefined: <arg>", ErrorSeverity.ERROR),

	UNKNOWN_ATTRIBUTE(65, "unknown attribute <arg> for rule <arg2> in <arg3>", ErrorSeverity.ERROR),

	ILLEGAL_OPTION_VALUE(84, "unsupported option value <arg>=<arg2>", ErrorSeverity.WARNING),
	NO_RULES(99, "<if(arg2.implicitLexerOwner)>implicitly generated <endif>grammar <arg> has no rules",
			ErrorSeverity.ERROR),

	IMPORTED_GRAMMAR_MISSING(110, "can't find or load grammar <arg>", ErrorSeverity.ERROR),
	INVALID_IMPORT(111, "<arg.typeString> grammar <arg.name> cannot import <arg2.typeString> grammar <arg2.name>",
			ErrorSeverity.ERROR),
	UNTERMINATED_STRING_LITERAL(152, "unterminated string literal", ErrorSeverity.ERROR),
	EMPTY_STRINGS_NOT_ALLOWED(174, "string literals cannot be empty", ErrorSeverity.ERROR),

	///////////////////////////////////////////////////////

	;

	/**
	 * The error or warning message, in StringTemplate 4 format using {@code <} and {@code >} as the
	 * delimiters. Arguments for the message may be referenced using the following names:
	 * <ul>
	 * <li>{@code arg}: The first template argument</li>
	 * <li>{@code arg2}: The second template argument</li>
	 * <li>{@code arg3}: The third template argument</li>
	 * <li>{@code verbose}: {@code true} if verbose messages were requested; otherwise,
	 * {@code false}</li>
	 * <li>{@code exception}: The exception which resulted in the error, if any.</li>
	 * <li>{@code stackTrace}: The stack trace for the exception, when available.</li>
	 * </ul>
	 */
	public final String msg;
	/**
	 * The error or warning number.
	 * <p>
	 * The code should be unique, and following its use in a release should not be altered or
	 * reassigned.
	 * </p>
	 */
	public final int code;
	/**
	 * The error severity.
	 */
	public final ErrorSeverity severity;

	/**
	 * Constructs a new {@link ErrorType} with the specified code, message, and severity.
	 *
	 * @param code The unique error number.
	 * @param msg The error message template.
	 * @param severity The error severity.
	 */
	ErrorType(int code, String msg, ErrorSeverity severity) {
		this.code = code;
		this.msg = msg;
		this.severity = severity;
	}
}
