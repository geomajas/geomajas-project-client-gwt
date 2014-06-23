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
package org.geomajas.plugin.deskmanager.client.gwt.manager.security.model;

import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;

import java.util.List;

/**
 * Interface for CRUD of {@link org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto} objects.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public interface GroupService {

	/* get dto list groups */
	void getGroups(final DataCallback<List<TerritoryDto>> onFinish);

	/* CRUD group */
	void createGroup(String name, String key, String crs, Geometry geometry,
					 DataCallback<TerritoryDto> onFinish);

	void getGroup(long id, DataCallback<TerritoryDto> onFinish);

	void updateGroup(TerritoryDto group, final DataCallback<TerritoryDto> onFinish);

	void deleteGroup(long groupId, DataCallback<Boolean> onFinish);

	void getGeometryOfShpFile(String shpFileToken, String toCrs, DataCallback<Geometry> onFinish);
}
