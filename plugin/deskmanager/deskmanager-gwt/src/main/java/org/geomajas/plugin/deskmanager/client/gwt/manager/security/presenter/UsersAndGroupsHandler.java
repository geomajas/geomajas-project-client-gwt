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

/**
 * Handler for the main tabs.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public interface UsersAndGroupsHandler {

	/**
	 * On select the specified main tab.
	 */
	void onMainTabSelected(MainTab mainTab);

	/**
	 * On create an object for the specific tab.
	 *
	 * @param objectTab
	 */
	void onCreateObjectForTab(MainTab objectTab);

	/**
	 * Return the view.
	 *
	 * @return view
	 */
	UsersAndGroupsView getView();

	/**
	 * Main tabs.
	 *
	 * @author Jan Venstermans
	 */
	enum MainTab {
		USERS,
		GROUPS,
		ADMINS
	}
}
