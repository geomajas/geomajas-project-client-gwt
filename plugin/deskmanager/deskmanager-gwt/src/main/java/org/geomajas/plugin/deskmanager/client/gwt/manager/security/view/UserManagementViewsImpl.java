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
 * Default implementation of {@link UserManagementViews}.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public class UserManagementViewsImpl implements UserManagementViews {

	private UsersAndGroupsView usersAndGroupsView;

	private GroupsView groupsView;

	private UsersView usersView;

	private AdminAssignView adminsView;

	private GroupDetailView groupDetailView;

	private UserDetailView userDetailView;

	private RoleSelectAssignView groupAssignView;

	private RoleSelectAssignView<UserDto, TerritoryDto> userAssignView;

	@Override
	public UsersAndGroupsView getUsersAndGroupsView() {
		if (usersAndGroupsView == null) {
			usersAndGroupsView = new UsersAndGroups();
		}
		return usersAndGroupsView;
	}

	@Override
	public GroupsView getGroupsView() {
		if (groupsView == null) {
			groupsView = new GroupGrid();
		}
		return groupsView;
	}

	@Override
	public UsersView getUsersView() {
		if (usersView == null) {
			usersView = new UserGrid();
		}
		return usersView;
	}

	@Override
	public AdminAssignView getAdminsView() {
		if (adminsView == null) {
			adminsView = new AdminAssign();
		}
		return adminsView;
	}

	@Override
	public UserDetailView getUserDetailView() {
		if (userDetailView == null) {
			userDetailView = new UserDetail();
		}
		return userDetailView;
	}

	@Override
	public GroupDetailView getGroupDetailView() {
		if (groupDetailView == null) {
			groupDetailView = new GroupDetail();
		}
		return groupDetailView;
	}

	@Override
	public RoleSelectAssignView<TerritoryDto, UserDto> getGroupAssignView() {
		if (groupAssignView == null) {
			groupAssignView = new GroupAssign();
		}
		return groupAssignView;
	}

	@Override
	public RoleSelectAssignView<UserDto, TerritoryDto> getUserAssignView() {
		if (userAssignView == null) {
			userAssignView = new UserAssign();
		}
		return userAssignView;
	}

	public void setUsersAndGroupsView(UsersAndGroupsView usersAndGroupsView) {
		this.usersAndGroupsView = usersAndGroupsView;
	}

	public void setGroupsView(GroupsView groupsView) {
		this.groupsView = groupsView;
	}

	public void setUsersView(UsersView usersView) {
		this.usersView = usersView;
	}

	public void setAdminsView(AdminAssignView adminsView) {
		this.adminsView = adminsView;
	}

	public void setGroupDetailView(GroupDetailView groupDetailView) {
		this.groupDetailView = groupDetailView;
	}

	public void setUserDetailView(UserDetailView userDetailView) {
		this.userDetailView = userDetailView;
	}

	public void setGroupAssignView(RoleSelectAssignView<TerritoryDto, UserDto> groupAssignView) {
		this.groupAssignView = groupAssignView;
	}

	public void setUserAssignView(RoleSelectAssignView<UserDto, TerritoryDto> userAssignView) {
		this.userAssignView = userAssignView;
	}
}