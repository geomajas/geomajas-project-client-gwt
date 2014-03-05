package org.geomajas.plugin.deskmanager.client.gwt.manager.security;

import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class GroupAssign extends VLayout {

	private static final String ROLE_GROUP = "roleGroup";

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private IButton editButton;

	private IButton saveButton;

	private IButton cancelButton;

	public GroupAssign() {
		super(10);
		editButton = new IButton(MESSAGES.editButtonText());
		saveButton = new IButton(MESSAGES.saveButtonText());
		cancelButton = new IButton(MESSAGES.cancelButtonText());

		VLayout containerLayout = new VLayout();
		containerLayout.setMembersMargin(10);

		HLayout buttonLayout = new HLayout(10);
		buttonLayout.addMember(editButton);
		buttonLayout.addMember(cancelButton);
		buttonLayout.addMember(saveButton);
		buttonLayout.setAutoHeight();

		containerLayout.addMember(buttonLayout);

		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth(200);
		toolStrip.setHeight(24);

		IButton roButton = new IButton(MESSAGES.securityRoleReadOnlyText());
		roButton.setShowRollOver(false);
		roButton.setActionType(SelectionType.RADIO);
		roButton.setRadioGroup(ROLE_GROUP);
		toolStrip.addMember(roButton);

		IButton rwButton = new IButton(MESSAGES.securityRoleReadWriteText());
		rwButton.setShowRollOver(false);
		rwButton.setActionType(SelectionType.RADIO);
		rwButton.setRadioGroup(ROLE_GROUP);
		toolStrip.addMember(rwButton);

		IButton managerButton = new IButton(MESSAGES.securityRoleDeskManagerText());
		managerButton.setShowRollOver(false);
		managerButton.setActionType(SelectionType.RADIO);
		managerButton.setRadioGroup(ROLE_GROUP);
		toolStrip.addMember(managerButton);

		IButton adminButton = new IButton(MESSAGES.securityRoleAdministratorText());
		adminButton.setShowRollOver(false);
		adminButton.setActionType(SelectionType.RADIO);
		adminButton.setRadioGroup(ROLE_GROUP);
		toolStrip.addMember(adminButton);

		containerLayout.addMember(toolStrip);

		UserSelectPanel selectPanel = new UserSelectPanel();
		containerLayout.addMember(selectPanel);

		addMember(containerLayout);
	}

}
