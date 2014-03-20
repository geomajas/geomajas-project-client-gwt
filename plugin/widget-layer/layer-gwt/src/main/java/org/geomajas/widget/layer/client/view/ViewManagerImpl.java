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

import org.geomajas.widget.layer.client.presenter.CreateClientWmsPresenter;
import org.geomajas.widget.layer.client.presenter.LayerListClientWmsPresenter;
import org.geomajas.widget.layer.client.presenter.LayerListPresenter;
import org.geomajas.widget.layer.client.view.wizard.GetCapabilitiesViewImpl;
import org.geomajas.widget.layer.client.view.wizard.SelectLayerViewImpl;

/**
 * Default implementation of {@link ViewManager}.
 * 
 * @author Jan Venstermans
 * 
 */
public class ViewManagerImpl implements ViewManager {

	private LayerListPresenter.View layerListView;
	private LayerListClientWmsPresenter.View layerListClientWmsView;
	private CreateClientWmsPresenter.ControllerButtonsView createClientWmsControllerButtonsView;
	private CreateClientWmsPresenter.GetCapabilitiesView getCapabilitiesView;
	private CreateClientWmsPresenter.SelectLayerView selectLayerView;

	@Override
	public LayerListPresenter.View getLayerListView() {
		if (layerListView == null) {
			// default view
			layerListView = new LayerListView();
		}
		return layerListView;
	}

	@Override
	public LayerListClientWmsPresenter.View getLayerListClientWmsView() {
		if (layerListClientWmsView == null) {
			// default view
			layerListClientWmsView = new LayerListClientWmsView();
		}
		return layerListClientWmsView;
	}

	@Override
	public CreateClientWmsPresenter.ControllerButtonsView getControllerButtonsView() {
		if (createClientWmsControllerButtonsView == null) {
			// default view
			createClientWmsControllerButtonsView = new ControllerButtonsView();
		}
		return createClientWmsControllerButtonsView;
	}

	@Override
	public CreateClientWmsPresenter.GetCapabilitiesView getGetCapabilitiesView() {
		if (getCapabilitiesView == null) {
			// default view
			getCapabilitiesView = new GetCapabilitiesViewImpl();
		}
		return getCapabilitiesView;
	}

	@Override
	public CreateClientWmsPresenter.SelectLayerView getSelectLayerView() {
		if (selectLayerView == null) {
			// default view
			selectLayerView = new SelectLayerViewImpl();
		}
		return selectLayerView;
	}

	public void setLayerListView(LayerListPresenter.View layerListView) {
		this.layerListView = layerListView;
	}

	public void setLayerListClientWmsView(LayerListClientWmsPresenter.View layerListClientWmsView) {
		this.layerListClientWmsView = layerListClientWmsView;
	}

	public void setCreateClientWmsControllerButtonsView(CreateClientWmsPresenter.ControllerButtonsView createClientWmsControllerButtonsView) {
		this.createClientWmsControllerButtonsView = createClientWmsControllerButtonsView;
	}

	public void setGetCapabilitiesView(CreateClientWmsPresenter.GetCapabilitiesView getCapabilitiesView) {
		this.getCapabilitiesView = getCapabilitiesView;
	}

	public void setSelectLayerView(CreateClientWmsPresenter.SelectLayerView selectLayerView) {
		this.selectLayerView = selectLayerView;
	}
}
