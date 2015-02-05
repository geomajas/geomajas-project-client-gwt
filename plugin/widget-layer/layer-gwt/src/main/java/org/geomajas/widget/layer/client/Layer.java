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
package org.geomajas.widget.layer.client;

import com.google.gwt.core.client.EntryPoint;
import org.geomajas.widget.layer.client.view.ViewFactory;
import org.geomajas.widget.layer.client.view.ViewFactoryImpl;


/**
 * Main entrypoint for the layertree plugin.
 * 
 * @author Oliver May
 *
 */
public class Layer implements EntryPoint {

	private static ViewFactory viewFactory;

	/* (non-Javadoc)
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	public void onModuleLoad() {
	}

	public static ViewFactory getViewFactory() {
		if (viewFactory == null) {
			viewFactory = new ViewFactoryImpl();
		}
		return viewFactory;
	}

	public static void setViewFactory(ViewFactory viewFactory) {
		Layer.viewFactory = viewFactory;
	}
}
