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
package org.geomajas.widget.searchandfilter.configuration.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Dto object for specification of a search rule on an attribute of a vecotor layer.
 *
 * @author Jan Venstermans
 */
public class SearchAttribute implements Serializable {

	private static final long serialVersionUID = 100L;

	private AttributeType attributeType;

	private String attributeName;

	private String label;

	private Operation operation;

	private InputType inputType;

	private List<String> inputTypeDropDownValues = new ArrayList<String>();

	/**
	 * Datatype of attribute. This type will know what
	 * {@link org.geomajas.widget.searchandfilter.configuration.client.SearchAttribute.Operation} and
	 * {@link org.geomajas.widget.searchandfilter.configuration.client.SearchAttribute.InputType}
	 * values can be associated with itself.
	 */
	public enum AttributeType {
		String(new Operation[] {Operation.EqualTo},
				new InputType[] {InputType.FreeText, InputType.DropDown}),
		Integer(new Operation[] {Operation.EqualTo, Operation.SmallerThan, Operation.LargerThan},
				new InputType[] {InputType.FreeNr, InputType.DropDown});

		private Operation[] operations;
		private InputType[] inputTypes;

		private AttributeType(Operation[] operations, InputType[] inputTypes) {
			 this.operations = operations;
			 this.inputTypes = inputTypes;
		}

		public Operation[] getOperations() {
			return operations;
		}

		public InputType[] getInputTypes() {
			return inputTypes;
		}

		public void setOperations(Operation[] operations) {
			this.operations = operations;
		}

		public void setInputTypes(InputType[] inputTypes) {
			this.inputTypes = inputTypes;
		}
	}

	/**
	 * Operation type that can be applied to the value of an vector layer attribute.
	 */
	public enum Operation {
		EqualTo, SmallerThan, LargerThan;
	}

	/**
	 * Type of input for the reference value of the operation.
	 */
	public enum InputType {
		FreeText, DropDown, FreeNr;
	}

	public AttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(AttributeType attributeType) {
		this.attributeType = attributeType;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public InputType getInputType() {
		return inputType;
	}

	public void setInputType(InputType inputType) {
		this.inputType = inputType;
	}

	public List<String> getInputTypeDropDownValues() {
		return inputTypeDropDownValues;
	}

	public void setInputTypeDropDownValues(List<String> inputTypeDropDownValues) {
		this.inputTypeDropDownValues = inputTypeDropDownValues;
	}
}
