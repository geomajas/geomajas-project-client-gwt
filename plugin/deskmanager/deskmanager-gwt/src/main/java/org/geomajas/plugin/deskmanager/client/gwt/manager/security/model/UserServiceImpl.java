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

import java.util.List;
import java.util.Map;

import org.geomajas.command.CommandResponse;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.command.security.dto.AdminAssignmentRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.AdminAssignmentResponse;
import org.geomajas.plugin.deskmanager.command.security.dto.CreateUserRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.CreateUserResponse;
import org.geomajas.plugin.deskmanager.command.security.dto.DeleteUserRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.DeleteUserResponse;
import org.geomajas.plugin.deskmanager.command.security.dto.GetUserProfilesRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.GetUserProfilesResponse;
import org.geomajas.plugin.deskmanager.command.security.dto.GetUserRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.FindUsersRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.FindUsersResponse;
import org.geomajas.plugin.deskmanager.command.security.dto.GetUserResponse;
import org.geomajas.plugin.deskmanager.command.security.dto.GroupAssignmentRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.GroupAssignmentResponse;
import org.geomajas.plugin.deskmanager.command.security.dto.UserAssignmentRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.UserAssignmentResponse;
import org.geomajas.plugin.deskmanager.command.security.dto.UpdateUserRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.UpdateUserResponse;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

/**
 * Default implementation of {@link UserService}.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public class UserServiceImpl implements UserService {

	@Override
	public void getUsers(boolean includeProfiles, final DataCallback<List<UserDto>> onFinish) {
		FindUsersRequest request = new FindUsersRequest();
		request.setIncludeProfiles(includeProfiles);
		GwtCommand command = new GwtCommand(FindUsersRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<FindUsersResponse>() {

			public void execute(FindUsersResponse response) {
				if (onFinish != null) {
					onFinish.execute(response.getUsers());
				}
			}
		});
	}

	@Override
	public void createUser(String email, String name, String surname,
						   String password, final DataCallback<UserDto> onFinish) {
		CreateUserRequest request = new CreateUserRequest();
		request.setEmail(email);
		request.setName(name);
		request.setSurname(surname);
		request.setPassword(password);
		GwtCommand command = new GwtCommand(CreateUserRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<CreateUserResponse>() {

			public void execute(CreateUserResponse response) {
				if (onFinish != null) {
					onFinish.execute(response.getUser());
				}
			}
		});
	}

	@Override
	public void updateUser(UserDto user, String newPassword, final DataCallback<UserDto> onFinish) {
		UpdateUserRequest request = new UpdateUserRequest();
		request.setUserDto(user);
		request.setPassword(newPassword);
		GwtCommand command = new GwtCommand(UpdateUserRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<UpdateUserResponse>() {

			public void execute(UpdateUserResponse response) {
				if (onFinish != null) {
					onFinish.execute(response.getUser());
				}
			}
		});
	}

	@Override
	public void deleteUser(final UserDto user, final DataCallback<Boolean> onFinish) {
		DeleteUserRequest request = new DeleteUserRequest();
		request.setId(user.getId());
		GwtCommand command = new GwtCommand(DeleteUserRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<DeleteUserResponse>() {

			public void execute(DeleteUserResponse response) {
				onFinish.execute(true);
			}

			@Override
			public void onCommunicationException(Throwable error) {
				onFinish.execute(false);
			}

			@Override
			public void onCommandException(CommandResponse response) {
				onFinish.execute(false);
			}
		});
	}

	@Override
	public void getProfilesOfUser(long userId, final DataCallback<List<ProfileDto>> onFinish) {
		GetUserProfilesRequest request = new GetUserProfilesRequest();
		request.setId(userId);
		GwtCommand command = new GwtCommand(GetUserProfilesRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<GetUserProfilesResponse>() {

			public void execute(GetUserProfilesResponse response) {
				if (onFinish != null) {
					onFinish.execute(response.getProfiles());
				}
			}
		});
	}

	@Override
	public void addAndRemoveProfilesOfUser(long userId, List<ProfileDto> addedProfiles,
										   List<ProfileDto> removedProfiles,
										   final DataCallback<List<ProfileDto>> onFinish) {
		UserAssignmentRequest request = new UserAssignmentRequest();
		request.setUserId(userId);
		request.setAddedProfiles(addedProfiles);
		request.setRemovedProfiles(removedProfiles);
		GwtCommand command = new GwtCommand(UserAssignmentRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<UserAssignmentResponse>() {

			public void execute(UserAssignmentResponse response) {
				if (onFinish != null) {
					onFinish.execute(response.getProfiles());
				}
			}
		});
	}

	@Override
	public void updateUsersOfGroup(TerritoryDto territoryDto, Map<Long, List<Role>> addedProfiles,
								   Map<Long, List<Role>> removedProfiles,
								   final DataCallback<List<UserDto>> dataCallback) {
		GroupAssignmentRequest request = new GroupAssignmentRequest();
		request.setTerritoryDto(territoryDto);
		request.setAddedProfiles(addedProfiles);
		request.setRemovedProfiles(removedProfiles);
		GwtCommand command = new GwtCommand(GroupAssignmentRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<GroupAssignmentResponse>() {

					public void execute(GroupAssignmentResponse response) {
						if (dataCallback != null) {
							dataCallback.execute(response.getUsers());
						}
					}
				});
	}

	@Override
	public void updateAdmins(List<UserDto> addedUsers, List<UserDto> removedUsers,
							 final DataCallback<List<UserDto>> dataCallback) {
		AdminAssignmentRequest request = new AdminAssignmentRequest();
		request.setAddedAdmins(addedUsers);
		request.setRemovedAdmins(removedUsers);
		GwtCommand command = new GwtCommand(AdminAssignmentRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<AdminAssignmentResponse>() {

			@Override
			public void execute(AdminAssignmentResponse response) {
				if (dataCallback != null) {
					dataCallback.execute(response.getUsers());
				}
			}
		});
	}

	@Override
	public void getUser(long id, final DataCallback<UserDto> dataCallback) {
		GetUserRequest request = new GetUserRequest();
		request.setId(id);
		GwtCommand command = new GwtCommand(GetUserRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<GetUserResponse>() {

			public void execute(GetUserResponse response) {
				if (dataCallback != null) {
					dataCallback.execute(response.getUserDto());
				}
			}
		});
	}

}