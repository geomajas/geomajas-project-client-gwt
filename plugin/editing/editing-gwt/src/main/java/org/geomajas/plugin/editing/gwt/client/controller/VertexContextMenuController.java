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

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.client.widgets.events.ShowContextMenuHandler;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.gwt.client.action.menu.AboutAction;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexDeselectedEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexDeselectedHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexSelectedEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexSelectedHandler;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller that controls the displayed context menu on right click.
 * You can add {@link Operation} values with custom display name.
 *
 * @author Jan Venstermans
 *
 */
public class VertexContextMenuController implements GeometryIndexSelectedHandler, GeometryIndexDeselectedHandler {

	private Menu defaultMenu = new Menu();

	private Menu vertexMenu = new Menu();

	private Map<Operation, String> vertexOperations;

	private AboutAction aboutAction;

	private GeometryEditService service;

	private boolean vertexItemsActive;

	private MapWidget map;

	private ShowContextMenuHandler dontShowContextMenu;

	private HandlerRegistration dontShowContextMenuHandler;

	public VertexContextMenuController(MapWidget map, GeometryEditService service) {
		this.map = map;
		this.service = service;
		vertexOperations = new LinkedHashMap<Operation, String>();

		// finally add about item
		aboutAction = new AboutAction();
		defaultMenu.addItem(aboutAction);
		dontShowContextMenu = new ShowContextMenuHandler() {

			@Override
			public void onShowContextMenu(ShowContextMenuEvent showContextMenuEvent) {
				showContextMenuEvent.cancel();
			}
		};

	}

	@Override
	public void onGeometryIndexSelected(GeometryIndexSelectedEvent event) {
		checkIfOnlyOneVertexSelected();
	}

	@Override
	public void onGeometryIndexDeselected(GeometryIndexDeselectedEvent event) {
		checkIfOnlyOneVertexSelected();
	}

	private void checkIfOnlyOneVertexSelected() {
		setVertexMenuItemsChecked(service.getIndexStateService().getSelection().size() == 1);
	}

	private void setVertexMenuItemsChecked(boolean vertexItemsActive) {
		if (this.vertexItemsActive != vertexItemsActive) {
			this.vertexItemsActive = vertexItemsActive;
			if (dontShowContextMenuHandler != null) {
				dontShowContextMenuHandler.removeHandler();
				dontShowContextMenuHandler = null;
			}
			if (vertexItemsActive) {
				map.setContextMenu(vertexMenu);
				if (vertexOperations.keySet().isEmpty()) {
					dontShowContextMenuHandler = map.addShowContextMenuHandler(dontShowContextMenu);
				}
			} else {
				map.setContextMenu(defaultMenu);
			}
		}

	}

	/**
	 * Operations that are supported in the vertex context menu.
	 *
	 * @author Jan Venstermans
	 *
	 */
	public enum Operation {
		REMOVE_SELECTED, DESELECT_ALL,
		ZOOM_IN, ZOOM_OUT, ZOOM_TO_FULL_OBJECT;
	}

	public void addVertexOperation(VertexContextMenuController.Operation operation, String displayName) {
		vertexOperations.put(operation, displayName);
		createVertexMenuFromOperations();
	}

	private void createVertexMenuFromOperations() {
		vertexMenu = new Menu();
		for (final Operation operation : vertexOperations.keySet()) {
			MenuItem item = new MenuItem(vertexOperations.get(operation));
			item.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent menuItemClickEvent) {
					VertexContextMenuController.this.onVertexOperation(operation);
				}
			});
			vertexMenu.addItem(item);
		}
	}

	public void onVertexOperation(Operation operation) {
		try {
			switch (operation) {
					case REMOVE_SELECTED:
						service.remove(service.getIndexStateService().getSelection());
						break;
					case DESELECT_ALL:
						service.getIndexStateService().deselectAll();
						break;
					case ZOOM_IN:
						map.getMapModel().getMapView().scale(2.0, MapView.ZoomOption.LEVEL_CHANGE,
								getCenterOfSelected());
						break;
					case ZOOM_OUT:
						map.getMapModel().getMapView().scale(0.5, MapView.ZoomOption.LEVEL_CHANGE,
								getCenterOfSelected());
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

	private Coordinate getCenterOfSelected() {
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		for (GeometryIndex index : service.getIndexStateService().getSelection()) {
			try {
				coordinates.add(service.getIndexService().getVertex(service.getGeometry(), index));
			} catch (GeometryIndexNotFoundException e) {
				e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			}
		}
		Geometry geomSelectedPointsDummy = new Geometry();
		geomSelectedPointsDummy.setCoordinates(coordinates.toArray(new Coordinate[coordinates.size()]));
		return BboxService.getCenterPoint(GeometryService.getBounds(geomSelectedPointsDummy)) ;
	}
}
