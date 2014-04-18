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

package org.geomajas.plugin.deskmanager.client.gwt.common.editor.loadingscreen;

import org.geomajas.configuration.client.ClientWidgetInfo;

/**
 * Loading screen image configuration object.
 *
 * @author Dosi Bingov
 *
 */
public class LoadingScreenImageInfo implements ClientWidgetInfo {
	
	private static final long serialVersionUID = 100L;

	public static final String IDENTIFIER = "loadingscreeninfo";

	private String imageUrl;

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}

