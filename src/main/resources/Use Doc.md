#### Quick Start

Given:

	* LangParser.g4		// grammar used to generate a parse tree
	* LangLexer.g4
	* input.txt			// source text to parse
	* LangVisitor.g4	// XVisitor grammar 

Compile:

	java org.antlr.v4.Tool -o .\gen -no-listener LangLexer.g4 LangParser.g4

	java net.certiv.antlr.xvisitor.Tool -o .\gen -lib .\gen LangVisitor.g4

		Note: the xvisitor generator tool requires access 
		to the Antlr generated parser.  Use the '-lib' 
		command line option to specifiy the directory 
		containing the Antlr generated parser.


Invoke:

	ANTLRFileStream input = new ANTLRFileStream(input.txt);
	LangLexer lexer = new LexerLexer(input);
	CommonTokenStream tokens = new CommonTokenStream(lexer);
	LangParser parser = new LangParser(tokens);
	LangContext parseTree = parser.lang();

	LangVisitor visitor = new LangVisitor(parseTree);
	visitor.findAll();

#### Runtime

The XVisitor runtime provides two entry points for invoking the visitor.

	/**
	 * Invokes the visitor for all XPath rules listed
	 * in the main rule of the visitor grammar.
	 */
	public void findAll() { .... }

	
	/**
	 * Invokes the visitor for an active subset of
	 * the XPath rules listed in the main rule of the
	 * visitor grammar. The argument names of XPath
	 * rules define the active subset. The remaining
	 * rules are inactive and are not considered in
	 * the operation of the visitor.
	 * 
	 * @param names Names of the XPath rules to set as active
	 */
	public void find(String... names) { .... }


