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
package org.geomajas.plugin.editing.jsapi.gwt.client.gfx;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.editing.client.gfx.GeometryRenderer;
import org.geomajas.plugin.editing.gwt.client.gfx.GeometryRendererImpl;
import org.geomajas.plugin.editing.jsapi.client.handler.JsEdgeMapHandlerFactory;
import org.geomajas.plugin.editing.jsapi.client.handler.JsGeometryIndexMapHandlerFactory;
import org.geomajas.plugin.editing.jsapi.client.handler.JsVertexMapHandlerFactory;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * JavaScript wrapper of {@link GeometryRenderer}.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 * 
 */
@Export("GeometryRenderer")
@ExportPackage("org.geomajas.plugin.editing.gfx")
@Api(allMethods = true)
public class JsGeometryRenderer implements Exportable {

	private final GeometryRendererImpl delegate;

	/**
	 * Constructor with a {@link GeometryRenderer} delegate.
	 * 
	 * @param delegate delegate
	 */
	public JsGeometryRenderer(GeometryRenderer delegate) {
		this.delegate = (GeometryRendererImpl) delegate;
	}

	/**
	 * Redraw the geometry to apply changes in the editor internal state.
	 */
	public void redraw() {
		delegate.redraw();
	}

	/**
	 * Make the edited geometry.visible/invisible.
	 * 
	 * @param visible true to make the geometry visible, false to make the geometry invisible
	 */
	public void setVisible(boolean visible) {
		delegate.setVisible(visible);
	}

	/**
	 * Add a custom factory that will create handler for every vertex. This factory will be added on top of the existing
	 * default factories.
	 * 
	 * @param factory the factory to be added to every vertex
	 */
	public void addVertexHandlerFactory(JsVertexMapHandlerFactory factory) {
		delegate.addVertexHandlerFactory(factory);
	}

	/**
	 * Add a custom factory that will create handler for every edge. This factory will be added on top of the existing
	 * default factories.
	 * 
	 * @param factory the factory to be added to every edge
	 */
	public void addEdgeHandlerFactory(JsEdgeMapHandlerFactory factory) {
		delegate.addEdgeHandlerFactory(factory);
	}
	
	/**
	 * Add a custom factory that will create handler for every geometry.
	 * 
	 * @param factory the factory to be added to every geometry
	 */
	public void addGeometryHandlerFactory(JsGeometryIndexMapHandlerFactory factory) {
		delegate.addGeometryHandlerFactory(factory);
	}

	/**
	 * Remove a custom factory that will create handler for every geometry.
	 * 
	 * @param factory the factory to be removed to every geometry
	 */
	public void removeGeometryHandlerFactory(JsGeometryIndexMapHandlerFactory factory) {
		delegate.removeGeometryHandlerFactory(factory);
	}


}
