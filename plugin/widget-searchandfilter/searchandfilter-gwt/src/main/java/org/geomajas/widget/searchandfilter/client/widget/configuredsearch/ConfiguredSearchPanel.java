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
package org.geomajas.widget.searchandfilter.client.widget.configuredsearch;

import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.widget.search.AbstractAttributeCriterionPane;
import org.geomajas.widget.searchandfilter.client.widget.search.AbstractAttributeSearchPanel;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchAttribute;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearch;
import org.geomajas.widget.searchandfilter.search.dto.AndCriterion;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;

import java.util.ArrayList;
import java.util.List;

/**
 * Geometric Search Widget. Contains framework + search functionality. But does
 * not create the geometries itself.
 * <p>
 * You add specific search methods through addSearchMethod().
 * 
 * @author Jan Venstermans
 */
public class ConfiguredSearchPanel extends AbstractAttributeSearchPanel {

	private ConfiguredSearch searchConfig;

	private ConfiguredFeatureSearch configuredFeatureSearch;

	public ConfiguredSearchPanel(MapWidget mapWidget) {
		super(mapWidget, false, null);
	}

	public void setSearchConfig(final ConfiguredSearch searchConfig, final String layerId) {
		this.searchConfig = searchConfig;
		configuredFeatureSearch.setSearchConfig(searchConfig.getAttributes(), layerId);
	}

	@Override
	public AbstractFeatureSearch createFeatureSearch(MapWidget mapWidget,
													 boolean manualLayerSelection, String layerId) {
		configuredFeatureSearch = new ConfiguredFeatureSearch(mapWidget.getMapModel());
		return configuredFeatureSearch;
	}

	/**
	 * Adapted from {@link org.geomajas.widget.searchandfilter.client.widget.configuredsearch
	 * .ConfiguredSearchPanel.SearchConfigurationCanvas}
	 * by Pieter De Graef
	 * and from {@link org.geomajas.widget.searchandfilter.client.widget.search
	 * .AbstractAttributeSearchPanel.AbstractFeatureSearch}
	 * by Kristof Heirwegh.
	 *
	 * @author Jan Venstermans
	 */
	private class ConfiguredFeatureSearch extends AbstractFeatureSearch {

		private List<Criterion> searchCriteria = new ArrayList<Criterion>();

		public ConfiguredFeatureSearch(MapModel mapModel) {
			super(mapModel, null, false, false, false);
		}

		/**
		 * @param searchAttributes
		 * @param layerId  fixed layerId
		 */
		public void setSearchConfig(final List<ConfiguredSearchAttribute> searchAttributes, final String layerId) {
			setLayer(layerId);
			// set the search criteria
			searchCriteria.clear();
			for (ConfiguredSearchAttribute searchAttribute : searchAttributes) {
				searchCriteria.add(searchAttribute);
			}
			AndCriterion criterion = new AndCriterion();
			criterion.setCriteria(searchCriteria);
			setSearchCriteria(criterion);
		}

		@Override
		public AbstractAttributeCriterionPane createAttributeCriterionPane(VectorLayer layer) {
			return new ConfiguredSearchAttributeCriterionPane(layer);
		}
	}
}
