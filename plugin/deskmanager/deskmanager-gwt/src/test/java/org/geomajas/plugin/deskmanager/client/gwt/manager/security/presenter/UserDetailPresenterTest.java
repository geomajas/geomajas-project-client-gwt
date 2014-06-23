package org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter;

import com.google.gwt.core.client.Callback;
import org.geomajas.geometry.service.WktException;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.UserMockTest;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.EditableLoadingView;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

/**
 * Test class for {@link UserDetailPresenter}.
 *
 * @author Jan Venstermans
 */
public class UserDetailPresenterTest extends UserMockTest {

	private static final String USER_NAME = "userName";
	private static final String USER_EMAIL = "a@a.aa";
	private static final String USER_SURNAME = "surname";
	private static final String USER_PASSWORD = "password";

	private UserDetailPresenter presenter;

	private UsersPresenter usersPresenterMock = mock(UsersPresenter.class);

	@Before
	public void setUpPresenter() {
		presenter = new UserDetailPresenter();
		presenter.setObjectsTabHandler(usersPresenterMock);
		reset(userDetailView);
	}

	@Test
	public void constructorBindsHandlerTest() {
		presenter = new UserDetailPresenter();
		presenter.setObjectsTabHandler(usersPresenterMock);
		verify(userDetailView).setHandler(presenter);
	}

	@Test
	public void onSaveNewUserTest() throws WktException {
		UserDto newUser = createUserDto();
		newUser.setId(-1);

		presenter.onSave(newUser, USER_PASSWORD);

		verifySetPresenterEditable(false);
		ArgumentCaptor<DataCallback> data = ArgumentCaptor.forClass(DataCallback.class);
		verify(userService).createUser(eq(newUser.getEmail()), eq(newUser.getName()), eq(newUser.getSurname()),
				eq(USER_PASSWORD), data.capture());
		UserDto userMock = mock(UserDto.class);
		data.getValue().execute(userMock);

		// internally: loadAllAndShowObject =>
		ArgumentCaptor<Callback> callback = ArgumentCaptor.forClass(Callback.class);
		verify(usersPresenterMock).loadAll(callback.capture());
		callback.getValue().onSuccess(true);
		verify(usersPresenterMock).onSelect(userMock);
	}

	@Test
	public void onSaveForUpdateUserTest() throws WktException {
		UserDto existingUser = createUserDto();

		presenter.onSave(existingUser, USER_PASSWORD);

		verifySetPresenterEditable(false);
		ArgumentCaptor<DataCallback> data = ArgumentCaptor.forClass(DataCallback.class);
		verify(userService).updateUser(eq(existingUser), eq(USER_PASSWORD),  data.capture());
		UserDto userMock = mock(UserDto.class);
		data.getValue().execute(userMock);

		// internally: loadAllAndShowObject =>
		ArgumentCaptor<Callback> callback = ArgumentCaptor.forClass(Callback.class);
		verify(usersPresenterMock).loadAll(callback.capture());
		callback.getValue().onSuccess(true);
		verify(usersPresenterMock).onSelect(userMock);
	}


	@Test
	public void createNewUserTest() {
		presenter.createNewObject();

		verify(userDetailView).clearValues();
		verify(userDetailView).setEnabled(true);
		verifySetPresenterEditable(true);
		verify(userDetailView).setCreateUserMode();
		verify(userDetailView).focusOnFirstField();
	}

	@Test
	public void loadNulUserTest() {
		presenter.loadObject(null);

		verify(userDetailView, atLeastOnce()).setEnabled(false);
		verify(userDetailView, never()).setEnabled(true);
		verify(userDetailView).clearValues();
		verifySetPresenterEditable(false);
	}

	@Test
	public void loadExistingUserTest() {
		UserDto userDto = createUserDto();

		presenter.loadObject(userDto);

		verify(userDetailView).setLoading();
		ArgumentCaptor<DataCallback> data = ArgumentCaptor.forClass(DataCallback.class);
		verify(userService).getUser(eq(1L), data.capture());
		data.getValue().execute(userDto);
		verify(userDetailView).setObject(userDto);
	}

	@Test
	public void setEnabledTrueTest() {
		presenter.setEnabled(true);
		verify(userDetailView).setEnabled(true);
	}

	@Test
	public void setEnabledFalseTest() {
		presenter.setEnabled(false);
		verify(userDetailView).setEnabled(false);
		verify(userDetailView).clearValues();
	}

	@Test
	public void setEditableTrueTest() {
		presenter.setEditable(true);
		verifySetPresenterEditable(true);
	}

	@Test
	public void setEditableFalseTest() {
		presenter.setEditable(false);
		verifySetPresenterEditable(false);
	}

	@Test
	public void onCancelTest() {
		UserDto user = createUserDto();
		// first set the user
		presenter.loadObject(user);
		verify(userDetailView).setLoading();
		ArgumentCaptor<DataCallback> data = ArgumentCaptor.forClass(DataCallback.class);
		verify(userService).getUser(eq(1l), data.capture());
		data.getValue().execute(user);
		reset(userDetailView);

		// part that is tested:
		presenter.onCancel();

		verifySetPresenterEditable(false);
		verify(userDetailView).clearValues();
		verify(userDetailView).setObject(user);
	}

	@Test
	public void onEditTest() {
		presenter.onEdit();

		verifySetPresenterEditable(true);
	}

	//----------------------------------------
	// private methods
	//----------------------------------------

	private UserDto createUserDto() {
		UserDto userDto = new UserDto();
		userDto.setName(USER_NAME);
		userDto.setEmail(USER_EMAIL);
		userDto.setSurname(USER_SURNAME);
		userDto.setId(1l);
		return userDto;
	}

	private void verifySetPresenterEditable(boolean editable) {
		verify(userDetailView).setButtonEnabled(EditableLoadingView.Button.EDIT, !editable);
		verify(userDetailView).setButtonEnabled(EditableLoadingView.Button.CANCEL, editable);
		verify(userDetailView).setButtonEnabled(EditableLoadingView.Button.SAVE, editable);
		verify(userDetailView).setEditable(editable);
	}
}