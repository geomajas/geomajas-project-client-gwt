package org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter;

import com.google.gwt.core.client.Callback;
import junit.framework.Assert;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.WktException;
import org.geomajas.geometry.service.WktService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.UserMockTest;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.EditableLoadingView;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter.GroupDetailPresenter}.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public class GroupDetailPresenterTest extends UserMockTest {

	private GroupDetailPresenter presenter;

	private GroupsPresenter groupsPresenterMock = mock(GroupsPresenter.class);

	private static final String GROUP_NAME = "GroupName";
	private static final String GROUP_CODE = "GroupCode";
	private static final String GROUP_CRS = "GroupCrs";

	@Before
	public void setUpPresenter() {
		presenter = new GroupDetailPresenter(groupDetailView, groupService);
		presenter.setObjectsTabHandler(groupsPresenterMock);
		reset(groupDetailView);
	}

	@Test
	public void constructorBindsHandlerTest() {
		presenter = new GroupDetailPresenter(groupDetailView, groupService);
		presenter.setObjectsTabHandler(groupsPresenterMock);
		verify(groupDetailView).setHandler(presenter);
	}

	@Test
	public void loadGroupTest() throws WktException {
		TerritoryDto group = createTerritoryDto();

		presenter.loadObject(group);

		verify(groupDetailView).setLoading();
		ArgumentCaptor<DataCallback> data = ArgumentCaptor.forClass(DataCallback.class);
		verify(groupService).getGroup(eq(1l), data.capture());
		data.getValue().execute(group);
		ArgumentCaptor<TerritoryDto> territoryCaptor = ArgumentCaptor.forClass(TerritoryDto.class);
		verify(groupDetailView).setObject(group);
	}

	@Test
	public void onSaveNewGroupTest() throws WktException {
		TerritoryDto newGroup = createTerritoryDto();
		newGroup.setId(null);

		presenter.onSave(newGroup);

		verifySetPresenterEditable(false);
		ArgumentCaptor<DataCallback> data = ArgumentCaptor.forClass(DataCallback.class);
		verify(groupService).createGroup(eq(newGroup.getName()),eq(newGroup.getCode()), eq(newGroup.getCrs()),
				eq(newGroup.getGeometry()), data.capture());
		TerritoryDto groupMock = mock(TerritoryDto.class);
		data.getValue().execute(groupMock);

		// internally: loadAllAndShowObject =>
		ArgumentCaptor<Callback> callback = ArgumentCaptor.forClass(Callback.class);
		verify(groupsPresenterMock).loadAll(callback.capture());
		callback.getValue().onSuccess(true);
		verify(groupsPresenterMock).onSelect(groupMock);
	}

	@Test
	public void onSaveForUpdateGroupTest() throws WktException {
		TerritoryDto existingGroup = createTerritoryDto();

		presenter.onSave(existingGroup);

		verifySetPresenterEditable(false);
		ArgumentCaptor<DataCallback> data = ArgumentCaptor.forClass(DataCallback.class);
		verify(groupService).updateGroup(eq(existingGroup), data.capture());
		TerritoryDto groupMock = mock(TerritoryDto.class);
		data.getValue().execute(groupMock);

		// internally: loadAllAndShowObject =>
		ArgumentCaptor<Callback> callback = ArgumentCaptor.forClass(Callback.class);
		verify(groupsPresenterMock).loadAll(callback.capture());
		callback.getValue().onSuccess(true);
		verify(groupsPresenterMock).onSelect(groupMock);
	}

	@Test
	public void setEnabledTrueTest() {
		presenter.setEnabled(true);
		verify(groupDetailView).setEnabled(true);
	}

	@Test
	public void setEnabledFalseTest() {
		presenter.setEnabled(false);
		verify(groupDetailView).setEnabled(false);
		verify(groupDetailView).clearValues();
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
	public void onFileUploadedTest() {
		String shpFileToken = "token";
		String mapCrs = "mapCrs";
		when(groupDetailView.getMapCrs()).thenReturn(mapCrs);
		ArgumentCaptor<DataCallback> data = ArgumentCaptor.forClass(DataCallback.class);
		Geometry geometryMock = mock(Geometry.class);

		presenter.onFileUploaded(shpFileToken);

		verify(groupService).getGeometryOfShpFile(eq(shpFileToken), eq(mapCrs), data.capture());
		data.getValue().execute(geometryMock);
		verify(groupDetailView).setImportedGeometry(geometryMock);
	}

	@Test
	public void createNewGroupTest() {
		presenter.createNewObject();

		verify(groupDetailView).clearValues();
		verify(groupDetailView).setEnabled(true);
		verifySetPresenterEditable(true);
		verify(groupDetailView).focusOnFirstField();
	}

	@Test
	public void onCancelTest() throws WktException {
		TerritoryDto group = createTerritoryDto();
		// first set the group
		presenter.loadObject(group);
		verify(groupDetailView).setLoading();
		ArgumentCaptor<DataCallback> data = ArgumentCaptor.forClass(DataCallback.class);
		verify(groupService).getGroup(eq(1l), data.capture());
		data.getValue().execute(group);
		reset(groupDetailView);

		// part that is tested:
		presenter.onCancel();

		verifySetPresenterEditable(false);
		verify(groupDetailView).clearValues();
		verify(groupDetailView).setObject(group);
	}

	@Test
	public void onEditTest() {
		presenter.onEdit();

		verifySetPresenterEditable(true);
	}

	//----------------------------------------
	// private methods
	//----------------------------------------

	private TerritoryDto createTerritoryDto() throws WktException {
		TerritoryDto group = new TerritoryDto();
		group.setName(GROUP_NAME);
		group.setCode(GROUP_CODE);
		group.setCrs(GROUP_CRS);
		group.setGeometry(WktService.toGeometry("POLYGON ((0 0, 1 0, 1 1, 0 0))"));
		group.setId(1l);
		return group;
	}

	private void verifySetPresenterEditable(boolean editable) {
		verify(groupDetailView).setButtonEnabled(EditableLoadingView.Button.EDIT, !editable);
		verify(groupDetailView).setButtonEnabled(EditableLoadingView.Button.CANCEL, editable);
		verify(groupDetailView).setButtonEnabled(EditableLoadingView.Button.SAVE, editable);
		verify(groupDetailView).setEditable(editable);
		verify(groupDetailView).setMapEditingButtonsEnabled(editable);
	}
}