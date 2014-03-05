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

import com.google.gwt.core.client.Callback;
import org.geomajas.global.ExceptionDto;

/**
 * Handler for showing, selecting and deleting Objects of type {@link T}.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 * @param <T> Type that can be selected/created/deleted.
 */
public interface ObjectsTabHandler<T> {
	
	void loadAll(Callback<Boolean, ExceptionDto> onLoadAllFinished);

	void onSelect(T user);

	void onDelete(T user);

	void createObject();

	/**
	 * Sub tabs.
	 *
	 * @author Jan Venstermans
	 */
	enum SubTab {
		DETAILS,
		ASSIGN
	}
}
