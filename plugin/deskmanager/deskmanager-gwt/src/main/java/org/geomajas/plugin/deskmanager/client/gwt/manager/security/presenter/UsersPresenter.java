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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Callback;
import org.geomajas.global.ExceptionDto;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.model.UserService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.UsersView;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.Manager;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

/**
 * Presenter for data of all users.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public class UsersPresenter implements ObjectsTabHandler<UserDto>, MainTabHandler {

	private UsersView view;

	private UserService userService;

	private UserDetailHandler detailPresenter;

	private UserAssignPresenter userAssignPresenter;

	private List<EditableHandler> subPresenters = new ArrayList<EditableHandler>();

	public UsersPresenter() {
		this(Manager.getUsersManagementViews().getUsersView(), Manager.getUserService());
	}

	public UsersPresenter(UsersView view, UserService userService) {
		this.userService = userService;
		this.view = view;
		detailPresenter = Manager.getUserManagementPresenters().getUserDetailPresenter();
		userAssignPresenter = Manager.getUserManagementPresenters().getUserAssignPresenter();
		view.setHandler(this);
		detailPresenter.setObjectsTabHandler(this);
		subPresenters.add(detailPresenter);
		subPresenters.add(userAssignPresenter);
	}

	//--------------------------------------------------------
	// methods of MainTabHandler
	//--------------------------------------------------------

	@Override
	public void loadAndShow() {
		loadAll(null);
	}

	@Override
	public void setEnabled(boolean enabled) {
		for (EditableHandler subPresenters : this.subPresenters) {
			subPresenters.setEnabled(enabled);
		}
	}

	//--------------------------------------------------------
	// methods of ObjectGroupHandler
	//--------------------------------------------------------

	@Override
	public void loadAll(final Callback<Boolean, ExceptionDto> onLoadAllFinished) {
		view.setLoading(true);
		setEnabled(false);
		userService.getUsers(false, new DataCallback<List<UserDto>>() {
			@Override
			public void execute(List<UserDto> users) {
				view.setUsers(users);
				if (onLoadAllFinished != null) {
					onLoadAllFinished.onSuccess(true);
				}
			}
		});
		// extra: load available groups for user assignment
		userAssignPresenter.loadGroups();
	}

	@Override
	public void onSelect(UserDto user) {
		view.selectUser(user);
		detailPresenter.loadObject(user);
		userAssignPresenter.setSelectedObject(user);
	}

	@Override
	public void onDelete(UserDto user) {
		userService.deleteUser(user, new DataCallback<Boolean>() {

			@Override
			public void execute(Boolean result) {
				if (result) {
					loadAll(null);
				} else {
					// TODO: show information somewhere?
				}
			}
		});
	}

	@Override
	public void createObject() {
		detailPresenter.createNewObject();
	}

}
