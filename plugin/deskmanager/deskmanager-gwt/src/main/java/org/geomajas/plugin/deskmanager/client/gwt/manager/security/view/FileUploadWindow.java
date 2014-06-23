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

import com.google.gwt.core.client.Callback;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.global.ExceptionDto;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.panels.GenericUploadForm;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;

/**
 * Window for uploading a file to the server.
 *
 * @author Jan Venstermans
 */
public class FileUploadWindow {

	private Window window;

	private IButton button;

	private GenericUploadForm fileUploadForm;

	private Callback<String, ExceptionDto> callback;

	public FileUploadWindow() {
		createWindow();
		addHandlers();
	}

	/**
	 * Shows window that ask to upload a file. The callback will be called with the token of the file.
	 *
	 * @param callback returns token of file when the file has been uploaded.
	 */
	public void showWindowAndUploadFile(final Callback<String, ExceptionDto> callback) {
		this.callback = callback;
		window.show();
	}

	public void setWindowTitle(String title) {
		window.setTitle(title);
	}

	public void setButtonText(String text) {
		button.setTitle(text);
	}

	//------------------------------------------------
	// private methods
	//------------------------------------------------

	private void createWindow() {
		window = new Window();
		window.setWidth(500);
		window.setHeight(300);
		window.setShowMinimizeButton(false);
		window.setIsModal(true);
		window.setShowModalMask(true);
		window.centerInPage();
		window.setShowCloseButton(true);
		window.setZIndex(2000);

		VLayout layout = new VLayout();
		layout.setLayoutMargin(25);
		layout.setMembersMargin(10);

		fileUploadForm = new GenericUploadForm();
		button = new IButton();
		layout.setLayoutMargin(25);

		layout.addMember(fileUploadForm);
		layout.addMember(button);

		window.addItem(layout);
	}

	private void addHandlers() {
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				if (fileUploadForm.validate()) {
					fileUploadForm.upload(new DataCallback<String>() {
						@Override
						public void execute(String result) {
							window.hide();
							if (callback != null) {
								if (result != null) {
									callback.onSuccess(result);
								} else {
									callback.onFailure(new ExceptionDto());
								}
							}
							callback = null;
						}
					});
				}
			}
		});
	}



	public GenericUploadForm getFileUploadForm() {
		return fileUploadForm;
	}
}
