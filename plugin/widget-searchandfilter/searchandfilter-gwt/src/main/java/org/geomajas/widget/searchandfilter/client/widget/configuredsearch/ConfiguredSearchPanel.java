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
package org.geomajas.widget.searchandfilter.client.widget.configuredsearch;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.util.AttributeCriterionUtil;
import org.geomajas.widget.searchandfilter.client.widget.search.AbstractSearchPanel;
import org.geomajas.widget.searchandfilter.search.dto.AndCriterion;
import org.geomajas.widget.searchandfilter.search.dto.AttributeCriterion;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearch;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchAttribute;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Extension of {@link AbstractSearchPanel} for {@link ConfiguredSearch}.
 * 
 * @author Jan Venstermans
 */
public class ConfiguredSearchPanel extends AbstractSearchPanel {

	private List<SearchAttributeFormItem> attributeFormItems = new ArrayList<SearchAttributeFormItem>();
	private DynamicForm dynamicForm;
	private ConfiguredSearch searchConfig;

	private VectorLayer vectorLayer;

	public ConfiguredSearchPanel(MapWidget mapWidget) {
		super(mapWidget);
	}

	@Override
	public boolean validate() {
		return dynamicForm.validate();
	}

	@Override
	public Criterion getFeatureSearchCriterion() {
		return convertAttributesToAndCriterion();
	}

	@Override
	public VectorLayer getFeatureSearchVectorLayer() {
		return vectorLayer;
	}

	@Override
	public void reset() {

	}

	@Override
	public void initialize(Criterion featureSearch) {

	}

	public void setSearchConfig(final ConfiguredSearch searchConfig, final String clientLayerId) {
		this.searchConfig = searchConfig;
		setLayerId(clientLayerId);
		buildUi();
	}

	public void setLayerId(final String clientLayerId) {
		this.vectorLayer = getMapWidget().getMapModel().getVectorLayer(clientLayerId);
	}

	private void buildUi() {
		clear();
		dynamicForm = new DynamicForm();
		dynamicForm.setWidth100();
		dynamicForm.setColWidths(130, 200);
		attributeFormItems.clear();

		List<FormItem> formItemsList = new ArrayList<FormItem>();
		for (ConfiguredSearchAttribute attribute : searchConfig.getAttributes()) {
			SearchAttributeFormItem attributeFormItem = new SearchAttributeFormItem(getFormItem(attribute), attribute);
			attributeFormItems.add(attributeFormItem);
			formItemsList.add(attributeFormItem.getFormItem());
		}
		dynamicForm.setFields(formItemsList.toArray(new FormItem[formItemsList.size()]));
		setAlign(Alignment.CENTER);
		setWidth(430);
		setHeight(100);
		addChild(dynamicForm);
	}

	private FormItem getFormItem(ConfiguredSearchAttribute attribute) {
		FormItem formItem = null;
		switch (attribute.getInputType()) {
			case FreeValue:
				switch (attribute.getAttributeType()) {
					case String:
						formItem = new TextItem();
						break;
					case Integer:
						formItem = new SpinnerItem();
						break;
				}
				break;
			case DropDown:
				formItem = new SelectItem();
				formItem.setValueMap(convertToMap(attribute.getInputTypeDropDownValues()));
				break;
		}
		if (formItem != null) {
			formItem.setTitle(attribute.getDisplayText());
		}
		return formItem;
	}


	private LinkedHashMap<String, String> convertToMap(List<String> values) {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		for (String value : values) {
			map.put(value, value);
		}
		return map;
	}

	private AndCriterion convertAttributesToAndCriterion() {
		AndCriterion criterion = new AndCriterion();
		for (SearchAttributeFormItem attributeFormItem : attributeFormItems) {
			criterion.getCriteria().add(attributeFormItem.getSearchCriterion());
		}
		return criterion;
	}

	/**
	 * Inner class for displaying a {@link ConfiguredSearchAttribute} as a {@link FormItem}.
	 * It also provides a getter for the {@link AttributeCriterion} to use in a search.
	 */
	private class SearchAttributeFormItem {

		private FormItem formItem;

		private ConfiguredSearchAttribute searchAttribute;

		public SearchAttributeFormItem(FormItem formItem, ConfiguredSearchAttribute searchAttribute) {
			this.formItem = formItem;
			this.searchAttribute = searchAttribute;
		}

		public FormItem getFormItem() {
			return formItem;
		}

		public void setFormItem(FormItem formItem) {
			this.formItem = formItem;
		}

		public ConfiguredSearchAttribute getSearchAttribute() {
			return searchAttribute;
		}

		public void setSearchAttribute(ConfiguredSearchAttribute searchAttribute) {
			this.searchAttribute = searchAttribute;
		}

		public String getOperatorString() {
			if (searchAttribute != null) {
				return searchAttribute.getOperation().getOperatorCode();
			}
			return null;
		}

		/**
		 * Return the actual search criterion object, or null if not all fields have been properly filled.
		 *
		 * @return search criterion
		 */
		public AttributeCriterion getSearchCriterion() {
			return AttributeCriterionUtil.getSearchCriterion(vectorLayer.getServerLayerId(),
					AttributeCriterionUtil.getAbstractReadOnlyAttributeInfo(vectorLayer, searchAttribute),
					formItem, getOperatorString());
		}
	}
}
