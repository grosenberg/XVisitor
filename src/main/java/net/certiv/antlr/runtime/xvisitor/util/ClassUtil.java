package net.certiv.antlr.runtime.xvisitor.util;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

public class ClassUtil {

	/** Dumps he packages defined in this class loader. */
	@SuppressWarnings("unchecked")
	public static String dump(Object loader) {

		StringBuilder sb = new StringBuilder();

		if (loader instanceof URLClassLoader) {
			URLClassLoader urlLoader = (URLClassLoader) loader;
			for (URL url : urlLoader.getURLs()) {
				sb.append(url.toString() + ";");
			}

		} else {
			Class<?> lc = loader.getClass();
			while (lc != ClassLoader.class) {
				lc = lc.getSuperclass();
			}

			HashMap<String, Package> packages;
			try {
				packages = (HashMap<String, Package>) Reflect.get(lc, "packages");
			} catch (NoSuchFieldException e) {
				return "Error: " + e.getMessage();
			}

			for (String pkg : packages.keySet()) {
				sb.append(pkg + ";");
			}
		}

		return sb.toString();
	}
}
