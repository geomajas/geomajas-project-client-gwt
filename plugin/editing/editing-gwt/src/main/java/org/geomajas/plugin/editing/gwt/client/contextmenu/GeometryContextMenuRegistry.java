/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.editing.gwt.client.contextmenu;

import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.handler.AbstractGeometryIndexMapHandler;
import org.geomajas.plugin.editing.client.handler.EdgeMapHandlerFactory;
import org.geomajas.plugin.editing.client.handler.GeometryIndexMapHandlerFactory;
import org.geomajas.plugin.editing.client.handler.VertexMapHandlerFactory;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;
import org.geomajas.plugin.editing.gwt.client.handler.EditingHandlerRegistry;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.smartgwt.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.client.widgets.events.ShowContextMenuHandler;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * Sets up a context menu on the map for geometry actions.
 * 
 * @author Jan De Moerloose
 */
public class GeometryContextMenuRegistry implements GeometryContextMenuAction.Context {

	private boolean onOneMenuItemSimulateClick;

	private Menu vertexMenu = new Menu();

	private Menu edgeMenu = new Menu();

	private Menu geometryMenu = new Menu();

	private GeometryEditService service;

	private MapWidget map;

	private ShowContextMenuHandler customContextMenuHandler;

	private GeometryIndexType selectedType;

	private GeometryIndex selectedIndex;

	/**
	 * Default constructor.
	 * 
	 * @param map the mapwidget to link the controller to
	 * @param service {@link GeometryEditService} to send the operations to
	 */
	public GeometryContextMenuRegistry(final MapWidget map, GeometryEditor editor) {
		this.map = map;
		this.service = editor.getEditService();
		customContextMenuHandler = new ShowContextMenuHandler() {

			@Override
			public void onShowContextMenu(ShowContextMenuEvent showContextMenuEvent) {
				Menu contextMenu = map.getContextMenu();
				int itemCount = contextMenu.getItems().length;
				if (itemCount > 0) {
					if (GeometryContextMenuRegistry.this.onOneMenuItemSimulateClick && itemCount == 1) {
						contextMenu.getItem(0).fireEvent(new MenuItemClickEvent(contextMenu.getItem(0).getJsObj()));
						contextMenu.hide();
					} else {
						contextMenu.showContextMenu();
					}
				}
				showContextMenuEvent.cancel();
			}
		};
		map.addShowContextMenuHandler(customContextMenuHandler);
		editor.getRenderer().addEdgeHandlerFactory(new EdgeMapHandlerFactory() {

			@Override
			public AbstractGeometryIndexMapHandler create() {
				return new MenuHandler(GeometryIndexType.TYPE_EDGE);
			}
		});
		editor.getRenderer().addVertexHandlerFactory(new VertexMapHandlerFactory() {

			@Override
			public AbstractGeometryIndexMapHandler create() {
				return new MenuHandler(GeometryIndexType.TYPE_VERTEX);
			}
		});
		EditingHandlerRegistry.addGeometryHandlerFactory(new GeometryIndexMapHandlerFactory() {
			
			@Override
			public AbstractGeometryIndexMapHandler create() {
				return new MenuHandler(GeometryIndexType.TYPE_GEOMETRY);
			}
		});
		editor.getRenderer().redraw();
	}

	/**
	 * Add a vertex action to the registry.
	 * 
	 * @param action
	 */
	public void addVertexAction(GeometryContextMenuAction action) {
		action.setContext(this);
		vertexMenu.addItem(action);
	}

	/**
	 * Add a edge action to the registry.
	 * 
	 * @param action
	 */
	public void addEdgeAction(GeometryContextMenuAction action) {
		action.setContext(this);
		edgeMenu.addItem(action);
	}

	/**
	 * Add a edge action to the registry.
	 * 
	 * @param action
	 */
	public void addGeometryAction(GeometryContextMenuAction action) {
		action.setContext(this);
		geometryMenu.addItem(action);
	}

	public boolean isOnOneMenuItemSimulateClick() {
		return onOneMenuItemSimulateClick;
	}

	public void setOnOneMenuItemSimulateClick(boolean onOneMenuItemSimulateClick) {
		this.onOneMenuItemSimulateClick = onOneMenuItemSimulateClick;
	}

	@Override
	public GeometryIndex getIndex() {
		return selectedIndex;
	}

	@Override
	public MapWidget getMap() {
		return map;
	}

	@Override
	public GeometryEditService getService() {
		return service;
	}

	protected void updateMenu() {
		if (GeometryIndexType.TYPE_EDGE == selectedType) {
			map.setContextMenu(edgeMenu);
		} else if (GeometryIndexType.TYPE_VERTEX == selectedType) {
			map.setContextMenu(vertexMenu);
		} else if (GeometryIndexType.TYPE_GEOMETRY == selectedType) {
			map.setContextMenu(geometryMenu);
		} else {
			// set default
			map.setContextMenu(null);
		}
	}

	/**
	 * Updates the meny when hovering edge or vertex.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class MenuHandler extends AbstractGeometryIndexMapHandler implements MouseOverHandler, MouseOutHandler {

		private GeometryIndexType type;

		public MenuHandler(GeometryIndexType type) {
			this.type = type;
		}

		@Override
		public void onMouseOut(MouseOutEvent event) {
			updateMenu();
		}

		@Override
		public void onMouseOver(MouseOverEvent event) {
			selectedType = type;
			selectedIndex = getIndex();
			updateMenu();
		}

	}

}
