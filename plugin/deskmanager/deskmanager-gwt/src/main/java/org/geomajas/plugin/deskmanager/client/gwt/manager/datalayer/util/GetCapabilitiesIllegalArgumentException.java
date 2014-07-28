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
package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.util;

/**
 *  Extension of {@link IllegalArgumentException} for an incorrect GetCapabilities url.
 *
 * @author Jan Venstermans
 */
public final class GetCapabilitiesIllegalArgumentException extends IllegalArgumentException {

	private WizardUtil.Capabilities_Parameter capabilitiesParameter;

	private String obligedValue;

	public GetCapabilitiesIllegalArgumentException(
			WizardUtil.Capabilities_Parameter capabilitiesParameter, String obligedValue) {
		super("Url query parameter " + capabilitiesParameter.getUrlKey() + " should have value " + obligedValue);
		this.capabilitiesParameter = capabilitiesParameter;
		this.obligedValue = obligedValue;
	}

	public WizardUtil.Capabilities_Parameter getCapabilitiesParameter() {
		return capabilitiesParameter;
	}

	public String getObligedValue() {
		return obligedValue;
	}
}
