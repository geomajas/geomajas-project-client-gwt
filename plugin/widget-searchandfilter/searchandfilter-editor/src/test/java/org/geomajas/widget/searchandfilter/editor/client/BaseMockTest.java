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

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.geomajas.widget.searchandfilter.editor.client.configuration.SearchesStatus;
import org.geomajas.widget.searchandfilter.editor.client.presenter.SearchAttributePresenter;
import org.geomajas.widget.searchandfilter.editor.client.presenter.SearchPresenter;
import org.geomajas.widget.searchandfilter.editor.client.presenter.SearchesPresenter;
import org.geomajas.widget.searchandfilter.editor.client.view.SearchesViewFactory;
import org.geomajas.widget.searchandfilter.editor.client.view.ViewManager;
import org.geomajas.widget.searchandfilter.search.dto.AndCriterion;
import org.geomajas.widget.searchandfilter.search.dto.AttributeCriterion;
import org.geomajas.widget.searchandfilter.search.dto.GeometryCriterion;
import org.geomajas.widget.searchandfilter.search.dto.OrCriterion;
import org.geomajas.widget.searchandfilter.service.DtoSearchConverterServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

/**
 * Basic test class with mock for the startup class.
 * 
 * @author Jan Venstermans
 *
 */
@RunWith(GwtMockitoTestRunner.class)
public class BaseMockTest {

	@Mock
	protected SearchesPresenter.View searchesView;

	@Mock
	protected SearchesViewFactory searchesViewFactory;

	@Mock
	protected SearchPresenter.View searchView;

	@Mock
	protected SearchAttributePresenter.View searchAttributeView;

	@Before
	public void setUp() {
		SearchAndFilterEditor.setViewManager(new ViewManager() {
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
		});
	}

}
