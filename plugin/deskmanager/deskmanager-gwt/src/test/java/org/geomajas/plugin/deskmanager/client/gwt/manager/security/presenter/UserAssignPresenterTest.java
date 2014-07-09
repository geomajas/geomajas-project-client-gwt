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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

/**
 * Test class for {@link UserAssignPresenter}.
 *
 * @author Jan Venstermans
 */
public class UserAssignPresenterTest extends UserMockTest {

	private UserAssignPresenter presenter;

	private UserDto user;

	private List<ProfileDto> profileDtoList;

	private List<TerritoryDto> territoryDtoList;

	private TerritoryDto group1;
	private TerritoryDto group2;

	private ArgumentCaptor<List> listAssigned = ArgumentCaptor.forClass(List.class);
	private ArgumentCaptor<List> listUnassigned = ArgumentCaptor.forClass(List.class);

	@Before
	public void setUpPresenter() {
		presenter = new UserAssignPresenter();
		createDtoObjects();
		reset(userAssignView);
	}

	@Test
	public void constructorBindsHandlerTest() {
		presenter = new UserAssignPresenter();
		verify(userAssignView).setHandler(presenter);
	}

	@Test
	public void setCurrentUserNullDisablesView() {
		presenter.setSelectedObject(null);

		verify(userAssignView).clearValues();
		verify(userAssignView).setEnabled(false);
	}

	@Test
	public void setCurrentUserNotNullEnablesView() {
		UserDto user = new UserDto();
		user.setId(1);
		List<ProfileDto> profileDtos = mock(List.class);

		presenter.setSelectedObject(user);

		verify(userAssignView).clearValues();
		verify(userAssignView).setEnabled(true);
		ArgumentCaptor<DataCallback> data = ArgumentCaptor.forClass(DataCallback.class);
		verify(userService).getProfilesOfUser(eq(user.getId()), data.capture());
		data.getValue().execute(profileDtos);
		verify(userAssignView).setProfilesForRole(eq(Role.CONSULTING_USER), Mockito.anyList(), Mockito.anyList());
	}

	@Test
	public void loadGroupsCallsUserService() {
		// test of a private method
		injectGroupsIntoPresenter(new ArrayList<TerritoryDto>());
	}

	@Test
	public void setCurrentUserRetrievesProfilesOfUserAndSendsAssignedMapsToView() {
		injectGroupsIntoPresenter(territoryDtoList);

		//test of a private method
		injectUserAndProfilesIntoPresenter(user, profileDtoList);

		// default role Role.CONSULTING_USER is selected
		verificationOneGroupAssignedOneUnassigned(Role.CONSULTING_USER);
	}

	@Test
	public void onSaveWhenNothingChangedTest() {
		injectGroupsIntoPresenter(territoryDtoList);
		injectUserAndProfilesIntoPresenter(user, profileDtoList);

		presenter.onSave();

		verifyNeverAddOrRemoveProfilesToService();
	}

	@Test
	public void onSelectRoleChangesProfileList() {
		injectGroupsIntoPresenter(territoryDtoList);
		injectUserAndProfilesIntoPresenter(user, profileDtoList);

		presenter.onSelectRole(Role.EDITING_USER);

		captureDataInArgumentCaptors(Role.EDITING_USER);
		verifyAllGroupsAreShown();

		// correct assigned maps
		Assert.assertEquals(1, listAssigned.getValue().size());
		Assert.assertEquals(1l, ((TerritoryDto) listAssigned.getValue().get(0)).getId().longValue());
		Assert.assertEquals(1, listUnassigned.getValue().size());
		Assert.assertEquals(2L, ((TerritoryDto) listUnassigned.getValue().get(0)).getId().longValue());
	}

	@Test
	public void onSelectAdministratorRoleIgnored() {
		injectGroupsIntoPresenter(territoryDtoList);
		injectUserAndProfilesIntoPresenter(user, profileDtoList);

		reset(userAssignView);

		presenter.onSelectRole(Role.ADMINISTRATOR);

		verify(userAssignView).setProfilesForRole(
				eq(Role.CONSULTING_USER), anyList(), anyList());
		verify(userAssignView, never()).setProfilesForRole(
				eq(Role.ADMINISTRATOR), anyList(), anyList());
	}

	@Test
	public void onAssignRoleToUserInGroupChangesAssignedMaps() {
		injectGroupsIntoPresenter(territoryDtoList);
		injectUserAndProfilesIntoPresenter(user, profileDtoList);
		// default role Role.CONSULTING_USER is selected

		reset(userAssignView);
		presenter.onAdd(Role.CONSULTING_USER, Arrays.asList(group1));

		verificationAllGroupsAssigned(Role.CONSULTING_USER);
	}

	@Test
	public void onSaveAfterAssignTest() {
		injectGroupsIntoPresenter(territoryDtoList);
		injectUserAndProfilesIntoPresenter(user, profileDtoList);
		// default role Role.CONSULTING_USER is selected

		presenter.onAdd(Role.CONSULTING_USER, Arrays.asList(group1));
		reset(userService);
		presenter.onSave();

		ArgumentCaptor<List> listAdded = ArgumentCaptor.forClass(List.class);
		ArgumentCaptor<List> listRemoved = ArgumentCaptor.forClass(List.class);
		verify(userService).addAndRemoveProfilesOfUser(
				eq((long) 1),
				listAdded.capture(),
				listRemoved.capture(),
				Mockito.any(DataCallback.class));
		Assert.assertEquals(1, listAdded.getValue().size());
		Assert.assertEquals(0, listRemoved.getValue().size());
		ProfileDto dto = (ProfileDto) listAdded.getValue().get(0);
		Assert.assertEquals(Role.CONSULTING_USER, dto.getRole());
		Assert.assertEquals((long) 1, dto.getTerritory().getId().longValue());
	}

	@Test
	public void onUnassignRoleToUserInGroupChangesAssignedMaps() {
		injectGroupsIntoPresenter(territoryDtoList);
		injectUserAndProfilesIntoPresenter(user, profileDtoList);
		// default role Role.CONSULTING_USER is selected

		reset(userAssignView);
		presenter.onRemove(Role.CONSULTING_USER, Arrays.asList(group2));

		verificationAllGroupsUnassigned(Role.CONSULTING_USER);
	}

	@Test
	public void onSaveAfterUnassignTest() {
		injectGroupsIntoPresenter(territoryDtoList);
		injectUserAndProfilesIntoPresenter(user, profileDtoList);
		// default role Role.CONSULTING_USER is selected

		presenter.onRemove(Role.CONSULTING_USER, Arrays.asList(group2));
		reset(userService);
		presenter.onSave();

		ArgumentCaptor<List> listAdded = ArgumentCaptor.forClass(List.class);
		ArgumentCaptor<List> listRemoved = ArgumentCaptor.forClass(List.class);
		verify(userService).addAndRemoveProfilesOfUser(
				eq((long) 1),
				listAdded.capture(),
				listRemoved.capture(),
				Mockito.any(DataCallback.class));
		Assert.assertEquals(0, listAdded.getValue().size());
		Assert.assertEquals(1, listRemoved.getValue().size());
		ProfileDto dto = (ProfileDto) listRemoved.getValue().get(0);
		Assert.assertEquals(Role.CONSULTING_USER, dto.getRole());
		Assert.assertEquals((long) 2, dto.getTerritory().getId().longValue());
	}

	@Test
	public void correctionUnassingAfterAssignTest() {
		injectGroupsIntoPresenter(territoryDtoList);
		injectUserAndProfilesIntoPresenter(user, profileDtoList);
		// default role Role.CONSULTING_USER is selected

		presenter.onAdd(Role.CONSULTING_USER, Arrays.asList(group1));
		reset(userAssignView);
		presenter.onRemove(Role.CONSULTING_USER, Arrays.asList(group1));

		verificationOneGroupAssignedOneUnassigned(Role.CONSULTING_USER);
	}

	@Test
	public void correctionUnassingAfterAssignNoSaveTest() {
		injectGroupsIntoPresenter(territoryDtoList);

		//test of a private method
		injectUserAndProfilesIntoPresenter(user, profileDtoList);

		presenter.onAdd(Role.CONSULTING_USER, Arrays.asList(group1));
		presenter.onRemove(Role.CONSULTING_USER, Arrays.asList(group1));

		presenter.onSave();

		verifyNeverAddOrRemoveProfilesToService();
	}

	@Test
	public void correctionAssingAfterUnassignTest() {
		injectGroupsIntoPresenter(territoryDtoList);
		injectUserAndProfilesIntoPresenter(user, profileDtoList);
		// default role Role.CONSULTING_USER is selected

		presenter.onRemove(Role.CONSULTING_USER, Arrays.asList(group2));
		reset(userAssignView);
		presenter.onAdd(Role.CONSULTING_USER, Arrays.asList(group2));

		verificationOneGroupAssignedOneUnassigned(Role.CONSULTING_USER);
	}

	@Test
	public void correctionAssignAfterUnassignNoSaveTest() {
		injectGroupsIntoPresenter(territoryDtoList);

		//test of a private method
		injectUserAndProfilesIntoPresenter(user, profileDtoList);

		presenter.onRemove(Role.CONSULTING_USER, Arrays.asList(group2));
		presenter.onAdd(Role.CONSULTING_USER, Arrays.asList(group2));

		presenter.onSave();

		verifyNeverAddOrRemoveProfilesToService();
	}

	@Test
	public void onSaveAddedAndRemovedAssignmentsAreMadeEmptyTest() {
		injectGroupsIntoPresenter(territoryDtoList);
		injectUserAndProfilesIntoPresenter(user, profileDtoList);
		// default role Role.CONSULTING_USER is selected

		presenter.onRemove(Role.CONSULTING_USER, Arrays.asList(group2));
		presenter.onAdd(Role.CONSULTING_USER, Arrays.asList(group1));
		reset(userService);
		presenter.onSave();
		// saved to db, add- and remove list should be set empty

		reset(userService);
		presenter.onSave();

		verifyNeverAddOrRemoveProfilesToService();
	}

	@Test
	public void setEnabledTrueTest() {
		presenter.setEnabled(true);
		verify(userAssignView).setEnabled(true);
	}

	@Test
	public void setEnabledFalseTest() {
		presenter.setEnabled(false);
		verify(userAssignView).setEnabled(false);
		verify(userAssignView).clearValues();
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

	private void createDtoObjects() {
		user = new UserDto();
		user.setId(1);

		/* groups */
		territoryDtoList = new ArrayList<TerritoryDto>();
		group1 = new TerritoryDto();
		group2 = new TerritoryDto();
		group1.setId(1l);
		group2.setId(2l);
		territoryDtoList.add(group1);
		territoryDtoList.add(group2);

		/* profiles */
		profileDtoList = new ArrayList<ProfileDto>();
		ProfileDto profile1 = new ProfileDto();
		ProfileDto profile2 = new ProfileDto();

		profile1.setId("profile1");
		profile1.setTerritory(group1);
		profile1.setRole(Role.EDITING_USER);

		profile2.setId("profile2");
		profile2.setTerritory(group2);
		profile2.setRole(Role.CONSULTING_USER);
		profileDtoList.add(profile1);
		profileDtoList.add(profile2);
	}

	private void injectGroupsIntoPresenter(List<TerritoryDto> territoryDtoList) {
		presenter.loadGroups();
		ArgumentCaptor<DataCallback> data = ArgumentCaptor.forClass(DataCallback.class);
		verify(groupService).getGroups(data.capture());
		data.getValue().execute(territoryDtoList);
	}

	private void injectUserAndProfilesIntoPresenter(UserDto user,
											 List<ProfileDto> profileDtoList) {
		presenter.setSelectedObject(user);
		ArgumentCaptor<DataCallback> data = ArgumentCaptor.forClass(DataCallback.class);
		verify(userService).getProfilesOfUser(eq((long) 1), data.capture());
		data.getValue().execute(profileDtoList);
	}

	// ----------------------------------------------------
	// private verification methods
	// ----------------------------------------------------

	private void verificationOneGroupAssignedOneUnassigned(Role role) {
		captureDataInArgumentCaptors(role);
		verifyAllGroupsAreShown();

		// correct assigned maps
		Assert.assertEquals(1, listAssigned.getValue().size());
		Assert.assertEquals(2l, ((TerritoryDto) listAssigned.getValue().get(0)).getId().longValue());
		Assert.assertEquals(1, listUnassigned.getValue().size());
		Assert.assertEquals(1L, ((TerritoryDto) listUnassigned.getValue().get(0)).getId().longValue());
	}

	private void verificationAllGroupsAssigned(Role role) {
		captureDataInArgumentCaptors(role);
		verifyAllGroupsAreShown();
		// correct assigned maps
		Assert.assertEquals(2, listAssigned.getValue().size());
		Assert.assertEquals((long) 1, ((TerritoryDto) listAssigned.getValue().get(0)).getId().longValue());
		Assert.assertEquals((long) 2, ((TerritoryDto) listAssigned.getValue().get(1)).getId().longValue());
		Assert.assertEquals(0, listUnassigned.getValue().size());
	}

	private void verificationAllGroupsUnassigned(Role role) {
		captureDataInArgumentCaptors(role);

		verifyAllGroupsAreShown();

		// correct assigned maps
		Assert.assertEquals(0, listAssigned.getValue().size());
		Assert.assertEquals(2, listUnassigned.getValue().size());
		Assert.assertEquals((long) 1, ((TerritoryDto) listUnassigned.getValue().get(0)).getId().longValue());
		Assert.assertEquals((long) 2, ((TerritoryDto) listUnassigned.getValue().get(1)).getId().longValue());
	}


	private void captureDataInArgumentCaptors(Role role) {
		// data capture
		verify(userAssignView).setProfilesForRole(
				eq(role), listAssigned.capture(), listUnassigned.capture());
	}

	private void verifyAllGroupsAreShown() {
		// all users are shown
		Assert.assertEquals(territoryDtoList.size(),
				listAssigned.getValue().size() + listUnassigned.getValue().size());
	}

	private void verifyNeverAddOrRemoveProfilesToService() {
		verify(userService, never()).addAndRemoveProfilesOfUser(
				Mockito.anyLong(), Mockito.anyList(), Mockito.anyList(),
				Mockito.any(DataCallback.class));
	}


	private void verifySetPresenterEditable(boolean editable) {
		verify(userAssignView).setEditable(editable);
	}
}