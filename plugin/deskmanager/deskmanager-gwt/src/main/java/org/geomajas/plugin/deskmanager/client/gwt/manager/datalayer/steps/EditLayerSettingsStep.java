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

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import org.geomajas.configuration.Parameter;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.NewLayerModelWizardWindow;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.Wizard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.WizardStepPanel;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.panels.LayerSettingsForm;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.panels.MaxBoundsForm;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.command.manager.dto.DynamicRasterLayerConfiguration;
import org.geomajas.plugin.deskmanager.command.manager.dto.RasterCapabilitiesInfo;
import org.geomajas.plugin.deskmanager.domain.dto.DynamicLayerConfiguration;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 */
public class EditLayerSettingsStep extends WizardStepPanel {
	
	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	/**
	 * Id used in creating Wms Layer bean server side.
	 *
	 * @see org.geomajas.plugin.runtimeconfig.service.factory.WmsLayerBeanFactory#ENABLE_FEATURE_INFO
	 */
	public static final String WMS_LAYER_BEAN_FACTORY_ENABLE_FEATURE_INFO = "enableFeatureInfoSupport";

	/**
	 * Id used in creating Wms Layer bean server side.
	 *
	 * @see org.geomajas.plugin.runtimeconfig.service.factory.WmsLayerBeanFactory#FEATURE_INFO_FORMAT
	 */
	public static final String WMS_LAYER_BEAN_FACTORY_FEATURE_INFO_FORMAT = "featureInfoFormat";

	private DynamicForm featureInfoForm;

	private LayerSettingsForm form;
	private DynamicForm featureForm;
	private MaxBoundsForm maxBoundsForm;

	private boolean first = true;

	private String prevStepName;

	private DynamicLayerConfiguration layerConfig;

	private RasterCapabilitiesInfo info;

	private CheckboxItem enableFeatureInfoItem;

	private SelectItem featureInfoFormatItem;

	public EditLayerSettingsStep(Wizard parent) {
		super(NewLayerModelWizardWindow.STEP_EDIT_LAYER_SETTINGS, 
				MESSAGES.editLayerSettingsStepNumbering() + " " + MESSAGES.editLayerSettingsStepTitle(),
				true, parent);
		setWindowTitle(MESSAGES.editLayerSettingsStepTitle());

		// -- layersettings --
		form = new LayerSettingsForm();
		form.addItemChangedHandler(new ItemChangedHandler() {
			public void onItemChanged(ItemChangedEvent event) {
				fireChangedEvent();
			}
		});
		addMember(form);

		featureForm = createFeatureInfoForm();
		addMember(featureForm);

		// -- maxbounds --
		maxBoundsForm = new MaxBoundsForm();
		maxBoundsForm.addItemChangedHandler(new ItemChangedHandler() {
			public void onItemChanged(ItemChangedEvent event) {
				fireChangedEvent();
			}
		});
		
		// -------------------------------------------------

//		HLayout formLayout = new HLayout();
//		LayoutSpacer ls = new LayoutSpacer();
//		ls.setWidth(100);
//		formLayout.addMember(ls);
//		formLayout.addMember(maxBoundsForm);
//		formLayout.addMember(new LayoutSpacer());
		
		VLayout root = new VLayout();
		root.addMember(form);
		Label l = new Label("<b>" + MESSAGES.editLayerSettingsStepVisibleArea() + " : </b>");
		l.setPadding(10);
		l.setWidth100();
		l.setAutoHeight();
		root.addMember(l);
		root.addMember(maxBoundsForm);
		root.addMember(new LayoutSpacer());
		addMember(root);
	}

	private DynamicForm createFeatureInfoForm() {
		featureInfoForm = new DynamicForm();
		featureInfoForm.setColWidths("125", "*");

		enableFeatureInfoItem = new CheckboxItem();
		enableFeatureInfoItem.setTitle(MESSAGES.layerSettingsEnableFeatureInfo());
		enableFeatureInfoItem.setTooltip(MESSAGES.layerSettingsEnableFeatureInfoTooltip());
		enableFeatureInfoItem.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent changedEvent) {
				featureInfoFormatItem.setRequired(enableFeatureInfoItem.getValueAsBoolean());
			}
		});

		featureInfoFormatItem = new SelectItem();
		featureInfoFormatItem.setTitle(MESSAGES.layerSettingsFeatureInfoFormat());
		featureInfoFormatItem.setTooltip(MESSAGES.layerSettingsFeatureInfoFormatTooltip());

		featureInfoForm.setFields(enableFeatureInfoItem, featureInfoFormatItem);
		featureInfoForm.addItemChangedHandler(new ItemChangedHandler() {
			@Override
			public void onItemChanged(ItemChangedEvent itemChangedEvent) {
				fireChangedEvent();
			}
		});

		return featureInfoForm;

	}

	/**
	 * previous step can be vector or raster.
	 *
	 * @param layerConfig
	 * @param info
	 * @param previousStepName
	 */
	public void setData(DynamicLayerConfiguration layerConfig, RasterCapabilitiesInfo info, String previousStepName) {
		this.prevStepName = previousStepName;
		this.layerConfig = layerConfig;
		this.info = info;
//		ShapefileUploadStep sfup =
//			(ShapefileUploadStep) parent.getStep(NewLayerModelWizardWindow.STEP_SHAPEFILE_UPLOAD);
//		if (sfup != null && sfup.getFileName() != null) {
//			layerConfig.getClientLayerInfo().setLabel(sfup.getFileName());
//		}

		if (info != null) {
			layerConfig.getClientLayerInfo().setLabel(info.getName());
		}

		if (layerConfig instanceof DynamicRasterLayerConfiguration) {
			featureInfoFormatItem.setValueMap(info.getGetFeatureInfoFormats().toArray(new String[0]));
		} else {
			featureInfoForm.hide();
		}

		isValid();


	}

	public DynamicLayerConfiguration getData() {
		if (isValid()) {
			form.getData();
			maxBoundsForm.getData();

			if (layerConfig instanceof DynamicRasterLayerConfiguration) {
				Boolean enableFeatureInfoSupport = enableFeatureInfoItem.getValueAsBoolean();
				layerConfig.getParameters().add(new Parameter(WMS_LAYER_BEAN_FACTORY_ENABLE_FEATURE_INFO,
						enableFeatureInfoSupport.toString()));
				if (enableFeatureInfoSupport) {
					layerConfig.getParameters().add(new Parameter(WMS_LAYER_BEAN_FACTORY_FEATURE_INFO_FORMAT,
							featureInfoFormatItem.getValueAsString()));
				}
			}

			return this.layerConfig;
		} else {
			return null;
		}
	}

	@Override
	public void initialize() {
		form.setData(layerConfig);
		maxBoundsForm.setData(layerConfig);
	}

	@Override
	public boolean isValid() {
		// don't check first time, otherwise errors are immediately shown
		if (first) {
			first = !first;
			return false;
		} else {
			return form.validate() && featureForm.validate() && maxBoundsForm.validate();
		}
	}

	@Override
	public String getNextStep() {
		return null;
	}

	@Override
	public String getPreviousStep() {
		return prevStepName;
	}

	@Override
	public void reset() {
		form.reset();
	}

	@Override
	public boolean stepFinished() {
		return true;
	}
}
