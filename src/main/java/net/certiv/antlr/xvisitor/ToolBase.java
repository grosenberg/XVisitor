package net.certiv.antlr.xvisitor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.LogManager;

public abstract class ToolBase implements ITool, IToolListener {

	public static final String MSG_FORMAT = "antlr";
	public static final Token INVALID_TOKEN = new CommonToken(Token.INVALID_TYPE);

	// defined options available in the grammar
	public static final String SUPER_CLASS = "superClass";
	public static final String PARSER_CLASS = "parserClass";
	public static final String TOKEN_CLASS = "tokenClass";

	private List<IToolListener> listeners = new CopyOnWriteArrayList<>();
	private DefaultToolListener defaultListener;

	public ErrorManager errMgr;
	public LogManager logMgr = new LogManager();

	/** Create an instance of the tool, pending configuration and use. */
	public ToolBase() {
		super();
		errMgr = new ErrorManager(this);
		errMgr.setFormat(MSG_FORMAT);
		defaultListener = new DefaultToolListener(this);
	}

	@Override
	public ErrorManager getErrMgr() {
		return errMgr;
	}

	protected DefaultToolListener getDefaultListener() {
		return defaultListener;
	}

	public void log(String msg) {
		log(null, msg);
	}

	public void log(String component, String msg) {
		logMgr.log(component, msg);
	}

	@Override
	public void info(String msg) {
		if (listeners.isEmpty()) {
			defaultListener.info(msg);
		} else {
			for (IToolListener l : listeners) {
				l.info(msg);
			}
		}
	}

	@Override
	public void warn(Messages msg) {
		if (listeners.isEmpty()) {
			defaultListener.warn(msg);
		} else {
			for (IToolListener l : listeners) {
				l.warn(msg);
			}
		}
	}

	@Override
	public void error(Messages msg) {
		if (listeners.isEmpty()) {
			defaultListener.error(msg);
		} else {
			for (IToolListener l : listeners) {
				l.error(msg);
			}
		}
	}

	public int getNumWarnings() {
		return errMgr.getNumWarnings();
	}

	public int getNumErrors() {
		return errMgr.getNumErrors();
	}

	public List<IToolListener> getListeners() {
		return listeners;
	}

	public void addListener(IToolListener tl) {
		if (tl != null) listeners.add(tl);
	}

	public void removeListener(IToolListener tl) {
		listeners.remove(tl);
	}

	public void removeListeners() {
		listeners.clear();
	}

	public abstract void version();
}
