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

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt.client.map.event.LayerChangedHandler;
import org.geomajas.gwt.client.map.event.LayerLabeledEvent;
import org.geomajas.gwt.client.map.event.LayerShownEvent;
import org.geomajas.gwt.client.map.event.MapModelChangedEvent;
import org.geomajas.gwt.client.map.event.MapModelChangedHandler;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link LayerListPresenter}.
 * The layers list is retrieved from a {@link MapWidget}.
 * 
 * @author Jan Venstermans
 * 
 */
public class LayerListPresenterImpl implements LayerListPresenter, LayerListPresenter.Handler, MapModelChangedHandler,
		LayerChangedHandler {

	private MapWidget mapWidget;

	private int amountOfRasterLayers; // necessary for drag drop in gwt2 !

	private List<HandlerRegistration> layerRegistrations = new ArrayList<HandlerRegistration>();

	private View view;

	public LayerListPresenterImpl(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
		this.view = createViewInConstructor();
		bind();
		if (mapWidget.getMapModel().isInitialized()) {
			updateView();
		}
	}

	protected View createViewInConstructor() {
		View view = org.geomajas.widget.layer.client.Layer.getViewFactory().createLayerListView();
		view.setHandler(this);
		return view;
	}

	private void bind() {
		mapWidget.getMapModel().addMapModelChangedHandler(this);
	}

	protected MapWidget getMapWidget() {
		return mapWidget;
	}

	@Override
	public void onMoveLayer(Layer layer, int index) {
		boolean success = false;
		if (layer instanceof VectorLayer) {
			// in gwt client, the index must be transformed to the 'vector' index, i.e. the nth vector layer
			// because raster layers are first in the list, we need to subtract
			// amountOfRasterLayers from given index value
			success = mapWidget.getMapModel().moveVectorLayer((VectorLayer) layer, index - amountOfRasterLayers);
		} else if (layer instanceof RasterLayer) {
			// in gwt client, the index must be transformed to the 'raster' index, i.e. the nth raster layer.
			// because raster layers are first in the list, we can take given index value
			success = mapWidget.getMapModel().moveRasterLayer((RasterLayer) layer, index);
		}
		if (!success) {
			// notify user ?
		}
	}

	@Override
	public void onToggleVisibility(Layer layer) {
		layer.setVisible(!layer.isShowing());
	}

	@Override
	public void setDragDropEnabled(boolean dragDropEnabled) {
		view.setDragDropEnabled(dragDropEnabled);
	}

	@Override
	public void updateView() {
		view.updateView(mapWidget.getMapModel().getLayers());
	}

	@Override
	public View getView() {
		return view;
	}

	protected void setView(View view) {
	   this.view = view;
	}

	@Override
	public Widget getWidget() {
		return view.getWidget();
	}

	@Override
	public void onMapModelChanged(MapModelChangedEvent event) {
		List<Layer<?>> layers = mapWidget.getMapModel().getLayers();
		amountOfRasterLayers = layers.size() -
				LayerListPresenterImpl.this.mapWidget.getMapModel().getVectorLayers().size();
		for (HandlerRegistration registration : layerRegistrations) {
			registration.removeHandler();
		}
		layerRegistrations.clear();
		for (Layer layer : layers) {
			layerRegistrations.add(layer.addLayerChangedHandler(new LayerChangedHandler() {
				@Override
				public void onVisibleChange(LayerShownEvent event) {
					updateView();
				}

				@Override
				public void onLabelChange(LayerLabeledEvent event) {
				   updateView();
				}
			}));
		}
		LayerListPresenterImpl.this.updateView();
	}

	@Override
	public void onVisibleChange(LayerShownEvent event) {
		updateView();
	}

	@Override
	public void onLabelChange(LayerLabeledEvent event) {
		updateView();
	}
}
