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

package org.geomajas.plugin.jsapi.client.map.controller.measuredistance;

import org.geomajas.annotation.Api;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * This object is returned to the handler when the measure distance controller is active.
 * 
 * @author Jan De Moerloose
 * @since 1.15.0
 */
@Api(allMethods = true)
@Export
@ExportPackage("org.geomajas.jsapi.map.controller")
public class MeasureDistanceInfo implements Exportable {
	
	public static final int STARTED = 0;
	
	public static final int BUSY = 1;
	
	public static final int STOPPED = 2;
	
	private int status;

	private double totalDistance;

	private double lastSegment;

	public MeasureDistanceInfo(int status, double totalDistance, double lastSegment) {
		this.status = status;
		this.totalDistance = totalDistance;
		this.lastSegment = lastSegment;
	}

	public int getStatus() {
		return status;
	}

	public double getTotalDistance() {
		return totalDistance;
	}

	public double getLastSegment() {
		return lastSegment;
	}

}