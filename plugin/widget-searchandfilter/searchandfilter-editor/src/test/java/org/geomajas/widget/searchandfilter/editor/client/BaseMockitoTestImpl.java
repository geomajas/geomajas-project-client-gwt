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
import org.geomajas.widget.searchandfilter.editor.client.presenter.SearchAttributePresenter;
import org.geomajas.widget.searchandfilter.editor.client.presenter.SearchPresenter;
import org.geomajas.widget.searchandfilter.editor.client.presenter.SearchesPresenter;
import org.geomajas.widget.searchandfilter.editor.client.view.SearchAttributeView;
import org.geomajas.widget.searchandfilter.editor.client.view.SearchView;
import org.geomajas.widget.searchandfilter.editor.client.view.SearchesView;
import org.geomajas.widget.searchandfilter.editor.client.view.SearchesViewFactory;
import org.geomajas.widget.searchandfilter.editor.client.view.ViewManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Basic test class with mock for the startup class.
 * 
 * @author Jan Venstermans
 *
 */
@RunWith(PowerMockRunner.class)
public class BaseMockitoTestImpl {

	//protected SearchesPresenter.View searchesView = mock(SearchesPresenter.View.class);
	protected SearchesView searchesView = mock(SearchesView.class);

	protected SearchesViewFactory searchesViewFactory = mock(SearchesViewFactory.class);;

	protected SearchPresenter.View searchView = mock(SearchPresenter.View.class);

	protected SearchAttributePresenter.View searchAttributeView = mock(SearchAttributePresenter.View.class);

	@Before
	public void setUp() {
		try {
			PowerMockito.whenNew(SearchesView.class).withNoArguments().thenReturn(searchesView);
			//PowerMockito.whenNew(SearchesPresenter.View.class).withNoArguments().thenReturn(searchesView);
		} catch (Exception e) {

		}
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
		when(searchesViewFactory.createSearchesView()).thenReturn(searchesView);
	}

	@Test
	public void test1() {
		int i = 5;
		i*=2;
		Assert.assertEquals(10, i);

	}

}
