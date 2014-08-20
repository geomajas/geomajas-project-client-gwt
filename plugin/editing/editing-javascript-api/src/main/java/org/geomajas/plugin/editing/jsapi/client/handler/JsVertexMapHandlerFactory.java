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
package org.geomajas.plugin.editing.jsapi.client.handler;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.editing.client.handler.VertexMapHandlerFactory;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * JavaScript wrapper of {@link VertexMapHandlerFactory}. This factory creates a singleton object.
 * 
 * @author Jan De Moerloose
 * @since 1.15.0
 * 
 */
@Api
@Export("VertexHandlerFactory")
@ExportPackage("org.geomajas.plugin.editing.handler")
public class JsVertexMapHandlerFactory extends JsGeometryIndexMapHandlerFactory implements VertexMapHandlerFactory,
		Exportable {

	public JsVertexMapHandlerFactory() {

	}

}
