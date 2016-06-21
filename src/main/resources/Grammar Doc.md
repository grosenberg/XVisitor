#### Grammar Lexicon

Essentially the same as [Antlr: Grammar Lexicon](https://theantlrguy.atlassian.net/wiki/display/ANTLR4/Grammar+Lexicon).

#### Grammar Structure {#structure}

```
/* Header comment (optional) */
xvisitor grammar name;					&#x25c0; 'xvisitor' qualifier required

options {
	parserClass = ParserGrammarName;	&#x25c0; required - grammar name of the parser used
										&#x25c0; to create the parse-tree
	superClass = ASuperClassName;		&#x25c0; optional - Antlr standard definition
}

@header  { .... }			&#x25c0; optional - uses Antlr standard definition

# line comments start with the '#' character
mainRuleName
	: XPathRule1			&#x25c0; for XPathRules that terminate on the same
	| XPathRule2			&#x25c0; parse-tree node, the order of listing in
	| XPathRule3			&#x25c0; the main rule determines the execution order
	....					&#x25c0; of the associated actions
	| XPathRuleN
	{ mainRuleAction(); }	&#x25c0; optional - action runs on visitor termination
	;


XPathRule1	: XPathSpec1					&#x25c0; a path specification 
				{ action1(); }				&#x25c0; unlabeled action; executes on entry
			;

XPathRule2	: XPathSpec2					&#x25c0; labeled actions for XPathSpecs that 
				{ onEntry: action2Beg(); }	&#x25c0; terminate on rule context nodes; either
				{ onExit:  action2End(); }	&#x25c0; or both may be specified
			;
....
XPathRuleN	: XPathSpecN
				{ actionN(); }
			;

```
