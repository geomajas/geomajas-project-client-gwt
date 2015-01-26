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

import java.util.Collections;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.gwt.client.i18n.EditingGwtMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * Implementation of {@link GeometryContextMenuAction} for a number of default actions. This includes remove, deselect,
 * zoom in/out and zoom to geometry bounds.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GeometryContextMenuDefaultAction extends GeometryContextMenuAction {

	private static final EditingGwtMessages MESSAGES = GWT.create(EditingGwtMessages.class);

	/**
	 * Action types that can be performed on a {@link GeometryIndex}.
	 * 
	 * @author Jan Venstermans
	 * 
	 */
	public enum ActionType {
		REMOVE, DESELECT, ZOOM_IN, ZOOM_OUT, ZOOM_TO_BOUNDS;
	}

	private ActionType operation;

	public GeometryContextMenuDefaultAction(ActionType operation) {
		super(getTitle(operation), getIcon(operation));
		this.operation = operation;
	}

	private static String getTitle(ActionType operation) {
		switch (operation) {
			case DESELECT:
				return MESSAGES.getDefaultMenuActionDeselectTitle();
			case REMOVE:
				return MESSAGES.getDefaultMenuActionRemoveTitle();
			case ZOOM_IN:
				return MESSAGES.getDefaultMenuActionZoomInTitle();
			case ZOOM_OUT:
				return MESSAGES.getDefaultMenuActionZoomOutTitle();
			case ZOOM_TO_BOUNDS:
			default:
				return MESSAGES.getDefaultMenuActionZoomToBoundsTitle();
		}
	}

	private static String getIcon(ActionType operation) {
		return null;
	}

	@Override
	public void onClick(MenuItemClickEvent event) {
		if (getIndex() == null) {
			return;
		}
		try {
			switch (operation) {
				case REMOVE:
					getContext().getService().remove(Collections.singletonList(getIndex()));
					break;
				case DESELECT:
					getContext().getService().getIndexStateService().deselect(Collections.singletonList(getIndex()));
					break;
				case ZOOM_IN:
					getMapView().scale(2.0, MapView.ZoomOption.LEVEL_CHANGE, getCoordinateOfGeometryIndex(getIndex()));
					break;
				case ZOOM_OUT:
					getMapView().scale(0.5, MapView.ZoomOption.LEVEL_CHANGE, getCoordinateOfGeometryIndex(getIndex()));
					break;
				case ZOOM_TO_BOUNDS:
					getMapView().applyBounds(getBboxOfSelectedGeometry(), MapView.ZoomOption.LEVEL_FIT);
					break;
				default:
					break;
			}
		} catch (GeometryOperationFailedException e) {
			GWT.log("context operation failed", e);
		} catch (GeometryIndexNotFoundException e) {
			GWT.log("context operation failed", e);
		}
	}

	private GeometryIndex getIndex() {
		return getContext().getIndex();
	}

	private MapView getMapView() {
		return getContext().getMap().getMapModel().getMapView();
	}

	private Bbox getBboxOfSelectedGeometry() {
		return new Bbox(GeometryService.getBounds(getContext().getService().getGeometry()));
	}

	private Coordinate getCoordinateOfGeometryIndex(GeometryIndex geometryIndex) throws GeometryIndexNotFoundException {
		Coordinate coordinate = getContext().getService().getIndexService()
				.getVertex(getContext().getService().getGeometry(), geometryIndex);
		return coordinate;
	}

}
