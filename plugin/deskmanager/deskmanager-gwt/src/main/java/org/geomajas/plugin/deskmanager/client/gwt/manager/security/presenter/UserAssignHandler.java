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
package org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter;

import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

/**
 * Handler for assigning roles to the selected user.
 * This handler is used in the Users section.
 *
 * @author Jan De Moerloose
 */
public interface UserAssignHandler extends EditableHandler {

	void onAdd(Role role, TerritoryDto group);

	void onRemove(Role role, TerritoryDto group);

	void onSelectRole(Role role);

	void setCurrentUser(UserDto user);

	void loadGroups();

}
