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

package org.geomajas.widget.layer.gwt.example.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import org.geomajas.gwt.client.widget.OverviewMap;
import org.geomajas.gwt.example.base.SampleTreeNode;
import org.geomajas.gwt.example.base.SampleTreeNodeRegistry;
import org.geomajas.widget.layer.client.widget.CombinedLayertree;
import org.geomajas.widget.layer.gwt.example.client.i18n.WidgetLayerExampleMessages;

/**
 * Entry point and main class for GWT application. This class defines the layout and functionality of this application.
 * 
 * @author geomajas-gwt-archetype
 */
public class WidgetLayerExample implements EntryPoint {

	private OverviewMap overviewMap;

	private CombinedLayertree layerTree;

	private WidgetLayerExampleMessages messages = GWT.create(WidgetLayerExampleMessages.class);

	public WidgetLayerExample() {
	}

	public void onModuleLoad() {
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(messages.layerListShowcaseTreeTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/mapset.png", LayerListSample.TITLE, "Layertree",
				LayerListSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(messages.layerListClientWmsShowcaseTreeTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/mapset.png", LayerListClientWmsSample.TITLE, "Layertree",
				LayerListClientWmsSample.FACTORY));
	}

	private void initialize() {
		overviewMap.setHeight(200);
	}
}
