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
package org.geomajas.plugin.jsapi.client.map.controller;

import org.geomajas.annotation.Api;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.Exportable;

/**
 * Handler for controllers that provide information to external handlers (versus showing the information on the map).
 * 
 * @author Jan De Moerloose
 * @since 1.15.0
 * 
 */
@Export
@ExportClosure
@Api(allMethods = true)
public interface InfoHandler extends Exportable {

	/**
	 * Inform the handler of the new information.
	 * 
	 * @param info the information object
	 */
	void onInfo(Exportable info);
}
