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
package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.panels;

import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.configuration.client.DeskmanagerClientLayerInfo;
import org.geomajas.plugin.deskmanager.domain.dto.DynamicLayerConfiguration;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

/**
 * @author Kristof Heirwegh
 */
public class LayerSettingsForm extends DynamicForm {
	
	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	private CheckboxItem publicLayer;

	private CheckboxItem active;

	private CheckboxItem visible;

	private TextItem name;

	private DeskmanagerClientLayerInfo extraLayerInfo;

	private DynamicLayerConfiguration layerConfig;

	private LayerModelDto layerModel;

	public LayerSettingsForm() {

// 		setWidth100();
		setColWidths("125", "*");

		name = new TextItem();
		name.setTitle(MESSAGES.layerSettingsLayerName());
		name.setWidth(250);
		name.setRequired(true);

		publicLayer = new CheckboxItem();
		publicLayer.setTitle(MESSAGES.layerSettingsPublic());
		publicLayer.setTooltip(MESSAGES.layerSettingsPublicTooltip());

		active = new CheckboxItem();
		active.setTitle(MESSAGES.layerSettingsActive());
		active.setTooltip(MESSAGES.layerSettingsActiveTooltip());

		visible = new CheckboxItem();
		visible.setTitle(MESSAGES.layerSettingsVisibleByDefault());
		visible.setTooltip(MESSAGES.layerSettingsVisibleByDefaultTooltip());

		// -------------------------------------------------

		setFields(name, publicLayer, active, visible);
	}

	public void setData(DynamicLayerConfiguration layerConfig) {
		this.layerConfig = layerConfig;
		this.extraLayerInfo = (DeskmanagerClientLayerInfo) layerConfig.getClientLayerInfo().getUserData();
		name.setValue(layerConfig.getClientLayerInfo().getLabel());
		publicLayer.setValue(extraLayerInfo.isPublic());
		active.setValue(extraLayerInfo.isActive());
		visible.setValue(layerConfig.getClientLayerInfo().isVisible());
	}

	public DynamicLayerConfiguration getData() {
		layerConfig.getClientLayerInfo().setLabel(name.getValueAsString());
		layerConfig.getClientLayerInfo().setVisible(visible.getValueAsBoolean());
		extraLayerInfo.setPublic(publicLayer.getValueAsBoolean());
		extraLayerInfo.setActive(active.getValueAsBoolean());
		return layerConfig;
	}

	public void setLayerModel(LayerModelDto model) {
		this.layerModel = model;
		name.setValue(model.getName());
		publicLayer.setValue(model.isPublic());
		active.setValue(model.isActive());
		visible.setValue(model.isDefaultVisible());
	}

	public LayerModelDto getLayerModel() {
		layerModel.setActive(active.getValueAsBoolean());
		layerModel.setDefaultVisible(visible.getValueAsBoolean());
		layerModel.setName(name.getValueAsString());
		layerModel.setPublic(publicLayer.getValueAsBoolean());
		return layerModel;
	}
}
