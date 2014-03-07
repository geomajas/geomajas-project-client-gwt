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
import java.util.Map;

/**
 * Interface for the presenter of {@link org.geomajas.widget.searchandfilter.configuration.client.SearchesInfo}.
 *
 * @author Jan Venstermans
 */
public interface SearchAttributePresenter {

	void setSearchAttribute(SearchAttribute searchAttribute);

	interface View {
		void setAttributeNameMap(LinkedHashMap<String, String> attributeNameMap);

		void setSelectedAttributeName(String attributeName);
		String getSelectedAttributeName();

		void setOperationMap(LinkedHashMap<SearchAttribute.Operation, String> operationMap);
		void selectOperationMap(SearchAttribute.AttributeType attributeType);

		void setSelectedOperation(SearchAttribute.Operation operation);
		SearchAttribute.Operation getSelectedOperation();

		void setInputTypeMap(LinkedHashMap<SearchAttribute.InputType, String> inputTypeMap);
		void selectInputTypeMap(SearchAttribute.AttributeType attributeType);

		void setSelectedInputType(SearchAttribute.InputType inputType);
		SearchAttribute.InputType getSelectedInputType();

		void setLabelText(String labelText);

		void setFieldsEnabled(boolean enabled);

		void setHandler(Handler handler);

		//void update();

		Canvas getCanvas();

		boolean validate();

		String getLabel();

		void show();

		void hide();

		void clearValues();

		void setDefaultOperationValueMaps(Map<SearchAttribute.AttributeType, LinkedHashMap<SearchAttribute.Operation, String>> defaultOperationValueMaps);

		void setDefaultInputTypeValueMaps(Map<SearchAttribute.AttributeType, LinkedHashMap<SearchAttribute.InputType, String>> defaultInputTypeValueMaps);
	}

	interface Handler extends SavePresenter.Handler {
	    void onSelectAttributeName(String attributeName);
	}

	void updateView();

	View getView();

	void editSelectedAttribute();

	void createSearchAttribute();
}
