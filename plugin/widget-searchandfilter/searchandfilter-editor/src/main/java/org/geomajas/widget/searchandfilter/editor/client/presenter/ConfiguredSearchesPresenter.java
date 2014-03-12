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
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearch;

/**
 * Interface for the presenter that deals with changing
 * {@link org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchesInfo}.
 *
 * @author Jan Venstermans
 */
public interface ConfiguredSearchesPresenter {

	/**
	 * View interface for {@link ConfiguredSearchesPresenter}.
	 */
	interface View extends EditorPresenter.GridView<ConfiguredSearch> {

		void setHandler(Handler handler);

		Canvas getCanvas();
	}

	/**
	 * Handler interface for {@link ConfiguredSearchesPresenter}.
	 */
	interface Handler {
		void onAddSearch();

		void onSelect(ConfiguredSearch search);

		void onEdit(ConfiguredSearch search);

		void onRemove(ConfiguredSearch search);
	}

	View getView();

	Canvas getCanvas();
}
