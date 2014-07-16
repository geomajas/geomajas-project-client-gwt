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

import com.google.gwt.resources.client.ClientBundle;
import org.geomajas.annotation.Api;


/**
 * Client bundle for manager.
 * 
 * @author Jan Venstermans
 */
public interface ManagerClientBundle extends ClientBundle {

	@Source("gm-gwt-deskmanager-manager.css")
	ManagerCssResource css();
	
}
