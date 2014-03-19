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
package org.geomajas.plugin.deskmanager.client.gwt.manager.editor;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientVectorLayerInfo;


/**
 * Extension of {@link LayerWidgetEditor}, for vector layers specifically.
 * Additional functionality: provide the widget editor with the clientVectorLayerInfo.
 * 
 * @author Jan Venstermans
 * @since 1.15.0
 */
@Api (allMethods = true)
public interface VectorLayerWidgetEditor extends LayerWidgetEditor {

	/**
	 * Set the {@link ClientVectorLayerInfo} of the vector layer.
	 * @param clientVectorLayerInfo
	 */
	void setClientVectorLayerInfo(ClientVectorLayerInfo clientVectorLayerInfo);
}
