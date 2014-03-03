/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.searchandfilter.editor.client.configuration;

import java.io.Serializable;
import java.util.List;


/**
 * Search Dto object.
 *
 * @author Jan Venstermans
 */
public class SearchAttribute implements Serializable {

	private static final long serialVersionUID = 100L;

	private String attribute, label, operation, inputType;

	private List<String> inputTypeValues;

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public List<String> getInputTypeValues() {
		return inputTypeValues;
	}

	public void setInputTypeValues(List<String> inputTypeValues) {
		this.inputTypeValues = inputTypeValues;
	}
}
