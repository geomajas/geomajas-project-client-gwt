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
package org.geomajas.plugin.deskmanager.client.gwt.manager.events;

import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

/**
 * Event that is fired when a user is selected/cruded.
 * 
 * @author Jan De Moerloose
 * 
 */
public class UserEvent {

	private final UserDto user;

	public UserEvent(UserDto user) {
		this.user = user;
	}

	public UserDto getUser() {
		return user;
	}

}
