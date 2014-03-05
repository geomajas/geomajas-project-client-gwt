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

import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

import java.util.List;

/**
 * Default implementation of {@link RoleSelectAssignView<UserDto, TerritoryDto>}.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public class UserAssign extends AbstractRoleSelectAssignLayout<UserDto, TerritoryDto>
		implements RoleSelectAssignView<UserDto, TerritoryDto> {

	@Override
	public SelectPanel<TerritoryDto> createSelectPanel() {
		return new UserAssignGroupSelectPanel();
	}

	/**
	 * Extension of {@link GroupSelectPanel} for {@link UserAssign}.
	 *
	 * @author Jan Venstermans
	 */
	protected class UserAssignGroupSelectPanel extends GroupSelectPanel {

		@Override
		protected void onClickAddButton() {
			List<TerritoryDto> selectedTerritoryDto = getSourceGrid().getSelectedObjects();
			if (selectedTerritoryDto != null) {
				getHandler().onAdd(getSelectedRole(), selectedTerritoryDto);
			}
		}

		@Override
		protected void onClickRemoveButton() {
			List<TerritoryDto> selectedTerritoryDto = getTargetGrid().getSelectedObjects();
			if (selectedTerritoryDto != null) {
				getHandler().onRemove(getSelectedRole(), selectedTerritoryDto);
			}
		}

		@Override
		public void onDroppedInTargetListGrid(List<TerritoryDto> droppedObjects) {
			if (droppedObjects != null && droppedObjects.size() > 0) {
				getHandler().onAdd(getSelectedRole(), droppedObjects);
			}
		}

		@Override
		public void onDroppedInSourceListGrid(List<TerritoryDto> droppedObjects) {
			if (droppedObjects != null && droppedObjects.size() > 0) {
				getHandler().onRemove(getSelectedRole(), droppedObjects);
			}
		}
	}
}
