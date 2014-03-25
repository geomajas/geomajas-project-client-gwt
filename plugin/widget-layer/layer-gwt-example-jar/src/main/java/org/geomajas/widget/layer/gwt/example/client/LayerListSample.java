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
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.widget.layer.client.presenter.LayerListPresenter;
import org.geomajas.widget.layer.client.presenter.LayerListPresenterImpl;
import org.geomajas.widget.layer.gwt.example.client.i18n.WidgetLayerExampleMessages;

/**
 * <p>
 * Sample that shows the usage of the {@link org.geomajas.widget.layer.client.presenter.LayerListPresenter}.
 * </p>
 * 
 * @author Jan Venstermans
 */
public class LayerListSample extends SamplePanel {

	private static final WidgetLayerExampleMessages MESSAGES = GWT.create(WidgetLayerExampleMessages.class);

	public static final String TITLE = "LayerList";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new LayerListSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();

		// Build a map, and set a PanController:
		VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);
		final MapWidget map = new MapWidget("mapLegend", "gwtExample");
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		// Build the LayerList:
		final LayerListPresenter layersManagementPresenter = new LayerListPresenterImpl(map);
		layersManagementPresenter.setDragDropEnabled(true);
		Canvas canvas = (Canvas) layersManagementPresenter.getWidget();
		canvas.setHeight(200);
		canvas.setWidth100();

		// Add both to the main layout:
		mainLayout.addMember(layersManagementPresenter.getWidget());
		mainLayout.addMember(mapLayout);

		return mainLayout;
	}

	public String getDescription() {
		return MESSAGES.layerListDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { }; // use configuration files of gwt-face-example-jar
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
