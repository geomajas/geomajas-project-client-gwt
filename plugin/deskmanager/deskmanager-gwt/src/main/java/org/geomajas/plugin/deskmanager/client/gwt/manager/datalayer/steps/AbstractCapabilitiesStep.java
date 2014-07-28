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
package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.steps;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import org.geomajas.gwt.client.util.Notify;
import org.geomajas.plugin.deskmanager.client.gwt.manager.ManagerClientBundle;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.DataReceiverWizardStep;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.NewLayerModelWizardWindow;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.Wizard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.WizardStepPanel;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.panels.FormElement;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.panels.KeyValueForm;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.util.GetCapabilitiesIllegalArgumentException;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.domain.dto.DynamicLayerConfiguration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link WizardStepPanel} for inserting connection parameters that result in a GetCapabilities url.
 *
 * @author Kristof Heirwegh
 * @author Jan Venstermans
 */
public abstract class AbstractCapabilitiesStep extends WizardStepPanel {

	protected static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	protected KeyValueForm form;

	protected boolean first = true;

	protected Label urlWarningLabel;

	protected ManagerClientBundle resource;

	private String currentStep;

	public AbstractCapabilitiesStep(Wizard parent, String currentStep, String title, String titlePrefix) {
		super(currentStep, titlePrefix + " "  + title, false, parent);
		this.currentStep = currentStep;
		setWindowTitle(title);

		resource = GWT.create(ManagerClientBundle.class);
		resource.css().ensureInjected();

		urlWarningLabel = new Label();
		urlWarningLabel.setVisible(false);
		urlWarningLabel.setWidth100();
		Layout errorLabelLayout = new Layout();
		errorLabelLayout.setStyleName(resource.css().addLayerGetCapabilitiesUrlErrorLabel());
		errorLabelLayout.addMember(urlWarningLabel);

		List<FormElement> fields = new ArrayList<FormElement>();
		fields.add(new FormElement(getCapabilitiesUrlTextFieldName(),
				getCapabilitiesUrlTextFieldTitle(), null, true, 400,
				getCapabilitiesUrlTextFieldTooltip(), null));
		fields.add(new FormElement(getUserNameTextFieldName(),
				getUserNameTextFieldTitle(), 150));
		fields.add(new FormElement(getPasswordTextFieldName(),
				getPasswordTextFieldTitle(),
				KeyValueForm.ITEMTYPE_PASSWORD, false, 150, null, null));

		form = new KeyValueForm();
		form.setWidth100();
		form.setColWidths("125", "*");
		form.updateFields(fields);

		form.addItemChangedHandler(new ItemChangedHandler() {

			public void onItemChanged(ItemChangedEvent event) {
				fireChangedEvent();
			}
		});

		LayoutSpacer spacer = new LayoutSpacer();
		spacer.setHeight100();

		addMember(errorLabelLayout);
		addMember(form);
		addMember(spacer);
	}

	protected abstract String getCapabilitiesUrlTextFieldName();
	protected abstract String getCapabilitiesUrlTextFieldTitle();
	protected abstract String getCapabilitiesUrlTextFieldTooltip();

	protected abstract String getUserNameTextFieldName();
	protected abstract String getUserNameTextFieldTitle();

	protected abstract String getPasswordTextFieldName();
	protected abstract String getPasswordTextFieldTitle();

	@Override
	public void initialize() {
		Map<String, String> values = new LinkedHashMap<String, String>();
		values.put(DynamicLayerConfiguration.PARAM_SOURCE_TYPE, getParamSourceType());
		form.setData(values);
	}

	protected abstract String getParamSourceType();

	@Override
	public boolean isValid() {
		// don't check first time, otherwise errors are immediately shown
		if (first) {
			first = !first;
			return false;
		} else {
			clearNonFormValidation();
			return form.validate();
		}
	}

	@Override
	public String getPreviousStep() {
		return NewLayerModelWizardWindow.STEP_CHOOSE_TYPE;
	}

	@Override
	public void reset() {
		form.reset();
	}

	@Override
	public boolean stepFinished() {
		clearNonFormValidation();
		Map<String, String> data;
		try {
			data = getData();
		} catch (GetCapabilitiesIllegalArgumentException iae) {
			urlWarningLabel.setContents(MESSAGES.addLayerGetCapabilitiesUrlError(
					iae.getCapabilitiesParameter().getUrlKey(), iae.getObligedValue()));
			urlWarningLabel.setVisible(true);
			return false;
		}
		// search next step
		DataReceiverWizardStep nextStep = (DataReceiverWizardStep) parent.getStep(getNextStep());
		if (nextStep != null) {
			nextStep.setPreviousStep(currentStep);
			nextStep.setData(data);
			return true;
		}
		Notify.error(cannotFindNextStepErrorMessage());
		return false;
	}

	protected abstract String cannotFindNextStepErrorMessage();

	public Map<String, String> getData() throws GetCapabilitiesIllegalArgumentException {
		Map<String, String> provisionalData = form.getData(true);
		String fullCapabilitiesUrl = convertToFullCapabilitiesUrl(
				provisionalData.get(getCapabilitiesUrlProperty()));
		provisionalData.put(getCapabilitiesUrlProperty(), fullCapabilitiesUrl);
		return provisionalData;
	}

	private void clearNonFormValidation() {
		urlWarningLabel.clear();
		urlWarningLabel.setVisible(false);
	}

	protected abstract String convertToFullCapabilitiesUrl(String inputUrl)
			throws GetCapabilitiesIllegalArgumentException;

	protected abstract String getCapabilitiesUrlProperty();
}
