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
package org.geomajas.widget.searchandfilter.editor.client;

import com.smartgwt.client.widgets.Canvas;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.plugin.deskmanager.client.gwt.manager.editor.VectorLayerWidgetEditor;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;
import org.geomajas.widget.searchandfilter.configuration.client.SearchesInfo;
import org.geomajas.widget.searchandfilter.editor.client.presenter.SearchesPresenter;
import org.geomajas.widget.searchandfilter.editor.client.presenter.SearchesPresenterImpl;

/**
 * Editor for the searches configuration.
 * Actualy a wrapper around {@link org.geomajas.widget.searchandfilter.editor.client.view.SearchesView}.
 *
 * @author Jan Venstermans
 *
 */
public class SearchConfigurationEditor implements VectorLayerWidgetEditor {

	private SearchesPresenter searchesPresenter;

	private SearchesStatus status;

	public SearchConfigurationEditor() {
		status = SearchesStatusImpl.getInstance();
		//create presenters
		searchesPresenter = new SearchesPresenterImpl();
	}

	@Override
	public Canvas getCanvas() {
		return searchesPresenter.getCanvas();
	}

	@Override
	public ClientWidgetInfo getWidgetConfiguration() {
		return status.getSearchesInfo();
	}

	@Override
	public void setWidgetConfiguration(ClientWidgetInfo configuration) {
		if (configuration == null) {
			status.setSearchesInfo(new SearchesInfo());
		} else if (configuration instanceof SearchesInfo) {
			status.setSearchesInfo((SearchesInfo) configuration);
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void setDisabled(boolean disabled) {
		status.setDisabled(disabled);
	}

	@Override
	public void setLayer(LayerModelDto layerModel) {
		// Do nothing
	}


	@Override
	public void setClientVectorLayerInfo(ClientVectorLayerInfo clientVectorLayerInfo) {
		status.setClientVectorLayerInfo(clientVectorLayerInfo);
	}
}
