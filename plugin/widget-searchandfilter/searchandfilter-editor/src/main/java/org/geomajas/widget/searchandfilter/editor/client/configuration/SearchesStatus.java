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
package org.geomajas.widget.searchandfilter.editor.client.configuration;

import org.geomajas.plugin.deskmanager.domain.dto.LayerDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;

/**
 * Interface for the presenter of {@link SearchesInfo}.
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

	LayerDto getLayerDto();

	void setLayerDto(LayerDto layerModel);

	/* save */

	void saveSearch(SearchConfig searchConfig);

	void saveSearchAttribute(SearchAttribute searchAttribute, SearchConfig searchConfig);

	/* remove */

	void removeSearch(SearchConfig searchConfig);

	void removeSearchAttribute(SearchAttribute searchAttribute, SearchConfig searchConfig);
}
