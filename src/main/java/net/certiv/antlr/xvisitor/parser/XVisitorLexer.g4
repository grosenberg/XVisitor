/*******************************************************************************
 * Copyright (c) 2010-2015 Gerald Rosenberg & others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the standard 3-clause BSD License.  A copy of the License
 * is provided with this distribution in the License.txt file.
 *******************************************************************************/
/* ANTLR v4 XVisitor grammar */

lexer grammar XVisitorLexer;

@header {
	package net.certiv.antlr.xvisitor.parser.gen;
	import net.certiv.antlr.xvisitor.parser.LexerAdaptor;
}

options {
	superClass = LexerAdaptor ;
}

tokens {
	INT,
	RBRACE,
	TEXT
}



// ---------------------------------------------------------------------------------
// default mode

// --------
// Comments
//
// All comments go on the hidden channel - no reason to handle JavaDoc
// comments in the parser. Consecutive line comments are gathered into a
// single token to simplify automated formatting.  Horizontal and vertical
// whitespace is tokenized separately to also simplify automated formatting.

DOC_COMMENT
	:	DocComment -> channel(HIDDEN)
	;

BLOCK_COMMENT
	:	BlockComment -> channel(HIDDEN)
	;

LINE_COMMENT
	:	LineComment -> channel(HIDDEN)
	;

// -------
// From the default mode, options and action blocks are handled in separate modes.
// The RBRACE should never be encountered in the default mode.
//

OPTIONS
	: 'options'	-> pushMode(Options)
	;

LBRACE
	: LBrace	-> pushMode(ActionBlock)
	;


// Keywords
// --------

GRAMMAR		: 'grammar'		;
XVISITOR	: 'xvisitor'	;

// -----------
// Punctuation
//
COLON		: Colon		;
COMMA		: Comma		;
SEMI		: Semi		;
ASSIGN		: Equal	;
QUESTION	: Question	;
STAR		: Star		;
AT			: At		;
ANY			: Any		;
SEP			: Sep		;
DOT			: Dot		;
OR			: Or		;
NOT			: Not		;

/** Allow unicode rule/token names */
ID		: Id		;
LITERAL	: Literal	;

// ----------
// Whitespace

HORZ_WS
	:	Hws+ -> channel(HIDDEN)
	;

VERT_WS
	:	Vws+ -> channel(HIDDEN)
	;

// -----------------
// Unknown/Illegal Character

ERRCHAR
	:	.	-> skip
	;


// ---------------------------------------------------------------------------------
mode Options;

	OPT_DOC_COMMENT		:	DocComment 		-> type(BLOCK_COMMENT), channel(HIDDEN)		;
	OPT_BLOCK_COMMENT	:	BlockComment 	-> type(BLOCK_COMMENT), channel(HIDDEN)		;
	OPT_LINE_COMMENT	:	LineComment 	-> type(LINE_COMMENT), channel(HIDDEN)		;

	OPT_LBRACE			: LBrace			-> type(LBRACE)				;
	OPT_RBRACE			: RBrace			-> type(RBRACE), popMode	;

	OPT_ID				: Id				-> type(ID)					;
	OPT_DOT				: Dot				-> type(DOT)				;
	OPT_ASSIGN			: Equal				-> type(ASSIGN)				;
	OPT_SEMI			: Semi				-> type(SEMI)				;
	OPT_STAR			: Star				-> type(STAR)				;
	OPT_INT				: Int				-> type(INT)				;

	OPT_LITERAL			: Literal			-> type(LITERAL)			;

	OPT_HORZ_WS			:	Hws+ 			-> type(HORZ_WS), channel(HIDDEN)		;
	OPT_VERT_WS			:	Vws+ 			-> type(VERT_WS), channel(HIDDEN)		;


// ---------------------------------------------------------------------------------
mode ActionBlock;

	ABLOCK_LBRACE	: LBrace	-> type(TEXT), pushMode(ActionBlock)		;
	ABLOCK_RBRACE	: RBrace	{ handleRightTerminator(TEXT, RBRACE); }	;

	ABLOCK_DOC_COMMENT		: DocComment 		-> type(TEXT)	;
	ABLOCK_BLOCK_COMMENT	: BlockComment		-> type(TEXT)	;
	ABLOCK_LINE_COMMENT		: LineComment		-> type(TEXT)	;

	ONENTRY			: ( Hws | Vws )* 'onEntry:' ;
	ONEXIT			: ( Hws | Vws )* 'onExit:'  ;

	ABLOCK_STRING	: DblQuoteLiteral 	->	type(TEXT)		;
	ABLOCK_CHAR		: SglQuoteLiteral	->	type(TEXT)		;

	REFERENCE		: Dollar NameChar+ ( Dot NameChar+ )?	;

	ABLOCK_TEXT		: NameChar+			->	type(TEXT)	;
	ABLOCK_OTHER	: .					->	type(TEXT)	;


// ---------------------------------------------------------------------------------
// General fragments

fragment Colon		: ':'	;
fragment Comma		: ','	;
fragment Semi		: ';'	;
fragment Equal		: '='	;
fragment Question	: '?'	;
fragment Star		: '*'	;
fragment At			: '@'	;
fragment Any		: '//'	;
fragment Sep		: '/'	;
fragment Bang		: '!'	;
fragment Dot		: '.'	;
fragment LBrace		: '{'	;
fragment RBrace		: '}'	;
fragment Or			: '|'	;
fragment Not		: '!'	;
fragment Dollar		: '$'	;

fragment Hws			:  [ \t\r]	;
fragment Vws			:  [\n\f]	;

fragment DocComment		: '/**' .*? '*/' ;
fragment BlockComment	: '/#' .*? '#/' ;
fragment LineComment	: '#' ~'\n'* ( '\n' Hws* '#' ~'\n'* )*	;

fragment Id	: NameStartChar NameChar* ;

fragment
NameStartChar
	:   'A'..'Z' | 'a'..'z'
	|   '\u00C0'..'\u00D6'
	|   '\u00D8'..'\u00F6'
	|   '\u00F8'..'\u02FF'
	|   '\u0370'..'\u037D'
	|   '\u037F'..'\u1FFF'
	|   '\u200C'..'\u200D'
	|   '\u2070'..'\u218F'
	|   '\u2C00'..'\u2FEF'
	|   '\u3001'..'\uD7FF'
	|   '\uF900'..'\uFDCF'
	|   '\uFDF0'..'\uFFFD'
	;

fragment
NameChar
	:   NameStartChar
	|   '0'..'9'
	|   '_'
	|   '\u00B7'
	|   '\u0300'..'\u036F'
	|   '\u203F'..'\u2040'
	;



fragment Literal			: DblQuoteLiteral | SglQuoteLiteral ;

fragment SglQuoteLiteral	:	'\'' ( EscSeq | ~['\\] )* '\''	;
fragment DblQuoteLiteral	:	'"'  ( EscSeq | ~["\\] )* '"'	;

fragment Int		: [0-9]+		;
fragment HexDigit	: [0-9a-fA-F]	;

fragment EscSeq		:	'\\' ( EscUnicode | . )		;
fragment EscUnicode :   'u' (HexDigit (HexDigit (HexDigit HexDigit?)?)?)? 	;

