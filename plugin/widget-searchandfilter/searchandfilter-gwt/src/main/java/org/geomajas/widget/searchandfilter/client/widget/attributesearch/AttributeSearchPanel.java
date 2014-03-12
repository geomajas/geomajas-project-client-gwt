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
package org.geomajas.widget.searchandfilter.client.widget.attributesearch;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.widget.search.AbstractAttributeCriterionPane;
import org.geomajas.widget.searchandfilter.client.widget.search.AbstractAttributeSearchPanel;
import org.geomajas.widget.searchandfilter.search.dto.AndCriterion;
import org.geomajas.widget.searchandfilter.search.dto.AttributeCriterion;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;
import org.geomajas.widget.searchandfilter.search.dto.OrCriterion;

import java.util.List;

/**
 * Search panel using (non geometry) attributes.
 *
 * @see org.geomajas.widget.searchandfilter.client.widget.search.SearchWidgetRegistry
 * @author Kristof Heirwegh
 * @since 1.0.0
 */
@Api
public class AttributeSearchPanel extends AbstractAttributeSearchPanel {

	/**
	 * Create an attribute search panel for manual layer selection.
	 *
	 * @param mapWidget map widget
	 */
	public AttributeSearchPanel(final MapWidget mapWidget) {
		this(mapWidget, true, null);
	}

	/**
	 * Create attribute search panel with pre-selected layer.
	 *
	 * @param mapWidget map widget
	 * @param layerId layer id
	 */
	public AttributeSearchPanel(final MapWidget mapWidget, String layerId) {
		this(mapWidget, true, layerId);
	}

	/**
	 * Create an attribute search panel with manual layer selection and preselected layer. If the layer selection is
	 * automatic then this will use the selected layer of no preselected layer is given.
	 *
	 * @param mapWidget map widget
	 * @param manualLayerSelection allow user to select layer
	 * @param layerId preselected layer
	 */
	public AttributeSearchPanel(final MapWidget mapWidget, boolean manualLayerSelection, String layerId) {
		super(mapWidget, manualLayerSelection, layerId);
	}

	@Override
	public AbstractFeatureSearch createFeatureSearch(MapWidget mapWidget,
													 boolean manualLayerSelection, String layerId) {
		return new FeatureSearch(mapWidget.getMapModel(), layerId, manualLayerSelection);
	}

	@Override
	public boolean validate() {
		Criterion cr = featureSearch.getSearchCriteria();
		return (cr != null && cr.isValid());
	}

	@Override
	public Criterion getFeatureSearchCriterion() {
		return featureSearch.getSearchCriteria();
	}

	@Override
	public VectorLayer getFeatureSearchVectorLayer() {
		return featureSearch.getLayer();
	}

	@Override
	public void reset() {
		featureSearch.empty();
	}

	@Override
	public void initialize(Criterion criterion) {
		featureSearch.setSearchCriteria(criterion);
	}

	public static boolean canHandle(Criterion criterion) {
		if (criterion == null) {
			return false;
		}
		List<Criterion> critters;
		if (criterion instanceof OrCriterion) {
			critters = ((OrCriterion) criterion).getCriteria();
		} else if (criterion instanceof AndCriterion) {
			critters = ((AndCriterion) criterion).getCriteria();
		} else {
			return false;
		}

		for (Criterion critter : critters) {
			if (!(critter instanceof AttributeCriterion)) {
				return false;
			}
		}
		return true;
	}

	// ----------------------------------------------------------

	/**
	 * Adapted from {@link org.geomajas.gwt.client.widget.FeatureSearch} by Pieter De Graef.
	 *
	 * @author Kristof Heirwegh
	 */
	public class FeatureSearch extends AbstractFeatureSearch {

		/**
		 * Create a search widget for searching in a specific map model. This
		 * widget will automatically react to the selection of layers within
		 * that map model, and redraw to compensate for the selected layer. In
		 * other words, searching always happens on the selected layer.
		 *
		 * @param mapModel               The MapModel containing the possible layer to search in.
		 * @param layerId                fixed layerId, in combination with manualLayerSelection this allows
		 *                                     the layer to be fixed
		 * @param manualLayerSelection   If true, a select box will be shown so the user can select
		 *                               what layer to search in. The possible list of layers
		 *                               consists of all the vector layers that are present in the
		 *                               given MapModel. If false, this widget will react to the
		 *                               layer select events that come from the MapModel. In that
		 *                               case searching happens in the selected layer (if it's a
		 *                               vector layer).<br/>
		 *                               This value cannot be altered anymore.
		 */
		public FeatureSearch(MapModel mapModel, String layerId, boolean manualLayerSelection) {
			super(mapModel, layerId, manualLayerSelection, true, true);
		}

		@Override
		public AbstractAttributeCriterionPane createAttributeCriterionPane(VectorLayer layer) {
			return new AttributeCriterionPane(layer);
		}
	}
}
