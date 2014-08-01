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
package org.geomajas.plugin.deskmanager.client.gwt.manager;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.command.TokenRequestHandler;
import org.geomajas.plugin.deskmanager.client.gwt.common.CommonClientBundle;
import org.geomajas.plugin.deskmanager.client.gwt.common.GdmLayout;
import org.geomajas.plugin.deskmanager.client.gwt.common.HasTokenRequestHandler;
import org.geomajas.plugin.deskmanager.client.gwt.common.LogoutHandler;
import org.geomajas.plugin.deskmanager.client.gwt.common.i18n.CommonMessages;
import org.geomajas.plugin.deskmanager.client.gwt.common.impl.DeskmanagerTokenRequestHandler;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.impl.LoadingScreen;
import org.geomajas.plugin.deskmanager.client.gwt.manager.impl.ManagerInitializer;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesRequest;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;

/**
 * Entry point and main class for deskmanager management application. This entrypoint will show a loading screen and
 * will load the management application, if it's needed asking for a login role.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Api(allMethods = true)
public final class ManagerApplicationLoader implements HasTokenRequestHandler {

	private static final ManagerApplicationLoader INSTANCE = new ManagerApplicationLoader();

	private static final CommonMessages MESSAGES_COMMON = GWT.create(CommonMessages.class);

	private LoadingScreen loadScreen;

	private ProfileDto profile;

	private String securityToken;
	private TokenRequestHandler fallbackHandler;

	/**
	 * Resource for the manager section.
	 */
	protected CommonClientBundle commonResource;

	// Hide default constructor.
	private ManagerApplicationLoader() {
		commonResource = GWT.create(CommonClientBundle.class);
		commonResource.css().ensureInjected();
	}

	/**
	 * Get the security token to log in with.
	 *
	 * @return the security token.
	 */
	public String getSecurityToken() {
		return securityToken;
	}

	/**
	 * Set the security token to log in with.
	 *
	 * @param securityToken the security token
	 */
	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}

	/**
	 * Get instance of this loader.
	 * 
	 * @return the loader.
	 */
	public static ManagerApplicationLoader getInstance() {
		return INSTANCE;
	}

	/**
	 * Loads the manager application, asks for the correct role, and adds it to the given layout. Calls the
	 * initialization handler when finished;
	 *
	 * @param parentLayout
	 *            the layout.
	 */
	public void loadManager(final Layout parentLayout, ManagerInitializationHandler handler) {
		loadManager(parentLayout, handler, null);
	}

	/**
	 * Loads the manager application, asks for the correct role, and adds it to the given layout. Calls the
	 * initialization handler when finished;
	 * 
	 * @param parentLayout
	 *            the layout.
	 */
	public void loadManager(final Layout parentLayout, ManagerInitializationHandler handler, final HLayout header) {
		loadScreen = new LoadingScreen();
		loadScreen.setZIndex(GdmLayout.loadingZindex);
		loadScreen.draw();

		ManagerInitializer initializer = new ManagerInitializer();
		final DeskmanagerTokenRequestHandler deskmanagerTokenRequestHandler =
				new DeskmanagerTokenRequestHandler(RetrieveRolesRequest.MANAGER_ID, fallbackHandler);
		initializer.loadManagerApplication(deskmanagerTokenRequestHandler);
		if (handler != null) {
			initializer.addHandler(handler);
		}
		initializer.addHandler(new ManagerInitializationHandler() {

			public void initialized(ProfileDto pr) {
				profile = pr;

				ManagerLayout managerLayout = header != null ? new ManagerLayout(header) : new ManagerLayout();
				managerLayout.addMember(createLogoutButtonLayout(deskmanagerTokenRequestHandler), 0);
				parentLayout.addMember(managerLayout);

				loadScreen.fadeOut();
			}
		});
	}

	/**
	 * Loads the manager application, asks for the correct role, and adds it to the given layout.
	 * 
	 * @param parentLayout
	 *            the layout.
	 */
	public void loadManager(final Layout parentLayout) {
		loadManager(parentLayout, null);
	}

	/**
	 * Return the currently logged in user profile.
	 * 
	 * @return the user profile.
	 */
	public ProfileDto getUserProfile() {
		return profile;
	}

	@Override
	public void setTokenRequestHandler(TokenRequestHandler fallbackHandler) {
		this.fallbackHandler = fallbackHandler;
	}

	private Layout createLogoutButtonLayout(final LogoutHandler logoutHandler) {
		IButton logoutButton;
		//--- logout button ---
		logoutButton = new IButton(MESSAGES_COMMON.logoutButtonText());
		// adding style to button does not work
		//logoutButton.addStyleName(commonResource.css().logoutButton());
		logoutButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				logoutHandler.logout();
			}
		});
		HLayout logoutLayout = new HLayout();
		logoutLayout.setAlign(Alignment.RIGHT);
		logoutLayout.addMember(logoutButton);
		logoutLayout.setStyleName(commonResource.css().logoutPanel());
		return logoutLayout;
	}
}
