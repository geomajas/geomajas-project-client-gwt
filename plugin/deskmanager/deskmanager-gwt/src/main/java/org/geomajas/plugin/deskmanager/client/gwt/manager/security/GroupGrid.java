package org.geomajas.plugin.deskmanager.client.gwt.manager.security;

import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

public class GroupGrid extends ListGrid {
	
	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	public static final String FLD_ID = "id";
	
	public static final String FLD_NAME = "name";
	
	public static final String FLD_KEY = "key";
	
	public static final String FLD_TERRITORY = "territory";
	
	public static final String FLD_ACTIONS = "actions";
	
	public GroupGrid() {
		super();
		setWidth100();
		setHeight100();
		setAlternateRecordStyles(true);
		setSelectionType(SelectionStyle.SINGLE);
		setShowRollOverCanvas(true);
		setShowAllRecords(true);

		// -- Fields --------------------------------------------------------

		ListGridField name = new ListGridField(FLD_NAME, MESSAGES.securityGroupGridColumnName());
		name.setWidth("45%");
		name.setType(ListGridFieldType.TEXT);
		
		ListGridField key = new ListGridField(FLD_KEY, MESSAGES.securityGroupGridColumnKey());
		key.setWidth("15%");
		key.setType(ListGridFieldType.TEXT);

		ListGridField territory = new ListGridField(FLD_TERRITORY, MESSAGES.securityGroupGridColumnTerritory());
		territory.setType(ListGridFieldType.ICON);
		territory.setWidth("40%");
		territory.setPrompt(MESSAGES.securityGroupGridColumnTerritoryTooltip());

		
		ListGridField actions = new ListGridField(FLD_ACTIONS, MESSAGES.securityGroupGridColumnActions());
		actions.setType(ListGridFieldType.ICON);
		actions.setWidth(100);
		actions.setCanEdit(false);
		actions.setPrompt(MESSAGES.securityGroupGridColumnActionsTooltip());
		
		setFields(name, key, territory, actions);
		// initially sort on blueprint name
		setSortField(0);
		setSortDirection(SortDirection.ASCENDING);

		// ----------------------------------------------------------

//		Whiteboard.registerHandler(this);
	}
	

}
