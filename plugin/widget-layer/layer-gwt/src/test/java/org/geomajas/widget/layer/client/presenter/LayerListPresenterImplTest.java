/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.layer.client.presenter;

import com.google.gwtmockito.GwtMockitoTestRunner;
import junit.framework.Assert;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.event.MapModelChangedEvent;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.layer.client.BasicGwtMockTest;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import org.mockito.Mock;


/**
 * Basic test class with gwt mock for the startup class.
 * 
 * @author Jan Venstermans
 *
 */
public class LayerListPresenterImplTest extends BasicForPresenterMock {

	private LayerListPresenterImpl presenter;

	@Before
	public void before() {
		presenter = new LayerListPresenterImpl(mapwidget);
	}

	@Test
	public void constructorTest() {
		Assert.assertEquals(layerListView, presenter.getView());
		verify(layerListView).setHandler(presenter);
	}

	@Test
	public void onToggleVisibilityTest() {
		// hide layer
		stub(vectorLayer1.isShowing()).toReturn(true);
		presenter.onToggleVisibility(vectorLayer1);
		verify(vectorLayer1).setVisible(false);

		// show layer
		stub(rasterLayer.isShowing()).toReturn(false);
		presenter.onToggleVisibility(rasterLayer);
		verify(rasterLayer).setVisible(true);
	}

	@Test
	public void onMoveLayerTest() {
		//first, layer configuration needs to be loaded
		presenter.onMapModelChanged(null);

		// raster layer
		presenter.onMoveLayer(rasterLayer, 8);
		verify(mapModel).moveRasterLayer(rasterLayer, 8);

		// vector layer !  In this case, call mapModel with vector layer index
		presenter.onMoveLayer(vectorLayer1, 5);
		verify(mapModel).moveVectorLayer(vectorLayer1, 4);
	}

	@Test
	public void updateViewTest() {
		presenter.updateView();
		verify(mapModel).getLayers();
		verify(layerListView).updateView(layers);
	}
}
