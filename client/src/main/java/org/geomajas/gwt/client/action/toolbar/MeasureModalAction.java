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

package org.geomajas.gwt.client.action.toolbar;

import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarModalAction;
import org.geomajas.gwt.client.controller.MeasureDistanceController;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Measure action. It is possible to configure whether to show:
 * 	-Total distance
 * 	-Last distance (the distance of the last line)
 * 	-Total area
 * 	-Current X/Y coordinate (map CRS)
 * 
 * @author Pieter De Graef
 * @author Oliver May
 */
public class MeasureModalAction extends ToolbarModalAction implements ConfigurableAction {

	/**
	 * Configuration key to configure whether the total area should be displayed.
	 */
	public static final String SHOW_AREA = "showArea";

	/**
	 * Configuration key to configure whether the current coordinate should be displayed.
	 */
	public static final String SHOW_COORDINATE = "showCoordinate";

	private boolean showArea;

	private boolean showCoordinate;

	private MapWidget mapWidget;

	private MeasureDistanceController controller;

	public MeasureModalAction(MapWidget mapWidget) {
		super(WidgetLayout.iconMeasureLength, I18nProvider.getToolbar().measureSelectTitle(),
				I18nProvider.getToolbar().measureSelectTooltip());
		this.mapWidget = mapWidget;
		setController(new MeasureDistanceController(mapWidget, showArea, showCoordinate));
	}

	public void setController(MeasureDistanceController controller) {
		this.controller = controller;
	}

	public MeasureDistanceController getController() {
		return controller;
	}

	@Override
	public void onSelect(ClickEvent event) {
		mapWidget.setController(controller);
	}

	@Override
	public void onDeselect(ClickEvent event) {
		mapWidget.setController(null);
	}

	@Override
	public void configure(String key, String value) {
		if (SHOW_AREA.equals(key)) {
			showArea = Boolean.parseBoolean(value);
		} else if (SHOW_COORDINATE.equals(key)) {
			showCoordinate = Boolean.parseBoolean(value);
		}
	}
}
