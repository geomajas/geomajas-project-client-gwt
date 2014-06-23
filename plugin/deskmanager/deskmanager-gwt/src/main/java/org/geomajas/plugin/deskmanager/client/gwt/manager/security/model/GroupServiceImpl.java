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
package org.geomajas.plugin.deskmanager.client.gwt.manager.security.model;

import org.geomajas.command.CommandResponse;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetTerritoriesRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetTerritoriesResponse;
import org.geomajas.plugin.deskmanager.command.security.dto.CreateGroupRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.CreateGroupResponse;
import org.geomajas.plugin.deskmanager.command.security.dto.DeleteGroupRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.DeleteGroupResponse;
import org.geomajas.plugin.deskmanager.command.security.dto.GetGroupRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.GetGroupResponse;
import org.geomajas.plugin.deskmanager.command.security.dto.GetTerritoryFromShpRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.GetTerritoryFromShpResponse;
import org.geomajas.plugin.deskmanager.command.security.dto.UpdateGroupRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.UpdateGroupResponse;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;

import java.util.List;

/**
 * @author Jan De Moerloose
 */
public class GroupServiceImpl implements GroupService {

	@Override
	public void getGroups(final DataCallback<List<TerritoryDto>> onFinish) {
		GetTerritoriesRequest request = new GetTerritoriesRequest();
		GwtCommand command = new GwtCommand(GetTerritoriesRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<GetTerritoriesResponse>() {

			public void execute(GetTerritoriesResponse response) {
				if (onFinish != null) {
					onFinish.execute(response.getTerritories());
				}
			}
		});
	}

	@Override
	public void createGroup(String name, String key, String crs, Geometry geometry,
							final DataCallback<TerritoryDto> onFinish) {
		CreateGroupRequest request = new CreateGroupRequest();
		request.setName(name);
		request.setKey(key);
		request.setCrs(crs);
		request.setGeometry(geometry);
		GwtCommand command = new GwtCommand(CreateGroupRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<CreateGroupResponse>() {

			public void execute(CreateGroupResponse response) {
				if (onFinish != null) {
					onFinish.execute(response.getGroup());
				}
			}
		});
	}

	@Override
	public void updateGroup(TerritoryDto group, final DataCallback<TerritoryDto> onFinish) {
		UpdateGroupRequest request = new UpdateGroupRequest();
		request.setGroup(group);
		GwtCommand command = new GwtCommand(UpdateGroupRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<UpdateGroupResponse>() {

			public void execute(UpdateGroupResponse response) {
				if (onFinish != null) {
					onFinish.execute(response.getGroup());
				}
			}
		});
	}

	@Override
	public void deleteGroup(long groupId, final DataCallback<Boolean> onFinish) {
		DeleteGroupRequest request = new DeleteGroupRequest();
		request.setId(groupId);
		GwtCommand command = new GwtCommand(DeleteGroupRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<DeleteGroupResponse>() {

			public void execute(DeleteGroupResponse response) {
				if (onFinish != null) {
					onFinish.execute(true);
				}
			}

			@Override
			public void onCommunicationException(Throwable error) {
				onFinish.execute(false);
			}

			@Override
			public void onCommandException(CommandResponse response) {
				onFinish.execute(false);
			}
		});
	}

	@Override
	public void getGeometryOfShpFile(String shpFileToken, String toCrs, final DataCallback<Geometry> onFinish) {
		GetTerritoryFromShpRequest request = new GetTerritoryFromShpRequest();
		request.setShpFileToken(shpFileToken);
		request.setToCrs(toCrs);
		GwtCommand command = new GwtCommand(GetTerritoryFromShpRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<GetTerritoryFromShpResponse>() {

			public void execute(GetTerritoryFromShpResponse response) {
				if (onFinish != null) {
					onFinish.execute(response.getGeometry());
				}
			}
		});
	}

	@Override
	public void getGroup(long id, final DataCallback<TerritoryDto> onFinish) {
		GetGroupRequest request = new GetGroupRequest();
		request.setId(id);
		GwtCommand command = new GwtCommand(GetGroupRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<GetGroupResponse>() {

			public void execute(GetGroupResponse response) {
				if (onFinish != null) {
					onFinish.execute(response.getGroup());
				}
			}
		});
	}

}