package org.geomajas.plugin.deskmanager.client.gwt.manager.security;

import java.awt.CardLayout;

import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.common.GdmLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

/**
 * Frontend presentation of a selectable list of users/groups, and a panel that contains configuration options of the
 * selected user or group.
 * 
 * @author Jan De Moerloose
 */
public class UsersAndGroups extends VLayout {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private IButton userButtonNew;

	private IButton groupButtonNew;

	private TabSet gridTabSet;

	private TabSet userDetailTabSet;

	private TabSet groupDetailTabSet;

	private UserGrid userGrid;

	private UserAssign userAssign;

	private UserDetail userDetail;

	private GroupGrid groupGrid;

	private GroupDetail groupDetail;

	private GroupAssign groupAssign;

	private static final int MARGIN = 20;

	public UsersAndGroups() {
		super(MARGIN);

		//--------- Top part
		VLayout topContainer = new VLayout(5);
		topContainer.setShowResizeBar(true);
		topContainer.setMinHeight(200);
		topContainer.setHeight("60%");
		topContainer.setLayoutBottomMargin(5);

		userButtonNew = new IButton(MESSAGES.securityNewUserButtonText());
		userButtonNew.setWidth(userButtonNew.getTitle().length() * GdmLayout.buttonFontWidth + GdmLayout.buttonOffset);
		userButtonNew.setIcon(WidgetLayout.iconAdd);
		userButtonNew.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				createUser();
			}

		});
		
		groupButtonNew = new IButton(MESSAGES.securityNewGroupButtonText());
		groupButtonNew.setWidth(userButtonNew.getTitle().length() * GdmLayout.buttonFontWidth + GdmLayout.buttonOffset);
		groupButtonNew.setIcon(WidgetLayout.iconAdd);
		groupButtonNew.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				createGroup();
			}
		});

		userGrid = new UserGrid();
		groupGrid = new GroupGrid();

		VLayout userLayout = new VLayout();
		userLayout.addMember(userGrid);
		userLayout.addMember(userButtonNew);
		Tab userTab = new Tab(MESSAGES.securityUsersTab());
		userTab.setPane(userLayout);

		VLayout groupLayout = new VLayout();
		groupLayout.addMember(groupGrid);
		groupLayout.addMember(groupButtonNew);
		Tab groupTab = new Tab(MESSAGES.securityGroupsTab());
		groupTab.setPane(groupLayout);

		gridTabSet = new TabSet();
		gridTabSet.addTab(userTab);
		gridTabSet.addTab(groupTab);
		gridTabSet.setTabBarPosition(Side.LEFT);
		gridTabSet.setTabBarThickness(70);
		gridTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				if(event.getTabNum() == 0) {
					showUserDetailTabSet();
				} else {
					showGroupDetailTabSet();
				}
				
			}

		});

		topContainer.addMember(gridTabSet);

		// Bottom part-------------------		
		userDetail = new UserDetail();
		Tab userDetailTab = new Tab(MESSAGES.securityUserDetailTab());
		userDetailTab.setPane(userDetail);
		
		userAssign = new UserAssign();
		Tab userAssignTab = new Tab(MESSAGES.securityUserAssignTab());
		userAssignTab.setPane(userAssign);
		
		userDetailTabSet = new TabSet();
		userDetailTabSet.addTab(userDetailTab);
		userDetailTabSet.addTab(userAssignTab);
		
		groupDetail = new GroupDetail();
		Tab groupDetailTab = new Tab(MESSAGES.securityGroupDetailTab());
		groupDetailTab.setPane(groupDetail);

		groupAssign = new GroupAssign();
		Tab groupAssignTab = new Tab(MESSAGES.securityGroupAssignTab());
		groupAssignTab.setPane(groupAssign);
		
		groupDetailTabSet = new TabSet();
		groupDetailTabSet.addTab(groupDetailTab);
		groupDetailTabSet.addTab(groupAssignTab);

		HLayout detailContainer = new HLayout();
		detailContainer.setMinHeight(200);
		detailContainer.setHeight("40%");
		detailContainer.setLayoutTopMargin(5);
		detailContainer.addMember(userDetailTabSet);
		detailContainer.addMember(groupDetailTabSet);

		addMember(topContainer);
		addMember(detailContainer);

		// Whiteboard.registerHandler(this);
	}

	private void showGroupDetailTabSet() {
		groupDetailTabSet.show();
		userDetailTabSet.hide();
	}

	private void showUserDetailTabSet() {
		groupDetailTabSet.hide();
		userDetailTabSet.show();
	}
	
	public UserAssign getUserAssign() {
		return userAssign;
	}
	
	public void setUserAssign(UserAssign userAssign) {
		this.userAssign = userAssign;
	}

	private void createUser() {
		// Window w = new ChooseUserAppNameWindow(new DataCallback<HashMap<String, Object>>() {
		//
		// public void execute(HashMap<String, Object> result) {
		// if (result != null && result.containsKey("userApplication") && result.containsKey("public")
		// && result.containsKey("name")) {
		// //TODO: i18n
		// ManagerCommandService.createNewBlueprint(result.get("userApplication").toString(),
		// (Boolean) result.get("public"), result.get("name").toString());
		// }
		// }
		// });
		// w.show();
	}

	
	private void createGroup() {
		// Window w = new ChooseUserAppNameWindow(new DataCallback<HashMap<String, Object>>() {
		//
		// public void execute(HashMap<String, Object> result) {
		// if (result != null && result.containsKey("userApplication") && result.containsKey("public")
		// && result.containsKey("name")) {
		// //TODO: i18n
		// ManagerCommandService.createNewBlueprint(result.get("userApplication").toString(),
		// (Boolean) result.get("public"), result.get("name").toString());
		// }
		// }
		// });
		// w.show();
	}


}
