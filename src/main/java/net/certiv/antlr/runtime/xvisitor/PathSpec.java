/*******************************************************************************
 * Copyright (c) 2010-2015 Gerald Rosenberg & others. All rights reserved.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the standard 3-clause BSD License.  A copy of the License 
 * is provided with this distribution in the License.txt file.
 *******************************************************************************/
package net.certiv.antlr.runtime.xvisitor;

import java.util.ArrayList;
import java.util.List;

import net.certiv.antlr.runtime.xvisitor.xpath.Element;

/**
 * Ordered list of typed Elements that define a corresponding XPath
 * 
 * @author Gbr
 */
public class PathSpec {

	private List<Element> xpath;	// list of elements that make up this xpath
	private String name; 			// rule name associated with this xpath
	private int id = -1;			// unique id associated with this xpath

	private boolean active; 		// state of this xpath
	private boolean valid; 			// properly constructed?

	public PathSpec(String name) {
		super();
		this.name = name;
		this.xpath = new ArrayList<>();
	}

	public int id() {
		return id;
	}

	public void id(int id) {
		if (this.id == -1) {
			this.id = id;
			this.valid = true; // TODO: validate?
		}
	}

	public String getName() {
		return name;
	}

	public boolean isValid() {
		return valid;
	}

	protected void setValid(boolean valid) {
		this.valid = valid;
	}

	public boolean isActive() {
		return valid && active;
	}

	public void setActive(boolean state) {
		this.active = state;
	}

	public void add(Element elem) {
		xpath.add(elem);
	}

	public Element get(int elemIdx) {
		return xpath.get(elemIdx);
	}

	public int size() {
		return xpath.size();
	}

	public void clear() {
		xpath.clear();
	}

	public String toString(int elemIdx) {
		String eStr = get(elemIdx).toString();
		return name + " [" + id + ": " + eStr + "]";
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name + " (" + id + ") ");
		for (Element elem : xpath) {
			sb.append(elem.toString());
		}
		return sb.toString();
	}
}
