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

package org.geomajas.plugin.deskmanager.client.gwt.common;

import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;

import java.util.Map;

/**
 * Interfaces for helpers windows.
 * Introduced for testing purposes.
 *
 * @author Jan Venstermans
 * @since 1.15.0
 */
public interface HelperWindows {

	/**
	 * Interface for enable testing of a window that shows a profile list.
	 */
	public interface RolesChoiceWindow {

		/**
		 *  Shows the roles in a window. If a profile is selected the callback is executed.
		 * @param roles roles to be shown (all will be shown)
		 * @param callback enables returning one entry of the map.
		 */
		void askRoleWindow(Map<String, ProfileDto> roles, final ProfileRequestCallback callback);
	}

	/**
	 * Interface for enable testing of a window that shows an unauthorized window.
	 */
	public interface UnauthorizedWindow {
		void show();
	}

}
