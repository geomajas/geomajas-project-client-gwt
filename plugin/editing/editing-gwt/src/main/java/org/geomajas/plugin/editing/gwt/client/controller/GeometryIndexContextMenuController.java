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
package org.geomajas.plugin.editing.gwt.client.controller;

import com.smartgwt.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.client.widgets.events.ShowContextMenuHandler;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.GeometryService;
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
 * The context menu contains operations for one {@link GeometryIndex} object.
 * You can add {@link VertexOperation}s and {@link EdgeOperation}s with custom display name.
 *
 * @author Jan Venstermans
 *
 */
public class GeometryIndexContextMenuController implements GeometryIndexMouseOverOutEvent.Handler {

	// menus where you can toggle between
	private Menu defaultMenu = new Menu(), vertexMenu = new Menu(), edgeMenu = new Menu();

	private Map<VertexOperation, String> vertexOperations;

	private Map<EdgeOperation, String> edgeOperations;

	private AboutAction aboutAction;

	private GeometryEditService service;

	private MapWidget map;

	private ShowContextMenuHandler customContextMenuHandler;

	private GeometryIndex geometryIndex, currentGeometryIndex;

	public GeometryIndexContextMenuController(final MapWidget map, GeometryEditService service) {
		this.map = map;
		this.service = service;
		vertexOperations = new LinkedHashMap<VertexOperation, String>();
		edgeOperations = new LinkedHashMap<EdgeOperation, String>();

		// defaultMenu: not on a Geometry Index. Add about item.
		aboutAction = new AboutAction();
		defaultMenu.addItem(aboutAction);

		customContextMenuHandler = new ShowContextMenuHandler() {

			@Override
			public void onShowContextMenu(ShowContextMenuEvent showContextMenuEvent) {
				currentGeometryIndex = geometryIndex;
				Menu contextMenu = map.getContextMenu();
				if (contextMenu.getItems().length > 0) {
					contextMenu.showContextMenu();
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
	 * Operations that are supported in the vertex context menu.
	 *
	 * @author Jan Venstermans
	 *
	 */
	public enum VertexOperation {
		REMOVE, DESELECT,
		ZOOM_IN, ZOOM_OUT, ZOOM_TO_FULL_OBJECT;
	}

	/**
	 * Operations that are supported in the edge context menu.
	 *
	 * @author Jan Venstermans
	 *
	 */
	public enum EdgeOperation {
		ZOOM_IN, ZOOM_OUT, ZOOM_TO_FULL_OBJECT;
	}

	public void addVertexOperation(GeometryIndexContextMenuController.VertexOperation operation, String displayName) {
		vertexOperations.put(operation, displayName);
		createVertexMenuFromOperations();
	}

	public void addEdgeOperation(GeometryIndexContextMenuController.EdgeOperation operation, String displayName) {
		edgeOperations.put(operation, displayName);
		createEdgeMenuFromOperations();
	}

	private void createVertexMenuFromOperations() {
		vertexMenu = new Menu();
		for (final VertexOperation operation : vertexOperations.keySet()) {
			MenuItem item = new MenuItem(vertexOperations.get(operation));
			item.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent menuItemClickEvent) {
					GeometryIndexContextMenuController.this.onVertexOperation(operation);
				}
			});
			vertexMenu.addItem(item);
		}
	}

	private void createEdgeMenuFromOperations() {
		edgeMenu = new Menu();
		for (final EdgeOperation operation : edgeOperations.keySet()) {
			MenuItem item = new MenuItem(edgeOperations.get(operation));
			item.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent menuItemClickEvent) {
					GeometryIndexContextMenuController.this.onEdgeOperation(operation);
				}
			});
			vertexMenu.addItem(item);
		}
	}

	public void onVertexOperation(VertexOperation operation) {
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

	public void onEdgeOperation(EdgeOperation operation) {
		if (currentGeometryIndex == null) {
			return;
		}
		switch (operation) {
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
}
