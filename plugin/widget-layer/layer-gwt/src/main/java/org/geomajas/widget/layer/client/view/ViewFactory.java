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
package org.geomajas.widget.layer.client.view;

import org.geomajas.widget.layer.client.presenter.CreateClientWmsPresenter;
import org.geomajas.widget.layer.client.presenter.RemovableLayerListPresenter;
import org.geomajas.widget.layer.client.presenter.LayerListPresenter;

/**
 * Manager of views.
 * 
 * @author Jan Venstermans
 * 
 */
public interface ViewFactory {

	LayerListPresenter.View createLayerListView();

	RemovableLayerListPresenter.View createLayerListWithRemoveButtonView();

	CreateClientWmsPresenter.ControllerButtonsView createControllerButtonsView();

	CreateClientWmsPresenter.GetCapabilitiesView createGetCapabilitiesView();

	CreateClientWmsPresenter.SelectLayerView createSelectLayerView();

	CreateClientWmsPresenter.EditLayerSettingsView createEditLayerSettingsView();
}
