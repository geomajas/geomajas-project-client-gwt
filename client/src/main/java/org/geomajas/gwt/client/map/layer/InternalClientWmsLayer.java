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
package org.geomajas.gwt.client.map.layer;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.layer.configuration.ClientWmsLayerInfo;
import org.geomajas.gwt.client.map.store.ClientWmsRasterLayerStore;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.client.map.render.Tile;
import org.geomajas.gwt2.client.service.TileService;
import org.geomajas.gwt2.plugin.wms.client.WmsClient;
import org.geomajas.gwt2.plugin.wms.client.layer.WmsLayer;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.TileCode;

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

	public List<RasterTile> getTiles(Bbox worldBounds, double resolution) {
		List<Tile> tiles = getTiles(wmsLayer, resolution, worldBounds.toDtoBbox());
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

	
	private List<Tile> getTiles(WmsLayer wmsLayer, double resolution, org.geomajas.geometry.Bbox worldBounds) {
 		TileConfiguration tileConfig = wmsLayer.getTileConfiguration();
 		List<org.geomajas.gwt2.client.map.render.TileCode> codes = TileService.getTileCodesForBounds(tileConfig,
 				worldBounds, resolution);
 		List<Tile> tiles = new ArrayList<Tile>();
 		if (!codes.isEmpty()) {
 			double actualResolution = tileConfig.getResolution(codes.get(0).getTileLevel());
 			for (org.geomajas.gwt2.client.map.render.TileCode code : codes) {
 				org.geomajas.geometry.Bbox bounds = TileService.getWorldBoundsForTile(tileConfig, code);
 				Tile tile = new Tile(getScreenBounds(actualResolution, bounds));
 				tile.setCode(code);
 				tile.setUrl(WmsClient.getInstance().getWmsService().getMapUrl(wmsLayer.getConfiguration(),
 						bounds, tileConfig.getTileWidth(), tileConfig.getTileHeight()));
 				tiles.add(tile);
 			}
 		}
 		return tiles;
 	}
	
	private org.geomajas.geometry.Bbox getScreenBounds(double resolution, org.geomajas.geometry.Bbox worldBounds) {
		return new org.geomajas.geometry.Bbox(Math.round(worldBounds.getX() / resolution), -Math.round(worldBounds
				.getMaxY() / resolution), Math.round(worldBounds.getMaxX() / resolution)
				- Math.round(worldBounds.getX() / resolution), Math.round(worldBounds.getMaxY() / resolution)
				- Math.round(worldBounds.getY() / resolution));
	}

	private TileCode convertTileCode(Tile tile) {
		return new TileCode(tile.getCode().getTileLevel(), tile.getCode().getX(), tile.getCode().getY());
	}

}
