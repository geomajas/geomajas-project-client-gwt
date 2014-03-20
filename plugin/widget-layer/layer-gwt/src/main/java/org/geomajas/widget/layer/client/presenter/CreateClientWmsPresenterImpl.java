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
import org.geomajas.geometry.Coordinate;
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
		CreateClientWmsPresenter.ControllersButtonHandler, CreateClientWmsPresenter.GetCapabilitiesHandler, CreateClientWmsPresenter.SelectLayerHandler {

	private ControllerButtonsView controllerButtonsWindow;

	private MapWidget mapWidget;

	private Callback<ClientWmsLayerInfo, String> callback;

	/* wizard panels */
	private List<WizardStepView> wizardSteps = new ArrayList<WizardStepView>();

	private GetCapabilitiesView getCapabilitiesView;

	private SelectLayerView selectLayerView;

	private List<WmsLayerInfo> wmsLayerInfos;

	private SelectedLayerInfo selectedLayerInfo;

	private WizardStepView currentStep;

	public CreateClientWmsPresenterImpl(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
		this.selectedLayerInfo = new SelectedLayerInfo();
		// dummy
		selectedLayerInfo.setName("new Layer");
		selectedLayerInfo.setWmsVersion(WmsService.WmsVersion.V1_3_0);

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

	private void showStep(int index) {
		if (index >= 0 && index < wizardSteps.size()) {
			currentStep = wizardSteps.get(index);
			// only show current step
			for (WizardStepView view : wizardSteps) {
				view.getWidget().setVisible(currentStep == view);
			}
			controllerButtonsWindow.setSubTitle(currentStep.getTitle());
			controllerButtonsWindow.getPanelContainer().clear();
			controllerButtonsWindow.setPreviousButtonEnabled(index > 0);
			controllerButtonsWindow.setNextButtonEnabled(index +1 < wizardSteps.size());

			//only allow save on last step
			controllerButtonsWindow.setSaveButtonEnabled(index + 1 == wizardSteps.size());
			controllerButtonsWindow.show();
		} else {
			controllerButtonsWindow.hide();
		}
	}

	private void goToNextStep() {
		showStep(wizardSteps.indexOf(currentStep) + 1);
	}

	private void goToPreviousStep() {
		showStep(wizardSteps.indexOf(currentStep) - 1);
	}

	private void destroyViews() {
		for (WizardStepView step : wizardSteps) {

		}
	}

	private ClientWmsLayerInfo createWmsLayerInfo() {
		WmsLayerConfiguration wmsConfig = new WmsLayerConfiguration();
		wmsConfig.setFormat("image/png");
		wmsConfig.setLayers(selectedLayerInfo.getName());
		wmsConfig.setVersion(selectedLayerInfo.getWmsVersion());
		wmsConfig.setBaseUrl(selectedLayerInfo.getBaseWmsUrl());
		wmsConfig.setTransparent(true);
		wmsConfig.setMaximumResolution(0);
		wmsConfig.setMinimumResolution(1 / mapWidget.getMapModel().getMapInfo().getMaximumScale());

		WmsTileConfiguration tileConfig = new WmsTileConfiguration(256, 256,
				new Coordinate(-20026376.393709917, -20026376.393709917));

		ClientWmsLayer wmsLayer = new ClientWmsLayer(selectedLayerInfo.getName(), wmsConfig, tileConfig,
				selectedLayerInfo.getWmsLayerInfo());

		ClientWmsLayerInfo wmsLayerInfo = new ClientWmsLayerInfo(wmsLayer);
		return wmsLayerInfo;
	}

	/* Handler methods */

	/* CreateClientWmsPresenter.ControllersButtonHandler */

	@Override
	public void onSave() {
		if (currentStep.validate()) {
			currentStep.sendDataToHandler();
			callback.onSuccess(createWmsLayerInfo());
			onCancel();
		}
	}

	@Override
	public void onCancel() {
		currentStep = null;
		controllerButtonsWindow.hide();
	}

	@Override
	public void onNext() {
		if (currentStep.validate()) {
			currentStep.sendDataToHandler();
			// check if there is next
			if (currentStep != null && wizardSteps.indexOf(currentStep) + 1 < wizardSteps.size()) {
				// finish();
			}
		}
	}

	@Override
	public void onPrevious() {
		goToPreviousStep();
	}

	/* CreateClientWmsPresenter.GetCapabilitiesHandler */

	@Override
	public void onGetCapabilities(String url, String version, String userName, String password) {
		if (url.contains("?")) {
			selectedLayerInfo.setBaseWmsUrl(url.substring(0, url.indexOf("?")));
		} else {
			selectedLayerInfo.setBaseWmsUrl(url);
		}
		WmsClient.getInstance().getWmsService().getCapabilities(selectedLayerInfo.getBaseWmsUrl(),
				selectedLayerInfo.getWmsVersion(),
				new Callback<WmsGetCapabilitiesInfo, String>() {

					@Override
					public void onFailure(String s) {

					}

					@Override
					public void onSuccess(WmsGetCapabilitiesInfo wmsGetCapabilitiesInfo) {
						if (currentStep instanceof GetCapabilitiesView) {
							setWmsLayerInfos(wmsGetCapabilitiesInfo.getLayers());
							goToNextStep();
						}
					}
				});
	}

	/* CreateClientWmsPresenter.SelectLayerHandler */

	@Override
	public void onSelectLayer(WmsLayerInfo layerInfo) {
		selectedLayerInfo.setWmsLayerInfo(layerInfo);
	}

	public void setWmsLayerInfos(List<WmsLayerInfo> wmsLayerInfos) {
		this.wmsLayerInfos = wmsLayerInfos;
		selectLayerView.setWmsLayersData(wmsLayerInfos);
	}

	/**
	 * Contains all info of the selected layer throughout the wizard process;
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
