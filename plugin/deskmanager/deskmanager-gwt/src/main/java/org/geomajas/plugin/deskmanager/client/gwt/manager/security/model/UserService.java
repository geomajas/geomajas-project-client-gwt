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

import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

import java.util.List;
import java.util.Map;

/**
 * Interface for CRUD of {@link org.geomajas.plugin.deskmanager.domain.security.dto.UserDto} objects.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public interface UserService {

	/* get dto list users */
	void getUsers(boolean includeProfiles, final DataCallback<List<UserDto>> onFinish);

	/* CRUD user */
	void getUser(long id, DataCallback<UserDto> dataCallback);

	void createUser(String email, String name, String surname, String password, DataCallback<UserDto> onFinish);

	void updateUser(UserDto user, String newPassword, DataCallback<UserDto> onFinish);

	void deleteUser(UserDto user, final DataCallback<Boolean> onFinish);

	void getProfilesOfUser(long userId, DataCallback<List<ProfileDto>> onFinish);

	/* CRUD groups, but this needs the user as well */
	void addAndRemoveProfilesOfUser(long userId, List<ProfileDto> addedProfiles,
			List<ProfileDto> removedProfiles, DataCallback<List<ProfileDto>> onFinish);

	void updateUsersOfGroup(TerritoryDto territoryDto, Map<Long, List<Role>> addedProfiles,
							Map<Long, List<Role>> removedProfiles, DataCallback<List<UserDto>> dataCallback);

	void updateAdmins(List<UserDto> addedUsers, List<UserDto> removedUsers, DataCallback<List<UserDto>> dataCallback);
}
