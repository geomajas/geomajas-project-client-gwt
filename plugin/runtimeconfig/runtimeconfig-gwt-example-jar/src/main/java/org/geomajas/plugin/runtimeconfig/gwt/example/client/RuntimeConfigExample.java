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

package org.geomajas.plugin.runtimeconfig.gwt.example.client;

import org.geomajas.gwt.client.action.ToolCreator;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.toolbar.ToolbarRegistry;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.example.base.SampleTreeNode;
import org.geomajas.gwt.example.base.SampleTreeNodeRegistry;
import org.geomajas.plugin.runtimeconfig.gwt.example.client.action.AddLayerShapeAction;
import org.geomajas.plugin.runtimeconfig.gwt.example.client.action.AddLayerWmsAction;
import org.geomajas.plugin.runtimeconfig.gwt.example.client.i18n.RuntimeConfigMessages;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Entry point and main class for GWT application. This class defines the layout and functionality of this application.
 * 
 * @author Jan De Moerloose
 */
public class RuntimeConfigExample implements EntryPoint {

	public static final RuntimeConfigMessages MESSAGES = GWT.create(RuntimeConfigMessages.class);

	public void onModuleLoad() {
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupPlugins(),
				"[ISOMORPHIC]/geomajas/silk/plugin.png", "Plugins", "topLevel"));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.adminTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/runtimeconfig.png", RuntimeConfigPanel.TITLE, "Plugins",
				RuntimeConfigPanel.FACTORY));
		ToolbarRegistry.put(AddLayerShapeAction.TOOL, new ToolCreator() {
			
			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new AddLayerShapeAction(mapWidget);
			}
		});
		ToolbarRegistry.put(AddLayerWmsAction.TOOL, new ToolCreator() {
			
			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new AddLayerWmsAction(mapWidget);
			}
		});
	}
}
