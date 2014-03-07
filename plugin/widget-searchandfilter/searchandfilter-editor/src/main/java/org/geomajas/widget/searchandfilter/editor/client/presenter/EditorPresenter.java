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

import java.util.List;

/**
 * Interface for views and handlers interfaces for specific aspects of the implemented panels etc.
 *
 * @author Jan Venstermans
 */
public interface EditorPresenter {

	/**
	 * View that can show a separate window.
	 */
	interface WindowView {
		void show();

		void hide();
	}

	/**
	 * View containing a form.
	 */
	interface FormView {
		boolean validateForm();

		void clearFormValues();
	}

	/**
	 * View containing a grid.
	 * @param <T> the model element that is represented in the grid.
	 *              One row represents data of one T instance.
	 */
	interface GridView<T> {
		void updateGrid(List<T> list);
	}

	/**
	 * View for saving data.
	 */
	interface SaveView {
		void setSaveHandler(SaveHandler handler);
	}

	/**
	 * Handler part of {@link EditorPresenter}.
	 */
	interface SaveHandler {
		void onSave();
	}

}
