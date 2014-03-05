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
package org.geomajas.plugin.deskmanager.client.gwt.common.impl;

import junit.framework.Assert;
import org.geomajas.gwt.client.command.TokenRequestHandler;
import org.geomajas.gwt.client.command.event.TokenChangedEvent;
import org.geomajas.gwt.client.command.event.TokenChangedHandler;
import org.geomajas.plugin.deskmanager.client.gwt.common.HelperWindows;
import org.geomajas.plugin.deskmanager.client.gwt.common.ProfileRequestCallback;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesResponse;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Iterator;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Test class for methods of {@link DeskmanagerTokenRequestHandler}.
 *
 * @author Jan Venstermans
 */
public class DeskmanagerTokenRequestHandlerTest {

	private static final String GEODESK_MANAGER = RetrieveRolesRequest.MANAGER_ID;
	private DeskmanagerTokenRequestHandler tokenRequestHandler;

	private final String GEODESK_ID = "geodeskId";
	private final String TOKEN1 = "token1";
	private final String TOKEN2 = "token2";

	private TokenRequestHandler fallbackTokenRequestHandlerMock = mock(TokenRequestHandler.class);
	private TokenChangedHandler tokenChangedHandlerMock = mock(TokenChangedHandler.class);
	HelperWindows.RolesChoiceWindow rolesWindowMock = mock(HelperWindows.RolesChoiceWindow.class);
	HelperWindows.UnauthorizedWindow unauthorizedWindowMock = mock(HelperWindows.UnauthorizedWindow.class);

	private ProfileDto profile1;
	private ProfileDto profile2;

	@Before
	public void setUpPresenter() {
		tokenRequestHandler = new DeskmanagerTokenRequestHandler(GEODESK_MANAGER, fallbackTokenRequestHandlerMock);
		tokenRequestHandler.setRoleSelectWindow(rolesWindowMock);
		tokenRequestHandler.setUnauthorizedWindow(unauthorizedWindowMock);
		profile1 = new ProfileDto();
		profile1.setRole(Role.CONSULTING_USER);
		profile2 = new ProfileDto();
		profile2.setRole(Role.CONSULTING_USER);
	}

	@Test
	public void responseContainsGuestTest() {
		RetrieveRolesResponse response = new RetrieveRolesResponse();
		profile1.setRole(Role.GUEST);
		response.getRoles().put(TOKEN1, profile1);

		tokenRequestHandler.processResponse(tokenChangedHandlerMock, fallbackTokenRequestHandlerMock, response);

		Assert.assertEquals(TOKEN1, tokenRequestHandler.getToken());
		Assert.assertEquals(null, tokenRequestHandler.getProfile());
		verifyTokenEventFired(TOKEN1);
	}

	@Test
	public void responseContainsOneProfileTest() {
		RetrieveRolesResponse response = new RetrieveRolesResponse();
		response.getRoles().put(TOKEN1, profile1);

		tokenRequestHandler.processResponse(tokenChangedHandlerMock, fallbackTokenRequestHandlerMock, response);

		Assert.assertEquals(TOKEN1, tokenRequestHandler.getToken());
		Assert.assertEquals(profile1, tokenRequestHandler.getProfile());
		verifyTokenEventFired(TOKEN1);
	}

	@Test
	public void responseForManagerViewFiltersConsultingOrEditingRoleTest() {
		RetrieveRolesResponse response = new RetrieveRolesResponse();
		profile1.setRole(Role.CONSULTING_USER);
		profile2.setRole(Role.EDITING_USER);
		response.getRoles().put(TOKEN1, profile1);
		response.getRoles().put(TOKEN2, profile2);

		tokenRequestHandler.processResponse(tokenChangedHandlerMock, fallbackTokenRequestHandlerMock, response);

		verify(fallbackTokenRequestHandlerMock, never()).login(any(TokenChangedHandler.class));
		verify(unauthorizedWindowMock).show();
		Assert.assertEquals(null, tokenRequestHandler.getToken());
		Assert.assertEquals(null, tokenRequestHandler.getProfile());
	}

	@Test
	public void responseContainsMoreThanOneProfileButOnlyOneAuthorizedTest() {
		RetrieveRolesResponse response = new RetrieveRolesResponse();
		profile2.setRole(Role.ADMINISTRATOR);
		response.getRoles().put(TOKEN1, profile1);
		response.getRoles().put(TOKEN2, profile2);

		tokenRequestHandler.processResponse(tokenChangedHandlerMock, fallbackTokenRequestHandlerMock, response);

		verifyTokenEventFired(TOKEN2);
	}

	@Test
	public void responseContainsMoreThanOneProfileWithMoreThanOneAuthorizedTest() {
		RetrieveRolesResponse response = new RetrieveRolesResponse();
		profile1.setRole(Role.ADMINISTRATOR);
		profile2.setRole(Role.DESK_MANAGER);
		response.getRoles().put(TOKEN1, profile1);
		response.getRoles().put(TOKEN2, profile2);

		tokenRequestHandler.processResponse(tokenChangedHandlerMock, fallbackTokenRequestHandlerMock, response);

		ArgumentCaptor<ProfileRequestCallback> profileRequestCallbackArgumentCaptor
				= ArgumentCaptor.forClass(ProfileRequestCallback.class);
		ArgumentCaptor<Map> mapArgumentCaptor
				= ArgumentCaptor.forClass(Map.class);
		verify(rolesWindowMock).askRoleWindow(mapArgumentCaptor.capture(), profileRequestCallbackArgumentCaptor.capture());
		Assert.assertEquals(2, mapArgumentCaptor.getValue().size());
		Iterator<Map.Entry<String, ProfileDto>> iterator = mapArgumentCaptor.getValue().entrySet().iterator();
		Map.Entry<String, ProfileDto> entry = iterator.next();
		Assert.assertEquals(TOKEN1, entry.getKey());
		Assert.assertEquals(profile1, entry.getValue());
		entry = iterator.next();
		Assert.assertEquals(TOKEN2, entry.getKey());
		Assert.assertEquals(profile2, entry.getValue());

		profileRequestCallbackArgumentCaptor.getValue().onTokenChanged(TOKEN2, profile2);
		Assert.assertEquals(TOKEN2, tokenRequestHandler.getToken());
		Assert.assertEquals(profile2, tokenRequestHandler.getProfile());
		verifyTokenEventFired(TOKEN2);
	}

	@Test
	public void responseForDeskViewDoesNotFiltersConsultingOrEditingTest() {
		// set geodesk Manager
		tokenRequestHandler = new DeskmanagerTokenRequestHandler(GEODESK_ID, fallbackTokenRequestHandlerMock);
		tokenRequestHandler.setRoleSelectWindow(rolesWindowMock);
		tokenRequestHandler.setUnauthorizedWindow(unauthorizedWindowMock);

		RetrieveRolesResponse response = new RetrieveRolesResponse();
		profile1.setRole(Role.CONSULTING_USER);
		profile2.setRole(Role.EDITING_USER);
		response.getRoles().put(TOKEN1, profile1);
		response.getRoles().put(TOKEN2, profile2);

		tokenRequestHandler.processResponse(tokenChangedHandlerMock, fallbackTokenRequestHandlerMock, response);

		ArgumentCaptor<ProfileRequestCallback> profileRequestCallbackArgumentCaptor
				= ArgumentCaptor.forClass(ProfileRequestCallback.class);
		ArgumentCaptor<Map> mapArgumentCaptor
				= ArgumentCaptor.forClass(Map.class);
		verify(rolesWindowMock).askRoleWindow(mapArgumentCaptor.capture(), profileRequestCallbackArgumentCaptor.capture());
		Assert.assertEquals(2, mapArgumentCaptor.getValue().size());
		Iterator<Map.Entry<String, ProfileDto>> iterator = mapArgumentCaptor.getValue().entrySet().iterator();
		Map.Entry<String, ProfileDto> entry = iterator.next();
		Assert.assertEquals(TOKEN1, entry.getKey());
		Assert.assertEquals(profile1, entry.getValue());
		entry = iterator.next();
		Assert.assertEquals(TOKEN2, entry.getKey());
		Assert.assertEquals(profile2, entry.getValue());

		profileRequestCallbackArgumentCaptor.getValue().onTokenChanged(TOKEN2, profile2);
		Assert.assertEquals(TOKEN2, tokenRequestHandler.getToken());
		Assert.assertEquals(profile2, tokenRequestHandler.getProfile());
		verifyTokenEventFired(TOKEN2);
	}

	@Test
	public void responseContainNoProfileGeodeskPublicTest() {
		RetrieveRolesResponse response = new RetrieveRolesResponse();
		response.setPublicGeodesk(true);

		tokenRequestHandler.processResponse(tokenChangedHandlerMock, fallbackTokenRequestHandlerMock, response);

		verify(tokenChangedHandlerMock).onTokenChanged(any(TokenChangedEvent.class));
	}

	@Test
	public void responseContainNoProfileActivatesFallbackTest() {
		RetrieveRolesResponse response = new RetrieveRolesResponse();

		tokenRequestHandler.processResponse(tokenChangedHandlerMock, fallbackTokenRequestHandlerMock, response);

		ArgumentCaptor<TokenChangedHandler> eventArgumentCaptor = ArgumentCaptor.forClass(TokenChangedHandler.class);
		verify(fallbackTokenRequestHandlerMock).login(eventArgumentCaptor.capture());
		// can't verify next, because of calling on GWT containing code doLogin
		//eventArgumentCaptor.getValue().onTokenChanged(new TokenChangedEvent(TOKEN2));
		//Assert.assertEquals(TOKEN1, tokenRequestHandler.getToken());
	}

	@Test
	public void responseContainNoProfileNoFallbackTest() {
		RetrieveRolesResponse response = new RetrieveRolesResponse();

		tokenRequestHandler.processResponse(tokenChangedHandlerMock, null, response);

		verify(fallbackTokenRequestHandlerMock, never()).login(any(TokenChangedHandler.class));
		verify(unauthorizedWindowMock).show();
		Assert.assertEquals(null, tokenRequestHandler.getToken());
		Assert.assertEquals(null, tokenRequestHandler.getProfile());
	}

	private void verifyTokenEventFired(String token) {
		ArgumentCaptor<TokenChangedEvent> eventArgumentCaptor = ArgumentCaptor.forClass(TokenChangedEvent.class);
		verify(tokenChangedHandlerMock).onTokenChanged(eventArgumentCaptor.capture());
		Assert.assertEquals(token, eventArgumentCaptor.getValue().getToken());
	}
}
