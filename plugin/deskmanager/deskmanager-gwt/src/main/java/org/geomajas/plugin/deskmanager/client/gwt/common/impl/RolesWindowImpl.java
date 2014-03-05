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
package org.geomajas.plugin.deskmanager.client.gwt.common.impl;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.plugin.deskmanager.client.gwt.common.HelperWindows;
import org.geomajas.plugin.deskmanager.client.gwt.common.ProfileRequestCallback;
import org.geomajas.plugin.deskmanager.client.gwt.common.i18n.CommonMessages;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Default role implementation that asks for a role from a list of roles retrieved from the server.
 * 
 * @author Oliver May
 * @author Jan Venstermans
 * 
 */
public class RolesWindowImpl implements HelperWindows.RolesChoiceWindow {

	private static final CommonMessages MESSAGES = GWT.create(CommonMessages.class);

	private final List<Role> roleOrderInWindow = Arrays.asList(Role.ADMINISTRATOR, Role.DESK_MANAGER, Role.EDITING_USER,
			Role.CONSULTING_USER, Role.GUEST, Role.UNASSIGNED);

	/**
	 * Ask the user to select a role.
	 *
	 * @param onlyAdminRoles
	 *            is true the window will only ask for admin roles. This is introduced for the 'beheersmodule'. TODO:
	 *            make sure the getAvailableRolesCommand only returns the correct roles so that this constructor can be
	 *            removed.
	 * @param roles a list of roles.
	 * @param callback the callback, called when a role is selected.
	 */
	public void askRoleWindow(Map<String, ProfileDto> roles, final ProfileRequestCallback callback) {
		final Window winModal = new Window();
		winModal.setWidth(500);
		winModal.setHeight(300);
		winModal.setTitle(MESSAGES.rolesWindowaskRoleWindowTitle());
		winModal.setShowMinimizeButton(false);
		winModal.setIsModal(true);
		winModal.setShowModalMask(true);
		winModal.centerInPage();
		winModal.setShowCloseButton(false);
		winModal.setZIndex(2000);

		VLayout layout = new VLayout();
		layout.setLayoutMargin(25);
		layout.setMembersMargin(10);

		boolean first = true;

		for (final Entry<String, ProfileDto> role : orderProfiles(roles).entrySet()) {
			if (first) {
				first = false;
				Label label = new Label(
						MESSAGES.rolesWindowWelcome() + " " + role.getValue().getName() + " " +
						role.getValue().getSurname() +  ". " +
						MESSAGES.rolesWindowPleaseSpecifyRole());
				label.setAutoHeight();
				layout.addMember(label);
			}

			final Button button = new Button();
			button.setTitle(getRoleContentC(role.getValue()));
			button.setWidth100();
			button.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

				public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
					callback.onTokenChanged(role.getKey(), role.getValue());
					winModal.destroy();
				}
			});

			layout.addMember(button);
		}

		if (!first) {
			winModal.addItem(layout);
			winModal.show();
		} else {
			SC.warn(MESSAGES.rolesWindowInsufficientRightsForDesk(), new BooleanCallback() {

				public void execute(Boolean value) {
					SC.warn(MESSAGES.rolesWindowStillInsufficientRightsForDesk(), this);
				}
			});
		}
		
	}

	private String getRoleDescription(Role role) {
		switch (role)  {
			case UNASSIGNED:
				return MESSAGES.roleUnassignedDescription();
			case GUEST:
				return MESSAGES.roleGuestDescription();
			case ADMINISTRATOR:
				return MESSAGES.roleAdministratorDescription();
			case DESK_MANAGER:
				return MESSAGES.roleDeskmanagerDescription();	
			case CONSULTING_USER:
				return MESSAGES.roleConsultingUserDescription();
			case EDITING_USER:
				return MESSAGES.roleEditingUserDescription();	
			default:
				return role.getDescription();
		}
		
	}
	private String getRoleContentC(ProfileDto profileDto) {
		if (profileDto.getTerritory() != null) {
			return "<b>" + 
					getRoleDescription(profileDto.getRole()) + "</b> (" + profileDto.getTerritory().getName() + ")";
		} else {
			return "<b>" + getRoleDescription(profileDto.getRole()) + "</b>";
		}
	}

	/**
	 * Order the profiles: first show administrator role, then the desk manager roles, then the others.
	 *
	 * @param profiles the map of token/profile as provided from server side
	 * @return an order retaining {@link LinkedHashMap} containing the same information as input,
	 * 	but ordered by {@link Role} based on local {@link #roleOrderInWindow} list.
	 */
	private LinkedHashMap<String, ProfileDto> orderProfiles(Map<String, ProfileDto> profiles) {
		// separate profiles per role
		Map<Role, List<Entry<String, ProfileDto>>> roleListMap = new HashMap<Role, List<Entry<String, ProfileDto>>>();
		for (Entry<String, ProfileDto> profileDtoEntry : profiles.entrySet()) {
			Role role = profileDtoEntry.getValue().getRole();
			if (!roleListMap.containsKey(role)) {
				roleListMap.put(role, new ArrayList<Entry<String, ProfileDto>>());
			}
			roleListMap.get(role).add(profileDtoEntry);
		}
		LinkedHashMap<String, ProfileDto> linkedProfileMap = new LinkedHashMap<String, ProfileDto>();
		for (Role role : roleOrderInWindow) {
		   if (roleListMap.containsKey(role)) {
			   for (Entry<String, ProfileDto> entry : roleListMap.get(role)) {
				   linkedProfileMap.put(entry.getKey(), entry.getValue());
			   }
		   }
		}
		return linkedProfileMap;
	}
}
