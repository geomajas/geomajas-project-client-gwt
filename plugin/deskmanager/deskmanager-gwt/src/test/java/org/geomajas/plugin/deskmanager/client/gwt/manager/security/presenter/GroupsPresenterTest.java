package org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter;

import com.google.gwt.core.client.Callback;
import org.geomajas.global.ExceptionDto;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.UserMockTest;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.Manager;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link GroupsPresenter}.
 *
 * @author Jan Venstermans
 */
public class GroupsPresenterTest extends UserMockTest {

	private GroupsPresenter presenter;

	private GroupDetailPresenter groupDetailPresenterMock = mock(GroupDetailPresenter.class);

	private GroupAssignPresenter groupAssignPresenterMock = mock(GroupAssignPresenter.class);

	@Before
	public void setUpPresenter() {
		Manager.getUserManagementPresenters().setGroupDetailPresenter(groupDetailPresenterMock);
		Manager.getUserManagementPresenters().setGroupAssignPresenter(groupAssignPresenterMock);
		presenter = new GroupsPresenter();
		reset(groupDetailPresenterMock);
		reset(groupAssignPresenterMock);
		reset(groupsView);
	}

	@Test
	public void constructorBindsHandlerTest() {
		presenter = new GroupsPresenter(); // create again, because mocks have been reset
		verify(groupsView).setHandler(presenter);
		verify(groupDetailPresenterMock).setObjectsTabHandler(presenter);
	}

	@Test
	public void setEnabledTrueTest() {
		presenter.setEnabled(true);
		verify(groupDetailPresenterMock).setEnabled(true);
		verify(groupAssignPresenterMock).setEnabled(true);
	}

	@Test
	public void setEnabledFalseTest() {
		presenter.setEnabled(false);
		verify(groupDetailPresenterMock).setEnabled(false);
		verify(groupAssignPresenterMock).setEnabled(false);
	}

	@Test
	public void loadAndShowTest() {
		presenter.loadAndShow();
		verify(groupsView).setLoading(true);
		verify(groupDetailPresenterMock).setEnabled(false);
		verify(groupAssignPresenterMock).setEnabled(false);
		verify(groupService).getGroups(Mockito.any(DataCallback.class));
	}

	@Test
	public void loadAllTest() {
		Callback<Boolean, ExceptionDto> callback = mock(Callback.class);
		ArgumentCaptor<DataCallback> data = ArgumentCaptor.forClass(DataCallback.class);
		List<TerritoryDto> groupListMock = mock(List.class);

		presenter.loadAll(callback);

		verify(groupsView).setLoading(true);
		verify(groupDetailPresenterMock).setEnabled(false);
		verify(groupAssignPresenterMock).setEnabled(false);

		verify(groupService).getGroups(data.capture());
		data.getValue().execute(groupListMock);
		verify(groupsView).setGroups(groupListMock);
		verify(callback).onSuccess(true);

	}

	@Test
	public void onSelectTest() {
		TerritoryDto groupMock = mock(TerritoryDto.class);
		presenter.onSelect(groupMock);

		verify(groupsView).selectGroup(groupMock);
		verify(groupDetailPresenterMock).loadObject(groupMock);
		verify(groupAssignPresenterMock).setSelectedObject(groupMock);
	}

	@Test
	public void onDeleteTest() {
		TerritoryDto groupMock = mock(TerritoryDto.class);
		when(groupMock.getId()).thenReturn(3L);
		ArgumentCaptor<DataCallback> data = ArgumentCaptor.forClass(DataCallback.class);

		presenter.onDelete(groupMock);

		verify(groupService).deleteGroup(eq(3L), data.capture());
	}

	@Test
	public void createObjectTest() {
		presenter.createObject();
		verify(groupDetailPresenterMock).createNewObject();
	}

}