package org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter;

import org.geomajas.plugin.deskmanager.client.gwt.manager.security.UserMockTest;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.Manager;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

/**
 * Test class for {@link UsersAndGroupsPresenter}.
 *
 * @author Jan Venstermans
 */
public class UsersAndGroupsPresenterTest extends UserMockTest {

	private UsersAndGroupsPresenter presenter;

	private UsersPresenter usersPresenterMock = mock(UsersPresenter.class);

	private GroupsPresenter groupsPresenterMock = mock(GroupsPresenter.class);

	private AdminAssignPresenter adminsPresenterMock = mock(AdminAssignPresenter.class);

	@Before
	public void setUpPresenter() {
		Manager.getUserManagementPresenters().setUsersPresenter(usersPresenterMock);
		Manager.getUserManagementPresenters().setGroupsPresenter(groupsPresenterMock);
		Manager.getUserManagementPresenters().setAdminAssignPresenter(adminsPresenterMock);
		presenter = new UsersAndGroupsPresenter();
		reset(usersPresenterMock);
		reset(groupsPresenterMock);
		reset(adminsPresenterMock);
	}

	@Test
	public void constructorBindsHandlerTest() {
		verify(usersAndGroupsView).setHandler(presenter);
	}

	@Test
	public void onMainTabSelectedGroupsTest() {
		presenter.onMainTabSelected(UsersAndGroupsHandler.MainTab.GROUPS);
		verify(groupsPresenterMock).setEnabled(true);
		verify(groupsPresenterMock).loadAndShow();

		verify(usersPresenterMock).setEnabled(false);
		verify(adminsPresenterMock).setEnabled(false);
	}

	@Test
	public void onMainTabSelectedUsersTest() {
		presenter.onMainTabSelected(UsersAndGroupsHandler.MainTab.USERS);
		verify(usersPresenterMock).setEnabled(true);
		verify(usersPresenterMock).loadAndShow();

		verify(groupsPresenterMock).setEnabled(false);
		verify(adminsPresenterMock).setEnabled(false);
	}

	@Test
	public void onMainTabSelectedAdminTest() {
		presenter.onMainTabSelected(UsersAndGroupsHandler.MainTab.ADMINS);
		verify(adminsPresenterMock).setEnabled(true);
		verify(adminsPresenterMock).loadAndShow();

		verify(groupsPresenterMock).setEnabled(false);
		verify(usersPresenterMock).setEnabled(false);
	}

	@Test
	public void onCreateObjectForTabGroupsTest() {
		presenter.onCreateObjectForTab(UsersAndGroupsHandler.MainTab.GROUPS);
		verify(groupsPresenterMock).createObject();
		verify(usersAndGroupsView).selectSubTab(UsersAndGroupsHandler.MainTab.GROUPS, ObjectsTabHandler.SubTab.DETAILS);
	}

	@Test
	public void onCreateObjectForTabUsersTest() {
		presenter.onCreateObjectForTab(UsersAndGroupsHandler.MainTab.USERS);
		verify(usersPresenterMock).createObject();
		verify(usersAndGroupsView).selectSubTab(UsersAndGroupsHandler.MainTab.USERS, ObjectsTabHandler.SubTab.DETAILS);
	}


}