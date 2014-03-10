/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.jsapi.gwt.client.exporter.map.controller.measuredistance;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.controller.MeasureDistanceController;
import org.geomajas.gwt.client.controller.MeasureDistanceInfoHandler;
import org.geomajas.plugin.jsapi.client.map.Map;
import org.geomajas.plugin.jsapi.client.map.controller.measuredistance.MeasureDistanceInfoController;
import org.geomajas.plugin.jsapi.client.map.controller.measuredistance.MeasureDistanceHandler;
import org.geomajas.plugin.jsapi.client.map.controller.measuredistance.MeasureDistanceInfo;
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
public class MeasureDistanceInfoControllerImpl extends MeasureDistanceInfoController {

	private final MeasureDistanceController controller;

	public MeasureDistanceInfoControllerImpl(Map map, final MeasureDistanceController controller) {
		super(map, controller);
		this.controller = controller;
	}
	
	

	@Override
	@Export
	public void setInfoHandler(final MeasureDistanceHandler handler) {
		controller.setInfoHandler(new MeasureDistanceInfoHandler() {

			private double totalDistance;

			private double lastSegment;

			@Override
			public void onStop() {
				handler.onInfo(new MeasureDistanceInfo(MeasureDistanceInfo.STOPPED, totalDistance,
						lastSegment));
			}

			@Override
			public void onStart() {
				this.totalDistance = 0;
				this.lastSegment = 0;
				handler.onInfo(new MeasureDistanceInfo(MeasureDistanceInfo.STARTED, 0, 0));
			}

			@Override
			public void onDistance(double totalDistance, double lastSegment) {
				this.totalDistance = totalDistance;
				this.lastSegment = lastSegment;
				handler.onInfo(new MeasureDistanceInfo(MeasureDistanceInfo.BUSY, totalDistance, lastSegment));
			}
		});
	}

}
