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
package org.geomajas.widget.searchandfilter.client.widget.configuredsearch;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.AbstractReadOnlyAttributeInfo;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.widget.searchandfilter.client.util.AttributeCriterionUtil;
import org.geomajas.widget.searchandfilter.client.widget.search.AbstractAttributeCriterionPane;
import org.geomajas.widget.searchandfilter.search.dto.AttributeCriterion;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchAttribute;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Adjusted from see {@link org.geomajas.widget.searchandfilter.client.widget.configuredsearch
 * .ConfiguredSearchAttributeCriterionPane} to work with AttributeCriterion.
 *
 * @author Pieter De Graef
 * @author Kristof Heirwegh
 * @author Jan Venstermans
 */
public class ConfiguredSearchAttributeCriterionPane extends AbstractAttributeCriterionPane {

	private ConfiguredSearchAttribute configuredSearchAttribute;

	/**
	 * Create a search criterion pane, for the given vector layer. The layer is required, as it's list of attribute
	 * definitions are a vital part of the search criteria.
	 *
	 * @param layer layer to create criterion for
	 */
	public ConfiguredSearchAttributeCriterionPane(VectorLayer layer) {
		super(layer);
	}

	@Override
	public void setSearchCriterion(AttributeCriterion ac) {
		if (ac != null && ac instanceof ConfiguredSearchAttribute) {
			configuredSearchAttribute = (ConfiguredSearchAttribute) ac;
			attributeSelect.setValue(configuredSearchAttribute.getDisplayText());
			operatorSelect.setValue(AttributeCriterionUtil.getOperationStringRepresentation(
					configuredSearchAttribute.getAttributeType(), configuredSearchAttribute.getOperation()));
			((ConfiguredSearchAttributeFormItem) valueItem).setConfiguredSearchAttributeInfo(configuredSearchAttribute);
		}
	}

	@Override
	public String getOperatorString() {
		if (configuredSearchAttribute != null) {
			return configuredSearchAttribute.getOperation().getOperatorString();
		}
		return null;
	}

	@Override
	protected FormItem createAttributeFormItem() {
		attributeSelect = createStaticTextItem("attributeItem");
		return attributeSelect;
	}

	@Override
	protected FormItem createOperatorFormItem() {
		operatorSelect = createStaticTextItem("operatorItem");
		return operatorSelect;
	}

	@Override
	protected AttributeFormItem createValueAttributeFormItem() {
		ConfiguredSearchAttributeFormItem configuredSearchAttributeFormItem =
				new ConfiguredSearchAttributeFormItem("valueItem");
		configuredSearchAttributeFormItem.setShowTitle(false);
		configuredSearchAttributeFormItem.setWidth(150);
		return configuredSearchAttributeFormItem;
	}

	private AbstractReadOnlyAttributeInfo getAttributeInfo(ConfiguredSearchAttribute configuredSearchAttribute) {
		Object value = configuredSearchAttribute.getAttributeName();
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

	/**
	 * Extension of {@link AttributeFormItem} for displaying a custom form item, depdending on
	 * {@link org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchAttribute.InputType}.
	 */
	protected class ConfiguredSearchAttributeFormItem extends AttributeFormItem {

		/**
		 * Create the form item with the given name. An internal form will already be created, and in that form a
		 * <code>TextItem</code> will be shown.
		 *
		 * @param name form item name
		 */
		public ConfiguredSearchAttributeFormItem(String name) {
			super(name);
		}


		public void setConfiguredSearchAttributeInfo(ConfiguredSearchAttribute configuredSearchAttribute) {
			selectedAttribute = getAttributeInfo(configuredSearchAttribute);
			switch (configuredSearchAttribute.getInputType()) {
				case DropDown:
					List<String> values = configuredSearchAttribute.getInputTypeDropDownValues();
					SelectItem selectItem = new SelectItem();
					selectItem.setValueMap(convertToMap(values));
					formItem = selectItem;
					updateFormStructure();
					break;
				case FreeNr:
				case FreeText:
					super.setAttributeInfo(selectedAttribute);
					break;
			}
		}

		private LinkedHashMap<String, String> convertToMap(List<String> values) {
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			for (String value : values) {
				map.put(value, value);
			}
			return map;
		}
	}
}