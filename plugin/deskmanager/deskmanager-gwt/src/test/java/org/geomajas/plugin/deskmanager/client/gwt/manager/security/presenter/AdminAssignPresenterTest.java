package org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter;

import junit.framework.Assert;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.UserMockTest;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.EditableLoadingView;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

/**
 * Test class for {@link AdminAssignPresenter}.
 *
 * @author Jan Venstermans
 */
public class AdminAssignPresenterTest extends UserMockTest {

	private AdminAssignPresenter presenter;

	private List<UserDto> usersDtoList;

	private UserDto user1;
	private UserDto user2;

	private ArgumentCaptor<List> listAssigned = ArgumentCaptor.forClass(List.class);
	private ArgumentCaptor<List> listUnassigned = ArgumentCaptor.forClass(List.class);

	@Before
	public void setUpPresenter() {
		presenter = new AdminAssignPresenter();
		createDtoObjects();
		reset(adminAssignView);
	}

	@Test
	public void constructorBindsHandlerTest() {
		presenter = new AdminAssignPresenter();
		verify(adminAssignView).setHandler(presenter);
	}

	@Test
	public void loadAndShowLoadsUsers() {
		injectUsersInPresenter(usersDtoList);
		verify(adminAssignView).setEnabled(true);
		verify(adminAssignView).clearValues();
		verify(adminAssignView).setAdminUsers(Mockito.anyList(), Mockito.anyList());
	}

	@Test
	public void loadAndShowLoadsUserAdminsToView() {
		injectUsersInPresenter(usersDtoList);

		// at start: user2 is administrator, user1 is not

		verificationOneUserAssignedOneUnassigned();
	}

	@Test
	public void onSaveWhenNothingChangedTest() {
		injectUsersInPresenter(usersDtoList);

		presenter.onSave();

		verifySetPresenterEditable(false);
		verifyNeverUpdateAdminsToUserService();
	}

	@Test
	public void onAddAdminChangesAssignedMaps() {
		injectUsersInPresenter(usersDtoList);
		reset(adminAssignView);

		// at start: user2 is administrator, user1 is not
		presenter.onAddAdminRole(Arrays.asList(user1));

		verificationAllUsersAssigned();
	}

	@Test
	public void onSaveAfterAssignTest() {
		injectUsersInPresenter(usersDtoList);
		// at start: user2 is administrator, user1 is not
		presenter.onAddAdminRole(Arrays.asList(user1));
		reset(userService);

		presenter.onSave();

		ArgumentCaptor<List> listAdded = ArgumentCaptor.forClass(List.class);
		ArgumentCaptor<List> listRemoved = ArgumentCaptor.forClass(List.class);
		verify(userService).updateAdmins(listAdded.capture(), listRemoved.capture(),
				Mockito.any(DataCallback.class));
		Assert.assertEquals(1, listAdded.getValue().size());
		Assert.assertEquals(0, listRemoved.getValue().size());
		Assert.assertEquals(user1, listAdded.getValue().get(0));
	}

	@Test
	public void onUnassignRoleToUserInGroupChangesAssignedMaps() {
		injectUsersInPresenter(usersDtoList);
		reset(adminAssignView);
		// at start: user2 is administrator, user1 is not
		presenter.onRemoveAdminRole(Arrays.asList(user2));

		verificationAllUsersUnassigned();
	}

	@Test
	public void onSaveAfterUnassignTest() {
		injectUsersInPresenter(usersDtoList);
		// at start: user2 is administrator, user1 is not
		presenter.onRemoveAdminRole(Arrays.asList(user2));
		reset(userService);
		presenter.onSave();

		ArgumentCaptor<List> listAdded = ArgumentCaptor.forClass(List.class);
		ArgumentCaptor<List> listRemoved = ArgumentCaptor.forClass(List.class);
		verify(userService).updateAdmins(listAdded.capture(), listRemoved.capture(),
				Mockito.any(DataCallback.class));
		Assert.assertEquals(0, listAdded.getValue().size());
		Assert.assertEquals(1, listRemoved.getValue().size());
		Assert.assertEquals(user2, listRemoved.getValue().get(0));
	}

	@Test
	public void correctionUnassingAfterAssignTest() {
		injectUsersInPresenter(usersDtoList);
		// at start: user2 is administrator, user1 is not
		presenter.onAddAdminRole(Arrays.asList(user1));
		reset(adminAssignView);
		presenter.onRemoveAdminRole(Arrays.asList(user1));

		verificationOneUserAssignedOneUnassigned();
	}

	@Test
	public void correctionUnassingAfterAssignNoSaveTest() {
		injectUsersInPresenter(usersDtoList);

		// at start: user2 is administrator, user1 is not
		presenter.onAddAdminRole(Arrays.asList(user1));
		presenter.onRemoveAdminRole(Arrays.asList(user1));

		presenter.onSave();

		verifyNeverUpdateAdminsToUserService();
	}

	@Test
	public void correctionAssingAfterUnassignTest() {
		injectUsersInPresenter(usersDtoList);

		// at start: user2 is administrator, user1 is not
		presenter.onRemoveAdminRole(Arrays.asList(user2));
		reset(adminAssignView);
		presenter.onAddAdminRole(Arrays.asList(user2));

		verificationOneUserAssignedOneUnassigned();
	}

	@Test
	public void correctionAssignAfterUnassignNoSaveTest() {
		injectUsersInPresenter(usersDtoList);

		// at start: user2 is administrator, user1 is not
		presenter.onRemoveAdminRole(Arrays.asList(user2));
		presenter.onAddAdminRole(Arrays.asList(user2));

		presenter.onSave();

		verifyNeverUpdateAdminsToUserService();
	}

	@Test
	public void onSaveAddedAndRemovedAssignmentsAreMadeEmptyTest() {
		injectUsersInPresenter(usersDtoList);

		// at start: user2 is administrator, user1 is not
		presenter.onAddAdminRole(Arrays.asList(user1));
		presenter.onRemoveAdminRole(Arrays.asList(user2));
		presenter.onSave();
		// saved to db, add- and remove list should be set empty

		reset(userService);
		presenter.onSave();

		verifyNeverUpdateAdminsToUserService();
	}

	@Test
	public void setEnabledTrueTest() {
		presenter.setEnabled(true);
		verify(adminAssignView).setEnabled(true);
	}

	@Test
	public void setEnabledFalseTest() {
		presenter.setEnabled(false);
		verify(adminAssignView).setEnabled(false);
		verify(adminAssignView).clearValues();
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
	public void onEditTest() {
		presenter.onEdit();
		verifySetPresenterEditable(true);
	}

	@Test
	public void onCancelTest() {
		presenter.onCancel();
		verifySetPresenterEditable(false);
	}

	// ----------------------------------------------------
	// private methods
	// ----------------------------------------------------

	/**
	 * Create Dto objects that will be used in the methods.
	 */
	private void createDtoObjects() {
		TerritoryDto group = new TerritoryDto();
		group.setId(1l);

		/* users */
		usersDtoList = new ArrayList<UserDto>();
		user1 = new UserDto();
		user2 = new UserDto();
		user1.setId(1L);
		user2.setId(2L);
		usersDtoList.add(user1);
		usersDtoList.add(user2);

		/* users */
		ProfileDto profile1 = new ProfileDto();
		ProfileDto profile2 = new ProfileDto();
		ProfileDto profile3 = new ProfileDto();
		ProfileDto profile4 = new ProfileDto();

		profile1.setId("profile1");
		profile1.setTerritory(group);
		profile1.setRole(Role.CONSULTING_USER);

		profile2.setId("profile2");
		profile2.setTerritory(group);
		profile2.setRole(Role.EDITING_USER);
		user1.getProfiles().add(profile1);
		user1.getProfiles().add(profile2);

		profile3.setId("profile3");
		profile3.setTerritory(group);
		profile3.setRole(Role.EDITING_USER);

		profile4.setId("profile4");
		profile4.setTerritory(null);
		profile4.setRole(Role.ADMINISTRATOR);

		user2.getProfiles().add(profile3);
		user2.getProfiles().add(profile4);
	}

	/**
	 * Method for injecting the users in the presenter.
	 *
	 * @param usersDtoList
	 */
	private void injectUsersInPresenter(List<UserDto> usersDtoList) {
		// this will load all users
		presenter.loadAndShow();

		ArgumentCaptor<DataCallback> data = ArgumentCaptor.forClass(DataCallback.class);
		verify(userService).getUsers(eq(true), data.capture());
		data.getValue().execute(usersDtoList);
	}

	// ----------------------------------------------------
	// private verification methods
	// ----------------------------------------------------

	private void verificationOneUserAssignedOneUnassigned() {
		captureDataInArgumentCaptors();

		verifyAllUsersAreShown();

		// correct assigned maps
		Assert.assertEquals(1, listAssigned.getValue().size());
		Assert.assertEquals(2L, ((UserDto) listAssigned.getValue().get(0)).getId());
		Assert.assertEquals(1, listUnassigned.getValue().size());
		Assert.assertEquals(1L, ((UserDto) listUnassigned.getValue().get(0)).getId());
	}

	private void verificationAllUsersAssigned() {
		captureDataInArgumentCaptors();

		verifyAllUsersAreShown();

		// correct assigned maps
		Assert.assertEquals(2, listAssigned.getValue().size());
		Assert.assertEquals(1L, ((UserDto) listAssigned.getValue().get(0)).getId());
		Assert.assertEquals(2L, ((UserDto) listAssigned.getValue().get(1)).getId());
		Assert.assertEquals(0, listUnassigned.getValue().size());
	}

	private void verificationAllUsersUnassigned() {
		captureDataInArgumentCaptors();

		verifyAllUsersAreShown();

		// correct assigned maps
		Assert.assertEquals(0, listAssigned.getValue().size());
		Assert.assertEquals(2, listUnassigned.getValue().size());
		Assert.assertEquals(1L, ((UserDto) listUnassigned.getValue().get(0)).getId());
		Assert.assertEquals(2L, ((UserDto) listUnassigned.getValue().get(1)).getId());
	}

	private void captureDataInArgumentCaptors() {
		// data capture
		verify(adminAssignView).setAdminUsers(listAssigned.capture(), listUnassigned.capture());
	}

	private void verifyAllUsersAreShown() {
		// all users are shown
		Assert.assertEquals(usersDtoList.size(),
				listAssigned.getValue().size() + listUnassigned.getValue().size());
	}

	private void verifyNeverUpdateAdminsToUserService() {
		verify(userService, never()).updateAdmins(anyList(), anyList(),
				Mockito.any(DataCallback.class));
	}

	private void verifySetPresenterEditable(boolean editable) {
		verify(adminAssignView).setEditable(editable);
	}
}