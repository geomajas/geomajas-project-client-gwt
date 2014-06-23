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

import com.google.gwt.i18n.client.LocaleInfo;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.command.TokenRequestHandler;
import org.geomajas.gwt.client.command.event.TokenChangedEvent;
import org.geomajas.gwt.client.command.event.TokenChangedHandler;
import org.geomajas.plugin.deskmanager.client.gwt.common.HelperWindows;
import org.geomajas.plugin.deskmanager.client.gwt.common.ProfileRequestCallback;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesResponse;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;

import java.util.HashMap;
import java.util.Map;

/**
 * Token request handler for the deskmanager. This handler will request a list of roles from the server, and let the
 * user choose the correct one.
 * <p/>
 * This implementation presumes that the user has already logged in trough another authentication way (reverse proxy,
 * sso,...) so that only a specific role for the application has to be chosen.
 *
 * @author Oliver May
 */
public class DeskmanagerTokenRequestHandler implements org.geomajas.gwt.client.command.TokenRequestHandler {

	private TokenRequestHandler fallbackTokenRequestHandler;
	private String geodeskId;

	protected String token;

	protected ProfileDto profile;

	private HelperWindows.RolesChoiceWindow roleSelectWindow;
	private HelperWindows.UnauthorizedWindow unauthorizedWindow;

	/**
	 * Create a deskmanaer token request handler. This will show a list of profiles valid for the selected
	 * geodesk/manager application. If needed it will call a fallbackTokenRequestHandler handler
	 * request a valid security token.
	 *
	 * @param geodeskId                   the geodesk to open.
	 * @param fallbackTokenRequestHandler the fallbackTokenRequestHandler handler to retrieve a token from
	 */
	public DeskmanagerTokenRequestHandler(String geodeskId, TokenRequestHandler fallbackTokenRequestHandler) {
		this.geodeskId = geodeskId;
		this.fallbackTokenRequestHandler = fallbackTokenRequestHandler;
	}

	@Override
	public void login(final TokenChangedHandler tokenChangedHandler) {
		doLogin(tokenChangedHandler, fallbackTokenRequestHandler);
	}

	private void doLogin(final TokenChangedHandler tokenChangedHandler,
						 final TokenRequestHandler fallbackTokenRequestHandler) {
		RetrieveRolesRequest request = new RetrieveRolesRequest();
		request.setGeodeskId(geodeskId);
		request.setLocale(LocaleInfo.getCurrentLocale().getLocaleName());
		request.setSecurityToken(token);

		GwtCommand command = new GwtCommand(RetrieveRolesRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback<RetrieveRolesResponse>() {

			public void execute(RetrieveRolesResponse response) {
				processResponse(tokenChangedHandler, fallbackTokenRequestHandler, response);
			}
		});
	}

	/**
	 * Method public for enable testing.
	 */
	public void processResponse(final TokenChangedHandler tokenChangedHandler,
								TokenRequestHandler fallbackTokenRequestHandler,
								RetrieveRolesResponse response) {
		Map<String, ProfileDto> profileMap = response.getRoles();
		//Guest role, proceed
		for (Map.Entry<String, ProfileDto> role : profileMap.entrySet()) {
			if (role.getValue().getRole().equals(Role.GUEST)) {
				token = role.getKey();
				profile = null;
				tokenChangedHandler.onTokenChanged(new TokenChangedEvent(token));
				return;
			}
		}

		// If only one role, use single role
		if (profileMap.size() == 1) {
			Map.Entry<String, ProfileDto> onlyEntry = profileMap.entrySet().iterator().next();
			selectTokenAndProfile(onlyEntry.getKey(), onlyEntry.getValue(), tokenChangedHandler);
		// If more than one role; ask for correct role
		} else if (profileMap.size() > 0) {
			// select the roles that need to be shown
			Map<String, ProfileDto> profilesChoice = new HashMap<String, ProfileDto>();
			boolean deskView = !RetrieveRolesRequest.MANAGER_ID.equals(geodeskId);
			for (final Map.Entry<String, ProfileDto> role : profileMap.entrySet()) {
				switch (role.getValue().getRole()) {
					case ADMINISTRATOR:
					case DESK_MANAGER:
						//always show
						profilesChoice.put(role.getKey(), role.getValue());
						break;
					default:
						// rest: only show if not manager 'geodesk'
						if (deskView) {
							profilesChoice.put(role.getKey(), role.getValue());
						}
						break;
				}
			}
			if (profilesChoice.size() < 1) {
				 // no appropriate role
				notAuthorized();
			} else if (profilesChoice.size() < 2) {
				 // one appropriate role
				Map.Entry<String, ProfileDto> onlyEntry = profilesChoice.entrySet().iterator().next();
				selectTokenAndProfile(onlyEntry.getKey(), onlyEntry.getValue(), tokenChangedHandler);
			} else {
				getRoleSelectWindow().askRoleWindow(profilesChoice,
						new ProfileRequestCallback() {
							@Override
							public void onTokenChanged(String token, ProfileDto profile) {
								selectTokenAndProfile(token, profile, tokenChangedHandler);
							}
						});
			}
			// if geodesk is public, don't use any role
		} else if (response.isPublicGeodesk()) {
			tokenChangedHandler.onTokenChanged(new TokenChangedEvent(token));
			return;
			// If no roles, try fallback.
		} else if (fallbackTokenRequestHandler != null) {
			fallbackTokenRequestHandler.login(new TokenChangedHandler() {
				@Override
				public void onTokenChanged(TokenChangedEvent event) {
					DeskmanagerTokenRequestHandler.this.token = event.getToken();
					doLogin(tokenChangedHandler, null);
				}
			});
			// Giving up, unauthorized.
		} else {
			notAuthorized();
		}
	}

	private void selectTokenAndProfile(String selectedToken, ProfileDto selectedProfile,
									   TokenChangedHandler tokenChangedHandler) {
		token = selectedToken;
		profile = selectedProfile;
		tokenChangedHandler.onTokenChanged(new TokenChangedEvent(token));
	}

	private void notAuthorized() {
		getUnauthorizedWindow().show();
	}


	/**
	 * Get the token for the current active role.
	 *
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Get the profile for the current active role.
	 *
	 * @return the profile
	 */
	public ProfileDto getProfile() {
		return profile;
	}

	/**
	 * Lazy loading of window.
	 * @return roleChoiceWindow
	 */
	public HelperWindows.RolesChoiceWindow getRoleSelectWindow() {
		if (roleSelectWindow == null) {
			roleSelectWindow = new RolesWindowImpl();
		}
		return roleSelectWindow;
	}

	/**
	 * Setter for enable testing.
	 * @param roleSelectWindow
	 */
	public void setRoleSelectWindow(HelperWindows.RolesChoiceWindow roleSelectWindow) {
		this.roleSelectWindow = roleSelectWindow;
	}

	/**
	 * Lazy loading of window.
	 * @return unauthorized window
	 */
	public HelperWindows.UnauthorizedWindow getUnauthorizedWindow() {
		if (unauthorizedWindow == null) {
			unauthorizedWindow = new UnauthorizedWindowImpl();
		}
		return unauthorizedWindow;
	}

	/**
	 * Setter for enable testing.
	 * @param unauthorizedWindow
	 */
	public void setUnauthorizedWindow(HelperWindows.UnauthorizedWindow unauthorizedWindow) {
		this.unauthorizedWindow = unauthorizedWindow;
	}
}
