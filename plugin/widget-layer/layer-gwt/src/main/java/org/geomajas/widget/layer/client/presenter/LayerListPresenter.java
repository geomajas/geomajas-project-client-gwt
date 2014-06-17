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

import com.smartgwt.client.widgets.grid.ListGrid;
import org.geomajas.gwt.client.map.layer.Layer;

import java.util.List;

/**
 * Presenter for a list of layers.
 * 
 * @author Jan Venstermans
 * 
 */
public interface LayerListPresenter {

	/**
	 * ControllerButtonsViewImpl of the presenter.
	 */
	public interface View {
		void setDragDropEnabled(boolean dragDropEnabled);

		void setHandler(Handler handler);

		void updateView(List<Layer<?>> layers);

		ListGrid getWidget();
	}

	/**
	 * ControllersButtonHandler of the presenter.
	 */
	public interface Handler {

		void onMoveLayer(Layer layer, int index);

		void onToggleVisibility(Layer layer);
	}

	void setDragDropEnabled(boolean dragDropEnabled);

	void updateView();

	View getView();

	ListGrid getWidget();
}
