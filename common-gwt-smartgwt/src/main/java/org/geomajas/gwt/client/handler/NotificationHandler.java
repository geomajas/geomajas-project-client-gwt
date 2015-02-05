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
package org.geomajas.gwt.client.handler;

import org.geomajas.annotation.Api;

/**
 * Notification handler that handles messages sent to the {@link Notify} class.
 *
 * @author Kristof Heirwegh
 * @author Oliver May
 * @since 1.2.0
 */
@Api(allMethods = true)
public interface NotificationHandler {

	/**
	 * Handle info message.
	 * 
	 * @param message the message
	 */
	void handleInfo(String message);

	/**
	 * Handle error message.
	 * 
	 * @param message the message
	 */
	void handleError(String message);
}
