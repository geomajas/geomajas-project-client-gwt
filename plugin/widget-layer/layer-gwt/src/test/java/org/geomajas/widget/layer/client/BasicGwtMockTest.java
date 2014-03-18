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
package org.geomajas.widget.layer.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwtmockito.GwtMockitoTestRunner;
import junit.framework.Assert;
import org.geomajas.widget.layer.client.presenter.LayerListPresenter;
import org.geomajas.widget.layer.client.view.LayerListView;
import org.geomajas.widget.layer.client.view.ViewManager;
import org.geomajas.widget.layer.client.view.ViewManagerImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Basic test class with gwt mock for the startup class.
 * 
 * @author Jan Venstermans
 *
 */
@RunWith(GwtMockitoTestRunner.class)
public class BasicGwtMockTest {

	@MockitoAnnotations.Mock
	protected LayerListView layerListView;

	@Before
	public void setUp() {
		Layer.setViewManager(new ViewManager() {

			@Override
			public LayerListPresenter.View getLayerListView() {
				return layerListView;
			}
		});
	}


	@Test
	public void setUpTest() {
		Assert.assertNotNull(layerListView);
		Assert.assertEquals(layerListView, Layer.getViewManager().getLayerListView());
	}
}
