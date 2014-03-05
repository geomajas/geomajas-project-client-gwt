package org.geomajas.plugin.deskmanager.client.gwt.manager.security;

import org.geomajas.plugin.deskmanager.client.gwt.manager.events.Whiteboard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;


public class UserGrid  extends ListGrid {
	
	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	public static final String FLD_ID = "id";
	
	public static final String FLD_EMAIL = "email";
	
	public static final String FLD_NAME = "name";
	
	public static final String FLD_SURNAME = "surname";
	
	public static final String FLD_ACTIVE = "active";
	
	public static final String FLD_ACTIONS = "actions";

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
		actions.setWidth(100);
		actions.setCanEdit(false);
		actions.setPrompt(MESSAGES.securityUserGridColumnActionsTooltip());
		
		setFields(email, name, surname, active, actions);
		// initially sort on blueprint name
		setSortField(0);
		setSortDirection(SortDirection.ASCENDING);

		// ----------------------------------------------------------

//		Whiteboard.registerHandler(this);
	}

	
}
