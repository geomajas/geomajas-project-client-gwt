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
import java.util.Map;

import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

/**
 * Test class for {@link org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter.GroupAssignPresenter}.
 *
 * @author Jan Venstermans
 */
public class GroupAssignPresenterTest extends UserMockTest {

	private GroupAssignPresenter presenter;

	private TerritoryDto group;

	private List<UserDto> usersDtoList;

	private UserDto user1;
	private UserDto user2;

	private ArgumentCaptor<List> mapAssigned = ArgumentCaptor.forClass(List.class);
	private ArgumentCaptor<List> mapUnassigned = ArgumentCaptor.forClass(List.class);

	@Before
	public void setUpPresenter() {
		presenter = new GroupAssignPresenter();
		createDtoObjects();
		reset(groupAssignView);
	}

	@Test
	public void constructorBindsHandlerTest() {
		presenter = new GroupAssignPresenter();
		verify(groupAssignView).setHandler(presenter);
	}

	@Test
	public void setCurrentGroupNullDisablesView() {
		presenter.setSelectedObject(null);

		verify(groupAssignView).setEnabled(false);
		verify(groupAssignView).clearValues();
	}

	@Test
	public void setCurrentGroupNotNullEnablesView() {
		TerritoryDto territoryDto = new TerritoryDto();
		territoryDto.setId(1l);
		List<UserDto> userDtos = new ArrayList<UserDto>();

		presenter.setSelectedObject(territoryDto);

		verify(groupAssignView).clearValues();
		verify(groupAssignView).setEnabled(true);
		ArgumentCaptor<DataCallback> data = ArgumentCaptor.forClass(DataCallback.class);
		verify(userService).getUsers(eq(true), data.capture());
		data.getValue().execute(userDtos);
		verify(groupAssignView).setProfilesForRole(eq(Role.CONSULTING_USER), Mockito.anyList(), Mockito.anyList());
	}

	@Test
	public void setCurrentGroupNotNullLoadsUsersTest() {
		// test of a private method
		injectGroupsIntoPresenter(new ArrayList<UserDto>());
	}

	@Test
	public void setCurrentGroupRetrievesProfilesOfUserAndSendsAssignedMapsToView() {
		injectGroupsIntoPresenter(usersDtoList);

		// default role Role.CONSULTING_USER is selected
		verificationOneUserAssignedOneUnassigned(Role.CONSULTING_USER);
	}



	@Test
	public void onSaveWhenNothingChangedTest() {
		injectGroupsIntoPresenter(usersDtoList);

		presenter.onSave();

		verifySetPresenterEditable(false);

		verifyNeverUpdateUsersOfGroupsToService();
	}

	@Test
	public void onSelectConsultingRoleChangesProfileList() {
		injectGroupsIntoPresenter(usersDtoList);

		presenter.onSelectRole(Role.EDITING_USER);

		verificationAllUsersAssigned(Role.EDITING_USER);
	}

	@Test
	public void onSelectAdministratorRoleIgnored() {
		injectGroupsIntoPresenter(usersDtoList);
		reset(groupAssignView);

		presenter.onSelectRole(Role.ADMINISTRATOR);

		verify(groupAssignView).setProfilesForRole(
				eq(Role.CONSULTING_USER), anyList(), anyList());
		verify(groupAssignView, never()).setProfilesForRole(
				eq(Role.ADMINISTRATOR), anyList(), anyList());
	}

	@Test
	public void onAssignRoleToUserInGroupChangesAssignedMaps() {
		injectGroupsIntoPresenter(usersDtoList);
		reset(groupAssignView);
		// default role Role.CONSULTING_USER is selected

		presenter.onAdd(Role.CONSULTING_USER, Arrays.asList(user2));

		verificationAllUsersAssigned(Role.CONSULTING_USER);
	}

	@Test
	public void onSaveAfterAssignTest() {
		injectGroupsIntoPresenter(usersDtoList);
		// default role Role.CONSULTING_USER is selected

		presenter.onAdd(Role.CONSULTING_USER, Arrays.asList(user2));
		reset(userService);
		presenter.onSave();

		ArgumentCaptor<Map> mapAdded = ArgumentCaptor.forClass(Map.class);
		ArgumentCaptor<Map> mapRemoved = ArgumentCaptor.forClass(Map.class);
		verify(userService).updateUsersOfGroup(
				eq(group),
				mapAdded.capture(),
				mapRemoved.capture(),
				Mockito.any(DataCallback.class));
		Assert.assertEquals(1, mapAdded.getValue().size());
		Assert.assertEquals(0, mapRemoved.getValue().size());
		Map.Entry<Long, List<Role>> entry = (Map.Entry<Long, List<Role>>)
				mapAdded.getValue().entrySet().iterator().next();
		Assert.assertTrue(user2.getId() == entry.getKey());
		Assert.assertEquals(1, entry.getValue().size());
		Assert.assertEquals(Role.CONSULTING_USER, entry.getValue().get(0));
	}

	@Test
	public void onUnassignRoleToUserInGroupChangesAssignedMaps() {
		injectGroupsIntoPresenter(usersDtoList);
		// default role Role.CONSULTING_USER is selected

		reset(groupAssignView);
		presenter.onRemove(Role.CONSULTING_USER, Arrays.asList(user1));

		verificationAllUsersUnassigned(Role.CONSULTING_USER);
	}

	@Test
	public void onSaveAfterUnassignTest() {
		injectGroupsIntoPresenter(usersDtoList);

		// default role Role.CONSULTING_USER is selected
		presenter.onRemove(Role.CONSULTING_USER, Arrays.asList(user1));
		reset(userService);
		presenter.onSave();

		ArgumentCaptor<Map> mapAdded = ArgumentCaptor.forClass(Map.class);
		ArgumentCaptor<Map> mapRemoved = ArgumentCaptor.forClass(Map.class);
		verify(userService).updateUsersOfGroup(
				eq(group),
				mapAdded.capture(),
				mapRemoved.capture(),
				Mockito.any(DataCallback.class));
		Assert.assertEquals(0, mapAdded.getValue().size());
		Assert.assertEquals(1, mapRemoved.getValue().size());
		Map.Entry<Long, List<Role>> entry = (Map.Entry<Long, List<Role>>)
				mapRemoved.getValue().entrySet().iterator().next();
		Assert.assertTrue(user1.getId() == entry.getKey());
		Assert.assertEquals(1, entry.getValue().size());
		Assert.assertEquals(Role.CONSULTING_USER, entry.getValue().get(0));
	}

	@Test
	public void correctionUnassingAfterAssignTest() {
		injectGroupsIntoPresenter(usersDtoList);
		// default role Role.CONSULTING_USER is selected

		presenter.onAdd(Role.CONSULTING_USER, Arrays.asList(user2));
		reset(groupAssignView);
		presenter.onRemove(Role.CONSULTING_USER, Arrays.asList(user2));

		verificationOneUserAssignedOneUnassigned(Role.CONSULTING_USER);
	}

	@Test
	public void correctionUnassingAfterAssignNoSaveTest() {
		injectGroupsIntoPresenter(usersDtoList);

		presenter.onAdd(Role.CONSULTING_USER, Arrays.asList(user2));
		presenter.onRemove(Role.CONSULTING_USER, Arrays.asList(user2));

		presenter.onSave();

		verifyNeverUpdateUsersOfGroupsToService();
	}

	@Test
	public void correctionAssingAfterUnassignTest() {
		injectGroupsIntoPresenter(usersDtoList);
		// default role Role.CONSULTING_USER is selected

		presenter.onRemove(Role.CONSULTING_USER, Arrays.asList(user1));
		reset(groupAssignView);
		presenter.onAdd(Role.CONSULTING_USER, Arrays.asList(user1));

		verificationOneUserAssignedOneUnassigned(Role.CONSULTING_USER);
	}

	@Test
	public void correctionAssignAfterUnassignNoSaveTest() {
		injectGroupsIntoPresenter(usersDtoList);

		presenter.onRemove(Role.CONSULTING_USER, Arrays.asList(user1));
		presenter.onAdd(Role.CONSULTING_USER, Arrays.asList(user1));

		presenter.onSave();

		verifyNeverUpdateUsersOfGroupsToService();
	}

	@Test
	public void onSaveAddedAndRemovedAssignmentsAreMadeEmptyTest() {
		injectGroupsIntoPresenter(usersDtoList);
		// default role Role.CONSULTING_USER is selected
		presenter.onAdd(Role.CONSULTING_USER, Arrays.asList(user2));
		presenter.onRemove(Role.CONSULTING_USER, Arrays.asList(user1));
		presenter.onSave();
		// saved to db, add- and remove list should be set empty

		reset(userService);
		presenter.onSave();

		verifyNeverUpdateUsersOfGroupsToService();
	}

	@Test
	public void setEnabledTrueTest() {
		presenter.setEnabled(true);
		verify(groupAssignView).setEnabled(true);
	}

	@Test
	public void setEnabledFalseTest() {
		presenter.setEnabled(false);
		verify(groupAssignView).setEnabled(false);
		verify(groupAssignView).clearValues();
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
		group = new TerritoryDto();
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

	private void injectGroupsIntoPresenter(List<UserDto> usersDtoList) {
		// this will load all users
		presenter.setSelectedObject(group);
		ArgumentCaptor<DataCallback> data = ArgumentCaptor.forClass(DataCallback.class);
		verify(userService).getUsers(eq(true), data.capture());
		data.getValue().execute(usersDtoList);
	}

	// ----------------------------------------------------
	// private verification methods
	// ----------------------------------------------------

	private void verificationOneUserAssignedOneUnassigned(Role role) {
		captureDataInArgumentCaptors(role);
		verifyAllUsersAreShown();

		// correct assigned maps
		Assert.assertEquals(1, mapAssigned.getValue().size());
		Assert.assertEquals(1L, ((UserDto) mapAssigned.getValue().get(0)).getId());
		Assert.assertEquals(1, mapUnassigned.getValue().size());
		Assert.assertEquals(2L, ((UserDto) mapUnassigned.getValue().get(0)).getId());
	}

	private void verificationAllUsersAssigned(Role role) {
		captureDataInArgumentCaptors(role);
		verifyAllUsersAreShown();
		// correct assigned maps
		Assert.assertEquals(2, mapAssigned.getValue().size());
		Assert.assertEquals(1L, ((UserDto) mapAssigned.getValue().get(0)).getId());
		Assert.assertEquals(2L, ((UserDto) mapAssigned.getValue().get(1)).getId());
		Assert.assertEquals(0, mapUnassigned.getValue().size());
	}

	private void verificationAllUsersUnassigned(Role role) {
		captureDataInArgumentCaptors(role);

		verifyAllUsersAreShown();

		// correct assigned maps
		Assert.assertEquals(0, mapAssigned.getValue().size());
		Assert.assertEquals(2, mapUnassigned.getValue().size());
		Assert.assertEquals((long) 1, ((UserDto) mapUnassigned.getValue().get(0)).getId());
		Assert.assertEquals((long) 2, ((UserDto) mapUnassigned.getValue().get(1)).getId());
	}


	private void captureDataInArgumentCaptors(Role role) {
		// data capture
		verify(groupAssignView).setProfilesForRole(
				eq(role), mapAssigned.capture(), mapUnassigned.capture());
	}

	private void verifyAllUsersAreShown() {
		// all users are shown
		Assert.assertEquals(usersDtoList.size(),
				mapAssigned.getValue().size() + mapUnassigned.getValue().size());
	}

	private void verifyNeverUpdateUsersOfGroupsToService() {
		verify(userService, never()).updateUsersOfGroup(
				Mockito.any(TerritoryDto.class), Mockito.anyMap(), Mockito.anyMap(),
				Mockito.any(DataCallback.class));
	}

	private void verifySetPresenterEditable(boolean editable) {
		verify(groupAssignView).setButtonEnabled(EditableLoadingView.Button.EDIT, !editable);
		verify(groupAssignView).setButtonEnabled(EditableLoadingView.Button.CANCEL, editable);
		verify(groupAssignView).setButtonEnabled(EditableLoadingView.Button.SAVE, editable);
		verify(groupAssignView).setEditable(editable);
	}
}