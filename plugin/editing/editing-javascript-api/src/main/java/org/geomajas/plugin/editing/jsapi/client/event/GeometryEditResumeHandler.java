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

package org.geomajas.plugin.editing.jsapi.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.plugin.jsapi.client.event.JsHandler;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.Exportable;

/**
 * Handler for catching edit resume events.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Export
@ExportClosure
@Api(allMethods = true)
@UserImplemented
public interface GeometryEditResumeHandler extends JsHandler, Exportable {

	/**
	 * Executed when the editing process has resumed.
	 * 
	 * @param event
	 *            The geometry edit resume event.
	 */
	void onGeometryEditResume(GeometryEditResumeEvent event);
}