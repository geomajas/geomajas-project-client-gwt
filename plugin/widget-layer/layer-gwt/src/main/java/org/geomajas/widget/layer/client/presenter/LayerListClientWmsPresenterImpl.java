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

import com.google.gwt.core.client.Callback;
import org.geomajas.gwt.client.map.layer.InternalClientWmsLayer;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.configuration.ClientWmsLayerInfo;
import org.geomajas.gwt.client.util.Log;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * Default implementation of {@link org.geomajas.widget.layer.client.presenter.LayerListClientWmsPresenter}.
 *
 * @author Jan Venstermans
 *
 */
public class LayerListClientWmsPresenterImpl extends LayerListPresenterImpl
		implements LayerListClientWmsPresenter, LayerListClientWmsPresenter.Handler {

	private CreateClientWmsPresenter createClientWmsPresenter;

	private boolean showDeleteButtons = true;

	public LayerListClientWmsPresenterImpl(MapWidget mapwidget) {
		super(mapwidget);
	}

	@Override
	protected LayerListPresenter.View createViewInConstructor() {
		LayerListClientWmsPresenter.View view = org.geomajas.widget.layer.client.Layer.getViewFactory().
				createLayerListClientWmsView();
		view.setHandler(this);
		return view;
	}

	public boolean isShowDeleteButtons() {
		return showDeleteButtons;
	}

	@Override
	public void setShowDeleteButtons(boolean showDeleteButtons) {
		if (this.showDeleteButtons != showDeleteButtons) {
			this.showDeleteButtons = showDeleteButtons;
			//change view
			setView(showDeleteButtons ? createViewInConstructor() : super.createViewInConstructor());
		}
	}

	@Override
	public void addClientWmsLayer() {
		createClientWmsPresenter = new CreateClientWmsPresenterImpl(getMapWidget());
		createClientWmsPresenter.createClientWmsLayer(new Callback<ClientWmsLayerInfo, String>() {
			@Override
			public void onFailure(String s) {
				// notify
			}

			@Override
			public void onSuccess(ClientWmsLayerInfo clientWmsLayerInfo) {
				getMapWidget().getMapModel().addLayer(clientWmsLayerInfo);
				Log.logServer(Log.LEVEL_INFO, "added layer to MapModel: " + clientWmsLayerInfo.toString());
				updateMapForClientLayers();
			}
		});
	}

	@Override
	public void onRemoveClientWmsLayer(InternalClientWmsLayer layer) {
		layer.setVisible(false);
		updateMapForClientLayers();
	}

	@Override
	public void onToggleVisibility(Layer layer) {
		super.onToggleVisibility(layer);
		updateMapForClientLayers();
	}

	private void updateMapForClientLayers() {
		for (Layer<?> layer : getMapWidget().getMapModel().getLayers()) {
			if (layer.getLayerInfo() instanceof ClientWmsLayerInfo) {
				getMapWidget().refreshLayer(layer);
			}
		}
		updateView();
	}
}
