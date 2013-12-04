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
package org.geomajas.plugin.editing.jsapi.gwt.client.handler;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import org.geomajas.annotation.Api;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;
import org.geomajas.plugin.editing.gwt.client.event.IndexMouseOverOutEvent;
import org.geomajas.plugin.editing.jsapi.gwt.client.JsGeometryEditor;
import org.geomajas.plugin.editing.jsapi.gwt.client.listener.IndexMouseOutListener;
import org.geomajas.plugin.editing.jsapi.gwt.client.listener.IndexMouseOverListener;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Javascript class that is a dispatcher for mouse over and out events on.
 * 
 * @author Jan Venstermans
 * @since 1.0.0
 * 
 */
@Export("InfoMouseOverOutHandler")
@ExportPackage("org.geomajas.plugin.editing.handler")
@Api(allMethods = true)
public class JsIndexMouseOverOutHandler implements Exportable, IndexMouseOverOutEvent.Handler {

	private GeometryEditor editor;

	private IndexMouseOverListener vertexOverListener, edgeOverListener;

	private IndexMouseOutListener vertexOutListener, edgeOutListener;

	private EventBus eventBus;

	/**
	 * Needed for exporter.
	 */
	public JsIndexMouseOverOutHandler() {
	}

	/**
	 * JavaScript constructor.
	 *
	 * @param jsEditor the editor service
	 */
	@Export
	public JsIndexMouseOverOutHandler(JsGeometryEditor jsEditor) {
		editor = jsEditor.getDelegate();
		editor.getGeometryEditorSpecificEventbus().addHandler(IndexMouseOverOutEvent.getType(), this);
		eventBus = new SimpleEventBus();
		eventBus.addHandler(IndexMouseOverOutEvent.getType(), this);
	}

	/**
	 * Register the listener of vertex mouse over event.
	 *
	 * @param listener the listener to register
	 */
	public void addVertexMouseOverListener(IndexMouseOverListener listener) {
		vertexOverListener = listener;
	}

	/**
	 * Register the listener of vertex mouse out event.
	 *
	 * @param listener the listener to register
	 */
	public void addVertexMouseOutListener(IndexMouseOutListener listener) {
		vertexOutListener = listener;
	}

	/**
	 * Register the listener of edge mouse over event.
	 *
	 * @param listener the listener to register
	 */
	public void addEdgeMouseOverListener(IndexMouseOverListener listener) {
		edgeOverListener = listener;
	}

	/**
	 * Register the listener of edge mouse out event.
	 *
	 * @param listener the listener to register
	 */
	public void addEdgeMouseOutListener(IndexMouseOutListener listener) {
		edgeOutListener = listener;
	}

	@Override
	public void onMouseOverVertex(IndexMouseOverOutEvent event) {
		GeometryIndex index = event.getIndex();
		while (index.getType().equals(GeometryIndexType.TYPE_GEOMETRY)) {
			index = index.getChild();
		}
		switch (index.getType())  {
			case TYPE_VERTEX:
				if (vertexOverListener != null) {
					vertexOverListener.onMouseOver(index);
				}
				break;
			case TYPE_EDGE:
				if (edgeOverListener != null) {
					edgeOverListener.onMouseOver(index);
				}
				break;
		}
	}

	@Override
	public void onMouseOutVertex(IndexMouseOverOutEvent event) {
		GeometryIndex index = event.getIndex();
		while (index.getType().equals(GeometryIndexType.TYPE_GEOMETRY)) {
			index = index.getChild();
		}
		switch (index.getType())  {
			case TYPE_VERTEX:
				if (vertexOutListener != null) {
					vertexOutListener.onMouseOut(index);
				}
				break;
			case TYPE_EDGE:
				if (edgeOutListener != null) {
					edgeOutListener.onMouseOut(index);
				}
				break;
		}
	}
}
