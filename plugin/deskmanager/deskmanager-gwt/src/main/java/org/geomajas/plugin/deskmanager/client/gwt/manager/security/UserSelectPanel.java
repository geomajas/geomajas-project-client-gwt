package org.geomajas.plugin.deskmanager.client.gwt.manager.security;

import org.geomajas.plugin.deskmanager.client.gwt.common.impl.DeskmanagerIcon;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.TransferImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

public class UserSelectPanel extends HLayout {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private UserListGrid sourceUsersGrid;

	private UserListGrid targetUsersGrid;

	public UserSelectPanel() {
		super(10);

		sourceUsersGrid = new UserListGrid(MESSAGES.userSelectAvailableUsers(), false);
		targetUsersGrid = new UserListGrid(MESSAGES.userSelectAssignedUsers(), true);
		targetUsersGrid.setEmptyMessage(MESSAGES.userSelectAssignedUsersTooltip());

		TransferImgButton add = new TransferImgButton(TransferImgButton.RIGHT);
		add.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				targetUsersGrid.transferSelectedData(sourceUsersGrid);
			}
		});

		TransferImgButton remove = new TransferImgButton(TransferImgButton.LEFT);
		remove.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				sourceUsersGrid.transferSelectedData(targetUsersGrid);
			}
		});

		Img help = new Img(DeskmanagerIcon.HELP_ICON, 24, 24);
		help.setTooltip(MESSAGES.userSelectAssignedUsersHelpText());
		help.setHoverWidth(350);
		help.setShowDisabled(false);
		help.setShowDown(false);

		VLayout buttons = new VLayout(10);
		buttons.addMember(add);
		buttons.addMember(remove);
		buttons.addMember(new LayoutSpacer());
		buttons.addMember(help);

		addMember(sourceUsersGrid);
		addMember(buttons);
		addMember(targetUsersGrid);
	}

	public void clearValues() {
		sourceUsersGrid.selectAllRecords();
		sourceUsersGrid.removeSelectedData();

		targetUsersGrid.selectAllRecords();
		targetUsersGrid.removeSelectedData();
	}

	public UserListGrid getSourceUsersGrid() {
		return sourceUsersGrid;
	}

	public UserListGrid getTargetUsersGrid() {
		return targetUsersGrid;
	}

	public class UserListGrid extends ListGrid {

		public static final String FLD_OBJECT = "object";

		public static final String FLD_NAME = "name";

		public UserListGrid(String title, boolean editable) {
			super();
			setWidth100();
			setHeight100();

			setCanReorderRecords(true);
			setDragDataAction(DragDataAction.MOVE);
			setCanAcceptDroppedRecords(true);
			setCanDragRecordsOut(true);
			setSelectionType(SelectionStyle.MULTIPLE);
			setShowAllRecords(true);

			ListGridField nameFld = new ListGridField(FLD_NAME);
			nameFld.setWidth("*");
			nameFld.setTitle(title);
			setFields(nameFld);

		}

		public void addRecord(UserDto user) {
			ListGridRecord record = new ListGridRecord();
			record.setAttribute(FLD_OBJECT, user);
			record.setAttribute(FLD_NAME, user.getEmail());
			addData(record);
		}
	}
}
