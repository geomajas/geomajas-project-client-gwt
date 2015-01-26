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

package org.geomajas.gwt.example.client.sample.attribute;

import com.google.gwt.core.client.GWT;
import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.command.dto.SearchFeatureResponse;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.FeatureListGrid;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.example.client.sample.i18n.SampleMessages;

/**
* <p>
* Sample that shows a FeatureListGrid widget and some features in it.
* </p>
* 
* @author Pieter De Graef
*/
public class EditableGridSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "EditableGridSample";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new EditableGridSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(10);

		// switch off lazy loading, we want to get everything
		GwtCommandDispatcher.getInstance().setUseLazyLoading(false);

		VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);

		// Map with ID featureListGridMap is defined in the XML configuration.
		final MapWidget map = new MapWidget("mapEditableGrid", "gwtExample");
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		// Create a layout with a FeatureListGrid in it:
		final FeatureListGrid grid = new FeatureListGrid(map.getMapModel());
		grid.setShowEdges(true);
		grid.setShowResizeBar(true);
		grid.setEditingEnabled(true);

		// Add a trigger to fill the grid when the map has finished loading:
		map.getMapModel().runWhenInitialized(new Runnable() {
			@Override
			public void run() {
				final VectorLayer layer = map.getMapModel().getVectorLayer("clientLayerBeansEditableGrid");
				grid.setLayer(layer);
				SearchFeatureRequest searchFeatureRequest = new SearchFeatureRequest();
				searchFeatureRequest.setCrs(map.getMapModel().getCrs());
				searchFeatureRequest.setFeatureIncludes(GeomajasConstant.FEATURE_INCLUDE_ATTRIBUTES);
				searchFeatureRequest.setLayerId("layerBeansEditableGrid");
				GwtCommand searchCommand = new GwtCommand(SearchFeatureRequest.COMMAND);
				searchCommand.setCommandRequest(searchFeatureRequest);

				GwtCommandDispatcher.getInstance().execute(searchCommand,
						new AbstractCommandCallback<SearchFeatureResponse>() {

							public void execute(SearchFeatureResponse response) {
								for (org.geomajas.layer.feature.Feature feature : response.getFeatures()) {
									Feature f = new Feature(feature, layer);
									grid.addFeature(f);
									layer.getFeatureStore().addFeature(f);
								}
							}
						}
				);
			}
		});



		layout.addMember(grid);
		layout.addMember(mapLayout);

		return layout;
	}

	public String getDescription() {
		return MESSAGES.editableGridDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/example/context/mapEditableGrid.xml",
				"classpath:org/geomajas/gwt/example/context/layerBeansEditableGrid.xml"};
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
