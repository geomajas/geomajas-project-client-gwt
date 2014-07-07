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

import java.util.List;

import org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter.ObjectsTabHandler;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

/**
 * @author Jan De Moerloose
 */
public interface UsersView {
	
	void setHandler(ObjectsTabHandler handler);

	void setUsers(List<UserDto> users);

	void selectUser(UserDto user);

	void setLoading(boolean loading);
	
	void show();
	
	void hide();

	/**
	 * Notify view that delete user has failed.
	 */
	void deleteFailed();

	void setDisabled(boolean isStart);
}
