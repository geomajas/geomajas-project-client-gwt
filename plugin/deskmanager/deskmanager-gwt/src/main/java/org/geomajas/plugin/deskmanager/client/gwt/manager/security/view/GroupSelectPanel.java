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
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.TransferImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.plugin.deskmanager.client.gwt.common.util.DeskmanagerLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;

import java.util.List;

/**
 * Panel for two groups of {@link org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto} objects.
 *
 * @author Jan De Moerloose
 */
public abstract class GroupSelectPanel extends HLayout implements SelectPanel<TerritoryDto> {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private GroupListGrid sourceGroupsGrid;

	private GroupListGrid targetGroupsGrid;

	public GroupSelectPanel() {
		super(10);

		sourceGroupsGrid = new GroupListGrid(MESSAGES.groupSelectAvailableGroups(), false);
		targetGroupsGrid = new GroupListGrid(MESSAGES.groupSelectAssignedGroups(), true);
		targetGroupsGrid.setEmptyMessage(MESSAGES.groupSelectAssignedGroupsTooltip());

		// pass dropped objects
		sourceGroupsGrid.addDropObjectsInSelectListGridHandler(
				new SelectListGrid.DropObjectsInSelectListGridHandler<TerritoryDto>() {
					@Override
					public void onObjectsDropped(List<TerritoryDto> objects) {
						onDroppedInSourceListGrid(objects);
				}
		});
		targetGroupsGrid.addDropObjectsInSelectListGridHandler(
				new SelectListGrid.DropObjectsInSelectListGridHandler<TerritoryDto>() {
					@Override
					public void onObjectsDropped(List<TerritoryDto> objects) {
						onDroppedInTargetListGrid(objects);
					}
				});

		TransferImgButton add = new TransferImgButton(TransferImgButton.RIGHT);
		add.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				onClickAddButton();
			}
		});

		TransferImgButton remove = new TransferImgButton(TransferImgButton.LEFT);
		remove.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				onClickRemoveButton();
			}
		});

		Img help = new Img(DeskmanagerLayout.iconHelp, 24, 24);
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

	protected void onClickAddButton() {
		targetGroupsGrid.transferSelectedData(sourceGroupsGrid);
	}

	protected void onClickRemoveButton() {
		sourceGroupsGrid.transferSelectedData(targetGroupsGrid);
	}

	@Override
	public SelectListGrid<TerritoryDto> getSourceGrid() {
		return sourceGroupsGrid;
	}

	@Override
	public SelectListGrid<TerritoryDto> getTargetGrid() {
		return targetGroupsGrid;
	}

	public void clearValues() {
		sourceGroupsGrid.selectAllRecords();
		sourceGroupsGrid.removeSelectedData();

		targetGroupsGrid.selectAllRecords();
		targetGroupsGrid.removeSelectedData();
	}

	@Override
	public Widget getWidget() {
		return this;
	}

	/**
	 * Extension of {@link com.smartgwt.client.widgets.grid.ListGrid} for
	 * {@link org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto} objects.
	 *
	 * @author Jan De Moerloose
	 * @author Jan Venstermans
	 */
	public class GroupListGrid extends SelectListGrid<TerritoryDto> {

		public GroupListGrid(String title, boolean editable) {
			super(title, editable);
		}

		@Override
		public String getObjectName(TerritoryDto group) {
			return group.getName();
		}
	}
}
