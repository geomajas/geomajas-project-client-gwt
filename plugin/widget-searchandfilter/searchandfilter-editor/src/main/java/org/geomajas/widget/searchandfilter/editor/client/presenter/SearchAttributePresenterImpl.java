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
import org.geomajas.widget.searchandfilter.configuration.client.SearchAttribute;
import org.geomajas.widget.searchandfilter.configuration.client.SearchConfig;
import org.geomajas.widget.searchandfilter.editor.client.event.VectorLayerInfoChangedEvent;

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

	private SearchAttribute searchAttribute;

	private SearchConfig parentSearch;

	private boolean newSearchAttribute;

	private Map<PrimitiveAttributeInfo, SearchAttribute.AttributeType> attributeTypeMap;

	//private Map<SearchAttribute.AttributeType, LinkedHashMap<SearchAttribute.Operation, String>> defaultOperationValueMaps;
	//private Map<SearchAttribute.AttributeType, LinkedHashMap<SearchAttribute.InputType, String>> defaultInputTypeValueMaps;

	public SearchAttributePresenterImpl() {
		this.view = SearchAndFilterEditor.getViewManager().getSearchAttributeView();
		view.setHandler(this);
//		loadSearchAttributeMaps();
//		view.setDefaultOperationValueMaps(defaultOperationValueMaps);
//		view.setDefaultInputTypeValueMaps(defaultInputTypeValueMaps);
		updateAttributeTypeMap();
		bind();
	}

	private void bind() {
		SearchAndFilterEditor.addVectorLayerInfoChangedHandler(this);
	}

	private SearchAttribute getSelectedSearchAttribute() {
		return SearchAndFilterEditor.getSearchesStatus().getSelectedSearchAttribute();
	}

	private SearchConfig getSelectedSearchConfig() {
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
			this.searchAttribute = new SearchAttribute();
		} else {
			this.searchAttribute = getSelectedSearchAttribute();
		}
		this.parentSearch = getSelectedSearchConfig();
		updateView();
		getView().show();
	}

	@Override
	public void onSave() {
		if (view.validate()) {
			updateSelectedSearchAttributed();
			SearchAndFilterEditor.getSearchesStatus().saveSearchAttribute(searchAttribute, parentSearch, newSearchAttribute);
			emptySelectedAttribute();
			view.hide();
		}
	}

	private void emptySelectedAttribute() {
		searchAttribute = null;
		// empty view
		view.clearValues();
	}

	private void updateSelectedSearchAttributed() {
		if (searchAttribute != null) {
			searchAttribute.setOperation(view.getSelectedOperation());
			searchAttribute.setInputType(view.getSelectedInputType());
			searchAttribute.setLabel(view.getLabel());
			// attribute type and attribute name are inserted upon selection of that value.
		}
	}

	@Override
	public void onSelectAttributeName(String attributeName) {
		clearSearchAttribute();
		view.clearValues();
		PrimitiveAttributeInfo attributeInfo = getPrimitiveAttributeFromName(attributeName);
		if (attributeInfo != null) {
			searchAttribute.setAttributeName(attributeName);
			SearchAttribute.AttributeType selectedAttributeType = attributeTypeMap.get(attributeInfo);
			searchAttribute.setAttributeType(selectedAttributeType);
			updateView();
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
	public void setSearchAttribute(SearchAttribute searchAttribute) {
		this.searchAttribute = searchAttribute;
		updateView();
	}

	@Override
	public void updateView() {
		view.clearValues();
		if (searchAttribute != null && searchAttribute.getAttributeType() != null) {
			view.setSelectedAttributeName(searchAttribute.getAttributeName());
			changeViewAccordingToAttributeType(searchAttribute.getAttributeType());
			view.setLabelText(searchAttribute.getLabel());
			view.setSelectedOperation(searchAttribute.getOperation());
			view.setSelectedInputType(searchAttribute.getInputType());
		}
	}

	private void clearSearchAttribute() {
		if (searchAttribute != null) {
			searchAttribute.setAttributeName(null);
			searchAttribute.setAttributeType(null);
			searchAttribute.setLabel(null);
			searchAttribute.setOperation(null);
			searchAttribute.setInputType(null);
			searchAttribute.setInputTypeValues(null);
		}
	}

	private void changeViewAccordingToAttributeType(SearchAttribute.AttributeType selectedAttributeType) {
		boolean hasAttributeType =  selectedAttributeType != null;
		// enable or disable the other fields
		view.setFieldsEnabled(hasAttributeType);
		// fill choice lists of drop downs
		if (hasAttributeType) {
			LinkedHashMap<SearchAttribute.Operation, String> operationMap =
					SearchAndFilterEditor.getSearchAttributeService().getOperationsValueMap(selectedAttributeType);
			view.setOperationMap(operationMap);
			view.setSelectedOperation(operationMap.keySet().iterator().next());
			LinkedHashMap<SearchAttribute.InputType, String> inputTypeMap =
					SearchAndFilterEditor.getSearchAttributeService().getInputTypeMap(selectedAttributeType);
			view.setInputTypeMap(inputTypeMap);
			view.setSelectedInputType(inputTypeMap.keySet().iterator().next());
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

	/*private void loadSearchAttributeMaps() {
		defaultOperationValueMaps = new LinkedHashMap<SearchAttribute.AttributeType,
				LinkedHashMap<SearchAttribute.Operation, String>>();
		defaultInputTypeValueMaps = new LinkedHashMap<SearchAttribute.AttributeType,
				LinkedHashMap<SearchAttribute.InputType, String>>();
		for (SearchAttribute.AttributeType attributeType : SearchAttribute.AttributeType.values()) {
			defaultOperationValueMaps.put(attributeType, getOperationsValueMap(attributeType));
			defaultInputTypeValueMaps.put(attributeType, getInputTypeMap(attributeType));
		}
	}*/
}
