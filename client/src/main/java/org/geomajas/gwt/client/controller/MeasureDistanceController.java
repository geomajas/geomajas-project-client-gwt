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

	protected static final ShapeStyle LINE_STYLE_1 = new ShapeStyle("#FFFFFF", 0, "#FF9900", 1, 2);

	protected static final ShapeStyle LINE_STYLE_2 = new ShapeStyle("#FFFFFF", 0, "#FF5500", 1, 2);

	protected boolean showArea;

	protected boolean showCoordinate;

	protected GfxGeometry distanceLine;

	protected GfxGeometry lineSegment;

	protected VLayout panel;

	protected DistanceLabel distanceLabel;

	protected AreaLabel areaLabel;

	protected CoordinateLabel coordinateLabel;

	protected GeometryFactory factory;

	protected float tempLength;

	protected Menu menu;

	protected GeometryFactory geometryFactory;

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
		this.showArea = showArea;
		this.showCoordinate = displayCoordinates;
		geometryFactory = new GeometryFactory(mapWidget.getMapModel().getPrecision(),
				mapWidget.getMapModel().getSrid());
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
			} else {
				Geometry geometry = (Geometry) distanceLine.getOriginalLocation();
				InsertCoordinateOperation op = new InsertCoordinateOperation(geometry.getNumPoints(), coordinate);
				geometry = op.execute(geometry);
				distanceLine.setGeometry(geometry);
				tempLength = (float) geometry.getLength();
				updateMeasure(event, true);
			}
			mapWidget.render(mapWidget.getMapModel(), RenderGroup.VECTOR, RenderStatus.UPDATE);
		}
	}

	/** Update the drawing while moving the mouse. */
	public void onMouseMove(MouseMoveEvent event) {
		if (isMeasuring() && distanceLine.getOriginalLocation() != null) {
			updateMeasure(event, false);
		}
	}

	protected void showPanel() {
		panel = new VLayout();
		panel.setParentCanvas(mapWidget);
		panel.setWidth(120);
		panel.setLeft(mapWidget.getWidth() - 130);
		panel.setTop(-80);
		panel.setStyleName(WidgetLayout.STYLE_MEASURE_DISTANCE_PANEL);
		panel.setAnimateTime(500);

		addLabelsToPanel();

		panel.animateMove(mapWidget.getWidth() - 130, 10);
	}

	protected void addLabelsToPanel() {
		Label header = new Label("<div class=\"" + WidgetLayout.MEASURE_DISTANCE_PANEL_HEADER + "\" ><b>" +
				I18nProvider.getMenu().measureDistancePanelHeader() + "</b></div>");
		header.setHeight100();

		panel.addMember(header);

		distanceLabel = new DistanceLabel();
		panel.addMember(distanceLabel);

		if (showArea) {
			areaLabel = new AreaLabel();
			panel.addMember(areaLabel);
		}
		if (showCoordinate) {
			coordinateLabel = new CoordinateLabel();
			panel.addMember(coordinateLabel);
		}
	}

	protected void updateMeasure(MouseEvent event, boolean complete) {
		Geometry lastClickedLineGeometry = (Geometry) distanceLine.getOriginalLocation();
		Coordinate lastClickedCoordinate =
				lastClickedLineGeometry.getCoordinates()[distanceLine.getGeometry().getNumPoints() - 1];
		Coordinate mouseCoordinate = getWorldPosition(event);
		lineSegment.setGeometry(getFactory().createLineString(
				new Coordinate[] { lastClickedCoordinate, mouseCoordinate }));
		mapWidget.render(mapWidget.getMapModel(), RenderGroup.VECTOR, RenderStatus.UPDATE);

		updatePanelLabels(complete, lastClickedLineGeometry, lastClickedCoordinate, mouseCoordinate);
	}

	protected void updatePanelLabels(boolean complete, Geometry lastClickedLineGeometry,
									 Coordinate lastClickedCoordinate, Coordinate mouseCoordinate) {
		distanceLabel.setDistance(tempLength, (float) ((Geometry) lineSegment.getOriginalLocation()).getLength());

		if (showArea && complete) {
			double area = getArea(lastClickedLineGeometry);
			areaLabel.setArea(DistanceFormat.asMapArea(mapWidget, area));
		}

		if (showCoordinate && complete) {
			coordinateLabel.setCoordinate(lastClickedCoordinate, mouseCoordinate);
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
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	protected boolean isMeasuring() {
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
	 * Get the area of a geometry.
	 * @param geometry
	 * @return
	 */
	protected double getArea(Geometry geometry) {
		return GeometryService.getArea(GeometryConverter.toDto(geometryFactory.
				createLinearRing(geometry.getCoordinates())));
	}

	// -------------------------------------------------------------------------
	// Private classes:
	// -------------------------------------------------------------------------

	/**
	 * The label that shows the distances.
	 *
	 * @author Pieter De Graef
	 * @author Jan Venstermans
	 */
	protected abstract class LabelForMeasurePanel extends Label {

		private String subtitle;

		private boolean showStaticLabel;

		private boolean showDynamicLabel;

		public LabelForMeasurePanel(String subtitle) {
			this(subtitle, true, false);
		}

		public LabelForMeasurePanel(String subtitle, boolean showStaticLabel, boolean showDynamicLabel) {
			super();
			this.subtitle = subtitle;
			this.showStaticLabel = showStaticLabel;
			this.showDynamicLabel = showDynamicLabel;
			setAutoHeight();
			setStyleName(WidgetLayout.MEASURE_DISTANCE_PANEL_CONTENT);
		}

		public void setStringInDivContents(String ... stringContents) {
			StringBuilder builder = new StringBuilder("<div class=\"" +
					WidgetLayout.MEASURE_DISTANCE_PANEL_SUBTITLE + "\" ><b>" +
					subtitle + "</b>:</div>");
			for (String content : stringContents) {
				if (content != null && !content.isEmpty()) {
					builder.append("<div>" + content + "</div>");
				}
			}
			setContents(builder.toString());
		}

		public String createLabelSection(String label, String value) {
			return I18nProvider.getMenu().measureDistancePaneStyleElement(label, value);
		}

		/* getters */

		public String getSubtitle() {
			return subtitle;
		}

		public boolean isShowStaticLabel() {
			return showStaticLabel;
		}

		public boolean isShowDynamicLabel() {
			return showDynamicLabel;
		}

		/* setters */

		public void setShowStaticLabel(boolean showStaticLabel) {
			this.showStaticLabel = showStaticLabel;
		}

		public void setShowDynamicLabel(boolean showDynamicLabel) {
			this.showDynamicLabel = showDynamicLabel;
		}
	}

	/**
	 * The label that shows the distances.
	 *
	 * @author Jan Venstermans
	 */
	protected class DistanceLabel extends LabelForMeasurePanel {

		public DistanceLabel() {
			super(I18nProvider.getMenu().measureDistancePanelDistanceSubtitle(), true, true);
		}

		public void setDistance(float totalDistance, float radius) {
			String totalString = null;
			String rString = null;
			if (isShowStaticLabel()) {
				String total = DistanceFormat.asMapLength(mapWidget, totalDistance);
				totalString = createLabelSection(
						I18nProvider.getMenu().measureDistancePanelDistanceLastClickLabel(),
						total);
			}
			if (isShowDynamicLabel()) {
				String r = DistanceFormat.asMapLength(mapWidget, radius);
				rString = createLabelSection(
						I18nProvider.getMenu().measureDistancePanelDistanceCurrentLabel(),
						r);
			}
			setStringInDivContents(totalString, rString);
		}
	}

	/**
	 * The label that shows the distances.
	 *
	 * @author Jan Venstermans
	 */
	protected class AreaLabel extends LabelForMeasurePanel {

		public AreaLabel() {
			super(I18nProvider.getMenu().measureDistancePanelAreaSubtitle());
		}

		public void setArea(String area) {
			String areaString = null;
			if (isShowStaticLabel()) {
				areaString = createLabelSection(
						I18nProvider.getMenu().measureDistancePanelAreaLastClickLabel(), area);
			}
			setStringInDivContents(areaString);
		}
	}

	/**
	 * The label that shows the distances.
	 *
	 * @author Jan Venstermans
	 */
	protected class CoordinateLabel extends LabelForMeasurePanel {

		public CoordinateLabel() {
			super(I18nProvider.getMenu().measureDistancePanelCoordinateSubtitle());
		}

		public void setCoordinate(Coordinate lastClickedCoordinate, Coordinate currentCoordinate) {
			String lastClickedCoordinateString = null;
			if (isShowStaticLabel()) {
				lastClickedCoordinateString = createLabelSection(
						I18nProvider.getMenu().measureDistancePanelCoordinateLastClickLabel(),
						coordinateToString(lastClickedCoordinate));
			}
			String currentCoordinateString = null;
			if (isShowDynamicLabel()) {
				currentCoordinateString = createLabelSection(
						I18nProvider.getMenu().measureDistancePanelCoordinateCurrentLabel(),
						coordinateToString(currentCoordinate));
			}
			setStringInDivContents(lastClickedCoordinateString, currentCoordinateString);
		}

		public String coordinateToString(Coordinate coordinate) {
			return I18nProvider.getMenu().getMeasureCoordinateString(
					NumberFormat.getFormat(".##").format(coordinate.getX()),
					NumberFormat.getFormat(".##").format(coordinate.getY()));
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
