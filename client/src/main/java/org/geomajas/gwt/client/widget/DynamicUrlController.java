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

package org.geomajas.gwt.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.map.event.MapViewChangedHandler;
/**
 * <p>
 * Includes x, y, r in the URL so that it can be used to bookmark the current map position and zoom level.
 * The map will navigate to x, y, r in case it is included in the bookmarked URL.
 * </p>
 *
 * @author Dosi Bingov
 *
 * @since 1.15.0
 */
@Api
public final class DynamicUrlController implements MapViewChangedHandler {
	private static DynamicUrlController instance;

	private boolean zoomedTo;

	private MapWidget mapWidget;

	private DynamicUrlParser urlParser;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new DynamicUrlController instance. Private because register method is responsible for the creation.
	 *
	 * @since 1.15.0
	 */
	private DynamicUrlController() {
		urlParser = new DynamicUrlParser();
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------
	private void extractHash() {
		String hash = Window.Location.getHash();

		GWT.log("DynamicUrlController hash => " + hash);

		if (null != hash && !hash.isEmpty()) {
			urlParser.parseHash(hash.substring(1));
		}
	}

	/**
	 * Sets the map.
	 *
	 * @param mapWidget
	 */
	private void setMap(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
		initHandlers();
	}

	/**
	 * Initialise all handlers used in {@link DynamicUrlController}.
	 */
	private void initHandlers() {
		mapWidget.getMapModel().getMapView().addMapViewChangedHandler(this);

		mapWidget.getMapModel().runWhenInitialized(new Runnable() {

			public void run() {
				zoomTo();
			}
		});
	}

	/**
	 * Changes hash part of the URL.
	 *
	 * @param newHash x, y, r hash string
	 */
	private void changeHash(String newHash) {
		History.newItem(newHash);
	}

	/**
	 * Zooms to x, y, r coordinates on the map taken from the {@link DynamicUrlParser}.
	 */
	private void zoomTo() {

		if (urlParser.isValid() && !zoomedTo) {

			final Timer timer = new Timer() {
				@Override
				public void run() {
					mapWidget.getMapModel().getMapView().setCurrentScale(urlParser.getR(), MapView.ZoomOption.EXACT);
					mapWidget.getMapModel().getMapView().setCenterPosition(new Coordinate(urlParser.getX(),
							urlParser.getY()));

					this.cancel();
					zoomedTo = true;

				}
			};
			//run one time after 0.1s to be sure that the map has been resized.
			timer.schedule(100);
		}
	}

	@Override
	public void onMapViewChanged(MapViewChangedEvent event) {
		double resolution = mapWidget.getMapModel().getMapView().getViewState().getScale();
		double x = mapWidget.getMapModel().getMapView().getViewState().getX();
		double y = mapWidget.getMapModel().getMapView().getViewState().getY();
		DynamicUrlParser parser = new DynamicUrlParser(x, y, resolution);
		changeHash(parser.getHash());
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * Help class that will parse hash part of the URL.
	 *
	 * x, y, r parser.
	 */
	private class DynamicUrlParser {
		private boolean isValid;

		private double y;

		private double x;

		private double r;

		public DynamicUrlParser() {
		}

		public DynamicUrlParser(double x, double y, double r) {
			this.x = x;
			this.y = y;
			this.r = r;
		}

		public DynamicUrlParser(String hash) {
			parseHash(hash);
		}

		public void setXYZ(Coordinate xy, double z) {
			x = xy.getX();
			y = xy.getY();
			this.r = z;
		}

		private void parseHash(String hash) {
			String[] xyr = hash.split(",");

			isValid = true;

			try {

				for (String s : xyr) {

					if (s.indexOf('x') != -1) {
						x = Double.parseDouble(s.substring(1));
					} else if (s.indexOf('y') != -1) {
						y = Double.parseDouble(s.substring(1));
					} else if (s.indexOf('r') != -1) {
						r = Double.parseDouble(s.substring(1));
					} else {
						GWT.log("invalid geomajas xyr hash Exception !");
						isValid = false;
					}
				}


			} catch (NumberFormatException ex) {
				GWT.log("invalid geomajas xyr hash Exception !", ex);
				isValid = false;
			}

		}

		public double getY() {
			return y;
		}

		public double getX() {
			return x;
		}

		public double getR() {
			return r;
		}

		public String getHash() {
			return "x" + getX() + ",y" + getY() + ",r" + r;
		}

		public boolean isValid() {
			return isValid;
		}
	}

	/**
	 * Creates instance of {@link DynamicUrlController} that will listen to map changes and apply current x,y,r in the
	 * hash part of the URL.
	 *
	 * This method should be called after {@link MapWidget} is created.
	 *
	 * @param mapWidget main map widget of the application.
	 */
	@Api
	public static void register(MapWidget mapWidget) {
		if (null == instance) {
			instance = new DynamicUrlController();
		}

		instance.extractHash();
		instance.setMap(mapWidget);
	}

}
