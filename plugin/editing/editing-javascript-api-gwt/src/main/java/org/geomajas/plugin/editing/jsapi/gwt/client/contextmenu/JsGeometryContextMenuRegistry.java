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
package org.geomajas.plugin.editing.jsapi.gwt.client.contextmenu;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.gwt.client.contextmenu.GeometryContextMenuAction;
import org.geomajas.plugin.editing.gwt.client.contextmenu.GeometryContextMenuRegistry;
import org.geomajas.plugin.editing.jsapi.client.service.JsGeometryEditService;
import org.geomajas.plugin.editing.jsapi.gwt.client.JsGeometryEditor;
import org.geomajas.plugin.jsapi.client.map.Map;
import org.geomajas.plugin.jsapi.gwt.client.exporter.map.MapImpl;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * Exports {@link GeometryContextMenuRegistry}.
 * 
 * @author Jan De Moerloose
 * @since 1.15.0
 * 
 */
@Export("GeometryContextMenuRegistry")
@ExportPackage("org.geomajas.plugin.editing.contextmenu")
@Api(allMethods = true)
public class JsGeometryContextMenuRegistry implements Exportable {

	private GeometryContextMenuRegistry delegate;

	private MapImpl map;

	private JsGeometryEditor editor;

	/**
	 * Default constructor.
	 */
	public JsGeometryContextMenuRegistry(MapImpl map, JsGeometryEditor editor) {
		delegate = new GeometryContextMenuRegistry(map.getMapWidget(), editor.getDelegate());
		this.map = map;
		this.editor = editor;
	}

	/**
	 * Adds a vertex context menu action.
	 * 
	 * @param action
	 * @param title
	 */
	public void addVertexAction(final JsGeometryContextMenuAction action, String title) {
		delegate.addVertexAction(new GeometryContextMenuAction(title, null) {

			@Override
			public void onClick(MenuItemClickEvent event) {
				action.execute(JsGeometryContextMenuRegistry.this);

			}
		});
	}

	/**
	 * Adds an edge context menu action.
	 * 
	 * @param action
	 * @param title
	 */
	public void addEdgeAction(final JsGeometryContextMenuAction action, String title) {
		delegate.addEdgeAction(new GeometryContextMenuAction(title, null) {

			@Override
			public void onClick(MenuItemClickEvent event) {
				action.execute(JsGeometryContextMenuRegistry.this);

			}
		});
	}

	/**
	 * Adds a geometry context menu action.
	 * 
	 * @param action
	 * @param title
	 */
	public void addGeometryAction(final JsGeometryContextMenuAction action, String title) {
		delegate.addGeometryAction(new GeometryContextMenuAction(title, null) {

			@Override
			public void onClick(MenuItemClickEvent event) {
				action.execute(JsGeometryContextMenuRegistry.this);

			}
		});
	}

	/**
	 * @see #setOnOneMenuItemSimulateClick(boolean).
	 * 
	 * @return true if 1 menu item should be executed directly
	 */
	public boolean isOnOneMenuItemSimulateClick() {
		return delegate.isOnOneMenuItemSimulateClick();
	}

	/**
	 * In case of 1 menu item, this settings allows to execute the item action directly instead of showing the menu (as
	 * if the user would have clicked it).
	 * 
	 * @param onOneMenuItemSimulateClick
	 */
	public void setOnOneMenuItemSimulateClick(boolean onOneMenuItemSimulateClick) {
		delegate.setOnOneMenuItemSimulateClick(onOneMenuItemSimulateClick);
	}

	/**
	 * Get the index of the element that was right-clicked.
	 * @return
	 */
	public GeometryIndex getIndex() {
		return delegate.getIndex();
	}

	/**
	 * Get the current map.
	 * @return
	 */
	public Map getMap() {
		return map;
	}

	/**
	 * Get the edit service.
	 * @return
	 */
	public JsGeometryEditService getService() {
		return editor.getService();
	}

}
