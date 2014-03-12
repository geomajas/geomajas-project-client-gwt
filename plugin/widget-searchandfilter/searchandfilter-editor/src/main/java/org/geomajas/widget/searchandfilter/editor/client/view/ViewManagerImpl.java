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

import org.geomajas.widget.searchandfilter.editor.client.presenter.ConfiguredSearchPresenter;
import org.geomajas.widget.searchandfilter.editor.client.presenter.ConfiguredSearchAttributePresenter;
import org.geomajas.widget.searchandfilter.editor.client.presenter.ConfiguredSearchesPresenter;

/**
 * Default implementation of {@link ViewManager}.
 *
 * @author Jan Venstermans
 */
public class ViewManagerImpl implements ViewManager {

	private ConfiguredSearchesPresenter.View searchesView = new ConfiguredSearchesView();

	private ConfiguredSearchesViewFactory configuredSearchesViewFactory = new ConfiguredSearchesViewFactory();

	private ConfiguredSearchPresenter.View searchView = new ConfiguredSearchView();

	private ConfiguredSearchAttributePresenter.View searchAttributeView = new ConfiguredSearchAttributeView();

	@Override
	public ConfiguredSearchesPresenter.View getSearchesView() {
		return searchesView;
	}

	@Override
	public ConfiguredSearchesViewFactory getConfiguredSearchesViewFactory() {
		return configuredSearchesViewFactory;
	}

	@Override
	public ConfiguredSearchPresenter.View getSearchView() {
		return searchView;
	}

	@Override
	public ConfiguredSearchAttributePresenter.View getSearchAttributeView() {
		return searchAttributeView;
	}

	public void setSearchesView(ConfiguredSearchesPresenter.View searchesView) {
		this.searchesView = searchesView;
	}

	public void setConfiguredSearchesViewFactory(ConfiguredSearchesViewFactory configuredSearchesViewFactory) {
		this.configuredSearchesViewFactory = configuredSearchesViewFactory;
	}

	public void setSearchView(ConfiguredSearchPresenter.View searchView) {
		this.searchView = searchView;
	}

	public void setSearchAttributeView(ConfiguredSearchAttributePresenter.View searchAttributeView) {

		this.searchAttributeView = searchAttributeView;
	}
}
