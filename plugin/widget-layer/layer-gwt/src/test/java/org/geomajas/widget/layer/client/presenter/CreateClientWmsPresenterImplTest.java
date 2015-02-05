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

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Canvas;
import junit.framework.Assert;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.layer.configuration.ClientWmsLayerInfo;
import org.geomajas.gwt2.client.map.layer.LegendConfig;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.client.map.render.LayerRenderer;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerInfo;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerMetadataUrlInfo;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerStyleInfo;
import org.geomajas.gwt2.plugin.wms.client.capabilities.v1_1_1.WmsLayerInfo111;
import org.geomajas.gwt2.plugin.wms.client.layer.WmsLayer;
import org.geomajas.gwt2.plugin.wms.client.layer.WmsLayerConfiguration;
import org.geomajas.gwt2.plugin.wms.client.service.WmsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

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
	public void testCreateWmsLayerInfo() throws Exception {
		ClientMapInfo clientMapInfoMock = Mockito.mock(ClientMapInfo.class);
		WmsLayerInfo wmsLayerInfoMock = Mockito.mock(WmsLayerInfo.class);
		MapView mapViewMock = Mockito.mock(MapView.class);
		String crsStub = "crsStub";
		String baseWmsUrlStub = "baseWmsUrlStub";
		String layerNameStub = "layerNameStub";
		WmsService.WmsVersion wmsVersionStub = WmsService.WmsVersion.V1_1_1;
		stub(mapModel.getCrs()).toReturn(crsStub);
		stub(mapModel.getMapInfo()).toReturn(clientMapInfoMock);
		stub(mapModel.getMapView()).toReturn(mapViewMock);
		float maxScaleStub = 1F;
		Bbox bboxStub = new Bbox();
		stub(clientMapInfoMock.getMaximumScale()).toReturn(maxScaleStub);
		stub(wmsLayerInfoMock.getBoundingBox(Mockito.anyString())).toReturn(bboxStub);
		WmsSelectedLayerInfo wmsSelectedLayerInfo = new WmsSelectedLayerInfo();
		wmsSelectedLayerInfo.setBaseWmsUrl(baseWmsUrlStub);
		wmsSelectedLayerInfo.setWmsVersion(wmsVersionStub);
		wmsSelectedLayerInfo.setName(layerNameStub);
		wmsSelectedLayerInfo.setWmsLayerInfo(wmsLayerInfoMock);

		ClientWmsLayerInfo clientWmsLayerInfo = presenter.createClientWmsLayerInfo(wmsSelectedLayerInfo, mapwidget);

		Assert.assertEquals(crsStub, clientWmsLayerInfo.getWmsLayer().getConfiguration().getCrs());
		Assert.assertEquals(baseWmsUrlStub, clientWmsLayerInfo.getWmsLayer().getConfiguration().getBaseUrl());
		Assert.assertEquals(wmsVersionStub, clientWmsLayerInfo.getWmsLayer().getConfiguration().getVersion());
		Assert.assertEquals(layerNameStub, clientWmsLayerInfo.getWmsLayer().getTitle());
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
		presenter.createClientWmsLayer();
		//TODO mock WmsClient
		// no params
		Mockito.reset(controllerButtonsView);
		presenter.onFinisStepGetCapabilities(
				"http://apps.geomajas.org/geoserver/ows?service=wms&version=1.3.0&request=GetCapabilities",
				null, null);
		verify(controllerButtonsView, Mockito.times(0)).setWarningLabelText(getCapablitiesWarning, true);
	}  */

}
