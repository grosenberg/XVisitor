/*******************************************************************************
 * Copyright (c) 2010-2023 Gerald Rosenberg & others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the standard 3-clause BSD License.  A copy of the License
 * is provided with this distribution in the License.txt file.
 *******************************************************************************/
package net.certiv.antlr.xvisitor.codegen;

import java.io.IOException;
import java.io.Writer;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import net.certiv.antlr.xvisitor.tool.ErrorType;

public class CodeGenerator {

	public static final String TEMPLATE_ROOT = "net/certiv/antlr/xvisitor/codegen";
	public static final String TEMPLATE_NAME = "XVisitor.stg";

	private CodeGenModel model;

	public CodeGenerator(CodeGenModel model) {
		this.model = model;
	}

	public void generate() {
		StringBuilder result = new StringBuilder();

		STGroup group = new STGroupFile(TEMPLATE_ROOT + "/" + TEMPLATE_NAME);
		group.registerRenderer(Integer.class, new IndexOffsetRenderer());

		ST st = group.getInstanceOf("VisitorTemplate");
		st.add("model", model);
		result.append(st.render(72));

		try (Writer w = model.tool.getOutputFileWriter(model);) {
			w.write(result.toString());
		} catch (IOException e) {
			model.tool.getErrMgr().toolError(ErrorType.CANNOT_WRITE_FILE, e);
		}
	}
}
