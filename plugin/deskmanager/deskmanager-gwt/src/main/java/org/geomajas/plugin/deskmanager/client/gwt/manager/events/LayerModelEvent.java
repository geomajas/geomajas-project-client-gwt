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
package org.geomajas.plugin.deskmanager.client.gwt.manager.events;

import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public class LayerModelEvent {

	private final LayerModelDto layerModel;

	private final boolean deleted;

	private final boolean newInstance;

	public LayerModelEvent(LayerModelDto layerModel) {
		this.layerModel = layerModel;
		this.deleted = false;
		this.newInstance = false;
	}

	public LayerModelEvent(LayerModelDto layerModel, boolean deleted, boolean newInstance) {
		this.layerModel = layerModel;
		this.deleted = deleted;
		this.newInstance = newInstance;
	}

	public LayerModelDto getLayerModel() {
		return layerModel;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public boolean isNewInstance() {
		return newInstance;
	}
}
