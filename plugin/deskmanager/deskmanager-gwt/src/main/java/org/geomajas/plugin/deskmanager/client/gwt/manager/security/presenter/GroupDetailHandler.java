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
package org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter;

import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;

/**
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public interface GroupDetailHandler extends DetailHandler<TerritoryDto> {

	void onSave(TerritoryDto group);

	/**
	 * To call when a file has been uploaded.
	 *
	 * @param result token of the file.
	 */
	void onFileUploaded(String result);

}
