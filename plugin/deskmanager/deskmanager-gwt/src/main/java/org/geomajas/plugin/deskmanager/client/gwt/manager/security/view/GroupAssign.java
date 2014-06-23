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

import com.google.gwt.core.client.GWT;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

import java.util.List;

/**
 * Default implementation of
 * {@link RoleSelectAssignView}.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public class GroupAssign extends AbstractRoleSelectAssignLayout<TerritoryDto, UserDto>
		implements RoleSelectAssignView<TerritoryDto, UserDto> {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	@Override
	public SelectPanel createSelectPanel() {
		return new GroupAssignUserSelectPanel();
	}

	/**
	 * Extension of {@link org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.GroupSelectPanel}
	 * for {@link GroupAssign}.
	 *
	 * @author Jan Venstermans
	 */
	protected class GroupAssignUserSelectPanel extends UserSelectPanel {

		@Override
		protected void onClickAddButton() {
			List<UserDto> selectedUsers = getSourceGrid().getSelectedObjects();
			if (selectedUsers != null && selectedUsers.size() > 0) {
				getHandler().onAdd(getSelectedRole(), selectedUsers);
			}
		}

		@Override
		protected void onClickRemoveButton() {
			List<UserDto> selectedUsers = getTargetGrid().getSelectedObjects();
			if (selectedUsers != null && selectedUsers.size() > 0) {
				getHandler().onRemove(getSelectedRole(), selectedUsers);
			}
		}

		@Override
		public void onDroppedInTargetListGrid(List<UserDto> droppedObjects) {
			getHandler().onAdd(getSelectedRole(), droppedObjects);
		}

		@Override
		public void onDroppedInSourceListGrid(List<UserDto> droppedObjects) {
			getHandler().onRemove(getSelectedRole(), droppedObjects);
		}

		protected String getHelpButtonTooltip() {
			return MESSAGES.userSelectAssignedUsersHelpText();
		}

		protected String getSourceUserGridTitle() {
			return MESSAGES.userSelectAvailableUsers();
		}

		protected String getTargetUserGridTitle() {
			return MESSAGES.userSelectAssignedUsers();
		}

		protected String getTargetUserGridEmptyMessage() {
			return MESSAGES.userSelectAssignedUsersTooltip();
		}
	}
}
