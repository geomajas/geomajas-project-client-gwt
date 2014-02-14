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

package org.geomajas.plugin.editing.gwt.example.client;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.map.event.MapModelChangedEvent;
import org.geomajas.gwt.client.map.event.MapModelChangedHandler;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.plugin.editing.client.snap.SnapSourceProvider;
import org.geomajas.plugin.editing.client.snap.algorithm.NearestEdgeSnapAlgorithm;
import org.geomajas.plugin.editing.client.snap.algorithm.NearestVertexSnapAlgorithm;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;
import org.geomajas.plugin.editing.gwt.client.GeometryEditorImpl;
import org.geomajas.plugin.editing.gwt.client.controller.GeometryIndexContextMenuController;
import org.geomajas.plugin.editing.gwt.client.gfx.PointSymbolizerShapeAndSize;
import org.geomajas.plugin.editing.gwt.client.snap.VectorLayerSourceProvider;
import org.geomajas.plugin.editing.gwt.example.client.i18n.EditingMessages;
import org.geomajas.plugin.editing.gwt.example.client.widget.MenuBar;

/**
 * Entry point and main class for GWT application. This class defines the layout and functionality of this application.
 *
 * @author Pieter De Graef
 * @author Jan Venstermans
 */
public class EditingPanel extends SamplePanel implements MapModelChangedHandler {

	public static final String TITLE = "gepEditing";

	public static final EditingMessages MESSAGES = GWT.create(EditingMessages.class);

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new EditingPanel();
		}
	};

	private GeometryEditor editor;

	private MapWidget map;

	@Override
	public Canvas getViewPanel() {
		map = new MapWidget("mapGepEditing", "appEditing");
		editor = new GeometryEditorImpl(map);


		// set shape and size of point symbolizer
		editor.getStyleService().getPointSymbolizerShapeAndSize().setShape(PointSymbolizerShapeAndSize.Shape.CIRCLE);
		editor.getStyleService().getPointSymbolizerShapeAndSize().setSize(6);

		// register operations for vertex contect menu
		editor.setContextMenuController(new GeometryIndexContextMenuController(map, editor.getEditService()));
		editor.getContextMenuController().addVertexOperation(
				GeometryIndexContextMenuController.GeometryIndexOperation.REMOVE, "verwijder punt");
		editor.getContextMenuController().addVertexOperation(
				GeometryIndexContextMenuController.GeometryIndexOperation.DESELECT, "deselecteer punt");
		editor.getContextMenuController().addVertexOperation(
				GeometryIndexContextMenuController.GeometryIndexOperation.ZOOM_IN, "zoom in");
		editor.getContextMenuController().addVertexOperation(
				GeometryIndexContextMenuController.GeometryIndexOperation.ZOOM_OUT, "zoom uit");
		editor.getContextMenuController().addVertexOperation(
				GeometryIndexContextMenuController.GeometryIndexOperation.ZOOM_TO_FULL_OBJECT, "zoom naar object");		
		editor.getEditService().setValidating(true);

		VLayout layout = new VLayout();
		MenuBar editingToolStrip = new MenuBar(editor);
		layout.addMember(editingToolStrip);
		layout.addMember(map);
		layout.setHeight("100%");

		map.getMapModel().addMapModelChangedHandler(this);

		return layout;
	}

	@Override
	public String getDescription() {
		return MESSAGES.editingDescription();
	}

	@Override
	public String[] getConfigurationFiles() {
		return new String[] {"classpath:org/geomajas/plugin/editing/gwt/example/context/appEditing.xml",
				"classpath:org/geomajas/plugin/editing/gwt/example/context/clientLayerCountries.xml",
				"classpath:org/geomajas/plugin/editing/gwt/example/context/clientLayerOsm.xml",
				"classpath:org/geomajas/plugin/editing/gwt/example/context/mapEditing.xml",
				"classpath:org/geomajas/gwt/example/base/layerOsm.xml",
				"classpath:org/geomajas/gwt/example/base/layerCountries.xml"};
	}

	@Override
	public String ensureUserLoggedIn() {
		return "luc";
	}

	@Override
	public void onMapModelChanged(MapModelChangedEvent event) {
		editor.getSnappingService().clearSnappingRules();
		SnapSourceProvider snapSourceProvider = new VectorLayerSourceProvider(editor.getMapWidget().getMapModel()
				.getVectorLayer("clientLayerGepCountries"));
//		editor.setSnapOnInsert(true);
//		editor.setSnapOnDrag(true);
		editor.getSnappingService().addSnappingRule(new NearestVertexSnapAlgorithm(), snapSourceProvider, 200000, true);
		editor.getSnappingService().addSnappingRule(new NearestEdgeSnapAlgorithm(), snapSourceProvider, 100000, false);
	}
}