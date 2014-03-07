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
package org.geomajas.widget.searchandfilter.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.widget.searchandfilter.configuration.client.SearchAttribute;
import org.geomajas.widget.searchandfilter.editor.client.i18n.SearchAndFilterEditorMessages;
import org.geomajas.widget.searchandfilter.editor.client.presenter.SearchAttributePresenter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;

/**
 * Configuration window for individual searches.
 *
 * @author Jan Venstermans
 */
public class SearchAttributeView implements SearchAttributePresenter.View {

	private static final SearchAndFilterEditorMessages MESSAGES =
			GWT.create(SearchAndFilterEditorMessages.class);

	private static final int FORMITEM_WIDTH = 150;
	private static final String FRM_OPERATION = "operation";
	private static final String FRM_INPUTTYPE = "inputType";

	private SearchAttributePresenter.Handler handler;

	/* form elements */
	private DynamicForm form;

	private SelectItem attributeNameSelectItem;
	private SelectItem operationSelectItem;

	private SelectItem inputTypeSelectItem;

	private TextItem label;

	private SaveCancelWindow window;

	/* model elements */
	private LinkedHashMap<String, String> attributeNameMap;
	private LinkedHashMap<SearchAttribute.Operation, String> operationMap;
	private LinkedHashMap<SearchAttribute.InputType, String> inputTypeMap;

	private Map<SearchAttribute.AttributeType, LinkedHashMap<SearchAttribute.Operation, String>> defaultOperationValueMaps;
	private Map<SearchAttribute.AttributeType, LinkedHashMap<SearchAttribute.InputType, String>> defaultInputTypeValueMaps;

	/**
	 * Construct a search configuration window.
	 *
	 */
	public SearchAttributeView() {
		layout();
	}

	/**
	 *
	 */
	private void layout() {
		// form //

		form = new DynamicForm();
		form.setAutoFocus(true); /* Set focus on first field */
		form.setWidth(FORMITEM_WIDTH + 100);
		form.setWrapItemTitles(false);

		attributeNameSelectItem = new SelectItem();
		attributeNameSelectItem.setTitle(MESSAGES.searchAttributeWindowAttributeNameLabel());
		attributeNameSelectItem.setWidth(FORMITEM_WIDTH);
		attributeNameSelectItem.setRequired(true);
		attributeNameSelectItem.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent changedEvent) {
				handler.onSelectAttributeName(getSelectedAttributeName());
			}
		});

		operationSelectItem = new SelectItem();
		operationSelectItem.setName(FRM_OPERATION);
		operationSelectItem.setTitle(MESSAGES.searchAttributeWindowOperationLabel());
		operationSelectItem.setWidth(FORMITEM_WIDTH);
		operationSelectItem.setRequired(true);
		operationSelectItem.setAddUnknownValues(false);

		inputTypeSelectItem = new SelectItem();
		inputTypeSelectItem.setName(FRM_INPUTTYPE);
		inputTypeSelectItem.setTitle(MESSAGES.searchAttributeWindowInputTypeLabel());
		inputTypeSelectItem.setWidth(FORMITEM_WIDTH);
		inputTypeSelectItem.setRequired(true);
		inputTypeSelectItem.setAddUnknownValues(false);

		label = new TextItem();
		label.setTitle(MESSAGES.searchAttributeWindowLabelLabel());
		label.setRequired(true);
		label.setWidth(FORMITEM_WIDTH);

		form.setFields(attributeNameSelectItem, label, operationSelectItem, inputTypeSelectItem);

		// layout structure //
		VLayout layout = new VLayout(10);
		layout.addMember(form);
		window = new SaveCancelWindow(layout);
	}

	@Override
	public String getSelectedAttributeName() {
		Object value = attributeNameSelectItem.getValue();
		if (value != null && attributeNameMap != null && attributeNameMap.size() > 0) {
			for (String attrName : attributeNameMap.keySet()) {
				if (value.equals(attrName)) {
					return attrName;
				}
			}
		}
		return null;
	}

	@Override
	public SearchAttribute.Operation getSelectedOperation() {
		Object value = operationSelectItem.getValue();
		if (value != null && operationMap != null && operationMap.size() > 0) {
			for (SearchAttribute.Operation operation : operationMap.keySet()) {
				if (value.equals(operation)) {
					return operation;
				}
			}
		}
		return null;
	}

	@Override
	public SearchAttribute.InputType getSelectedInputType() {
		Object value = inputTypeSelectItem.getValue();
		if (value != null && inputTypeMap != null && inputTypeMap.size() > 0) {
			for (SearchAttribute.InputType type : inputTypeMap.keySet()) {
				if (value.equals(type)) {
					return type;
				}
			}
		}
		return null;
	}

	@Override
	public void setAttributeNameMap(LinkedHashMap<String, String> attributeNameMap) {
		this.attributeNameMap = attributeNameMap;
		if (attributeNameMap == null) {
			attributeNameSelectItem.clearValue();
		} else {
			attributeNameSelectItem.setValueMap(attributeNameMap);
		}

	}

	@Override
	public void setSelectedAttributeName(String attributeName) {
		if (attributeNameMap != null && attributeNameMap.size() > 0 &&
				attributeNameMap.keySet().contains(attributeName)) {
			attributeNameSelectItem.setValue(attributeNameMap.get(attributeName));
		}
	}

	@Override
	public void setOperationMap(LinkedHashMap<SearchAttribute.Operation, String> operationMap) {
		this.operationMap = operationMap;
		if (operationMap == null) {
			operationSelectItem.clearValue();
		} else {
			operationSelectItem.setValueMap(operationMap);
		}
	}

	@Override
	public void selectOperationMap(SearchAttribute.AttributeType attributeType) {
		form.getField(FRM_OPERATION).setValueMap(defaultOperationValueMaps.get(attributeType));
	}

	@Override
	public void setSelectedOperation(SearchAttribute.Operation operation) {
		if (operationMap != null && operationMap.size() > 0 &&
				operationMap.keySet().contains(operation)) {
			operationSelectItem.setValue(operationMap.get(operation));
		}
	}

	@Override
	public void setInputTypeMap(LinkedHashMap<SearchAttribute.InputType, String> inputTypeMap) {
		this.inputTypeMap = inputTypeMap;
		if (inputTypeMap == null) {
			inputTypeSelectItem.clearValue();
		} else {
			inputTypeSelectItem.setValueMap(inputTypeMap);
		}
	}

	@Override
	public void selectInputTypeMap(SearchAttribute.AttributeType attributeType) {
		form.getField(FRM_INPUTTYPE).setValueMap(defaultInputTypeValueMaps.get(attributeType));
	}

	@Override
	public void setSelectedInputType(SearchAttribute.InputType inputType) {
		if (inputTypeMap != null && inputTypeMap.size() > 0 &&
				inputTypeMap.keySet().contains(inputType)) {
			inputTypeSelectItem.setValue(inputTypeMap.get(inputType));
		}
	}

	@Override
	public void setLabelText(String labelText) {
		label.setValue(labelText);
	}

	@Override
	public void setFieldsEnabled(boolean enabled) {
		label.setCanEdit(enabled);
		operationSelectItem.setCanEdit(enabled);
		inputTypeSelectItem.setCanEdit(enabled);
	}

	@Override
	public void setHandler(SearchAttributePresenter.Handler handler) {
		window.setHandler(handler);
		this.handler = handler;
	}

	@Override
	public Canvas getCanvas() {
		return window;
	}

	@Override
	public boolean validate() {
		return form.validate();
	}

	@Override
	public String getLabel() {
		return label.getValueAsString();
	}

	@Override
	public void show() {
	  	window.show();
	}

	@Override
	public void hide() {
	   	window.hide();
	}

	@Override
	public void clearValues() {
		form.clearValues();
	}

	@Override
	public void setDefaultOperationValueMaps(final Map<SearchAttribute.AttributeType, LinkedHashMap<SearchAttribute.Operation, String>> defaultOperationValueMaps) {
		this.defaultOperationValueMaps = defaultOperationValueMaps;
	}

	@Override
	public void setDefaultInputTypeValueMaps(final Map<SearchAttribute.AttributeType, LinkedHashMap<SearchAttribute.InputType, String>> defaultInputTypeValueMaps) {
		this.defaultInputTypeValueMaps = defaultInputTypeValueMaps;
	}
}
