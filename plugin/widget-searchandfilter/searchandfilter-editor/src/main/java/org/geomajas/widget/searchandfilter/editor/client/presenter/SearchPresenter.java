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
import org.geomajas.widget.searchandfilter.configuration.client.SearchAttribute;
import org.geomajas.widget.searchandfilter.configuration.client.SearchConfig;

/**
 * Interface for the presenter of {@link org.geomajas.widget.searchandfilter.configuration.client.SearchesInfo}.
 *
 * @author Jan Venstermans
 */
public interface SearchPresenter {

	interface View {
	    void setSearchConfig(SearchConfig searchConfig);

		void setHandler(Handler handler);

		void update();

		Canvas getCanvas();

		boolean validate();

		SearchConfig getSearchConfig();

		void show(SearchConfig searchConfig);

		void hide();
	}

	interface Handler extends SavePresenter.Handler {
		void onAddAttribute();

	    void onSelect(SearchAttribute attribute);

		void onEdit(SearchAttribute attribute);
	}

	View getView();

	void editSelectedSearch();

	void createSearch();
}
