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
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchAttribute;
import org.geomajas.widget.searchandfilter.editor.client.i18n.SearchAndFilterEditorMessages;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton implementation of
 * {@link org.geomajas.widget.searchandfilter.editor.client.SearchesStatus}.
 *
 * @author Jan Venstermans
 */
public final class SearchAttributeServiceImpl implements SearchAttributeService {

	private final SearchAndFilterEditorMessages messages =
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
	public String getOperationStringRepresentation(ConfiguredSearchAttribute.AttributeType attributeType,
												   ConfiguredSearchAttribute.Operation operation) {
		if (attributeType != null && operation != null) {
			switch (attributeType) {
			case Integer:
				switch (operation) {
					case EqualTo:
						return messages.searchAttributeOperationEqualToInteger();
					case LargerThan:
						return messages.searchAttributeOperationLargerThanInteger();
					case SmallerThan:
						return messages.searchAttributeOperationSmallerThanInteger();
				}
				break;
			case String:
				switch (operation) {
					case EqualTo:
						return messages.searchAttributeOperationEqualToString();
					case LargerThan:
						return messages.searchAttributeOperationLargerThanString();
					case SmallerThan:
						return messages.searchAttributeOperationSmallerThanString();
				}
				break;
			}
		}
		return null;
	}

	@Override
	public String getInputTypeStringRepresentation(ConfiguredSearchAttribute.AttributeType attributeType,
												   ConfiguredSearchAttribute.InputType inputType) {
		if (inputType != null) {
			switch (inputType) {
				case DropDown:
					return messages.searchAttributeInputTypeDropDownToString();
				case FreeNr:
					return messages.searchAttributeInputTypeFreeNrToString();
				case FreeText:
					return messages.searchAttributeInputTypeFreeTextToString();
			}
		}
		return null;
	}

	@Override
	public Map<org.geomajas.configuration.PrimitiveAttributeInfo, ConfiguredSearchAttribute.AttributeType>
	getPrimitiveAttributesMap() {
		Map<org.geomajas.configuration.PrimitiveAttributeInfo, ConfiguredSearchAttribute.AttributeType>
				attributesMap =
				new HashMap<org.geomajas.configuration.PrimitiveAttributeInfo, ConfiguredSearchAttribute.AttributeType>();
		ClientVectorLayerInfo clientVectorLayerInfo =
				SearchAndFilterEditor.getSearchesStatus().getClientVectorLayerInfo();
		if (clientVectorLayerInfo != null) {
			List<org.geomajas.configuration.AttributeInfo> attributeInfoList =
					clientVectorLayerInfo.getFeatureInfo().getAttributes();
			for (org.geomajas.configuration.AttributeInfo attr : attributeInfoList) {
				if (attr instanceof PrimitiveAttributeInfo) {
					PrimitiveAttributeInfo primAttr = (PrimitiveAttributeInfo) attr;
					ConfiguredSearchAttribute.AttributeType attributeType = null;
					switch (primAttr.getType()) {
						case STRING:
							attributeType = ConfiguredSearchAttribute.AttributeType.String;
							break;
						case INTEGER:
							attributeType = ConfiguredSearchAttribute.AttributeType.Integer;
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
	public LinkedHashMap<ConfiguredSearchAttribute.Operation, String> getOperationsValueMap(
			ConfiguredSearchAttribute.AttributeType attributeType) {
		LinkedHashMap<ConfiguredSearchAttribute.Operation, String> result
				= new LinkedHashMap<ConfiguredSearchAttribute.Operation, String>();
		for (ConfiguredSearchAttribute.Operation operation : attributeType.getOperations()) {
			result.put(operation, SearchAndFilterEditor.getSearchAttributeService().
					getOperationStringRepresentation(attributeType, operation));
		}
		return result;
	}

	@Override
	public LinkedHashMap<ConfiguredSearchAttribute.InputType, String> getInputTypeMap(
			ConfiguredSearchAttribute.AttributeType attributeType) {
		LinkedHashMap<ConfiguredSearchAttribute.InputType, String> result
				= new LinkedHashMap<ConfiguredSearchAttribute.InputType, String>();
		for (ConfiguredSearchAttribute.InputType type : attributeType.getInputTypes()) {
			result.put(type, SearchAndFilterEditor.getSearchAttributeService().
					getInputTypeStringRepresentation(attributeType, type));
		}
		return result;
	}
}
