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
package org.geomajas.gwt.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.gwt.client.action.MenuAction;
import org.geomajas.gwt.client.action.menu.ToggleSnappingAction;
import org.geomajas.gwt.client.controller.MeasureDistanceHandler.State;
import org.geomajas.gwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.spatial.geometry.operation.InsertCoordinateOperation;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.gwt.client.widget.MapWidget.RenderStatus;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * <p>
 * Controller that measures distances on the map, by clicking points. The actual distances are displayed in a label at
 * the top left of the map.
 * </p>
 * 
 * @author Pieter De Graef
 * @author Oliver May
 * @author Jan De Moerloose
 * 
 */
public class MeasureDistanceController extends AbstractSnappingController {

	private static final ShapeStyle LINE_STYLE_1 = new ShapeStyle("#FFFFFF", 0, "#FF9900", 1, 2);

	private static final ShapeStyle LINE_STYLE_2 = new ShapeStyle("#FFFFFF", 0, "#FF5500", 1, 2);

	private GfxGeometry distanceLine;

	private GfxGeometry lineSegment;

	private VLayout panel;

	private GeometryFactory factory;

	private float tempLength;

	private Menu menu;

	private GeometryFactory geometryFactory;

	private List<MeasureDistanceHandler> handlers = new ArrayList<MeasureDistanceHandler>();

	private MeasureDistanceContext context = new MeasureDistanceContextImpl();

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * Construct a measureDistanceController. Default is to display the total distance and last line distance.
	 * 
	 * @param mapWidget the mapwidget where the distance is measured on.
	 */
	public MeasureDistanceController(MapWidget mapWidget) {
		this(mapWidget, false, false);
	}

	/**
	 * Construct a measureDistanceController.
	 * 
	 * @param mapWidget the mapwidget where the distance is measured on.
	 * @param showArea true if the area should be displayed
	 * @param displayCoordinates the if the coordinates should be displayed.
	 */
	public MeasureDistanceController(MapWidget mapWidget, boolean showArea, boolean displayCoordinates) {
		super(mapWidget);
		distanceLine = new GfxGeometry("measureDistanceLine");
		distanceLine.setStyle(LINE_STYLE_1);
		lineSegment = new GfxGeometry("measureDistanceLineSegment");
		lineSegment.setStyle(LINE_STYLE_2);
		geometryFactory = new GeometryFactory(mapWidget.getMapModel().getPrecision(), mapWidget.getMapModel().getSrid());
		addHandler(new MeasureDistancePanel(mapWidget, showArea, displayCoordinates));
	}
	
	public void addHandler(MeasureDistanceHandler handler) {
		handlers.add(handler);
	}
	
	public void removeHandler(MeasureDistanceHandler handler) {
		handlers.remove(handler);
	}
	
	public void clearHandlers() {
		handlers.clear();
	}

	// -------------------------------------------------------------------------
	// GraphicsController interface:
	// -------------------------------------------------------------------------

	/** Create the context menu for this controller. */
	public void onActivate() {
		menu = new Menu();
		menu.addItem(new CancelMeasuringAction(this));
		Layer selectedLayer = mapWidget.getMapModel().getSelectedLayer();
		if (selectedLayer instanceof VectorLayer) {
			menu.addItem(new ToggleSnappingAction((VectorLayer) selectedLayer, this));
		}
		mapWidget.setContextMenu(menu);
	}

	/** Clean everything up. */
	public void onDeactivate() {
		onDoubleClick(null);
		menu.destroy();
		menu = null;
		mapWidget.setContextMenu(null);
		mapWidget.unregisterWorldPaintable(distanceLine);
		mapWidget.unregisterWorldPaintable(lineSegment);
	}

	/** Set a new point on the distance-line. */
	public void onMouseUp(MouseUpEvent event) {
		if (event.getNativeButton() != NativeEvent.BUTTON_RIGHT) {
			Coordinate coordinate = getWorldPosition(event);
			if (distanceLine.getOriginalLocation() == null) {
				distanceLine.setGeometry(getFactory().createLineString(new Coordinate[] { coordinate }));
				mapWidget.registerWorldPaintable(distanceLine);
				mapWidget.registerWorldPaintable(lineSegment);
				dispatchState(State.START);
			} else {
				Geometry geometry = (Geometry) distanceLine.getOriginalLocation();
				InsertCoordinateOperation op = new InsertCoordinateOperation(geometry.getNumPoints(), coordinate);
				geometry = op.execute(geometry);
				distanceLine.setGeometry(geometry);
				tempLength = (float) geometry.getLength();
				updateMeasure(event, true);
				dispatchState(State.CLICK);
			}
			mapWidget.render(mapWidget.getMapModel(), RenderGroup.VECTOR, RenderStatus.UPDATE);
		}
	}

	/** Update the drawing while moving the mouse. */
	public void onMouseMove(MouseMoveEvent event) {
		if (isMeasuring() && distanceLine.getOriginalLocation() != null) {
			updateMeasure(event, false);
			dispatchState(State.MOVE);
		}
	}

	/** Stop the measuring, and remove all graphics from the map. */
	public void onDoubleClick(DoubleClickEvent event) {
		tempLength = 0;
		mapWidget.unregisterWorldPaintable(distanceLine);
		mapWidget.unregisterWorldPaintable(lineSegment);
		distanceLine.setGeometry(null);
		lineSegment.setGeometry(null);
		if (panel != null) {
			panel.destroy();
		}
		dispatchState(State.STOP);
	}

	protected void updateMeasure(MouseEvent event, boolean complete) {
		Geometry lastClickedLineGeometry = (Geometry) distanceLine.getOriginalLocation();
		Coordinate lastClickedCoordinate = lastClickedLineGeometry.getCoordinates()[distanceLine.getGeometry()
				.getNumPoints() - 1];
		Coordinate mouseCoordinate = getWorldPosition(event);
		lineSegment.setGeometry(getFactory().createLineString(
				new Coordinate[] { lastClickedCoordinate, mouseCoordinate }));
		mapWidget.render(mapWidget.getMapModel(), RenderGroup.VECTOR, RenderStatus.UPDATE);
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private void dispatchState(State state) {
		for (MeasureDistanceHandler handler : handlers) {
			handler.onChange(state, context);
		}	
	}

	private boolean isMeasuring() {
		return distanceLine.getGeometry() != null;
	}

	/**
	 * The factory can only be used after the MapModel has initialized, that is why this getter exists...
	 * 
	 * @return geometry factory
	 */
	protected GeometryFactory getFactory() {
		if (factory == null) {
			factory = mapWidget.getMapModel().getGeometryFactory();
		}
		return factory;
	}

	/**
	 * Context to pass to our handlers.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class MeasureDistanceContextImpl implements MeasureDistanceContext {

		@Override
		public Geometry getGeometry() {
			return distanceLine == null ? null : (Geometry) distanceLine.getOriginalLocation();
		}

		@Override
		public double getPreviousDistance() {
			return tempLength;
		}

		@Override
		public double getCurrentDistance() {
			return tempLength + getRadius();
		}

		@Override
		public double getPreviousArea() {
			return getGeometry() == null ? 0 : getArea(getGeometry());
		}

		@Override
		public double getCurrentArea() {
			Geometry geometry = getGeometry();
			if (geometry != null) {
				InsertCoordinateOperation op = new InsertCoordinateOperation(geometry.getNumPoints(),
						getCurrentCoordinate());
				geometry = op.execute(geometry);
				return getArea(getGeometry());
			} else {
				return 0;
			}
		}

		@Override
		public double getRadius() {
			if (lineSegment.getOriginalLocation() != null) {
				return (float) ((Geometry) lineSegment.getOriginalLocation()).getLength();
			} else {
				return 0;
			}
		}

		@Override
		public Coordinate getPreviousCoordinate() {
			if (getLastSegment() != null && getLastSegment().getNumPoints() > 0) {
				return getLastSegment().getCoordinates()[0];
			} else {
				return null;
			}
		}

		@Override
		public Coordinate getCurrentCoordinate() {
			if (getLastSegment() != null && getLastSegment().getNumPoints() > 1) {
				return getLastSegment().getCoordinates()[1];
			} else {
				return null;
			}
		}

		protected Geometry getLastSegment() {
			return lineSegment == null ? null : ((Geometry) lineSegment.getOriginalLocation());
		}

		protected double getArea(Geometry geometry) {
			return GeometryService.getArea(GeometryConverter.toDto(geometryFactory.createLinearRing(geometry
					.getCoordinates())));
		}

	}

	/**
	 * Menu item that stop the measuring.
	 * 
	 * @author Pieter De Graef
	 */
	protected class CancelMeasuringAction extends MenuAction {

		private final MeasureDistanceController controller;

		public CancelMeasuringAction(final MeasureDistanceController controller) {
			super(I18nProvider.getMenu().cancelMeasuring(), WidgetLayout.iconQuit);
			this.controller = controller;
			setEnableIfCondition(new MenuItemIfFunction() {

				public boolean execute(Canvas target, Menu menu, MenuItem item) {
					return controller.isMeasuring();
				}
			});
		}

		public void onClick(MenuItemClickEvent event) {
			controller.onDoubleClick(null);
		}
	}
}
