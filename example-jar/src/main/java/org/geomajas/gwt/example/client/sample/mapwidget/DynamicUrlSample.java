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

package org.geomajas.gwt.example.client.sample.mapwidget;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.widget.DynamicUrlController;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.gwt.example.client.sample.i18n.SampleMessages;

/**
 * Sample that shows how to register dynamic url controller to a map.
 * 
 * @author Dosi Bingov
 */
public class DynamicUrlSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "DynamicUrl";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new DynamicUrlSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(10);

		// Create map with OSM layer, and add a PanController to it:
		VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);
		mapLayout.setHeight("60%");

		final MapWidget map = new MapWidget("mapOsm", "gwtExample");

		//Register a map to dynamic url controller.
		//Do that after MapWidget is instatiated.
		DynamicUrlController.register(map);

		map.setController(new PanController(map));
		mapLayout.addMember(map);

		// Place map layout in tha main layout:
		layout.addMember(mapLayout);

		return layout;
	}

	public String getDescription() {
		return MESSAGES.dynamicUrlDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] {
				"classpath:org/geomajas/gwt/example/context/mapOsm.xml",
				"classpath:org/geomajas/gwt/example/base/layerOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
