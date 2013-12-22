/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.editing.gwt.client.controller;

import com.smartgwt.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.client.widgets.events.ShowContextMenuHandler;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.gwt.client.action.MenuAction;
import org.geomajas.gwt.client.action.menu.AboutAction;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.gwt.client.event.GeometryIndexMouseOverOutEvent;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controller that controls the displayed context menu on right click.
 * The context menu contains {@link GeometryIndexOperation}s that can be peromed for one {@link GeometryIndex} object.
 * There is a specific context menu for vertices and edges.
 *
 * @author Jan Venstermans
 *
 */
public class GeometryIndexContextMenuController implements GeometryIndexMouseOverOutEvent.Handler {

	private boolean onOneMenuItemSimulateClick;

	// menus where you can toggle between
	private Menu defaultMenu = new Menu(), vertexMenu = new Menu(), edgeMenu = new Menu();

	private Map<GeometryIndexOperation, String> vertexOperations;

	private Map<GeometryIndexOperation, String> edgeOperations;

	private AboutAction aboutAction;

	private GeometryEditService service;

	private MapWidget map;

	private ShowContextMenuHandler customContextMenuHandler;

	private GeometryIndex geometryIndex, currentGeometryIndex;

	/**
	 * Default constructor.
	 *
	 * @param map the mapwidget to link the controller to
	 * @param service {@link GeometryEditService} to send the operations to
	 */
	public GeometryIndexContextMenuController(final MapWidget map, GeometryEditService service) {
		this.map = map;
		this.service = service;
		vertexOperations = new LinkedHashMap<GeometryIndexOperation, String>();
		edgeOperations = new LinkedHashMap<GeometryIndexOperation, String>();

		// defaultMenu: not on a Geometry Index. Add about item.
		aboutAction = new AboutAction();
		defaultMenu.addItem(aboutAction);

		customContextMenuHandler = new ShowContextMenuHandler() {

			@Override
			public void onShowContextMenu(ShowContextMenuEvent showContextMenuEvent) {
				currentGeometryIndex = geometryIndex;
				Menu contextMenu = map.getContextMenu();
				if (contextMenu.getItems().length > 0) {
					if (GeometryIndexContextMenuController.this.onOneMenuItemSimulateClick
							&& contextMenu.getItems().length == 1) {
						contextMenu.getItem(0).fireEvent(new MenuItemClickEvent(contextMenu.getItem(0).getJsObj()));
					} else {
						contextMenu.showContextMenu();
					}
				}
				showContextMenuEvent.cancel();
			}
		};
		map.addShowContextMenuHandler(customContextMenuHandler);
	}

	@Override
	public void onMouseOverGeometryIndex(GeometryIndexMouseOverOutEvent event) {
		geometryIndex = event.getIndex();
		switch (event.getVertexOrEdgeIndex().getType()) {
			case TYPE_VERTEX:
				map.setContextMenu(vertexMenu);
				break;
			case TYPE_EDGE:
				map.setContextMenu(edgeMenu);
				break;
		}
	}

	@Override
	public void onMouseOutGeometryIndex(GeometryIndexMouseOverOutEvent event) {
		geometryIndex = null;
		map.setContextMenu(defaultMenu);
	}

	/**
	 * Operations that can be performed on a {@link GeometryIndex}.
	 *
	 * @author Jan Venstermans
	 *
	 */
	public enum GeometryIndexOperation {
		REMOVE, DESELECT,
		ZOOM_IN, ZOOM_OUT, ZOOM_TO_FULL_OBJECT;
	}

	public void addVertexOperation(GeometryIndexContextMenuController.GeometryIndexOperation operation,
								   String displayName) {
		vertexOperations.put(operation, displayName);
		createVertexMenuFromOperations();
	}

	public void addEdgeOperation(GeometryIndexContextMenuController.GeometryIndexOperation operation,
								 String displayName) {
		edgeOperations.put(operation, displayName);
		createEdgeMenuFromOperations();
	}

	private void createVertexMenuFromOperations() {
		vertexMenu = new Menu();
		for (final GeometryIndexOperation operation : vertexOperations.keySet()) {
			vertexMenu.addItem(new GeometryIndexOperationMenuAction(operation, vertexOperations.get(operation), null));
		}
	}

	private void createEdgeMenuFromOperations() {
		edgeMenu = new Menu();
		for (final GeometryIndexOperation operation : edgeOperations.keySet()) {
			vertexMenu.addItem(new GeometryIndexOperationMenuAction(operation, vertexOperations.get(operation), null));
		}
	}

	public void onOperation(GeometryIndexOperation operation) {
		if (currentGeometryIndex == null) {
			return;
		}
		try {
			switch (operation) {
					case REMOVE:
						service.remove(Collections.singletonList(currentGeometryIndex));
						break;
					case DESELECT:
						service.getIndexStateService().deselect(Collections.singletonList(currentGeometryIndex));
						break;
					case ZOOM_IN:
						map.getMapModel().getMapView().scale(2.0, MapView.ZoomOption.LEVEL_CHANGE,
								getCoordinateOfGeometryIndex(currentGeometryIndex));
						break;
					case ZOOM_OUT:
						map.getMapModel().getMapView().scale(0.5, MapView.ZoomOption.LEVEL_CHANGE,
								getCoordinateOfGeometryIndex(currentGeometryIndex));
						break;
					case ZOOM_TO_FULL_OBJECT:
						map.getMapModel().getMapView().applyBounds(getBboxOfSelectedGeometry(),
								MapView.ZoomOption.LEVEL_FIT);
						break;
					default:
						break;
				}
		} catch (GeometryOperationFailedException e) {
			e.printStackTrace();
		}
	}

	private Bbox getBboxOfSelectedGeometry() {
		return new Bbox(GeometryService.getBounds(service.getGeometry()));
	}

	private Coordinate getCoordinateOfGeometryIndex(GeometryIndex geometryIndex) {
		Coordinate coordinate = new Coordinate();

		try {
			coordinate = service.getIndexService().getVertex(service.getGeometry(), geometryIndex);
		} catch (GeometryIndexNotFoundException e) {
			e.printStackTrace();
		}
		return coordinate;
	}

	public boolean isOnOneMenuItemSimulateClick() {
		return onOneMenuItemSimulateClick;
	}

	public void setOnOneMenuItemSimulateClick(boolean onOneMenuItemSimulateClick) {
		this.onOneMenuItemSimulateClick = onOneMenuItemSimulateClick;
	}

	/**
	 * {@link MenuAction} implementation for a {@link GeometryIndex}.
	 * This object can be used added to a context menu.
	 *
	 * @author Jan Venstermans
	 */
	public class GeometryIndexOperationMenuAction extends MenuAction {

		private final GeometryIndexOperation operation;

		/**
		 * Constructor that expects you to immediately fill in the title and the icon.
		 *
		 * @param operation vertex operation to be performed on click
		 * @param title The textual title of the menu item.
		 * @param icon  A picture to be used as icon for the menu item.
		 */
		protected GeometryIndexOperationMenuAction(GeometryIndexOperation operation, String title, String icon) {
			super(title, icon);
			this.operation = operation;
		}

		@Override
		public void onClick(MenuItemClickEvent menuItemClickEvent) {
			GeometryIndexContextMenuController.this.onOperation(operation);
		}
	}
}
