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

import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchAttribute;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Interface for the presenter of {@link org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchesInfo}.
 *
 * @author Jan Venstermans
 */
public interface SearchAttributeService {

	Map<org.geomajas.configuration.PrimitiveAttributeInfo, ConfiguredSearchAttribute.AttributeType>
	getPrimitiveAttributesMap();

	LinkedHashMap<ConfiguredSearchAttribute.Operation, String> getOperationsValueMap(
			ConfiguredSearchAttribute.AttributeType attributeType);

	LinkedHashMap<ConfiguredSearchAttribute.InputType, String> getInputTypeMap(
			ConfiguredSearchAttribute.AttributeType attributeType);
}
