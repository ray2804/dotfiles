/*
 *  RapidMiner
 *
 *  Copyright (C) 2001-2013 by Rapid-I and the contributors
 *
 *  Complete list of developers available at our web site:
 *
 *       http://rapid-i.com
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package com.rapidminer.parameter;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.rapidminer.tools.XMLException;

/**
 * A parameter type for files. Operators ask for the selected file with
 * {@link com.rapidminer.operator.Operator#getParameterAsFile(String)}. The
 * extension should be defined without the point (separator).
 * 
 * @author Ingo Mierswa, Simon Fischer
 */
public class ParameterTypeFile extends ParameterTypeString {

	private static final long serialVersionUID = -1350352634043084406L;

	private static final String ATTRIBUTE_EXTENSION_ELEMENT = "extension";
	private static final String ATTRIBUTE_EXTENSION_ATTRIBUTE = "value";

	private String[] extensions = null;

	public ParameterTypeFile(Element element) throws XMLException {
		super(element);

		int counter = 0;
		for (int i = 0; i < element.getChildNodes().getLength(); i++) {
			Node extensionNode = element.getChildNodes().item(i);
			if (extensionNode instanceof Element) {
				Element extensionElement = (Element) extensionNode;
				if (extensionElement.getNodeName().equals(ATTRIBUTE_EXTENSION_ELEMENT)) {
					this.extensions[counter++] = extensionElement.getAttribute(ATTRIBUTE_EXTENSION_ATTRIBUTE);
				}
			}
		}
	}

	/** Creates a new parameter type for files with the given extension. If the extension is null
	 *  no file filters will be used. If the parameter is not optional, it is set to be not expert.*/
	public ParameterTypeFile(String key, String description, boolean optional, String[] extensions) {
		super(key, description, null);
		this.extensions = new String[] { null };
		setOptional(optional);
		this.extensions = extensions;
	}

	/** Creates a new parameter type for files with the given extension. If the extension is null
	 *  no file filters will be used. If the parameter is not optional, it is set to be not expert.*/
	public ParameterTypeFile(String key, String description, String extension, boolean optional) {
		super(key, description, null);
		setOptional(optional);
		this.extensions = new String[] { extension };
	}

	/** Creates a new parameter type for file with the given extension. If the extension is null
	 *  no file filters will be used. The parameter will be optional. */
	public ParameterTypeFile(String key, String description, String extension, String defaultFileName) {
		super(key, description, defaultFileName);
		this.extensions = new String[] { extension };
	}

	public ParameterTypeFile(String key, String description, String extension, boolean optional, boolean expert) {
		this(key, description, extension, optional);
		setExpert(expert);
	}

	public String getExtension() {
		return extensions[0];
	}

	public String[] getExtensions() {
		return extensions;
	}

	public String[] getKeys() {
		String[] keys = new String[extensions.length];
		for (int i = 0; i < extensions.length; i++) {
			keys[i] = getKey();
		}
		return keys;
	}

	public void setExtension(String extension) {
		this.extensions[0] = extension;
	}

	public void setExtensions(String[] extensions) {
		this.extensions = extensions;
	}

	@Override
	public String getRange() {
		return "filename";
	}

	@Override
	protected void writeDefinitionToXML(Element typeElement) {
		super.writeDefinitionToXML(typeElement);
		if (extensions != null) {
			for (String extension : extensions) {
				Element extensionElement = typeElement.getOwnerDocument().createElement(ATTRIBUTE_EXTENSION_ELEMENT);
				extensionElement.setAttribute(ATTRIBUTE_EXTENSION_ATTRIBUTE, extension);
				typeElement.appendChild(extensionElement);
			}
		}
	}

}
