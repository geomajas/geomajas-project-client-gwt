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

import com.google.gwtmockito.GwtMockitoTestRunner;
import junit.framework.Assert;
import org.geomajas.widget.layer.client.presenter.CreateClientWmsPresenter;
import org.geomajas.widget.layer.client.presenter.DeletableLayerListPresenter;
import org.geomajas.widget.layer.client.presenter.LayerListPresenter;
import org.geomajas.widget.layer.client.view.ControllerButtonsViewImpl;
import org.geomajas.widget.layer.client.view.ViewFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
	protected LayerListPresenter.View layerListView;

	@MockitoAnnotations.Mock
	protected DeletableLayerListPresenter.View layerListClientWmsView;

	@MockitoAnnotations.Mock
	protected ControllerButtonsViewImpl controllerButtonsView;

	@MockitoAnnotations.Mock
	protected CreateClientWmsPresenter.GetCapabilitiesView getCapabilitiesView;

	@MockitoAnnotations.Mock
	protected CreateClientWmsPresenter.SelectLayerView selectLayerView;

	@MockitoAnnotations.Mock
	protected CreateClientWmsPresenter.EditLayerSettingsView editLayerSettingsView;

	@Before
	public void setUp() {
		Layer.setViewFactory(new ViewFactory() {

			@Override
			public LayerListPresenter.View createLayerListView() {
				return layerListView;
			}

			@Override
			public DeletableLayerListPresenter.View createLayerListClientWmsView() {
				return layerListClientWmsView;
			}

			@Override
			public CreateClientWmsPresenter.ControllerButtonsView createControllerButtonsView() {
				return controllerButtonsView;
			}

			@Override
			public CreateClientWmsPresenter.GetCapabilitiesView createGetCapabilitiesView() {
				return getCapabilitiesView;
			}

			@Override
			public CreateClientWmsPresenter.SelectLayerView createSelectLayerView() {
				return selectLayerView;
			}

			@Override
			public CreateClientWmsPresenter.EditLayerSettingsView createEditLayerSettingsView() {
				return editLayerSettingsView;
			}
		});
	}


	@Test
	public void setUpTest() {
		Assert.assertNotNull(layerListView);
		Assert.assertNotNull(layerListClientWmsView);
		Assert.assertNotNull(controllerButtonsView);
		Assert.assertNotNull(getCapabilitiesView);
		Assert.assertNotNull(selectLayerView);
		Assert.assertNotNull(editLayerSettingsView);

		Assert.assertEquals(layerListView, Layer.getViewFactory().createLayerListView());
		Assert.assertEquals(layerListClientWmsView, Layer.getViewFactory().createLayerListClientWmsView());
		Assert.assertEquals(controllerButtonsView, Layer.getViewFactory().createControllerButtonsView());
		Assert.assertEquals(getCapabilitiesView, Layer.getViewFactory().createGetCapabilitiesView());
		Assert.assertEquals(selectLayerView, Layer.getViewFactory().createSelectLayerView());
		Assert.assertEquals(editLayerSettingsView, Layer.getViewFactory().createEditLayerSettingsView());
	}
}
