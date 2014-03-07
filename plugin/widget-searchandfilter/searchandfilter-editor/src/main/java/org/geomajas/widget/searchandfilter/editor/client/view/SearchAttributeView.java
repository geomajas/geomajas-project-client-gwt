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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Default implementation of {@link SearchAttributePresenter.View}.
 *
 * @author Jan Venstermans
 */
public class SearchAttributeView implements SearchAttributePresenter.View {

	private final SearchAndFilterEditorMessages MESSAGES =
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
	public Canvas getCanvas() {
		return window;
	}

	@Override
	public boolean validateForm() {
		return form.validate();
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
	public void clearFormValues() {
		form.clearValues();
	}

	/* setters for from elements */

	@Override
	public void setLabelText(String labelText) {
		label.setValue(labelText);
	}

	@Override
	public void setSelectedAttributeName(String attributeName) {
		setDropDownValue(attributeNameSelectItem, attributeName);
	}

	@Override
	public void setSelectedOperation(SearchAttribute.Operation operation) {
		setDropDownValue(operationSelectItem, operation);
	}

	@Override
	public void setSelectedInputType(SearchAttribute.InputType inputType) {
		setDropDownValue(inputTypeSelectItem, inputType);
	}

	private void setDropDownValue(SelectItem selectItem, Object key) {
		selectItem.setValue(key);
	}

	/* getters for from elements */

	@Override
	public String getLabel() {
		return label.getValueAsString();
	}

	@Override
	public String getSelectedAttributeName() {
		return getSelectedKey(attributeNameSelectItem, attributeNameMap);
	}

	@Override
	public SearchAttribute.Operation getSelectedOperation() {
		return getSelectedKey(operationSelectItem, operationMap);
	}

	@Override
	public SearchAttribute.InputType getSelectedInputType() {
		return getSelectedKey(inputTypeSelectItem, inputTypeMap);
	}

	private <T> T getSelectedKey(SelectItem selectItem,  LinkedHashMap<T, String> map) {
		try {
			return (T) selectItem.getValue();
		} catch (Exception e) {
			String stringValue = selectItem.getValueAsString();
			if (stringValue != null) {
				for (T key : map.keySet()) {
					if (stringValue.equals(key.toString())) {
						return key;
					}
				}
			}
		}
		return null;
	}

	/* fill drop down list */

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
	public void setOperationMap(LinkedHashMap<SearchAttribute.Operation, String> operationMap) {
		this.operationMap = operationMap;
		if (operationMap == null) {
			operationSelectItem.clearValue();
		} else {
			operationSelectItem.setValueMap(operationMap);
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
	public void setFieldsEnabled(boolean enabled) {
		label.setCanEdit(enabled);
		operationSelectItem.setCanEdit(enabled);
		inputTypeSelectItem.setCanEdit(enabled);
	}

	@Override
	public void setHandler(SearchAttributePresenter.Handler handler) {
		window.setSaveHandler(handler);
		this.handler = handler;
	}
}
