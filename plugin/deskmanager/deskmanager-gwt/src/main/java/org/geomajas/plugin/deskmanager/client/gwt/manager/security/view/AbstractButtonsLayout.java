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
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;

/**
 * Abstract class for a panel with three buttons: edit, cancel, save.
 *
 * @author Jan Venstermans
 */
public abstract class AbstractButtonsLayout extends VLayout implements EditableView {

	public static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	/* buttons */

	private IButton editButton;

	private IButton saveButton;

	private IButton cancelButton;

	protected HLayout buttonLayout;

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
		bind();
	}

	@Override
	public void setButtonEnabled(Button button, boolean enabled) {
		switch(button) {
			case CANCEL:
				cancelButton.setDisabled(!enabled);
				break;
			case EDIT:
				editButton.setDisabled(!enabled);
				break;
			case SAVE:
				saveButton.setDisabled(!enabled);
				break;
			default:
				break;
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		setDisabled(!enabled);
	}

	//---------------------------------------------------------------------
	// abstract/extensible methods
	//---------------------------------------------------------------------

	/**
	 * Extend this method to fill the containerLayout.
	 */
	protected void fillContainerLayout() {
		containerLayout.addMember(buttonLayout);
	}

	public abstract void onEdit();
	public abstract void onCancel();
	public abstract void onSave();

	//---------------------------------------------------------------------
	// private methods
	//---------------------------------------------------------------------

	private void bind() {
		editButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onEdit();
			}
		});

		saveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onSave();
			}
		});

		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onCancel();
			}
		});
	}

	private void createButtonLayout() {
		editButton = new IButton(MESSAGES.editButtonText());
		saveButton = new IButton(MESSAGES.saveButtonText());
		cancelButton = new IButton(MESSAGES.cancelButtonText());

		buttonLayout = new HLayout(10);
		buttonLayout.addMember(editButton);
		buttonLayout.addMember(cancelButton);
		buttonLayout.addMember(saveButton);
		buttonLayout.setAutoHeight();
	}
}
