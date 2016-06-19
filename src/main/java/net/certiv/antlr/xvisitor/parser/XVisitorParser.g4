/*******************************************************************************
 * Copyright (c) 2010-2015 Gerald Rosenberg & others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the standard 3-clause BSD License.  A copy of the License
 * is provided with this distribution in the License.txt file.
 *******************************************************************************/
/* ANTLR v4 XVisitor grammar */

parser grammar XVisitorParser;

options {
	superClass = ParserAdaptor	;
	tokenVocab = XVisitorLexer	;
}

@header {
	package net.certiv.antlr.xvisitor.parser.gen;

	import net.certiv.antlr.xvisitor.parser.ParserAdaptor;
}

grammarSpec
    :   XVISITOR GRAMMAR ID SEMI 
    	optionsSpec?
    	action*
    	xmain
    	xpath+
    	EOF
    ;

optionsSpec
	:	OPTIONS OPT_LBRACE option+ OPT_RBRACE
	;

option
    :   OPT_ID 
    	( OPT_ASSIGN optionValue
    	| ( OPT_DOT OPT_ID )+
    	) OPT_SEMI
 	;
 	
optionValue
    :   OPT_ID
    |   OPT_LITERAL
    |   OPT_INT
    |	OPT_STAR
    ;

action
	:	AT ID actionBlock 
	;

xmain
	:	name=ID COLON ID ( OR ID )* actionBlock? SEMI
	;

xpath
	:	name=ID COLON xpathSpec actionBlock* SEMI
	; 

xpathSpec
	:	( separator word )+
	;

actionBlock
	:	LBRACE ( ONENTRY | ONEXIT )? ( text | reference )+ RBRACE
	;

text 
	: TEXT+
	;

reference 
	: REFERENCE
	;

separator
	:	( ANY | SEP ) NOT?
	;

word
	:	ID
	|	STAR
	|	LITERAL
	;

