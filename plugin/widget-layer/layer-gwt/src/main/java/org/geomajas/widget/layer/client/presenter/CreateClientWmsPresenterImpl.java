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
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.map.layer.ClientWmsLayer;
import org.geomajas.gwt.client.map.layer.configuration.ClientWmsLayerInfo;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.wms.client.WmsClient;
import org.geomajas.plugin.wms.client.capabilities.WmsGetCapabilitiesInfo;
import org.geomajas.plugin.wms.client.capabilities.WmsLayerInfo;
import org.geomajas.plugin.wms.client.layer.WmsLayerConfiguration;
import org.geomajas.plugin.wms.client.layer.WmsTileConfiguration;
import org.geomajas.plugin.wms.client.service.WmsService;
import org.geomajas.widget.layer.client.Layer;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link LayerListClientWmsPresenter}.
 *
 * @author Jan Venstermans
 *
 */
public class CreateClientWmsPresenterImpl implements CreateClientWmsPresenter,
		CreateClientWmsPresenter.ControllersButtonHandler, CreateClientWmsPresenter.GetCapabilitiesHandler,
		CreateClientWmsPresenter.SelectLayerHandler, CreateClientWmsPresenter.EditLayerSettingsHandler {

	private ControllerButtonsView controllerButtonsWindow;

	private MapWidget mapWidget;

	private Callback<ClientWmsLayerInfo, String> callback;

	/* wizard panels */
	private List<WizardStepView> wizardSteps = new ArrayList<WizardStepView>();

	private GetCapabilitiesView getCapabilitiesView;

	private SelectLayerView selectLayerView;

	private EditLayerSettingsView editLayerSettingsView;

	private WizardStepView currentStep;

	/* selected layer info */
	private SelectedLayerInfo selectedLayerInfo;
	private List<WmsLayerInfo> wmsLayerInfos = new ArrayList<WmsLayerInfo>();

	public CreateClientWmsPresenterImpl(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
		this.selectedLayerInfo = new SelectedLayerInfo();

		controllerButtonsWindow = Layer.getViewFactory().createControllerButtonsView();
		controllerButtonsWindow.setControllersButtonHandler(this);

		// set all buttons visible
		controllerButtonsWindow.setNextButtonVisible(true);
		controllerButtonsWindow.setPreviousButtonVisible(true);
		controllerButtonsWindow.setCancelButtonVisible(true);
		controllerButtonsWindow.setSaveButtonVisible(true);

		// set next previous and save button disabled
		controllerButtonsWindow.setNextButtonEnabled(false);
		controllerButtonsWindow.setPreviousButtonEnabled(false);
		controllerButtonsWindow.setCancelButtonEnabled(true);
		controllerButtonsWindow.setSaveButtonEnabled(false);

		// steps
		getCapabilitiesView = Layer.getViewFactory().createGetCapabilitiesView();
		getCapabilitiesView.setGetCapabilitiesHandler(this);
		wizardSteps.add(getCapabilitiesView);

		selectLayerView = Layer.getViewFactory().createSelectLayerView();
		selectLayerView.setSelectLayerFromCapabilitiesHandler(this);
		wizardSteps.add(selectLayerView);

		editLayerSettingsView = Layer.getViewFactory().createEditLayerSettingsView();
		editLayerSettingsView.setEditLayerSettingsHandler(this);
		wizardSteps.add(editLayerSettingsView);

		loadWizardStepsInWindow();

		WmsClient.getInstance().getWmsService().setWmsUrlTransformer(new WmsService.WmsUrlTransformer() {

			@Override
			public String transform(WmsService.WmsRequest request, String url) {
				switch (request) {
					case GETCAPABILITIES:
					case GETFEATUREINFO:
						return "d/proxy?url=" + url;
					default:
						return url;
				}
			}
		});
	}

	private void loadWizardStepsInWindow() {
		for (WizardStepView view : wizardSteps) {
			view.getWidget().setVisible(false);
			controllerButtonsWindow.getPanelContainer().addChild(view.getWidget());
		}
	}

	@Override
	public void createClientWmsLayer(Callback<ClientWmsLayerInfo, String> callback) {
		this.callback = callback;
		showStep(0);
		controllerButtonsWindow.show();
	}

	public void setWmsLayerInfos(List<WmsLayerInfo> wmsLayerInfoList) {
		// filter out wms layers with crs of map
		this.wmsLayerInfos = new ArrayList<WmsLayerInfo>();
		String crs = mapWidget.getMapModel().getCrs();
		for (WmsLayerInfo wmsLayerInfo : wmsLayerInfoList) {
			if (wmsLayerInfo.getCrs().contains(crs)) {
				wmsLayerInfos.add(wmsLayerInfo);
			}
		}
		selectLayerView.setWmsLayersData(wmsLayerInfos);
	}

	/* Handler methods */

	/* CreateClientWmsPresenter.ControllersButtonHandler */

	@Override
	public void onSave() {
		onNext();
	}

	@Override
	public void onCancel() {
		callback.onFailure("Has been canceled");
		controllerButtonsWindow.hide();
	}

	@Override
	public void onNext() {
		if (currentStep.isValid()) {
			currentStep.sendDataToHandler();
		} else {
			setWarningLabelText(currentStep.getInvalidMessage(), true);
		}
	}

	@Override
	public void onPrevious() {
		goToPreviousStep();
	}

	/* CreateClientWmsPresenter.GetCapabilitiesHandler */

	@Override
	public void onFinisStepGetCapabilities(String url, String userName, String password) {
		if (checkFullWmsUrl(url)) {
			WmsClient.getInstance().getWmsService().getCapabilities(selectedLayerInfo.getBaseWmsUrl(),
				selectedLayerInfo.getWmsVersion(),
				new Callback<WmsGetCapabilitiesInfo, String>() {

					@Override
					public void onFailure(String s) {

					}

					@Override
					public void onSuccess(WmsGetCapabilitiesInfo wmsGetCapabilitiesInfo) {
						controllerButtonsWindow.setWarningLabelText(null, false);
						if (currentStep instanceof GetCapabilitiesView) {
							setWmsLayerInfos(wmsGetCapabilitiesInfo.getLayers());
							goToNextStep();
						}
					}
				});
		} else {
			setWarningLabelText(currentStep.getInvalidMessage(), true);
		}
	}

	/* CreateClientWmsPresenter.SelectLayerHandler */

	@Override
	public void onFinishStepSelectLayer(WmsLayerInfo layerInfo) {
		selectedLayerInfo.setWmsLayerInfo(layerInfo);
		goToNextStep();
	}

	/* CreateClientWmsPresenter.EditLayerSettingsHandler */

	@Override
	public void onFinishStepSetLayerName(String layerName) {
	   	selectedLayerInfo.setName(layerName);
		goToNextStep();
	}

	/* private methods */
	private void showStep(int index) {
		if (index >= 0 && index < wizardSteps.size()) {
			currentStep = wizardSteps.get(index);
			// only show current step
			for (WizardStepView view : wizardSteps) {
				view.getWidget().setVisible(currentStep == view);
			}
			controllerButtonsWindow.setSubTitle(currentStep.getTitle());
			controllerButtonsWindow.setPreviousButtonEnabled(index > 0);
			controllerButtonsWindow.setNextButtonEnabled(index +1 < wizardSteps.size());
			controllerButtonsWindow.setWarningLabelText(null, false);

			//only allow save on last step
			controllerButtonsWindow.setSaveButtonEnabled(index + 1 == wizardSteps.size());
		} else {
			controllerButtonsWindow.hide();
		}
	}

	private void goToNextStep() {
		int index = wizardSteps.indexOf(currentStep) + 1;
		if (index == wizardSteps.size()) {
			finishWizard();
		} else {
			showStep(index);
		}
	}

	private void goToPreviousStep() {
		showStep(wizardSteps.indexOf(currentStep) - 1);
	}

	private void finishWizard() {
		callback.onSuccess(createWmsLayerInfo());
		currentStep = null;
		controllerButtonsWindow.hide();
	}

	private boolean checkFullWmsUrl(String url) {
		if (url!=null && url.contains("?")) {
			String baseUrl = url.substring(0, url.indexOf("?"));
			String parameterPart = url.substring(url.indexOf("?") + 1).toLowerCase();
			if (!baseUrl.isEmpty() && parameterPart.contains("service=wms")
					&& parameterPart.contains("request=getcapabilities")
					&& parameterPart.contains("version=")) {
				WmsService.WmsVersion version = null;
				if (parameterPart.contains("version=1.1.1")) {
					version = WmsService.WmsVersion.V1_1_1;
				} else if (parameterPart.contains("version=1.3.0")) {
					version = WmsService.WmsVersion.V1_3_0;
				}
				if (version != null) {
					selectedLayerInfo.setBaseWmsUrl(baseUrl);
					selectedLayerInfo.setWmsVersion(version);
					return true;
				}
			}
		}
		return false;
	}

	private ClientWmsLayerInfo createWmsLayerInfo() {
		WmsLayerConfiguration wmsConfig = new WmsLayerConfiguration();
		wmsConfig.setFormat("image/png");
		wmsConfig.setLayers(selectedLayerInfo.getWmsLayerInfo().getName());
		wmsConfig.setVersion(selectedLayerInfo.getWmsVersion());
		wmsConfig.setBaseUrl(selectedLayerInfo.getBaseWmsUrl());
		wmsConfig.setTransparent(true);
		wmsConfig.setMaximumResolution(0);
		wmsConfig.setMinimumResolution(1 / mapWidget.getMapModel().getMapInfo().getMaximumScale());

		Bbox bounds = mapWidget.getMapModel().getMapInfo().getMaxBounds();
		WmsTileConfiguration tileConfig = new WmsTileConfiguration(256, 256,
				new Coordinate(bounds.getX(), bounds.getY()));

		ClientWmsLayer wmsLayer = new ClientWmsLayer(selectedLayerInfo.getName(), wmsConfig, tileConfig,
				selectedLayerInfo.getWmsLayerInfo());

		ClientWmsLayerInfo wmsLayerInfo = new ClientWmsLayerInfo(wmsLayer);
		return wmsLayerInfo;
	}

	@Override
	public void setWarningLabelText(String text, boolean error) {
		controllerButtonsWindow.setWarningLabelText(text, error);
	}

	/**
	 * Contains all info of the selected layer throughout the wizard process.
	 */
	private class SelectedLayerInfo {

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
}
