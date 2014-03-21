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

package org.geomajas.widget.layer.gwt.example.client;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.widget.layer.client.presenter.LayerListClientWmsPresenterImpl;
import org.geomajas.widget.layer.gwt.example.client.i18n.WidgetLayerExampleMessages;

/**
 * <p>
 * Sample that shows the usage of the {@link org.geomajas.widget.layer.client.presenter.LayerListClientWmsPresenter}.
 * </p>
 * 
 * @author Jan Venstermans
 */
public class LayerListClientWmsSample extends SamplePanel {

	private static final WidgetLayerExampleMessages MESSAGES = GWT.create(WidgetLayerExampleMessages.class);

	public static final String TITLE = "LayerListClientWms";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new LayerListClientWmsSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();

		// Build a map, and set a PanController:
		VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);
		//final MapWidget map = new MapWidget("mapLegend", "gwtExample");
		final MapWidget map = new MapWidget("mapWmsClientWithResolutions", "appWmsClient");
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		// Build the LayerList:
		VLayout layout = new VLayout();
		layout.setWidth(350);
		layout.setHeight(200);

		final LayerListClientWmsPresenterImpl layersManagementPresenter = new LayerListClientWmsPresenterImpl(map);
		layersManagementPresenter.setDragDropEnabled(true);
		layersManagementPresenter.setShowDeleteButtons(false);

		Layout addImgContainer = new Layout();
		addImgContainer.setWidth(64 + 16); //16 from scroller in grid
		addImgContainer.setAlign(Alignment.CENTER);
		addImgContainer.setHeight(16);
		addImgContainer.setLayoutAlign(Alignment.RIGHT);

		ImgButton addImg = new ImgButton();
		addImg.setSrc(WidgetLayout.iconAdd);
		addImg.setShowDown(false);
		addImg.setShowRollOver(false);
		addImg.setPrompt("Add a layer");
		addImg.setHeight(16);
		addImg.setWidth(16);
		addImg.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				layersManagementPresenter.addClientWmsLayer();
			}
		});
		addImgContainer.addMember(addImg);

		layout.addMember(addImgContainer);
		layout.addMember(layersManagementPresenter.getWidget());
		layout.setBorder("1px solid");

		// Add both to the main layout:
		mainLayout.addMember(layout);
		mainLayout.addMember(mapLayout);

		return mainLayout;
	}

	public String getDescription() {
		return MESSAGES.layerListClientWmsDescription();
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
