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

package org.geomajas.plugin.editing.jsapi.client.merge.event;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.jsapi.client.event.JsEvent;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Event that reports a geometry has been added to the list for merging.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@Export
@ExportPackage("org.geomajas.plugin.editing.merge.event")
public class GeometryMergeAddedEvent extends JsEvent<GeometryMergeAddedHandler> implements Exportable {

	private final Geometry geometry;

	/**
	 * Main constructor.
	 * 
	 * @param geometry geometry
	 */
	public GeometryMergeAddedEvent(Geometry geometry) {
		this.geometry = geometry;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<GeometryMergeAddedHandler> getType() {
		return GeometryMergeAddedHandler.class;
	}

	protected void dispatch(GeometryMergeAddedHandler handler) {
		handler.onGeometryMergeAdded(this);
	}

	/**
	 * Get the geometry that has been added to the list for merging.
	 * 
	 * @return The geometry that has been added to the list for merging.
	 */
	public Geometry getGeometry() {
		return geometry;
	}
}