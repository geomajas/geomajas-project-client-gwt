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
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.widget.layer.client.presenter.ClientWmsLayerListPresenterImpl;
import org.geomajas.widget.layer.client.presenter.CreateClientWmsPresenter;
import org.geomajas.widget.layer.client.presenter.CreateClientWmsPresenterImpl;
import org.geomajas.widget.layer.client.presenter.RemovableLayerListPresenterImpl;
import org.geomajas.widget.layer.gwt.example.client.i18n.WidgetLayerExampleMessages;

/**
 * <p>
 * Sample that shows the usage of the {@link org.geomajas.widget.layer.client.presenter.RemovableLayerListPresenter}.
 * </p>
 * 
 * @author Jan Venstermans
 */
public class ClientWmsLayerListSample extends SamplePanel {

	private static final WidgetLayerExampleMessages MESSAGES = GWT.create(WidgetLayerExampleMessages.class);

	public static final String TITLE = "LayerListClientWms";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new ClientWmsLayerListSample();
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
		final MapWidget map = new MapWidget("mapWmsClientWithResolutions", "appWidgetLayer");
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		// Build the LayerList:
		VLayout layout = new VLayout();
		layout.setWidth(700);
		layout.setHeight(200);

		HLayout gridsLayout = new HLayout();
		gridsLayout.setWidth100();
		gridsLayout.setHeight100();

		RemovableLayerListPresenterImpl layersManagementPresenter = new RemovableLayerListPresenterImpl(map);
		layersManagementPresenter.setDragDropEnabled(true);
		layersManagementPresenter.setShowDeleteButtons(false);

		ClientWmsLayerListPresenterImpl clientLayerListPresenter = new ClientWmsLayerListPresenterImpl(map);
		clientLayerListPresenter.setDragDropEnabled(false);
		clientLayerListPresenter.setShowDeleteButtons(true);
		clientLayerListPresenter.setRemoveIconUrl(WidgetLayout.iconRemove);

		final CreateClientWmsPresenter createClientWmsPresenter = new CreateClientWmsPresenterImpl(map);

		VLayout layoutWidget1 = new VLayout();
		layoutWidget1.setIsGroup(true);
		layoutWidget1.setGroupTitle(MESSAGES.layerListClientWmsAllLayersGroupTitle());
		layoutWidget1.addMember(layersManagementPresenter.getWidget());
		gridsLayout.addMember(layoutWidget1);

		VLayout layoutWidget2 = new VLayout();
		layoutWidget2.setIsGroup(true);
		layoutWidget2.setGroupTitle(MESSAGES.layerListClientWmsClientLayersGroupTitle());
		layoutWidget2.addMember(clientLayerListPresenter.getWidget());
		gridsLayout.addMember(layoutWidget2);

		HLayout addImgContainer = new HLayout(10);
		addImgContainer.setAlign(Alignment.CENTER);
		addImgContainer.setHeight(25);
		addImgContainer.setLayoutAlign(Alignment.CENTER);

		Label label = new Label(MESSAGES.layerListClientWmsAddClientLayerButtonLabel());
		label.setWrap(false);
		addImgContainer.addMember(label);

		ImgButton addImg = new ImgButton();
		addImg.setSrc(WidgetLayout.iconAdd);
		addImg.setShowDown(false);
		addImg.setShowRollOver(false);
		addImg.setPrompt(MESSAGES.layerListClientWmsAddClientLayerButtonTooltip());
		addImg.setHeight(16);
		addImg.setWidth(16);
		addImg.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				createClientWmsPresenter.createClientWmsLayer();
			}
		});
		addImgContainer.addMember(addImg);

		layout.addMember(addImgContainer);
		layout.addMember(gridsLayout);
		layout.setBorder("1px solid");

		// Add both to the main layout:
		mainLayout.addMember(layout);
		mainLayout.addMember(mapLayout);

		return mainLayout;
	}

	public String getDescription() {
		return MESSAGES.layerListClientWmsDescription();
	}

	/*public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/example/context/mapLegend.xml",
				"classpath:org/geomajas/gwt/example/base/layerLakes110m.xml",
				"classpath:org/geomajas/gwt/example/base/layerRivers50m.xml",
				"classpath:org/geomajas/gwt/example/base/layerPopulatedPlaces110m.xml",
				"classpath:org/geomajas/gwt/example/base/layerWmsBluemarble.xml" };*/
		public String[] getConfigurationFiles() {
			return new String[] {
					"classpath:org/geomajas/widget/layer/gwt/example/context/appWidgetLayer.xml",
					"classpath:org/geomajas/widget/layer/gwt/example/context/mapLayerList.xml",
					"classpath:org/geomajas/widget/layer/gwt/example/context/mapWmsClientWithResolutions.xml",
					"classpath:org/geomajas/widget/layer/gwt/example/context/layerPopulatedPlaces110m.xml"};
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
