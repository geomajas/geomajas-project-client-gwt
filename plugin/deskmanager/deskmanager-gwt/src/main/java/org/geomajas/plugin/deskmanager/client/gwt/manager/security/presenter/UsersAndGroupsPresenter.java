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

import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.UsersAndGroupsView;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.Manager;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Root presenter for Users and Groups functions.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public class UsersAndGroupsPresenter implements UsersAndGroupsHandler {

	private UsersPresenter usersPresenter;

	private GroupsPresenter groupsPresenter;

	private AdminAssignPresenter adminsPresenter;

	private UsersAndGroupsView view;

	private Map<UsersAndGroupsHandler.MainTab, MainTabHandler> tabPresenters
			= new LinkedHashMap<UsersAndGroupsHandler.MainTab, MainTabHandler>();

	public UsersAndGroupsPresenter() {
		this(Manager.getUsersManagementViews().getUsersAndGroupsView());
	}

	public UsersAndGroupsPresenter(UsersAndGroupsView view) {
		this.view = view;
		/* create sub presenters */
		usersPresenter = Manager.getUserManagementPresenters().getUsersPresenter();
		groupsPresenter = Manager.getUserManagementPresenters().getGroupsPresenter();
		adminsPresenter = Manager.getUserManagementPresenters().getAdminAssignPresenter();
		/* keep sub presenters in a map */
		tabPresenters.put(UsersAndGroupsHandler.MainTab.USERS, usersPresenter);
		tabPresenters.put(UsersAndGroupsHandler.MainTab.GROUPS, groupsPresenter);
		tabPresenters.put(UsersAndGroupsHandler.MainTab.ADMINS, adminsPresenter);
		view.setHandler(this);
	}

	@Override
	public void onMainTabSelected(UsersAndGroupsHandler.MainTab mainTab) {
		for (Map.Entry<MainTab, MainTabHandler> entry : tabPresenters.entrySet()) {
		   if (entry.getKey().equals(mainTab)) {
			   entry.getValue().setEnabled(true);
			   entry.getValue().loadAndShow();
		   } else {
			   entry.getValue().setEnabled(false);
		   }
		}
	}

	@Override
	public void onCreateObjectForTab(MainTab objectTab) {
		view.selectSubTab(objectTab, ObjectsTabHandler.SubTab.DETAILS);
		switch (objectTab) {
			case GROUPS:
				groupsPresenter.createObject();
				break;
			case USERS:
				usersPresenter.createObject();
				break;
		}
	}

	@Override
	public UsersAndGroupsView getView() {
		return view;
	}
}
