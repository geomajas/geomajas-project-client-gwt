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

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Canvas;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerInfo;

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
		boolean isValid();

		String getInvalidMessage();

		/**
		 * Clear all data.
		 */
		void clear();
	}

	/**
	 * Interface for steps of the wizard.
	 */
	public interface WizardStepHandler {
		void setWarningLabelText(String text, boolean error);
	}

	/**
	 * ControllerButtonsViewImpl of the presenter.
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
		void setWarningLabelText(String text, boolean error);

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
	public interface GetCapabilitiesHandler extends WizardStepHandler {
		void onFinisStepGetCapabilities(String wmsFullUrl, String userName, String password);
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
	public interface SelectLayerHandler extends WizardStepHandler  {
		void onFinishStepSelectLayer(WmsLayerInfo layerInfo);
	}

	/**
	 * View for extra specifications of layer.
	 */
	public interface EditLayerSettingsView extends WizardStepView {
		void setEditLayerSettingsHandler(EditLayerSettingsHandler handler);
	}

	/**
	 * Handler for extra specifications of layer.
	 */
	public interface EditLayerSettingsHandler extends WizardStepHandler {
		void onFinishStepSetLayerName(String layerName);
	}

	void createClientWmsLayer();
}
