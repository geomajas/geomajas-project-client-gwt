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
package org.geomajas.plugin.rasterizing.api;

import java.io.OutputStream;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.rasterizing.dto.LegendRasterizingInfo;

/**
 * Service to render maps and legends as images.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface ImageService {

	/**
	 * Writes a map to the specified output stream.
	 * 
	 * @param stream
	 *            output stream
	 * @param clientMapInfo
	 *            metadata of the map
	 * @throws GeomajasException
	 *             thrown when the stream could not be written
	 */
	void writeMap(OutputStream stream, ClientMapInfo clientMapInfo) throws GeomajasException;

	/**
	 * Writes a legend to the specified output stream.
	 * 
	 * @param stream
	 *            output stream
	 * @param legendRasterizingInfo
	 *            metadata of the legend
	 * @throws GeomajasException
	 *             thrown when the stream could not be written
	 */
	void writeLegend(OutputStream stream, LegendRasterizingInfo legendRasterizingInfo) throws GeomajasException;

}
