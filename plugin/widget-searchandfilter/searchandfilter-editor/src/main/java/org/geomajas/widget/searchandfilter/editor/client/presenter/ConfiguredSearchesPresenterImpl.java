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

import com.smartgwt.client.widgets.Canvas;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchesInfo;
import org.geomajas.widget.searchandfilter.editor.client.SearchAndFilterEditor;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearch;
import org.geomajas.widget.searchandfilter.editor.client.event.SearchesInfoChangedEvent;

/**
 * Default implementation of {@link ConfiguredSearchesPresenter}.
 *
 * @author Jan Venstermans
 */
public class ConfiguredSearchesPresenterImpl implements ConfiguredSearchesPresenter,
		ConfiguredSearchesPresenter.Handler, SearchesInfoChangedEvent.Handler {

	private View view;

	private ConfiguredSearchPresenter configuredSearchPresenter;

	private ConfiguredSearchesInfo searchesInfo;

	public ConfiguredSearchesPresenterImpl() {
		this.view = SearchAndFilterEditor.getViewManager().getConfiguredSearchesViewFactory().createSearchesView();
		this.configuredSearchPresenter = new ConfiguredSearchPresenterImpl();
		view.setHandler(this);
		searchesInfo = SearchAndFilterEditor.getConfiguredSearchesStatus().getSearchesInfo();
		bind();
	}

	private void bind() {
		SearchAndFilterEditor.addSearchesInfoChangedHandler(this);
	}

	@Override
	public void onAddSearch() {
		configuredSearchPresenter.createSearch();
	}

	@Override
	public void onSelect(ConfiguredSearch search) {
		SearchAndFilterEditor.getConfiguredSearchesStatus().setSelectedSearchConfig(search);
	}

	@Override
	public void onEdit(ConfiguredSearch search) {
		onSelect(search);
		configuredSearchPresenter.editSelectedSearch();
	}

	@Override
	public void onRemove(ConfiguredSearch search) {
		SearchAndFilterEditor.getConfiguredSearchesStatus().removeSearch(search);
	}

	@Override
	public View getView() {
		return view;
	}

	@Override
	public Canvas getCanvas() {
		return view.getCanvas();
	}

	@Override
	public void onSearchInfoChanged(SearchesInfoChangedEvent event) {
		searchesInfo = SearchAndFilterEditor.getConfiguredSearchesStatus().getSearchesInfo();
		updateView();
	}

	private void updateView() {
		if (searchesInfo != null) {
			view.updateGrid(searchesInfo.getSearchConfigs());
		}
	}
}
