package org.geomajas.plugin.deskmanager.client.gwt.manager.security;

import com.google.gwtmockito.GwtMockitoTestRunner;
import junit.framework.Assert;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.model.GroupService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.model.UserService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.AdminAssignView;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.GroupDetailView;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.GroupsView;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.RoleSelectAssignView;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.UserDetailView;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.UserManagementViews;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.UsersAndGroupsView;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.UsersView;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.Manager;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

/**
 * Test class containing the mock objects for the views and services uses in User/Groups presenters.
 *
 * @author Jan Venstermans
 */
@RunWith(GwtMockitoTestRunner.class)
public class UserMockTest {

	/* services */
	@Mock
	protected UserService userService;

	@Mock
	protected GroupService groupService;

	/* views */
	@Mock
	protected UsersAndGroupsView usersAndGroupsView;

	@Mock
	protected GroupsView groupsView;

	@Mock
	protected UsersView usersView;

	@Mock
	protected AdminAssignView adminAssignView;

	@Mock
	protected UserDetailView userDetailView;

	@Mock
	protected GroupDetailView groupDetailView;

	@Mock
	protected RoleSelectAssignView<TerritoryDto, UserDto> groupAssignView;

	@Mock
	protected RoleSelectAssignView<UserDto, TerritoryDto> userAssignView;

	@Before
	public void setUp() {
		Manager.setUserService(userService);
		Manager.setGroupService(groupService);
		Manager.setUserManagementViews(new UserManagementViews() {

			@Override
			public UsersView getUsersView() {
				return usersView;
			}

			@Override
			public AdminAssignView getAdminsView() {
				return adminAssignView;
			}

			@Override
			public UserDetailView getUserDetailView() {
				return userDetailView;
			}

			@Override
			public RoleSelectAssignView<UserDto, TerritoryDto> getUserAssignView() {
				return userAssignView;
			}

			@Override
			public UsersAndGroupsView getUsersAndGroupsView() {
				return usersAndGroupsView;
			}

			@Override
			public GroupsView getGroupsView() {
				return groupsView;
			}

			@Override
			public GroupDetailView getGroupDetailView() {
				return groupDetailView;
			}

			@Override
			public RoleSelectAssignView getGroupAssignView() {
				return groupAssignView;
			}
		});
	}

	@Test
	public void mockManagerSetupTest() {
		// services
		Assert.assertEquals(groupService, Manager.getGroupService());
		Assert.assertEquals(userService, Manager.getUserService());
		// views
		Assert.assertEquals(usersAndGroupsView, Manager.getUsersManagementViews().getUsersAndGroupsView());
		Assert.assertEquals(groupAssignView, Manager.getUsersManagementViews().getGroupAssignView());
		Assert.assertEquals(groupDetailView, Manager.getUsersManagementViews().getGroupDetailView());
		Assert.assertEquals(groupsView, Manager.getUsersManagementViews().getGroupsView());
		Assert.assertEquals(userAssignView, Manager.getUsersManagementViews().getUserAssignView());
		Assert.assertEquals(userDetailView, Manager.getUsersManagementViews().getUserDetailView());
		Assert.assertEquals(usersView, Manager.getUsersManagementViews().getUsersView());
		Assert.assertEquals(adminAssignView, Manager.getUsersManagementViews().getAdminsView());
	}
}
