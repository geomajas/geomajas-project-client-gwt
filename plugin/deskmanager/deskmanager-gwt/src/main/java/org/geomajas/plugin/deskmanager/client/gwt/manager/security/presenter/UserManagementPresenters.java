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


/**
 * Interface with getters for the different linked presenters.
 * This interface is created for test purposes.
 *
 * @author Jan Venstermans
 */
public class UserManagementPresenters {

	private UsersAndGroupsPresenter usersAndGroupsPresenter;

	private GroupsPresenter groupsPresenter;

	private UsersPresenter usersPresenter;

	private AdminAssignPresenter adminAssignPresenter;

	private UserDetailPresenter userDetailPresenter;

	private GroupDetailPresenter groupDetailPresenter;

	private GroupAssignPresenter groupAssignPresenter;

	private UserAssignPresenter userAssignPresenter;

	public UsersAndGroupsPresenter getUsersAndGroupsPresenter() {
		if (usersAndGroupsPresenter == null) {
			usersAndGroupsPresenter = new UsersAndGroupsPresenter();
		}
		return usersAndGroupsPresenter;
	}

	public void setUsersAndGroupsPresenter(UsersAndGroupsPresenter usersAndGroupsPresenter) {
		this.usersAndGroupsPresenter = usersAndGroupsPresenter;
	}

	public GroupsPresenter getGroupsPresenter() {
		if (groupsPresenter == null) {
			groupsPresenter = new GroupsPresenter();
		}
		return groupsPresenter;
	}

	public void setGroupsPresenter(GroupsPresenter groupsPresenter) {
		this.groupsPresenter = groupsPresenter;
	}

	public UsersPresenter getUsersPresenter() {
		if (usersPresenter == null) {
			usersPresenter = new UsersPresenter();
		}
		return usersPresenter;
	}

	public void setUsersPresenter(UsersPresenter usersPresenter) {
		this.usersPresenter = usersPresenter;
	}

	public AdminAssignPresenter getAdminAssignPresenter() {
		if (adminAssignPresenter == null) {
			adminAssignPresenter = new AdminAssignPresenter();
		}
		return adminAssignPresenter;
	}

	public void setAdminAssignPresenter(AdminAssignPresenter adminAssignPresenter) {
		this.adminAssignPresenter = adminAssignPresenter;
	}

	public UserDetailPresenter getUserDetailPresenter() {
		if (userDetailPresenter == null) {
			userDetailPresenter = new UserDetailPresenter();
		}
		return userDetailPresenter;
	}

	public void setUserDetailPresenter(UserDetailPresenter userDetailPresenter) {
		this.userDetailPresenter = userDetailPresenter;
	}

	public GroupDetailPresenter getGroupDetailPresenter() {
		if (groupDetailPresenter == null) {
			groupDetailPresenter = new GroupDetailPresenter();
		}
		return groupDetailPresenter;
	}

	public void setGroupDetailPresenter(GroupDetailPresenter groupDetailPresenter) {
		this.groupDetailPresenter = groupDetailPresenter;
	}

	public GroupAssignPresenter getGroupAssignPresenter() {
		if (groupAssignPresenter == null) {
			groupAssignPresenter = new GroupAssignPresenter();
		}
		return groupAssignPresenter;
	}

	public void setGroupAssignPresenter(GroupAssignPresenter groupAssignPresenter) {
		this.groupAssignPresenter = groupAssignPresenter;
	}

	public UserAssignPresenter getUserAssignPresenter() {
		if (userAssignPresenter == null) {
			userAssignPresenter = new UserAssignPresenter();
		}
		return userAssignPresenter;
	}

	public void setUserAssignPresenter(UserAssignPresenter userAssignPresenter) {
		this.userAssignPresenter = userAssignPresenter;
	}
}
