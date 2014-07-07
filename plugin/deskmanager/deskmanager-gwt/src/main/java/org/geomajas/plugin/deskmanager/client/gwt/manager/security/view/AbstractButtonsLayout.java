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
package org.geomajas.plugin.deskmanager.client.gwt.manager.security.view;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.WoaEventHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for a panel with three buttons: edit, cancel, save.
 *
 * @author Jan Venstermans
 */
public abstract class AbstractButtonsLayout extends VLayout implements EditableView, WoaEventHandler {

	public static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	protected SaveButtonBar saveButtonBar;

	private List<WoaChangedHandler> changedHandlers = new ArrayList<WoaChangedHandler>();

	/* base container */
	protected VLayout containerLayout;

	public AbstractButtonsLayout() {
		super(10);
		createButtonLayout();

		containerLayout = new VLayout();
		containerLayout.setMargin(10);
		containerLayout.setMembersMargin(10);

		fillContainerLayout();

		addMember(containerLayout);
		setDisabled(true);
	}

	@Override
	public void setEnabled(boolean enabled) {
		setDisabled(!enabled);
	}

	@Override
	public boolean onResetClick(ClickEvent event) {
		return false;
	}

	@Override
	public boolean isDefault() {
		return false;
	}

	@Override
	public void registerChangedHandler(WoaChangedHandler handler) {
		changedHandlers.add(handler);
	}

	//---------------------------------------------------------------------
	// abstract/extensible methods
	//---------------------------------------------------------------------

	/**
	 * Extend this method to fill the containerLayout.
	 */
	protected void fillContainerLayout() {
		containerLayout.addMember(saveButtonBar);
	}

	protected void fireChangedHandler() {
		for (WoaEventHandler.WoaChangedHandler handler : changedHandlers) {
			handler.onChange();
		}
	}

	//---------------------------------------------------------------------
	// private methods
	//---------------------------------------------------------------------

	private void createButtonLayout() {
		saveButtonBar = new SaveButtonBar(this);
		saveButtonBar.setHideResetButton(true);
	}
}
