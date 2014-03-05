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

import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter.RoleSelectEditableHandler;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of
 * {@link RoleSelectAssignView}.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 * @param <T>  The type of object that is selected and where other other object will be assigned to.
 *           There is only one object at the time of this type.
 * @param <S> The type of object that will be assigned or unassigned. There will be a list of this type of object.
 */
public abstract class AbstractRoleSelectAssignLayout<T, S>
		extends AbstractButtonsLayout implements RoleSelectAssignView<T, S> {

	private static final String ROLE_GROUP = "roleGroup";

	private RoleSelectEditableHandler<T, S> handler;

	private ToolStrip toolStrip;

	private SelectPanel<S> selectPanel;

	private Role selectedRole;

	private Map<Role, IButton> radioButtons;

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
	public void setHandler(RoleSelectEditableHandler handler) {
		this.handler = handler;
	}

	@Override
	public void setSelectionRoles(List<Role> roles) {
		if (toolStrip != null) {
			toolStrip.clear();
			getRadioButtons().clear();
			for (Role role : roles) {
				addRadioRoleButton(role);
			}
		}
	}

	@Override
	public void setProfilesForRole(Role role, List<S> assignedUsers, List<S> unAssignedUsers) {
		setSelectedRole(role);
		getRadioButtons().get(role).setSelected(true);
		getSelectPanel().getSourceGrid().fillGrid(unAssignedUsers);
		getSelectPanel().getTargetGrid().fillGrid(assignedUsers);
	}

	@Override
	public void setEditable(boolean editable) {
		selectPanel.setDisabled(!editable);
	}


	@Override
	public void clearValues() {
	   selectPanel.clearValues();
	}

	@Override
	protected void fillContainerLayout() {
		//buttonLayout.setAutoHeight();
		createToolStrip();
		selectPanel = createSelectPanel();

		containerLayout.addMember(buttonLayout);
		containerLayout.addMember(toolStrip);
		containerLayout.addMember(selectPanel.getWidget());
	}

	public abstract SelectPanel<S> createSelectPanel();

	private void createToolStrip() {
		toolStrip = new ToolStrip();
		toolStrip.setWidth(200);
		toolStrip.setHeight(24);
		toolStrip.setMembersMargin(3);
		// TODO: should be moved to css. Default toolstrip has border (in current theme)!
		//toolStrip.setBorder("none");
	}

	private void addRadioRoleButton(final Role role) {
		IButton roleButton = new IButton(getRoleMessage(role));
		roleButton.setShowRollOver(false);
		roleButton.setActionType(SelectionType.RADIO);
		roleButton.setRadioGroup(ROLE_GROUP);
		roleButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				handler.onSelectRole(role);
			}
		});
		getRadioButtons().put(role, roleButton);
		toolStrip.addMember(roleButton);
	}

	private String getRoleMessage(Role role) {
		switch (role) {
			case CONSULTING_USER: //read only
				return MESSAGES.securityRoleReadOnlyText();
			case EDITING_USER: // read write
				return MESSAGES.securityRoleReadWriteText();
			case DESK_MANAGER:
				return MESSAGES.securityRoleDeskManagerText();
			case ADMINISTRATOR:
				return MESSAGES.securityRoleAdministratorText();
		}
		return null;
	}

	public Map<Role, IButton> getRadioButtons() {
		// necessary because radioButtons is used in super constructor
		if (radioButtons == null) {
			radioButtons = new HashMap<Role, IButton>();
		}
		return radioButtons;
	}

	protected SelectPanel<S> getSelectPanel() {
		return selectPanel;
	}

	protected RoleSelectEditableHandler<T, S> getHandler() {
		return handler;
	}

	protected Role getSelectedRole() {
		return selectedRole;
	}

	protected void setSelectedRole(Role selectedRole) {
		this.selectedRole = selectedRole;
	}
}
