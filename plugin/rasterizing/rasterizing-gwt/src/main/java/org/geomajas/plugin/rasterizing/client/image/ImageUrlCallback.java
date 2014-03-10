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
package org.geomajas.plugin.rasterizing.client.image;

import org.geomajas.annotation.Api;

/**
 * Callback invoked after image generation on the server.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 * 
 */
@Api(allMethods = true)
public interface ImageUrlCallback {

	/**
	 * Called when the images are generated.
	 * 
	 * @param mapUrl url to access the map image
	 * @param legendUrl url to access the legend image
	 */
	void onImageUrl(String mapUrl, String legendUrl);

}
