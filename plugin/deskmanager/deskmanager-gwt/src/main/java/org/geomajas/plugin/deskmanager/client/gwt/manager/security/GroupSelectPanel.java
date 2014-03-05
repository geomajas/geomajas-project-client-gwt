package org.geomajas.plugin.deskmanager.client.gwt.manager.security;

import org.geomajas.plugin.deskmanager.client.gwt.common.impl.DeskmanagerIcon;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;

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

public class GroupSelectPanel extends HLayout {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private GroupListGrid sourceGroupsGrid;

	private GroupListGrid targetGroupsGrid;

	public GroupSelectPanel() {
		super(10);

		sourceGroupsGrid = new GroupListGrid(MESSAGES.groupSelectAvailableGroups(), false);
		targetGroupsGrid = new GroupListGrid(MESSAGES.groupSelectAssignedGroups(), true);
		targetGroupsGrid.setEmptyMessage(MESSAGES.groupSelectAssignedGroupsTooltip());

		TransferImgButton add = new TransferImgButton(TransferImgButton.RIGHT);
		add.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				targetGroupsGrid.transferSelectedData(sourceGroupsGrid);
			}
		});

		TransferImgButton remove = new TransferImgButton(TransferImgButton.LEFT);
		remove.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				sourceGroupsGrid.transferSelectedData(targetGroupsGrid);
			}
		});

		Img help = new Img(DeskmanagerIcon.HELP_ICON, 24, 24);
		help.setTooltip(MESSAGES.groupSelectAssignedGroupsHelpText());
		help.setHoverWidth(350);
		help.setShowDisabled(false);
		help.setShowDown(false);

		VLayout buttons = new VLayout(10);
		buttons.addMember(add);
		buttons.addMember(remove);
		buttons.addMember(new LayoutSpacer());
		buttons.addMember(help);

		addMember(sourceGroupsGrid);
		addMember(buttons);
		addMember(targetGroupsGrid);
	}

	public void clearValues() {
		sourceGroupsGrid.selectAllRecords();
		sourceGroupsGrid.removeSelectedData();

		targetGroupsGrid.selectAllRecords();
		targetGroupsGrid.removeSelectedData();
	}

	public GroupListGrid getSourceGroupsGrid() {
		return sourceGroupsGrid;
	}

	public GroupListGrid getTargetGroupsGrid() {
		return targetGroupsGrid;
	}

	public class GroupListGrid extends ListGrid {

		public static final String FLD_OBJECT = "object";

		public static final String FLD_NAME = "name";

		public GroupListGrid(String title, boolean editable) {
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

		public void addRecord(TerritoryDto group) {
			ListGridRecord record = new ListGridRecord();
			record.setAttribute(FLD_OBJECT, group);
			record.setAttribute(FLD_NAME, group.getName());
			addData(record);
		}
	}
}
