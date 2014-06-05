package org.geomajas.gwt.client.controller;

import java.util.Map;

import org.geomajas.geometry.Coordinate;
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
import org.geomajas.gwt.client.spatial.geometry.Geometry;

/**
 * Contextual information about a measurement.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface MeasureDistanceContext {

	/**
	 * Get the geometry of the measuring line (not including the last segment).
	 * 
	 * @return geometry
	 */
	Geometry getGeometry();

	/**
	 * Get the previous distance measured.
	 * 
	 * @return previous distance
	 */
	double getPreviousDistance();

	/**
	 * Get the current distance measured.
	 * 
	 * @return current distance
	 */
	double getCurrentDistance();

	/**
	 * Get the previous area measured.
	 * 
	 * @return previous area
	 */
	double getPreviousArea();

	/**
	 * Get the current area measured.
	 * 
	 * @return current area
	 */
	double getCurrentArea();

	/**
	 * Get the distance of the last segment.
	 * 
	 * @return radius
	 */
	double getRadius();

	/**
	 * Get the previous coordinate
	 * 
	 * @return the previous coordinate
	 */
	Coordinate getPreviousCoordinate();

	/**
	 * Get the current coordinate
	 * 
	 * @return the current coordinate
	 */
	Coordinate getCurrentCoordinate();

}
