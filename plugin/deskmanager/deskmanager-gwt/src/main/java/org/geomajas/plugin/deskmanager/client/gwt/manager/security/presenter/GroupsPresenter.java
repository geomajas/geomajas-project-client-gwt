/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter;

import com.google.gwt.core.client.Callback;
import org.geomajas.global.ExceptionDto;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.model.GroupService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.GroupsView;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.Manager;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link ObjectsTabHandler} for {@link TerritoryDto}.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public class GroupsPresenter implements ObjectsTabHandler<TerritoryDto>, MainTabHandler {

	private GroupsView view;

	private GroupService groupService;

	private GroupDetailHandler detailPresenter;

	private GroupAssignPresenter groupAssignPresenter;

	private List<EditableHandler> subPresenters = new ArrayList<EditableHandler>();

	public GroupsPresenter() {
		this(Manager.getUsersManagementViews().getGroupsView(), Manager.getGroupService());
	}

	public GroupsPresenter(GroupsView view, GroupService groupService) {
		this.groupService = groupService;
		this.view = view;
		detailPresenter = Manager.getUserManagementPresenters().getGroupDetailPresenter();
		groupAssignPresenter = Manager.getUserManagementPresenters().getGroupAssignPresenter();
		view.setHandler(this);
		detailPresenter.setObjectsTabHandler(this);
		subPresenters.add(detailPresenter);
		subPresenters.add(groupAssignPresenter);
	}

	//--------------------------------------------------------
	// methods of MainTabHandler
	//--------------------------------------------------------

	@Override
	public void setEnabled(boolean enabled) {
		for (EditableHandler subPresenters : this.subPresenters) {
			subPresenters.setEnabled(enabled);
		}
	}

	@Override
	public void loadAndShow() {
		loadAll(null);
	}

	//--------------------------------------------------------
	// methods of ObjectGroupHandler
	//--------------------------------------------------------

	@Override
	public void loadAll(final Callback<Boolean, ExceptionDto> onLoadAllFinished) {
		view.setLoading(true);
		setEnabled(false);
		groupService.getGroups(new DataCallback<List<TerritoryDto>>() {

			@Override
			public void execute(List<TerritoryDto> groups) {
				view.setGroups(groups);
				if (onLoadAllFinished != null) {
					onLoadAllFinished.onSuccess(true);
				}
			}
		});
	}

	@Override
	public void onSelect(TerritoryDto group) {
		view.selectGroup(group);
		detailPresenter.loadObject(group);
		groupAssignPresenter.setSelectedObject(group);
	}

	@Override
	public void onDelete(TerritoryDto group) {
		groupService.deleteGroup(group.getId(), new DataCallback<Boolean>() {
			@Override
			public void execute(Boolean result) {
				if (result) {
					loadAll(null);
				} else {
					// TODO: show information somewhere?
				}
			}
		});
	}

	@Override
	public void createObject() {
		detailPresenter.createNewObject();
	}
}
