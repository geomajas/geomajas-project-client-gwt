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
import org.geomajas.widget.searchandfilter.configuration.client.SearchAttribute;
import org.geomajas.widget.searchandfilter.configuration.client.SearchConfig;
import org.geomajas.widget.searchandfilter.configuration.client.SearchesInfo;

/**
 * Interface for managing the status of the current
 * {@link org.geomajas.widget.searchandfilter.configuration.client.SearchesInfo}
 * and {@link ClientVectorLayerInfo}.
 *
 * @author Jan Venstermans
 */
public interface SearchesStatus {

	void setSearchesInfo(SearchesInfo searchesInfo);

	SearchesInfo getSearchesInfo();

	void setSelectedSearchConfig(SearchConfig selectedSearchConfig);

	SearchConfig getSelectedSearchConfig();

	void setSelectedSearchAttribute(SearchAttribute selectedSearchAttribute);

	SearchAttribute getSelectedSearchAttribute();

	boolean isDisabled();

	void setDisabled(boolean disabled);

	ClientVectorLayerInfo getClientVectorLayerInfo();

	void setClientVectorLayerInfo(ClientVectorLayerInfo layerModel);

	/* save */

	void saveSearch(SearchConfig searchConfig, boolean newSearch);

	void saveSearchAttribute(SearchAttribute searchAttribute, SearchConfig searchConfig, boolean newAttribute);

	/* remove */

	void removeSearch(SearchConfig searchConfig);

	void removeSearchAttribute(SearchAttribute searchAttribute, SearchConfig searchConfig);
}
