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
package org.geomajas.widget.layer.client.view;

import org.geomajas.widget.layer.client.presenter.LayerListPresenter;

/**
 * Default implementation of {@link ViewManager}.
 * 
 * @author Jan Venstermans
 * 
 */
public class ViewManagerImpl implements ViewManager {

	private LayerListPresenter.View layerListView;

	@Override
	public LayerListPresenter.View getLayerListView() {
		if (layerListView == null) {
			layerListView = new LayerListView();
		}
		return layerListView;
	}

	public void setLayerListView(LayerListPresenter.View layerListView) {
		this.layerListView = layerListView;
	}
}
