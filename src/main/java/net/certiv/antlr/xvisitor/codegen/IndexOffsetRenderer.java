package net.certiv.antlr.xvisitor.codegen;

import java.util.Locale;

import org.stringtemplate.v4.AttributeRenderer;

/**
 * Renders an int value as that value plus a constant offset.
 */
public class IndexOffsetRenderer implements AttributeRenderer {

	// o is expected to be an int value
	// offset is a string containing the offset value, e.g., "500"
	public String toString(Object o, String offset, Locale locale) {
		if (offset == null) return o.toString();

		// convert o to int; registration ensures that o is Integer
		int value = ((Integer) o).intValue();

		// convert offset to int and add
		try {
			value += Integer.valueOf(offset).intValue();
		} catch (NumberFormatException e) {
			value += 500; // silent exception default
		}
		return String.valueOf(value);
	}
}
