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
package org.geomajas.plugin.editing.jsapi.gwt.client.gfx;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.editing.client.snap.SnapService;
import org.geomajas.plugin.editing.client.snap.SnapSourceProvider;
import org.geomajas.plugin.editing.client.snap.SnappingRule;
import org.geomajas.plugin.editing.client.snap.algorithm.NearestEdgeSnapAlgorithm;
import org.geomajas.plugin.editing.client.snap.algorithm.NearestVertexSnapAlgorithm;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;
import org.geomajas.plugin.editing.gwt.client.snap.VectorLayerSourceProvider;
import org.geomajas.plugin.editing.jsapi.gwt.client.JsGeometryEditor;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * JavaScript wrapper of {@link org.geomajas.plugin.editing.client.snap.SnapService}.
 *
 * @author Jan Venstermans
 * @since 1.0.0
 *
 */
@Export("SnapService")
@ExportPackage("org.geomajas.plugin.editing.snapping")
@Api(allMethods = true)
public class JsSnapService implements Exportable {

	private SnapService delegate;

	private GeometryEditor editor;

	/**
	 * Default constructor (needed for exporter?).
	 */
	public JsSnapService() {
	}

	/**
	 * Constructor, delegate is retreived from {@link JsGeometryEditor}.
	 */
	public JsSnapService(JsGeometryEditor editor) {
		this.editor = editor.getDelegate();
		delegate = this.editor.getSnappingService();
	}


	/**
	 * Add a new snapping rules to the list. Each new rule provides information on how snapping should occur.
	 * The added rule will use algorithm {@link NearestVertexSnapAlgorithm}.
	 *
	 * @param snapLayer
	 *            The layer id that will provide the target geometries where to snap.
	 * @param distance
	 *            The maximum distance to bridge during snapping. unit=meters.
	 */
	public void addNearestVertexSnappingRule(String snapLayer, double distance) {
		SnapSourceProvider snapSourceProvider = new VectorLayerSourceProvider(editor.getMapWidget().getMapModel()
				.getVectorLayer(snapLayer));
		delegate.addSnappingRule(new SnappingRule(new NearestVertexSnapAlgorithm(), snapSourceProvider, distance));
	}

	/**
	 * Add a new snapping rules to the list. Each new rule provides information on how snapping should occur.
	 * The added rule will use algorithm {@link NearestVertexSnapAlgorithm}.
	 *
	 * @param snapLayer
	 *            The layer id that will provide the target geometries where to snap.
	 * @param distance
	 *            The maximum distance to bridge during snapping. unit=meters.
	 */
	public void addNearestEdgeSnappingRule(String snapLayer, double distance) {
		SnapSourceProvider snapSourceProvider = new VectorLayerSourceProvider(editor.getMapWidget().getMapModel()
				.getVectorLayer(snapLayer));
		delegate.addSnappingRule(new SnappingRule(new NearestEdgeSnapAlgorithm(), snapSourceProvider, distance));
	}

	/** Remove all snapping rules from this service. Without any snapping rules, snapping can not occur. */
	public void clearSnappingRules() {
		delegate.clearSnappingRules();
	}

}
