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

import org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter.AdminAssignHandler;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

import java.util.List;

/**
 * Interface for showing the admin users.
 *
 * @author Jan Venstermans
 */
public interface AdminAssignView extends EditableView {

	void setHandler(AdminAssignHandler handler);

	void setAdminUsers(List<UserDto> admins, List<UserDto> nonAdmins);
	
	void setLoading(boolean loading);
	
	void show();
	
	void hide();

	void setDisabled(boolean disabled);
}
