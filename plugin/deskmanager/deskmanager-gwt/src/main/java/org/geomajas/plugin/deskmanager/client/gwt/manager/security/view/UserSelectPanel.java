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

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.TransferImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.plugin.deskmanager.client.gwt.common.util.DeskmanagerLayout;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

import java.util.List;

/**
 * @author Jan De Moerloose
 *
 */
public abstract class UserSelectPanel extends HLayout implements SelectPanel<UserDto> {

	private UserListGrid sourceUsersGrid;

	private UserListGrid targetUsersGrid;

	private Img help;

	public UserSelectPanel() {
		super(10);

		sourceUsersGrid = new UserListGrid(getSourceUserGridTitle(), false);
		targetUsersGrid = new UserListGrid(getTargetUserGridTitle(), true);
		targetUsersGrid.setEmptyMessage(getTargetUserGridEmptyMessage());

		// pass dropped objects
		sourceUsersGrid.addDropObjectsInSelectListGridHandler(
				new SelectListGrid.DropObjectsInSelectListGridHandler<UserDto>() {
					@Override
					public void onObjectsDropped(List<UserDto> objects) {
						onDroppedInSourceListGrid(objects);
					}
				});
		targetUsersGrid.addDropObjectsInSelectListGridHandler(
				new SelectListGrid.DropObjectsInSelectListGridHandler<UserDto>() {
					@Override
					public void onObjectsDropped(List<UserDto> objects) {
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

		help = new Img(DeskmanagerLayout.iconHelp, 24, 24);
		help.setTooltip(getHelpButtonTooltip());
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

	/* force customize messages*/
	protected abstract String getHelpButtonTooltip();

	protected abstract String getSourceUserGridTitle();

	protected abstract String getTargetUserGridTitle() ;

	protected abstract String getTargetUserGridEmptyMessage();

	protected void onClickAddButton() {
		targetUsersGrid.transferSelectedData(sourceUsersGrid);
	}

	protected void onClickRemoveButton() {
		sourceUsersGrid.transferSelectedData(targetUsersGrid);
	}

	public void clearValues() {
		sourceUsersGrid.selectAllRecords();
		sourceUsersGrid.removeSelectedData();

		targetUsersGrid.selectAllRecords();
		targetUsersGrid.removeSelectedData();
	}

	public UserListGrid getSourceGrid() {
		return sourceUsersGrid;
	}

	public UserListGrid getTargetGrid() {
		return targetUsersGrid;
	}

	@Override
	public Widget getWidget() {
		return this;
	}

	/**
	 * @author Jan De Moerloose
	 * @author Jan Venstermans
	 */
	public class UserListGrid extends SelectListGrid<UserDto> {

		public UserListGrid(String title, boolean editable) {
			super(title, editable);
		}

		@Override
		public String getObjectName(UserDto userDto) {
			return userDto.getEmail();
		}
	}
}
