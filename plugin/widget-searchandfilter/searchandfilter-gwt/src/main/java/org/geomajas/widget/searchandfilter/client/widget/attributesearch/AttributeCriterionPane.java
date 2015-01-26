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
package org.geomajas.widget.searchandfilter.client.widget.attributesearch;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.BlurbItem;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.AbstractReadOnlyAttributeInfo;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.attribute.AttributeFormFieldRegistry;
import org.geomajas.widget.searchandfilter.client.util.AttributeCriterionUtil;
import org.geomajas.widget.searchandfilter.search.dto.AttributeCriterion;

/**
 * Adjusted from see {@link AttributeCriterionPane} to work with AttributeCriterion.
 *
 * @author Pieter De Graef
 * @author Kristof Heirwegh
 */
public class AttributeCriterionPane extends Canvas {

	private static final String CQL_WILDCARD = "*";
	private static final String CQL_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZZ";
	private static final String ID_SUFFIX = ".@id";

	private SelectItem attributeSelect;

	private SelectItem operatorSelect;

	private AttributeFormItem valueItem;

	private VectorLayer layer;

	private AbstractReadOnlyAttributeInfo selectedAttribute;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Create a search criterion pane, for the given vector layer. The layer is required, as it's list of attribute
	 * definitions are a vital part of the search criteria.
	 *
	 * @param layer layer to create criterion for
	 */
	public AttributeCriterionPane(VectorLayer layer) {
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

	public void setSearchCriterion(AttributeCriterion ac) {
		if (ac != null) {
			attributeSelect.setValue(getAttributeByName(ac.getAttributeName()).getLabel());
			attributeChanged();
			operatorSelect.setValue(AttributeCriterionUtil.getLabelFromOperatorCode(ac.getOperator()));
			valueItem.setValue(trimLikeValue(ac.getValue(), ac.getOperator()));
		}
	}

	private String trimLikeValue(String value, String operator) {
		if ("like".equalsIgnoreCase(operator) && value != null) {
			String tmp = value;
			if (tmp.startsWith(CQL_WILDCARD)) {
				tmp = tmp.substring(1, tmp.length());
			}
			if (tmp.endsWith(CQL_WILDCARD)) {
				tmp = tmp.substring(0, tmp.length() - 1);
			}
			return tmp;
		} else {
			return value;
		}
	}

	/**
	 * Return the actual search criterion object, or null if not all fields have been properly filled.
	 *
	 * @return search criterion
	 */
	public AttributeCriterion getSearchCriterion() {
		Object operator = operatorSelect.getValue();
		if (operator != null) {
			return AttributeCriterionUtil.getSearchCriterion(layer.getServerLayerId(), selectedAttribute, valueItem,
					org.geomajas.gwt.client.widget.attribute.AttributeCriterionPane.
							getOperatorCodeFromLabel(operator.toString()));
		}
		return null;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private void buildUI() {

		// Attribute select:
		attributeSelect = new SelectItem("attributeItem");
		attributeSelect.setWidth(140);
		attributeSelect.setShowTitle(false);
		attributeSelect.setValueMap(org.geomajas.gwt.client.widget.attribute.AttributeCriterionPane.
				getSearchableAttributes(layer));
		attributeSelect.setHint(I18nProvider.getSearch().gridChooseAttribute());
		attributeSelect.setShowHintInField(true);

		attributeSelect.setValidateOnChange(true);
		attributeSelect.setShowErrorStyle(true);
		attributeSelect.setRequired(true);

		// Operator select:
		operatorSelect = new SelectItem("operatorItem");
		operatorSelect.setDisabled(true);
		operatorSelect.setWidth(140);
		operatorSelect.setShowTitle(false);

		operatorSelect.setValidateOnChange(true);
		operatorSelect.setShowErrorStyle(true);
		operatorSelect.setRequired(true);

		// Value form item:
		valueItem = new AttributeFormItem("valueItem");
		valueItem.setShowTitle(false);
		valueItem.setDisabled(true);
		valueItem.setWidth(150);

		// Mechanisms:
		attributeSelect.addChangedHandler(new ChangedHandler() {
			public void onChanged(ChangedEvent event) {
				attributeChanged();
			}
		});

		// Finalize:
		DynamicForm form = new DynamicForm();
		form.setNumCols(6);
		form.setHeight(26);
		form.setFields(attributeSelect, operatorSelect, valueItem);
		addChild(form);
	}

	private void attributeChanged() {
		selectedAttribute = getSelectedAttribute();
		if (selectedAttribute != null) {
			// Adjust operator value map and enabled:
			operatorSelect.setDisabled(false);
			String[] operators = org.geomajas.gwt.client.widget.attribute.AttributeCriterionPane.
					getOperatorsForAttributeType(selectedAttribute);
			operatorSelect.setValueMap(operators);
			operatorSelect.setValue(operators[0]);

			// Adjust value form item and enable:
			valueItem.setAttributeInfo(selectedAttribute);
			valueItem.setDisabled(false);
		}
	}

	private AbstractReadOnlyAttributeInfo getSelectedAttribute() {
		Object value = attributeSelect.getValue();
		if (value != null) {
			for (AbstractAttributeInfo attributeInfo : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
				if (attributeInfo instanceof AbstractReadOnlyAttributeInfo && value.equals(
						((AbstractReadOnlyAttributeInfo) attributeInfo).getLabel())) {
					return (AbstractReadOnlyAttributeInfo) attributeInfo;
				}
			}
		}
		return null;
	}

	private AbstractReadOnlyAttributeInfo getAttributeByName(String name) {
		if (name != null) {
			for (AbstractAttributeInfo attributeInfo : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
				if (attributeInfo instanceof AbstractReadOnlyAttributeInfo && attributeInfo.getName().equals(name)) {
					return (AbstractReadOnlyAttributeInfo) attributeInfo;
				}
			}
		}
		return null;
	}

	/**
	 * <p>
	 * Editable form item implementation that can edit any kind of feature attribute. It starts by using a default
	 * <code>TextItem</code> as <code>FormItem</code> representative. Every time the <code>setAttributeInfo</code>
	 * method is called, a new internal <code>FormItem</code> will be created and shown in the place of the
	 * <code>TextItem</code>. In order to create the correct representation for each kind of attribute, a
	 * {@link AttributeFormFieldRegistry} is used.
	 * </p>
	 *
	 * @author Pieter De Graef
	 */
	private class AttributeFormItem extends CanvasItem {

		private DynamicForm form;

		private FormItem formItem;

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
		 * {@link AttributeFormFieldRegistry} is used.
		 *
		 * @param attributeInfo The new attribute definition for which to display the correct <code>FormItem</code>.
		 */
		public void setAttributeInfo(AbstractReadOnlyAttributeInfo attributeInfo) {
			formItem = AttributeFormFieldRegistry.createFormItem(attributeInfo, layer);
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