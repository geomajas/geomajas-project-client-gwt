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
package org.geomajas.widget.searchandfilter.client.widget.attributesearch;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.AbstractReadOnlyAttributeInfo;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.widget.searchandfilter.client.util.AttributeCriterionUtil;
import org.geomajas.widget.searchandfilter.client.widget.search.AbstractAttributeCriterionPane;
import org.geomajas.widget.searchandfilter.search.dto.AttributeCriterion;

/**
 * Extension of {@link AbstractAttributeCriterionPane} for {@link AttributeCriterion} that can be altered.
 * The attribute and operation can be picked from drop down items.
 *
 * @author Pieter De Graef
 * @author Kristof Heirwegh
 */
public class AttributeCriterionPane extends AbstractAttributeCriterionPane {


	/**
	 * Create a search criterion pane, for the given vector layer. The layer is required, as it's list of attribute
	 * definitions are a vital part of the search criteria.
	 *
	 * @param layer layer to create criterion for
	 */
	public AttributeCriterionPane(VectorLayer layer) {
		super(layer);
	}

	@Override
	protected FormItem createAttributeFormItem() {
		SelectItem attributeSelectItem = createSelectItem("attributeItem");
		attributeSelectItem.setValueMap(org.geomajas.gwt.client.widget.attribute.AttributeCriterionPane.
				getSearchableAttributes(layer));
		attributeSelectItem.setHint(I18nProvider.getSearch().gridChooseAttribute());
		attributeSelectItem.setShowHintInField(true);

		// Mechanisms:
		attributeSelectItem.addChangedHandler(new ChangedHandler() {
			public void onChanged(ChangedEvent event) {
				attributeChanged();
			}
		});

		return attributeSelectItem;
	}

	@Override
	protected FormItem createOperatorFormItem() {
		SelectItem operatorSelectItem = createSelectItem("operatorItem");
		operatorSelectItem.setDisabled(true);
		return operatorSelectItem;
	}

	public void setSearchCriterion(AttributeCriterion ac) {
		if (ac != null) {
			attributeSelect.setValue(getAttributeByName(ac.getAttributeName()).getLabel());
			attributeChanged();
			operatorSelect.setValue(AttributeCriterionUtil.getLabelFromOperatorCode(ac.getOperator()));
			valueItem.setValue(trimLikeValue(ac.getValue(), ac.getOperator()));
		}
	}

	@Override
	public String getOperatorString() {
		Object operator = operatorSelect.getValue();
		if (operator != null) {
			return org.geomajas.gwt.client.widget.attribute.AttributeCriterionPane.
					getOperatorCodeFromLabel(operator.toString());
		}
		return null;
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

	protected void attributeChanged() {
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

	protected AbstractReadOnlyAttributeInfo getSelectedAttribute() {
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

	protected AbstractReadOnlyAttributeInfo getAttributeByName(String name) {
		if (name != null) {
			for (AbstractAttributeInfo attributeInfo : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
				if (attributeInfo instanceof AbstractReadOnlyAttributeInfo && attributeInfo.getName().equals(name)) {
					return (AbstractReadOnlyAttributeInfo) attributeInfo;
				}
			}
		}
		return null;
	}

}