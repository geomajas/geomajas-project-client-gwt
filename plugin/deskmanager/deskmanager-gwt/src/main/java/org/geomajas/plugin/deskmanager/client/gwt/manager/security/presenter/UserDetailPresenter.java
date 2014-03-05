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
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.UserDetailView;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.Manager;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

/**
 * Presenter for showing data of a
 * {@link org.geomajas.plugin.deskmanager.domain.security.dto.UserDto} object.
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public class UserDetailPresenter extends AbstractDetailPresenter<UserDto> implements UserDetailHandler {

	private UserDetailView view;

	private UserService userService;

	public UserDetailPresenter() {
		this(Manager.getUsersManagementViews().getUserDetailView(), Manager.getUserService());
	}

	public UserDetailPresenter(UserDetailView view, UserService userService) {
		super(view);
		this.userService = userService;
		this.view = view;
		view.setPasswordValidationRule(Manager.PASSWORD_VALIDATION_REGEX);
	}

	@Override
	public void createNewObject() {
		view.setCreateUserMode();
		super.createNewObject();
	}

	//---------------------------------------------
	// override abstract methods
	//---------------------------------------------

	@Override
	protected void getObjectFromService(UserDto user, final DataCallback<UserDto> onFinish) {
		userService.getUser(user.getId(), new DataCallback<UserDto>() {

			@Override
			public void execute(UserDto userFromDb) {
				onFinish.execute(userFromDb);
			}

		});
	}



	@Override
	protected UserDto createEmptyObject() {
		return new UserDto();
	}

	//---------------------------------------------
	// handler methods
	//---------------------------------------------

	@Override
	public void onSave(UserDto user, String newPassword) {
		super.onSave();
		if (user.getId() <= 0) {
			// create new user
			userService.createUser(user.getEmail(), user.getName(), user.getSurname(), newPassword,
					new DataCallback<UserDto>() {
						@Override
						public void execute(UserDto result) {
							loadAllAndShowObject(result);
						}
					});


		} else {
			// update existing user
			userService.updateUser(user, newPassword, new DataCallback<UserDto>() {

				@Override
				public void execute(UserDto result) {
					loadAllAndShowObject(result);
				}
			});
		}
	}

}
