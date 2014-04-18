/*
* Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
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

