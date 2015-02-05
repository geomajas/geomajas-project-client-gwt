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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.layer.ClientWmsLayer;
import org.geomajas.gwt.client.map.layer.configuration.ClientWmsLayerInfo;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.plugin.wms.client.WmsClient;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsGetCapabilitiesInfo;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerInfo;
import org.geomajas.gwt2.plugin.wms.client.layer.WmsLayerConfiguration;
import org.geomajas.gwt2.plugin.wms.client.service.WmsService;
import org.geomajas.widget.layer.client.Layer;

import com.google.gwt.core.client.Callback;

/**
 * Default implementation of {@link RemovableLayerListPresenter}.
 *
 * @author Jan Venstermans
 *
 */
public class CreateClientWmsPresenterImpl implements CreateClientWmsPresenter,
		CreateClientWmsPresenter.ControllersButtonHandler, CreateClientWmsPresenter.GetCapabilitiesHandler,
		CreateClientWmsPresenter.SelectLayerHandler, CreateClientWmsPresenter.EditLayerSettingsHandler {

	private static Logger logger = Logger.getLogger(CreateClientWmsPresenterImpl.class.getName());

	private ControllerButtonsView controllerButtonsWindow;

	private MapWidget mapWidget;

	/* wizard panels */
	private List<WizardStepView> wizardSteps = new ArrayList<WizardStepView>();

	private GetCapabilitiesView getCapabilitiesView;

	private SelectLayerView selectLayerView;

	private EditLayerSettingsView editLayerSettingsView;

	private WizardStepView currentStep;

	/* selected layer info */
	private WmsSelectedLayerInfo wmsSelectedLayerInfo;
	private List<WmsLayerInfo> wmsLayerInfos = new ArrayList<WmsLayerInfo>();

	public CreateClientWmsPresenterImpl(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
		this.wmsSelectedLayerInfo = new WmsSelectedLayerInfo();

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
	public void createClientWmsLayer() {
		showStep(0);
		controllerButtonsWindow.show();
	}

	public void setWmsLayerInfos(List<WmsLayerInfo> wmsLayerInfoList) {
		// do filter out wms layers with crs of map => info of layers is not complete on GetCapabilities
		this.wmsLayerInfos = new ArrayList<WmsLayerInfo>();
		for (WmsLayerInfo wmsLayerInfo : wmsLayerInfoList) {
			wmsLayerInfos.add(wmsLayerInfo);
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
		logger.info("Client WMS wizard finished without creating a ClientWmsLayerInfo object.");
		hideAndCleanWindow();
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
			WmsClient.getInstance().getWmsService().getCapabilities(wmsSelectedLayerInfo.getBaseWmsUrl(),
				wmsSelectedLayerInfo.getWmsVersion(),
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
		wmsSelectedLayerInfo.setWmsLayerInfo(layerInfo);
		goToNextStep();
	}

	/* CreateClientWmsPresenter.EditLayerSettingsHandler */

	@Override
	public void onFinishStepSetLayerName(String layerName) {
	   	wmsSelectedLayerInfo.setName(layerName);
		goToNextStep();
	}

	/* WizardStepHandler */

	@Override
	public void setWarningLabelText(String text, boolean error) {
		controllerButtonsWindow.setWarningLabelText(text, error);
	}

	/**
	 * Factory method for {@link ClientWmsLayerInfo} from
	 * {@link org.geomajas.widget.layer.client.presenter.WmsSelectedLayerInfo} and {@link MapWidget}.
	 * Could be a static method in a util class.
	 *
	 * @param wmsSelectedLayerInfo
	 * @param mapWidget
	 * @return
	 */
	public ClientWmsLayerInfo createClientWmsLayerInfo(WmsSelectedLayerInfo wmsSelectedLayerInfo, MapWidget mapWidget) {
		WmsLayerConfiguration wmsConfig = new WmsLayerConfiguration();
		wmsConfig.setFormat("image/png");
		wmsConfig.setLayers(wmsSelectedLayerInfo.getWmsLayerInfo().getName());
		wmsConfig.setVersion(wmsSelectedLayerInfo.getWmsVersion());
		wmsConfig.setBaseUrl(wmsSelectedLayerInfo.getBaseWmsUrl());
		wmsConfig.setTransparent(true);
		wmsConfig.setMaximumResolution(Double.MAX_VALUE);
		wmsConfig.setMinimumResolution(1 / mapWidget.getMapModel().getMapInfo().getMaximumScale());
		wmsConfig.setCrs(mapWidget.getMapModel().getCrs());

		Bbox bounds = wmsSelectedLayerInfo.getWmsLayerInfo().getBoundingBox(mapWidget.getMapModel().getCrs());
		if (bounds == null) {
			bounds = mapWidget.getMapModel().getMapInfo().getInitialBounds();
		}
		TileConfiguration tileConfig = new TileConfiguration(256, 256, new Coordinate(bounds.getX(), bounds.getY()),
				mapWidget.getMapModel().getMapView().getResolutions());

		ClientWmsLayer wmsLayer = new ClientWmsLayer(wmsSelectedLayerInfo.getName(), mapWidget.getMapModel().getCrs(),
				wmsConfig, tileConfig, wmsSelectedLayerInfo.getWmsLayerInfo());

		ClientWmsLayerInfo wmsLayerInfo = new ClientWmsLayerInfo(wmsLayer);
		return wmsLayerInfo;
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
			controllerButtonsWindow.setNextButtonEnabled(index + 1 < wizardSteps.size());
			controllerButtonsWindow.setWarningLabelText(null, false);

			//only allow save on last step
			controllerButtonsWindow.setSaveButtonEnabled(index + 1 == wizardSteps.size());
			logger.info("Client WMS layer wizard, current step "
					+ (currentStep != null ? currentStep.getClass().toString() : "none"));
		} else {
			hideAndCleanWindow();
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
		ClientWmsLayerInfo wmsLayerInfo = createClientWmsLayerInfo(wmsSelectedLayerInfo, mapWidget);
		logger.info("Client WMS wizard finished successfully, " +
				"created ClientWmsLayerInfo: " + wmsLayerInfo.toString());

		currentStep = null;
		hideAndCleanWindow();
		mapWidget.getMapModel().addLayer(wmsLayerInfo);
		logger.info("added layer to MapModel: " + wmsLayerInfo.toString());
	}

	private void hideAndCleanWindow() {
		for (WizardStepView stepView : wizardSteps) {
		  	stepView.clear();
		}
		controllerButtonsWindow.hide();
	}

	private boolean checkFullWmsUrl(String url) {
		if (url != null && url.contains("?")) {
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
					wmsSelectedLayerInfo.setBaseWmsUrl(baseUrl);
					wmsSelectedLayerInfo.setWmsVersion(version);
					return true;
				}
			}
		}
		return false;
	}
}
