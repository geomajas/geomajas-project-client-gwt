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

import org.geomajas.widget.searchandfilter.editor.client.BasePowerMockitoTest;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

/**
 * Basic test class with mock for the startup class.
 * 
 * @author Jan Venstermans
 *
 */
public class SearchesPresenterTestImpl extends BasePowerMockitoTest {

	private ConfiguredSearchesPresenterImpl searchesPresenter;

	@Before
	public void before() {
		searchesPresenter = new ConfiguredSearchesPresenterImpl();
	}


	@Test
	public void constructor() {
		verify(configuredSearchesViewMock).setHandler(searchesPresenter);
	}

	@Test
	public void addSearch() {
		searchesPresenter.onAddSearch();
		//verify(searchView).show(any(ConfiguredSearch.class));
	}

}
