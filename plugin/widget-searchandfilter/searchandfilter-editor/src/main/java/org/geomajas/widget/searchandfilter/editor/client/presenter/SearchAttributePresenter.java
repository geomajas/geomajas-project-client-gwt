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

import java.util.LinkedHashMap;

/**
 * Interface for the presenter that deals with changing
 * {@link org.geomajas.widget.searchandfilter.configuration.client.SearchAttribute}.
 *
 * @author Jan Venstermans
 */
public interface SearchAttributePresenter {

	/**
	 * View interface for {@link SearchAttributePresenter}.
	 */
	interface View extends EditorPresenter.FormView, EditorPresenter.WindowView, EditorPresenter.GridView<String> {

		void setHandler(Handler handler);

		Canvas getCanvas();

		/* model elements setters */

		void setSelectedAttributeName(String attributeName);
		void setSelectedOperation(SearchAttribute.Operation operation);
		void setSelectedInputType(SearchAttribute.InputType inputType);
		void setLabelText(String labelText);

		/* model elements getters */

		String getSelectedAttributeName();
		SearchAttribute.Operation getSelectedOperation();
		SearchAttribute.InputType getSelectedInputType();
		String getLabel();

		/* configuration after layer selection */

		void setAttributeNameMap(LinkedHashMap<String, String> attributeNameMap);

		/* configuration after attribute name selection */

		void setOperationMap(LinkedHashMap<SearchAttribute.Operation, String> operationMap);
		void setInputTypeMap(LinkedHashMap<SearchAttribute.InputType, String> inputTypeMap);
		void setFieldsEnabled(boolean enabled);
		void setInputTypeDropDown(boolean inputDropDown);
	}

	/**
	 * Handler interface for {@link SearchAttributePresenter}.
	 */
	interface Handler extends EditorPresenter.SaveHandler {
		void onSelectAttributeName(String attributeName);
		void onChangeSelectInputType();
		void onAddDropDownValue();
	}

	void updateView();

	View getView();

	void editSelectedAttribute();

	void createSearchAttribute();

	void setSearchAttribute(SearchAttribute searchAttribute);
}
