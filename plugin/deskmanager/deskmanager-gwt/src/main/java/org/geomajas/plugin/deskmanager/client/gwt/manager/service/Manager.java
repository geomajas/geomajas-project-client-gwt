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
package org.geomajas.plugin.deskmanager.client.gwt.manager.service;

import javax.validation.Validator;

import org.geomajas.plugin.deskmanager.client.gwt.manager.security.model.GroupService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.model.GroupServiceImpl;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.model.UserService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.model.UserServiceImpl;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter.UserManagementPresenters;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.UserManagementViews;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.UserManagementViewsImpl;

import com.google.gwt.validation.client.impl.Validation;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;

import java.util.Arrays;
import java.util.List;

/**
 * Util class for retrieving module services.
 *
 * @author Jan De Moerloose
 */
public class Manager {

	private static UserService userService = new UserServiceImpl();

	private static GroupService groupService = new GroupServiceImpl();

	private static UserManagementViews usersViewFactory = new UserManagementViewsImpl();

	private static UserManagementPresenters userManagementPresenters = new UserManagementPresenters();

	private static Validator validator;

	public static final String PASSWORD_VALIDATION_REGEX = "^(?=.*[0-9])(?=.*[a-z]).{8,}$";

	public static final List<Role> SELECTION_ROLES =
			Arrays.asList(Role.CONSULTING_USER, Role.EDITING_USER, Role.DESK_MANAGER);

	protected Manager() {
	}

	public static UserService getUserService() {
		return userService;
	}

	public static GroupService getGroupService() {
		return groupService;
	}

	public static UserManagementViews getUsersManagementViews() {
		return usersViewFactory;
	}

	public static UserManagementPresenters getUserManagementPresenters() {
		return userManagementPresenters;
	}

	public static Validator getValidator() {
		if (validator == null) {
			validator = Validation.buildDefaultValidatorFactory().getValidator();
		}
		return validator;
	}

	public static void setUserManagementViews(UserManagementViews factory) {
		usersViewFactory = factory;
	}

	public static void setUserService(UserService service) {
		userService = service;
	}

	public static void setGroupService(GroupService service) {
		groupService = service;
	}
	
	public static void setValidator(Validator v) {
		validator = v;
	}

}
