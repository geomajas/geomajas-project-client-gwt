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
package org.geomajas.widget.layer.client.presenter;

import org.geomajas.gwt.client.map.layer.InternalClientWmsLayer;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * Default implementation of {@link RemovableLayerListPresenter}.
 *
 * @author Jan Venstermans
 *
 */
public class RemovableLayerListPresenterImpl extends LayerListPresenterImpl
		implements RemovableLayerListPresenter, RemovableLayerListPresenter.Handler {

	private boolean showDeleteButtons;

	private String removeIconUrl;

	public RemovableLayerListPresenterImpl(MapWidget mapwidget) {
		super(mapwidget);
		showDeleteButtons = true;
	}

	@Override
	protected LayerListPresenter.View createViewInConstructor() {
		return createLayerListWithRemoveButtonView();
	}

	protected RemovableLayerListPresenter.View createLayerListWithRemoveButtonView() {
		RemovableLayerListPresenter.View view = org.geomajas.widget.layer.client.Layer.getViewFactory().
				createLayerListWithRemoveButtonView();
		view.setHandler(this);
		view.setDragDropEnabled(isDragDropEnabled());
		if (removeIconUrl != null) {
			view.setRemoveIconUrl(removeIconUrl);
		}
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
			setView(showDeleteButtons ? createLayerListWithRemoveButtonView() : createLayerListView());
		}
	}

	@Override
	public void setRemoveIconUrl(String removeIconUrl) {
		this.removeIconUrl = removeIconUrl;
		if (getView() instanceof RemovableLayerListPresenter.View) {
			((RemovableLayerListPresenter.View) getView()).setRemoveIconUrl(removeIconUrl);
		}
	}

	@Override
	public void onRemoveClientWmsLayer(InternalClientWmsLayer layer) {
		getMapWidget().getMapModel().removeLayer(layer);
	}

	@Override
	public void onToggleVisibility(Layer layer) {
		super.onToggleVisibility(layer);
	}
}
