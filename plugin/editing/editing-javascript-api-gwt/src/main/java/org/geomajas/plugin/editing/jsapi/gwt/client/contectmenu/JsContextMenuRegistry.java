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
package org.geomajas.plugin.editing.jsapi.gwt.client.contectmenu;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;
import org.geomajas.plugin.editing.gwt.client.controller.GeometryIndexContextMenuController;
import org.geomajas.plugin.editing.jsapi.gwt.client.JsGeometryEditor;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * JavaScript class for registering
 * {@link org.geomajas.plugin.editing.gwt.client.controller.GeometryIndexContextMenuController.GeometryIndexOperation}s
 * to the vertex or edge context menu.
 * 
 * @author Jan Venstermans
 * @since 1.0.0
 * 
 */
@Export("ContextMenuRegister")
@ExportPackage("org.geomajas.plugin.editing.contextmenu")
@Api(allMethods = true)
public class JsContextMenuRegistry implements Exportable {

	private GeometryEditor editor;

	/**
	 * Needed for exporter.
	 */
	public JsContextMenuRegistry() {
	}

	/**
	 * JavaScript constructor.
	 *
	 * @param jsEditor the editor service
	 */
	@Export
	public JsContextMenuRegistry(JsGeometryEditor jsEditor) {
		editor = jsEditor.getDelegate();
		editor.setContextMenuController(new GeometryIndexContextMenuController(editor.getMapWidget(),
				editor.getEditService()));
	}

	/**
	 * Add a delete operation to the vertex context menu.
	 *
	 * @param displayName the display name in the context menu.
	 */
	public void addRemoveVertexOperation(String displayName) {
		editor.getContextMenuController().addVertexOperation(
				GeometryIndexContextMenuController.GeometryIndexOperation.REMOVE, displayName);
	}

	/**
	 * Add a deselect operation to the vertex context menu.
	 *
	 * @param displayName the display name in the context menu.
	 */
	public void addDeselectVertexOperation(String displayName) {
		editor.getContextMenuController().addVertexOperation(
				GeometryIndexContextMenuController.GeometryIndexOperation.DESELECT, displayName);
	}

	/**
	 * Add a zoom in operation to the vertex context menu.
	 *
	 * @param displayName the display name in the context menu.
	 */
	public void addZoomInVertexOperation(String displayName) {
		editor.getContextMenuController().addVertexOperation(
				GeometryIndexContextMenuController.GeometryIndexOperation.ZOOM_IN, displayName);
	}

	/**
	 * Add a zoom out operation to the vertex context menu.
	 *
	 * @param displayName the display name in the context menu.
	 */
	public void addZoomOutVertexOperation(String displayName) {
		editor.getContextMenuController().addVertexOperation(
				GeometryIndexContextMenuController.GeometryIndexOperation.ZOOM_OUT, displayName);
	}

	/**
	 * Add a zoom-to-selected-geometry operation to the vertex context menu.
	 *
	 * @param displayName the display name in the context menu.
	 */
	public void addZoomToFullObjectVertexOperation(String displayName) {
		editor.getContextMenuController().addVertexOperation(
				GeometryIndexContextMenuController.GeometryIndexOperation.ZOOM_TO_FULL_OBJECT, displayName);
	}

	/**
	 * Add a delete operation to the edge context menu.
	 *
	 * @param displayName the display name in the context menu.
	 */
	public void addRemoveEdgeOperation(String displayName) {
		editor.getContextMenuController().addEdgeOperation(
				GeometryIndexContextMenuController.GeometryIndexOperation.REMOVE, displayName);
	}

	/**
	 * Add a deselect operation to the edge context menu.
	 *
	 * @param displayName the display name in the context menu.
	 */
	public void addDeselectEdgeOperation(String displayName) {
		editor.getContextMenuController().addEdgeOperation(
				GeometryIndexContextMenuController.GeometryIndexOperation.DESELECT, displayName);
	}

	/**
	 * Add a zoom in operation to the edge context menu.
	 *
	 * @param displayName the display name in the context menu.
	 */
	public void addZoomInEdgeOperation(String displayName) {
		editor.getContextMenuController().addEdgeOperation(
				GeometryIndexContextMenuController.GeometryIndexOperation.ZOOM_IN, displayName);
	}

	/**
	 * Add a zoom out operation to the edge context menu.
	 *
	 * @param displayName the display name in the context menu.
	 */
	public void addZoomOutEdgeOperation(String displayName) {
		editor.getContextMenuController().addEdgeOperation(
				GeometryIndexContextMenuController.GeometryIndexOperation.ZOOM_OUT, displayName);
	}

	/**
	 * Add a zoom-to-selected-geometry operation to the edge context menu.
	 *
	 * @param displayName the display name in the context menu.
	 */
	public void addZoomToFullObjectEdgeOperation(String displayName) {
		editor.getContextMenuController().addEdgeOperation(
				GeometryIndexContextMenuController.GeometryIndexOperation.ZOOM_TO_FULL_OBJECT, displayName);
	}

	/**
	 * Should action of single item in context menu be performed automatically?
	 *
	 * @param onOneMenuItemSimulateClick boolean.
	 */
	public void setOnOneMenuItemSimulateClick(boolean onOneMenuItemSimulateClick) {
		editor.getContextMenuController().setOnOneMenuItemSimulateClick(onOneMenuItemSimulateClick);
	}

}
