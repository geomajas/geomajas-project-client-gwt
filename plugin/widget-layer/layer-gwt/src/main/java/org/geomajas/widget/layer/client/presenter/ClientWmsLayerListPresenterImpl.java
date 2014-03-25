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
package org.geomajas.widget.layer.client.presenter;

import org.geomajas.gwt.client.map.layer.InternalClientWmsLayer;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.widget.MapWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Extension of {@link RemovableLayerListPresenterImpl}
 * for client side layers.
 *
 * @author Jan Venstermans
 *
 */
public class ClientWmsLayerListPresenterImpl extends RemovableLayerListPresenterImpl {


	public ClientWmsLayerListPresenterImpl(MapWidget mapwidget) {
		super(mapwidget);
	}

	@Override
	public void updateView() {
		getView().updateView(getLayerList());
	}

	public int layerCount() {
		return getLayerList().size();
	}

	private List<Layer<?>> getLayerList() {
		List<Layer<?>> layerList = new ArrayList<Layer<?>>();
		for (Layer layer : getMapWidget().getMapModel().getLayers()) {
			if (layer instanceof InternalClientWmsLayer) {
				layerList.add(layer);
			}
		}
		return layerList;
	}
}
