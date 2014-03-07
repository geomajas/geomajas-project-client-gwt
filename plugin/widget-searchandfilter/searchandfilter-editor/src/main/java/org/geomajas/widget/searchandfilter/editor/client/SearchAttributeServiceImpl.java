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
package org.geomajas.widget.searchandfilter.editor.client;

import com.google.gwt.core.client.GWT;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.widget.searchandfilter.configuration.client.SearchAttribute;
import org.geomajas.widget.searchandfilter.configuration.client.SearchConfig;
import org.geomajas.widget.searchandfilter.configuration.client.SearchesInfo;
import org.geomajas.widget.searchandfilter.editor.client.event.SearchesInfoChangedEvent;
import org.geomajas.widget.searchandfilter.editor.client.i18n.SearchAndFilterEditorMessages;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton implementation of {@link org.geomajas.widget.searchandfilter.editor.client.SearchesStatus}.
 *
 * @author Jan Venstermans
 */
public class SearchAttributeServiceImpl implements SearchAttributeService {

	private static final SearchAndFilterEditorMessages MESSAGES =
			GWT.create(SearchAndFilterEditorMessages.class);

	private static SearchAttributeService instance;

	private SearchAttributeServiceImpl() {

	}

	public static SearchAttributeService getInstance() {
		if (instance == null) {
			instance = new SearchAttributeServiceImpl();
		}
		return instance;
	}

	@Override
	public String getOperationStringRepresentation(SearchAttribute.AttributeType attributeType, SearchAttribute.Operation operation) {
		switch (attributeType) {
		case Integer:
			switch (operation) {
				case EqualTo:
					return MESSAGES.searchAttributeOperationEqualToInteger();
				case LargerThan:
					return MESSAGES.searchAttributeOperationLargerThanInteger();
				case SmallerThan:
					return MESSAGES.searchAttributeOperationSmallerThanInteger();
			}
			break;
		case String:
			switch (operation) {
				case EqualTo:
					return MESSAGES.searchAttributeOperationEqualToString();
				case LargerThan:
					return MESSAGES.searchAttributeOperationLargerThanString();
				case SmallerThan:
					return MESSAGES.searchAttributeOperationSmallerThanString();
			}
			break;
		}
		return null;
	}

	@Override
	public String getInputTypeStringRepresentation(SearchAttribute.AttributeType attributeType, SearchAttribute.InputType inputType) {
		switch (inputType) {
			case DropDown:
				return MESSAGES.searchAttributeInputTypeDropDownToString();
			case FreeNr:
				return MESSAGES.searchAttributeInputTypeFreeNrToString();
			case FreeText:
				return MESSAGES.searchAttributeInputTypeFreeTextToString();
		}
		return null;
	}

	@Override
	public Map<org.geomajas.configuration.PrimitiveAttributeInfo, SearchAttribute.AttributeType>
	getPrimitiveAttributesMap() {
		Map<org.geomajas.configuration.PrimitiveAttributeInfo, SearchAttribute.AttributeType> attributesMap =
				new HashMap<org.geomajas.configuration.PrimitiveAttributeInfo, SearchAttribute.AttributeType>();
		ClientVectorLayerInfo clientVectorLayerInfo =
				SearchAndFilterEditor.getSearchesStatus().getClientVectorLayerInfo();
		if (clientVectorLayerInfo != null) {
			List<org.geomajas.configuration.AttributeInfo> attributeInfoList =
					clientVectorLayerInfo.getFeatureInfo().getAttributes();
			for (org.geomajas.configuration.AttributeInfo attr : attributeInfoList) {
				if (attr instanceof PrimitiveAttributeInfo) {
					PrimitiveAttributeInfo primAttr = (PrimitiveAttributeInfo) attr;
					SearchAttribute.AttributeType attributeType = null;
					switch (primAttr.getType()){
						case STRING:
							attributeType = SearchAttribute.AttributeType.String;
							break;
						case INTEGER:
							attributeType = SearchAttribute.AttributeType.Integer;
							break;
					}
					if (attributeType != null) {
						attributesMap.put(primAttr, attributeType);
					}
				}
			}
		}
		return attributesMap;
	}

	@Override
	public LinkedHashMap<SearchAttribute.Operation, String> getOperationsValueMap(SearchAttribute.AttributeType attributeType) {
		LinkedHashMap<SearchAttribute.Operation, String> result = new LinkedHashMap<SearchAttribute.Operation, String>();
		for (SearchAttribute.Operation operation : attributeType.getOperations()) {
			result.put(operation, SearchAndFilterEditor.getSearchAttributeService().
					getOperationStringRepresentation(attributeType, operation));
		}
		return result;
	}

	@Override
	public LinkedHashMap<SearchAttribute.InputType, String> getInputTypeMap(SearchAttribute.AttributeType attributeType) {
		LinkedHashMap<SearchAttribute.InputType, String> result = new LinkedHashMap<SearchAttribute.InputType, String>();
		for (SearchAttribute.InputType type : attributeType.getInputTypes()) {
			result.put(type, SearchAndFilterEditor.getSearchAttributeService().
					getInputTypeStringRepresentation(attributeType, type));
		}
		return result;
	}
}
