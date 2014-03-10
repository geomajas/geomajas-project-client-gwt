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
package org.geomajas.gwt.client.util;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.gfx.paintable.Image;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.spatial.Bbox;

/**
 * Util class for creating {@link Image}s and reducing the lines of code needed for one {@link Image}.
 * 
 * @author Emiel Ackermann
 * @since 1.10.0
 */
@Api
public final class ImageUtil {
	
	private ImageUtil() {
	}
	
	/**
	 * Creates an {@link Image} with full opacity (new PictureStyle(1)).
	 * horMargin, verMargin, width and height are used to create the {@link Bbox}
	 * @param id is used to create the actual {@link Image} (new Image(id)).
	 * @param url
	 * @param horMargin 
	 * @param verMargin
	 * @param width
	 * @param height
	 * @return
	 */
	public static Image createRectangleImage(String id, String url, 
			double horMargin, double verMargin, double width, double height) {
		Image i = new Image(id);
		i.setHref(url);
		i.setStyle(new PictureStyle(1));
		i.setBounds(new Bbox(horMargin, verMargin, width, height));
		return i;
	}
	
	/**
	 * Creates an {@link Image} with full opacity (new PictureStyle(1)).
	 * horMargin, verMargin and diameter are used to create the {@link Bbox}
	 * @param id is used to create the actual {@link Image} (new Image(id)).
	 * @param url
	 * @param horMargin 
	 * @param verMargin
	 * @param width
	 * @param height
	 * @return
	 */
	public static Image createSquareImage(String id, String location, double horMargin, double verMargin, double d) {
		return createRectangleImage(id, location, horMargin, verMargin, d, d);
	}
}
