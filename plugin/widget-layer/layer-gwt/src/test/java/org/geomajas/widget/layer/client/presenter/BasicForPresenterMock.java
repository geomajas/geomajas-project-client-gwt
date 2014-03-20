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
package org.geomajas.widget.layer.client.presenter;

import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.layer.client.BasicGwtMockTest;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.stub;


/**
 * Basic test class with gwt mock for the startup class.
 * 
 * @author Jan Venstermans
 *
 */
public abstract class BasicForPresenterMock extends BasicGwtMockTest {

	protected List<org.geomajas.gwt.client.map.layer.Layer<?>> layers = new ArrayList<org.geomajas.gwt.client.map.layer.Layer<?>>();

	protected List<VectorLayer> vectorLayers;

	@MockitoAnnotations.Mock
	protected MapWidget mapwidget;

	@MockitoAnnotations.Mock
	protected MapModel mapModel;

	@MockitoAnnotations.Mock
	protected VectorLayer vectorLayer1;

	@MockitoAnnotations.Mock
	protected VectorLayer vectorLayer2;

	@MockitoAnnotations.Mock
	protected RasterLayer rasterLayer;

	@Before
	public void createMockLayers() {
		stub(mapwidget.getMapModel()).toReturn(mapModel);
		layers.clear();
		layers.add(rasterLayer);
		layers.add(vectorLayer1);
		layers.add(vectorLayer2);
		stub(mapModel.getLayers()).toReturn(layers);
		vectorLayers = new ArrayList<VectorLayer>(Arrays.asList(vectorLayer1, vectorLayer2));
		stub(mapModel.getVectorLayers()).toReturn(vectorLayers);
	}
}
