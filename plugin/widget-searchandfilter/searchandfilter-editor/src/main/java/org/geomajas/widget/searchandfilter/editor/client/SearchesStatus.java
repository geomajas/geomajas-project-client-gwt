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

import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearch;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchAttribute;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchesInfo;

/**
 * Interface for managing the status of the current
 * {@link org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchesInfo}
 * and {@link ClientVectorLayerInfo}.
 *
 * @author Jan Venstermans
 */
public interface SearchesStatus {

	void setSearchesInfo(ConfiguredSearchesInfo searchesInfo);

	ConfiguredSearchesInfo getSearchesInfo();

	void setSelectedSearchConfig(ConfiguredSearch selectedSearchConfig);

	ConfiguredSearch getSelectedSearchConfig();

	void setSelectedSearchAttribute(ConfiguredSearchAttribute selectedSearchAttribute);

	ConfiguredSearchAttribute getSelectedSearchAttribute();

	boolean isDisabled();

	void setDisabled(boolean disabled);

	ClientVectorLayerInfo getClientVectorLayerInfo();

	void setClientVectorLayerInfo(ClientVectorLayerInfo layerModel);

	/* save */

	void saveSearch(ConfiguredSearch searchConfig, boolean newSearch);

	void saveSearchAttribute(ConfiguredSearchAttribute searchAttribute,
							 ConfiguredSearch searchConfig, boolean newAttribute);

	/* remove */

	void removeSearch(ConfiguredSearch searchConfig);

	void removeSearchAttribute(ConfiguredSearchAttribute searchAttribute, ConfiguredSearch searchConfig);
}
