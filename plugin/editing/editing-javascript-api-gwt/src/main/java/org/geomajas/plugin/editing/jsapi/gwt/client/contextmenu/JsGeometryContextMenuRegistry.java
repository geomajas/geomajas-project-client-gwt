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

	public void addVertexAction(final JsGeometryContextMenuAction action, String title) {
		delegate.addVertexAction(new GeometryContextMenuAction(title, null) {

			@Override
			public void onClick(MenuItemClickEvent event) {
				action.execute(JsGeometryContextMenuRegistry.this);

			}
		});
	}

	public void addEdgeAction(final JsGeometryContextMenuAction action, String title) {
		delegate.addEdgeAction(new GeometryContextMenuAction(title, null) {

			@Override
			public void onClick(MenuItemClickEvent event) {
				action.execute(JsGeometryContextMenuRegistry.this);

			}
		});
	}

	public boolean isOnOneMenuItemSimulateClick() {
		return delegate.isOnOneMenuItemSimulateClick();
	}

	public void setOnOneMenuItemSimulateClick(boolean onOneMenuItemSimulateClick) {
		delegate.setOnOneMenuItemSimulateClick(onOneMenuItemSimulateClick);
	}

	public GeometryIndex getIndex() {
		return delegate.getIndex();
	}

	public Map getMap() {
		return map;
	}

	public JsGeometryEditService getService() {
		return editor.getService();
	}

}
