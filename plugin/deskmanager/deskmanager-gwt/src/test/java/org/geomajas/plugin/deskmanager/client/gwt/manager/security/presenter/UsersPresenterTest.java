package org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter;

import com.google.gwt.core.client.Callback;
import org.geomajas.global.ExceptionDto;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.UserMockTest;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.Manager;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

/**
 * Test class for {@link UsersPresenter}.
 *
 * @author Jan Venstermans
 */
public class UsersPresenterTest extends UserMockTest {

	private UsersPresenter presenter;

	private UserDetailPresenter userDetaiPresenterMock = mock(UserDetailPresenter.class);

	private UserAssignPresenter userAssignPresenterMock = mock(UserAssignPresenter.class);

	@Before
	public void setUpPresenter() {
		Manager.getUserManagementPresenters().setUserDetailPresenter(userDetaiPresenterMock);
		Manager.getUserManagementPresenters().setUserAssignPresenter(userAssignPresenterMock);
		presenter = new UsersPresenter();
		reset(userDetaiPresenterMock);
		reset(userAssignPresenterMock);
		reset(usersView);
	}

	@Test
	public void constructorBindsHandlerTest() {
		presenter = new UsersPresenter(); // create again, because mocks have been reset
		verify(usersView).setHandler(presenter);
		verify(userDetaiPresenterMock).setObjectsTabHandler(presenter);
	}

	@Test
	public void setEnabledTrueTest() {
		presenter.setEnabled(true);
		verify(userDetaiPresenterMock).setEnabled(true);
		verify(userAssignPresenterMock).setEnabled(true);
	}

	@Test
	public void setEnabledFalseTest() {
		presenter.setEnabled(false);
		verify(userDetaiPresenterMock).setEnabled(false);
		verify(userAssignPresenterMock).setEnabled(false);
	}

	@Test
	public void loadAndShowTest() {
		presenter.loadAndShow();
		verify(usersView).setLoading(true);
		verify(userDetaiPresenterMock).setEnabled(false);
		verify(userAssignPresenterMock).setEnabled(false);
		verify(userService).getUsers(eq(false), Mockito.any(DataCallback.class));
		verify(userAssignPresenterMock).loadGroups();
	}

	@Test
	public void loadAllTest() {
		Callback<Boolean, ExceptionDto> callback = mock(Callback.class);
		ArgumentCaptor<DataCallback> data = ArgumentCaptor.forClass(DataCallback.class);
		List<UserDto> userListMock = mock(List.class);

		presenter.loadAll(callback);

		verify(usersView).setLoading(true);
		verify(userDetaiPresenterMock).setEnabled(false);
		verify(userAssignPresenterMock).setEnabled(false);

		verify(userService).getUsers(eq(false), data.capture());
		data.getValue().execute(userListMock);
		verify(usersView).setUsers(userListMock);
		verify(callback).onSuccess(true);

		verify(userAssignPresenterMock).loadGroups();
	}

	@Test
	public void onSelectTest() {
		UserDto userMock = mock(UserDto.class);
		presenter.onSelect(userMock);

		verify(usersView).selectUser(userMock);
		verify(userDetaiPresenterMock).loadObject(userMock);
		verify(userAssignPresenterMock).setSelectedObject(userMock);
	}

	@Test
	public void onDeleteTest() {
		UserDto userMock = mock(UserDto.class);
		ArgumentCaptor<DataCallback> data = ArgumentCaptor.forClass(DataCallback.class);

		presenter.onDelete(userMock);

		verify(userService).deleteUser(eq(userMock), data.capture());
	}

	@Test
	public void createObjectTest() {
		presenter.createObject();
		verify(userDetaiPresenterMock).createNewObject();
	}

}