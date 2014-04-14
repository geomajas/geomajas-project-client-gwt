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
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellSavedEvent;
import com.smartgwt.client.widgets.grid.events.CellSavedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.widget.searchandfilter.editor.client.i18n.SearchAndFilterEditorMessages;
import org.geomajas.widget.searchandfilter.editor.client.presenter.ConfiguredSearchAttributePresenter;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchAttribute;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Default implementation of {@link org.geomajas.widget.searchandfilter.editor.client
 * .presenter.ConfiguredSearchAttributePresenter.View}.
 *
 * @author Jan Venstermans
 */
public class ConfiguredSearchAttributeView implements ConfiguredSearchAttributePresenter.View {

	private final SearchAndFilterEditorMessages messages =
			GWT.create(SearchAndFilterEditorMessages.class);

	private static final int FORM_WIDTH = 200;
	private static final int FORMITEM_WIDTH = 150;
	private static final String FRM_OPERATION = "operation";
	private static final String FRM_INPUTTYPE = "inputType";

	private static final int VALUES_GRID_WIDTH = 200;
	private static final int VALUES_GRID_HEIGHT = 200;

	private ConfiguredSearchAttributePresenter.Handler handler;

	/* form elements */
	private DynamicForm form;

	private SelectItem attributeNameSelectItem;
	private SelectItem operationSelectItem;

	private SelectItem inputTypeSelectItem;

	private TextItem label;

	private DropdownValueListGrid grid;

	private IButton addImg;

	private VLayout gridLayout;

	private SaveCancelWindow window;

	/* model elements */
	private LinkedHashMap<String, String> attributeNameMap;
	private LinkedHashMap<ConfiguredSearchAttribute.Operation, String> operationMap;
	private LinkedHashMap<ConfiguredSearchAttribute.InputType, String> inputTypeMap;

	/**
	 * Construct a search configuration window.
	 *
	 */
	public ConfiguredSearchAttributeView() {
		layout();
	}

	/**
	 *
	 */
	private void layout() {
		// form //

		form = new DynamicForm();
		form.setAutoFocus(true); /* Set focus on first field */
		form.setWidth(FORM_WIDTH);
		form.setWrapItemTitles(false);

		label = new TextItem();
		label.setTitle(messages.searchAttributeWindowLabelLabel());
		label.setRequired(true);
		label.setWidth(FORMITEM_WIDTH);

		attributeNameSelectItem = new SelectItem();
		attributeNameSelectItem.setTitle(messages.searchAttributeWindowAttributeNameLabel());
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
		operationSelectItem.setTitle(messages.searchAttributeWindowOperationLabel());
		operationSelectItem.setWidth(FORMITEM_WIDTH);
		operationSelectItem.setRequired(true);
		operationSelectItem.setAddUnknownValues(false);

		inputTypeSelectItem = new SelectItem();
		inputTypeSelectItem.setName(FRM_INPUTTYPE);
		inputTypeSelectItem.setTitle(messages.searchAttributeWindowInputTypeLabel());
		inputTypeSelectItem.setWidth(FORMITEM_WIDTH);
		inputTypeSelectItem.setRequired(true);
		inputTypeSelectItem.setAddUnknownValues(false);
		inputTypeSelectItem.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent changedEvent) {
				handler.onChangeSelectInputType();
			}
		});

		form.setFields(label, attributeNameSelectItem, operationSelectItem,
				inputTypeSelectItem);

		VLayout gridLayout = new VLayout();
		gridLayout.setWidth(VALUES_GRID_WIDTH);
		gridLayout.setHeight(VALUES_GRID_HEIGHT);
		grid = new DropdownValueListGrid();
		gridLayout.addMember(grid);

		Layout addImgContainer = new Layout();
		addImgContainer.setWidth(64 + 16); //16 from scroller in grid
		addImgContainer.setAlign(Alignment.CENTER);
		addImgContainer.setHeight(25);
		addImgContainer.setLayoutAlign(Alignment.RIGHT);
		addImg = new IButton();

		addImg.setIcon(WidgetLayout.iconAdd);
		addImg.setTitle("");
		addImg.setHeight(22);
		addImg.setWidth(28);
		addImg.setIconAlign("center");

		addImg.setShowDown(false);
		addImg.setShowRollOver(false);
		addImg.setPrompt(messages.searchesAddSearchConfigButtonTooltip());
		addImg.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				handler.onAddDropDownValue();
			}
		});
		addImgContainer.addMember(addImg);
		gridLayout.addMember(addImgContainer);

		// layout structure //
		VLayout layout = new VLayout(10);
		layout.setAlign(Alignment.CENTER);
		layout.addMember(form);
		layout.addMember(gridLayout);

		window = new SaveCancelWindow(layout);
		window.setTitle(messages.searchAttributeWindowTitle());
		setInputTypeDropDown(false);
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
	public void setSelectedOperation(ConfiguredSearchAttribute.Operation operation) {
		setDropDownValue(operationSelectItem, operation);
	}

	@Override
	public void setSelectedInputType(ConfiguredSearchAttribute.InputType inputType) {
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
	public ConfiguredSearchAttribute.Operation getSelectedOperation() {
		return getSelectedKey(operationSelectItem, operationMap);
	}

	@Override
	public ConfiguredSearchAttribute.InputType getSelectedInputType() {
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
	public void setOperationMap(LinkedHashMap<ConfiguredSearchAttribute.Operation, String> operationMap) {
		this.operationMap = operationMap;
		if (operationMap == null) {
			operationSelectItem.clearValue();
		} else {
			operationSelectItem.setValueMap(operationMap);
		}
	}

	@Override
	public void setInputTypeMap(LinkedHashMap<ConfiguredSearchAttribute.InputType, String> inputTypeMap) {
		this.inputTypeMap = inputTypeMap;
		if (inputTypeMap == null) {
			inputTypeSelectItem.clearValue();
		} else {
			inputTypeSelectItem.setValueMap(inputTypeMap);
		}
	}

	@Override
	public void setFieldsEnabled(boolean enabled) {
		operationSelectItem.setCanEdit(enabled);
		inputTypeSelectItem.setCanEdit(enabled);
	}

	@Override
	public void setInputTypeDropDown(boolean inputDropDown) {
		grid.setVisible(inputDropDown);
		addImg.setVisible(inputDropDown);
		window.redraw();
	}

	@Override
	public List<String> getDropDownValues() {
		return grid.getDropDownValues();
	}

	@Override
	public void setHandler(ConfiguredSearchAttributePresenter.Handler handler) {
		window.setSaveHandler(handler);
		this.handler = handler;
	}

	@Override
	public void updateGrid(List<String> list) {
		grid.fillGrid(list);
	}

	@Override
	public void clearGrid() {
		grid.clear();
	}

	/**
	 * Used by {@link ConfiguredSearchAttributeView}.
	 *
	 * @author Jan Venstermans
	 *
	 */
	public class DropdownValueListGrid extends ListGrid {

		public static final String FLD_DROPDOWN_VALUE = "dropdownValue";

		private static final String FLD_DEL = "delete";

		public static final String FLD_OBJECT = "object";

		private static final String FLD_DROPDOWN_VALUE_WIDTH = "*";
		private static final int FLD_REMOVE_BUTTON_WIDTH = 50;

		public DropdownValueListGrid() {
			super();
			/*grid config*/
			setCanEdit(true);
			setEditEvent(ListGridEditEvent.CLICK);
			setEditByCell(true);

			setWidth100();
			setHeight100();
			setAlternateRecordStyles(true);
			setSelectionType(SelectionStyle.SINGLE);
			setShowRollOverCanvas(true);
			setShowAllRecords(true);
			setAlternateRecordStyles(true);
			setShowRecordComponents(true);
			setShowRecordComponentsByCell(true);

			/*columns*/
			ListGridField valueFld = new ListGridField(FLD_DROPDOWN_VALUE,
					messages.searchAttributeWindowGridValue());
			valueFld.setWidth(FLD_DROPDOWN_VALUE_WIDTH);
			valueFld.setCanEdit(true);
			valueFld.addCellSavedHandler(new CellSavedHandler() {
				@Override
				public void onCellSaved(CellSavedEvent cellSavedEvent) {
					handler.onChangeDropdownValues();
				}
			});

			ListGridField delete = new ListGridField(FLD_DEL, "");
			delete.setWidth(FLD_REMOVE_BUTTON_WIDTH);
			delete.setAlign(Alignment.CENTER);

			setFields(valueFld, delete);
		}

		public void fillGrid(List<String> dropdownValues) {
			deselectAllRecords();
			setData(new ListGridRecord[]{});
			// fill
			for (String value : dropdownValues) {
				addRow(value);
			}
		}

		public void addRow(String value) {
			ListGridRecord record = new ListGridRecord();
			record.setAttribute(FLD_DROPDOWN_VALUE, value);
			record.setAttribute(FLD_OBJECT, value);
			addData(record);
		}

		public List<String> getDropDownValues() {
			List<String> values = new ArrayList<String>();
			for (int i = 0 ; i < getDataAsRecordList().getLength() ; i++) {
				values.add(getRecord(i).getAttributeAsString("dropdownValue"));
			}
			return values;
		}

		@Override
		protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
			if (FLD_DEL.equals(grid.getFieldName(colNum))) {
				HLayout layout = new HLayout();
				layout.setHeight(16);
				layout.setWidth(1);

				ImgButton deleteImg = new ImgButton();
				deleteImg.setSrc(WidgetLayout.iconRemove);
				deleteImg.setShowDown(false);
				deleteImg.setShowRollOver(false);
				deleteImg.setPrompt(messages.searchAttributeWindowGridRemoveTooltip());
				deleteImg.setHeight(16);
				deleteImg.setWidth(16);
				deleteImg.addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent event) {
						handler.onRemove((String) record.getAttributeAsObject(FLD_DROPDOWN_VALUE));
					}
				});

				layout.addMember(deleteImg);

				return layout;
			}
			return super.createRecordComponent(record, colNum);
		}
	}
}
