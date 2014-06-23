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

import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

import java.util.List;

/**
 * Handler for adding or removing the admin role to/from users.
 *
 * @author Jan Venstermans
 */
public interface AdminAssignHandler extends EditableHandler {

	void onAddAdminRole(List<UserDto> selectedUsers);

	void onRemoveAdminRole(List<UserDto> selectedUsers);

}
