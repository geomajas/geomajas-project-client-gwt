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

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.gwt.client.action.MenuAction;
import org.geomajas.gwt.client.action.menu.ToggleSnappingAction;
import org.geomajas.gwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.spatial.geometry.operation.InsertCoordinateOperation;
import org.geomajas.gwt.client.util.DistanceFormat;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.gwt.client.widget.MapWidget.RenderStatus;

/**
 * <p>
 * Controller that measures distances on the map, by clicking points. The actual distances are displayed in a label at
 * the top left of the map.
 * </p>
 * 
 * @author Pieter De Graef
 * @author Oliver May
 */
public class MeasureDistanceController extends AbstractSnappingController {

	private static final ShapeStyle LINE_STYLE_1 = new ShapeStyle("#FFFFFF", 0, "#FF9900", 1, 2);

	private static final ShapeStyle LINE_STYLE_2 = new ShapeStyle("#FFFFFF", 0, "#FF5500", 1, 2);

	private boolean showArea;

	private boolean showCoordinate;

	private GfxGeometry distanceLine;

	private GfxGeometry lineSegment;

	private VLayout panel;

	private DistanceLabel label;

	private DistanceLabel areaLabel;

	private DistanceLabel coordinateLabel;

	private GeometryFactory factory;

	private float tempLength;

	private Menu menu;

	private GeometryFactory geometryFactory;

	private MeasureDistanceInfoHandler infoHandler;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * Construct a measureDistanceController. Default is to display the total distance and last line distance.
	 *
	 * @param mapWidget the mapwidget where the distance is measured on.
	 */
	public MeasureDistanceController(MapWidget mapWidget) {
		this(mapWidget, false, false, null);
	}
	
	/**
	 * Construct a measureDistanceController. Default is to display the total distance and last line distance.
	 *
	 * @param mapWidget the mapwidget where the distance is measured on.
	 * @param showArea true if the area should be displayed
	 * @param displayCoordinates the if the coordinates should be displayed.
	 */
	public MeasureDistanceController(MapWidget mapWidget, boolean showArea, boolean displayCoordinates) {
		this(mapWidget, showArea, displayCoordinates, null);
	}

	/**
	 * Construct a measureDistanceController.
	 *
	 * @param mapWidget the mapwidget where the distance is measured on.
	 * @param showArea true if the area should be displayed
	 * @param displayCoordinates the if the coordinates should be displayed.
	 * @param handler a custom info handler or null to use the default
	 */
	public MeasureDistanceController(MapWidget mapWidget, boolean showArea, boolean displayCoordinates, MeasureDistanceInfoHandler handler) {
		super(mapWidget);
		setInfoHandler(handler);
		distanceLine = new GfxGeometry("measureDistanceLine");
		distanceLine.setStyle(LINE_STYLE_1);
		lineSegment = new GfxGeometry("measureDistanceLineSegment");
		lineSegment.setStyle(LINE_STYLE_2);
		this.showArea = showArea;
		this.showCoordinate = displayCoordinates;
		geometryFactory = new GeometryFactory(mapWidget.getMapModel().getPrecision(),
				mapWidget.getMapModel().getSrid());
	}

	public void setInfoHandler(MeasureDistanceInfoHandler handler) {
		this.infoHandler = (handler != null ? handler : new ShowLabelInfoHandler());
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
				distanceLine.setGeometry(getFactory().createLineString(new Coordinate[]{coordinate}));
				mapWidget.registerWorldPaintable(distanceLine);
				mapWidget.registerWorldPaintable(lineSegment);
				showPanel();
				infoHandler.onStart();
			} else {
				Geometry geometry = (Geometry) distanceLine.getOriginalLocation();
				InsertCoordinateOperation op = new InsertCoordinateOperation(geometry.getNumPoints(), coordinate);
				geometry = op.execute(geometry);
				distanceLine.setGeometry(geometry);
				tempLength = (float) geometry.getLength();
				updateMeasure(event, true);
				infoHandler.onDistance(tempLength, 0);
			}
			mapWidget.render(mapWidget.getMapModel(), RenderGroup.VECTOR, RenderStatus.UPDATE);
		}
	}

	/** Update the drawing while moving the mouse. */
	public void onMouseMove(MouseMoveEvent event) {
		if (isMeasuring() && distanceLine.getOriginalLocation() != null) {
			updateMeasure(event, false);
			infoHandler.onDistance(tempLength, (float) ((Geometry) lineSegment.getOriginalLocation()).getLength());
		}
	}

	private void showPanel() {
		panel = new VLayout();
		panel.setParentElement(mapWidget);
//		panel.setValign(VerticalAlignment.TOP);
		panel.setShowEdges(true);
		panel.setWidth(120);
		panel.setPadding(3);
		panel.setLeft(mapWidget.getWidth() - 130);
		panel.setTop(-80);
		panel.setBackgroundColor("#FFFFFF");
		panel.setAnimateTime(500);


		label = new DistanceLabel();
		areaLabel = new DistanceLabel();
		coordinateLabel = new DistanceLabel();

		panel.addMember(label);
		if (showArea) {
			panel.addMember(areaLabel);
		}
		if (showCoordinate) {
			panel.addMember(coordinateLabel);
		}

		panel.animateMove(mapWidget.getWidth() - 130, 10);
	}

	private void updateMeasure(MouseEvent event, boolean complete) {
		Geometry geometry = (Geometry) distanceLine.getOriginalLocation();
		Coordinate coordinate1 = geometry.getCoordinates()[distanceLine.getGeometry().getNumPoints() - 1];
		Coordinate coordinate2 = getWorldPosition(event);
		lineSegment.setGeometry(getFactory().createLineString(new Coordinate[] { coordinate1, coordinate2 }));
		mapWidget.render(mapWidget.getMapModel(), RenderGroup.VECTOR, RenderStatus.UPDATE);
		label.setDistance(tempLength, (float) ((Geometry) lineSegment.getOriginalLocation()).getLength());
		
		if (showArea && complete) {

			double area = GeometryService.getArea(GeometryConverter.toDto(geometryFactory.
							createLinearRing(geometry.getCoordinates())));

			areaLabel.setArea(DistanceFormat.asMapArea(mapWidget, area));
		}

		if (showCoordinate && complete) {
			coordinateLabel.setCoordinate(coordinate2.getX(), coordinate2.getY());
		}
	}

	/** Stop the measuring, and remove all graphics from the map. */
	public void onDoubleClick(DoubleClickEvent event) {
		tempLength = 0;
		mapWidget.unregisterWorldPaintable(distanceLine);
		mapWidget.unregisterWorldPaintable(lineSegment);
		distanceLine.setGeometry(null);
		lineSegment.setGeometry(null);
		infoHandler.onStop();
		if (panel != null) {
			panel.destroy();
		}
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private boolean isMeasuring() {
		return distanceLine.getGeometry() != null;
	}

	/**
	 * The factory can only be used after the MapModel has initialized, that is why this getter exists...
	 *
	 * @return geometry factory
	 */
	private GeometryFactory getFactory() {
		if (factory == null) {
			factory = mapWidget.getMapModel().getGeometryFactory();
		}
		return factory;
	}

	// -------------------------------------------------------------------------
	// Private classes:
	// -------------------------------------------------------------------------

	/**
	 * The label that shows the distances.
	 *
	 * @author Pieter De Graef
	 */
	private class DistanceLabel extends Label {

		public DistanceLabel() {
			super();
			setPadding(3);
			setAutoHeight();
		}

		public void setDistance(float totalDistance, float radius) {
			String total = DistanceFormat.asMapLength(mapWidget, totalDistance);
			String r = DistanceFormat.asMapLength(mapWidget, radius);
			String dist = I18nProvider.getMenu().getMeasureDistanceString(total, r);
			setContents("<div><b>" + I18nProvider.getMenu().distance() + "</b>:</div><div style='margin-top:5px;'>"
					+ dist + "</div>");
		}

		public void setArea(String area) {
			String areaString = I18nProvider.getMenu().getMeasureAreaString(area);
			setContents("<div><b>" + I18nProvider.getMenu().area() + "</b>:</div><div style='margin-top:5px;'>"
					+ areaString + "</div>");
		}

		public void setCoordinate(double x, double y) {


			String coordinate = I18nProvider.getMenu().getMeasureCoordinateString(NumberFormat.getFormat(".##").
					format(x), NumberFormat.getFormat(".##").format(y));
			setContents("<div><b>" + I18nProvider.getMenu().coordinate() + "</b>:</div><div style='margin-top:5px;'>"
					+ coordinate + "</div>");
		}

	}

	/**
	 * Menu item that stop the measuring
	 * 
	 * @author Pieter De Graef
	 */
	private class CancelMeasuringAction extends MenuAction {

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

	/**
	 * Callback that steers the label.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class ShowLabelInfoHandler implements MeasureDistanceInfoHandler {

		@Override
		public void onStart() {
			label = new DistanceLabel();
			label.setDistance(0, 0);
			label.animateMove(mapWidget.getWidth() - 130, 10);
		}

		@Override
		public void onDistance(double totalDistance, double lastSegment) {
			label.setDistance((float)totalDistance, (float)lastSegment);
		}

		@Override
		public void onStop() {
			if (label != null) {
				label.destroy();
			}
		}

	}

}
