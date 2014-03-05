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

import org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter.RoleSelectEditableHandler;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;

import java.util.List;

/**
 * View for assigning user+role to one selected group.
 * This view is used in the Users section.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 * @param <T>  The type of object that is selected and where other other object will be assigned to.
 *           There is only one object at the time of this type.
 * @param <S> The type of object that will be assigned or unassigned. There will be a list of this type of object.
 */
public interface RoleSelectAssignView<T, S> extends EditableView {

	void setHandler(RoleSelectEditableHandler<T, S> handler);

	void setProfilesForRole(Role role, List<S> assignedUsers, List<S> unAssignedUsers);

	void setSelectionRoles(List<Role> roles);
}
