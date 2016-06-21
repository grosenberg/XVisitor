### XVisitor

A tool for generating customized Antlr4 parse-trees visitors using grammar-style definitions.

[Documentation](http://www.certiv.net/projects/xvisitor.html).

Antlr-standard BSD License.  

#### Summary

XVisitor enables multiple XPath-styled probes to be evaluated in parallel against the parse-tree.  When any XPath is matched, actions defined in the grammar specific to that XPath are invoked.  Actions for either or both 'onEntry' and 'onExit' states can be defined.  Consistent with standard Antlr grammars, actions are implemented using target language code.  Token and rule '$'-styled references give actions direct access to the currently matched context and token attributes.

#### Features

* Natural order of results:
	* the XPath match actions are invoked in natural (parse-tree) order by the visitor
* Clarity:
	* 'onEntry' and 'onExit' actions can be separately defined for each XPath
	* the grammar definition is simple and conceptually similar to standard Antlr grammars
	* symbolic context references are available for use in actions
	* the actual path traversed to reach a matched XPath is recorded and available in actions
* Flexibilty:
	* concurrent evaluation fully supports overlapping XPaths
	* supports XPath wildcards: '//', '*', and '?'
* Efficiency:
	* complex path state analysis can be reduced to a set of simply-defined path matches
	* parse-tree branches that cannot be matched are skipped [TBI]
	

#### Example Visitor Grammar 

The following grammar will generate an outline listing of select nodes in a parse-tree generated using the [ANTLRv4](https://github.com/antlr/grammars-v4/tree/master/antlr4) grammar.

``` 
xvisitor grammar Outline;

options {
	parserClass = AntlrDT4Parser ;
	# superClass = OutlineAdaptor ;
}

@header {
	package net.certiv.antlrdt4.core.parser.gen;
	# import net.certiv.antlrdt4.core.parser.OutlineAdaptor;
}

outline
	: grammarSpec
	| optionsBlock
	| optionStatement
	| tokensBlock
	| tokenStatement
	| atAction
	| parserRule
	| lexerRule
	;

grammarSpec		: /grammarSpec
					{ System.out.println($grammarType.text + " " + $identifier.text); }
				;

optionsBlock	: //prequelConstruct/optionsSpec
					{ onEntry: System.out.println("Options: "); } 	
					{ onExit:  System.out.println("End Options."); }	
				;

optionStatement	: //prequelConstruct/optionsSpec/option
					{ 
						System.out.print($identifier.text);
						System.out.print(" = ");
						System.out.println($optionValue.text);
					}	
				;

tokensBlock		: //tokensSpec 
					{ onEntry: System.out.println("Tokens: "); } 	
					{ onExit:  System.out.println("End Tokens."); }	
				;

tokenStatement	: //tokensSpec//id
					{ System.out.println($id.text); }
				;

atAction		: /grammarSpec/prequelConstruct/action
					{ 
						System.out.print("@");
						if ($COLONCOLON != null) {
							System.out.print($actionScopeName.text + "::");
						}
						System.out.print($identifier.text);
						System.out.print(" = ");
						System.out.println($optionValue.text);
					}
				;

parserRule		: //ruleSpec/parserRuleSpec
					{
						System.out.print("Parser rule: "); 
						System.out.println($RULE_REF.text);
					}
				;

lexerRule		: //lexerRuleSpec
					{
						System.out.print("Lexer rule: "); 
						System.out.println($TOKEN_REF.text);
					}
				;
```
