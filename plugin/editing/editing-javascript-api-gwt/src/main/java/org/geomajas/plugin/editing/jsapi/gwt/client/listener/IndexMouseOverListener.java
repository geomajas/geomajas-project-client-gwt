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
package org.geomajas.plugin.editing.jsapi.gwt.client.listener;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * 
 * Closure that returns an HTML fragment with information about the geometry.
 * 
 * @since 1.0.0
 * @author Jan De Moerloose
 * 
 */
@Export
@ExportPackage("org.geomajas.plugin.editing.listener")
@ExportClosure
@Api(allMethods = true)
public interface IndexMouseOverListener extends Exportable {

	/**
	 * Get the HTML content of the informational panel.
	 * 
	 * @param geometryIndex the geometryIndex
	 */
	void onMouseOver(GeometryIndex geometryIndex);
}