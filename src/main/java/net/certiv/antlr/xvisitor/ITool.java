package net.certiv.antlr.xvisitor;

import java.io.IOException;
import java.io.Writer;

import net.certiv.antlr.xvisitor.codegen.CodeGenModel;

public interface ITool {

	String getOutputDirectory();

	String getLibDirectory();

	ErrorManager getErrMgr();

	// used for code gen only
	Writer getOutputFileWriter(CodeGenModel model) throws IOException;
}
