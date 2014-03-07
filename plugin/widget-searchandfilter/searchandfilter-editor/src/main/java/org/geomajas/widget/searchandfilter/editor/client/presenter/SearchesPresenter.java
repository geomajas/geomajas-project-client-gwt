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
package org.geomajas.widget.searchandfilter.editor.client.presenter;

import com.smartgwt.client.widgets.Canvas;
import org.geomajas.widget.searchandfilter.configuration.client.SearchConfig;

/**
 * Interface for the presenter that deals with changing
 * {@link org.geomajas.widget.searchandfilter.configuration.client.SearchesInfo}.
 *
 * @author Jan Venstermans
 */
public interface SearchesPresenter {

	/**
	 * View interface for {@link SearchesPresenter}.
	 */
	interface View extends EditorPresenter.GridView<SearchConfig> {

		void setHandler(Handler handler);

		Canvas getCanvas();
	}

	/**
	 * Handler interface for {@link SearchesPresenter}.
	 */
	interface Handler {
		void onAddSearch();

		void onSelect(SearchConfig config);

		void onEdit(SearchConfig config);
	}

	View getView();

	Canvas getCanvas();
}
