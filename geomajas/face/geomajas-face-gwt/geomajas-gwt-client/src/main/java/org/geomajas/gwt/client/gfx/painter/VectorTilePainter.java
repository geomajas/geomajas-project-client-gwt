/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.gfx.painter;

import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.gfx.paintable.Image;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.cache.tile.VectorTile;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.Matrix;
import org.geomajas.gwt.client.spatial.transform.WorldViewTransformer;
import org.geomajas.gwt.client.util.DOM;

/**
 * Paints a vector tile.
 * 
 * @author Pieter De Graef
 */
public class VectorTilePainter implements Painter {

	private MapView mapView;

	private WorldViewTransformer transformer;

	public VectorTilePainter(MapView mapView) {
		this.mapView = mapView;
		transformer = new WorldViewTransformer(mapView);
	}

	public String getPaintableClassName() {
		return VectorTile.class.getName();
	}

	public void paint(Paintable paintable, GraphicsContext graphics) {
		VectorTile tile = (VectorTile) paintable;
		Matrix transformationMatrix = createTransformationMatrix(tile);

		if (tile.getFeatureImage() != null) {
			Image im = tile.getFeatureImage();
			graphics.drawGroup(tile.getId(), DOM.NS_VML, transformationMatrix);
			graphics.drawImage(tile.getId() + ".img", im.getHref(), im.getBounds(), im.getStyle(), false);
		} else if (tile.getFeatureFragment() != null) {
			graphics.drawGroup(tile.getId(), DOM.NS_VML, (int) tile.getScreenWidth(), (int) tile.getScreenHeight(),
					transformationMatrix);
			graphics.drawData(tile.getId() + ".data", tile.getFeatureFragment());
		}

		if (tile.getLabelImage() != null) {
			Image im = tile.getLabelImage();
			graphics.drawGroup(createLabelId(tile), DOM.NS_VML, transformationMatrix);
			graphics.drawImage(createLabelId(tile) + ".img", im.getHref(), im.getBounds(), im.getStyle(), false);
		} else if (tile.getLabelFragment() != null) {
			graphics.drawGroup(createLabelId(tile), DOM.NS_VML, (int) tile.getScreenWidth(),
					(int) tile.getScreenHeight(), transformationMatrix);
			graphics.drawData(createLabelId(tile) + ".data", tile.getLabelFragment());
		}
	}

	/**
	 * Delete a <code>Paintable</code> object from the given <code>GraphicsContext</code>. It the object does not exist,
	 * nothing will be done.
	 * 
	 * @param paintable
	 *            The MapModel
	 * @param graphics
	 *            The context to paint on.
	 */
	public void deleteShape(Paintable paintable, GraphicsContext graphics) {
		graphics.deleteShape(paintable.getId(), false);
		graphics.deleteShape(createLabelId(paintable), false);
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private String createLabelId(Paintable paintable) {
		return paintable.getId().replace("features", "labels");
	}

	private Matrix createTransformationMatrix(VectorTile tile) {
		// We assume the geometries are in screen space, beginning from a tile's upper-left corner.

		// The map has already been translated by this, so we compensate again.
		Matrix trans = mapView.getPanToViewTranslation();
		double dX = 0;
		double dY = 0;

		// clipped tiles have the pan origin as origin, so no need to translate:
		if (!tile.isClipped()) {
			// To find the origin of the tile, we transform it's bounds to view space.
			Bbox viewBounds = transformer.worldToView(tile.getBounds());
			dX = Math.round(viewBounds.getX() - trans.getDx());
			dY = Math.round(viewBounds.getY() - trans.getDy());
		}

		return new Matrix(1, 0, 0, 1, dX, dY);
	}
}
