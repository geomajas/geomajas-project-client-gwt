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
package org.geomajas.plugin.jsapi.gwt.client;

import org.geomajas.plugin.jsapi.client.map.controller.measuredistance.MeasureDistanceInfoController;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * GWT entry point for the JavaScript API for the GWT face.
 * 
 * @author Pieter De Graef
 */
public class JsApiEntryPoint implements EntryPoint {

	public void onModuleLoad() {
		GWT.create(MeasureDistanceInfoController.class);
	}
}