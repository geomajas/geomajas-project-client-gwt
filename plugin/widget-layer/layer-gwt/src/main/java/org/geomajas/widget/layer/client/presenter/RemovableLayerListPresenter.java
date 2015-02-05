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

/**
 * Extension of {@link org.geomajas.widget.layer.client.presenter.LayerListPresenter}
 * for adding/deleting ClientWms layers.
 * 
 * @author Jan Venstermans
 * 
 */
public interface RemovableLayerListPresenter extends LayerListPresenter {

	/**
	 * ControllerButtonsViewImpl of the presenter.
	 */
	public interface View extends LayerListPresenter.View {
		void setHandler(Handler handler);

		void setRemoveIconUrl(String removeIconUrl);
	}

	/**
	 * ControllersButtonHandler of the presenter.
	 */
	public interface Handler extends LayerListPresenter.Handler {

		void onRemoveClientWmsLayer(InternalClientWmsLayer layer);
	}

	void setShowDeleteButtons(boolean showDeleteButtons);

	void setRemoveIconUrl(String removeIconUrl);
}
