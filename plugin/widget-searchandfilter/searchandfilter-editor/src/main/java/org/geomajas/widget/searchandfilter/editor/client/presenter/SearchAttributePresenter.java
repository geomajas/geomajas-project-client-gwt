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
import org.geomajas.widget.searchandfilter.editor.client.configuration.SearchAttribute;
import org.geomajas.widget.searchandfilter.editor.client.configuration.SearchConfig;

/**
 * Interface for the presenter of {@link org.geomajas.widget.searchandfilter.editor.client.configuration.SearchesInfo}.
 *
 * @author Jan Venstermans
 */
public interface SearchAttributePresenter {

	interface View {
	    void setSearchAttribute(SearchAttribute searchAttribute);

		void setHandler(Handler handler);

		void updateStatus();

		void updateView();

		Canvas getCanvas();

		boolean validate();

		SearchAttribute getSearchAttribute();

		void show(SearchAttribute searchAttribute);

		void hide();
	}

	interface Handler extends SavePresenter.Handler {
	    void onSelect(SearchAttribute config);

		void onEdit(SearchAttribute config);
	}

	View getView();

	void editSelectedAttribute();

	void createSearchAttribute();
}
