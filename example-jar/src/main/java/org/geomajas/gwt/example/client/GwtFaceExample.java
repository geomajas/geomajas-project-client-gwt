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

package org.geomajas.gwt.example.client;

import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.example.base.SampleTreeNode;
import org.geomajas.gwt.example.base.SampleTreeNodeRegistry;
import org.geomajas.gwt.example.client.sample.attribute.AttributeCustomFormSample;
import org.geomajas.gwt.example.client.sample.attribute.AttributeCustomTypeSample;
import org.geomajas.gwt.example.client.sample.attribute.AttributeIncludeInFormSample;
import org.geomajas.gwt.example.client.sample.attribute.AttributeSearchSample;
import org.geomajas.gwt.example.client.sample.attribute.EditAttributeSample;
import org.geomajas.gwt.example.client.sample.attribute.EditableGridSample;
import org.geomajas.gwt.example.client.sample.attribute.FeatureListGridSample;
import org.geomajas.gwt.example.client.sample.attribute.SearchSample;
import org.geomajas.gwt.example.client.sample.controller.CircleControllerSample;
import org.geomajas.gwt.example.client.sample.controller.ControllerOnElementSample;
import org.geomajas.gwt.example.client.sample.controller.CustomControllerSample;
import org.geomajas.gwt.example.client.sample.controller.FallbackControllerSample;
import org.geomajas.gwt.example.client.sample.controller.MouseMoveListenerSample;
import org.geomajas.gwt.example.client.sample.controller.MultipleListenersSample;
import org.geomajas.gwt.example.client.sample.controller.RectangleControllerSample;
import org.geomajas.gwt.example.client.sample.editing.EditLineLayerSample;
import org.geomajas.gwt.example.client.sample.editing.EditMultiLineLayerSample;
import org.geomajas.gwt.example.client.sample.editing.EditMultiPolygonLayerSample;
import org.geomajas.gwt.example.client.sample.editing.EditPointLayerSample;
import org.geomajas.gwt.example.client.sample.editing.EditPolygonLayerSample;
import org.geomajas.gwt.example.client.sample.mapwidget.CanvasMapAddonSample;
import org.geomajas.gwt.example.client.sample.general.PipelineConfigSample;
import org.geomajas.gwt.example.client.sample.general.ServerErrorSample;
import org.geomajas.gwt.example.client.sample.i18n.SampleMessages;
import org.geomajas.gwt.example.client.sample.layer.OpenStreetMapSample;
import org.geomajas.gwt.example.client.sample.layer.WmsSample;
import org.geomajas.gwt.example.client.sample.layertree.LayerOrderSample;
import org.geomajas.gwt.example.client.sample.layertree.LayertreeSample;
import org.geomajas.gwt.example.client.sample.layertree.LegendSample;
import org.geomajas.gwt.example.client.sample.mapwidget.CrsSample;
import org.geomajas.gwt.example.client.sample.mapwidget.DynamicUrlSample;
import org.geomajas.gwt.example.client.sample.mapwidget.GroupAndSingleAddonSample;
import org.geomajas.gwt.example.client.sample.mapwidget.LayerOpacitySample;
import org.geomajas.gwt.example.client.sample.mapwidget.MaxBoundsToggleSample;
import org.geomajas.gwt.example.client.sample.mapwidget.NavigationSample;
import org.geomajas.gwt.example.client.sample.mapwidget.OverviewMapSample;
import org.geomajas.gwt.example.client.sample.mapwidget.PanAndZoomSliderSample;
import org.geomajas.gwt.example.client.sample.mapwidget.PanScaleToggleSample;
import org.geomajas.gwt.example.client.sample.mapwidget.RenderingSample;
import org.geomajas.gwt.example.client.sample.mapwidget.UnitTypesSample;
import org.geomajas.gwt.example.client.sample.mapwidget.WorldScreenSample;
import org.geomajas.gwt.example.client.sample.toolbar.CustomToolbarSample;
import org.geomajas.gwt.example.client.sample.toolbar.CustomToolbarToolsSample;
import org.geomajas.gwt.example.client.sample.toolbar.ScaleSelectCustomSample;
import org.geomajas.gwt.example.client.sample.toolbar.ScaleSelectDefaultSample;
import org.geomajas.gwt.example.client.sample.toolbar.ToolbarFeatureInfoSample;
import org.geomajas.gwt.example.client.sample.toolbar.ToolbarMeasureAreaLocationSample;
import org.geomajas.gwt.example.client.sample.toolbar.ToolbarMeasureSample;
import org.geomajas.gwt.example.client.sample.toolbar.ToolbarNavigationSample;
import org.geomajas.gwt.example.client.sample.toolbar.ToolbarSelectionSample;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * <p>
 * The GWT test case sample application. Here here!
 * </p>
 *
 * @author Pieter De Graef
 */
public class GwtFaceExample implements EntryPoint {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String GROUP_TOP_LEVEL_ID = "topLevel";
	public static final String GROUP_LAYERS_ID = "Layers";
	public static final String GROUP_MAP_WIDGET_ID = "MapWidget";
	public static final String GROUP_GEO_GRAPHICS_EDITING_ID = "GeoGraphicEditing";
	public static final String GROUP_LAYER_TREE_ID = "Layertree";
	public static final String GROUP_MAP_CONTROLLER_ID = "MapController";
	public static final String GROUP_FEATURE_LIST_ID = "FeatureListGridGroup";
	public static final String GROUP_TOOLBAR_CONTROLLERS_ID = "ToolbarAndControllers";
	public static final String GROUP_GENERAL_ID = "General";

	public void onModuleLoad() {
		addLayersSamples();
		addMapWidgetSamples();
		addGeoGraphicsEditingSamples();
		addLayerTreeSamples();
		addFeatureListSamples();
		addMapControllerSamples();
		addToolbarAndControllerSamples();
		addGeneralSamples();
	}

	private void addLayersSamples() {
		// Layers samples:
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupLayers(),
				"[ISOMORPHIC]/geomajas/osgeo/layer.png", GROUP_LAYERS_ID, GROUP_TOP_LEVEL_ID));

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.osmTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/layer-raster.png", OpenStreetMapSample.OSM_TITLE, GROUP_LAYERS_ID,
				OpenStreetMapSample.FACTORY));

		//WMS Layers
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.clientWmsTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/mapset.png", WmsSample.TITLE, GROUP_LAYERS_ID,
				WmsSample.FACTORY));
	}

	private void addMapWidgetSamples() {
		// MapWidget samples:
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupMap(),
				"[ISOMORPHIC]/geomajas/example/image/silk/world.png", GROUP_MAP_WIDGET_ID, GROUP_TOP_LEVEL_ID));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.navigationTitle(),
				"[ISOMORPHIC]/geomajas/example/image/silk/world.png", NavigationSample.TITLE, GROUP_MAP_WIDGET_ID,
				NavigationSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.crsTitle(),
				"[ISOMORPHIC]/geomajas/example/image/silk/world.png", CrsSample.TITLE, GROUP_MAP_WIDGET_ID,
				CrsSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.unitTypesTitle(),
				"[ISOMORPHIC]/geomajas/example/image/silk/world.png", UnitTypesSample.TITLE, GROUP_MAP_WIDGET_ID,
				UnitTypesSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.maxBoundsToggleTitle(),
				"[ISOMORPHIC]/geomajas/example/image/silk/world.png", MaxBoundsToggleSample.TITLE,
				GROUP_MAP_WIDGET_ID, MaxBoundsToggleSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.panScaleToggleTitle(),
				"[ISOMORPHIC]/geomajas/example/image/silk/world.png", PanScaleToggleSample.TITLE, GROUP_MAP_WIDGET_ID,
				PanScaleToggleSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.groupAndSingleTitle(),
				"[ISOMORPHIC]/geomajas/example/image/silk/world.png",
				GroupAndSingleAddonSample.TITLE, GROUP_MAP_WIDGET_ID,
				GroupAndSingleAddonSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.panAndSliderTitle(),
				"[ISOMORPHIC]/geomajas/example/image/silk/world.png", PanAndZoomSliderSample.TITLE, GROUP_MAP_WIDGET_ID,
				PanAndZoomSliderSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.dynamicUrlTitle(),
				"[ISOMORPHIC]/geomajas/example/image/silk/world.png", DynamicUrlSample.TITLE, GROUP_MAP_WIDGET_ID,
				DynamicUrlSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.renderingTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/edit.png", RenderingSample.TITLE, GROUP_MAP_WIDGET_ID,
				RenderingSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.screenWorldTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/edit.png", WorldScreenSample.TITLE, GROUP_MAP_WIDGET_ID,
				WorldScreenSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.overviewMapTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/region.png", OverviewMapSample.TITLE, GROUP_MAP_WIDGET_ID,
				OverviewMapSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.layerOpacityTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/layer-raster.png", LayerOpacitySample.TITLE, GROUP_MAP_WIDGET_ID,
				LayerOpacitySample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.canvasMapAddonTitle(),
				"[ISOMORPHIC]/geomajas/example/image/silk/world.png", CanvasMapAddonSample.TITLE, GROUP_MAP_WIDGET_ID,
				CanvasMapAddonSample.FACTORY));
	}

	private void addGeoGraphicsEditingSamples() {
		// Editing:
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupEditing(),
				WidgetLayout.iconEdit, GROUP_GEO_GRAPHICS_EDITING_ID, GROUP_TOP_LEVEL_ID));

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.editPointLayerTitle(),
				WidgetLayout.iconEdit, EditPointLayerSample.TITLE, GROUP_GEO_GRAPHICS_EDITING_ID,
				EditPointLayerSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.editLineLayerTitle(),
				WidgetLayout.iconEdit, EditLineLayerSample.TITLE, GROUP_GEO_GRAPHICS_EDITING_ID,
				EditLineLayerSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.editPolygonLayerTitle(),
				WidgetLayout.iconEdit, EditPolygonLayerSample.TITLE, GROUP_GEO_GRAPHICS_EDITING_ID,
				EditPolygonLayerSample.FACTORY));
		// SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.editMultiPointLayerTitle(),
		// "[ISOMORPHIC]/geomajas/osgeo/edit.png", EditMultiPointLayerSample.TITLE, "GeoGraphicEditing",
		// EditMultiPointLayerSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.editMultiLineLayerTitle(),
				WidgetLayout.iconEdit, EditMultiLineLayerSample.TITLE, GROUP_GEO_GRAPHICS_EDITING_ID,
				EditMultiLineLayerSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.editMultiPolygonLayerTitle(),
				WidgetLayout.iconEdit, EditMultiPolygonLayerSample.TITLE, GROUP_GEO_GRAPHICS_EDITING_ID,
				EditMultiPolygonLayerSample.FACTORY));
	}

	private void addLayerTreeSamples() {
		// LayerTree & Legend samples:
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupLayerTree(),
				"[ISOMORPHIC]/geomajas/osgeo/mapset.png", GROUP_LAYER_TREE_ID, GROUP_TOP_LEVEL_ID));

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.layertreeTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/mapset.png", LayertreeSample.TITLE, GROUP_LAYER_TREE_ID,
				LayertreeSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.legendTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/legend-add.png", LegendSample.TITLE, GROUP_LAYER_TREE_ID,
				LegendSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.layerOrderTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/mapset.png", LayerOrderSample.TITLE, GROUP_LAYER_TREE_ID,
				LayerOrderSample.FACTORY));
	}

	private void addFeatureListSamples() {
		// Attribute samples:
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupAttributes(),
				WidgetLayout.iconTable, GROUP_FEATURE_LIST_ID, GROUP_TOP_LEVEL_ID));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.fltTitle(),
				WidgetLayout.iconTable, FeatureListGridSample.TITLE, GROUP_FEATURE_LIST_ID,
				FeatureListGridSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.searchTitle(),
				WidgetLayout.iconTable, SearchSample.TITLE, GROUP_FEATURE_LIST_ID,
				SearchSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.search2Title(),
				WidgetLayout.iconTable, AttributeSearchSample.TITLE, GROUP_FEATURE_LIST_ID,
				AttributeSearchSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.editableGridTitle(),
				WidgetLayout.iconTable, EditableGridSample.TITLE, GROUP_FEATURE_LIST_ID,
				EditableGridSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.editAttributeTitle(),
				WidgetLayout.iconTable, EditAttributeSample.TITLE, GROUP_FEATURE_LIST_ID,
				EditAttributeSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.attributeIncludeInFormTitle(),
				WidgetLayout.iconTable, AttributeIncludeInFormSample.TITLE,
				GROUP_FEATURE_LIST_ID, AttributeIncludeInFormSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.attributeCustomTypeTitle(),
				WidgetLayout.iconTable, AttributeCustomTypeSample.TITLE,
				GROUP_FEATURE_LIST_ID, AttributeCustomTypeSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.attributeCustomFormTitle(),
				WidgetLayout.iconTable, AttributeCustomFormSample.TITLE,
				GROUP_FEATURE_LIST_ID, AttributeCustomFormSample.FACTORY));
	}

	private void addMapControllerSamples() {
		// Map controller:
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupMapController(),
				WidgetLayout.iconTools, GROUP_MAP_CONTROLLER_ID, GROUP_TOP_LEVEL_ID));

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.customControllerTitle(),
				WidgetLayout.iconTools, CustomControllerSample.TITLE, GROUP_MAP_CONTROLLER_ID,
				CustomControllerSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.controllerOnElementTitle(),
				WidgetLayout.iconTools, ControllerOnElementSample.TITLE, GROUP_MAP_CONTROLLER_ID,
				ControllerOnElementSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.rectangleControllerTitle(),
				WidgetLayout.iconTools, RectangleControllerSample.TITLE, GROUP_MAP_CONTROLLER_ID,
				RectangleControllerSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.circleControllerTitle(),
				WidgetLayout.iconTools, CircleControllerSample.TITLE, GROUP_MAP_CONTROLLER_ID,
				CircleControllerSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.fallbackControllerTitle(),
				WidgetLayout.iconTools, FallbackControllerSample.TITLE, GROUP_MAP_CONTROLLER_ID,
				FallbackControllerSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.mouseMoveListenerTitle(),
				"[ISOMORPHIC]/geomajas/silk/monitor.png", MouseMoveListenerSample.TITLE, GROUP_MAP_CONTROLLER_ID,
				MouseMoveListenerSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.multipleListenersTitle(),
				"[ISOMORPHIC]/geomajas/silk/monitor.png", MultipleListenersSample.TITLE, GROUP_MAP_CONTROLLER_ID,
				MultipleListenersSample.FACTORY));
	}

	private void addToolbarAndControllerSamples() {
		// Toolbar and controllers:
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupToolbarAndControllers(),
				WidgetLayout.iconZoomIn, GROUP_TOOLBAR_CONTROLLERS_ID, GROUP_TOP_LEVEL_ID));

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.toolbarNavigationTitle(),
				WidgetLayout.iconPan, ToolbarNavigationSample.TITLE, GROUP_TOOLBAR_CONTROLLERS_ID,
				ToolbarNavigationSample.FACTORY));

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.toolbarSelectionTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/select.png", ToolbarSelectionSample.TITLE,
				GROUP_TOOLBAR_CONTROLLERS_ID, ToolbarSelectionSample.FACTORY));

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.toolbarMeasureTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/length-measure.png", ToolbarMeasureSample.TITLE,
				GROUP_TOOLBAR_CONTROLLERS_ID, ToolbarMeasureSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.toolbarMeasureAreaLocationTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/length-measure.png", ToolbarMeasureAreaLocationSample.TITLE,
				GROUP_TOOLBAR_CONTROLLERS_ID, ToolbarMeasureAreaLocationSample.FACTORY));

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.toolbarFeatureInfoTitle(),
				WidgetLayout.iconInfo, ToolbarFeatureInfoSample.TITLE,
				GROUP_TOOLBAR_CONTROLLERS_ID, ToolbarFeatureInfoSample.FACTORY));

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.scaleSelectDefaultTitle(),
				WidgetLayout.iconTools, ScaleSelectDefaultSample.TITLE,
				GROUP_TOOLBAR_CONTROLLERS_ID, ScaleSelectDefaultSample.FACTORY));

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.scaleSelectCustomTitle(),
				WidgetLayout.iconTools, ScaleSelectCustomSample.TITLE,
				GROUP_TOOLBAR_CONTROLLERS_ID, ScaleSelectCustomSample.FACTORY));

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.customToolbarToolsTitle(),
				WidgetLayout.iconTools, CustomToolbarToolsSample.TITLE,
				GROUP_TOOLBAR_CONTROLLERS_ID, CustomToolbarToolsSample.FACTORY));

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.customToolbarTitle(),
				WidgetLayout.iconTools, CustomToolbarSample.TITLE, GROUP_TOOLBAR_CONTROLLERS_ID,
				CustomToolbarSample.FACTORY));
	}

	private void addGeneralSamples() {
		// General samples:
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupGeneral(),
				"[ISOMORPHIC]/geomajas/osgeo/settings.png", GROUP_GENERAL_ID, GROUP_TOP_LEVEL_ID));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.serverErrorTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/help-contents.png", ServerErrorSample.TITLE, GROUP_GENERAL_ID,
				ServerErrorSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.pipelineConfigTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/help-contents.png", PipelineConfigSample.TITLE, GROUP_GENERAL_ID,
				PipelineConfigSample.FACTORY));
	}

}
