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
import org.geomajas.plugin.deskmanager.domain.dto.LayerDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;


/**
 * Extention of {@link LayerWidgetEditor}, for vector layers specifically.
 * 
 * @author Jan Venstermans
 * @since 1.15.0
 */
@Api (allMethods = true)
public interface VectorLayerWidgetEditor extends LayerWidgetEditor {

	void setClientVectorLayerInfo(ClientVectorLayerInfo clientVectorLayerInfo);
}
