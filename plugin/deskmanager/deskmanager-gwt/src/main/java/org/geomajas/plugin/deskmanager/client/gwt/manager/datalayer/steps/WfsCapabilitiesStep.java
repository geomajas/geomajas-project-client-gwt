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
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeotoolsVectorCapabilitiesRequest;
import org.geomajas.plugin.deskmanager.domain.dto.DynamicLayerConfiguration;

/**
 * Implementation of {@link AbstractCapabilitiesStep} for WFS layers.
 *
 * @author Kristof Heirwegh
 * @author Jan Venstermans
 */
public class WfsCapabilitiesStep extends AbstractCapabilitiesStep {

	public WfsCapabilitiesStep(Wizard parent) {
		super(parent, NewLayerModelWizardWindow.STEP_WFS_PROPS, MESSAGES.wfsCapabilitiesStepTitle(),
				MESSAGES.wfsCapabilitiesStepNumbering());
	}

	@Override
	protected String getCapabilitiesUrlTextFieldName() {
		return GetGeotoolsVectorCapabilitiesRequest.PROPERTY_WFS_CAPABILITIESURL;
	}

	@Override
	protected String getCapabilitiesUrlTextFieldTitle() {
		return MESSAGES.wfsCapabilitiesStepParametersCapabilitiesURL();
	}

	@Override
	protected String getCapabilitiesUrlTextFieldTooltip() {
		return MESSAGES.wfsCapabilitiesStepParametersCapabilitiesURLTooltip();
	}

	@Override
	protected String getUserNameTextFieldName() {
		return GetGeotoolsVectorCapabilitiesRequest.PROPERTY_WFS_USERNAME;
	}

	@Override
	protected String getUserNameTextFieldTitle() {
		return MESSAGES.wfsCapabilitiesStepParametersUserName();
	}

	@Override
	protected String getPasswordTextFieldName() {
		return GetGeotoolsVectorCapabilitiesRequest.PROPERTY_WFS_PASSWORD;
	}

	@Override
	protected String getPasswordTextFieldTitle() {
		return MESSAGES.wfsCapabilitiesStepParametersPassword();
	}

	@Override
	protected String getParamSourceType() {
		return DynamicLayerConfiguration.SOURCE_TYPE_WFS;
	}

	@Override
	public String getNextStep() {
		return NewLayerModelWizardWindow.STEP_VECTOR_CHOOSE_LAYER;
	}

	@Override
	protected String cannotFindNextStepErrorMessage() {
		return MESSAGES.wfsCapabilitiesStepNextStepNotFound();
	}

	@Override
	protected String convertToFullCapabilitiesUrl(String inputUrl) throws GetCapabilitiesIllegalArgumentException {
		return WizardUtil.constructWfsGetCapabilities(inputUrl);
	}

	@Override
	protected String getCapabilitiesUrlProperty() {
		return GetGeotoolsVectorCapabilitiesRequest.PROPERTY_WFS_CAPABILITIESURL;
	}

}
