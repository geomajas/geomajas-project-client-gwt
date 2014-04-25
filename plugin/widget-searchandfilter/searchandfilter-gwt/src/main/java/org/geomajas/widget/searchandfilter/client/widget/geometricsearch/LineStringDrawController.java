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

package org.geomajas.widget.searchandfilter.client.widget.geometricsearch;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.user.client.Event;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.editing.EditController.EditMode;
import org.geomajas.gwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.gwt.client.widget.MapWidget.RenderStatus;

/**
 * Drawing controller for LineString geometries.
 * 
 * @author Bruce Palmkoeck
 * @author Jan Venstermans
 */
public class LineStringDrawController extends AbstractFreeDrawingController {

	/**
	 * Coordinates of current line string, after last click.
	 * Separate field for redraw on update.
	 */
	private Coordinate[] currentLineStringCoordinates;

	/**
	 * {@link GfxGeometry} object that is updated on mouse move.
	 */
	protected GfxGeometry tempLine;

	private ShapeStyle drawStyle = new ShapeStyle("#FF7F00", 0f, "#FF7F00", 1, 2);

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public LineStringDrawController(MapWidget mapWidget, AbstractFreeDrawingController parent,
			GeometryDrawHandler handler) {
		super(mapWidget, parent, handler);
		geometry = factory.createLineString(new Coordinate[0]);
	}

	// -------------------------------------------------------------------------
	// DrawController implementation:
	// -------------------------------------------------------------------------

	@Override
	public void cleanup() {
		removeTempLine();
	}

	@Override
	public boolean isBusy() {
		// busy when inserting or dragging has started
		return getEditMode() == EditMode.INSERT_MODE || getEditMode() == EditMode.DRAG_MODE;
	}

	// -------------------------------------------------------------------------
	// MapController implementation:
	// -------------------------------------------------------------------------

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		updateTempLineAfterMove(event);
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		if (event.getNativeButton() != Event.BUTTON_RIGHT) {
			addCoordinateToGeometry(getWorldPosition(event));
		}
	}

	// Getters and setters:

	@Override
	public void onDoubleClick(DoubleClickEvent event) {
		if (event.getNativeButton() != Event.BUTTON_RIGHT) {
			handler.onDraw(geometry);
		}
	}

	@Override
	public void setEditMode(EditMode editMode) {
		super.setEditMode(editMode);
		if (editMode == EditMode.DRAG_MODE) {
			removeTempLine();
		}
	}

	// -------------------------------------------------------------------------
	// Protected methods:
	// -------------------------------------------------------------------------

	protected void addCoordinateToGeometry(Coordinate newCoordinate) {
		int length = geometry.getCoordinates().length;
		Coordinate[] oldCoords = geometry.getCoordinates();
		Coordinate[] newCoords = new Coordinate[length + 1];
		System.arraycopy(oldCoords, 0, newCoords, 0, length);

		// put new coordinate in right position
		newCoords[length] = newCoordinate;

		// create new geometry
		geometry = factory.createLineString(newCoords);

		// update drawing
		updateTempLineAfterClick();
	}

	protected void updateTempLineAfterClick() {
		if (tempLine == null) {
			tempLine = new GfxGeometry("LineStringDrawController.updateLine");
			tempLine.setStyle(drawStyle);
		}

		Coordinate[] srCoords = new Coordinate[geometry.getCoordinates().length];
		Coordinate[] worldCoords = geometry.getCoordinates();
		for (int i = 0; i < srCoords.length; i++) {
			srCoords[i] = getTransformer().worldToPan(worldCoords[i]);
		}

		currentLineStringCoordinates = srCoords;
	}

	protected void updateTempLineAfterMove(MouseEvent<?> event) {
		if (tempLine == null) {
			updateTempLineAfterClick();
		}

		if (currentLineStringCoordinates != null && currentLineStringCoordinates.length > 0) {
			Coordinate[] newCoords = new Coordinate[currentLineStringCoordinates.length + 1];
			System.arraycopy(currentLineStringCoordinates, 0, newCoords, 0, currentLineStringCoordinates.length);
			newCoords[currentLineStringCoordinates.length] = getPanPosition(event);

			LineString lineString = geometry.getGeometryFactory().createLineString(newCoords);
			tempLine.setGeometry(lineString);
			mapWidget.render(tempLine, RenderGroup.VECTOR, RenderStatus.UPDATE);
		}
	}

	protected void removeTempLine() {
		if (tempLine != null) {
			mapWidget.render(tempLine, RenderGroup.VECTOR, RenderStatus.DELETE);
			tempLine = null;
		}
	}
}
