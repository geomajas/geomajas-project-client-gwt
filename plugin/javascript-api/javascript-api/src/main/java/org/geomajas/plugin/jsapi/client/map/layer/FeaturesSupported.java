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

package org.geomajas.plugin.jsapi.client.map.layer;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.jsapi.client.map.feature.Feature;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;

/**
 * Extension for the layer interface which signifies that this particular layer has support for features. Features are
 * the individual objects that make up a layer. Most methods in this interface will revolve around filtering (usually
 * using the feature attributes) and feature selection.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@Export
public interface FeaturesSupported extends Layer, Exportable {

	/**
	 * Apply a filter on the layer. Such a filter specifies which features are to be shown on the map, and which aren't.
	 * This filter is actually used on the back-end and therefore follows the CQL standards.
	 * 
	 * @param filter
	 *            The CQL filter, based upon the layer attribute definitions. Use null to disable filtering.
	 */
	void setFilter(String filter);

	/**
	 * Returns the filter that has been set onto this layer.
	 * 
	 * @return Returns the CQL filter.
	 */
	String getFilter();

	/**
	 * Is a certain feature selected or not?
	 * 
	 * @param featureId
	 *            The unique identifier of the feature within this layer.
	 * @return Returns true if the feature has been selected, false otherwise.
	 */
	boolean isFeatureSelected(String featureId);

	/**
	 * Select the given feature.
	 * 
	 * @param feature
	 *            The feature to select. Must be part of this layer.
	 * @return Return true if the selection was successful.
	 */
	boolean selectFeature(Feature feature);

	/**
	 * Deselect the given feature.
	 * 
	 * @param feature
	 *            The feature to deselect. Must be part of this layer.
	 * @return Return true if the deselection was successful.
	 */
	boolean deselectFeature(Feature feature);

	/** Deselect all features within this layer. */
	void clearSelectedFeatures();

	/**
	 * Get the full list of currently selected features within this layer.
	 * 
	 * @return The list of selected features as an array.
	 */
	Feature[] getSelectedFeatures();
}