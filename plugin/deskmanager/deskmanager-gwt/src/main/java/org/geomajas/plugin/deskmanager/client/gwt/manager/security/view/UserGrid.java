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
package org.geomajas.plugin.deskmanager.client.gwt.manager.security.view;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter.ObjectsTabHandler;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

import java.util.List;

/**
 * @author Jan De Moerloose
 */
public class UserGrid extends ListGrid implements UsersView {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	public static final String FLD_ID = "id";

	public static final String FLD_OBJECT = "object";

	public static final String FLD_EMAIL = "email";

	public static final String FLD_NAME = "name";

	public static final String FLD_SURNAME = "surname";

	public static final String FLD_ACTIVE = "active";

	public static final String FLD_ACTIONS = "actions";
	public static final int FLD_ACTIONS_WIDTH = 100;

	private ObjectsTabHandler handler;

	private ListGridRecord rollOverRecord;

	private HLayout rollOverCanvas;

	public UserGrid() {
		super();
		setWidth100();
		setHeight100();
		setAlternateRecordStyles(true);
		setSelectionType(SelectionStyle.SINGLE);
		setShowRollOverCanvas(true);
		setShowAllRecords(true);

		// -- Fields --------------------------------------------------------

		ListGridField email = new ListGridField(FLD_EMAIL, MESSAGES.securityUserGridColumnEmail());
		email.setWidth("40%");
		email.setType(ListGridFieldType.TEXT);

		ListGridField name = new ListGridField(FLD_NAME, MESSAGES.securityUserGridColumnName());
		name.setWidth("15%");
		name.setType(ListGridFieldType.TEXT);

		ListGridField surname = new ListGridField(FLD_SURNAME, MESSAGES.securityUserGridColumnSurname());
		surname.setWidth("15%");
		surname.setType(ListGridFieldType.TEXT);

		ListGridField active = new ListGridField(FLD_ACTIVE, MESSAGES.securityUserGridColumnActiv());
		active.setType(ListGridFieldType.BOOLEAN);
		active.setWidth("15%");
		active.setPrompt(MESSAGES.securityUserGridActivTooltip());

		ListGridField actions = new ListGridField(FLD_ACTIONS, MESSAGES.securityUserGridColumnActions());
		actions.setType(ListGridFieldType.ICON);
		actions.setWidth(FLD_ACTIONS_WIDTH);
		actions.setCanEdit(false);
		actions.setPrompt(MESSAGES.securityUserGridColumnActionsTooltip());

		setFields(email, name, surname, active, actions);
		// initially sort on blueprint name
		setSortField(0);
		setSortDirection(SortDirection.ASCENDING);

		// ----------------------------------------------------------
		// Whiteboard.registerHandler(this);
	}

	@Override
	public void setHandler(final ObjectsTabHandler handler) {
		this.handler = handler;
		bind();
	}

	private void bind() {
		addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				if (event.getState()) { // true == selected
					ListGridRecord record = (ListGridRecord) event.getRecord();
					if (record != null && record.getAttributeAsObject(FLD_OBJECT) != null) {
						handler.onSelect((UserDto) record.getAttributeAsObject(FLD_OBJECT));
					} else {
						handler.onSelect(null);
					}
				}
			}
		});
	}

	@Override
	public void setUsers(List<UserDto> users) {
		clearData();
		for (UserDto user : users) {
			ListGridRecord record = toGridRecord(user);
			addData(record);
		}
		setShowEmptyMessage(false);
		redraw();
	}

	@Override
	public void selectUser(UserDto user) {
		selectRecord(getRecordList().find(FLD_ID, user.getId()));
	}

	@Override
	public void setLoading(boolean loading) {
		clearData();

		setShowEmptyMessage(true);
		setEmptyMessage("<i>" + MESSAGES.usersLoading() + " <img src='" + Geomajas.getIsomorphicDir()
				+ "/images/circle.gif' style='height: 1em' /></i>");
		redraw();
	}

	@Override
	public void deleteFailed() {
		SC.warn(MESSAGES.deleteUserFailedWarnMessage());
	}

	void clearData() {
		deselectAllRecords();
		setData(new ListGridRecord[] {});
	}

	private ListGridRecord toGridRecord(UserDto user) {
		ListGridRecord record = new ListGridRecord();
		record.setAttribute(FLD_ID, user.getId());
		record.setAttribute(FLD_NAME, user.getName());
		record.setAttribute(FLD_EMAIL, user.getEmail());
		record.setAttribute(FLD_SURNAME, user.getSurname());
		record.setAttribute(FLD_ACTIVE, user.isActive());
		record.setAttribute(FLD_OBJECT, user);
		return record;
	}

	@Override
	protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
		rollOverRecord = this.getRecord(rowNum);

		if (rollOverCanvas == null) {
			rollOverCanvas = new HLayout(3);
			rollOverCanvas.setSnapTo("TR");
			rollOverCanvas.setWidth(FLD_ACTIONS_WIDTH);
			rollOverCanvas.setHeight(22);

			ImgButton deleteImg = new ImgButton();
			deleteImg.setShowDown(false);
			deleteImg.setShowRollOver(false);
			deleteImg.setLayoutAlign(Alignment.CENTER);
			deleteImg.setSrc(WidgetLayout.iconRemove);
			deleteImg.setPrompt(MESSAGES.userGridActionsColumnRemoveTooltip());
			deleteImg.setHeight(16);
			deleteImg.setWidth(16);
			deleteImg.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					final UserDto model = (UserDto) rollOverRecord.getAttributeAsObject(FLD_OBJECT);
					SC.ask(MESSAGES.removeTitle(), MESSAGES.usersGridRemoveConfirmQuestion(model.getEmail()),
							new BooleanCallback() {

						public void execute(Boolean value) {
							if (value) {
								handler.onDelete(model);
							}
						}
					} );
				}
			});
			rollOverCanvas.addMember(new LayoutSpacer());
			rollOverCanvas.addMember(deleteImg);
			rollOverCanvas.addMember(new LayoutSpacer());
		}
		return rollOverCanvas;
	}
}
