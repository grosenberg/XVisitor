#### Grammar Lexicon

Essentially the same as [Antlr: Grammar Lexicon](https://theantlrguy.atlassian.net/wiki/display/ANTLR4/Grammar+Lexicon).

#### Grammar Structure

	/** Optional javadoc style header */

	xvisitor grammar name ;					// 'xvisitor' required

	options {
		parserClass = ParserGrammarName ; 	// required - grammar name of the parser used
											// to create the parse-tree
		superClass = ASuperClassName    ;	// optional- Antlr standard definition
	}

	@header  { .... }						// optional - Antlr standard definition

	mainRuleName
		: XPathRule1				// for XPathRules that terminate on the same
    	| XPathRule2				// parse-tree node, the order of listing in
    	| XPathRule3				// the main rule determines the execution order
    	....						// of the associated actions
    	| XPathRuleN
    	{ mainRuleAction(); } 		// optional - action to run on visitor termination
    	;


	XPathRule1	: XPathSpec1					// unlabeled action for XPathSpecs that 
					{ Action1(); }				// terminate on Terminal Nodes
				;

	XPathRule2	: XPathSpec2					// labeled actions for XPathSpecs that 
					{ onEntry: Action2In(); }	// terminate on Context Nodes; either
					{ onExit : Action2Out(); }	// or both may be specified
				;
	....
	XPathRuleN	: XPathSpecN
					{ ActionN(); }
				;

#### XPath Specifications

Essentially the same as [Antlr standard XPaths](https://theantlrguy.atlassian.net/wiki/display/ANTLR4/Parse+Tree+Matching+and+XPath#ParseTreeMatchingandXPath-UsingXPathtoidentifyparsetreenodesets).

The primary significant difference is that every XPath specification **must** start with either '/' or '//'.  

#### Actions and Attributes

Essentially the same as Antlr standard parser rule [Actions and Attributes](https://theantlrguy.atlassian.net/wiki/display/ANTLR4/Actions+and+Attributes).
