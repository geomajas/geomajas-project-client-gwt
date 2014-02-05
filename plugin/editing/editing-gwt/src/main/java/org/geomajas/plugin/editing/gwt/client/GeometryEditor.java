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
package org.geomajas.plugin.editing.gwt.client;

import com.google.gwt.event.shared.EventBus;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.BaseGeometryEditor;
import org.geomajas.plugin.editing.client.handler.AbstractGeometryIndexMapHandler;
import org.geomajas.plugin.editing.gwt.client.controller.GeometryIndexContextMenuController;
import org.geomajas.plugin.editing.gwt.client.gfx.StyleService;

/**
 * Extends {@link BaseGeometryEditor} to provide access to the {@link MapWidget} and {@link StyleService}
 *  and {@link GeometryIndexContextMenuController}.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface GeometryEditor extends BaseGeometryEditor {

	/**
	 * Get the map on which this editor is running.
	 * 
	 * @return the map widget
	 */
	MapWidget getMapWidget();

	/**
	 * Get the style service of the renderer.
	 * 
	 * @return the style service
	 */
	StyleService getStyleService();

	/**
	 * Get the context menu controller.
	 *
	 * @return the context menu controller
	 */
	GeometryIndexContextMenuController getContextMenuController();

	/**
	 * Set the context menu controller.
	 *
	 * @param contextMenuController the context menu controller
	 */
	void setContextMenuController(GeometryIndexContextMenuController contextMenuController);

	void addVertexHandlerFactory(final AbstractGeometryIndexMapHandler handler);

	void addEdgeHandlerFactory(AbstractGeometryIndexMapHandler handler);

	EventBus getGeometryEditorSpecificEventbus();

}
