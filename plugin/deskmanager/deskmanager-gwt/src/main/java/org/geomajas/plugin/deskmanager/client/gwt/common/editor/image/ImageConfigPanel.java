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
package org.geomajas.plugin.deskmanager.client.gwt.common.editor.image;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.plugin.deskmanager.client.gwt.common.FileUploadForm;
import org.geomajas.plugin.deskmanager.client.gwt.common.i18n.CommonMessages;

/**
 * Configuration panel for {@link ImageInfo}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ImageConfigPanel extends HLayout {

	private static final CommonMessages MESSAGES = GWT.create(CommonMessages.class);

	private ImageInfo imageInfo;

	private DynamicForm form;

	private HiddenItem imageUrl;

	private TextItem imageAlt;

	private TextItem imageHref;

	private FileUploadForm imageFileForm;

	private VLayout formLayout;

	public void setDisabled(boolean disabled) {
		super.setDisabled(disabled);
		imageFileForm.setDisabled(disabled);
	}

	public ImageConfigPanel() {
		setMembersMargin(5);

		// --- properties ---
		form = new DynamicForm();
		form.setWidth100();
		form.setHeight100();
		form.setAutoFocus(true); /* Set focus on first field */
		form.setNumCols(2);
		form.setColWidths(150, "*");

		RegExpValidator regExpValidator = new RegExpValidator();
		regExpValidator.setExpression("^(http|https)\\://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}(:[a-zA-Z0-9]*)"
				+ "?/?([a-zA-Z0-9\\-\\._\\?\\,\\'/\\\\\\+&amp;%\\$#\\=~])*[^\\.\\,\\)\\(\\s]$");

		imageUrl = new HiddenItem();

		imageHref = new TextItem();
		imageHref.setTitle(MESSAGES.imageConfigLinkTitle());
		imageHref.setValidators(regExpValidator);
		imageHref.setValidateOnChange(true);
		imageHref.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				if (imageInfo != null && imageHref.validate()) {
					imageInfo.setHref((String) imageHref.getValue());
				}
			}
		});
		imageAlt = new TextItem();
		imageAlt.setTitle(MESSAGES.imageConfigAltTitle());
		imageAlt.setPrompt(MESSAGES.imageConfigAltTooltip());
		imageAlt.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				if (imageInfo != null) {
					imageInfo.setAlt((String) imageAlt.getValue());
				}
			}
		});

		form.setTitleOrientation(TitleOrientation.LEFT);
		form.setFields(imageUrl, imageHref, imageAlt);
		form.setAutoHeight();

		imageFileForm = new FileUploadForm(MESSAGES.imageConfigFileTitle(), "");
		imageFileForm.setDisabled(true);
		imageFileForm.addChangedHandler(new FileUploadForm.ChangedHandler() {

			public void onChange(FileUploadForm.ChangedEvent event) {
				imageUrl.setValue(event.getNewValue());
				if (imageInfo != null && imageUrl.getValue() != null && !"".equals(imageUrl.getValue())) {
					imageInfo.setUrl(event.getNewValue());
				}
			}
		});

		// --- form ---
		formLayout = new VLayout(3);
		formLayout.addMember(form);
		formLayout.addMember(imageFileForm);
		addMember(formLayout);
	}

	public void setLogoInfo(ImageInfo imageInfo) {
		this.imageInfo = imageInfo;
		imageUrl.setValue(imageInfo.getUrl());
		imageFileForm.setUrl(imageInfo.getUrl());
		imageAlt.setValue(imageInfo.getAlt());
		imageHref.setValue(imageInfo.getHref());
		imageFileForm.setToolTip(MESSAGES.imageConfigIdealSize(imageInfo.getWidth(), imageInfo.getHeight()));
		imageFileForm.setParameter("targetWidth", imageInfo.getWidth() + "");
		imageFileForm.setParameter("targetHeight", imageInfo.getHeight() + "");
	}

	public void setFormGroupTitle(String formGroupTitle) {
		formLayout.setGroupTitle(formGroupTitle);
	}

	public void setAltTitle(String altTitle) {
		imageAlt.setTitle(altTitle);
	}

	public void setFileUploadLabel(String label) {
		imageFileForm.setLabel(label);
	}

	public ImageInfo getLogoInfo() {
		return imageInfo;
	}
}
