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
package org.geomajas.widget.searchandfilter.editor.client.presenter;

import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.widget.searchandfilter.editor.client.SearchAndFilterEditor;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearch;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchAttribute;
import org.geomajas.widget.searchandfilter.editor.client.event.VectorLayerInfoChangedEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Default implementation of {@link SearchAttributePresenter}.
 *
 * @author Jan Venstermans
 */
public class SearchAttributePresenterImpl implements SearchAttributePresenter,
		SearchAttributePresenter.Handler, VectorLayerInfoChangedEvent.Handler {

	private View view;

	private ConfiguredSearchAttribute searchAttribute;

	private ConfiguredSearch parentSearch;

	private boolean newSearchAttribute;

	private Map<PrimitiveAttributeInfo, ConfiguredSearchAttribute.AttributeType> attributeTypeMap;

	public SearchAttributePresenterImpl() {
		this.view = SearchAndFilterEditor.getViewManager().getSearchAttributeView();
		view.setHandler(this);
		updateAttributeTypeMap();
		bind();
	}

	private void bind() {
		SearchAndFilterEditor.addVectorLayerInfoChangedHandler(this);
	}

	private ConfiguredSearchAttribute getSelectedSearchAttribute() {
		return SearchAndFilterEditor.getSearchesStatus().getSelectedSearchAttribute();
	}

	private ConfiguredSearch getSelectedSearchConfig() {
		return SearchAndFilterEditor.getSearchesStatus().getSelectedSearchConfig();
	}

	@Override
	public View getView() {
		return view;
	}

	@Override
	public void editSelectedAttribute() {
		loadAndView(false);
	}

	@Override
	public void createSearchAttribute() {
		loadAndView(true);
	}

	private void loadAndView(boolean newAttribute) {
		emptySelectedAttribute();
		newSearchAttribute = newAttribute;
		if (newAttribute) {
			this.searchAttribute = new ConfiguredSearchAttribute();
		} else {
			this.searchAttribute = getSelectedSearchAttribute();
		}
		this.parentSearch = getSelectedSearchConfig();
		updateView();
		getView().show();
	}

	@Override
	public void onSave() {
		if (view.validateForm()) {
			updateSelectedSearchAttributed();
			SearchAndFilterEditor.getSearchesStatus().
					saveSearchAttribute(searchAttribute, parentSearch, newSearchAttribute);
			emptySelectedAttribute();
			view.hide();
		}
	}

	private void emptySelectedAttribute() {
		searchAttribute = null;
		// empty view
		view.clearFormValues();
	}

	private void updateSelectedSearchAttributed() {
		if (searchAttribute != null) {
			searchAttribute.setOperation(view.getSelectedOperation());
			searchAttribute.setInputType(view.getSelectedInputType());
			searchAttribute.setDisplayText(view.getLabel());
			if (searchAttribute.getInputType().equals(ConfiguredSearchAttribute.InputType.DropDown)) {
				searchAttribute.setInputTypeDropDownValues(view.getDropDownValues());
			} else {
				searchAttribute.getInputTypeDropDownValues().clear();
			}
			// attribute type and attribute name are inserted upon selection of that value.
		}
	}

	@Override
	public void onSelectAttributeName(String attributeName) {
		clearSearchAttribute();
		view.clearFormValues();
		PrimitiveAttributeInfo attributeInfo = getPrimitiveAttributeFromName(attributeName);
		if (attributeInfo != null) {
			searchAttribute.setAttributeName(attributeName);
			ConfiguredSearchAttribute.AttributeType selectedAttributeType = attributeTypeMap.get(attributeInfo);
			searchAttribute.setAttributeType(selectedAttributeType);
			updateView();
		}
	}

	@Override
	public void onChangeSelectInputType() {
		boolean dropdownType = ConfiguredSearchAttribute.InputType.DropDown.equals(view.getSelectedInputType());
		view.setInputTypeDropDown(dropdownType);
	}

	@Override
	public void onAddDropDownValue() {
		if (searchAttribute.getInputTypeDropDownValues() == null) {
			searchAttribute.setInputTypeDropDownValues(new ArrayList<String>());
		}
		searchAttribute.getInputTypeDropDownValues().add("");
		view.updateGrid(searchAttribute.getInputTypeDropDownValues());
	}

	@Override
	public void onChangeDropdownValues() {
		searchAttribute.setInputTypeDropDownValues(view.getDropDownValues());
	}

	private PrimitiveAttributeInfo getPrimitiveAttributeFromName(String name) {
		PrimitiveAttributeInfo result = null;
		if (name != null) {
			for (PrimitiveAttributeInfo info : attributeTypeMap.keySet()) {
				if (name.equals(info.getName())) {
					return info;
				}
			}
		}
		return result;
	}

	@Override
	public void onVectorLayerInfoChanged(VectorLayerInfoChangedEvent event) {
		updateAttributeTypeMap();
	}

	@Override
	public void setSearchAttribute(ConfiguredSearchAttribute searchAttribute) {
		this.searchAttribute = searchAttribute;
		updateView();
	}

	@Override
	public void updateView() {
		view.clearFormValues();
		if (searchAttribute != null && searchAttribute.getAttributeType() != null) {
			view.setSelectedAttributeName(searchAttribute.getAttributeName());
			changeViewAccordingToAttributeType(searchAttribute.getAttributeType());
			view.setLabelText(searchAttribute.getDisplayText());
			view.setSelectedOperation(searchAttribute.getOperation());
			view.setSelectedInputType(searchAttribute.getInputType());
			onChangeSelectInputType();
			view.updateGrid(searchAttribute.getInputTypeDropDownValues());
		}
	}

	private void clearSearchAttribute() {
		if (searchAttribute != null) {
			searchAttribute.setAttributeName(null);
			searchAttribute.setAttributeType(null);
			searchAttribute.setDisplayText(null);
			searchAttribute.setOperation(null);
			searchAttribute.setInputType(null);
			searchAttribute.getInputTypeDropDownValues().clear();
		}
	}

	private void changeViewAccordingToAttributeType(ConfiguredSearchAttribute.AttributeType selectedAttributeType) {
		boolean hasAttributeType =  selectedAttributeType != null;
		// enable or disable the other fields
		view.setFieldsEnabled(hasAttributeType);
		// fill choice lists of drop downs
		if (hasAttributeType) {
			LinkedHashMap<ConfiguredSearchAttribute.Operation, String> operationMap =
					SearchAndFilterEditor.getSearchAttributeService().getOperationsValueMap(selectedAttributeType);
			view.setOperationMap(operationMap);
			view.setSelectedOperation(operationMap.keySet().iterator().next());
			LinkedHashMap<ConfiguredSearchAttribute.InputType, String> inputTypeMap =
					SearchAndFilterEditor.getSearchAttributeService().getInputTypeMap(selectedAttributeType);
			view.setInputTypeMap(inputTypeMap);
			view.setSelectedInputType(inputTypeMap.keySet().iterator().next());
			onChangeSelectInputType();
		}
	}

	private void updateAttributeTypeMap() {
		this.attributeTypeMap = SearchAndFilterEditor.getSearchAttributeService().getPrimitiveAttributesMap();
		view.setAttributeNameMap(createAttributeNameMap());
	}

	private LinkedHashMap<String, String> createAttributeNameMap() {
		LinkedHashMap<String, String> attributeNameMap = new LinkedHashMap<String, String>();
		for (PrimitiveAttributeInfo attributeInfo : attributeTypeMap.keySet()) {
			attributeNameMap.put(attributeInfo.getName(), attributeInfo.getLabel());
		}
		return attributeNameMap;
	}

}
