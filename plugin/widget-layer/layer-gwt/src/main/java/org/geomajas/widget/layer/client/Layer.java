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
package org.geomajas.widget.layer.client;

import com.google.gwt.core.client.EntryPoint;
import org.geomajas.widget.layer.client.view.ViewManager;
import org.geomajas.widget.layer.client.view.ViewManagerImpl;


/**
 * Main entrypoint for the layertree plugin.
 * 
 * @author Oliver May
 *
 */
public class Layer implements EntryPoint {

	private static ViewManager viewManager;

	/* (non-Javadoc)
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	public void onModuleLoad() {
	}

	public static ViewManager getViewManager() {
		if (viewManager == null) {
			viewManager = new ViewManagerImpl();
		}
		return viewManager;
	}

	public static void setViewManager(ViewManager viewManager) {
		Layer.viewManager = viewManager;
	}
}
