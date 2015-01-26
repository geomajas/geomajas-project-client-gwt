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

package org.geomajas.gwt.example.client.sample.toolbar;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.action.ToolCreator;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.toolbar.MeasureModalAction;
import org.geomajas.gwt.client.action.toolbar.ToolId;
import org.geomajas.gwt.client.action.toolbar.ToolbarRegistry;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.gwt.example.client.sample.i18n.SampleMessages;

/**
 * <p>
 * Sample that shows how a toolbar can be added to the map. The toolbar contains a buttons the user can use to do
 * measurements actions on the map.
 * </p>
 * 
 * @author Frank Wynants
 */
public class ToolbarMeasureAreaLocationSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "ToolbarMeasure with area/location";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new ToolbarMeasureAreaLocationSample();
		}
	};


	public ToolbarMeasureAreaLocationSample() {
		ToolbarRegistry.put(ToolId.TOOL_MEASURE_DISTANCE_MODE, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				MeasureModalAction measureModalAction = new MeasureModalAction(mapWidget);
				measureModalAction.configure(MeasureModalAction.SHOW_AREA, "true");
				measureModalAction.configure(MeasureModalAction.SHOW_COORDINATE, "true");
				return measureModalAction;
			}
		});
	}

	/**
	 * @return The viewPanel Canvas
	 */
	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		// Map with ID osmMeasureMap is defined in the XML configuration. (mapOsmMeasure.xml)
		final MapWidget map = new MapWidget("mapOsmMeasure", "gwtExample");

		final Toolbar toolbar = new Toolbar(map, WidgetLayout.toolbarLargeButtonSize);

		layout.addMember(toolbar);
		layout.addMember(map);

		return layout;
	}

	public String getDescription() {
		return MESSAGES.toolbarMeasureAreaLocationDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] {
				"classpath:org/geomajas/gwt/example/context/mapMeasureAreaLocation.xml",
				"classpath:org/geomajas/gwt/example/base/layerOsm.xml",
				"classpath:org/geomajas/gwt/example/base/tools.xml"};
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
