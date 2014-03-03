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
package org.geomajas.widget.searchandfilter.editor.client;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.common.FileUploadForm;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.ManagerCommandService;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.editor.client.configuration.SearchAttribute;
import org.geomajas.widget.searchandfilter.editor.client.configuration.SearchConfig;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Configuration window for individual searches.
 *
 * @author Jan Venstermans
 */
public class SearchAttributeWindow extends Window {

	private static final SearchAndFilterMessages MESSAGES =
			GWT.create(SearchAndFilterMessages.class);

	private LayerModelDto layerModelDto;

	private static final int FORMITEM_WIDTH = 150;

	private SearchAttribute attribute;

	private BooleanCallback callback;

	private DynamicForm form;

	private SelectItem attributeName, operation, inputType;

	private TextItem label;

	/**
	 * Construct a search configuration window.
	 *
	 * @param attribute
	 * @param callback returns true if saved, false if canceled.
	 */
	public SearchAttributeWindow(final LayerModelDto layerModelDto,
								 final SearchAttribute attribute, BooleanCallback callback) {
		this.layerModelDto = layerModelDto;
		this.attribute = attribute;
		this.callback = callback;
		if (layerModelDto.getLayerType().equals("Vector") && layerModelDto.getLayerConfiguration() == null) {
			ManagerCommandService.getLayerModel(id, new DataCallback<LayerModelDto>() {

				public void execute(LayerModelDto result) {
					SearchAttributeWindow.this.layerModelDto = result;
					layout();
				}
			});
		} else {
			layout();
		}
	}

	/**
	 *
	 */
	private void layout() {
		setAutoSize(true);
		setCanDragReposition(true);
		setCanDragResize(false);
		setKeepInParentRect(true);
		setOverflow(Overflow.HIDDEN);
		setAutoCenter(true);
		setTitle("Search attribute");
		setShowCloseButton(false);
		setShowMinimizeButton(false);
		setShowMaximizeButton(false);
		setIsModal(true);
		setShowModalMask(true);

		// form //

		form = new DynamicForm();
		form.setAutoFocus(true); /* Set focus on first field */
		form.setWidth(FORMITEM_WIDTH + 100);
		form.setWrapItemTitles(false);


		attributeName = new SelectItem();
//		attributeName.setOptionDataSource(getDummyDataSource());
		attributeName.setTitle("Attribute:");
		attributeName.setWidth(FORMITEM_WIDTH);
		attributeName.setValueMap(getDummyLinkedHashMap());

		operation = new SelectItem();
		operation.setOptionDataSource(getDummyDataSource());
		operation.setTitle("Operation:");
		operation.setWidth(FORMITEM_WIDTH);

		inputType = new SelectItem();
		inputType.setOptionDataSource(getDummyDataSource());
		inputType.setTitle("Input type:");
		inputType.setWidth(FORMITEM_WIDTH);

		label = new TextItem();
		label.setTitle("Label");
		label.setRequired(true);
		label.setWidth(FORMITEM_WIDTH);

		form.setFields(attributeName, label, operation, inputType);

		// buttons //

		HLayout buttons = new HLayout(10);
		IButton save = new IButton(MESSAGES.saveButtonText());
		save.setIcon(WidgetLayout.iconSave);
		save.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				saved();
			}
		});
		IButton cancel = new IButton(MESSAGES.cancelButtonText());
		cancel.setIcon(WidgetLayout.iconCancel);
		cancel.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				cancelled();
			}
		});
		buttons.addMember(save);
		buttons.addMember(cancel);

		// layout structure //
		VLayout layout = new VLayout(10);
		layout.addMember(form);
		layout.addMember(buttons);
		addItem(layout);
	}

	public void show() {
		form.clearValues();
		attributeName.setValue(attribute.getAttribute());
		label.setValue(attribute.getLabel());
		operation.setValue(attribute.getOperation());
		inputType.setValue(attribute.getInputType());
		super.show();
	}

	private void cancelled() {
		hide();
		destroy();
		if (callback != null) {
			callback.execute(false);
		}
	}

	private void saved() {
		if (form.validate()) {
			attribute.setAttribute(attributeName.getValueAsString());
			attribute.setLabel(label.getValueAsString());
			attribute.setOperation(operation.getValueAsString());
			attribute.setInputType(inputType.getValueAsString());
			hide();
			destroy();
			if (callback != null) {
				callback.execute(true);
			}
		}
	}

	private DataSource getDummyDataSource() {
		String FIELD_ID = "id";
		String FIELD_LABEL = "value";
		DataSource dataSource = new DataSource();
		dataSource.setClientOnly(true);
		DataSourceField label = new DataSourceIntegerField(FIELD_ID);
		DataSourceField regex = new DataSourceTextField("FIELD_LABEL");
		dataSource.setFields(label, regex);

		Record record;
		record = new Record();
		record.setAttribute(FIELD_ID, 0);
		record.setAttribute(FIELD_LABEL, ".*");
		dataSource.addData(record);
		record = new Record();
		record.setAttribute(FIELD_ID, 1);
		record.setAttribute(FIELD_LABEL, "yahoo");
		dataSource.addData(record);
		record = new Record();
		record.setAttribute(FIELD_ID, 2);
		record.setAttribute(FIELD_LABEL, "geonames");
		dataSource.addData(record);
		record = new Record();
		record.setAttribute(FIELD_ID, 3);
		record.setAttribute(FIELD_LABEL, "offline");
		dataSource.addData(record);

		return dataSource;
	}

	private LinkedHashMap<String, String> getDummyLinkedHashMap() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("one", "oneone");
		valueMap.put("two", "twotwo");
		valueMap.put("three", "threethree");
		return valueMap;
	}
}
