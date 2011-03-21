/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.widget.featureinfo.client.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Mathlib;
import org.geomajas.gwt.client.spatial.WorldViewTransformer;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.FeatureInfoMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.impl.StringBuilderImpl;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.user.client.Timer;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 
 * @author Kristof Heirwegh
 * 
 */
public class TooltipOnMouseoverController extends AbstractGraphicsController {

	private FeatureInfoMessages messages = GWT.create(FeatureInfoMessages.class);
	private Label tooltip;
	private int pixelTolerance;
	private Canvas loadingImg;

	private Coordinate lastPosition; // screen
	private Coordinate currentPosition; // screen
	private Coordinate worldPosition; // world !!
	private int minPixelMove = 5;
	private int showDelay = 250;
	private int layersToSearch = SearchByLocationRequest.SEARCH_ALL_LAYERS;
	private int maxLabelCount = 15;
	private Timer timer;

	private static final String CSS = "<style>.tblcLayerLabel {font-size: 0.9em; font-weight: bold;} "
			+ ".tblcFeatureLabelList {margin: 0; padding-left: 20px;} "
			+ ".tblcFeatureLabel {margin: 0; font-size: 0.9em; font-style: italic;} "
			+ ".tblcMore {padding-top: 5px; font-size: 0.9em; font-style: italic;}</style>";

	public TooltipOnMouseoverController(MapWidget mapWidget, int pixelTolerance) {
		super(mapWidget);
		this.pixelTolerance = pixelTolerance;
		VLayout vl = new VLayout();
		vl.setPadding(5);
		vl.setWidth(26);
		vl.setHeight(26);
		vl.addMember(new Img("[ISOMORPHIC]/geomajas/loading_small.gif", 16, 16));
		loadingImg = vl;
	}

	/**
	 * Initialization.
	 */
	public void onActivate() {
	}

	/** Clean everything up. */
	public void onDeactivate() {
		destroyTooltip();
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		currentPosition = new Coordinate(event.getClientX(), event.getClientY());
		if (lastPosition == null) {
			lastPosition = currentPosition;
		} else {
			double distance = currentPosition.distance(lastPosition);

			if (distance > minPixelMove) {
				lastPosition = currentPosition;
				worldPosition = getWorldPosition(event);
				if (isShowing()) {
					destroyTooltip();
				}
				// place new tooltip after some time
				if (timer == null) {
					timer = new Timer() {
						public void run() {
							showTooltip();
						}
					};
					timer.schedule(showDelay);

				} else {
					timer.cancel();
					timer.schedule(showDelay);
				}
			}
		}
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		onDeactivate();
	}

	// ----------------------------------------------------------

	private boolean isShowing() {
		return (tooltip != null);
	}

	private void showTooltip() {
		if (!isShowing()) {
			// SC.logWarn("Showing tooltip");
			createTooltip((int) currentPosition.getX(), (int) currentPosition.getY());
			getData();
		}
	}

	private void writeLayerStart(StringBuilderImpl sb, String label) {
		sb.append("<span class='tblcLayerLabel'>");
		sb.append(label);
		sb.append("</span><ul class='tblcFeatureLabelList'>");
	}

	private void writeFeature(StringBuilderImpl sb, String label) {
		sb.append("<li class='tblcFeatureLabel'>");
		sb.append(label);
		sb.append("</li>");
	}

	private void writeLayerEnd(StringBuilderImpl sb) {
		sb.append("</ul>");
	}

	private void writeNone(StringBuilderImpl sb) {
		sb.append("<span class='tblcMore'>(");
		sb.append(messages.tooltipOnMouseoverNoResult());
		sb.append(")</span>");
	}

	private void writeTooMany(StringBuilderImpl sb, int tooMany) {
		sb.append("<span class='tblcMore'>");
		sb.append("" + tooMany);
		sb.append("</span>");
	}

	private void setTooltipData(Coordinate coordUsedForRetrieval,
			Map<String, List<org.geomajas.layer.feature.Feature>> featureMap) {
		if (coordUsedForRetrieval.equals(worldPosition) && tooltip != null) {
			StringBuilderImpl sb = new StringBuilderImpl.ImplStringAppend();
			sb.append(CSS);
			int widest = 10;
			int count = 0;
			for (VectorLayer layer : mapWidget.getMapModel().getVectorLayers()) {
				if (featureMap.containsKey(layer.getServerLayerId())) {
					List<org.geomajas.layer.feature.Feature> features = featureMap.get(layer.getServerLayerId());
					if (features.size() > 0) {
						if (count < maxLabelCount) {
							writeLayerStart(sb, layer.getLabel());
							if (widest < layer.getLabel().length()) {
								widest = layer.getLabel().length();
							}
							for (org.geomajas.layer.feature.Feature feature : features) {
								if (count < maxLabelCount) {
									writeFeature(sb, feature.getLabel());
									if (widest < feature.getLabel().length()) {
										widest = feature.getLabel().length();
									}
								}
								count++;
							}
							writeLayerEnd(sb);
						} else {
							count += features.size();
						}
					}
				}
			}
			if (count > maxLabelCount) {
				writeTooMany(sb, count - maxLabelCount);
			} else if (count == 0) {
				writeNone(sb);
			}
			tooltip.removeChild(loadingImg);
			tooltip.setContents("<div style='margin:5px; width: " + (widest * 5 + 15) + "px'>" + sb.toString()
					+ "</div>");
		} // else - mouse moved between request and data retrieval
	}

	private void getData() {
		Point point = mapWidget.getMapModel().getGeometryFactory().createPoint(worldPosition);
		final Coordinate coordUsedForRetrieval = worldPosition;

		SearchByLocationRequest request = new SearchByLocationRequest();
		request.setLocation(GeometryConverter.toDto(point));
		request.setCrs(mapWidget.getMapModel().getCrs());
		request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
		request.setSearchType(layersToSearch);
		request.setBuffer(calculateBufferFromPixelTolerance());
		request.setFeatureIncludes(GwtCommandDispatcher.getInstance().getLazyFeatureIncludesSelect());
		request.setLayerIds(getServerLayerIds(mapWidget.getMapModel()));

		GwtCommand commandRequest = new GwtCommand("command.feature.SearchByLocation");
		commandRequest.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(commandRequest, new CommandCallback() {
			public void execute(CommandResponse commandResponse) {
				if (commandResponse instanceof SearchByLocationResponse) {
					SearchByLocationResponse response = (SearchByLocationResponse) commandResponse;
					setTooltipData(coordUsedForRetrieval, response.getFeatureMap());
				}
			}
		});
	}

	private void createTooltip(int x, int y) {
		tooltip = new Label();
		tooltip.setBackgroundColor("white");
		tooltip.setShowShadow(true);
		tooltip.setOpacity(85);
		tooltip.setBorder("thin solid #AAAAAA");
		tooltip.moveTo(x + 12, y + 12);
		tooltip.addChild(loadingImg);
		tooltip.setAutoWidth();
		tooltip.setAutoHeight();
		tooltip.draw();
	}

	private void destroyTooltip() {
		if (tooltip != null) {
			tooltip.destroy();
			tooltip = null;
		}
	}

	private String[] getServerLayerIds(MapModel mapModel) {
		List<String> layerIds = new ArrayList<String>();
		for (VectorLayer layer : mapModel.getVectorLayers()) {
			if (layer.isShowing()) {
				layerIds.add(layer.getServerLayerId());
			}
		}
		return layerIds.toArray(new String[layerIds.size()]);
	}

	private double calculateBufferFromPixelTolerance() {
		WorldViewTransformer transformer = mapWidget.getMapModel().getMapView().getWorldViewTransformer();
		Coordinate c1 = transformer.viewToWorld(new Coordinate(0, 0));
		Coordinate c2 = transformer.viewToWorld(new Coordinate(pixelTolerance, 0));
		return Mathlib.distance(c1, c2);
	}

}
