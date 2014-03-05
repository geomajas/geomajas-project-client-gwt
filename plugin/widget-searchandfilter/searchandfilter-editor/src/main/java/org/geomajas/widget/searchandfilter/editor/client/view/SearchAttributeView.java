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
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.ManagerCommandService;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.editor.client.configuration.SearchAttribute;
import org.geomajas.widget.searchandfilter.editor.client.presenter.SearchAttributePresenter;

import java.util.LinkedHashMap;

/**
 * Configuration window for individual searches.
 *
 * @author Jan Venstermans
 */
public class SearchAttributeView implements SearchAttributePresenter.View {

	private static final SearchAndFilterMessages MESSAGES =
			GWT.create(SearchAndFilterMessages.class);

	private static final int FORMITEM_WIDTH = 150;

	private SearchAttribute searchAttribute;

	private SearchAttributePresenter.Handler handler;

	private DynamicForm form;

	private SelectItem attributeName, operation, inputType;

	private TextItem label;

	private SaveCancelWindow window;

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

		// layout structure //
		VLayout layout = new VLayout(10);
		layout.addMember(form);

		window = new SaveCancelWindow(layout);
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

	@Override
	public void setSearchAttribute(SearchAttribute searchAttribute) {
		this.searchAttribute = searchAttribute;
		updateView();
	}

	@Override
	public void setHandler(SearchAttributePresenter.Handler handler) {
		this.handler = handler;
	}

	@Override
	public void updateStatus() {
		if (searchAttribute != null) {
			searchAttribute.setAttribute(attributeName.getValueAsString());
			searchAttribute.setLabel(label.getValueAsString());
			searchAttribute.setOperation(operation.getValueAsString());
			searchAttribute.setInputType(inputType.getValueAsString());
		}

	}

	@Override
	public void updateView() {
		if (searchAttribute != null) {
			form.clearValues();
			attributeName.setValue(searchAttribute.getAttribute());
			label.setValue(searchAttribute.getLabel());
			operation.setValue(searchAttribute.getOperation());
			inputType.setValue(searchAttribute.getInputType());
		}
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
	public SearchAttribute getSearchAttribute() {
		return searchAttribute;
	}

	@Override
	public void show(SearchAttribute searchAttribute) {
		setSearchAttribute(searchAttribute);
	  	window.show();
	}

	@Override
	public void hide() {
	   	window.hide();
	}
}
