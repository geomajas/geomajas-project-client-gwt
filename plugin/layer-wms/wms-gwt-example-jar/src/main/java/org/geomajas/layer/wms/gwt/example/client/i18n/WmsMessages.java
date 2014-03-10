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

package org.geomajas.layer.wms.gwt.example.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * Messages for the wms example panels.
 *
 * @author Jan De Moerloose
 */
public interface WmsMessages extends Messages {

	/** @return title for the layers tree node. */
	String treeGroupLayers();

	/** @return title for the google panel. */
	String wmsTitle();

	/** @return description for the google panel. */
	String wmsDescription();

}
