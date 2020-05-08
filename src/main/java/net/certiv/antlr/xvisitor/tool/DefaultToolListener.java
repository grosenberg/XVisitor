/*
 * [The "BSD license"] Copyright (c) 2012 Terence Parr Copyright (c) 2012 Sam Harwell All rights
 * reserved. Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met: 1. Redistributions of source code must
 * retain the above copyright notice, this list of conditions and the following disclaimer. 2.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. 3. The name of the author may not be used to endorse or promote products derived
 * from this software without specific prior written permission. THIS SOFTWARE IS PROVIDED BY THE
 * AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 */
package net.certiv.antlr.xvisitor.tool;

import org.stringtemplate.v4.ST;

import net.certiv.antlr.runtime.xvisitor.util.Strings;
import net.certiv.antlr.xvisitor.ITool;
import net.certiv.antlr.xvisitor.IToolListener;

public class DefaultToolListener implements IToolListener {

	public final ITool tool;
	private Level level;

	public DefaultToolListener(ITool tool) {
		this.tool = tool;
		level = Level.WARN;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	@Override
	public void info(String msg) {
		if (skip(Level.INFO)) return;
		if (tool.getErrMgr().formatWantsSingleLineMessage()) {
			msg = Strings.inline(msg);
		}
		System.out.println(msg);
	}

	@Override
	public void warn(Messages msg) {
		if (skip(Level.WARN)) return;
		ST msgST = tool.getErrMgr().getMessageTemplate(msg);
		String out = msgST.render();
		if (tool.getErrMgr().formatWantsSingleLineMessage()) {
			out = Strings.inline(out);
		}
		System.err.println(out);
	}

	@Override
	public void error(Messages msg) {
		if (skip(Level.ERROR)) return;
		ST msgST = tool.getErrMgr().getMessageTemplate(msg);
		String out = msgST.render();
		if (tool.getErrMgr().formatWantsSingleLineMessage()) {
			out = Strings.inline(out);
		}
		System.err.println(out);
	}

	private boolean skip(Level target) {
		switch (level) {
			default:
			case INFO:
				return false;
			case WARN:
				if (target != Level.INFO) return false;
				return true;
			case ERROR:
				if (target == Level.ERROR) return false;
				return true;
			case QUIET:
				return true;
		}
	}
}
