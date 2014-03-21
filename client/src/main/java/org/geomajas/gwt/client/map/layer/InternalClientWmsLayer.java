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
package org.geomajas.gwt.client.map.layer;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.event.LayerShownEvent;
import org.geomajas.gwt.client.map.layer.configuration.ClientWmsLayerInfo;
import org.geomajas.gwt.client.map.store.ClientWmsRasterLayerStore;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt2.client.map.render.Tile;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.TileCode;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * The client side representation of a wms raster layer. Wraps a gwt2 WMS layer.
 * </p>
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 * @since 2.0.0
 */
@Api
public class InternalClientWmsLayer extends RasterLayer {

	private ClientWmsLayer wmsLayer;

	/**
	 * The only constructor! Set the MapModel and the layer info.
	 *
	 * @param mapModel
	 *            The model of layers and features behind a map. This layer will be a part of this model.
	 */
	public InternalClientWmsLayer(MapModel mapModel, ClientWmsLayerInfo clientWmsLayerInfo) {
		super(mapModel, clientWmsLayerInfo);
		this.wmsLayer = clientWmsLayerInfo.getWmsLayer();
		this.wmsLayer.setMapModel(mapModel);

		store = new ClientWmsRasterLayerStore(this);
	}

	public List<RasterTile> getTiles(Bbox worldBounds, double scale) {
		List<Tile> tiles = wmsLayer.getTiles(1 / scale, worldBounds.toDtoBbox());

		List<RasterTile> rasterTiles = new ArrayList<RasterTile>(tiles.size());
		for (Tile tile : tiles) {
			RasterTile rasterTile = new RasterTile();
			rasterTile.setUrl(tile.getUrl());
			rasterTile.setBounds(tile.getBounds());
			rasterTile.setCode(convertTileCode(tile));
			rasterTile.setId(tile.getCode().toString());
			rasterTiles.add(rasterTile);
		}
		return rasterTiles;
	}

	private TileCode convertTileCode(Tile tile) {
		return new TileCode(tile.getCode().getTileLevel(), tile.getCode().getX(), tile.getCode().getY());
	}

	public ClientWmsLayer getWmsLayer() {
		return wmsLayer;
	}

	/**
	 * Update showing state.
	 *
	 * @param fireEvents Should events be fired if state changes?
	 */
	/*protected void updateShowing(boolean fireEvents) {
		//setShowing(isVisible());
		// don't do anything?
	} */

}
