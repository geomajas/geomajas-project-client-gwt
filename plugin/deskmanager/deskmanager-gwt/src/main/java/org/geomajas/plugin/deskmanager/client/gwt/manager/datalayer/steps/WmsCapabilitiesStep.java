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

import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.NewLayerModelWizardWindow;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.Wizard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.util.GetCapabilitiesIllegalArgumentException;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.util.WizardUtil;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetWmsCapabilitiesRequest;
import org.geomajas.plugin.deskmanager.domain.dto.DynamicLayerConfiguration;
import org.geomajas.plugin.runtimeconfig.service.factory.WmsLayerBeanFactory;

/**
 * Implementation of {@link AbstractCapabilitiesStep} for WMS layers.
 *
 * @author Kristof Heirwegh
 * @author Jan Venstermans
 */
public class WmsCapabilitiesStep extends AbstractCapabilitiesStep {

	public WmsCapabilitiesStep(Wizard parent) {
		super(parent, NewLayerModelWizardWindow.STEP_WMS_PROPS,
				MESSAGES.wmsCapabilitiesStepTitle(), MESSAGES.wmsCapabilitiesStepNumbering());
	}

	@Override
	protected String getCapabilitiesUrlTextFieldName() {
		return GetWmsCapabilitiesRequest.GET_CAPABILITIES_URL;
	}

	@Override
	protected String getCapabilitiesUrlTextFieldTitle() {
		return MESSAGES.wmsCapabilitiesStepParametersCapabilitiesURL();
	}

	@Override
	protected String getCapabilitiesUrlTextFieldTooltip() {
		return MESSAGES.wmsCapabilitiesStepParametersCapabilitiesURLTooltip();
	}

	@Override
	protected String getUserNameTextFieldName() {
		return WmsLayerBeanFactory.WMS_USERNAME;
	}

	@Override
	protected String getUserNameTextFieldTitle() {
		return MESSAGES.wmsCapabilitiesStepParametersUserName();
	}

	@Override
	protected String getPasswordTextFieldName() {
		return WmsLayerBeanFactory.WMS_PASSWORD;
	}

	@Override
	protected String getPasswordTextFieldTitle() {
		return MESSAGES.wmsCapabilitiesStepParametersPassword();
	}

	@Override
	protected String getParamSourceType() {
		return DynamicLayerConfiguration.SOURCE_TYPE_WMS;
	}

	@Override
	public String getNextStep() {
		return NewLayerModelWizardWindow.STEP_WMS_CHOOSE_LAYER;
	}

	@Override
	protected String cannotFindNextStepErrorMessage() {
		return MESSAGES.wmsCapabilitiesStepNextStepNotFound();
	}

	@Override
	protected String convertToFullCapabilitiesUrl(String inputUrl) throws GetCapabilitiesIllegalArgumentException  {
		return WizardUtil.constructWmsGetCapabilities(inputUrl);
	}

	@Override
	protected String getCapabilitiesUrlProperty() {
		return GetWmsCapabilitiesRequest.GET_CAPABILITIES_URL;
	}
}
