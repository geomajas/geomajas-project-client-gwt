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

package org.geomajas.gwt.example.client.sample.mapwidget;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Slider;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.gfx.paintable.mapaddon.CanvasMapAddon;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.gwt.example.client.sample.i18n.SampleMessages;

/**
 * <p>
 * Sample to display how to register {@link org.geomajas.gwt.client.gfx.paintable.mapaddon.CanvasMapAddon}
 * instances to the map.
 * </p>
 * 
 * @author Jan Venstermans
 */
public class CanvasMapAddonSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "ButtonMapResize";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new CanvasMapAddonSample();
		}
	};

	public Canvas getViewPanel() {
		// create map
		final MapWidget map = new MapWidget("mapPipeline", "gwtExample");
		map.setController(new PanController(map));

		// create button and place as a MapAddon on the map, top right
		IButton editorAddButton = new IButton("A button");
		CanvasMapAddon buttonMapAddon = new CanvasMapAddon("exampleButton", editorAddButton, map);
		buttonMapAddon.setAlignment(Alignment.RIGHT);
		buttonMapAddon.setVerticalAlignment(VerticalAlignment.TOP);
		buttonMapAddon.setHorizontalOffset(100);
		buttonMapAddon.setVerticalOffset(50);
		map.registerMapAddon(buttonMapAddon);

		// create slider and place as a MapAddon on the map, right bottom
		final Slider slider = new Slider("A Slider");
		slider.setVertical(false);
		slider.setMinValue(1.0);
		slider.setMaxValue(5.0);
		slider.setNumValues(5);
		CanvasMapAddon calendarMapAddon = new CanvasMapAddon("slider", slider, map);
		calendarMapAddon.setAlignment(Alignment.RIGHT);
		calendarMapAddon.setVerticalAlignment(VerticalAlignment.BOTTOM);
		calendarMapAddon.setHorizontalOffset(20);
		calendarMapAddon.setVerticalOffset(20);
		map.registerMapAddon(calendarMapAddon);

		VLayout mapLayout = new VLayout();
		setWidth100();
		setHeight100();
		mapLayout.setShowEdges(true);
		mapLayout.addMember(map);
		return mapLayout;
	}

	public String getDescription() {
		return MESSAGES.canvasMapAddonDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { /*"classpath:org/geomajas/gwt/example/context/mapPipeline.xml",
				"classpath:org/geomajas/gwt/example/context/layerPipeline.xml",
				"classpath:org/geomajas/gwt/example/server/samples/ProcessFeaturesPipelineStep.java",
				"classpath:org/geomajas/gwt/example/base/layerPopulatedPlaces110m.xml",
				"classpath:org/geomajas/gwt/example/base/layerWmsBluemarble.xml"*/ };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
