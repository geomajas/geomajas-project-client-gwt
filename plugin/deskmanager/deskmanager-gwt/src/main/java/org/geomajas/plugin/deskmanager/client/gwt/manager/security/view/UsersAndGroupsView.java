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
package org.geomajas.plugin.deskmanager.client.gwt.manager.security.view;

import org.geomajas.plugin.deskmanager.client.gwt.manager.common.ManagerTab;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter.ObjectsTabHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter.UsersAndGroupsHandler;

/**
 * Interface for the main view, with main tabs and sub tabs.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public interface UsersAndGroupsView extends ManagerTab {

	void setHandler(UsersAndGroupsHandler handler);

	void selectSubTab(UsersAndGroupsHandler.MainTab mainTab, ObjectsTabHandler.SubTab subTab);
}
