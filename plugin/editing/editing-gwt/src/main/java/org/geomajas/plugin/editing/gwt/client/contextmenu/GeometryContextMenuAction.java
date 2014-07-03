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
package org.geomajas.plugin.editing.gwt.client.contextmenu;

import org.geomajas.gwt.client.action.MenuAction;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryIndex;

/**
 * {@link MenuAction} that has knowledge of the geometry edge/vertex selected.
 * 
 * @author Jan De Moerloose
 * 
 */
public abstract class GeometryContextMenuAction extends MenuAction {

	private Context context;

	public GeometryContextMenuAction(String title, String icon) {
		super(title, icon);
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	/**
	 * Context passed to this action. Holds index, map and service.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface Context {

		GeometryIndex getIndex();

		MapWidget getMap();

		GeometryEditService getService();
	}

}
