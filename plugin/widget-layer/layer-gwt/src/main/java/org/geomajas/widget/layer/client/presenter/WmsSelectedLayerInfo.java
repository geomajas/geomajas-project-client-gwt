/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.layer.client.presenter;

import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerInfo;
import org.geomajas.gwt2.plugin.wms.client.service.WmsService;

/**
 * Contains all info for the selected layer throughout the wizard process in
 * {@link org.geomajas.widget.layer.client.presenter.CreateClientWmsPresenterImpl}.
 *
 * @author Jan Venstermans
 *
 */
public class WmsSelectedLayerInfo {

	private WmsLayerInfo wmsLayerInfo;

	private String baseWmsUrl;

	private WmsService.WmsVersion wmsVersion;

	private String name;

	public WmsLayerInfo getWmsLayerInfo() {
		return wmsLayerInfo;
	}

	public void setWmsLayerInfo(WmsLayerInfo wmsLayerInfo) {
		this.wmsLayerInfo = wmsLayerInfo;
	}

	public String getBaseWmsUrl() {
		return baseWmsUrl;
	}

	public void setBaseWmsUrl(String baseWmsUrl) {
		this.baseWmsUrl = baseWmsUrl;
	}

	public WmsService.WmsVersion getWmsVersion() {
		return wmsVersion;
	}

	public void setWmsVersion(WmsService.WmsVersion wmsVersion) {
		this.wmsVersion = wmsVersion;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}