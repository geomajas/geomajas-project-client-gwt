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

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Canvas;
import org.geomajas.gwt.client.map.layer.configuration.ClientWmsLayerInfo;
import org.geomajas.plugin.wms.client.capabilities.WmsLayerInfo;

import java.util.List;

/**
 * Presenter for creating Client Wms layer via choosing a layer from a get capabilites list.
 *
 * @author Jan Venstermans
 *
 */
public interface CreateClientWmsPresenter {

	/**
	 * Interface for steps of the wizard.
	 */
	public interface WizardStepView {
		void sendDataToHandler();
		Widget getWidget();
		String getTitle();
		boolean validate();
	}

	/**
	 * ControllerButtonsView of the presenter.
	 */
	public interface ControllerButtonsView {
		void setControllersButtonHandler(ControllersButtonHandler handler);

		void setSaveButtonVisible(boolean visible);
		void setCancelButtonVisible(boolean visible);
		void setNextButtonVisible(boolean visible);
		void setPreviousButtonVisible(boolean visible);

		void setSaveButtonEnabled(boolean enabled);
		void setCancelButtonEnabled(boolean enabled);
		void setNextButtonEnabled(boolean enabled);
		void setPreviousButtonEnabled(boolean enabled);

		Canvas getPanelContainer();

		void show();
		void hide();

		void setSubTitle(String subTitle);
	}

	/**
	 * ControllersButtonHandler of the presenter.
	 */
	public interface ControllersButtonHandler {
		void onSave();
		void onCancel();
		void onNext();
		void onPrevious();
	}

	/**
	 * GetCapabilitiesView of the presenter.
	 */
	public interface GetCapabilitiesView extends WizardStepView {
		void setGetCapabilitiesHandler(GetCapabilitiesHandler handler);
	}

	/**
	 * GetCapabilitiesHandler of the presenter.
	 */
	public interface GetCapabilitiesHandler {
		void onGetCapabilities(String wmsBaseUrl, String version, String userName, String password);
	}

	/**
	 * View for selection of a wms layer from a list.
	 */
	public interface SelectLayerView extends WizardStepView {
		void setSelectLayerFromCapabilitiesHandler(SelectLayerHandler handler);
		void setWmsLayersData(List<WmsLayerInfo> wmsLayersData);
	}

	/**
	 * Handler for a selection of a wms layer from a list.
	 */
	public interface SelectLayerHandler {
		void onSelectLayer(WmsLayerInfo layerInfo);
	}

	void createClientWmsLayer(Callback<ClientWmsLayerInfo, String> callback);
}
