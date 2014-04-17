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
package org.geomajas.widget.layer.client.util;

import org.geomajas.annotation.Api;


/**
 * @author Oliver May
 *
 * @author Dosi Bingov
 *
 * @since 1.0.0
 */
@Api
public final class GltLayout {

	private GltLayout() {  }
	
	// CHECKSTYLE VISIBILITY MODIFIER: OFF

	/*============================================================
	Margins
	============================================================*/
	public static int layerTreeButtonSize = 22;

	public static int layerTreeIconSize = 18;


	/*============================================================
	Icons
	============================================================*/

	/** Layer label overlay icon URL. */
	public static String layerLabelOverlayUrl = "[ISOMORPHIC]/geomajas/layerIcons/layer_label_overlay.png";

	/** Layer label opacity underlay icon URL. */
	public static String layerOpacityUnderlayUrl = "[ISOMORPHIC]/geomajas/layerIcons/"
			+ "layer_transparency_underlay.png";

	/** Raster layer large icon URL. */
	public static String layerRasterIconLargeUrl = "[ISOMORPHIC]/geomajas/layerIcons/raster_icon_large.png";

	/** Point layer large icon- URL. */
	public static String layerVectorPointIconLargeUrl = "[ISOMORPHIC]/geomajas/layerIcons/"
			+ "vector_point_icon_large.png";

	/** Line layer large icon- URL. */
	public static String layerVectorLineIconLargeUrl = "[ISOMORPHIC]/geomajas/layerIcons/"
			+ "vector_line_icon_large.png";

	/** Polygon layer large icon- URL. */
	public static String layerVectorPolygonIconLargeUrl = "[ISOMORPHIC]/geomajas/layerIcons/"
			+ "vector_polygon_icon_large.png";

	public static String iconShowLegend = "[ISOMORPHIC]/geomajas/silk/information.png";

	public static String iconRemoveFilter = "[SKIN]/actions/remove.png";

	public static String iconLayerSettings = "[ISOMORPHIC]/geomajas/silk/cog.png";

	/**
	 * The path where we put following layer tree icons.
	 *
	 * layer-show.png, layer-hide.png, layer-show-filtered.png, layer-show-labeled-filtered.png,
	 * layer-show-outofrange.png, layer-show-outofrange-filtered.png, layer-show-outofrange-labeled.png,
	 * layer-show-outofrange-labeled-filtered.png
	 */
	public static String layerTreeIconsPath = "[ISOMORPHIC]/geomajas/widget/layertree/";


	// CHECKSTYLE VISIBILITY MODIFIER: ON
	
}
