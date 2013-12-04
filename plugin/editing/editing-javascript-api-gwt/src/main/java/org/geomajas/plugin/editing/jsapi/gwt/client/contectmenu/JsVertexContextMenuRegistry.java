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
import org.geomajas.plugin.editing.gwt.client.controller.VertexContextMenuController;
import org.geomajas.plugin.editing.jsapi.gwt.client.JsGeometryEditor;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * JavaScript class for registering {@link VertexContextMenuController.Operation}s to the vertex contect menu.
 * Every operation has its own registration method.
 * 
 * @author Jan Venstermans
 * @since 1.0.0
 * 
 */
@Export("VertexContextMenuRegister")
@ExportPackage("org.geomajas.plugin.editing.contextmenu")
@Api(allMethods = true)
public class JsVertexContextMenuRegistry implements Exportable {

	private GeometryEditor editor;

	/**
	 * Needed for exporter.
	 */
	public JsVertexContextMenuRegistry() {
	}

	/**
	 * JavaScript constructor.
	 *
	 * @param jsEditor the editor service
	 */
	@Export
	public JsVertexContextMenuRegistry(JsGeometryEditor jsEditor) {
		editor = jsEditor.getDelegate();
	}

	/**
	 * Add a delete operation to the vertex context menu.
	 *
	 * @param displayName the display name in the context menu.
	 */
	public void addRemoveSelectedVertexOperation(String displayName) {
		editor.addVertexOperation(VertexContextMenuController.Operation.REMOVE_SELECTED, displayName);
	}

	/**
	 * Add a deselect operation to the vertex context menu.
	 *
	 * @param displayName the display name in the context menu.
	 */
	public void addDeselectAllVertexOperation(String displayName) {
		editor.addVertexOperation(VertexContextMenuController.Operation.DESELECT_ALL, displayName);
	}

	/**
	 * Add a zoom in operation to the vertex context menu.
	 *
	 * @param displayName the display name in the context menu.
	 */
	public void addZoomInVertexOperation(String displayName) {
		editor.addVertexOperation(VertexContextMenuController.Operation.ZOOM_IN, displayName);
	}

	/**
	 * Add a zoom out operation to the vertex context menu.
	 *
	 * @param displayName the display name in the context menu.
	 */
	public void addZoomOutVertexOperation(String displayName) {
		editor.addVertexOperation(VertexContextMenuController.Operation.ZOOM_OUT, displayName);
	}

	/**
	 * Add a zoom-to-selected-geometry operation to the vertex context menu.
	 *
	 * @param displayName the display name in the context menu.
	 */
	public void addZoomToFullObjectVertexOperation(String displayName) {
		editor.addVertexOperation(VertexContextMenuController.Operation.ZOOM_TO_FULL_OBJECT, displayName);
	}

}
