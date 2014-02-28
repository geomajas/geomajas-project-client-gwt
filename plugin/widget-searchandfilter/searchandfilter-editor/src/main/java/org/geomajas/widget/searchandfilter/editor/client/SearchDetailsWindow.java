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
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.editor.client.configuration.SearchConfig;

/**
 * Configuration window for individual searches.
 *
 * @author Jan Venstermans
 */
public class SearchDetailsWindow extends Window {

	private static final SearchAndFilterMessages MESSAGES =
			GWT.create(SearchAndFilterMessages.class);

	private static final int FORMITEM_WIDTH = 300;

	public static final String FLD_NAME = "Name";

	private SearchConfig searchConfig;

	private BooleanCallback callback;

	private DynamicForm form;

	private TextItem title, description, titleInWindow, iconUrl;

	private CheckboxItem publicLayer;

	private CheckboxItem defaultVisible;

	private TextItem minScale;

	private TextItem maxScale;

	/**
	 * Construct a search configuration window.
	 *
	 * @param searchConfig
	 * @param callback returns true if saved, false if canceled.
	 */
	public SearchDetailsWindow(SearchConfig searchConfig, BooleanCallback callback) {
		this.searchConfig = searchConfig;
		this.callback = callback;

		setAutoSize(true);
		setCanDragReposition(true);
		setCanDragResize(false);
		setKeepInParentRect(true);
		setOverflow(Overflow.HIDDEN);
		setAutoCenter(true);
		setTitle(MESSAGES.searchDetailsWindowTitle());
		setShowCloseButton(false);
		setShowMinimizeButton(false);
		setShowMaximizeButton(false);
		setIsModal(true);
		setShowModalMask(true);

		// form //

		form = new DynamicForm();
		form.setAutoFocus(true); /* Set focus on first field */
		form.setWidth(FORMITEM_WIDTH + 100);

		title = new TextItem(FLD_NAME);
		title.setTitle(MESSAGES.searchDetailsWindowFieldTitleLabel());
		title.setRequired(true);
		title.setWidth(FORMITEM_WIDTH);
		title.setWrapTitle(false);
		title.setTooltip(MESSAGES.searchDetailsWindowFieldTitleTooltip());

		description = new TextItem();
		description.setTitle(MESSAGES.searchDetailsWindowFieldDescriptionLabel());
		description.setRequired(true);
		description.setWidth(FORMITEM_WIDTH);
		description.setWrapTitle(false);
		description.setTooltip(MESSAGES.searchDetailsWindowFieldDescriptionTooltip());

		titleInWindow = new TextItem();
		titleInWindow.setTitle(MESSAGES.searchDetailsWindowFieldTitleInWindowLabel());
		titleInWindow.setRequired(true);
		titleInWindow.setWidth(FORMITEM_WIDTH);
		titleInWindow.setWrapTitle(false);
		titleInWindow.setTooltip(MESSAGES.searchDetailsWindowFieldTitleInWindowTooltip());

		iconUrl = new TextItem();
		iconUrl.setTitle(MESSAGES.searchDetailsWindowFieldIconUrlLabel());
		iconUrl.setRequired(false);
		iconUrl.setWidth(FORMITEM_WIDTH);
		iconUrl.setWrapTitle(false);
		iconUrl.setTooltip(MESSAGES.searchDetailsWindowFieldIconUrlTooltip());

		form.setFields(title, description, titleInWindow, iconUrl);

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
		layout.setMargin(10);
		layout.addMember(form);
		layout.addMember(buttons);
		addItem(layout);

		show();
	}

	public void show() {
		form.clearValues();
		title.setValue(searchConfig.getTitle());
		description.setValue(searchConfig.getDescription());
		titleInWindow.setValue(searchConfig.getTitleInWindow());
		iconUrl.setValue(searchConfig.getIconUrl());
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
			hide();
			destroy();
			if (callback != null) {
				callback.execute(true);
			}
		}
	}
}
