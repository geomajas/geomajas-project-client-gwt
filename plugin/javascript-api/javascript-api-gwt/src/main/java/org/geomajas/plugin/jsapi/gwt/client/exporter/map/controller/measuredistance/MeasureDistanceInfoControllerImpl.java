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

package org.geomajas.plugin.jsapi.gwt.client.exporter.map.controller.measuredistance;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.controller.MeasureDistanceContext;
import org.geomajas.gwt.client.controller.MeasureDistanceController;
import org.geomajas.gwt.client.controller.MeasureDistanceHandler;
import org.geomajas.plugin.jsapi.client.map.Map;
import org.geomajas.plugin.jsapi.client.map.controller.InfoHandler;
import org.geomajas.plugin.jsapi.client.map.controller.MapController;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;

/**
 * SmartGWT implementation of {@link MeasureDistanceController}.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api
@Export
@ExportPackage("org.geomajas.jsapi.map.controller")
public class MeasureDistanceInfoControllerImpl extends MapController {

	private MeasureDistanceController controller;

	private InfoHandler infoHandler;

	public MeasureDistanceInfoControllerImpl() {

	}

	public MeasureDistanceInfoControllerImpl(Map map, MeasureDistanceController controller) {
		super(map, controller);
		this.controller = controller;
	}

	@Override
	public void setInfoHandler(InfoHandler infoHandler) {
		this.infoHandler = infoHandler;
		controller.clearHandlers();
		controller.addHandler(new MeasureDistanceHandlerImpl());
	}

	/**
	 * Passes through to {@link InfoHandler}.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	private class MeasureDistanceHandlerImpl implements MeasureDistanceHandler {

		@Override
		public void onChange(State state, MeasureDistanceContext context) {
			infoHandler.onInfo(new MeasureDistanceInfo(state, context));
		}
	}

}
