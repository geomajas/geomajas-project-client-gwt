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


import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

/**
 * Interface with getters for the different views.
 *
 * @author Jan De Moerloose
 */
public interface UserManagementViews {

	UsersAndGroupsView getUsersAndGroupsView();

	GroupsView getGroupsView();

	UsersView getUsersView();

	AdminAssignView getAdminsView();

	UserDetailView getUserDetailView();

	GroupDetailView getGroupDetailView();

	RoleSelectAssignView<TerritoryDto, UserDto> getGroupAssignView();

	RoleSelectAssignView<UserDto, TerritoryDto> getUserAssignView();
}
