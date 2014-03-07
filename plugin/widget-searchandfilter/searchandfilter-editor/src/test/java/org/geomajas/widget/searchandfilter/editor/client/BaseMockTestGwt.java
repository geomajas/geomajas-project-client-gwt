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
import org.geomajas.widget.searchandfilter.editor.client.view.SearchAttributeView;
import org.geomajas.widget.searchandfilter.editor.client.view.SearchView;
import org.geomajas.widget.searchandfilter.editor.client.view.SearchesView;
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
public class BaseMockTestGwt {

	@Mock
	protected SearchesView searchesView;

	//@Mock
	//protected SearchesViewFactory searchesViewFactory;

	@Mock
	protected SearchView searchView;

	@Mock
	protected SearchAttributeView searchAttributeView;

	@Before
	public void setUp() {
		/*SearchAndFilterEditor.setViewManager(new ViewManager() {
			@Override
			public SearchesPresenter.View getSearchesView() {
				return searchesView;
			}

			@Override
			public SearchesViewFactory getSearchesViewFactory() {
				return new SearchesViewFactory();
			}

			@Override
			public SearchPresenter.View getSearchView() {
				return searchView;
			}

			@Override
			public SearchAttributePresenter.View getSearchAttributeView() {
				return searchAttributeView;
			}
		});*/
	}

	@Test
	public void test1() {
		int i = 5;
		i*=2;
		Assert.assertEquals(10, i);

	}

}
