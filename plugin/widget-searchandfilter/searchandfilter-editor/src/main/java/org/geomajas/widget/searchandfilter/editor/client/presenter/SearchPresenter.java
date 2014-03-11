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
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchAttribute;

/**
 * Interface for the presenter that deals with changing
 * {@link org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearch}.
 *
 * @author Jan Venstermans
 */
public interface SearchPresenter {

	/**
	 * View interface for {@link SearchPresenter}.
	 */
	interface View extends EditorPresenter.FormView, EditorPresenter.WindowView,
			EditorPresenter.GridView<ConfiguredSearchAttribute> {

		void setHandler(Handler handler);

		Canvas getCanvas();

		/* model elements setters */

		void setTitle(String title);
		void setDescription(String description);
		void setTitleInWindow(String titleInWindow);
		void setIconUrl(String iconUrl);

		/* model elements getters */

		String getTitle();
		String getDescription();
		String getTitleInWindow();
		String getIconUrl();
	}

	/**
	 * Handler interface for {@link SearchPresenter}.
	 */
	interface Handler extends EditorPresenter.SaveHandler {
		void onAddAttribute();

		void onSelect(ConfiguredSearchAttribute attribute);

		void onEdit(ConfiguredSearchAttribute attribute);
	}

	View getView();

	void editSelectedSearch();

	void createSearch();
}
