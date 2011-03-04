/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.configuration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.geomajas.geometry.Bbox;
import org.geomajas.global.Api;
import org.geomajas.layer.LayerType;

/**
 * Layer configuration info.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class LayerInfo implements Serializable {

	private static final long serialVersionUID = 151L;
	@NotNull
	private LayerType layerType;
	@NotNull
	private String crs;
	private Bbox maxExtent = Bbox.ALL;
	private Map<String, Object> extraInfo = new HashMap<String, Object>();

	/**
	 * Get layer type, indicating whether it is a raster layer or which type of geometries which are contained in the
	 * layer if it is a vector layer.
	 *
	 * @return layer type
	 */
	public LayerType getLayerType() {
		return layerType;
	}

	/**
	 * Set layer type. This indicates either that it is a raster layer, or the type of geometry when it is a vector
	 * layer.
	 *
	 * @param layerType layer type
	 */
	public void setLayerType(LayerType layerType) {
		this.layerType = layerType;
	}

	/**
	 * Get the CRS code which is used for expressing the coordinated in this layer.
	 *
	 * @return CRS code for this layer
	 */
	public String getCrs() {
		return crs;
	}

	/**
	 * Set the CRS code which is used for expressing the coordinated in this layer.
	 *
	 * @param crs CRS code for this layer
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}

	/**
	 * Get the maximum extent for the layer, the bounding box for the visible/usable area for this layer.
	 * <p/>
	 * Note that this may need to be limited some more based on security configuration.
	 *
	 * @return maximum extent for layer
	 */
	public Bbox getMaxExtent() {
		return maxExtent;
	}

	/**
	 * Set the maximum extent for the layer, the bounding box for the visible/usable area for this layer.
	 * <p/>
	 * Note that this is the largest area for any user. It may be limited based on logged in user.
	 *
	 * @param maxExtent maximum extent for layer
	 */
	public void setMaxExtent(Bbox maxExtent) {
		this.maxExtent = maxExtent;
	}

	/**
	 * Get extra configuration for the plug-ins. It is expected that the class name is used as key.
	 *
	 * @return extra configuration
	 * @since 1.9.0
	 */
	public Map<String, Object> getExtraInfo() {
		return extraInfo;
	}

	/**
	 * Set the extra configuration for the plug-ins. It is expected that the class name is used as key.
	 *
	 * @param extraInfo extra configuration
	 * @since 1.9.0
	 */
	public void setExtraInfo(Map<String, Object> extraInfo) {
		this.extraInfo = extraInfo;
	}

}
