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

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Canvas;
import org.geomajas.plugin.wms.client.WmsClient;
import org.geomajas.plugin.wms.client.capabilities.WmsGetCapabilitiesInfo;
import org.geomajas.plugin.wms.client.service.WmsService;
import org.geomajas.plugin.wms.client.service.WmsServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

/**
 * Default implementation of {@link LayerListClientWmsPresenter}.
 *
 * @author Jan Venstermans
 *
 */
public class GetCapabilitiesTest {

	private WmsService wmsService;

	@Test
	public void urlTest() {
		String url = "http://apps.geomajas.org/geoserver/ows?service=wms&version=1.3.0&request=GetCapabilities";
		WmsClient.getInstance().getWmsService().getCapabilities(url, WmsService.WmsVersion.V1_3_0,
				new Callback<WmsGetCapabilitiesInfo, String>() {

					@Override
					public void onFailure(String s) {

					}

					@Override
					public void onSuccess(WmsGetCapabilitiesInfo wmsGetCapabilitiesInfo) {

					}
				});
	}

	@Test
	public void testCapabilities130() throws IOException {
		String url = "http://apps.geomajas.org/geoserver/ows?service=wms&version=1.3.0&request=GetCapabilities";
		wmsService = new WmsServiceImpl();
		wmsService.getCapabilities(url, WmsService.WmsVersion.V1_3_0,
				new Callback<WmsGetCapabilitiesInfo, String>() {

					public void onSuccess(WmsGetCapabilitiesInfo result) {
						//assertNotNull(result);

						//List<WmsLayerInfo> layers = result.getLayers();
						//assertNotNull(layers);
						//assertEquals(2, layers.size());

						//checkLayer(1, layers.get(0), WmsVersion.V1_3_0);
						//checkLayer(2, layers.get(1), WmsVersion.V1_3_0);

						//finishTest();
					}

					public void onFailure(String reason) {
						//fail(reason);
						//finishTest();
					}
				});
		//delayTestFinish(10000);
	}
}
