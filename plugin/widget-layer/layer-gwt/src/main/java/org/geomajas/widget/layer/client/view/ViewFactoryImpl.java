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
 * Default implementation of {@link org.geomajas.widget.layer.client.view.ViewFactory}.
 *
 * @author Jan Venstermans
 *
 */
public class ViewFactoryImpl implements ViewFactory {

	@Override
	public LayerListPresenter.View createLayerListView() {
		return new LayerListView();
	}

	@Override
	public LayerListClientWmsPresenter.View createLayerListClientWmsView() {
		return new LayerListClientWmsView();
	}

	@Override
	public CreateClientWmsPresenter.ControllerButtonsView createControllerButtonsView() {
		return new ControllerButtonsView();
	}

	@Override
	public CreateClientWmsPresenter.GetCapabilitiesView createGetCapabilitiesView() {
		return new GetCapabilitiesViewImpl();
	}

	@Override
	public CreateClientWmsPresenter.SelectLayerView createSelectLayerView() {
		return new SelectLayerViewImpl();
	}
}
