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
package org.geomajas.widget.searchandfilter.editor.client.view;

import org.geomajas.widget.searchandfilter.editor.client.presenter.SearchAttributePresenter;
import org.geomajas.widget.searchandfilter.editor.client.presenter.SearchPresenter;
import org.geomajas.widget.searchandfilter.editor.client.presenter.SearchesPresenter;

/**
 * Interface for views.
 *
 * @author Jan Venstermans
 */
public class ViewManagerImpl implements ViewManager {

	private SearchesPresenter.View searchesView = new SearchesView();

	private SearchesViewFactory searchesViewFactory = new SearchesViewFactory();

	private SearchPresenter.View searchView = new SearchView();

	private SearchAttributePresenter.View searchAttributeView = new SearchAttributeView();

	@Override
	public SearchesPresenter.View getSearchesView() {
		return searchesView;
	}

	@Override
	public SearchesViewFactory getSearchesViewFactory() {
		return searchesViewFactory;
	}

	@Override
	public SearchPresenter.View getSearchView() {
		return searchView;
	}

	@Override
	public SearchAttributePresenter.View getSearchAttributeView() {
		return searchAttributeView;
	}

	public void setSearchesView(SearchesPresenter.View searchesView) {
		this.searchesView = searchesView;
	}

	public void setSearchesViewFactory(SearchesViewFactory searchesViewFactory) {
		this.searchesViewFactory = searchesViewFactory;
	}

	public void setSearchView(SearchPresenter.View searchView) {
		this.searchView = searchView;
	}

	public void setSearchAttributeView(SearchAttributePresenter.View searchAttributeView) {

		this.searchAttributeView = searchAttributeView;
	}
}
