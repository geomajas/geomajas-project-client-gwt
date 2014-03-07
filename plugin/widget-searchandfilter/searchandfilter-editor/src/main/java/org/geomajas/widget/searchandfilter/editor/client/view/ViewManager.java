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
 * Manager of views.
 *
 * @author Jan Venstermans
 */
public interface ViewManager {

	SearchesPresenter.View getSearchesView();

	SearchesViewFactory getSearchesViewFactory();

	SearchPresenter.View getSearchView();

	SearchAttributePresenter.View getSearchAttributeView();
}
