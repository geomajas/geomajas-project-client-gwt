/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.searchandfilter.search.dto;

import java.util.ArrayList;
import java.util.List;


/**
 * Dto object for specification of a search rule on an attribute of a vecotor layer.
 *
 * @author Jan Venstermans
 */
public class ConfiguredSearchAttribute extends AttributeCriterion {

	private static final long serialVersionUID = 100L;

	private AttributeType attributeType;

	private Operation operation;

	private InputType inputType;

	private List<String> inputTypeDropDownValues = new ArrayList<String>();

	/**
	 * Datatype of attribute. This type will know what
	 * {@link ConfiguredSearchAttribute.Operation} and
	 * {@link ConfiguredSearchAttribute.InputType}
	 * values can be associated with itself.
	 */
	public enum AttributeType {
		String(new Operation[] {Operation.EqualToString},
				new InputType[] {InputType.FreeValue, InputType.DropDown}),
		Integer(new Operation[] {Operation.EqualToInteger, Operation.SmallerThan, Operation.LargerThan},
				new InputType[] {InputType.FreeValue, InputType.DropDown});

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
		EqualToString("like"), //use "like" in stead of "=" to enable wildcards
		EqualToInteger("="),
		SmallerThan("<"),
		LargerThan(">");

		/**
		 *   A code string representation of the search operator. Inspired by
		 *   {@link org.geomajas.gwt.client.widget.attribute
		 *                       .AttributeCriterionPane#getOperatorCodeFromLabel(String)}
		 */
		private String operatorCode;

		private Operation(String operatorCode) {
			this.operatorCode = operatorCode;
		}

		public String getOperatorCode() {
			return operatorCode;
		}
	}

	/**
	 * Type of input for the reference value of the operation.
	 */
	public enum InputType {
		FreeValue, DropDown;
	}

	public AttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(AttributeType attributeType) {
		this.attributeType = attributeType;
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

	@Override
	public String getDisplayText() {
		if (super.getDisplayText().equals(super.toString())) {
			return null;
		}
		return super.getDisplayText();
	}
}
