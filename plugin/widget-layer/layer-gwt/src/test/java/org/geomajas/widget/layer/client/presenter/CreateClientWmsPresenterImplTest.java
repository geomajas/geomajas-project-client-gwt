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

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Canvas;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

/**
 * Default implementation of {@link RemovableLayerListPresenter}.
 *
 * @author Jan Venstermans
 *
 */
public class CreateClientWmsPresenterImplTest extends BasicForPresenterMock {

	private CreateClientWmsPresenterImpl presenter;

	@MockitoAnnotations.Mock
	protected Canvas controllerButtonsViewPanelContainer;

	@MockitoAnnotations.Mock
	protected Widget getCapabilitiesViewWidget;

	@MockitoAnnotations.Mock
	protected Widget selectLayerViewWidget;

	@MockitoAnnotations.Mock
	protected Widget editLayerSettingsViewWidget;

	private final String getCapablitiesWarning = "getCapablitiesWarning";

	@Before
	public void before() {
		// stub panel
		stub(controllerButtonsView.getPanelContainer()).toReturn(controllerButtonsViewPanelContainer);
		// stub steps
		stub(getCapabilitiesView.getWidget()).toReturn(getCapabilitiesViewWidget);
		stub(getCapabilitiesView.getInvalidMessage()).toReturn(getCapablitiesWarning);
		stub(selectLayerView.getWidget()).toReturn(selectLayerViewWidget);
		stub(selectLayerView.getInvalidMessage()).toReturn(null);
		stub(editLayerSettingsView.getWidget()).toReturn(editLayerSettingsViewWidget);
		stub(editLayerSettingsView.getInvalidMessage()).toReturn(null);
		presenter = new CreateClientWmsPresenterImpl(mapwidget);
	}

	@Test
	public void constructorTest() {
		verify(controllerButtonsViewPanelContainer).addChild(getCapabilitiesViewWidget);
		verify(controllerButtonsView).setControllersButtonHandler(presenter);
		verify(selectLayerView).setSelectLayerFromCapabilitiesHandler(presenter);
		verify(editLayerSettingsView).setEditLayerSettingsHandler(presenter);
	}

	@Test
	public void onGetCapabilitiesUrlIncorrectTest() {
		presenter.createClientWmsLayer();
		// no params
		Mockito.reset(controllerButtonsView);
		presenter.onFinisStepGetCapabilities(
				"http://apps.geomajas.org/geoserver/ows",
				null, null);
		verify(controllerButtonsView).setWarningLabelText(getCapablitiesWarning, true);

		// no service
		Mockito.reset(controllerButtonsView);
		presenter.onFinisStepGetCapabilities(
				"http://apps.geomajas.org/geoserver/ows?version=1.3.0&request=GetCapabilities",
				null, null);
		verify(controllerButtonsView).setWarningLabelText(getCapablitiesWarning, true);

		// no GetCapabilities
		Mockito.reset(controllerButtonsView);
		presenter.onFinisStepGetCapabilities(
				"http://apps.geomajas.org/geoserver/ows?service=wms&version=1.3.0",
				null, null);
		verify(controllerButtonsView).setWarningLabelText(getCapablitiesWarning, true);

		// no correct version
		Mockito.reset(controllerButtonsView);
		presenter.onFinisStepGetCapabilities(
				"http://apps.geomajas.org/geoserver/ows?service=wms&version=1.0.0&request=GetCapabilities",
				null, null);
		verify(controllerButtonsView).setWarningLabelText(getCapablitiesWarning, true);
	}

	/*@Test
	public void onGetCapabilitiesUrlCorrectTest() {
		presenter.createClientWmsLayer(null);
		//TODO mock WmsClient
		// no params
		Mockito.reset(controllerButtonsView);
		presenter.onFinisStepGetCapabilities(
				"http://apps.geomajas.org/geoserver/ows?service=wms&version=1.3.0&request=GetCapabilities",
				null, null);
		verify(controllerButtonsView, times(0)).setWarningLabelText(getCapablitiesWarning, true);
	} */
}
