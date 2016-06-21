#### Quick Start {#quickstart}

Given:

```
LangParser.g4		// grammar used to generate a parse tree
LangLexer.g4
input.txt			// contains source text to parse
LangVisitor.xv		// XVisitor grammar 
```

Compile:

```
java org.antlr.v4.Tool -o .\gen -no-listener LangLexer.g4 LangParser.g4

java net.certiv.antlr.xvisitor.Tool -o .\gen -lib .\gen LangVisitor.xv
```

* Note: the xvisitor generator tool requires access to the Antlr generated parser.  
* Use the '-lib' command line option to specifiy the directory containing the Antlr generated parser.


Invoke:

```
ANTLRFileStream input = new ANTLRFileStream(input.txt);
LangLexer lexer = new LangLexer(input);
CommonTokenStream tokens = new CommonTokenStream(lexer);
LangParser parser = new LangParser(tokens);
LangContext parseTree = parser.lang();

LangVisitor visitor = new LangVisitor(parseTree);
visitor.findAll();
```

#### Runtime {#runtime}

The XVisitor runtime provides two entry points for invoking the visitor.


```
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
```

Within Actions, the runtime provides the following context discovery methods:

```
/**
 * Returns a list of parse tree path nodes evaluated in the current path. 
 * By default, this is just the non-wildcard'd nodes, <i>i.e.</i>, just 
 * the nodes symbolically identified in the rule path specification.
 *   
 * If {@link Processor#keepAllPathContexts(boolean keep) is set,
 * then all path nodes are recorded, and the result will be equivalent 
 * to Processor#ancestors()}.
 */
public List<ParseTree> pathNodes() { .... } 

/**
 * Returns the current context 
 */
public ParseTree lastPathNode() { .... } 

/**
 * Returns the parent chain of parse-tree node ancestors starting with  
 * the current node and ending at the root node, inclusive.
 */
public List<ParseTree> ancestors() { .... } 

/**
 * Returns whether a parse tree node of the given ruleIndex corresponding 
 * types exist as an ancestor of the current node. The generated visitor
 * defines the literal values of the known ruleIndexes.
 */
public boolean hasAncestor(int... ruleIndexes) { .... }
```
