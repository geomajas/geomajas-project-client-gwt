/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.widget;

import java.util.List;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.command.dto.GetMapConfigurationResponse;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.UnitType;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.action.menu.AboutAction;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.controller.PanArrowController;
import org.geomajas.gwt.client.gfx.MenuGraphicsContext;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.gfx.paintable.ScaleBar;
import org.geomajas.gwt.client.gfx.painter.CirclePainter;
import org.geomajas.gwt.client.gfx.painter.FeaturePainter;
import org.geomajas.gwt.client.gfx.painter.FeatureTransactionPainter;
import org.geomajas.gwt.client.gfx.painter.GeometryPainter;
import org.geomajas.gwt.client.gfx.painter.ImagePainter;
import org.geomajas.gwt.client.gfx.painter.MapModelPainter;
import org.geomajas.gwt.client.gfx.painter.RasterLayerPainter;
import org.geomajas.gwt.client.gfx.painter.RasterTilePainter;
import org.geomajas.gwt.client.gfx.painter.RectanglePainter;
import org.geomajas.gwt.client.gfx.painter.TextPainter;
import org.geomajas.gwt.client.gfx.painter.VectorLayerPainter;
import org.geomajas.gwt.client.gfx.painter.VectorTilePainter;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.event.FeatureDeselectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectionHandler;
import org.geomajas.gwt.client.map.event.LayerChangedHandler;
import org.geomajas.gwt.client.map.event.LayerLabeledEvent;
import org.geomajas.gwt.client.map.event.LayerShownEvent;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.map.event.MapViewChangedHandler;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.spatial.Bbox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import com.smartgwt.client.widgets.menu.Menu;

/**
 * The map widget is responsible for rendering the map model. It has a Model-View relationship with the map model. A
 * single controller for capturing user events can be set on the map.
 * 
 * @author Pieter De Graef
 */
public class MapWidget extends Canvas implements MapViewChangedHandler, MapModelHandler {

	private static final String IMAGE_NAV_NORTH = "geomajas/nav_up.gif";

	private static final String IMAGE_NAV_SOUTH = "geomajas/nav_down.gif";

	private static final String IMAGE_NAV_WEST = "geomajas/nav_left.gif";

	private static final String IMAGE_NAV_EAST = "geomajas/nav_right.gif";

	private static final String IMAGE_NAV_NORTHWEST = "geomajas/nav_up_left.gif";

	private static final String IMAGE_NAV_NORTHEAST = "geomajas/nav_up_right.gif";

	private static final String IMAGE_NAV_SOUTHWEST = "geomajas/nav_down_left.gif";

	private static final String IMAGE_NAV_SOUTHEAST = "geomajas/nav_down_right.gif";

	private static final String POINTER_CURSOR = Cursor.POINTER.getValue();

	private MapModel mapModel;

	private GraphicsWidget graphics;

	private PainterVisitor painterVisitor;

	protected boolean panButtonsEnabled = true;

	protected boolean scaleBarEnabled = true;

	private ScaleBar scalebar;

	// TODO: implement this: metric and English units; need: formatters, conversions, etc; can be static methods?
	private UnitType unitType;

	private double unitLength;

	private double pixelLength;

	/** Is zooming in and out using the mouse wheel enabled or not? This is true by default. */
	private boolean zoomOnScrollEnabled;

	/** Registration for the zoom on scroll handler. */
	private HandlerRegistration zoomOnScrollRegistration;

	private boolean initialized;

	private boolean resizedHandlerDisabled;

	private Menu defaultMenu;

	protected String applicationId;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public MapWidget(String id, String applicationId) {
		super(id);
		this.applicationId = applicationId;
		mapModel = new MapModel(getID() + "Graphics");
		mapModel.addMapModelHandler(this);
		mapModel.getMapView().addMapViewChangedHandler(this);
		graphics = new GraphicsWidget(this, getID() + "Graphics");
		painterVisitor = new PainterVisitor(graphics);
		mapModel.addFeatureSelectionHandler(new MapWidgetFeatureSelectionHandler(this));

		// Painter registration:
		painterVisitor.registerPainter(new CirclePainter());
		painterVisitor.registerPainter(new RectanglePainter());
		painterVisitor.registerPainter(new TextPainter());
		painterVisitor.registerPainter(new GeometryPainter());
		painterVisitor.registerPainter(new ImagePainter());
		painterVisitor.registerPainter(new MapModelPainter(this));
		painterVisitor.registerPainter(new RasterLayerPainter());
		painterVisitor.registerPainter(new RasterTilePainter());
		painterVisitor.registerPainter(new VectorLayerPainter());
		painterVisitor.registerPainter(new VectorTilePainter(mapModel.getMapView()));
		painterVisitor.registerPainter(new FeatureTransactionPainter(this));

		defaultMenu = new Menu();
		defaultMenu.addItem(new AboutAction());
		setContextMenu(defaultMenu);

		// Resizing should work correctly!
		setWidth100();
		setHeight100();
		setDynamicContents(true);
		addResizedHandler(new MapResizedHandler(this));
		setZoomOnScrollEnabled(true);
	}

	// -------------------------------------------------------------------------
	// Class specific methods:
	// -------------------------------------------------------------------------

	/**
	 * Initialize the MapWidget. This function must always be explicitly called! It fetches the needed configuration
	 * from the server, and applies it.
	 * 
	 * @param applicationId
	 *            application unique id
	 */
	public void initialize() {
		if (!initialized) {
			GwtCommand commandRequest = new GwtCommand("command.configuration.GetMap");
			commandRequest.setCommandRequest(new GetMapConfigurationRequest(id, applicationId));
			GwtCommandDispatcher.getInstance().execute(commandRequest, new CommandCallback() {

				public void execute(CommandResponse response) {
					if (response instanceof GetMapConfigurationResponse) {
						try {
							GetMapConfigurationResponse r = (GetMapConfigurationResponse) response;
							initializationCallback(r);
						} catch (Throwable t) {
							String st = t.getClass().getName() + ": " + t.getMessage();
							for (StackTraceElement ste : t.getStackTrace()) {
								st += "\n" + ste.toString();
							}
							st += "";
						}
					}
				}
			});
		}
	}

	public double getUnitLength() {
		return unitLength;
	}

	public double getPixelLength() {
		return pixelLength;
	}

	protected void initializationCallback(GetMapConfigurationResponse r) {
		if (r.getMapInfo() != null) {
			ClientMapInfo info = r.getMapInfo();
			unitType = info.getDisplayUnitType();
			unitLength = info.getUnitLength();
			pixelLength = info.getPixelLength();
			graphics.setBackgroundColor(info.getBackgroundColor());
			mapModel.initialize(info);
			setPanButtonsEnabled(info.isPanButtonsEnabled());
			setScalebarEnabled(info.isScaleBarEnabled());
			painterVisitor.registerPainter(new FeaturePainter(new ShapeStyle(info.getPointSelectStyle()),
					new ShapeStyle(info.getLineSelectStyle()), new ShapeStyle(info.getPolygonSelectStyle())));

			for (final Layer<?> layer : mapModel.getLayers()) {
				layer.addLayerChangedHandler(new LayerChangedHandler() {

					public void onLabelChange(LayerLabeledEvent event) {
						render(layer, "all");
					}

					public void onVisibleChange(LayerShownEvent event) {
						render(layer, "all");
					}
				});
			}
			initialized = true;
		}
	}

	public void registerPainter(Painter painter) {
		painterVisitor.registerPainter(painter);
	}

	public void unregisterPainter(Painter painter) {
		painterVisitor.unregisterPainter(painter);
	}

	public void setZoomOnScrollEnabled(boolean zoomOnScrollEnabled) {
		if (zoomOnScrollRegistration != null) {
			zoomOnScrollRegistration.removeHandler();
			zoomOnScrollRegistration = null;
		}
		this.zoomOnScrollEnabled = zoomOnScrollEnabled;
		if (zoomOnScrollEnabled) {
			zoomOnScrollRegistration = graphics.addMouseWheelHandler(new ZoomOnScrollController());
		}
	}

	public boolean isZoomOnScrollEnabled() {
		return zoomOnScrollEnabled;
	}

	/**
	 * Apply a new <code>GraphicsController</code> on the map. This controller will handle all mouse-events that are
	 * global for the map. Only one controller can be set at any given time.
	 * 
	 * @param controller
	 *            The new <code>MapController</code> object.
	 */
	public void setController(GraphicsController controller) {
		graphics.setController(controller);
	}

	public void render(Paintable paintable, String status) {
		if (paintable == null) {
			paintable = this.mapModel;
		}
		if ("delete".equals(status)) {
			List<Painter> painters = painterVisitor.getPaintersForObject(paintable);
			for (Painter painter : painters) {
				painter.deleteShape(paintable, graphics);
			}
		} else {
			if ("all".equals(status)) {
				paintable.accept(painterVisitor, mapModel.getMapView().getBounds(), true);
			} else if ("update".equals(status)) {
				paintable.accept(painterVisitor, mapModel.getMapView().getBounds(), false);
			}
		}
	}

	/**
	 * Enables or disables the scalebar. This setting has immediate effect on the map.
	 * 
	 * @param enabled
	 *            set status
	 */
	public void setScalebarEnabled(boolean enabled) {
		scaleBarEnabled = enabled;
		if (scaleBarEnabled) {
			if (null == scalebar) {
				scalebar = new ScaleBar("screen.scalebar");
			}
			scalebar.initialize(unitType, unitLength, new Coordinate(20, graphics.getHeight() - 30));
			scalebar.adjustScale(mapModel.getMapView().getCurrentScale());
			render(scalebar, "all");
		} else {
			if (null != scalebar) {
				render(scalebar, "delete");
				scalebar = null;
			}
		}
	}

	public boolean isScaleBarEnabled() {
		return scaleBarEnabled;
	}

	/**
	 * Enables or disables the panning buttons. This setting has immediate effect on the map.
	 * 
	 * @param enabled
	 *            enabled status
	 */
	public void setPanButtonsEnabled(boolean enabled) {
		panButtonsEnabled = enabled;
		if (enabled) {
			graphics.drawImage("screen.panNImage", Geomajas.getIsomorphicDir() + IMAGE_NAV_NORTH, new Bbox(graphics
					.getWidth() / 2 - 9, 0, 18, 18), new PictureStyle(0.7), false);
			graphics.drawImage("screen.panSImage", Geomajas.getIsomorphicDir() + IMAGE_NAV_SOUTH, new Bbox(graphics
					.getWidth() / 2 - 9, graphics.getHeight() - 18, 18, 18), new PictureStyle(0.7), false);
			graphics.drawImage("screen.panWImage", Geomajas.getIsomorphicDir() + IMAGE_NAV_WEST, new Bbox(0, graphics
					.getHeight() / 2 - 9, 18, 18), new PictureStyle(0.7), false);
			graphics.drawImage("screen.panEImage", Geomajas.getIsomorphicDir() + IMAGE_NAV_EAST, new Bbox(graphics
					.getWidth() - 18, graphics.getHeight() / 2 - 9, 18, 18), new PictureStyle(0.7), false);
			graphics.drawImage("screen.panNWImage", Geomajas.getIsomorphicDir() + IMAGE_NAV_NORTHWEST, new Bbox(0, 0,
					18, 18), new PictureStyle(0.7), false);
			graphics.drawImage("screen.panNEImage", Geomajas.getIsomorphicDir() + IMAGE_NAV_NORTHEAST, new Bbox(
					graphics.getWidth() - 18, 0, 18, 18), new PictureStyle(0.7), false);
			graphics.drawImage("screen.panSWImage", Geomajas.getIsomorphicDir() + IMAGE_NAV_SOUTHWEST, new Bbox(0,
					graphics.getHeight() - 18, 18, 18), new PictureStyle(0.7), false);
			graphics.drawImage("screen.panSEImage", Geomajas.getIsomorphicDir() + IMAGE_NAV_SOUTHEAST, new Bbox(
					graphics.getWidth() - 18, graphics.getHeight() - 18, 18, 18), new PictureStyle(0.7), false);

			graphics.setController("screen.panNImage", new PanArrowController(this, new Coordinate(0, 1)),
					Event.MOUSEEVENTS);
			graphics.setController("screen.panSImage", new PanArrowController(this, new Coordinate(0, -1)),
					Event.MOUSEEVENTS);
			graphics.setController("screen.panWImage", new PanArrowController(this, new Coordinate(-1, 0)),
					Event.MOUSEEVENTS);
			graphics.setController("screen.panEImage", new PanArrowController(this, new Coordinate(1, 0)),
					Event.MOUSEEVENTS);
			graphics.setController("screen.panNWImage", new PanArrowController(this, new Coordinate(-1, 1)),
					Event.MOUSEEVENTS);
			graphics.setController("screen.panNEImage", new PanArrowController(this, new Coordinate(1, 1)),
					Event.MOUSEEVENTS);
			graphics.setController("screen.panSWImage", new PanArrowController(this, new Coordinate(-1, -1)),
					Event.MOUSEEVENTS);
			graphics.setController("screen.panSEImage", new PanArrowController(this, new Coordinate(1, -1)),
					Event.MOUSEEVENTS);

			graphics.setCursor("screen.panNImage", POINTER_CURSOR);
			graphics.setCursor("screen.panSImage", POINTER_CURSOR);
			graphics.setCursor("screen.panWImage", POINTER_CURSOR);
			graphics.setCursor("screen.panEImage", POINTER_CURSOR);
			graphics.setCursor("screen.panNWImage", POINTER_CURSOR);
			graphics.setCursor("screen.panNEImage", POINTER_CURSOR);
			graphics.setCursor("screen.panSWImage", POINTER_CURSOR);
			graphics.setCursor("screen.panSEImage", POINTER_CURSOR);

		} else {
			graphics.deleteShape("screen.panNImage", false);
			graphics.deleteShape("screen.panSImage", false);
			graphics.deleteShape("screen.panWImage", false);
			graphics.deleteShape("screen.panEImage", false);
			graphics.deleteShape("screen.panNWImage", false);
			graphics.deleteShape("screen.panNEImage", false);
			graphics.deleteShape("screen.panSWImage", false);
			graphics.deleteShape("screen.panSEImage", false);
		}
	}

	public boolean isPanButtonsEnabled() {
		return panButtonsEnabled;
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public MapModel getMapModel() {
		return mapModel;
	}

	public MenuGraphicsContext getGraphics() {
		return graphics;
	}

	public PainterVisitor getPainterVisitor() {
		return painterVisitor;
	}

	@Override
	public void setContextMenu(Menu contextMenu) {
		if (null == contextMenu) {
			super.setContextMenu(defaultMenu);
		} else {
			super.setContextMenu(contextMenu);
		}
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	protected void onDraw() {
		final int width = getWidth();
		final int height = getHeight();
		mapModel.getMapView().setSize(width, height);
		graphics.setSize(width, height);
	}

	public void onMapViewChanged(MapViewChangedEvent event) {
		if (initialized) {
			if (scaleBarEnabled && scalebar != null) {
				scalebar.adjustScale(mapModel.getMapView().getCurrentScale());
				render(scalebar, "update");
			}
			render(mapModel, "all");
		}
	}

	public void onMapModelChange(MapModelEvent event) {
		render(mapModel, "all");
	}

	public ShapeStyle getLineSelectStyle() {
		FeaturePainter painter = getFeaturePainter();
		if (painter != null) {
			return painter.getLineSelectStyle();
		}
		return null;
	}

	public void setLineSelectStyle(ShapeStyle lineSelectStyle) {
		FeaturePainter painter = getFeaturePainter();
		if (painter != null) {
			painter.setLineSelectStyle(lineSelectStyle);
		}
	}

	public ShapeStyle getPointSelectStyle() {
		FeaturePainter painter = getFeaturePainter();
		if (painter != null) {
			return painter.getPointSelectStyle();
		}
		return null;
	}

	public void setPointSelectStyle(ShapeStyle pointSelectStyle) {
		FeaturePainter painter = getFeaturePainter();
		if (painter != null) {
			painter.setPointSelectStyle(pointSelectStyle);
		}
	}

	public ShapeStyle getPolygonSelectStyle() {
		FeaturePainter painter = getFeaturePainter();
		if (painter != null) {
			return painter.getPolygonSelectStyle();
		}
		return null;
	}

	public void setPolygonSelectStyle(ShapeStyle polygonSelectStyle) {
		FeaturePainter painter = getFeaturePainter();
		if (painter != null) {
			painter.setPolygonSelectStyle(polygonSelectStyle);
		}
	}

	public void setCursor(Cursor cursor) {
		super.setCursor(cursor);
		graphics.setCursor(null, cursor.getValue());
	}

	public boolean isResizedHandlerDisabled() {
		return resizedHandlerDisabled;
	}

	public void setResizedHandlerDisabled(boolean resizedHandlerDisabled) {
		this.resizedHandlerDisabled = resizedHandlerDisabled;
	}

	public Menu getDefaultMenu() {
		return defaultMenu;
	}

	public void setDefaultMenu(Menu defaultMenu) {
		this.defaultMenu = defaultMenu;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private FeaturePainter getFeaturePainter() {
		List<Painter> painters = painterVisitor.getPaintersForObject(new Feature());
		for (Painter painter : painters) {
			if (painter instanceof FeaturePainter) {
				return (FeaturePainter) painter;
			}
		}
		return null;
	}

	// -------------------------------------------------------------------------
	// Private ResizedHandler class
	// -------------------------------------------------------------------------

	/**
	 * Handles map view and scalebar on resize
	 */
	private class MapResizedHandler implements ResizedHandler {

		private MapWidget map;

		public MapResizedHandler(MapWidget map) {
			this.map = map;
		}

		public void onResized(ResizedEvent event) {
			if (!map.isResizedHandlerDisabled()) {
				try {
					final int width = map.getWidth();
					final int height = map.getHeight();
					mapModel.getMapView().setSize(width, height);
					if (scaleBarEnabled) {
						scalebar.setPosition(new Coordinate(20, graphics.getHeight() - 30));
						render(scalebar, "update");
					}
					if (panButtonsEnabled) {
						setPanButtonsEnabled(false);
						setPanButtonsEnabled(true);
					}

					GWT.log("MapWidget has resized: " + width + "," + height, null);
				} catch (Exception e) {
					GWT.log("OnResized", null);
				}
			}
		}
	}

	/**
	 * Controller that allows for zooming when scrolling the mouse wheel.
	 * 
	 * @author Pieter De Graef
	 */
	private class ZoomOnScrollController implements MouseWheelHandler {

		public void onMouseWheel(MouseWheelEvent event) {
			if (event.isNorth()) {
				mapModel.getMapView().scale(2.0f, MapView.ZoomOption.LEVEL_CHANGE);
			} else {
				mapModel.getMapView().scale(0.5f, MapView.ZoomOption.LEVEL_CHANGE);
			}
		}
	}

	/**
	 * Renders feature on select/deselect
	 */
	private class MapWidgetFeatureSelectionHandler implements FeatureSelectionHandler {

		private MapWidget mapWidget;

		public MapWidgetFeatureSelectionHandler(MapWidget mapWidget) {
			this.mapWidget = mapWidget;
		}

		public void onFeatureSelected(FeatureSelectedEvent event) {
			mapWidget.render(event.getFeature(), "update");
		}

		public void onFeatureDeselected(FeatureDeselectedEvent event) {
			mapWidget.render(event.getFeature(), "delete");
		}
	}
}