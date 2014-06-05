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

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.util.DistanceFormat;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.i18n.client.NumberFormat;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Default {@link MeasureDistanceHandler} that shows a "crock-eye" panel.
 * 
 * @author Jan De Moerloose
 * 
 */
public class MeasureDistancePanel implements MeasureDistanceHandler {

	protected boolean showArea;

	protected boolean showCoordinate;

	protected VLayout panel;

	protected DistanceLabel distanceLabel;

	protected AreaLabel areaLabel;

	protected CoordinateLabel coordinateLabel;

	private MapWidget mapWidget;

	public MeasureDistancePanel(MapWidget mapWidget, boolean showArea, boolean showCoordinate) {
		this.mapWidget = mapWidget;
		this.showArea = showArea;
		this.showCoordinate = showCoordinate;
	}

	@Override
	public void onChange(State state, MeasureDistanceContext context) {
		if (state == State.START) {
			showPanel();
		}
		if (state == State.STOP) {
			if (panel != null) {
				panel.destroy();
			}
		}
		distanceLabel.setDistance(context.getPreviousDistance(), context.getRadius());

		if (state == State.CLICK) {
			if (showArea) {
				areaLabel.setArea(DistanceFormat.asMapArea(mapWidget, context.getPreviousArea()));
			}

			if (showCoordinate) {
				coordinateLabel.setCoordinate(context.getPreviousCoordinate(), context.getCurrentCoordinate());
			}
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
		Label header = new Label("<div class=\"" + WidgetLayout.MEASURE_DISTANCE_PANEL_HEADER + "\" ><b>"
				+ I18nProvider.getMenu().measureDistancePanelHeader() + "</b></div>");
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

		public void setStringInDivContents(String... stringContents) {
			StringBuilder builder = new StringBuilder("<div class=\"" + WidgetLayout.MEASURE_DISTANCE_PANEL_SUBTITLE
					+ "\" ><b>" + subtitle + "</b>:</div>");
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

		public void setDistance(double totalDistance, double radius) {
			String totalString = null;
			String rString = null;
			if (isShowStaticLabel()) {
				String total = DistanceFormat.asMapLength(mapWidget, totalDistance);
				totalString = createLabelSection(I18nProvider.getMenu().measureDistancePanelDistanceLastClickLabel(),
						total);
			}
			if (isShowDynamicLabel()) {
				String r = DistanceFormat.asMapLength(mapWidget, radius);
				rString = createLabelSection(I18nProvider.getMenu().measureDistancePanelDistanceCurrentLabel(), r);
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
				areaString = createLabelSection(I18nProvider.getMenu().measureDistancePanelAreaLastClickLabel(), area);
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
				lastClickedCoordinateString = createLabelSection(I18nProvider.getMenu()
						.measureDistancePanelCoordinateLastClickLabel(), coordinateToString(lastClickedCoordinate));
			}
			String currentCoordinateString = null;
			if (isShowDynamicLabel()) {
				currentCoordinateString = createLabelSection(I18nProvider.getMenu()
						.measureDistancePanelCoordinateCurrentLabel(), coordinateToString(currentCoordinate));
			}
			setStringInDivContents(lastClickedCoordinateString, currentCoordinateString);
		}

		public String coordinateToString(Coordinate coordinate) {
			return I18nProvider.getMenu().getMeasureCoordinateString(
					NumberFormat.getFormat(".##").format(coordinate.getX()),
					NumberFormat.getFormat(".##").format(coordinate.getY()));
		}
	}

}
