// Generated from D:/DevFiles/Eclipse/Tools/XVisitor/net.certiv.xvisitor/src/main/java/net/certiv/antlr/xvisitor/parser/XVisitorLexer.g4 by ANTLR 4.8

	package net.certiv.antlr.xvisitor.parser.gen;
	import net.certiv.antlr.xvisitor.parser.LexerAdaptor;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class XVisitorLexer extends LexerAdaptor {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		INT=1, RBRACE=2, TEXT=3, DOC_COMMENT=4, BLOCK_COMMENT=5, LINE_COMMENT=6, 
		OPTIONS=7, LBRACE=8, GRAMMAR=9, XVISITOR=10, COLON=11, COMMA=12, SEMI=13, 
		ASSIGN=14, QUESTION=15, STAR=16, AT=17, ANY=18, SEP=19, DOT=20, OR=21, 
		NOT=22, ID=23, LITERAL=24, HORZ_WS=25, VERT_WS=26, ERRCHAR=27, ABLOCK_RBRACE=28, 
		ONENTRY=29, ONEXIT=30, REFERENCE=31;
	public static final int
		Options=1, ActionBlock=2;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE", "Options", "ActionBlock"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"DOC_COMMENT", "BLOCK_COMMENT", "LINE_COMMENT", "OPTIONS", "LBRACE", 
			"GRAMMAR", "XVISITOR", "COLON", "COMMA", "SEMI", "ASSIGN", "QUESTION", 
			"STAR", "AT", "ANY", "SEP", "DOT", "OR", "NOT", "ID", "LITERAL", "HORZ_WS", 
			"VERT_WS", "ERRCHAR", "OPT_DOC_COMMENT", "OPT_BLOCK_COMMENT", "OPT_LINE_COMMENT", 
			"OPT_LBRACE", "OPT_RBRACE", "OPT_ID", "OPT_DOT", "OPT_ASSIGN", "OPT_SEMI", 
			"OPT_STAR", "OPT_INT", "OPT_LITERAL", "OPT_HORZ_WS", "OPT_VERT_WS", "ABLOCK_LBRACE", 
			"ABLOCK_RBRACE", "ABLOCK_DOC_COMMENT", "ABLOCK_BLOCK_COMMENT", "ABLOCK_LINE_COMMENT", 
			"ONENTRY", "ONEXIT", "ABLOCK_STRING", "ABLOCK_CHAR", "REFERENCE", "ABLOCK_TEXT", 
			"ABLOCK_OTHER", "Colon", "Comma", "Semi", "Equal", "Question", "Star", 
			"At", "Any", "Sep", "Bang", "Dot", "LBrace", "RBrace", "Or", "Not", "Dollar", 
			"Hws", "Vws", "DocComment", "BlockComment", "LineComment", "Id", "NameStartChar", 
			"NameChar", "Literal", "SglQuoteLiteral", "DblQuoteLiteral", "Int", "HexDigit", 
			"EscSeq", "EscUnicode"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "INT", "RBRACE", "TEXT", "DOC_COMMENT", "BLOCK_COMMENT", "LINE_COMMENT", 
			"OPTIONS", "LBRACE", "GRAMMAR", "XVISITOR", "COLON", "COMMA", "SEMI", 
			"ASSIGN", "QUESTION", "STAR", "AT", "ANY", "SEP", "DOT", "OR", "NOT", 
			"ID", "LITERAL", "HORZ_WS", "VERT_WS", "ERRCHAR", "ABLOCK_RBRACE", "ONENTRY", 
			"ONEXIT", "REFERENCE"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public XVisitorLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "XVisitorLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 39:
			ABLOCK_RBRACE_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void ABLOCK_RBRACE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			 handleRightTerminator(TEXT, RBRACE); 
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2!\u022e\b\1\b\1\b"+
		"\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n"+
		"\t\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21"+
		"\4\22\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30"+
		"\4\31\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37"+
		"\4 \t \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t"+
		"*\4+\t+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63"+
		"\4\64\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t"+
		"<\4=\t=\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4"+
		"H\tH\4I\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\3\2\3"+
		"\2\3\2\3\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3"+
		"\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3"+
		"\25\3\25\3\26\3\26\3\27\6\27\u00f0\n\27\r\27\16\27\u00f1\3\27\3\27\3\30"+
		"\6\30\u00f7\n\30\r\30\16\30\u00f8\3\30\3\30\3\31\3\31\3\31\3\31\3\32\3"+
		"\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3"+
		"\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3 \3 "+
		"\3 \3 \3!\3!\3!\3!\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3$\3$\3$\3$\3%\3%\3%\3"+
		"%\3&\6&\u0136\n&\r&\16&\u0137\3&\3&\3&\3\'\6\'\u013e\n\'\r\'\16\'\u013f"+
		"\3\'\3\'\3\'\3(\3(\3(\3(\3(\3)\3)\3)\3*\3*\3*\3*\3+\3+\3+\3+\3,\3,\3,"+
		"\3,\3-\3-\7-\u015b\n-\f-\16-\u015e\13-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3.\3"+
		".\7.\u016b\n.\f.\16.\u016e\13.\3.\3.\3.\3.\3.\3.\3.\3.\3/\3/\3/\3/\3\60"+
		"\3\60\3\60\3\60\3\61\3\61\6\61\u0182\n\61\r\61\16\61\u0183\3\61\3\61\6"+
		"\61\u0188\n\61\r\61\16\61\u0189\5\61\u018c\n\61\3\62\6\62\u018f\n\62\r"+
		"\62\16\62\u0190\3\62\3\62\3\63\3\63\3\63\3\63\3\64\3\64\3\65\3\65\3\66"+
		"\3\66\3\67\3\67\38\38\39\39\3:\3:\3;\3;\3;\3<\3<\3=\3=\3>\3>\3?\3?\3@"+
		"\3@\3A\3A\3B\3B\3C\3C\3D\3D\3E\3E\3F\3F\3F\3F\3F\7F\u01c3\nF\fF\16F\u01c6"+
		"\13F\3F\3F\3F\3G\3G\3G\3G\7G\u01cf\nG\fG\16G\u01d2\13G\3G\3G\3G\3H\3H"+
		"\7H\u01d9\nH\fH\16H\u01dc\13H\3H\3H\7H\u01e0\nH\fH\16H\u01e3\13H\3H\3"+
		"H\7H\u01e7\nH\fH\16H\u01ea\13H\7H\u01ec\nH\fH\16H\u01ef\13H\3I\3I\7I\u01f3"+
		"\nI\fI\16I\u01f6\13I\3J\3J\3K\3K\5K\u01fc\nK\3L\3L\5L\u0200\nL\3M\3M\3"+
		"M\7M\u0205\nM\fM\16M\u0208\13M\3M\3M\3N\3N\3N\7N\u020f\nN\fN\16N\u0212"+
		"\13N\3N\3N\3O\6O\u0217\nO\rO\16O\u0218\3P\3P\3Q\3Q\3Q\5Q\u0220\nQ\3R\3"+
		"R\3R\3R\3R\5R\u0227\nR\5R\u0229\nR\5R\u022b\nR\5R\u022d\nR\4\u01c4\u01d0"+
		"\2S\5\6\7\7\t\b\13\t\r\n\17\13\21\f\23\r\25\16\27\17\31\20\33\21\35\22"+
		"\37\23!\24#\25%\26\'\27)\30+\31-\32/\33\61\34\63\35\65\2\67\29\2;\2=\2"+
		"?\2A\2C\2E\2G\2I\2K\2M\2O\2Q\2S\36U\2W\2Y\2[\37] _\2a\2c!e\2g\2i\2k\2"+
		"m\2o\2q\2s\2u\2w\2y\2{\2}\2\177\2\u0081\2\u0083\2\u0085\2\u0087\2\u0089"+
		"\2\u008b\2\u008d\2\u008f\2\u0091\2\u0093\2\u0095\2\u0097\2\u0099\2\u009b"+
		"\2\u009d\2\u009f\2\u00a1\2\u00a3\2\u00a5\2\5\2\3\4\13\5\2\13\13\17\17"+
		"\"\"\4\2\f\f\16\16\3\2\f\f\17\2C\\c|\u00c2\u00d8\u00da\u00f8\u00fa\u0301"+
		"\u0372\u037f\u0381\u2001\u200e\u200f\u2072\u2191\u2c02\u2ff1\u3003\ud801"+
		"\uf902\ufdd1\ufdf2\uffff\7\2\62;aa\u00b9\u00b9\u0302\u0371\u2041\u2042"+
		"\4\2))^^\4\2$$^^\3\2\62;\5\2\62;CHch\2\u022b\2\5\3\2\2\2\2\7\3\2\2\2\2"+
		"\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2"+
		"\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2"+
		"\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2"+
		"\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\3\65\3\2\2"+
		"\2\3\67\3\2\2\2\39\3\2\2\2\3;\3\2\2\2\3=\3\2\2\2\3?\3\2\2\2\3A\3\2\2\2"+
		"\3C\3\2\2\2\3E\3\2\2\2\3G\3\2\2\2\3I\3\2\2\2\3K\3\2\2\2\3M\3\2\2\2\3O"+
		"\3\2\2\2\4Q\3\2\2\2\4S\3\2\2\2\4U\3\2\2\2\4W\3\2\2\2\4Y\3\2\2\2\4[\3\2"+
		"\2\2\4]\3\2\2\2\4_\3\2\2\2\4a\3\2\2\2\4c\3\2\2\2\4e\3\2\2\2\4g\3\2\2\2"+
		"\5\u00a7\3\2\2\2\7\u00ab\3\2\2\2\t\u00af\3\2\2\2\13\u00b3\3\2\2\2\r\u00bd"+
		"\3\2\2\2\17\u00c1\3\2\2\2\21\u00c9\3\2\2\2\23\u00d2\3\2\2\2\25\u00d4\3"+
		"\2\2\2\27\u00d6\3\2\2\2\31\u00d8\3\2\2\2\33\u00da\3\2\2\2\35\u00dc\3\2"+
		"\2\2\37\u00de\3\2\2\2!\u00e0\3\2\2\2#\u00e2\3\2\2\2%\u00e4\3\2\2\2\'\u00e6"+
		"\3\2\2\2)\u00e8\3\2\2\2+\u00ea\3\2\2\2-\u00ec\3\2\2\2/\u00ef\3\2\2\2\61"+
		"\u00f6\3\2\2\2\63\u00fc\3\2\2\2\65\u0100\3\2\2\2\67\u0105\3\2\2\29\u010a"+
		"\3\2\2\2;\u010f\3\2\2\2=\u0113\3\2\2\2?\u0118\3\2\2\2A\u011c\3\2\2\2C"+
		"\u0120\3\2\2\2E\u0124\3\2\2\2G\u0128\3\2\2\2I\u012c\3\2\2\2K\u0130\3\2"+
		"\2\2M\u0135\3\2\2\2O\u013d\3\2\2\2Q\u0144\3\2\2\2S\u0149\3\2\2\2U\u014c"+
		"\3\2\2\2W\u0150\3\2\2\2Y\u0154\3\2\2\2[\u015c\3\2\2\2]\u016c\3\2\2\2_"+
		"\u0177\3\2\2\2a\u017b\3\2\2\2c\u017f\3\2\2\2e\u018e\3\2\2\2g\u0194\3\2"+
		"\2\2i\u0198\3\2\2\2k\u019a\3\2\2\2m\u019c\3\2\2\2o\u019e\3\2\2\2q\u01a0"+
		"\3\2\2\2s\u01a2\3\2\2\2u\u01a4\3\2\2\2w\u01a6\3\2\2\2y\u01a9\3\2\2\2{"+
		"\u01ab\3\2\2\2}\u01ad\3\2\2\2\177\u01af\3\2\2\2\u0081\u01b1\3\2\2\2\u0083"+
		"\u01b3\3\2\2\2\u0085\u01b5\3\2\2\2\u0087\u01b7\3\2\2\2\u0089\u01b9\3\2"+
		"\2\2\u008b\u01bb\3\2\2\2\u008d\u01bd\3\2\2\2\u008f\u01ca\3\2\2\2\u0091"+
		"\u01d6\3\2\2\2\u0093\u01f0\3\2\2\2\u0095\u01f7\3\2\2\2\u0097\u01fb\3\2"+
		"\2\2\u0099\u01ff\3\2\2\2\u009b\u0201\3\2\2\2\u009d\u020b\3\2\2\2\u009f"+
		"\u0216\3\2\2\2\u00a1\u021a\3\2\2\2\u00a3\u021c\3\2\2\2\u00a5\u0221\3\2"+
		"\2\2\u00a7\u00a8\5\u008dF\2\u00a8\u00a9\3\2\2\2\u00a9\u00aa\b\2\2\2\u00aa"+
		"\6\3\2\2\2\u00ab\u00ac\5\u008fG\2\u00ac\u00ad\3\2\2\2\u00ad\u00ae\b\3"+
		"\2\2\u00ae\b\3\2\2\2\u00af\u00b0\5\u0091H\2\u00b0\u00b1\3\2\2\2\u00b1"+
		"\u00b2\b\4\2\2\u00b2\n\3\2\2\2\u00b3\u00b4\7q\2\2\u00b4\u00b5\7r\2\2\u00b5"+
		"\u00b6\7v\2\2\u00b6\u00b7\7k\2\2\u00b7\u00b8\7q\2\2\u00b8\u00b9\7p\2\2"+
		"\u00b9\u00ba\7u\2\2\u00ba\u00bb\3\2\2\2\u00bb\u00bc\b\5\3\2\u00bc\f\3"+
		"\2\2\2\u00bd\u00be\5\177?\2\u00be\u00bf\3\2\2\2\u00bf\u00c0\b\6\4\2\u00c0"+
		"\16\3\2\2\2\u00c1\u00c2\7i\2\2\u00c2\u00c3\7t\2\2\u00c3\u00c4\7c\2\2\u00c4"+
		"\u00c5\7o\2\2\u00c5\u00c6\7o\2\2\u00c6\u00c7\7c\2\2\u00c7\u00c8\7t\2\2"+
		"\u00c8\20\3\2\2\2\u00c9\u00ca\7z\2\2\u00ca\u00cb\7x\2\2\u00cb\u00cc\7"+
		"k\2\2\u00cc\u00cd\7u\2\2\u00cd\u00ce\7k\2\2\u00ce\u00cf\7v\2\2\u00cf\u00d0"+
		"\7q\2\2\u00d0\u00d1\7t\2\2\u00d1\22\3\2\2\2\u00d2\u00d3\5i\64\2\u00d3"+
		"\24\3\2\2\2\u00d4\u00d5\5k\65\2\u00d5\26\3\2\2\2\u00d6\u00d7\5m\66\2\u00d7"+
		"\30\3\2\2\2\u00d8\u00d9\5o\67\2\u00d9\32\3\2\2\2\u00da\u00db\5q8\2\u00db"+
		"\34\3\2\2\2\u00dc\u00dd\5s9\2\u00dd\36\3\2\2\2\u00de\u00df\5u:\2\u00df"+
		" \3\2\2\2\u00e0\u00e1\5w;\2\u00e1\"\3\2\2\2\u00e2\u00e3\5y<\2\u00e3$\3"+
		"\2\2\2\u00e4\u00e5\5}>\2\u00e5&\3\2\2\2\u00e6\u00e7\5\u0083A\2\u00e7("+
		"\3\2\2\2\u00e8\u00e9\5\u0085B\2\u00e9*\3\2\2\2\u00ea\u00eb\5\u0093I\2"+
		"\u00eb,\3\2\2\2\u00ec\u00ed\5\u0099L\2\u00ed.\3\2\2\2\u00ee\u00f0\5\u0089"+
		"D\2\u00ef\u00ee\3\2\2\2\u00f0\u00f1\3\2\2\2\u00f1\u00ef\3\2\2\2\u00f1"+
		"\u00f2\3\2\2\2\u00f2\u00f3\3\2\2\2\u00f3\u00f4\b\27\2\2\u00f4\60\3\2\2"+
		"\2\u00f5\u00f7\5\u008bE\2\u00f6\u00f5\3\2\2\2\u00f7\u00f8\3\2\2\2\u00f8"+
		"\u00f6\3\2\2\2\u00f8\u00f9\3\2\2\2\u00f9\u00fa\3\2\2\2\u00fa\u00fb\b\30"+
		"\2\2\u00fb\62\3\2\2\2\u00fc\u00fd\13\2\2\2\u00fd\u00fe\3\2\2\2\u00fe\u00ff"+
		"\b\31\5\2\u00ff\64\3\2\2\2\u0100\u0101\5\u008dF\2\u0101\u0102\3\2\2\2"+
		"\u0102\u0103\b\32\6\2\u0103\u0104\b\32\2\2\u0104\66\3\2\2\2\u0105\u0106"+
		"\5\u008fG\2\u0106\u0107\3\2\2\2\u0107\u0108\b\33\6\2\u0108\u0109\b\33"+
		"\2\2\u01098\3\2\2\2\u010a\u010b\5\u0091H\2\u010b\u010c\3\2\2\2\u010c\u010d"+
		"\b\34\7\2\u010d\u010e\b\34\2\2\u010e:\3\2\2\2\u010f\u0110\5\177?\2\u0110"+
		"\u0111\3\2\2\2\u0111\u0112\b\35\b\2\u0112<\3\2\2\2\u0113\u0114\5\u0081"+
		"@\2\u0114\u0115\3\2\2\2\u0115\u0116\b\36\t\2\u0116\u0117\b\36\n\2\u0117"+
		">\3\2\2\2\u0118\u0119\5\u0093I\2\u0119\u011a\3\2\2\2\u011a\u011b\b\37"+
		"\13\2\u011b@\3\2\2\2\u011c\u011d\5}>\2\u011d\u011e\3\2\2\2\u011e\u011f"+
		"\b \f\2\u011fB\3\2\2\2\u0120\u0121\5o\67\2\u0121\u0122\3\2\2\2\u0122\u0123"+
		"\b!\r\2\u0123D\3\2\2\2\u0124\u0125\5m\66\2\u0125\u0126\3\2\2\2\u0126\u0127"+
		"\b\"\16\2\u0127F\3\2\2\2\u0128\u0129\5s9\2\u0129\u012a\3\2\2\2\u012a\u012b"+
		"\b#\17\2\u012bH\3\2\2\2\u012c\u012d\5\u009fO\2\u012d\u012e\3\2\2\2\u012e"+
		"\u012f\b$\20\2\u012fJ\3\2\2\2\u0130\u0131\5\u0099L\2\u0131\u0132\3\2\2"+
		"\2\u0132\u0133\b%\21\2\u0133L\3\2\2\2\u0134\u0136\5\u0089D\2\u0135\u0134"+
		"\3\2\2\2\u0136\u0137\3\2\2\2\u0137\u0135\3\2\2\2\u0137\u0138\3\2\2\2\u0138"+
		"\u0139\3\2\2\2\u0139\u013a\b&\22\2\u013a\u013b\b&\2\2\u013bN\3\2\2\2\u013c"+
		"\u013e\5\u008bE\2\u013d\u013c\3\2\2\2\u013e\u013f\3\2\2\2\u013f\u013d"+
		"\3\2\2\2\u013f\u0140\3\2\2\2\u0140\u0141\3\2\2\2\u0141\u0142\b\'\23\2"+
		"\u0142\u0143\b\'\2\2\u0143P\3\2\2\2\u0144\u0145\5\177?\2\u0145\u0146\3"+
		"\2\2\2\u0146\u0147\b(\24\2\u0147\u0148\b(\4\2\u0148R\3\2\2\2\u0149\u014a"+
		"\5\u0081@\2\u014a\u014b\b)\25\2\u014bT\3\2\2\2\u014c\u014d\5\u008dF\2"+
		"\u014d\u014e\3\2\2\2\u014e\u014f\b*\24\2\u014fV\3\2\2\2\u0150\u0151\5"+
		"\u008fG\2\u0151\u0152\3\2\2\2\u0152\u0153\b+\24\2\u0153X\3\2\2\2\u0154"+
		"\u0155\5\u0091H\2\u0155\u0156\3\2\2\2\u0156\u0157\b,\24\2\u0157Z\3\2\2"+
		"\2\u0158\u015b\5\u0089D\2\u0159\u015b\5\u008bE\2\u015a\u0158\3\2\2\2\u015a"+
		"\u0159\3\2\2\2\u015b\u015e\3\2\2\2\u015c\u015a\3\2\2\2\u015c\u015d\3\2"+
		"\2\2\u015d\u015f\3\2\2\2\u015e\u015c\3\2\2\2\u015f\u0160\7q\2\2\u0160"+
		"\u0161\7p\2\2\u0161\u0162\7G\2\2\u0162\u0163\7p\2\2\u0163\u0164\7v\2\2"+
		"\u0164\u0165\7t\2\2\u0165\u0166\7{\2\2\u0166\u0167\7<\2\2\u0167\\\3\2"+
		"\2\2\u0168\u016b\5\u0089D\2\u0169\u016b\5\u008bE\2\u016a\u0168\3\2\2\2"+
		"\u016a\u0169\3\2\2\2\u016b\u016e\3\2\2\2\u016c\u016a\3\2\2\2\u016c\u016d"+
		"\3\2\2\2\u016d\u016f\3\2\2\2\u016e\u016c\3\2\2\2\u016f\u0170\7q\2\2\u0170"+
		"\u0171\7p\2\2\u0171\u0172\7G\2\2\u0172\u0173\7z\2\2\u0173\u0174\7k\2\2"+
		"\u0174\u0175\7v\2\2\u0175\u0176\7<\2\2\u0176^\3\2\2\2\u0177\u0178\5\u009d"+
		"N\2\u0178\u0179\3\2\2\2\u0179\u017a\b/\24\2\u017a`\3\2\2\2\u017b\u017c"+
		"\5\u009bM\2\u017c\u017d\3\2\2\2\u017d\u017e\b\60\24\2\u017eb\3\2\2\2\u017f"+
		"\u0181\5\u0087C\2\u0180\u0182\5\u0097K\2\u0181\u0180\3\2\2\2\u0182\u0183"+
		"\3\2\2\2\u0183\u0181\3\2\2\2\u0183\u0184\3\2\2\2\u0184\u018b\3\2\2\2\u0185"+
		"\u0187\5}>\2\u0186\u0188\5\u0097K\2\u0187\u0186\3\2\2\2\u0188\u0189\3"+
		"\2\2\2\u0189\u0187\3\2\2\2\u0189\u018a\3\2\2\2\u018a\u018c\3\2\2\2\u018b"+
		"\u0185\3\2\2\2\u018b\u018c\3\2\2\2\u018cd\3\2\2\2\u018d\u018f\5\u0097"+
		"K\2\u018e\u018d\3\2\2\2\u018f\u0190\3\2\2\2\u0190\u018e\3\2\2\2\u0190"+
		"\u0191\3\2\2\2\u0191\u0192\3\2\2\2\u0192\u0193\b\62\24\2\u0193f\3\2\2"+
		"\2\u0194\u0195\13\2\2\2\u0195\u0196\3\2\2\2\u0196\u0197\b\63\24\2\u0197"+
		"h\3\2\2\2\u0198\u0199\7<\2\2\u0199j\3\2\2\2\u019a\u019b\7.\2\2\u019bl"+
		"\3\2\2\2\u019c\u019d\7=\2\2\u019dn\3\2\2\2\u019e\u019f\7?\2\2\u019fp\3"+
		"\2\2\2\u01a0\u01a1\7A\2\2\u01a1r\3\2\2\2\u01a2\u01a3\7,\2\2\u01a3t\3\2"+
		"\2\2\u01a4\u01a5\7B\2\2\u01a5v\3\2\2\2\u01a6\u01a7\7\61\2\2\u01a7\u01a8"+
		"\7\61\2\2\u01a8x\3\2\2\2\u01a9\u01aa\7\61\2\2\u01aaz\3\2\2\2\u01ab\u01ac"+
		"\7#\2\2\u01ac|\3\2\2\2\u01ad\u01ae\7\60\2\2\u01ae~\3\2\2\2\u01af\u01b0"+
		"\7}\2\2\u01b0\u0080\3\2\2\2\u01b1\u01b2\7\177\2\2\u01b2\u0082\3\2\2\2"+
		"\u01b3\u01b4\7~\2\2\u01b4\u0084\3\2\2\2\u01b5\u01b6\7#\2\2\u01b6\u0086"+
		"\3\2\2\2\u01b7\u01b8\7&\2\2\u01b8\u0088\3\2\2\2\u01b9\u01ba\t\2\2\2\u01ba"+
		"\u008a\3\2\2\2\u01bb\u01bc\t\3\2\2\u01bc\u008c\3\2\2\2\u01bd\u01be\7\61"+
		"\2\2\u01be\u01bf\7,\2\2\u01bf\u01c0\7,\2\2\u01c0\u01c4\3\2\2\2\u01c1\u01c3"+
		"\13\2\2\2\u01c2\u01c1\3\2\2\2\u01c3\u01c6\3\2\2\2\u01c4\u01c5\3\2\2\2"+
		"\u01c4\u01c2\3\2\2\2\u01c5\u01c7\3\2\2\2\u01c6\u01c4\3\2\2\2\u01c7\u01c8"+
		"\7,\2\2\u01c8\u01c9\7\61\2\2\u01c9\u008e\3\2\2\2\u01ca\u01cb\7\61\2\2"+
		"\u01cb\u01cc\7%\2\2\u01cc\u01d0\3\2\2\2\u01cd\u01cf\13\2\2\2\u01ce\u01cd"+
		"\3\2\2\2\u01cf\u01d2\3\2\2\2\u01d0\u01d1\3\2\2\2\u01d0\u01ce\3\2\2\2\u01d1"+
		"\u01d3\3\2\2\2\u01d2\u01d0\3\2\2\2\u01d3\u01d4\7%\2\2\u01d4\u01d5\7\61"+
		"\2\2\u01d5\u0090\3\2\2\2\u01d6\u01da\7%\2\2\u01d7\u01d9\n\4\2\2\u01d8"+
		"\u01d7\3\2\2\2\u01d9\u01dc\3\2\2\2\u01da\u01d8\3\2\2\2\u01da\u01db\3\2"+
		"\2\2\u01db\u01ed\3\2\2\2\u01dc\u01da\3\2\2\2\u01dd\u01e1\7\f\2\2\u01de"+
		"\u01e0\5\u0089D\2\u01df\u01de\3\2\2\2\u01e0\u01e3\3\2\2\2\u01e1\u01df"+
		"\3\2\2\2\u01e1\u01e2\3\2\2\2\u01e2\u01e4\3\2\2\2\u01e3\u01e1\3\2\2\2\u01e4"+
		"\u01e8\7%\2\2\u01e5\u01e7\n\4\2\2\u01e6\u01e5\3\2\2\2\u01e7\u01ea\3\2"+
		"\2\2\u01e8\u01e6\3\2\2\2\u01e8\u01e9\3\2\2\2\u01e9\u01ec\3\2\2\2\u01ea"+
		"\u01e8\3\2\2\2\u01eb\u01dd\3\2\2\2\u01ec\u01ef\3\2\2\2\u01ed\u01eb\3\2"+
		"\2\2\u01ed\u01ee\3\2\2\2\u01ee\u0092\3\2\2\2\u01ef\u01ed\3\2\2\2\u01f0"+
		"\u01f4\5\u0095J\2\u01f1\u01f3\5\u0097K\2\u01f2\u01f1\3\2\2\2\u01f3\u01f6"+
		"\3\2\2\2\u01f4\u01f2\3\2\2\2\u01f4\u01f5\3\2\2\2\u01f5\u0094\3\2\2\2\u01f6"+
		"\u01f4\3\2\2\2\u01f7\u01f8\t\5\2\2\u01f8\u0096\3\2\2\2\u01f9\u01fc\5\u0095"+
		"J\2\u01fa\u01fc\t\6\2\2\u01fb\u01f9\3\2\2\2\u01fb\u01fa\3\2\2\2\u01fc"+
		"\u0098\3\2\2\2\u01fd\u0200\5\u009dN\2\u01fe\u0200\5\u009bM\2\u01ff\u01fd"+
		"\3\2\2\2\u01ff\u01fe\3\2\2\2\u0200\u009a\3\2\2\2\u0201\u0206\7)\2\2\u0202"+
		"\u0205\5\u00a3Q\2\u0203\u0205\n\7\2\2\u0204\u0202\3\2\2\2\u0204\u0203"+
		"\3\2\2\2\u0205\u0208\3\2\2\2\u0206\u0204\3\2\2\2\u0206\u0207\3\2\2\2\u0207"+
		"\u0209\3\2\2\2\u0208\u0206\3\2\2\2\u0209\u020a\7)\2\2\u020a\u009c\3\2"+
		"\2\2\u020b\u0210\7$\2\2\u020c\u020f\5\u00a3Q\2\u020d\u020f\n\b\2\2\u020e"+
		"\u020c\3\2\2\2\u020e\u020d\3\2\2\2\u020f\u0212\3\2\2\2\u0210\u020e\3\2"+
		"\2\2\u0210\u0211\3\2\2\2\u0211\u0213\3\2\2\2\u0212\u0210\3\2\2\2\u0213"+
		"\u0214\7$\2\2\u0214\u009e\3\2\2\2\u0215\u0217\t\t\2\2\u0216\u0215\3\2"+
		"\2\2\u0217\u0218\3\2\2\2\u0218\u0216\3\2\2\2\u0218\u0219\3\2\2\2\u0219"+
		"\u00a0\3\2\2\2\u021a\u021b\t\n\2\2\u021b\u00a2\3\2\2\2\u021c\u021f\7^"+
		"\2\2\u021d\u0220\5\u00a5R\2\u021e\u0220\13\2\2\2\u021f\u021d\3\2\2\2\u021f"+
		"\u021e\3\2\2\2\u0220\u00a4\3\2\2\2\u0221\u022c\7w\2\2\u0222\u022a\5\u00a1"+
		"P\2\u0223\u0228\5\u00a1P\2\u0224\u0226\5\u00a1P\2\u0225\u0227\5\u00a1"+
		"P\2\u0226\u0225\3\2\2\2\u0226\u0227\3\2\2\2\u0227\u0229\3\2\2\2\u0228"+
		"\u0224\3\2\2\2\u0228\u0229\3\2\2\2\u0229\u022b\3\2\2\2\u022a\u0223\3\2"+
		"\2\2\u022a\u022b\3\2\2\2\u022b\u022d\3\2\2\2\u022c\u0222\3\2\2\2\u022c"+
		"\u022d\3\2\2\2\u022d\u00a6\3\2\2\2$\2\3\4\u00f1\u00f8\u0137\u013f\u015a"+
		"\u015c\u016a\u016c\u0183\u0189\u018b\u0190\u01c4\u01d0\u01da\u01e1\u01e8"+
		"\u01ed\u01f4\u01fb\u01ff\u0204\u0206\u020e\u0210\u0218\u021f\u0226\u0228"+
		"\u022a\u022c\26\2\3\2\7\3\2\7\4\2\b\2\2\t\7\2\t\b\2\t\n\2\t\4\2\6\2\2"+
		"\t\31\2\t\26\2\t\20\2\t\17\2\t\22\2\t\3\2\t\32\2\t\33\2\t\34\2\t\5\2\3"+
		")\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}