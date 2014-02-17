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
import org.geomajas.annotation.UserImplemented;
import org.geomajas.plugin.jsapi.client.event.JsHandler;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.Exportable;

/**
 * Interface for handling validation events. This will allow to notify users on validation errors during editing.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Export
@ExportClosure
@Api(allMethods = true)
@UserImplemented
public interface GeometryEditValidationHandler extends JsHandler, Exportable {

	/**
	 * Executed during geometry operation when the operation results in an invalid geometry (for validating editing
	 * services). The operation is by default reverted, so no need to do anything except notifying the user in some way.
	 * 
	 * @param event The geometry validation event.
	 */
	void onGeometryEditValidation(GeometryEditValidationEvent event);
}