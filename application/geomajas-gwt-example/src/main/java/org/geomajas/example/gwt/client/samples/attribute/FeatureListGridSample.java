/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.example.gwt.client.samples.attribute;

import java.util.List;

import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.cache.tile.TileFunction;
import org.geomajas.gwt.client.map.cache.tile.VectorTile;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.LazyLoadCallback;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.FeatureListGrid;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows a FeatureListGrid widget and some features in it.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class FeatureListGridSample extends SamplePanel {

	public static final String TITLE = "FeatureListGridSample";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new FeatureListGridSample();
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
		final MapWidget map = new MapWidget("mapFeatureListGrid", "gwt-samples");
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		// Create a layout with a FeatureListGrid in it:
		final FeatureListGrid grid = new FeatureListGrid(map.getMapModel());
		grid.setShowEdges(true);
		grid.setShowResizeBar(true);

		// Add a trigger to fill the grid when the map has finished loading:
		map.getMapModel().addMapModelHandler(new MapModelHandler() {

			public void onMapModelChange(MapModelEvent event) {
				MapModel mapModel = map.getMapModel();
				VectorLayer layer = (VectorLayer) mapModel.getLayer("clientLayerCountries110mGrid");
				grid.setLayer(layer);
				Bbox bounds = mapModel.getMapView().getBounds();
				layer.getFeatureStore().queryAndSync(bounds, null, null, new TileFunction<VectorTile>() {

					public void execute(VectorTile tile) {
						tile.getFeatures(GeomajasConstant.FEATURE_INCLUDE_ALL, new LazyLoadCallback() {

							public void execute(List<Feature> response) {
								for (Feature feature : response) {
									grid.addFeature(feature);
								}
							}
						});
					}
				});
			}
		});

		layout.addMember(grid);
		layout.addMember(mapLayout);

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().fltDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/attribute/FeatureListGridSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/mapFeatureListGrid.xml", "WEB-INF/layerCountries110m.xml",
				"WEB-INF/layerWmsBluemarble.xml", };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
