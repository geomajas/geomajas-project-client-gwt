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
package org.geomajas.widget.layer.client.view;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.widget.layer.client.i18n.LayerMessages;
import org.geomajas.widget.layer.client.presenter.CreateClientWmsPresenter;

/**
 * Inspired by {@link org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.NewLayerModelWizardWindow}
 * @author Kristof Heirwegh
 * @author Jan Venstermans
 */
public class ControllerButtonsViewImpl extends Window implements CreateClientWmsPresenter.ControllerButtonsView {

	private static final LayerMessages MESSAGES = GWT.create(LayerMessages.class);

	private CreateClientWmsPresenter.ControllersButtonHandler controllersButtonHandler;
	
	public static final String STEP_CHOOSE_TYPE = "choose_type";

	public static final String STEP_WMS_PROPS = "wms_props";

	public static final String STEP_WFS_PROPS = "wfs_props";

	public static final String STEP_SHAPEFILE_UPLOAD = "shapefile_upload";

	public static final String STEP_DATABASE_PROPS = "database_props";

	public static final String STEP_VECTOR_CHOOSE_LAYER = "vector_choose_layer";

	public static final String STEP_VECTOR_EDIT_LAYER_ATTRIBUTES = "vector_edit_layer_attributes_layer";

	public static final String STEP_VECTOR_EDIT_LAYER_STYLE = "vector_edit_style";

	public static final String STEP_WMS_CHOOSE_LAYER = "raster_choose_layer";

	public static final String STEP_WMS_PREVIEW_LAYER = "raster_preview_layer";

	public static final String STEP_EDIT_LAYER_SETTINGS = "edit_layer_settings";

	private static final int WIDTH = 650;

	private static final int HEIGHT = 400;


	public static final String PARAM_SOURCE_TYPE = "_source_type_";
	public static final String SOURCE_TYPE_WMS = "WMS";

	private Canvas panelContainer;

	private Label warningLabel;

	/* buttons */
	private IButton saveButton;

	private IButton cancelButton;

	private IButton prevButton;

	private IButton nextButton;

	public ControllerButtonsViewImpl() {
		/* window config */
		setHeight(HEIGHT);
		setWidth(WIDTH);
		setCanDragReposition(true);
		setCanDragResize(false);
		setKeepInParentRect(true);
		setOverflow(Overflow.HIDDEN);
		setAutoCenter(true);
		setShowCloseButton(false);
		setShowMinimizeButton(false);
		setShowMaximizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		setPadding(10);

		panelContainer = new Canvas();
		panelContainer.setWidth100();
		panelContainer.setHeight("*");

		// ----------------------------------------------------------

		warningLabel = new Label();
		warningLabel.setWidth100();
		warningLabel.setAutoHeight();
		warningLabel.setPadding(3);
		warningLabel.setOverflow(Overflow.VISIBLE);
		warningLabel.setVisible(false);

		// ----------------------------------------------------------

		HLayout buttons = new HLayout(10);
		buttons.setWidth100();
		buttons.setHeight(25);
		prevButton = new IButton(MESSAGES.layerListClientWmsWizardPreviousButtonText());
		prevButton.setIcon(WidgetLayout.iconZoomLast);
		prevButton.setDisabled(true);
		prevButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				controllersButtonHandler.onPrevious();
			}
		});

		nextButton = new IButton(MESSAGES.layerListClientWmsWizardNextButtonText());
		nextButton.setIcon(WidgetLayout.iconZoomNext);
		nextButton.setDisabled(true);
		nextButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				controllersButtonHandler.onNext();
			}
		});

		saveButton = new IButton(MESSAGES.layerListClientWmsWizardSaveButtonText());
		saveButton.setIcon(WidgetLayout.iconAdd);
		saveButton.setDisabled(true);
		saveButton.setVisible(false);
		saveButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				controllersButtonHandler.onSave();
			}
		});

		cancelButton = new IButton(MESSAGES.layerListClientWmsWizardCancelButtonText());
		cancelButton.setIcon(WidgetLayout.iconCancel);
		cancelButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				controllersButtonHandler.onCancel();
			}
		});

		LayoutSpacer ls = new LayoutSpacer();
		ls.setWidth("*");
		buttons.addMember(ls);
		buttons.addMember(prevButton);
		buttons.addMember(nextButton);
		buttons.addMember(saveButton);
		buttons.addMember(cancelButton);

		// ----------------------------------------------------------

		VLayout vl = new VLayout(10);
		vl.setWidth100();
		vl.setMargin(10);
		vl.addMember(panelContainer);
		vl.addMember(warningLabel);
		vl.addMember(buttons);
		addItem(vl);
	}

	@Override
	public void setControllersButtonHandler(CreateClientWmsPresenter.ControllersButtonHandler controllersButtonHandler) {
		 this.controllersButtonHandler = controllersButtonHandler;
	}

	@Override
	public void setSaveButtonVisible(boolean visible) {
		saveButton.setVisible(visible);
	}

	@Override
	public void setCancelButtonVisible(boolean visible) {
		cancelButton.setVisible(visible);
	}

	@Override
	public void setNextButtonVisible(boolean visible) {
		nextButton.setVisible(visible);
	}

	@Override
	public void setPreviousButtonVisible(boolean visible) {
		prevButton.setVisible(visible);
	}

	@Override
	public void setSaveButtonEnabled(boolean enabled) {
		saveButton.setDisabled(!enabled);
	}

	@Override
	public void setCancelButtonEnabled(boolean enabled) {
		cancelButton.setDisabled(!enabled);
	}

	@Override
	public void setNextButtonEnabled(boolean enabled) {
		nextButton.setDisabled(!enabled);
	}

	@Override
	public void setPreviousButtonEnabled(boolean enabled) {
		prevButton.setDisabled(!enabled);
	}

	@Override
	public Canvas getPanelContainer() {
		return panelContainer;
	}

	@Override
	public void setWarningLabelText(String text, boolean error){
	   if (text == null || text.isEmpty()) {
		  warningLabel.clear();
		  warningLabel.setVisible(false);
	   } else {
		   warningLabel.setVisible(true);
		   warningLabel.setContents("<b><i>" + text + "</i></b>");
	   }
		warningLabel.setBackgroundColor(error ? "#FFCCCC" : "transparent");
	}

	@Override
	public void setSubTitle(String subTitle) {
		setTitle(MESSAGES.layerListClientWmsWizardGeneralTitle() + " : " + subTitle);
	}
}
