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

package org.geomajas.plugin.editing.jsapi.example.client;

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point and main class for GWT application. This class defines the layout and functionality of this application.
 * 
 * @author Pieter De Graef
 */
public class Showcase implements EntryPoint {

	public void onModuleLoad() {
		ExporterUtil.exportAll();
	}

	
}