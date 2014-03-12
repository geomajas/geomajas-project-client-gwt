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

import org.geomajas.widget.searchandfilter.editor.client.presenter.ConfiguredSearchPresenter;
import org.geomajas.widget.searchandfilter.editor.client.presenter.ConfiguredSearchAttributePresenter;
import org.geomajas.widget.searchandfilter.editor.client.presenter.ConfiguredSearchesPresenter;
import org.geomajas.widget.searchandfilter.editor.client.view.ConfiguredSearchAttributeView;
import org.geomajas.widget.searchandfilter.editor.client.view.ConfiguredSearchView;
import org.geomajas.widget.searchandfilter.editor.client.view.ConfiguredSearchesView;
import org.geomajas.widget.searchandfilter.editor.client.view.ConfiguredSearchesViewFactory;
import org.geomajas.widget.searchandfilter.editor.client.view.ViewManager;
import org.geomajas.widget.searchandfilter.editor.client.view.ViewManagerImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;

/**
 * Basic test class with mock for the startup class.
 * 
 * @author Jan Venstermans
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ViewManagerImpl.class, ConfiguredSearchesViewFactory.class, SearchAndFilterEditor.class })
public class BasePowerMockitoTest {

	protected ConfiguredSearchesView configuredSearchesViewMock;

	protected ConfiguredSearchView configuredSearchViewMock;

	protected ConfiguredSearchAttributeView configuredSearchAttributeViewMock;

	protected ConfiguredSearchAttributeService configuredSearchAttributeServiceMock;

	@Before
	public void setUp() throws Exception {
		configuredSearchesViewMock = mock(ConfiguredSearchesView.class);
		configuredSearchViewMock = mock(ConfiguredSearchView.class);
		configuredSearchAttributeViewMock = mock(ConfiguredSearchAttributeView.class);
		configuredSearchAttributeServiceMock = mock(ConfiguredSearchAttributeService.class);
		PowerMockito.whenNew(ConfiguredSearchesView.class).withNoArguments().thenReturn(configuredSearchesViewMock);
		PowerMockito.whenNew(ConfiguredSearchView.class).withNoArguments().thenReturn(configuredSearchViewMock);
		PowerMockito.whenNew(ConfiguredSearchAttributeView.class).withNoArguments().thenReturn(configuredSearchAttributeViewMock);
		//PowerMockito.whenNew(AttributeCriterionUtil.class).withNoArguments().thenReturn(configuredSearchAttributeServiceMock);
		SearchAndFilterEditor.setViewManager(new ViewManager() {
			@Override
			public ConfiguredSearchesPresenter.View getSearchesView() {
				return configuredSearchesViewMock;
			}

			@Override
			public ConfiguredSearchesViewFactory getConfiguredSearchesViewFactory() {
				return new ConfiguredSearchesViewFactory();
			}

			@Override
			public ConfiguredSearchPresenter.View getSearchView() {
				return configuredSearchViewMock;
			}

			@Override
			public ConfiguredSearchAttributePresenter.View getSearchAttributeView() {
				return configuredSearchAttributeViewMock;
			}
		});
	}

	@Test
	public void test1() {
		//dummy test for the setUp before method
	}

}
