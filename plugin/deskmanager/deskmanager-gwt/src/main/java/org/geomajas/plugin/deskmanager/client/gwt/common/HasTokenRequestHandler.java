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

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.command.TokenRequestHandler;

/**
 * Interface supporting a token request handler.
 *
 * @author Oliver May
 * @since 1.15.0
 */
@Api(allMethods = true)
public interface HasTokenRequestHandler {

	/**
	 * Attach a token request handler. When a token is needed (login required) this handler will be called.
	 * @param fallbackHandler the handler
	 */
	void setTokenRequestHandler(TokenRequestHandler fallbackHandler);

}
