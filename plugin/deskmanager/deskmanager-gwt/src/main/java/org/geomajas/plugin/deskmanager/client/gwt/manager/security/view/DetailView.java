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
package org.geomajas.plugin.deskmanager.client.gwt.manager.security.view;

import org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter.DetailHandler;

/**
 * Extension of {@link org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.EditableLoadingView}
 * interface for view that show details of an object.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 * @param <T> object type whose details will be shown.
 */
public interface DetailView<T> extends EditableLoadingView {

	void setHandler(DetailHandler<T> handler);
	
	void setObject(T object);

	void prepareForNewObjectInput();
}
