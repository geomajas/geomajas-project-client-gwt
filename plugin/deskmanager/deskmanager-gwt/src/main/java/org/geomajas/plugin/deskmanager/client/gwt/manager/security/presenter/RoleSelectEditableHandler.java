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

import org.geomajas.plugin.deskmanager.domain.security.dto.Role;

import java.util.List;

/**
 * Interface for presenters dealing with editing.
 *
 * @author Jan Venstermans
 * @param <T> The selected object, there is one of this.
 * @param <S> The assignable objects. There is a list of this.
 */
public interface RoleSelectEditableHandler<T, S> extends EditableHandler {

	void onAdd(Role role, List<S> assignableObjectList);

	void onRemove(Role role, List<S> assignableObjectList);

	void onSelectRole(Role role);

	void setSelectedObject(T selectedObject);
}
