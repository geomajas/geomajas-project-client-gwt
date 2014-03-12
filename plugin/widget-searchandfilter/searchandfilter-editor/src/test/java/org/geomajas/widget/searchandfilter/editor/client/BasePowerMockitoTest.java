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

import org.geomajas.widget.searchandfilter.editor.client.presenter.SearchAttributePresenter;
import org.geomajas.widget.searchandfilter.editor.client.presenter.SearchPresenter;
import org.geomajas.widget.searchandfilter.editor.client.presenter.SearchesPresenter;
import org.geomajas.widget.searchandfilter.editor.client.view.SearchAttributeView;
import org.geomajas.widget.searchandfilter.editor.client.view.SearchView;
import org.geomajas.widget.searchandfilter.editor.client.view.SearchesView;
import org.geomajas.widget.searchandfilter.editor.client.view.SearchesViewFactory;
import org.geomajas.widget.searchandfilter.editor.client.view.ViewManager;
import org.geomajas.widget.searchandfilter.editor.client.view.ViewManagerImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
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
@PrepareForTest({ViewManagerImpl.class, SearchesViewFactory.class, SearchAndFilterEditor.class })
public class BasePowerMockitoTest {

	protected SearchesView searchesViewMock;

	protected SearchView searchViewMock;

	protected SearchAttributeView searchAttributeViewMock;

	protected SearchAttributeService searchAttributeServiceMock;

	@Before
	public void setUp() throws Exception {
		searchesViewMock = mock(SearchesView.class);
		searchViewMock = mock(SearchView.class);
		searchAttributeViewMock = mock(SearchAttributeView.class);
		searchAttributeServiceMock = mock(SearchAttributeService.class);
		PowerMockito.whenNew(SearchesView.class).withNoArguments().thenReturn(searchesViewMock);
		PowerMockito.whenNew(SearchView.class).withNoArguments().thenReturn(searchViewMock);
		PowerMockito.whenNew(SearchAttributeView.class).withNoArguments().thenReturn(searchAttributeViewMock);
		//PowerMockito.whenNew(AttributeCriterionUtil.class).withNoArguments().thenReturn(searchAttributeServiceMock);
		SearchAndFilterEditor.setViewManager(new ViewManager() {
			@Override
			public SearchesPresenter.View getSearchesView() {
				return searchesViewMock;
			}

			@Override
			public SearchesViewFactory getSearchesViewFactory() {
				return new SearchesViewFactory();
			}

			@Override
			public SearchPresenter.View getSearchView() {
				return searchViewMock;
			}

			@Override
			public SearchAttributePresenter.View getSearchAttributeView() {
				return searchAttributeViewMock;
			}
		});
	}

	@Test
	public void test1() {
		//dummy test for the setUp before method
	}

}
