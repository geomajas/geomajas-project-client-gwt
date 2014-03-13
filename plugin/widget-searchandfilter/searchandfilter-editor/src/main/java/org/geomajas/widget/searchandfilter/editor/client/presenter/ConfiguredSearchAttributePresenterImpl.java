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
 * Default implementation of {@link ConfiguredSearchAttributePresenter}.
 *
 * @author Jan Venstermans
 */
public class ConfiguredSearchAttributePresenterImpl implements ConfiguredSearchAttributePresenter,
		ConfiguredSearchAttributePresenter.Handler, VectorLayerInfoChangedEvent.Handler {

	private View view;

	private ConfiguredSearchAttribute searchAttribute;

	private ConfiguredSearch parentSearch;

	private boolean newSearchAttribute;

	private Map<PrimitiveAttributeInfo, ConfiguredSearchAttribute.AttributeType> attributeTypeMap;

	/* model elements */
	private LinkedHashMap<String, String> attributeNameMap;
	private LinkedHashMap<ConfiguredSearchAttribute.Operation, String> operationMap;
	private LinkedHashMap<ConfiguredSearchAttribute.InputType, String> inputTypeMap;

	public ConfiguredSearchAttributePresenterImpl() {
		this.view = SearchAndFilterEditor.getViewManager().getSearchAttributeView();
		view.setHandler(this);
		updateAttributeTypeMap();
		bind();
	}

	private void bind() {
		SearchAndFilterEditor.addVectorLayerInfoChangedHandler(this);
	}

	private ConfiguredSearchAttribute getSelectedSearchAttribute() {
		return SearchAndFilterEditor.getConfiguredSearchesStatus().getSelectedSearchAttribute();
	}

	private ConfiguredSearch getSelectedSearchConfig() {
		return SearchAndFilterEditor.getConfiguredSearchesStatus().getSelectedSearchConfig();
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
		updateOperationAndInputMaps();
		this.parentSearch = getSelectedSearchConfig();
		updateView();
		getView().show();
	}

	@Override
	public void onSave() {
		if (view.validateForm()) {
			updateSelectedSearchAttribute();
			SearchAndFilterEditor.getConfiguredSearchesStatus().
					saveSearchAttribute(searchAttribute, parentSearch, newSearchAttribute);
			emptySelectedAttribute();
			view.hide();
		}
	}

	private void emptySelectedAttribute() {
		searchAttribute = null;
		view.clearFormValues();
		view.setInputTypeDropDown(false);
		view.clearGrid();
	}

	private void updateSelectedSearchAttribute() {
		if (searchAttribute != null) {
			searchAttribute.setDisplayText(view.getLabel());
			// attribute type and attribute name are inserted upon selection of that value.
			searchAttribute.setOperation(view.getSelectedOperation());
			searchAttribute.setInputType(view.getSelectedInputType());
			if (searchAttribute.getInputType().equals(ConfiguredSearchAttribute.InputType.DropDown)) {
				searchAttribute.setInputTypeDropDownValues(view.getDropDownValues());
			} else {
				searchAttribute.getInputTypeDropDownValues().clear();
			}
		}
	}

	@Override
	public void onSelectAttributeName(String attributeName) {
		clearSearchAttribute();
		searchAttribute.setDisplayText(view.getLabel()); // keep the label upon change of attribute name
		PrimitiveAttributeInfo attributeInfo = getPrimitiveAttributeFromName(attributeName);
		if (attributeInfo != null) {
			searchAttribute.setAttributeName(attributeName);
			ConfiguredSearchAttribute.AttributeType selectedAttributeType = attributeTypeMap.get(attributeInfo);
			searchAttribute.setAttributeType(selectedAttributeType);
			updateOperationAndInputMaps();
			searchAttribute.setOperation(operationMap.keySet().iterator().next());
			searchAttribute.setInputType(inputTypeMap.keySet().iterator().next());
			searchAttribute.getInputTypeDropDownValues().clear();
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

	@Override
	public void onRemove(String dropdownValue) {
		try {
		   	searchAttribute.getInputTypeDropDownValues().remove(dropdownValue);
			updateView();
		} catch (Exception e) {
			// do nothing
		}
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
		view.setInputTypeDropDown(false);
		view.clearGrid();
		if (searchAttribute != null && searchAttribute.getAttributeType() != null) {
			view.setLabelText(searchAttribute.getDisplayText());
			view.setSelectedAttributeName(searchAttribute.getAttributeName());
			changeViewAccordingToAttributeType(searchAttribute.getAttributeType());
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

	private void updateOperationAndInputMaps() {
		if (searchAttribute.getAttributeType() != null) {
			operationMap = SearchAndFilterEditor.getSearchAttributeService().
					getOperationsValueMap(searchAttribute.getAttributeType());
			inputTypeMap = SearchAndFilterEditor.getSearchAttributeService().
					getInputTypeMap(searchAttribute.getAttributeType());
		}
	}

	/**
	 * Changes to the view as a result of a certain attribute type.
	 * In this case: set editability of certain fields + set the map values for operation and input type.
	 * @param selectedAttributeType
	 */
	private void changeViewAccordingToAttributeType(ConfiguredSearchAttribute.AttributeType selectedAttributeType) {
		boolean hasAttributeType =  selectedAttributeType != null;
		// enable or disable the other fields
		view.setFieldsEnabled(hasAttributeType);
		// fill choice lists of drop downs
		view.setOperationMap(hasAttributeType ? operationMap : null);
		view.setInputTypeMap(hasAttributeType ? inputTypeMap : null);
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
