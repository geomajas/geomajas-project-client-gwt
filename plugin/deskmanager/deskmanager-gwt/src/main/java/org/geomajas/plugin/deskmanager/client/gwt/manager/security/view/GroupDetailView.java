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
package org.geomajas.plugin.deskmanager.client.gwt.manager.security.view;

import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;

/**
 * Extension of {@link DetailView} for {@link TerritoryDto}.
 * Also
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public interface GroupDetailView extends DetailView<TerritoryDto> {

	String getMapCrs();

	void setMapEditingButtonsEnabled(boolean editing);

	void setImportedGeometry(Geometry importedPolygon);
}
