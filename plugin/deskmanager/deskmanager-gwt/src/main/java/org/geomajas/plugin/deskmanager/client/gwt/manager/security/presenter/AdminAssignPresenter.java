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

import org.geomajas.plugin.deskmanager.client.gwt.manager.security.model.UserService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.AdminAssignView;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.Manager;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of
 * {@link org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter.AdminAssignPresenter}.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public class AdminAssignPresenter extends AbstractEditablePresenter
		implements AdminAssignHandler, MainTabHandler {

	private AdminAssignView view;

	private UserService userService;

	/**
	 * This list is used for creating view data. It is not used for sending data to service on save.
	 */
	private List<UserDto> currentAdmins;

	private List<UserDto> addedUsers = new ArrayList<UserDto>();

	private List<UserDto> removedUsers = new ArrayList<UserDto>();

	private List<UserDto> users = new ArrayList<UserDto>();

	public AdminAssignPresenter() {
		this(Manager.getUsersManagementViews().getAdminsView(), Manager.getUserService());
	}

	public AdminAssignPresenter(AdminAssignView view, UserService userService) {
		super(view);
		this.userService = userService;
		this.view = view;
		view.setHandler(this);
		view.setEnabled(false);
		setEditable(false);
	}

	@Override
	public void onSave() {
		super.onSave();
		if (addedUsers.size() > 0 || removedUsers.size() > 0) {
			userService.updateAdmins(new ArrayList<UserDto>(addedUsers),
					new ArrayList<UserDto>(removedUsers),
					new DataCallback<List<UserDto>>() {

				@Override
				public void execute(List<UserDto> result) {
					setUsers(result);
					loadAdminUsersToView();
				}
			});
			addedUsers.clear();
			removedUsers.clear();
		}
	}

	@Override
	public void onCancel() {
		super.onCancel();
		for (UserDto removeAdmin : removedUsers) {
			currentAdmins.add(removeAdmin);
		}
		removedUsers.clear();
		for (UserDto addAdmin : addedUsers) {
			if (currentAdmins.contains(addAdmin)) {
				currentAdmins.remove(addAdmin);
			}
		}
		addedUsers.clear();
	}

	@Override
	public void onAddAdminRole(List<UserDto> selectedUsers) {
		for (UserDto userDto : selectedUsers) {
			if (!currentAdmins.contains(userDto)) {
				currentAdmins.add(userDto);
				// possibly remove from removedUsers, else add to addedUsers.
				if (removedUsers.contains(userDto)) {
					removedUsers.remove(userDto);
				} else {
					addedUsers.add(userDto);
				}
			}
		}
		loadAdminUsersToView();
	}

	@Override
	public void onRemoveAdminRole(List<UserDto> selectedUsers) {
		for (UserDto userDto : selectedUsers) {
			if (currentAdmins.contains(userDto)) {
				currentAdmins.remove(userDto);
				// possibly remove from addedUsers, else add to removedUsers.
				if (addedUsers.contains(userDto)) {
					addedUsers.remove(userDto);
				} else {
					removedUsers.add(userDto);
				}
			}
		}
		loadAdminUsersToView();
	}

	public void setUsers(List<UserDto> users) {
		if (users != null) {
			this.users = users;
		} else {
			this.users.clear();
		}
		currentAdmins = new ArrayList<UserDto>();
		for (UserDto user : users) {
			if (isAdmin(user)) {
				currentAdmins.add(user);
			}
		}
	}

	@Override
	public void loadAndShow() {
		currentAdmins = null;
		addedUsers.clear();
		removedUsers.clear();
		view.clearValues();
		userService.getUsers(true, new DataCallback<List<UserDto>>() {
			@Override
			public void execute(List<UserDto> result) {
				setUsers(result);
				view.setEnabled(true);
				loadAdminUsersToView();
			}
		});
	}

	// ----------------------------------------
	// private methods
	// ----------------------------------------

	private void loadAdminUsersToView() {
		List<UserDto> nonAdmins = new ArrayList<UserDto>();
		List<UserDto> admins = new ArrayList<UserDto>();
		for (UserDto userDto : users) {
			if (currentAdmins.contains(userDto))  {
				admins.add(userDto);
			} else {
				nonAdmins.add(userDto);
			}
		}
		view.setAdminUsers(admins, nonAdmins);
	}

	private boolean isAdmin(UserDto userDto) {
		for (ProfileDto profileDto : userDto.getProfiles()) {
			if (profileDto.getRole().equals(Role.ADMINISTRATOR)) {
				return true;
			}
		}
		return false;
	}
}
