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
import org.geomajas.widget.searchandfilter.editor.client.SearchAndFilterEditor;
import org.geomajas.widget.searchandfilter.editor.client.event.SearchesInfoChangedEvent;

/**
 * Singleton implementation of {@link SearchesStatus}.
 *
 * @author Jan Venstermans
 */
public class SearchesStatusImpl implements SearchesStatus {

	private static SearchesStatus instance;

	private boolean disabled;

	private LayerDto layerDto;

	private SearchesInfo searchesInfo;

	private SearchConfig selectedSearchConfig;

	private SearchAttribute selectedSearchAttribute;

	private SearchesStatusImpl() {

	}

	public static SearchesStatus getInstance() {
		if (instance == null) {
			instance = new SearchesStatusImpl();
		}
		return instance;
	}

	@Override
	public void setSearchesInfo(SearchesInfo searchesInfo) {
		this.searchesInfo = searchesInfo;
		this.selectedSearchConfig = null;
		this.selectedSearchAttribute = null;
		SearchAndFilterEditor.fireSearchesInfoChangedEvent(new SearchesInfoChangedEvent());
	}

	@Override
	public SearchesInfo getSearchesInfo() {
		return searchesInfo;
	}

	@Override
	public void setSelectedSearchConfig(SearchConfig selectedSearchConfig) {
		if (isSearchesInfoContainsSearchConfig(selectedSearchConfig)) {
			this.selectedSearchConfig = selectedSearchConfig;
			this.selectedSearchAttribute = null;
			SearchAndFilterEditor.fireSearchesInfoChangedEvent(new SearchesInfoChangedEvent());
		}
	}

	@Override
	public SearchConfig getSelectedSearchConfig() {
		return selectedSearchConfig;
	}

	@Override
	public void setSelectedSearchAttribute(SearchAttribute selectedSearchAttribute) {
		if (isSelectedSearchConfigContainsSearchAttribute(selectedSearchAttribute)) {
			this.selectedSearchAttribute = selectedSearchAttribute;
			SearchAndFilterEditor.fireSearchesInfoChangedEvent(new SearchesInfoChangedEvent());
		}
	}

	@Override
	public SearchAttribute getSelectedSearchAttribute() {
		return selectedSearchAttribute;
	}

	@Override
	public boolean isDisabled() {
		return disabled;
	}

	@Override
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	@Override
	public LayerDto getLayerDto() {
		return layerDto;
	}

	@Override
	public void setLayerDto(LayerDto layerDto) {
		this.layerDto = layerDto;
	}

	@Override
	public void saveSearch(SearchConfig searchConfig) {
		if (isSearchesInfoContainsSearchConfig(searchConfig)) {
			for (SearchConfig config : searchesInfo.getSearchConfigs()) {
				if (searchConfig.getTitle().equals(config.getTitle())) {
					config = searchConfig;
					SearchAndFilterEditor.fireSearchesInfoChangedEvent(new SearchesInfoChangedEvent());
				}
			}
		} else {
			searchesInfo.getSearchConfigs().add(searchConfig);
			SearchAndFilterEditor.fireSearchesInfoChangedEvent(new SearchesInfoChangedEvent());
		}

	}

	@Override
	public void saveSearchAttribute(SearchAttribute searchAttribute, SearchConfig searchConfig) {
		if (isSearchesInfoContainsSearchConfig(searchConfig)) {
			searchConfig.getAttributes().add(searchAttribute);
			SearchAndFilterEditor.fireSearchesInfoChangedEvent(new SearchesInfoChangedEvent());
		}
	}

	@Override
	public void removeSearch(SearchConfig searchConfig) {
		if (isSearchesInfoContainsSearchConfig(searchConfig)) {
			searchesInfo.getSearchConfigs().remove(searchConfig);
			SearchAndFilterEditor.fireSearchesInfoChangedEvent(new SearchesInfoChangedEvent());
		}
	}

	@Override
	public void removeSearchAttribute(SearchAttribute searchAttribute, SearchConfig searchConfig) {
		if (isSearchesInfoContainsSearchConfig(searchConfig)
				&& searchConfig.getAttributes().contains(searchAttribute)) {
			searchConfig.getAttributes().remove(searchAttribute);
			SearchAndFilterEditor.fireSearchesInfoChangedEvent(new SearchesInfoChangedEvent());
		}
	}

	/* private methods */

	private boolean isSearchesInfoContainsSearchConfig(SearchConfig searchConfig) {
		if (searchesInfo != null && searchesInfo.getSearchConfigs() != null) {
			for (SearchConfig config : searchesInfo.getSearchConfigs()) {
				if (searchConfig.getTitle().equals(config.getTitle())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isSelectedSearchConfigContainsSearchAttribute(SearchAttribute searchAttribute) {
		if (selectedSearchConfig != null && selectedSearchConfig.getAttributes() != null &&
				selectedSearchConfig.getAttributes().contains(searchAttribute)) {
			return true;
		}
		return false;
	}
}
