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
package org.geomajas.widget.layer.client.presenter;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Canvas;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

/**
 * Default implementation of {@link org.geomajas.widget.layer.client.presenter.LayerListClientWmsPresenter}.
 *
 * @author Jan Venstermans
 *
 */
public class CreateClientWmsPresenterImplTest extends BasicForPresenterMock {

	private CreateClientWmsPresenterImpl presenter;

	@MockitoAnnotations.Mock
	protected Widget getCapabilitiesViewWidget;

	@MockitoAnnotations.Mock
	protected Canvas controllerButtonsViewPanelContainer;

	@Before
	public void before() {
		stub(getCapabilitiesView.getWidget()).toReturn(getCapabilitiesViewWidget);
		stub(controllerButtonsView.getPanelContainer()).toReturn(controllerButtonsViewPanelContainer);
		presenter = new CreateClientWmsPresenterImpl(mapwidget);
	}

	@Test
	public void constructorTest() {
		verify(controllerButtonsView).setControllersButtonHandler(presenter);
		verify(controllerButtonsViewPanelContainer).addChild(getCapabilitiesViewWidget);
	}

	@Test
	public void onGetCapabilitiesTest() {
		presenter.onFinisStepGetCapabilities(
				"http://apps.geomajas.org/geoserver/ows?service=wms&version=1.3.0&request=GetCapabilities",
				null, null);
	}
}
