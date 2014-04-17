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
package org.geomajas.plugin.deskmanager.client.gwt.geodesk.css;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt.client.util.css.CssLoaderUtil;

/**
 * @author Dosi Bingov
 *         <p/>
 *         Implementation of {@link org.geomajas.gwt.client.util.css.CssLoaderUtil}.
 */
public class CssLoaderGeodeskImpl implements CssLoaderGeodesk {
	private static final String STYLE_SHEET_PATH = GWT.getModuleBaseURL() + "css/gm-gwt-deskmanager.css";

	@Override
	public void loadStyleSheet() {
		CssLoaderUtil.loadStyleSheet(STYLE_SHEET_PATH);
	}

	public static void load() {
		CssLoaderGeodesk loader = new GWT().create(CssLoaderGeodesk.class);
		loader.loadStyleSheet();
	}
}
