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
import org.geomajas.widget.searchandfilter.editor.client.event.SearchesInfoChangedEvent;

/**
 * Singleton implementation of {@link SearchesStatus}.
 *
 * @author Jan Venstermans
 */
public final class SearchesStatusImpl implements SearchesStatus {

	private static SearchesStatus instance;

	private boolean disabled;

	private ClientVectorLayerInfo clientVectorLayerInfo;

	private ConfiguredSearchesInfo searchesInfo;

	private ConfiguredSearch selectedSearchConfig;

	private ConfiguredSearchAttribute selectedSearchAttribute;

	private SearchesStatusImpl() {

	}

	public static SearchesStatus getInstance() {
		if (instance == null) {
			instance = new SearchesStatusImpl();
		}
		return instance;
	}

	@Override
	public void setSearchesInfo(ConfiguredSearchesInfo searchesInfo) {
		this.searchesInfo = searchesInfo;
		this.selectedSearchConfig = null;
		this.selectedSearchAttribute = null;
		SearchAndFilterEditor.fireSearchesInfoChangedEvent(new SearchesInfoChangedEvent());
	}

	@Override
	public ConfiguredSearchesInfo getSearchesInfo() {
		return searchesInfo;
	}

	@Override
	public void setSelectedSearchConfig(ConfiguredSearch selectedSearchConfig) {
		if (isSearchesInfoContainsSearchConfig(selectedSearchConfig)) {
			this.selectedSearchConfig = selectedSearchConfig;
			this.selectedSearchAttribute = null;
			SearchAndFilterEditor.fireSearchesInfoChangedEvent(new SearchesInfoChangedEvent());
		}
	}

	@Override
	public ConfiguredSearch getSelectedSearchConfig() {
		return selectedSearchConfig;
	}

	@Override
	public void setSelectedSearchAttribute(ConfiguredSearchAttribute selectedSearchAttribute) {
		if (isSelectedSearchConfigContainsSearchAttribute(selectedSearchAttribute)) {
			this.selectedSearchAttribute = selectedSearchAttribute;
			SearchAndFilterEditor.fireSearchesInfoChangedEvent(new SearchesInfoChangedEvent());
		}
	}

	@Override
	public ConfiguredSearchAttribute getSelectedSearchAttribute() {
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
	public ClientVectorLayerInfo getClientVectorLayerInfo() {
		return clientVectorLayerInfo;
	}

	@Override
	public void setClientVectorLayerInfo(ClientVectorLayerInfo clientVectorLayerInfo) {
		this.clientVectorLayerInfo = clientVectorLayerInfo;
		SearchAndFilterEditor.fireVectorLayerInfoChangedEvent();
	}

	@Override
	public void saveSearch(ConfiguredSearch searchConfig, boolean newSearch) {
		if (!isSearchesInfoContainsSearchConfig(searchConfig) && newSearch) {
			searchesInfo.getSearchConfigs().add(searchConfig);
		}
		SearchAndFilterEditor.fireSearchesInfoChangedEvent(new SearchesInfoChangedEvent());
	}

	@Override
	public void saveSearchAttribute(ConfiguredSearchAttribute searchAttribute, ConfiguredSearch searchConfig, boolean newAttribute) {
		if (newAttribute) {
			searchConfig.getAttributes().add(searchAttribute);
		} else {
			// TODO update
		}
		SearchAndFilterEditor.fireSearchesInfoChangedEvent(new SearchesInfoChangedEvent());
	}

	@Override
	public void removeSearch(ConfiguredSearch searchConfig) {
		if (isSearchesInfoContainsSearchConfig(searchConfig)) {
			searchesInfo.getSearchConfigs().remove(searchConfig);
			SearchAndFilterEditor.fireSearchesInfoChangedEvent(new SearchesInfoChangedEvent());
		}
	}

	@Override
	public void removeSearchAttribute(ConfiguredSearchAttribute searchAttribute, ConfiguredSearch searchConfig) {
		if (isSearchesInfoContainsSearchConfig(searchConfig)
				&& searchConfig.getAttributes().contains(searchAttribute)) {
			searchConfig.getAttributes().remove(searchAttribute);
			SearchAndFilterEditor.fireSearchesInfoChangedEvent(new SearchesInfoChangedEvent());
		}
	}

	/* private methods */

	private boolean isSearchesInfoContainsSearchConfig(ConfiguredSearch searchConfig) {
		if (searchesInfo != null && searchesInfo.getSearchConfigs() != null) {
			for (ConfiguredSearch config : searchesInfo.getSearchConfigs()) {
				if (searchConfig.getTitle().equals(config.getTitle())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isSelectedSearchConfigContainsSearchAttribute(ConfiguredSearchAttribute searchAttribute) {
		if (selectedSearchConfig != null && selectedSearchConfig.getAttributes() != null &&
				selectedSearchConfig.getAttributes().contains(searchAttribute)) {
			return true;
		}
		return false;
	}
}
