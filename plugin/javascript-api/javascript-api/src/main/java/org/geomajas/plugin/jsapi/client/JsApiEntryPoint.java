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
package org.geomajas.plugin.jsapi.client;

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.EntryPoint;

/**
 * GWT entry point for the general JavaScript API.
 * 
 * @author Pieter De Graef
 */
public class JsApiEntryPoint implements EntryPoint {

	public void onModuleLoad() {
		ExporterUtil.exportAll();
		onLoad();
	}

	private native void onLoad()
	/*-{
		if ($wnd.onGeomajasLoad && typeof $wnd.onGeomajasLoad == 'function') {
			$wnd.onGeomajasLoad();
		}
	}-*/;
}