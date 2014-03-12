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
package org.geomajas.widget.searchandfilter.client.widget.search;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.BlurbItem;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import org.geomajas.configuration.AbstractReadOnlyAttributeInfo;
import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AssociationType;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.attribute.AttributeFormFieldRegistry;
import org.geomajas.widget.searchandfilter.search.dto.AttributeCriterion;

import java.util.Date;

/**
 * General abstract version of  {@link org.geomajas.gwt.client.widget.attribute
 * .AttributeCriterionPane}. It is a canvas for showing info of an
 * {@link org.geomajas.widget.searchandfilter.search.dto.AttributeCriterion} object.
 *
 * @author Pieter De Graef
 * @author Kristof Heirwegh
 * @author Jan Venstermans
 */
public abstract class AbstractAttributeCriterionPane extends Canvas {

	protected static final String CQL_WILDCARD = "*";
	protected static final String CQL_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZZ";
	protected static final String ID_SUFFIX = ".@id";

	protected FormItem attributeSelect;

	protected FormItem operatorSelect;

	protected AttributeFormItem valueItem;

	protected VectorLayer layer;

	protected AbstractReadOnlyAttributeInfo selectedAttribute;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Create a search criterion pane, for the given vector layer. The layer is required, as it's list of attribute
	 * definitions are a vital part of the search criteria.
	 *
	 * @param layer layer to create criterion for
	 */
	public AbstractAttributeCriterionPane(VectorLayer layer) {
		super();
		this.layer = layer;

		buildUI();
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Validate the value that the user filled in. If it is not valid, don't ask for the SearchCriterion.
	 *
	 * @return true when user entered invalid value
	 */
	public boolean hasErrors() {
		return valueItem.getForm().hasErrors();
	}

	public abstract void setSearchCriterion(AttributeCriterion ac);
	public abstract String getOperatorString();

	/**
	 * Return the actual search criterion object, or null if not all fields have been properly filled.
	 *
	 * @return search criterion
	 */
	public AttributeCriterion getSearchCriterion() {
		String operatorString = getOperatorString();
		Object value = valueItem.getValue();

		if (selectedAttribute != null && operatorString != null) {
			String valueString = "";
			String nameString = selectedAttribute.getName();
			String displayText = nameString + " " + operatorString + " " + valueItem.getDisplayValue();
			if (value != null) {
				valueString = value.toString();
			}

			// CQL does not recognize "contains", so change to "like":
			if ("contains".equals(operatorString)) {
				operatorString = "like";
				valueString = CQL_WILDCARD + valueString + CQL_WILDCARD;
			}

			// If value was null, and no "contains" operator, return null:
			if (valueString == null || valueString.length() == 0) {
				return null;
			}

			if (selectedAttribute instanceof PrimitiveAttributeInfo) {
				PrimitiveAttributeInfo attr = (PrimitiveAttributeInfo) selectedAttribute;
				if (attr.getType().equals(PrimitiveType.DATE)) {
					if (value instanceof Date) {
						// In case of a date, parse correctly for CQL: 2006-11-30T01:30:00Z
						DateTimeFormat format = DateTimeFormat.getFormat(CQL_TIME_FORMAT);
						valueString = format.format((Date) value);
						if ("=".equals(operatorString)) {
							// Date equals not supported by CQL, so we use the DURING operator instead and
							// create a day period for this day (browser time zone !)
							operatorString = "DURING";
							String startOfDay = valueString.replaceAll("\\d\\d:\\d\\d:\\d\\d", "00:00:00");
							// 1 day period, starting at 0h00
							valueString = startOfDay + "/P1D";
						} else if ("AFTER".equals(operatorString)) {
							// we can't discriminate between date and timestamp values yet, use end of day for now
							valueString = valueString.replaceAll("\\d\\d:\\d\\d:\\d\\d", "23:59:59");
						} else if ("BEFORE".equals(operatorString)) {
							// we can't discriminate between date and timestamp values yet, use start of day for now
							valueString = valueString.replaceAll("\\d\\d:\\d\\d:\\d\\d", "00:00:00");
						}
					}
				}
			} else if (selectedAttribute instanceof AssociationAttributeInfo) {
				AssociationAttributeInfo assInfo = (AssociationAttributeInfo) selectedAttribute;
				if (AssociationType.MANY_TO_ONE == assInfo.getType()) {
					nameString = nameString + ID_SUFFIX;
				}
			}

			// Now create the criterion:
			AttributeCriterion criterion = new AttributeCriterion();
			criterion.setServerLayerId(layer.getServerLayerId());
			criterion.setAttributeName(nameString);
			criterion.setOperator(operatorString);
			criterion.setValue(valueString);
			criterion.setDisplayText(displayText);
			return criterion;
		}
		return null;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	protected abstract FormItem createAttributeFormItem();
	protected abstract FormItem createOperatorFormItem();

	protected AttributeFormItem createValueAttributeFormItem() {
		AttributeFormItem attributeFormItem = new AttributeFormItem("valueItem");
		attributeFormItem.setShowTitle(false);
		attributeFormItem.setDisabled(true);
		attributeFormItem.setWidth(150);
		return attributeFormItem;
	}

	protected void buildUI() {
		// Attribute select:
		attributeSelect = createAttributeFormItem();

		// Operator select:
		operatorSelect = createOperatorFormItem();

		// Value form item:
		valueItem = createValueAttributeFormItem();

		// Finalize:
		DynamicForm form = new DynamicForm();
		form.setNumCols(6);
		form.setHeight(26);
		form.setFields(attributeSelect, operatorSelect, valueItem);
		addChild(form);
	}

	/* creator methods for default form elements */
	protected SelectItem createSelectItem(String name) {
		SelectItem formItem = new SelectItem(name);
		formItem.setWidth(140);
		formItem.setShowTitle(false);

		formItem.setValidateOnChange(true);
		formItem.setShowErrorStyle(true);
		formItem.setRequired(true);
		return formItem;
	}

	protected StaticTextItem createStaticTextItem(String name) {
		StaticTextItem formItem = new StaticTextItem(name);
		formItem.setDisabled(true);
		formItem.setWidth(140);
		formItem.setShowTitle(false);

		//formItem.setValidateOnChange(true);
		//formItem.setShowErrorStyle(true);
		formItem.setRequired(true);
		return formItem;
	}

	/**
	 * <p>
	 * Editable form item implementation that can edit any kind of feature attribute. It starts by using a default
	 * <code>TextItem</code> as <code>FormItem</code> representative. Every time the <code>setAttributeInfo</code>
	 * method is called, a new internal <code>FormItem</code> will be created and shown in the place of the
	 * <code>TextItem</code>. In order to create the correct representation for each kind of attribute, a
	 * {@link org.geomajas.gwt.client.widget.attribute.AttributeFormFieldRegistry} is used.
	 * </p>
	 *
	 * @author Pieter De Graef
	 */
	protected class AttributeFormItem extends CanvasItem {

		private DynamicForm form;

		protected FormItem formItem;

		// -------------------------------------------------------------------------
		// Constructors:
		// -------------------------------------------------------------------------

		/**
		 * Create the form item with the given name. An internal form will already be created, and in that form a
		 * <code>TextItem</code> will be shown.
		 *
		 * @param name form item name
		 */
		public AttributeFormItem(String name) {
			super(name);

			form = new DynamicForm();
			form.setHeight(26);
			formItem = new BlurbItem();
			formItem.setShowTitle(false);
			formItem.setValue("...................");
			form.setFields(formItem);
			setCanvas(form);
		}

		// -------------------------------------------------------------------------
		// Public methods:
		// -------------------------------------------------------------------------

		/**
		 * Set a new attribute information object. This will alter the internal form, to display a new
		 * <code>FormItem</code> for the new type of attribute. In order to accomplish this, a
		 * {@link org.geomajas.gwt.client.widget.attribute.AttributeFormFieldRegistry} is used.
		 *
		 * @param attributeInfo The new attribute definition for which to display the correct <code>FormItem</code>.
		 */
		public void setAttributeInfo(AbstractReadOnlyAttributeInfo attributeInfo) {
			formItem = AttributeFormFieldRegistry.createFormItem(attributeInfo, layer);
			updateFormStructure();
		}

		protected void updateFormStructure() {
			if (formItem != null) {
				formItem.setDisabled(false);
				formItem.setShowTitle(false);
				form.setFields(formItem);
				form.setDisabled(false);
				form.setCanFocus(true);
			}
		}

		/**
		 * Set a new width on this instance. Delegates to the internal form.
		 *
		 * @param  width width
		 */
		public void setWidth(int width) {
			form.setWidth(width);
			if (formItem != null) {
				formItem.setWidth(width);
			}
		}

		/**
		 * Get the current value form the internal <code>FormItem</code>.
		 *
		 * @return value
		 */
		public Object getValue() {
			if (formItem != null) {
				return formItem.getValue();
			}
			return null;
		}

		/** Get the current value form the internal <code>FormItem</code>. */
		public void setValue(String value) {
			if (formItem != null) {
				formItem.setValue(value);
			}
		}

		/**
		 * Get the display value form the internal <code>FormItem</code>.
		 *
		 * @return value
		 */
		public String getDisplayValue() {
			if (formItem != null) {
				return formItem.getDisplayValue();
			}
			return null;
		}

		/**
		 * Return the form for the inner FormItem. On the returned form, validation will work.
		 *
		 * @return form
		 */
		public DynamicForm getForm() {
			return form;
		}
	}
}