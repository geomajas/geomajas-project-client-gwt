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

package org.geomajas.plugin.jsapi.client.map.controller.measuredistance;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.controller.Controller;
import org.geomajas.plugin.jsapi.client.map.Map;
import org.geomajas.plugin.jsapi.client.map.controller.MapController;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Adds an extra callback for {@link MeasureDistanceController}.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api
@Export
@ExportPackage("org.geomajas.jsapi.map.controller")
public class MeasureDistanceInfoController extends MapController implements Exportable {

	public MeasureDistanceInfoController(Map map, Controller controller) {
		super(map, controller);
	}

	public void setInfoHandler(final MeasureDistanceHandler infoHandler) {
		
	}

}
