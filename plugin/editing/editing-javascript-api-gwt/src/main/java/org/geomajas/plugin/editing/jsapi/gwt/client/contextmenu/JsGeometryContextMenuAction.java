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
package org.geomajas.plugin.editing.jsapi.gwt.client.contextmenu;

import org.geomajas.annotation.Api;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Exports {@link org.geomajas.plugin.editing.gwt.client.contextmenu.GeometryContextMenuAction} as a closure. Pass a
 * function and receive a context to base the action upon.
 * 
 * @author Jan De Moerloose
 * @since 1.15.0
 * 
 */
@Export
@ExportPackage("org.geomajas.plugin.editing.contextmenu")
@ExportClosure
@Api(allMethods = true)
public interface JsGeometryContextMenuAction extends Exportable {

	/**
	 * Execute the action.
	 * 
	 * @param context
	 */
	void execute(JsGeometryContextMenuRegistry context);
}