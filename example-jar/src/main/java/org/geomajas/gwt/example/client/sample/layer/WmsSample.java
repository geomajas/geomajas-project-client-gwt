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

package org.geomajas.gwt.example.client.sample.layer;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.map.layer.ClientWmsLayer;
import org.geomajas.gwt.client.map.layer.configuration.ClientWmsLayerInfo;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.gwt.example.client.sample.i18n.SampleMessages;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.plugin.wms.client.layer.WmsLayerConfiguration;
import org.geomajas.plugin.wms.client.service.WmsService;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample allows the user to add WMS layers.
 * </p>
 * 
 * @author Niels Charlier
 */
public class WmsSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "WMS Client";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new WmsSample();
		}
	};

	private MapWidget mapWidget;

	public Canvas getViewPanel() {
		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();

		// Build a map, and set a PanController:
		VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);
		mapWidget = new MapWidget("mapOsm", "gwtExample");
		mapWidget.setController(new PanController(mapWidget));
		mapLayout.addMember(mapWidget);

		VLayout addWmsLayerLayout = new VLayout(10);
		addWmsLayerLayout.setHeight(80);
		addWmsLayerLayout.setShowEdges(true);

		addWmsLayerLayout.addMember(new HTMLFlow(MESSAGES.wmsTxt()));
		final TextItem txtURL = new TextItem("layer_url", MESSAGES.layerUrl());
		txtURL.setValue("http://apps.geomajas.org/geoserver/wms");
		txtURL.setWidth(250);
		final TextItem txtLayer = new TextItem("layer_name", MESSAGES.layerName());
		txtLayer.setValue("demo_world:simplified_country_borders");
		DynamicForm dynamicForm = new DynamicForm();
		dynamicForm.setFields(txtURL, txtLayer);
		addWmsLayerLayout.addMember(dynamicForm);

		HLayout buttonLayout = new HLayout(5);
		buttonLayout.setPadding(10);

		IButton addLayerButton = new IButton(MESSAGES.addLayer());
		addLayerButton.setWidth(150);
		addLayerButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				mapWidget.getMapModel().addLayer(createSampleWmsLayerInfo(txtLayer.getValueAsString(),
						txtURL.getValueAsString()));

			}
		});
		buttonLayout.addMember(addLayerButton);
		addWmsLayerLayout.addMember(buttonLayout);
		addWmsLayerLayout.setShowResizeBar(true);
		
		// Add both to the main layout:
		mainLayout.addMember(addWmsLayerLayout);
		mainLayout.addMember(mapLayout);

		return mainLayout;
	}

	private ClientWmsLayerInfo createSampleWmsLayerInfo(String name, String url) {

		WmsLayerConfiguration wmsConfig = new WmsLayerConfiguration();
		wmsConfig.setFormat("image/png");
		wmsConfig.setLayers(name);
		wmsConfig.setVersion(WmsService.WmsVersion.V1_1_1);
		wmsConfig.setBaseUrl(url);
		wmsConfig.setTransparent(true);
		wmsConfig.setMaximumResolution(Double.MAX_VALUE);
		wmsConfig.setMinimumResolution(1 / mapWidget.getMapModel().getMapInfo().getMaximumScale());

		TileConfiguration tileConfig = new TileConfiguration(256, 256, new Coordinate(-20026376.393709917,
				-20026376.393709917), mapWidget.getMapModel().getMapView().getResolutions());

		ClientWmsLayer wmsLayer = new ClientWmsLayer(name, mapWidget.getMapModel().getMapInfo().getCrs(), wmsConfig,
				tileConfig);

		ClientWmsLayerInfo wmsLayerInfo = new ClientWmsLayerInfo(wmsLayer);
		return wmsLayerInfo;
	}

	public String getDescription() {
		return MESSAGES.clientWmsDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/example/context/mapLegend.xml",
				"classpath:org/geomajas/gwt/example/base/layerLakes110m.xml",
				"classpath:org/geomajas/gwt/example/base/layerRivers50m.xml",
				"classpath:org/geomajas/gwt/example/base/layerPopulatedPlaces110m.xml",
				"classpath:org/geomajas/gwt/example/base/layerWmsBluemarble.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
