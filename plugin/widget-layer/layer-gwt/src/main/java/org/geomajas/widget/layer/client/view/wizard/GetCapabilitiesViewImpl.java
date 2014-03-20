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
package org.geomajas.widget.layer.client.view.wizard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.widget.layer.client.i18n.LayerMessages;
import org.geomajas.widget.layer.client.presenter.CreateClientWmsPresenter;
import org.geomajas.widget.layer.client.view.ControllerButtonsView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jan Venstermans
 */
public class GetCapabilitiesViewImpl implements CreateClientWmsPresenter.GetCapabilitiesView {

	protected static final LayerMessages MESSAGES = GWT.create(LayerMessages.class);

	/* form fields */
	public static final String GET_CAPABILITIES_URL = "GetCapabilitiesURL";
	public static final String GET_CAPABILITIES_VERSION = "GetCapabilitiesVersion";
	public static final String WMS_USERNAME = "userName";
	public static final String WMS_PASSWORD = "password";

	private CreateClientWmsPresenter.GetCapabilitiesHandler handler;

	private VLayout layout;
	private KeyValueForm form;

	public GetCapabilitiesViewImpl() {
		buildGui();
	}

	private void buildGui() {
		List<FormElement> fields = new ArrayList<FormElement>();
		fields.add(new FormElement(GET_CAPABILITIES_URL,
				MESSAGES.layerListClientWmsWizardStepGetCapabilitiesUrlLabel(),
				KeyValueForm.ITEMTYPE_TEXT, true, 300, null, null));
		fields.add(new FormElement(WMS_USERNAME,
				MESSAGES.layerListClientWmsWizardStepGetCapabilitiesUserNameLabel(), 150));
		fields.add(new FormElement(WMS_PASSWORD,
				MESSAGES.layerListClientWmsWizardStepGetCapabilitiesPasswordLabel(),
				KeyValueForm.ITEMTYPE_PASSWORD, false, 150, null, null));

		form = new KeyValueForm();
		form.setWidth100();
		form.setColWidths("200", "*");
		form.updateFields(fields);

		layout = new VLayout();
		layout.addMember(form);
	}

	@Override
	public void setGetCapabilitiesHandler(CreateClientWmsPresenter.GetCapabilitiesHandler handler) {
		this.handler = handler;
	}

	@Override
	public Widget getWidget() {
		return layout;
	}

	@Override
	public String getTitle() {
		return MESSAGES.layerListClientWmsWizardStepGetCapabilitiesWindowTitle();
	}

	@Override
	public boolean validate() {
		return form.validate();
	}

	@Override
	public void sendDataToHandler() {
		Map<String, String> data = form.getData();
		handler.onGetCapabilities(data.get(GET_CAPABILITIES_URL),
				data.get(GET_CAPABILITIES_VERSION), data.get(WMS_USERNAME),data.get(WMS_PASSWORD));
	}
}
