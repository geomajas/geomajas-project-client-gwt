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
package org.geomajas.plugin.deskmanager.client.gwt.common.editor.loadingscreen;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.plugin.deskmanager.client.gwt.common.FileUploadForm;
import org.geomajas.plugin.deskmanager.client.gwt.common.i18n.CommonMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.editor.WidgetEditor;

/**
 * Editor for {@link org.geomajas.plugin.deskmanager.client.gwt.common.editor.loadingscreen.LoadingScreenImageInfo}.
 *
 * @author Dosi Bingov
 *
 */
public class LoadingScreenImageEditor implements WidgetEditor {
	private static final CommonMessages MESSAGES = GWT.create(CommonMessages.class);

	private HLayout hLayout;


	private FileUploadForm imageUploadForm;

	private LoadingScreenImageInfo layoutInfo;

	public LoadingScreenImageEditor() {
		hLayout = new HLayout();
		hLayout.setMargin(15);

		imageUploadForm = new FileUploadForm(MESSAGES.imageConfigFileTitle() + " : ", "");
		//request parameters needed for file upload mvc
		imageUploadForm.setParameter("targetWidth", "");
		imageUploadForm.setParameter("targetHeight", "");

		hLayout.addMember(imageUploadForm);
	}

	@Override
	public Canvas getCanvas() {
		return hLayout;
	}

	@Override
	public ClientWidgetInfo getWidgetConfiguration() {
		layoutInfo.setImageUrl(imageUploadForm.getUrl());

		return layoutInfo;
	}

	@Override
	public void setWidgetConfiguration(ClientWidgetInfo configuration) {
		if (configuration == null) {
			configuration = new LoadingScreenImageInfo();
		}

		this.layoutInfo = (LoadingScreenImageInfo) configuration;

		imageUploadForm.setUrl(layoutInfo.getImageUrl());
	}

	@Override
	public void setDisabled(boolean disabled) {
		imageUploadForm.setDisabled(disabled);
	}

}
