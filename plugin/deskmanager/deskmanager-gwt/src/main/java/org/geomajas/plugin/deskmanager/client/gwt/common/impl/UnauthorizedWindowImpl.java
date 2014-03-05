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
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Window;
import org.geomajas.plugin.deskmanager.client.gwt.common.GdmLayout;
import org.geomajas.plugin.deskmanager.client.gwt.common.HelperWindows;
import org.geomajas.plugin.deskmanager.client.gwt.common.i18n.CommonMessages;

/**
 * Window that shows that the user is not authorized to view this geodesk/manager.
 * 
 * @author Oliver May
 * @author Jan Venstermans
 */
public class UnauthorizedWindowImpl implements HelperWindows.UnauthorizedWindow {

	private static final CommonMessages MESSAGES = GWT.create(CommonMessages.class);


	/**
	 * Show the window.
	 */
	public void show() {
		final Window winModal = new Window();
		winModal.setWidth(500);
		winModal.setHeight(300);
		winModal.setTitle(MESSAGES.rolesWindowUnauthorizedWindowTitle());
		winModal.setShowMinimizeButton(false);
		winModal.setIsModal(true);
		winModal.setShowModalMask(true);
		winModal.centerInPage();
		winModal.setShowCloseButton(false);
		winModal.setZIndex(GdmLayout.roleSelectZindex);

		HTMLPane pane = new HTMLPane();
		pane.setContents("<br/><br/><center>" + MESSAGES.rolesWindowInsufficientRightsForDesk() + "</center>");
		winModal.addItem(pane);
		winModal.show();
	}
}
