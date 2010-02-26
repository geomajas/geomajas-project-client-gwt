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

package org.geomajas.internal.rendering.painter.tile;

import java.awt.geom.AffineTransform;
import java.io.StringWriter;

import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.internal.layer.feature.InternalFeatureImpl;
import org.geomajas.internal.layer.tile.InternalTileImpl;
import org.geomajas.internal.rendering.DefaultSvgDocument;
import org.geomajas.internal.rendering.DefaultVmlDocument;
import org.geomajas.internal.rendering.strategy.TileService;
import org.geomajas.internal.rendering.writers.svg.SvgFeatureScreenWriter;
import org.geomajas.internal.rendering.writers.svg.SvgFeatureTileWriter;
import org.geomajas.internal.rendering.writers.svg.SvgLabelTileWriter;
import org.geomajas.internal.rendering.writers.vml.VmlFeatureScreenWriter;
import org.geomajas.internal.rendering.writers.vml.VmlLabelTileWriter;
import org.geomajas.internal.rendering.writers.vml.VmlVectorTileWriter;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;
import org.geomajas.rendering.painter.tile.TilePainter;
import org.geomajas.service.GeoService;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * <p>
 * TilePainter implementation for {@link InternalVectorTile} objects.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class StringContentTilePainter implements TilePainter {

	private final Logger log = LoggerFactory.getLogger(StringContentTilePainter.class);

	/**
	 * The maximum number of allowed fraction digits.
	 */
	protected static final int MAXIMUM_FRACTION_DIGITS = 0;

	/**
	 * Should this painter paint a feature's geometries or not?
	 */
	private boolean paintGeometries = true;

	/**
	 * Should this painter paint a feature's labels or not?
	 */
	private boolean paintLabels;

	/**
	 * The layer wherein the tiles lie.
	 */
	private VectorLayer layer;

	/**
	 * The layer style.
	 */
	private NamedStyleInfo style;

	/**
	 * Rendering output type. This can be either "SVG" or "VML". This depends on what the client requests.
	 */
	private String renderer;

	/**
	 * The tile to render.
	 */
	private InternalTile tile;

	/**
	 * The current client-side scale.
	 */
	private double scale;

	/**
	 * The tile's bounding box.
	 */
	private Envelope bbox;

	/**
	 * Transformer that transforms the feature's geometries from world to view space.
	 */
	private GeometryCoordinateSequenceTransformer unitToPixel;

	/**
	 * A DOM document for parsing the tile's features.
	 */
	private GraphicsDocument featureDocument;

	/**
	 * A DOM document for parsing the tile's labels.
	 */
	private GraphicsDocument labelDocument;

	/**
	 * The current origin may differ, depending on whether or not the client has been panning.
	 */
	private Coordinate panOrigin;

	private GeoService geoService;

	private CoordinateReferenceSystem targetCrs;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Initialize this painter with all the info it needs.
	 * 
	 * @param layer
	 *            The vector layer wherein the tiles lie.
	 * @param style
	 *            The rendering style.
	 * @param renderer
	 *            Rendering output type: "SVG" or "VML".
	 * @param scale
	 *            The current client-side scale. Needed for creating the world to view space coordinate transformer.
	 * @param panOrigin
	 *            The current origin may differ, depending on whether or not the client has been panning.Needed for
	 *            creating the world to view space coordinate transformer.
	 */
	public StringContentTilePainter(VectorLayer layer, NamedStyleInfo style, String renderer, double scale,
			Coordinate panOrigin, GeoService geoService, CoordinateReferenceSystem targetCrs) {
		this.layer = layer;
		// @todo: duplicate code, can we just depend on the VectorLayerService ?
		if (style == null) {
			// no style specified, take the first
			style = layer.getLayerInfo().getNamedStyleInfos().get(0);
		} else if (style.getFeatureStyles().isEmpty()) {
			// only name specified, find it
			style = layer.getLayerInfo().getNamedStyleInfo(style.getName());
		}

		this.style = style;
		this.renderer = renderer;
		this.scale = scale;
		this.panOrigin = panOrigin;
		this.geoService = geoService;
		this.targetCrs = targetCrs;
	}

	// -------------------------------------------------------------------------
	// TilePainter implementation:
	// -------------------------------------------------------------------------

	/**
	 * Paint the tile! The tile must be an instance of {@link InternalTile}. This function will create 2 DOM documents
	 * for geometries and labels. In case the renderer says "SVG" these documents will be of the type
	 * {@link org.geomajas.internal.rendering.DefaultSvgDocument}, otherwise
	 * {@link org.geomajas.internal.rendering.DefaultVmlDocument}. These documents in turn are built using
	 * {@link org.geomajas.internal.rendering.writers.GraphicsWriter} classes.
	 * 
	 * @param tileToPaint
	 *            The instance of {@link InternalTile}. Using the DOM documents, the tile's "featureFragment" and
	 *            "labelFragment" will be created.
	 * @return Returns a fully rendered vector tile.
	 */
	public InternalTile paint(InternalTile tileToPaint) throws RenderException {
		if (tileToPaint != null) {
			tile = tileToPaint;

			// Create the SVG / VML feature fragment:
			if (paintGeometries && featureDocument == null) {
				StringWriter writer = new StringWriter();
				try {
					featureDocument = createFeatureDocument(writer);
					featureDocument.setRootId(layer.getLayerInfo().getId());
					featureDocument.writeObject(tile, false);
					featureDocument.flush();
				} catch (RenderException e) {
					log.error("Unable to write this tile's feature fragment", e);
				}
				((InternalTileImpl) tile).setFeatureContent(writer.toString());
			}

			// Create the SVG / VML label fragment:
			if (paintLabels && labelDocument == null) {
				StringWriter writer = new StringWriter();
				try {
					labelDocument = createLabelDocument(writer, style.getLabelStyle());
					labelDocument.setRootId(layer.getLayerInfo().getId());
					labelDocument.writeObject(tileToPaint, false);
					labelDocument.flush();
				} catch (RenderException e) {
					log.error("Unable to write this tile's label fragment", e);
				}
				((InternalTileImpl) tile).setLabelContent(writer.toString());
			}
			return tile;
		}

		return tileToPaint;
	}

	/**
	 * Enables or disabled the use of painter that paint the geometries of the features in the tile.
	 * 
	 * @param paintGeometries
	 *            true or false.
	 */
	public void setPaintGeometries(boolean paintGeometries) {
		this.paintGeometries = paintGeometries;
	}

	/**
	 * Enables or disabled the use of painter that paint the labels of the features in the tile.
	 * 
	 * @param paintLabels
	 *            true or false.
	 */
	public void setPaintLabels(boolean paintLabels) {
		this.paintLabels = paintLabels;
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	/**
	 * Create a document that parses the tile's featureFragment, using GraphicsWriter classes.
	 * 
	 * @param writer
	 *            writer
	 * @return document
	 * @throws RenderException
	 *             oops
	 */
	private GraphicsDocument createFeatureDocument(StringWriter writer) throws RenderException {
		if (TileMetadata.PARAM_SVG_RENDERER.equalsIgnoreCase(renderer)) {
			DefaultSvgDocument document = new DefaultSvgDocument(writer, false);
			document.setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS);
			document.registerWriter(InternalFeatureImpl.class, new SvgFeatureScreenWriter(getTransformer()));
			document.registerWriter(InternalTileImpl.class, new SvgFeatureTileWriter());
			return document;
		} else if (TileMetadata.PARAM_VML_RENDERER.equalsIgnoreCase(renderer)) {
			DefaultVmlDocument document = new DefaultVmlDocument(writer);
			int coordWidth = (int) tile.getScreenWidth();
			int coordHeight = (int) tile.getScreenHeight();
			document.registerWriter(InternalFeatureImpl.class, new VmlFeatureScreenWriter(getTransformer(), coordWidth,
					coordHeight));
			document.registerWriter(InternalTileImpl.class, new VmlVectorTileWriter(coordWidth, coordHeight));
			document.setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS);
			return document;
		} else {
			throw new RenderException(ExceptionCode.RENDERER_TYPE_NOT_SUPPORTED, renderer);
		}
	}

	/**
	 * Create a document that parses the tile's labelFragment, using GraphicsWriter classes.
	 */
	private GraphicsDocument createLabelDocument(StringWriter writer, LabelStyleInfo labelStyleInfo)
			throws RenderException {

		if (TileMetadata.PARAM_SVG_RENDERER.equalsIgnoreCase(renderer)) {
			DefaultSvgDocument document = new DefaultSvgDocument(writer, false);
			document.setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS);
			document.registerWriter(InternalTileImpl.class, new SvgLabelTileWriter(getTransformer(), labelStyleInfo
					.getBackgroundStyle(), geoService));
			return document;
		} else if (TileMetadata.PARAM_VML_RENDERER.equalsIgnoreCase(renderer)) {
			DefaultVmlDocument document = new DefaultVmlDocument(writer);
			int coordWidth = (int) Math.round(scale * getTileBbox().getWidth());
			int coordHeight = (int) Math.round(scale * getTileBbox().getHeight());
			document.registerWriter(InternalFeatureImpl.class, new VmlFeatureScreenWriter(getTransformer(), coordWidth,
					coordHeight));
			document.registerWriter(InternalTileImpl.class, new VmlLabelTileWriter(coordWidth, coordHeight,
					getTransformer(), labelStyleInfo.getBackgroundStyle(), geoService));
			document.setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS);
			return document;
		} else {
			throw new RenderException(ExceptionCode.RENDERER_TYPE_NOT_SUPPORTED, renderer);
		}
	}

	/**
	 * Can't we get this out of this class???
	 * 
	 * @return
	 */
	private GeometryCoordinateSequenceTransformer getTransformer() throws RenderException {
		if (unitToPixel == null) {
			unitToPixel = new GeometryCoordinateSequenceTransformer();
			if (tile.isClipped()) {
				// find coords wrt to pan origin (0,0) and scale (pix/unit)
				unitToPixel.setMathTransform(ProjectiveTransform.create(new AffineTransform(scale, 0, 0, -scale, -scale
						* panOrigin.x, scale * panOrigin.y)));
			} else {
				// find coords wrt to upper left corner (0,0) and scale
				// (pix/unit)
				unitToPixel.setMathTransform(ProjectiveTransform.create(new AffineTransform(scale, 0, 0, -scale, -scale
						* getTileBbox().getMinX(), scale * (getTileBbox().getMinY() + getTileBbox().getHeight()))));
			}
		}
		return unitToPixel;
	}

	/**
	 * Convenience method for fetching the tile's bbox.
	 * 
	 * @return bbox
	 * @throws RenderException crs conversion problems
	 */
	private Envelope getTileBbox() throws RenderException {
		if (bbox == null) {
			try {
				bbox = TileService.getTransformedTileBounds(tile, layer,
						geoService.findMathTransform(layer.getCrs(), targetCrs));
			} catch (GeomajasException ge) {
				throw new RenderException(ge);
			}
		}
		return bbox;
	}
}