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
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter.AdminAssignHandler;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

import java.util.List;

/**
 * Default implementation of {@link AdminAssignView}.
 *
 * @author Jan Venstermans
 */
public class AdminAssign extends AbstractButtonsLayout implements AdminAssignView {

	private static final String ROLE_GROUP = "roleGroup";

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private AdminAssignHandler handler;

	private UserSelectPanel selectPanel;

	@Override
	public void onEdit() {
	   handler.onEdit();
	}

	@Override
	public void onCancel() {
		handler.onCancel();
	}

	@Override
	public void onSave() {
	   handler.onSave();
	}

	@Override
	public void setHandler(AdminAssignHandler handler) {
		this.handler = handler;
	}

	@Override
	public void setAdminUsers(List<UserDto> admins, List<UserDto> nonAdmins) {
		selectPanel.getSourceGrid().fillGrid(nonAdmins);
		selectPanel.getTargetGrid().fillGrid(admins);
	}

	@Override
	public void setLoading(boolean loading) {

	}

	@Override
	public void setEditable(boolean editable) {
		selectPanel.setDisabled(!editable);
	}


	@Override
	public void clearValues() {
	   //selectPanel.getSourceGroupsGrid().clear();
	   //selectPanel.getTargetGroupsGrid().clear();
	}

	@Override
	protected void fillContainerLayout() {
		selectPanel = new GroupAssignUserSelectPanel();

		containerLayout.addMember(buttonLayout);
		containerLayout.addMember(selectPanel);
	}

	/**
	 * Extension of {@link org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.UserSelectPanel}
	 * for {@link org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.AdminAssign}.
	 *
	 * @author Jan Venstermans
	 */
	protected class GroupAssignUserSelectPanel extends UserSelectPanel {

		@Override
		protected void onClickAddButton() {
			List<UserDto> selectedUsers = getSourceGrid().getSelectedObjects();
			if (selectedUsers != null && selectedUsers.size() > 0) {
				handler.onAddAdminRole(selectedUsers);
			}
		}

		@Override
		protected void onClickRemoveButton() {
			List<UserDto> selectedUsers = getTargetGrid().getSelectedObjects();
			if (selectedUsers != null && selectedUsers.size() > 0) {
				handler.onRemoveAdminRole(selectedUsers);
			}
		}

		protected String getHelpButtonTooltip() {
			return MESSAGES.adminAssignHelpText();
		}

		protected String getSourceUserGridTitle() {
			return MESSAGES.adminAssignAvailableUsers();
		}

		protected String getTargetUserGridTitle() {
			return MESSAGES.adminAssignAssignedUsers();
		}

		protected String getTargetUserGridEmptyMessage() {
			return MESSAGES.adminAssignAssignedUsersTooltip();
		}

		@Override
		public void onDroppedInTargetListGrid(List<UserDto> droppedObjects) {
			handler.onAddAdminRole(droppedObjects);
		}

		@Override
		public void onDroppedInSourceListGrid(List<UserDto> droppedObjects) {
			handler.onRemoveAdminRole(droppedObjects);
		}
	}
}
