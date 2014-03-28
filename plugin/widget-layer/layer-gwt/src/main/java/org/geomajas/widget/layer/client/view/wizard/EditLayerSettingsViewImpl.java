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
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.widget.layer.client.i18n.LayerMessages;
import org.geomajas.widget.layer.client.presenter.CreateClientWmsPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jan Venstermans
 */
public class EditLayerSettingsViewImpl implements CreateClientWmsPresenter.EditLayerSettingsView {

	protected static final LayerMessages MESSAGES = GWT.create(LayerMessages.class);

	/* form fields */
	public static final String LAYER_NAME = "LayerName";

	private CreateClientWmsPresenter.EditLayerSettingsHandler handler;

	private VLayout layout;
	private KeyValueForm form;

	public EditLayerSettingsViewImpl() {
		buildGui();
	}

	private void buildGui() {
		List<FormElement> fields = new ArrayList<FormElement>();
		fields.add(new FormElement(LAYER_NAME,
				MESSAGES.layerListClientWmsWizardStepEditLayerSettingsNameLabel(),
				KeyValueForm.ITEMTYPE_TEXT, true, 300, null, null));

		form = new KeyValueForm();
		form.setWidth100();
		form.setColWidths("*");
		form.updateFields(fields);

		layout = new VLayout();
		layout.addMember(form);
	}

	@Override
	public Widget getWidget() {
		return layout;
	}

	@Override
	public String getTitle() {
		return MESSAGES.layerListClientWmsWizardStepEditLayerSettingsWindowTitle();
	}

	@Override
	public boolean isValid() {
		return form.validate();
	}

	@Override
	public String getInvalidMessage() {
		return MESSAGES.layerListClientWmsWizardStepEditLayerSettingsInvalidMessage();
	}

	@Override
	public void clear() {
		form.clearValues();
	}

	@Override
	public void sendDataToHandler() {
		Map<String, String> data = form.getData();
		handler.onFinishStepSetLayerName(data.get(LAYER_NAME));
	}

	@Override
	public void setEditLayerSettingsHandler(CreateClientWmsPresenter.EditLayerSettingsHandler handler) {
		this.handler = handler;
	}
}
