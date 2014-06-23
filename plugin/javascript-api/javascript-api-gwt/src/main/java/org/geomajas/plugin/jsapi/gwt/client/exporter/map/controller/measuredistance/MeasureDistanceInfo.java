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
package org.geomajas.plugin.jsapi.gwt.client.exporter.map.controller.measuredistance;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.MeasureDistanceContext;
import org.geomajas.gwt.client.controller.MeasureDistanceHandler.State;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Info object for {@link MeasureDistanceInfoControllerImpl}.
 * 
 * @author Jan De Moerloose
 * @since 1.15.0
 * 
 */
@Api
@Export
@ExportPackage("org.geomajas.jsapi.map.controller")
public class MeasureDistanceInfo implements MeasureDistanceContext, Exportable {

	private State state;

	private MeasureDistanceContext context;

	/**
	 * Constructor for GWT exporter.
	 */
	public MeasureDistanceInfo() {

	}

	/**
	 * Create an info for the current state and context.
	 * 
	 * @param state
	 * @param context
	 */
	public MeasureDistanceInfo(State state, MeasureDistanceContext context) {
		this.state = state;
		this.context = context;
	}

	@Override
	public Geometry getGeometry() {
		return context.getGeometry();
	}

	@Override
	public double getPreviousDistance() {
		return context.getPreviousDistance();
	}

	@Override
	public double getCurrentDistance() {
		return context.getCurrentDistance();
	}

	@Override
	public double getPreviousArea() {
		return context.getPreviousArea();
	}

	@Override
	public double getCurrentArea() {
		return context.getCurrentArea();
	}

	@Override
	public double getRadius() {
		return context.getRadius();
	}

	@Override
	public Coordinate getPreviousCoordinate() {
		return context.getPreviousCoordinate();
	}

	@Override
	public Coordinate getCurrentCoordinate() {
		return context.getCurrentCoordinate();
	}

	/**
	 * Get the state of the controller.
	 * @see {@link MeasureDistanceHandler.State}
	 * 
	 * @return
	 */
	public String getState() {
		return state.name();
	}

}
