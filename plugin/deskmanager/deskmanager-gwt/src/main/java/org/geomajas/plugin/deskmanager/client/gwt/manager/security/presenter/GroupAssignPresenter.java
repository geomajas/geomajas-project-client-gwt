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

import org.geomajas.plugin.deskmanager.client.gwt.manager.security.model.GroupService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.model.UserService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.RoleSelectAssignView;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.Manager;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of {@link RoleSelectEditableHandler<TerritoryDto, UserDto>}.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public class GroupAssignPresenter extends AbstractEditablePresenter
		implements RoleSelectEditableHandler<TerritoryDto, UserDto> {

	private RoleSelectAssignView view;

	private GroupService groupService;

	private UserService userService;

	private TerritoryDto currentGroup;

	/**
	 * This list is used for creating view data. It is not used for sending data to service on save.
	 */
	private List<RoleAndUserId> currentProfiles;

	private List<RoleAndUserId> addedProfiles = new ArrayList<RoleAndUserId>();

	private List<RoleAndUserId> removedProfiles = new ArrayList<RoleAndUserId>();

	private List<UserDto> users = new ArrayList<UserDto>();

	private Role currentRole;

	public GroupAssignPresenter() {
		this(Manager.getUsersManagementViews().getGroupAssignView(), Manager.getGroupService(),
				Manager.getUserService());
	}

	public GroupAssignPresenter(RoleSelectAssignView view, GroupService groupService, UserService userService) {
		super(view);
		this.groupService = groupService;
		this.userService = userService;
		this.view = view;
		view.setHandler(this);
		view.setSelectionRoles(Manager.SELECTION_ROLES);
		view.setEnabled(false);
		setEditable(false);
		currentRole = Role.CONSULTING_USER; //give initial value
	}

	@Override
	public void onSave() {
		super.onSave();
		if (addedProfiles.size() > 0 || removedProfiles.size() > 0) {
			userService.updateUsersOfGroup(currentGroup, toMap(addedProfiles),
					toMap(removedProfiles),
					new DataCallback<List<UserDto>>() {

				@Override
				public void execute(List<UserDto> result) {
					users = result;
					loadUsersForRoleToView();
				}
			});
			addedProfiles.clear();
			removedProfiles.clear();
		}
	}

	private Map<Long, List<Role>> toMap(List<RoleAndUserId> roleAndUserIds) {
		Map<Long, List<Role>> result = new HashMap<Long, List<Role>>();
		for (RoleAndUserId profile : roleAndUserIds) {
			if (!result.containsKey(profile.getUserId())) {
				result.put(profile.getUserId(), new ArrayList<Role>());
			}
			result.get(profile.getUserId()).add(profile.getRole());
		}
		return result;
	}

	@Override
	public void onCancel() {
		super.onCancel();
		for (RoleAndUserId removeProfile : removedProfiles) {
			currentProfiles.add(removeProfile);
		}
		removedProfiles.clear();
		for (RoleAndUserId addProfile : addedProfiles) {
			if (currentProfiles.contains(addProfile)) {
				currentProfiles.remove(addProfile);
			}
		}
		addedProfiles.clear();
		onSelectRole(currentRole);
	}

	@Override
	public void onAdd(Role role, List<UserDto> selectedUsers) {
		for (UserDto userDto : selectedUsers) {
			if (getUserDtoAndRoleInCurrentList(currentProfiles, role, userDto) == null) {
				RoleAndUserId newProfile = new RoleAndUserId(role, userDto.getId());
				currentProfiles.add(newProfile);
				// possibly remove from removedProfiles, else add to addedProfiles.
				RoleAndUserId removedProfilePossible = getUserDtoAndRoleInCurrentList(removedProfiles, role, userDto);
				if (removedProfilePossible != null) {
					removedProfiles.remove(removedProfilePossible);
				} else {
					addedProfiles.add(newProfile);
				}
				loadUsersForRoleToView();
			}
		}
	}

	@Override
	public void onRemove(Role role, List<UserDto> selectedUsers) {
		for (UserDto userDto : selectedUsers) {
			RoleAndUserId existingProfile = getUserDtoAndRoleInCurrentList(currentProfiles, role, userDto);
			if (existingProfile != null) {
				currentProfiles.remove(existingProfile);
				// possibly remove from addedProfiles, else add to removedProfiles.
				RoleAndUserId addedProfilePossible = getUserDtoAndRoleInCurrentList(addedProfiles, role, userDto);
				if (addedProfilePossible != null) {
					addedProfiles.remove(addedProfilePossible);
				} else {
					removedProfiles.add(existingProfile);
				}
				loadUsersForRoleToView();
			}
		}
	}

	@Override
	public void onSelectRole(Role role) {
		if (role != null && !role.equals(Role.ADMINISTRATOR)) {
			currentRole = role;
		}
		loadUsersForRoleToView();
	}

	@Override
	public void setSelectedObject(TerritoryDto group) {
		this.currentGroup = group;
		currentProfiles = null;
		addedProfiles.clear();
		removedProfiles.clear();
		view.clearValues();
		if (group != null) {
			view.setEnabled(true);
			userService.getUsers(true, new DataCallback<List<UserDto>>() {
				@Override
				public void execute(List<UserDto> result) {
					users = result;
					currentProfiles = getUserDtoAndRoleForCurrentGroup(result);
					onSelectRole(currentRole);
				}
			});
		} else {
			view.setEnabled(false);
		}
	}

	// ----------------------------------------
	// private methods
	// ----------------------------------------

	private void loadUsersForRoleToView() {
		List<UserDto> unAssignedUsers = new ArrayList<UserDto>();
		List<UserDto> assignedUsers = new ArrayList<UserDto>();
		for (UserDto territoryDto : users) {
			if (isAssignedToRoleInGroup(currentRole, territoryDto))  {
				assignedUsers.add(territoryDto);
			} else {
				unAssignedUsers.add(territoryDto);
			}
		}
		view.setProfilesForRole(currentRole, assignedUsers, unAssignedUsers);
	}

	private boolean isAssignedToRoleInGroup(Role role, UserDto userDto) {
		for (RoleAndUserId profileDto : currentProfiles) {
			if (profileDto.getUserId() == userDto.getId() &&
					profileDto.getRole().equals(role)) {
				return true;
			}
		}
		return false;
	}

	private List<RoleAndUserId> getUserDtoAndRoleForCurrentGroup(List<UserDto> users) {
		List<RoleAndUserId> result = new ArrayList<RoleAndUserId>();
		for (UserDto userDto : users) {
			for (ProfileDto profileDto : userDto.getProfiles()) {
				if (!profileDto.getRole().equals(Role.ADMINISTRATOR)
				&& profileDto.getTerritory().getId() == currentGroup.getId()) {
					result.add(new RoleAndUserId(profileDto.getRole(), userDto.getId()));
				}
			}
		}
		return result;
	}

	private RoleAndUserId getUserDtoAndRoleInCurrentList(List<RoleAndUserId> referenceList, Role role, UserDto input) {
		for (RoleAndUserId profileDto : referenceList) {
			if (profileDto.getRole().equals(role)
					&& profileDto.getUserId() == input.getId()) {
				return profileDto;
			}
		}
		return null;
	}

	/**
	 * Helper class, for the combination of role and userId.
	 */
	public class RoleAndUserId {

		private Role role;

		private long userId;

		public RoleAndUserId(Role role, long userId) {
			this.role = role;
			this.userId = userId;
		}

		public Role getRole() {
			return role;
		}

		public void setRole(Role role) {
			this.role = role;
		}

		public long getUserId() {
			return userId;
		}

		public void setUserId(long userId) {
			this.userId = userId;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			RoleAndUserId that = (RoleAndUserId) o;

			if (userId != that.userId) {
				return false;
			}
			if (role != that.role) {
				return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			int result = role.hashCode();
			result = 31 * result + (int) (userId ^ (userId >>> 32));
			return result;
		}
	}

}
