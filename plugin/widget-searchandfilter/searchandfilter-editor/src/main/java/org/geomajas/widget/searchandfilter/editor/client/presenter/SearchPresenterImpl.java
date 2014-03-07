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
import org.geomajas.widget.searchandfilter.configuration.client.SearchAttribute;
import org.geomajas.widget.searchandfilter.configuration.client.SearchConfig;
import org.geomajas.widget.searchandfilter.editor.client.event.SearchesInfoChangedEvent;

/**
 * Default implementation of {@link SearchPresenter}.
 *
 * @author Jan Venstermans
 */
public class SearchPresenterImpl implements SearchPresenter, SearchPresenter.Handler,
		SearchesInfoChangedEvent.Handler {

	private View view;

	private SearchAttributePresenter searchAttributePresenter;

	private SearchConfig searchConfig;

	private boolean newSearchConfig;

	public SearchPresenterImpl() {
		this.view = SearchAndFilterEditor.getViewManager().getSearchView();
		this.searchAttributePresenter = new SearchAttributePresenterImpl();
		view.setHandler(this);
		bind();
	}

	private void bind() {
		SearchAndFilterEditor.addSearchesInfoChangedHandler(this);
	}

	private SearchConfig getSelectedSearchConfig() {
		return SearchAndFilterEditor.getSearchesStatus().getSelectedSearchConfig();
	}

	@Override
	public void onAddAttribute() {
		searchAttributePresenter.createSearchAttribute();
	}

	@Override
	public void onSelect(SearchAttribute attribute) {
		SearchAndFilterEditor.getSearchesStatus().setSelectedSearchAttribute(attribute);
	}

	@Override
	public void onEdit(SearchAttribute attribute) {
		onSelect(attribute);
		searchAttributePresenter.editSelectedAttribute();
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
			this.searchConfig = new SearchConfig();
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
			SearchAndFilterEditor.getSearchesStatus().saveSearch(searchConfig, newSearchConfig);
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
