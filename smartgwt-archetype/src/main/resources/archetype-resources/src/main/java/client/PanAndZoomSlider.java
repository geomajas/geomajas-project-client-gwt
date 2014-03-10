#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
package ${package}.client;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.controller.ZoomInAndOutController;
import org.geomajas.gwt.client.gfx.paintable.Image;
import org.geomajas.gwt.client.gfx.paintable.mapaddon.PanButtonCollection;
import org.geomajas.gwt.client.gfx.paintable.mapaddon.SingleMapAddon;
import org.geomajas.gwt.client.gfx.paintable.mapaddon.ZoomSlider;
import org.geomajas.gwt.client.util.ImageUtil;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * Combined pan and zoom slider.
 *
 * @author Oliver May TODO: this should move to the GWT face
 */
public class PanAndZoomSlider extends ZoomSlider {

	private static final String IMAGE_FOLDER = Geomajas.getIsomorphicDir() + "geomajas/mapaddon/";

	private static final Integer SLIDER_WIDTH = 20;

	private static final Integer SLIDER_PART_HEIGHT = 13;

	private static final Integer SLIDER_UNIT_WIDTH = 25;

	private static final Integer SLIDER_UNIT_HEIGHT = 14;

	private static final String PANID = "myPanBTNCollection";

	private static final String ZOOMID = "myZoomAddon";

	private static final String ZOOMRECTID = "myZoomRectAddon";

	private static final String ZOOM_IN = "zoomIn";

	private static final String ZOOM_OUT = "zoomOut";

	private static final String ICON = "GeodeskIcon";

	/**
	 * Convenience method to add the zoomslider to the map.
	 * <p>
	 * Call this only when Map has been initialized (eg. in onMapmodelChange)!!
	 *
	 * @param mapWidget
	 */
	public static void addPanAndZoomSliderTo(MapWidget mapWidget) {
		// ----- Pan -----
		PanButtonCollection panButtons = new PanButtonCollection(PANID, mapWidget);
		panButtons.setHorizontalMargin(5);
		panButtons.setVerticalMargin(5);
		mapWidget.registerMapAddon(panButtons);

		Image zoomToLocationIcon = ImageUtil.createRectangleImage("zoomToLocationIcon", Geomajas.getIsomorphicDir()
				+ "geomajas/mapaddon/zoomToLocation.png", 0, 0, SLIDER_WIDTH, SLIDER_WIDTH);
		SingleMapAddon zoomToLocation = new SingleMapAddon("zoomToLocation", zoomToLocationIcon, mapWidget,
				new ZoomToLocationController(mapWidget));
		zoomToLocation.setHorizontalMargin(25);
		zoomToLocation.setVerticalMargin(72);
		mapWidget.registerMapAddon(zoomToLocation);


		// ----- zoom -----
		PanAndZoomSlider mzs = new PanAndZoomSlider(ZOOMID, mapWidget);
		mzs.setHorizontalMargin(25);
		mzs.setVerticalMargin(95);
		mapWidget.registerMapAddon(mzs);
		mapWidget.getMapModel().getMapView().addMapViewChangedHandler(mzs);

		// // ----- ZoomToRectangle -----
		// ZoomToRectangleAddon zoomToRectangleAddon = new ZoomToRectangleAddon(ZOOMRECTID, mapWidget);
		// zoomToRectangleAddon.setHorizontalMargin(19);
		// zoomToRectangleAddon.setVerticalMargin(62);
		// mapWidget.registerMapAddon(zoomToRectangleAddon);
	}

	public PanAndZoomSlider(String id, MapWidget mapWidget) {
		super(id, mapWidget);
		init();
	}

	// -------------------------------------------------

	private void init() {
		MapWidget mapWidget = getMapWidget();

		Image sliderUnit = ImageUtil.createRectangleImage(ZoomSlider.SLIDER_UNIT + "icon", IMAGE_FOLDER
				+ "sliderUnit.png", 0, 0, SLIDER_UNIT_WIDTH, SLIDER_UNIT_HEIGHT);
		Image backgroundPart = ImageUtil.createRectangleImage(ZoomSlider.SLIDER + "Bg", IMAGE_FOLDER + "sliderbg.png",
				0, 0, SLIDER_WIDTH, SLIDER_PART_HEIGHT);
		Image in = ImageUtil.createSquareImage(ZOOM_IN + ICON, Geomajas.getIsomorphicDir()
				+ "geomajas/mapaddon/zoomPlus.png", 0, 0, SLIDER_WIDTH);
		Image sliderTop = ImageUtil.createSquareImage(ZoomSlider.SLIDER + "Top", IMAGE_FOLDER + "sliderbgtop.png", 0,
				0, SLIDER_WIDTH);
		SingleMapAddon zoomIn = new SingleMapAddon(ZOOM_IN, in, sliderTop, mapWidget, new ZoomInAndOutController(
				mapWidget, 1.01));

		Image out = ImageUtil.createRectangleImage(ZOOM_OUT + ICON, Geomajas.getIsomorphicDir()
				+ "geomajas/mapaddon/zoomMinus.png", 0, 0, SLIDER_WIDTH, SLIDER_WIDTH);
		Image sliderBottom = ImageUtil.createRectangleImage(ZoomSlider.SLIDER + "Bottom", IMAGE_FOLDER
				+ "sliderbgbottom.png", 0, 0, SLIDER_WIDTH, SLIDER_WIDTH);
		SingleMapAddon zoomOut = new SingleMapAddon(ZOOM_OUT, out, sliderBottom, mapWidget, new ZoomInAndOutController(
				mapWidget, 0.99));

		setZoomIn(zoomIn);
		setBackgroundPart(backgroundPart);
		setSliderUnit(sliderUnit);
		setZoomOut(zoomOut);
	}
}
