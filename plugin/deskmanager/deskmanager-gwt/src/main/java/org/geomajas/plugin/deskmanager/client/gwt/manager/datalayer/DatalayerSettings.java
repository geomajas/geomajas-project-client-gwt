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
package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer;

import org.geomajas.plugin.deskmanager.client.gwt.manager.common.AbstractConfigurationLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.panels.LayerSettingsForm;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.ManagerCommandService;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveLayerModelRequest;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 */
public class DatalayerSettings extends AbstractConfigurationLayout {
	
	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	private LayerModelDto lmd;

	private LayerSettingsForm form;

	public DatalayerSettings() {
		super();
		setMargin(5);
		setWidth100();

		SaveButtonBar buttonBar = new SaveButtonBar(this);
		addMember(buttonBar);

		// ----------------------------------------------------------

		form = new LayerSettingsForm();
		form.setWidth100();
		form.setAutoFocus(true); /* Set focus on first field */
		form.setDisabled(true);

		VLayout group = new VLayout();
		group.setPadding(10);
		group.addMember(form);
		group.setOverflow(Overflow.AUTO);

		addMember(group);
	}

	public void setLayerModel(LayerModelDto lmd) {
		form.clearValues();
		this.lmd = lmd;

		if (lmd != null) {
			if (lmd.getLayerConfiguration().getClientLayerInfo() == null) {
				form.setLayerModel(lmd);
			} else {
				form.setData(lmd.getLayerConfiguration());
			}
		}
		fireChangedHandler();
	}

	// -- SaveButtonBar events --------------------------------------------------------

	public boolean onEditClick(ClickEvent event) {
		form.setDisabled(false);
		return true;
	}

	public boolean onSaveClick(ClickEvent event) {
		if (validate()) {
			// update changes
			if (lmd.getLayerConfiguration().getClientLayerInfo() != null) {
				form.getData();
			} else {
				form.getLayerModel();
			}

			// persist
			ManagerCommandService.saveLayerModel(lmd, SaveLayerModelRequest.SAVE_SETTINGS);
			form.setDisabled(true);
			return true;
		} else {
			return false;
		}
	}

	public boolean onCancelClick(ClickEvent event) {
		setLayerModel(lmd);
		form.setDisabled(true);
		return true;
	}

	public boolean validate() {
		if (!form.validate()) {
			SC.say(MESSAGES.formWarnNotvalid());
			return false;
		}
		return true;
	}
	

	public boolean onResetClick(ClickEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isDefault() {
		// TODO Auto-generated method stub
		return true;
	}

}
