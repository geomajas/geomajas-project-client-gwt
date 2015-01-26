/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.staticsecurity.client.event;

import com.google.gwt.event.shared.GwtEvent;
import org.geomajas.annotation.Api;

/**
 * Event that reports when the user has successfully logged out.
 * 
 * @author Pieter De Graef
 * @since 1.7.1
 * @deprecated use {@link org.geomajas.gwt.client.command.event.TokenChangedEvent}
 */
@Api
@Deprecated
public class LogoutSuccessEvent extends GwtEvent<LogoutHandler> {

	public LogoutSuccessEvent() {
		super();
	}

	@SuppressWarnings("unchecked")
	public Type getAssociatedType() {
		return LogoutHandler.TYPE;
	}

	protected void dispatch(LogoutHandler logoutHandler) {
		logoutHandler.onLogoutSuccess(this);
	}
}
