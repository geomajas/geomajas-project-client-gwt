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
package org.geomajas.gwt.client.map.layer;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.util.Log;
import org.geomajas.gwt2.client.animation.NavigationAnimation;
import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.ViewPortTransformationService;
import org.geomajas.gwt2.client.map.ZoomOption;
import org.geomajas.plugin.wms.client.capabilities.WmsLayerInfo;
import org.geomajas.plugin.wms.client.layer.WmsLayerConfiguration;
import org.geomajas.plugin.wms.client.layer.WmsLayerImpl;
import org.geomajas.plugin.wms.client.layer.WmsTileConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * SmartGWT implementation of the client WMS layer. This is an extension of the GWT2 wms layer adding support for the
 * SmartGWT map.
 *
 * @author Oliver May
 */
public class ClientWmsLayer extends WmsLayerImpl {


	/**
	 * Create a new Client WMS layer.
	 *
	 * @param title the title
	 * @param wmsConfig the wms configuration
	 * @param tileConfig the tile configuration
	 */
	public ClientWmsLayer(String title, WmsLayerConfiguration wmsConfig, WmsTileConfiguration tileConfig) {
		this(title, wmsConfig, tileConfig, null);
	}

	/**
	 * Create a new Client WMS layer.
	 *
	 * @param title the title
	 * @param wmsConfig the wms configuration
	 * @param tileConfig the tile configuration
	 * @param layerCapabilities the layer capabilities or null
	 */
	public ClientWmsLayer(String title, WmsLayerConfiguration wmsConfig,
			WmsTileConfiguration tileConfig,
			WmsLayerInfo layerCapabilities) {
		super(title, wmsConfig, tileConfig, layerCapabilities);
	}

	/**
	 * Set the map model on this layer.
	 *
	 * @param mapModel the mapModel.
	 */
	public void setMapModel(MapModel mapModel) {
		setViewPort(new SmartGwtViewport(mapModel));
	}

	/**
	 * SmartGwt implementation of the GWT2 viewport. This is intended for internal use in the client WMS layer, as it
	 * does not implement all ViewPort methods. It can however be extended to fully support everything.
	 */
	private class SmartGwtViewport implements ViewPort {

		private final MapModel mapModel;

		private final List<Double> fixedScales = new ArrayList<Double>();

		public SmartGwtViewport(MapModel mapModel) {
			this.mapModel = mapModel;

			//Calculate fixed scales based on the resolutions.
			//FIXME: what to do when no fixed resolutions exist.
			if (mapModel.getMapView().getResolutions() == null || mapModel.getMapView().getResolutions().size() < 1) {
				RuntimeException e = new RuntimeException("Error while adding Client WMS layer, " +
						"the map should define a list of resolutions.");
				Log.logError("Error while adding Client WMS layer.", e);
				throw e;
			}
			for (Double resolution : mapModel.getMapView().getResolutions()) {
				fixedScales.add(1 / resolution);
			}
			Collections.sort(fixedScales);
		}

		@Override
		public org.geomajas.geometry.Bbox getMaximumBounds() {
			return mapModel.getMapView().getMaxBounds().toDtoBbox();
		}

		@Override
		public double getMinimumScale() {
			if (fixedScales.size() == 0) {
				return 0;
			}
			return fixedScales.get(0);
		}

		@Override
		public double getMaximumScale() {
			if (fixedScales.size() == 0) {
				return Double.MAX_VALUE;
			}
			return fixedScales.get(fixedScales.size() - 1);
		}

		@Override
		public int getFixedScaleCount() {
			return fixedScales.size();
		}

		@Override
		public double getFixedScale(int index) {
			if (index < 0) {
				throw new IllegalArgumentException("Scale index cannot be found.");
			}
			if (index >= fixedScales.size()) {
				throw new IllegalArgumentException("Scale index cannot be found.");
			}
			return fixedScales.get(index);
		}

		@Override
		public int getFixedScaleIndex(double scale) {
			double minimumScale = getMinimumScale();
			if (scale <= minimumScale) {
				return 0;
			}
			double maximumScale = getMaximumScale();
			if (scale >= maximumScale) {
				return fixedScales.size() - 1;
			}

			for (int i = 0; i < fixedScales.size(); i++) {
				double lower = fixedScales.get(i);
				double upper = fixedScales.get(i + 1);
				if (scale <= upper && scale > lower) {
					if (Math.abs(upper - scale) >= Math.abs(lower - scale)) {
						return i;
					} else {
						return i + 1;
					}
				}
			}
			return 0;
		}

		@Override
		public int getMapWidth() {
			return mapModel.getMapView().getWidth();
		}

		@Override
		public int getMapHeight() {
			return mapModel.getMapView().getWidth();
		}

		@Override
		public String getCrs() {
			return mapModel.getCrs();
		}

		@Override
		public Coordinate getPosition() {
			return mapModel.getMapView().getPanOrigin();
		}

		@Override
		public double getScale() {
			return mapModel.getMapView().getCurrentScale();
		}

		@Override
		public View getView() {
			return new View(getPosition(), getScale());
		}

		@Override
		public org.geomajas.geometry.Bbox getBounds() {
			return mapModel.getMapView().getBounds().toDtoBbox();
		}

		@Override
		public void registerAnimation(NavigationAnimation navigationAnimation) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void applyPosition(Coordinate coordinate) {
			mapModel.getMapView().setCenterPosition(coordinate);
		}

		@Override
		public void applyScale(double v) {
			applyScale(v, ZoomOption.FREE);
		}

		private MapView.ZoomOption convertZoomOption(ZoomOption zoomOption) {
			switch (zoomOption) {
				case FREE:
					return MapView.ZoomOption.EXACT;
				case LEVEL_FIT:
					return MapView.ZoomOption.LEVEL_FIT;
				case LEVEL_CLOSEST:
				default:
					return MapView.ZoomOption.LEVEL_CLOSEST;
			}
		}

		@Override
		public void applyScale(double v, ZoomOption zoomOption) {
			mapModel.getMapView().setCurrentScale(v, convertZoomOption(zoomOption));
		}

		@Override
		public void applyBounds(org.geomajas.geometry.Bbox bbox) {
			applyBounds(bbox, ZoomOption.FREE);
		}

		@Override
		public void applyBounds(org.geomajas.geometry.Bbox bbox, ZoomOption zoomOption) {
			mapModel.getMapView().applyBounds(new Bbox(bbox), convertZoomOption(zoomOption));
		}

		@Override
		public void applyView(View view) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void applyView(View view, ZoomOption zoomOption) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ViewPortTransformationService getTransformationService() {
			throw new UnsupportedOperationException();
		}

		@Override
		public double toScale(double v) {
			throw new UnsupportedOperationException();
		}

		@Override
		public org.geomajas.geometry.Bbox asBounds(View view) {
			throw new UnsupportedOperationException();
		}

		@Override
		public View asView(org.geomajas.geometry.Bbox bbox, ZoomOption zoomOption) {
			throw new UnsupportedOperationException();
		}
	}

}
