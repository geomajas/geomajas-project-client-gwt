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

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Interface for the presenter that deals with changing
 * {@link org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchAttribute}.
 *
 * @author Jan Venstermans
 */
public interface ConfiguredSearchAttributePresenter {

	/**
	 * View interface for {@link ConfiguredSearchAttributePresenter}.
	 */
	interface View extends EditorPresenter.FormView, EditorPresenter.WindowView, EditorPresenter.GridView<String> {

		void setHandler(Handler handler);

		Canvas getCanvas();

		/* model elements setters */

		void setSelectedAttributeName(String attributeName);
		void setSelectedOperation(ConfiguredSearchAttribute.Operation operation);
		void setSelectedInputType(ConfiguredSearchAttribute.InputType inputType);
		void setLabelText(String labelText);

		/* model elements getters */

		String getSelectedAttributeName();
		ConfiguredSearchAttribute.Operation getSelectedOperation();
		ConfiguredSearchAttribute.InputType getSelectedInputType();
		String getLabel();

		/* configuration after layer selection */

		void setAttributeNameMap(LinkedHashMap<String, String> attributeNameMap);

		/* configuration after attribute name selection */

		void setOperationMap(LinkedHashMap<ConfiguredSearchAttribute.Operation, String> operationMap);
		void setInputTypeMap(LinkedHashMap<ConfiguredSearchAttribute.InputType, String> inputTypeMap);
		void setFieldsEnabled(boolean enabled);
		void setInputTypeDropDown(boolean inputDropDown);

		List<String> getDropDownValues();
	}

	/**
	 * Handler interface for {@link ConfiguredSearchAttributePresenter}.
	 */
	interface Handler extends EditorPresenter.SaveHandler {
		void onSelectAttributeName(String attributeName);
		void onChangeSelectInputType();
		void onAddDropDownValue();
		void onChangeDropdownValues();

		void onRemove(String dropdownValue);
	}

	void updateView();

	View getView();

	void editSelectedAttribute();

	void createSearchAttribute();

	void setSearchAttribute(ConfiguredSearchAttribute searchAttribute);
}
