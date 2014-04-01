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

import com.google.gwt.event.dom.client.MouseEvent;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.gwt.client.widget.MapWidget.RenderStatus;

/**
 * Drawing controller for Polygon geometries.
 * 
 * @author Bruce Palmkoeck
 * @author Jan Venstermans
 */
public class PolygonDrawController extends LineStringDrawController {

	/**
	 * visualisation of currently saved polygon (after last click),
	 * without the extra line between last and first point.
	 */
	private GfxGeometry tempPolygon;

	private ShapeStyle drawStyleGood = new ShapeStyle("#FF7F00", 0.3f, "#FF7F00", 1, 2);

	private ShapeStyle drawStyleBad = new ShapeStyle("#FF3322", 0.4f, "#FF3322", 1, 2);

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public PolygonDrawController(MapWidget mapWidget, AbstractFreeDrawingController parent,
								 GeometryDrawHandler handler) {
		super(mapWidget, parent, handler);
		// overwrite created geometry in superclass
		geometry = factory.createPolygon(null, null);
	}

	// -------------------------------------------------------------------------
	// Protected methods:
	// -------------------------------------------------------------------------

	@Override
	protected void addCoordinateToGeometry(Coordinate newCoordinate) {
		boolean geometryHasCoordinates = geometry.getCoordinates() != null;

		// determine length and coordinates
		int length = geometryHasCoordinates ? geometry.getCoordinates().length : 0;
		Coordinate[] oldCoords = geometryHasCoordinates ? geometry.getCoordinates() : null;
		Coordinate[] newCoords = new Coordinate[length + 1];
		if (geometryHasCoordinates) {
			System.arraycopy(oldCoords, 0, newCoords, 0, length);
		}

		// put new coordinate in right position
		if (newCoords.length < 2) {
			newCoords[length] = newCoordinate;
		} else {
			newCoords[length] = newCoords[length - 1];
			newCoords[length - 1] = newCoordinate;
		}

		// create new geometry
		LinearRing linearRing = factory.createLinearRing(newCoords);
		geometry = factory.createPolygon(linearRing, null);

		// update drawing
		updateTempLineAfterClick();
	}

	@Override
	protected void updateTempLineAfterClick() {
		if (tempPolygon == null) {
			tempPolygon = new GfxGeometry("LineStringEditController.updatePolygon");
			tempLine = new GfxGeometry("LineStringEditController.updateLine");
		}

		if (geometry.isValid()) {
			tempPolygon.setStyle(drawStyleGood);
			tempLine.setStyle(drawStyleGood);
		} else {
			tempPolygon.setStyle(drawStyleBad);
			tempLine.setStyle(drawStyleBad);
		}

		Coordinate[] worldCoords = geometry.getCoordinates() != null ? geometry.getCoordinates() : new Coordinate[0];
		int length = worldCoords != null ? worldCoords.length : 0;
		// remove the connection line between last and first point
		if (length > 2 && worldCoords[0].equals(worldCoords[length - 1])) {
			length--;
		}
		Coordinate[] srCoords = new Coordinate[length];
		for (int i = 0; i < srCoords.length; i++) {
			srCoords[i] = getTransformer().worldToPan(worldCoords[i]);
		}

		GeometryFactory geometryFactory = geometry.getGeometryFactory();
		LinearRing linearRing = geometryFactory.createLinearRing(srCoords);
		Polygon polygon = geometryFactory.createPolygon(linearRing, null);
		tempPolygon.setGeometry(polygon);
		mapWidget.render(tempPolygon, RenderGroup.VECTOR, RenderStatus.UPDATE);
	}

	@Override
	protected void updateTempLineAfterMove(MouseEvent<?> event) {
		if (tempPolygon == null) {
			updateTempLineAfterClick();
		}

		Polygon polygon = (Polygon) geometry;

		if (polygon != null && polygon.getCoordinates() != null && polygon.getCoordinates().length > 0) {

			LinearRing ring = geometry.getGeometryFactory().createLinearRing(polygon.getCoordinates());
			if (ring != null && geometry.getCoordinates().length > 0) {

				Coordinate[] coordinates = ring.getCoordinates();
				Coordinate firstCoordinate = coordinates[0];
				Coordinate lastCoordinate = coordinates[coordinates.length - 2];

				LineString lineStringPan = geometry.getGeometryFactory().createLineString(new Coordinate[] {
								getTransformer().worldToPan(lastCoordinate),
								getPanPosition(event),
								getTransformer().worldToPan(firstCoordinate) });
				tempLine.setGeometry(lineStringPan);

				mapWidget.render(tempLine, RenderGroup.VECTOR, RenderStatus.ALL);
			}
		}
	}

	@Override
	protected void removeTempLine() {
		if (tempPolygon != null) {
			mapWidget.render(tempPolygon, RenderGroup.VECTOR, RenderStatus.DELETE);
			tempPolygon = null;
		}
		super.removeTempLine();
	}
}
