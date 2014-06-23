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
import java.util.List;

/**
 * Default implementation of {@link UserAssignHandler}.
 *
 * @author Jan De Moerloose
 */
public class UserAssignPresenter extends AbstractEditablePresenter
		implements RoleSelectEditableHandler<UserDto, TerritoryDto> {

	private RoleSelectAssignView<UserDto, TerritoryDto> view;

	private UserService userService;

	private GroupService groupService;

	private UserDto currentUser;

	/**
	 * This list is used for creating view data. It is not used for sending data to service on save.
	 */
	private List<ProfileDto> currentProfiles;

	private List<ProfileDto> addedProfiles = new ArrayList<ProfileDto>();

	private List<ProfileDto> removedProfiles = new ArrayList<ProfileDto>();

	private List<TerritoryDto> groups = new ArrayList<TerritoryDto>();

	private Role currentRole;

	public UserAssignPresenter() {
		this(Manager.getUsersManagementViews().getUserAssignView(), Manager.getUserService(),
				Manager.getGroupService());
	}

	public UserAssignPresenter(RoleSelectAssignView<UserDto, TerritoryDto> view,
							   UserService userService, GroupService groupService) {
		super(view);
		this.userService = userService;
		this.groupService = groupService;
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
			userService.addAndRemoveProfilesOfUser(currentUser.getId(),
					new ArrayList<ProfileDto>(addedProfiles),
					new ArrayList<ProfileDto>(removedProfiles), new DataCallback<List<ProfileDto>>() {

				@Override
				public void execute(List<ProfileDto> result) {
					currentProfiles = result;
					onSelectRole(currentRole);
				}
			});
			addedProfiles.clear();
			removedProfiles.clear();
		}
	}

	@Override
	public void onCancel() {
		super.onCancel();
		for (ProfileDto removeProfile : removedProfiles) {
			currentProfiles.add(removeProfile);
		}
		removedProfiles.clear();
		for (ProfileDto addProfile : addedProfiles) {
			ProfileDto existingProfile = getProfileInProfilesList(currentProfiles,
					addProfile.getRole(), addProfile.getTerritory());
			if (existingProfile != null) {
				currentProfiles.remove(existingProfile);
			}
		}
		addedProfiles.clear();
		onSelectRole(currentRole);
	}

	@Override
	public void onAdd(Role role, List<TerritoryDto> groups) {
		for (TerritoryDto group : groups) {
			if (getProfileInProfilesList(currentProfiles, role, group) == null) {
				ProfileDto newProfile = new ProfileDto();
				newProfile.setRole(role);
				newProfile.setTerritory(group);
				currentProfiles.add(newProfile);
				// possibly remove from removedProfiles, else add to addedProfiles.
				ProfileDto removedProfilePossible = getProfileInProfilesList(removedProfiles, role, group);
				if (removedProfilePossible != null) {
				   removedProfiles.remove(removedProfilePossible);
				} else {
					addedProfiles.add(newProfile);
				}
			}
		}
		loadProfilesForRoleToView();
	}

	@Override
	public void onRemove(Role role, List<TerritoryDto> groups) {
		for (TerritoryDto group : groups) {
			ProfileDto existingProfile = getProfileInProfilesList(currentProfiles, role, group);
			if (existingProfile != null) {
				currentProfiles.remove(existingProfile);
				// possibly remove from addedProfiles, else add to removedProfiles.
				ProfileDto addedProfilePossible = getProfileInProfilesList(addedProfiles, role, group);
				if (addedProfilePossible != null) {
					addedProfiles.remove(addedProfilePossible);
				} else {
					removedProfiles.add(existingProfile);
				}
				loadProfilesForRoleToView();
			}
		}
	}

	@Override
	public void onSelectRole(Role role) {
		if (role != null && !role.equals(Role.ADMINISTRATOR)) {
			currentRole = role;
		}
		loadProfilesForRoleToView();
	}

	@Override
	public void setSelectedObject(UserDto user) {
		this.currentUser = user;
		currentProfiles = null;
		addedProfiles.clear();
		removedProfiles.clear();
		view.clearValues();
		if (user != null) {
			view.setEnabled(true);
			userService.getProfilesOfUser(currentUser.getId(), new DataCallback<List<ProfileDto>>() {
				@Override
				public void execute(List<ProfileDto> result) {
					currentProfiles = result;
					onSelectRole(currentRole);
				}
			});
		} else {
			view.setEnabled(false);
		}
	}

	public void loadGroups() {
		groups.clear();
		groupService.getGroups(new DataCallback<List<TerritoryDto>>() {
			@Override
			public void execute(List<TerritoryDto> result) {
				groups = result;
			}
		});
	}

	// ----------------------------------------
	// private methods
	// ----------------------------------------

	private void loadProfilesForRoleToView() {
		List<TerritoryDto> unassignedGroups = new ArrayList<TerritoryDto>();
		List<TerritoryDto> assignedGroups = new ArrayList<TerritoryDto>();
		for (TerritoryDto territoryDto : groups) {
		   if (isAssignedToRoleInGroup(currentRole, territoryDto))  {
			   assignedGroups.add(territoryDto);
		   } else {
			   unassignedGroups.add(territoryDto);
		   }
		}
	   	view.setProfilesForRole(currentRole, assignedGroups, unassignedGroups);
	}

	private boolean isAssignedToRoleInGroup(Role role, TerritoryDto territoryDto) {
		for (ProfileDto profileDto : currentProfiles) {
			if (!profileDto.getRole().equals(Role.ADMINISTRATOR) &&
					profileDto.getTerritory().getId() == territoryDto.getId() &&
					profileDto.getRole().equals(role)) {
				return true;
			}
		}
		return false;
	}

	private ProfileDto getProfileInProfilesList(List<ProfileDto> profiles, Role role, TerritoryDto group) {
		for (ProfileDto profileDto : profiles) {
			if (profileDto.getRole().equals(role)
					&& profileDto.getTerritory().getId() == group.getId()) {
				return profileDto;
			}
		}
		return null;
	}
}
