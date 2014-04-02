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
package org.geomajas.plugin.deskmanager.client.gwt.geodesk.impl;

import com.google.gwt.core.client.EntryPoint;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.css.CssLoaderGeodeskImpl;


/**
 * Entry point for the geodesks, this loads all necessary widgets. For starting up an actual geodesk application, see 
 * {@link org.geomajas.plugin.deskmanager.client.gwt.geodesk.GeodeskApplicationLoader}.
 * 
 * @author Oliver May
 *
 */
public class GeodeskEntryPoint implements EntryPoint {
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	public void onModuleLoad() {
		CssLoaderGeodeskImpl.load();
	}

}
