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

/**
 * Contains general handler methods for a presenter that manages the details of an object.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 * @param <T> type of object to manage
 */
public interface DetailHandler<T> extends EditableHandler {

	void setObjectsTabHandler(ObjectsTabHandler<T> usersHandler);

	void loadObject(T object);

	void createNewObject();

}
