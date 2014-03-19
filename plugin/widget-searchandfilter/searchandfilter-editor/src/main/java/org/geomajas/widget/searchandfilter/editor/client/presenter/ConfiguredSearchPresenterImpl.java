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

import org.geomajas.widget.searchandfilter.editor.client.SearchAndFilterEditor;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearch;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchAttribute;
import org.geomajas.widget.searchandfilter.editor.client.event.SearchesInfoChangedEvent;

/**
 * Default implementation of {@link ConfiguredSearchPresenter}.
 *
 * @author Jan Venstermans
 */
public class ConfiguredSearchPresenterImpl implements ConfiguredSearchPresenter, ConfiguredSearchPresenter.Handler,
		SearchesInfoChangedEvent.Handler {

	private View view;

	private ConfiguredSearchAttributePresenter configuredSearchAttributePresenter;

	private ConfiguredSearch searchConfig;

	private boolean newSearchConfig;

	public ConfiguredSearchPresenterImpl() {
		this.view = SearchAndFilterEditor.getViewManager().getSearchView();
		this.configuredSearchAttributePresenter = new ConfiguredSearchAttributePresenterImpl();
		view.setHandler(this);
		bind();
	}

	private void bind() {
		SearchAndFilterEditor.addSearchesInfoChangedHandler(this);
	}

	private ConfiguredSearch getSelectedSearchConfig() {
		return SearchAndFilterEditor.getConfiguredSearchesStatus().getSelectedSearchConfig();
	}

	@Override
	public void onAddAttribute() {
		// if current search has not been saved, this should first be done
		if (newSearchConfig) {
			if (view.validateForm()) {
				updateSelectedSearchConfig();
				SearchAndFilterEditor.getConfiguredSearchesStatus().saveSearch(searchConfig, newSearchConfig);
				SearchAndFilterEditor.getConfiguredSearchesStatus().setSelectedSearchConfig(searchConfig);
			} else {
				// needs to be validated first
				return;
			}
		}
		configuredSearchAttributePresenter.createSearchAttribute();
	}

	@Override
	public void onSelect(ConfiguredSearchAttribute attribute) {
		SearchAndFilterEditor.getConfiguredSearchesStatus().setSelectedSearchAttribute(attribute);
	}

	@Override
	public void onEdit(ConfiguredSearchAttribute attribute) {
		onSelect(attribute);
		configuredSearchAttributePresenter.editSelectedAttribute();
	}

	@Override
	public void onRemove(ConfiguredSearchAttribute attribute) {
		SearchAndFilterEditor.getConfiguredSearchesStatus().removeSearchAttribute(attribute, searchConfig);
	}

	@Override
	public View getView() {
		return view;
	}

	@Override
	public void editSelectedSearch() {
		loadAndView(false);
	}

	@Override
	public void createSearch() {
		loadAndView(true);
	}

	private void loadAndView(boolean newSearch) {
		emptySelectedSearchConfig();
		newSearchConfig = newSearch;
		if (newSearch) {
			this.searchConfig = new ConfiguredSearch();
		} else {
			this.searchConfig = getSelectedSearchConfig();
		}
		updateView();
		getView().show();
	}

	private void updateView() {
		view.clearFormValues();
		if (searchConfig != null) {
			view.setTitle(searchConfig.getTitle());
			view.setDescription(searchConfig.getDescription());
			view.setTitleInWindow(searchConfig.getTitleInWindow());
			view.setIconUrl(searchConfig.getIconUrl());
			view.updateGrid(searchConfig.getAttributes());
		}
	}

	@Override
	public void onSave() {
		if (view.validateForm()) {
			updateSelectedSearchConfig();
			SearchAndFilterEditor.getConfiguredSearchesStatus().saveSearch(searchConfig, newSearchConfig);
			emptySelectedSearchConfig();
			view.hide();
		}
	}

	private void emptySelectedSearchConfig() {
		searchConfig = null;
		// empty view
		view.clearFormValues();
	}

	private void updateSelectedSearchConfig() {
		if (searchConfig != null) {
			searchConfig.setTitle(view.getTitle());
			searchConfig.setDescription(view.getDescription());
			searchConfig.setTitleInWindow(view.getTitleInWindow());
			searchConfig.setIconUrl(view.getIconUrl());
			// don't set the search attributes here, this is done separately
		}
	}

	@Override
	public void onSearchInfoChanged(SearchesInfoChangedEvent event) {
		updateView();
	}
}
