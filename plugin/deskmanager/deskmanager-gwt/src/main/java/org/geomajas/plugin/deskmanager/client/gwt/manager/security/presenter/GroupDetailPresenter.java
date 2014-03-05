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

import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.model.GroupService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.GroupDetailView;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.Manager;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;

/**
 * Default implementation of {@link GroupDetailHandler}.
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public class GroupDetailPresenter extends AbstractDetailPresenter<TerritoryDto> implements GroupDetailHandler {

	private GroupDetailView view;

	private GroupService groupService;
	
	public GroupDetailPresenter() {
		this(Manager.getUsersManagementViews().getGroupDetailView(), Manager.getGroupService());
	}

	public GroupDetailPresenter(GroupDetailView view, GroupService groupService) {
		super(view);
		this.groupService = groupService;
		this.view = view;
	}

	//---------------------------------------------
	// override abstract methods
	//---------------------------------------------

	@Override
	protected void getObjectFromService(TerritoryDto group, final DataCallback<TerritoryDto> onFinish) {
		groupService.getGroup(group.getId(), new DataCallback<TerritoryDto>() {

			@Override
			public void execute(TerritoryDto groupFromDb) {
				onFinish.execute(groupFromDb);
			}
		});
	}

	@Override
	protected TerritoryDto createEmptyObject() {
		TerritoryDto groupDto = new TerritoryDto();
		groupDto.setCrs(view.getMapCrs());
		return groupDto;
	}

	//---------------------------------------------
	// handler methods
	//---------------------------------------------

	@Override
	public void onSave(TerritoryDto group) {
		super.onSave();
		if (group != null) {
			if (group.getId() != null) {
				groupService.updateGroup(group, new DataCallback<TerritoryDto>() {

					@Override
					public void execute(TerritoryDto group) {
						loadAllAndShowObject(group);
					}

				});
			} else {
				groupService.createGroup(group.getName(), group.getCode(), group.getCrs(),
						group.getGeometry(), new DataCallback<TerritoryDto>() {
					@Override
					public void execute(TerritoryDto result) {
						loadAllAndShowObject(result);
					}
				});
			}
		}
	}
	@Override
	public void setEditable(boolean editable) {
		super.setEditable(editable);
		if (view != null) {
			view.setMapEditingButtonsEnabled(editable);
		}
	}

	@Override
	public void onFileUploaded(String shpFileToken) {
		groupService.getGeometryOfShpFile(shpFileToken, view.getMapCrs(), new DataCallback<Geometry>() {
			@Override
			public void execute(Geometry result) {
				view.setImportedGeometry(result);
			}
		});
	}
}
