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
import org.geomajas.widget.searchandfilter.editor.client.SearchAndFilterEditor;
import org.geomajas.widget.searchandfilter.editor.client.configuration.SearchConfig;
import org.geomajas.widget.searchandfilter.editor.client.event.SearchesInfoChangedEvent;

/**
 * Default implementation of {@link SearchesPresenter}.
 *
 * @author Jan Venstermans
 */
public class SearchesPresenterImpl implements SearchesPresenter, SearchesPresenter.Handler,
		SearchesInfoChangedEvent.Handler {

	private View view;

	private SearchPresenter searchPresenter;

	public SearchesPresenterImpl() {
		this.view = SearchAndFilterEditor.getViewManager().getSearchesViewFactory().createSearchesView();
		this.searchPresenter = new SearchPresenterImpl();
		view.setHandler(this);
		view.setStatus(SearchAndFilterEditor.getSearchesStatus());
		bind();
	}

	private void bind() {
		SearchAndFilterEditor.addSearchesInfoChangedHandler(this);
	}

	@Override
	public void onAddSearch() {
		searchPresenter.createSearch();
	}

	@Override
	public void onSelect(SearchConfig config) {
		SearchAndFilterEditor.getSearchesStatus().setSelectedSearchConfig(config);
	}

	@Override
	public void onEdit(SearchConfig config) {
		onSelect(config);
		searchPresenter.editSelectedSearch();
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
		view.update();
	}
}
