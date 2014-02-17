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

package org.geomajas.plugin.editing.jsapi.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.jsapi.client.event.JsEvent;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Event that reports invalid geometry state caused by intersection of segments, polygon holes out of the boundary,
 * etc...
 * 
 * @author Jan De Moerloose
 * @since 1.15.0
 */
@Api(allMethods = true)
@Export
@ExportPackage("org.geomajas.plugin.editing.event")
public class GeometryEditValidationEvent extends JsEvent<GeometryEditValidationHandler> implements Exportable {

	private Geometry geometry;

	private GeometryIndex index;

	private int code;

	/**
	 * Main constructor.
	 * 
	 * @param geometry geometry
	 * @param index index of operation
	 */
	public GeometryEditValidationEvent(Geometry geometry, GeometryIndex index, int code) {
		this.geometry = geometry;
		this.index = index;
		this.code = code;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<GeometryEditValidationHandler> getType() {
		return GeometryEditValidationHandler.class;
	}

	protected void dispatch(GeometryEditValidationHandler handler) {
		handler.onGeometryEditValidation(this);
	}

	/**
	 * Returns the geometry (before the invalid operation).
	 * 
	 * @return
	 */
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * Get the operation index that caused the invalid state.
	 * 
	 * @return
	 */
	public GeometryIndex getIndex() {
		return index;
	}

	/**
	 * Get the validation state code.
	 * 
	 * <ul>
	 * <li>0 : valid
	 * <li>1 : hole outside shell
	 * <li>2 : nested holes
	 * <li>3 : disconnected interior
	 * <li>4 : self intersection
	 * <li>5 : ring self intersection
	 * <li>6 : nested shells
	 * <li>7 : duplicate rings
	 * <li>8 : too few points
	 * <li>9 : invalid coordinate
	 * <li>10 : ring not closed
	 * </ul>
	 * 
	 * @return
	 * @see org.geomajas.geometry.service.GeometryValidationState
	 */
	public int getCode() {
		return code;
	}

}